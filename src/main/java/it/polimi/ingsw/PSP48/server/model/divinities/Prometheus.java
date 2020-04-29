package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Prometheus extends Divinity {
    private final String name = "Prometheus";

    public static Boolean supportedDivinity(int pNum) {
        switch (pNum) {
            case 2:
                return true;
            case 3:
                return true;
        }
        return false;
    }

    boolean previousBuild = false;
    boolean doneMove = false;


    /**
     * don't do anything since without a divinity there isn't a modifier
     *
     * @param gd the model
     */
    @Override
    public Consumer<GameController> turnBegin(Model gd) {
        previousBuild = false;
        doneMove = false;
        //for building and doming, i must check if the first optional building allows to complete the turn, so that the player can move
        ArrayList<Position> workers = gd.getPlayerPositionsInMap(gd.getCurrentPlayer().getName());

        boolean build = false;
        boolean dome = false;

        ArrayList<Divinity> otherDivinities = new ArrayList<>();
        for (Player p : gd.getPlayersInGame()) {
            if (!p.getName().equals(gd.getCurrentPlayer().getName())) otherDivinities.add(p.getDivinity());
        }

        //for each worker, checks if he can end the turn
        ArrayList<WorkerValidCells> buildCells = new ArrayList<>();
        ArrayList<WorkerValidCells> domeCells = new ArrayList<>();

        for (Position w : workers) {
            buildCells.add(new WorkerValidCells(getValidCellForBuilding(w.getColumn(), w.getRow(), otherDivinities, gd.getClonedGameBoard()), w.getRow(), w.getColumn()));
            domeCells.add(new WorkerValidCells(getValidCellsToPutDome(w.getColumn(), w.getRow(), gd.getClonedGameBoard(), otherDivinities), w.getRow(), w.getColumn()));
        }

        //now i have for each workers cells where he could build or put dome; now i must check if building or doming before the move allows the player to complete the turn
        for (WorkerValidCells c : buildCells) {
            for (Position p : c.getValidPositions()) {
                if (simulateBuildingCheckIfCanMoveAfterWards(c.getwR(), c.getwC(), p.getRow(), p.getColumn(), gd.getClonedGameBoard(), false, otherDivinities)) {
                    build = true;
                    break;
                }
            }
            if (build) break;
        }

        if (!build) for (WorkerValidCells c : buildCells) {
            for (Position p : c.getValidPositions()) {
                if (simulateBuildingCheckIfCanMoveAfterWards(c.getwR(), c.getwC(), p.getRow(), p.getColumn(), gd.getClonedGameBoard(), true, otherDivinities)) {
                    dome = true;
                    break;
                }
            }
            if (dome) break;
        }

        if (!build && !dome) return GameController::requestOptionalBuilding;
        else return GameController::CheckIfCanEndTurnBaseDivinity;
    }

    /**
     * @param WorkerColumn          the column where the worker is
     * @param WorkerRow             the row where the worker is
     * @param gameCells             the actual board state
     * @param otherDivinitiesInGame the other divinities in game
     * @return a list of cells valid for the move of the worker
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Position> getValidCellForMove(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> otherDivinitiesInGame) {
        return super.getValidCellForMove(WorkerColumn, WorkerRow, gameCells, otherDivinitiesInGame).stream()
                .filter(cell -> !previousBuild || !(gameCells[cell.getRow()][cell.getColumn()].getLevel() > gameCells[WorkerRow][WorkerColumn].getLevel()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * @param WorkerColumn the column of the cell where the worker is
     * @param WorkerRow    the row of the cell where the worker is
     * @param moveColumn   the column of the board where the worker wants to move
     * @param moveRow      the row of the board where the worker wants to move
     * @param gd           the actual game state
     * @throws NotAdjacentCellException if the destination cell is not adiacent to the worker
     * @throws IncorrectLevelException  if the destination cell is too high to be reached
     * @throws OccupiedCellException    if the destination cell has another worker on it
     * @throws DomedCellException       if the destination cell has a dome on it
     * @author Daniele Mammone
     */
    @Override
    public Consumer<GameController> move(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, Model gd) throws NotAdjacentCellException, IncorrectLevelException, OccupiedCellException, DomedCellException, DivinityPowerException, NoTurnEndException {
        //i must check if i'm not growing up level if i built before
        if (previousBuild && gd.getCell(moveRow, moveColumn).getLevel() > gd.getCell(WorkerRow, WorkerColumn).getLevel())
            throw new DivinityPowerException("trying to level up after previous building");
        super.move(WorkerColumn, WorkerRow, moveColumn, moveRow, gd);
        doneMove = true;
        return GameController::requestBuildDome;
    }

    /**
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param buildRow     the row where the player wants to add a level
     * @param buildColumn  the column where the player wants to add a level
     * @param gd           the actual game board state
     * @return the next action of the controller
     * @throws NotAdjacentCellException     if the cell where the player wants to build is not adiacent to the worker's one
     * @throws OccupiedCellException        if the destination cell is occupied by another worker
     * @throws DomedCellException           is the cell is already domed
     * @throws MaximumLevelReachedException if the cell contains a level 3 building
     * @throws DivinityPowerException       if another divinity blocks the increment of the level
     * @author Daniele Mammone
     */
    @Override
    public Consumer<GameController> build(int workerRow, int workerColumn, int buildRow, int buildColumn, Model gd) throws NotAdjacentCellException, OccupiedCellException, DomedCellException, MaximumLevelReachedException, DivinityPowerException {
        Consumer<GameController> nextAction;
        if (buildRow == -1 && buildColumn == -1) return GameController::requestMove;
        super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
        if (!previousBuild && !doneMove) nextAction = GameController::requestOptionalBuilding;
        else nextAction = GameController::turnEnd;
        previousBuild = true;
        return nextAction;
    }

    /**
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param domeRow      the row where the player wants to add the dome
     * @param domeColumn   the column where the player wants to add the dome
     * @param gd           the current game board state
     * @return the next action of the controller
     * @throws NotAdjacentCellException        if the cell where the player wants to add the dome is not adiacent to the worker's one
     * @throws OccupiedCellException           if the destination cell is occupied by another worker
     * @throws DomedCellException              is the cell is already domed
     * @throws MaximumLevelNotReachedException if the cell doesn't contain a level 3 building
     * @throws DivinityPowerException          if another divinity blocks the adding of the dome
     * @author Daniele Mammone
     */
    @Override
    public Consumer<GameController> dome(int workerRow, int workerColumn, int domeRow, int domeColumn, Model gd) throws NotAdjacentCellException, OccupiedCellException, DomedCellException, MaximumLevelNotReachedException, DivinityPowerException {
        Consumer<GameController> nextAction;
        if (domeRow == -1 && domeColumn == -1) return GameController::requestMove;
        super.dome(workerRow, workerColumn, domeRow, domeColumn, gd);
        if (!previousBuild && !doneMove) nextAction = GameController::requestOptionalBuilding;
        else nextAction = GameController::turnEnd;
        previousBuild = true;
        return nextAction;
    }

    @Override
    public String getName() {
        return name;
    }

    private boolean simulateBuildingCheckIfCanMoveAfterWards(int wR, int wC, int mR, int mC, Cell[][] gameBoard, boolean dome, ArrayList<Divinity> otherDiv) {
        //arrived here, the cell is valid, i simulate the move
        if (!dome) try {
            gameBoard[mR][mC].addLevel();
        } catch (Exception e) {
            System.out.println("Fatal error");
        }

        else try {
            gameBoard[mR][mC].addDome();
        } catch (Exception e) {
            System.out.println("Fatal error");
        }
        return !getValidCellForMove(wC, wC, gameBoard, otherDiv).isEmpty();
    }


}
