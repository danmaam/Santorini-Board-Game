package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.server.controller.ControllerState.*;
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
     * Calculates if Prometheus can do a first optional build allowing him to complete the turn. If not, checks if he can normally end the turn.
     *
     * @param gd the model
     * @return
     */
    @Override
    public GameControllerState turnBegin(Model gd) {
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
            buildCells.add(new WorkerValidCells(getValidCellForBuilding(w.getRow(), w.getColumn(), otherDivinities, gd.getGameBoard()), w.getRow(), w.getColumn()));
            domeCells.add(new WorkerValidCells(getValidCellsToPutDome(w.getRow(), w.getColumn(), gd.getGameBoard(), otherDivinities), w.getRow(), w.getColumn()));
        }

        //now i have for each workers cells where he could build or put dome; now i must check if building or doming before the move allows the player to complete the turn
        for (WorkerValidCells c : buildCells) {
            for (Position p : c.getValidPositions()) {
                if (simulateBuildingCheckIfCanMoveAfterWards(c.getwR(), c.getwC(), p.getRow(), p.getColumn(), gd.getGameBoard(), false, otherDivinities)) {
                    build = true;
                    break;
                }
            }
            if (build) break;
        }

        if (!build) for (WorkerValidCells c : domeCells) {
            for (Position p : c.getValidPositions()) {
                if (simulateBuildingCheckIfCanMoveAfterWards(c.getwR(), c.getwC(), p.getRow(), p.getColumn(), gd.getGameBoard(), true, otherDivinities)) {
                    dome = true;
                    break;
                }
            }
            if (dome) break;
        }

        if (build || dome) return new PrometheusInitialOptionalBuild();
        else return super.turnBegin(gd);
    }

    /**
     * @param workerRow             the row where the worker is
     * @param workerColumn          the column where the worker is
     * @param gameCells             the actual board state
     * @param otherDivinitiesInGame the other divinities in game
     * @return a list of cells valid for the move of the worker
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Position> getValidCellForMove(int workerRow, int workerColumn, Cell[][] gameCells, ArrayList<Divinity> otherDivinitiesInGame) {
        return super.getValidCellForMove(workerRow, workerColumn, gameCells, otherDivinitiesInGame).stream()
                .filter(cell -> !previousBuild || !(gameCells[cell.getRow()][cell.getColumn()].getLevel() > gameCells[workerRow][workerColumn].getLevel()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Calls the super move method, and updates Prometheus game status, setting that the move has done.
     *
     * @param workerRow    the row of the cell where the worker is
     * @param workerColumn the column of the cell where the worker is
     * @param moveRow      the row of the board where the worker wants to move
     * @param moveColumn   the column of the board where the worker wants to move
     * @param gd           the actual game state
     * @return the next controller FSM state
     * @throws NotAdjacentCellException if the destination cell is not adiacent to the worker
     * @throws IncorrectLevelException  if the destination cell is too high to be reached
     * @throws OccupiedCellException    if the destination cell has another worker on it
     * @throws DomedCellException       if the destination cell has a dome on it
     * @throws DivinityPowerException   if the move isn't allowed by another divinity
     * @throws NoTurnEndException       if the move doesn't allow the player to end the turn
     * @author Daniele Mammone
     */
    @Override
    public GameControllerState move(int workerRow, int workerColumn, int moveRow, int moveColumn, Model gd) throws NotAdjacentCellException, IncorrectLevelException, OccupiedCellException, DomedCellException, DivinityPowerException, NoTurnEndException {
        //i must check if i'm not growing up level if i built before
        if (previousBuild && gd.getCell(moveRow, moveColumn).getLevel() > gd.getCell(workerRow, workerColumn).getLevel())
            throw new DivinityPowerException("trying to level up after previous building");
        super.move(workerRow, workerColumn, moveRow, moveColumn, gd);
        doneMove = true;
        return new RequestBuildDome();
    }

    /**
     * Requests the model to register the build action by calling the super build method, and updates the status of the turn: if it's the optional build,
     * it's requested the move, while if it's the second is returned a turnEnd state
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param buildRow     the row where the player wants to add a level
     * @param buildColumn  the column where the player wants to add a level
     * @param gd           the actual game board state
     * @return the next action of the controller
     * @throws NotAdjacentCellException     if the cell where the player wants to build is not adjacent to the worker's one
     * @throws OccupiedCellException        if the destination cell is occupied by another worker
     * @throws DomedCellException           is the cell is already domed
     * @throws MaximumLevelReachedException if the cell contains a level 3 building
     * @throws DivinityPowerException       if another divinity blocks the increment of the level
     * @author Daniele Mammone
     */
    @Override
    public GameControllerState build(int workerRow, int workerColumn, int buildRow, int buildColumn, Model gd) throws NotAdjacentCellException, OccupiedCellException, DomedCellException, MaximumLevelReachedException, DivinityPowerException {
        GameControllerState nextAction;
        if (buildRow == -1 && buildColumn == -1) return super.turnBegin(gd);
        super.build(workerRow, workerColumn, buildRow, buildColumn, gd);
        if (!previousBuild && !doneMove) {
            gd.getCurrentPlayer().setLastWorkerUsed(workerRow, workerColumn);
            nextAction = new PrometheusMovePostOptionalBuild();
        } else nextAction = new TurnEnd();
        previousBuild = true;
        return nextAction;
    }

    /**
     *  Requests the model to register the dome action by calling the super build method, and updates the status of the turn: if it's the optional build,
     *  it's requested the move, while if it's the second is returned a turnEnd state
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param domeRow      the row where the player wants to add the dome
     * @param domeColumn   the column where the player wants to add the dome
     * @param gd           the current game board state
     * @return the next action of the controller
     * @throws NotAdjacentCellException        if the cell where the player wants to add the dome is not adjacent to the worker's one
     * @throws OccupiedCellException           if the destination cell is occupied by another worker
     * @throws DomedCellException              is the cell is already domed
     * @throws MaximumLevelNotReachedException if the cell doesn't contain a level 3 building
     * @throws DivinityPowerException          if another divinity blocks the adding of the dome
     * @author Daniele Mammone
     */
    @Override
    public GameControllerState dome(int workerRow, int workerColumn, int domeRow, int domeColumn, Model gd) throws NotAdjacentCellException, OccupiedCellException, DomedCellException, MaximumLevelNotReachedException, DivinityPowerException {
        GameControllerState nextAction;
        if (domeRow == -1 && domeColumn == -1) return super.turnBegin(gd);
        super.dome(workerRow, workerColumn, domeRow, domeColumn, gd);
        if (!previousBuild && !doneMove) {
            nextAction = new PrometheusMovePostOptionalBuild();
            gd.getCurrentPlayer().setLastWorkerUsed(workerRow, workerColumn);
        } else nextAction = new TurnEnd();
        previousBuild = true;
        return nextAction;
    }

    @Override
    public String getName() {
        return "Prometheus";
    }

    private boolean simulateBuildingCheckIfCanMoveAfterWards(int wR, int wC, int mR, int mC, Cell[][] gameBoard, boolean dome, ArrayList<Divinity> otherDiv) {
        //arrived here, the cell is valid, i simulate the move
        int oldLevel = gameBoard[mR][mC].getLevel();
        boolean isCellDomed = gameBoard[mR][mC].isDomed();
        String playerOnCell = gameBoard[mR][mC].getPlayer();

        if (!dome) try {
            gameBoard[mR][mC].addLevel();
            previousBuild = true;
        } catch (Exception e) {
            System.out.println("Can't add level");
        }

        else try {
            gameBoard[mR][mC].addDome();
            previousBuild = true;
        } catch (Exception e) {
            System.out.println("Fatal error");
        }

        boolean can = !getValidCellForMove(wR, wC, gameBoard, otherDiv).isEmpty();
        previousBuild = false;
        gameBoard[mR][mC] = new Cell(mR, mC, oldLevel, playerOnCell, isCellDomed);
        return can;
    }

    @Override
    public String getDescription() {
        return "If your Worker does not move up, it may build both before and after moving.";
    }

    /**
     * Generates a list of cell where a certain worker can build: if the method is called for the optional build, checks if building in
     * a certain cell, the player can complete the turn.
     *
     * @param workerRow             the row where the worker is
     * @param workerColumn          the column where the worker is
     * @param otherDivinitiesInGame the other divinities in game
     * @param gameCell              the game board
     * @return a list of valid cells where the player can build
     */
    @Override
    public ArrayList<Position> getValidCellForBuilding(int workerRow, int workerColumn, ArrayList<Divinity> otherDivinitiesInGame, Cell[][] gameCell) {

        return super.getValidCellForBuilding(workerRow, workerColumn, otherDivinitiesInGame, gameCell).stream().filter(x -> previousBuild || simulateBuildingCheckIfCanMoveAfterWards(workerRow, workerColumn, x.getRow(), x.getColumn(), gameCell, false, otherDivinitiesInGame)).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Generates a list of cell where a certain worker can build: if the method is called for the optional build, checks if building in
     * a certain cell, the player can complete the turn.
     *
     * @param workerRow        the row where the worker is
     * @param workerColumn     the column where the worker is
     * @param gameCells        the actual state of the board
     * @param divinitiesInGame the other divinities in game
     * @return a list of valid cells where the player can build
     */
    @Override
    public ArrayList<Position> getValidCellsToPutDome(int workerRow, int workerColumn, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {

        return super.getValidCellsToPutDome(workerRow, workerColumn, gameCells, divinitiesInGame).stream().filter(x -> previousBuild || simulateBuildingCheckIfCanMoveAfterWards(workerRow, workerColumn, x.getRow(), x.getColumn(), gameCells, true, divinitiesInGame)).collect(Collectors.toCollection(ArrayList::new));
    }
}
