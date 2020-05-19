package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import it.polimi.ingsw.PSP48.server.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Divinity {
    private final String name = "Basic";
    private final String Description = "questa divinità fa cose e robe";

    public static Boolean supportedDivinity(int pNum) {
        switch (pNum) {
            case 2:
                return true;
            case 3:
                return true;
        }
        return false;
    }

    //Methods

    /**
     * @return the name of the divinity
     */
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.Description;
    }


    //
    //
    //
    //METHODS FOR MOVING

    /**
     * @param WorkerColumn          the column where the worker is
     * @param WorkerRow             the row where the worker is
     * @param gameCells             the actual board state
     * @param otherDivinitiesInGame the other divinities in game
     * @return a list of cells valid for the move of the worker
     * @author Daniele Mammone
     */
    public ArrayList<Position> getValidCellForMove(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> otherDivinitiesInGame) {
        Cell actualWorkerCell = gameCells[WorkerRow][WorkerColumn];
        ArrayList<Cell> validCells = new ArrayList<>();

        //with the for loop, i'm adding to the arrayList the cell adiacent to the worker
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && 0 <= WorkerRow + i && WorkerRow + i <= 4 && 0 <= WorkerColumn + j && WorkerColumn + j <= 4) {
                    validCells.add(gameCells[WorkerRow + i][WorkerColumn + j]);
                }
            }
        }


        validCells = validCells.stream()
                .filter(cell -> cell.getPlayer() == null) // deletes from the valid cells ones where there's a worker on
                //deletes from the valid the cell which are too high or too low to be reached
                .filter(cell -> cell.getLevel() - actualWorkerCell.getLevel() <= 1)
                //deletes the domed cells
                .filter(cell -> !cell.isDomed())
                .collect(Collectors.toCollection(ArrayList::new));

        //now we have to remove cells where the move is impossible due to other divinity powers

        ArrayList<Cell> cellsToBeRemoved = new ArrayList<>();

        for (Cell c : validCells) {
            for (Divinity d : otherDivinitiesInGame) {
                if (!d.getName().equals(this.getName()) && !d.othersMove(new MovePosition(WorkerRow, WorkerColumn, c.getRow(), c.getColumn(), gameCells[c.getRow()][c.getColumn()].getLevel() - gameCells[WorkerRow][WorkerColumn].getLevel()))) {
                    cellsToBeRemoved.add(c);
                    break;
                }
            }
        }

        for (Cell c : cellsToBeRemoved) validCells.remove(c);

        //now in valid cells there is the list with compatible moves cells

        ArrayList<Position> validPositions = new ArrayList<>();
        for (Cell c : validCells) {
            validPositions.add(new Position(c.getRow(), c.getColumn()));
        }
        return validPositions;
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
    public Consumer<GameController> move(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, Model gd) throws
            NotAdjacentCellException, IncorrectLevelException, OccupiedCellException, DomedCellException, DivinityPowerException, NoTurnEndException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(WorkerRow, WorkerColumn, moveRow, moveColumn)))
            throw new NotAdjacentCellException("Celle non adiacenti");
        //second check: the two levels must be compatible
        int workerLevel = gd.getCell(WorkerRow, WorkerColumn).getLevel();
        int moveLevel = gd.getCell(moveRow, moveColumn).getLevel();
        if (!(moveLevel - workerLevel <= 1))
            throw new IncorrectLevelException("Stai cerando di salire a un livello troppo alto");
        //third check: the cell must not be occupied
        if ((gd.getCell(moveRow, moveColumn).getPlayer() != null)) throw new OccupiedCellException("Cella occupata");
        //fourth check: the cell must not be domed
        if (gd.getCell(moveRow, moveColumn).isDomed())
            throw new DomedCellException("Stai cercando di salire su una cella con cupola");
        //fifth check: if another different divinity doesn't invalid this move

        for (Player p : gd.getPlayersInGame()) {
            if (p != gd.getCurrentPlayer() && !p.getDivinity().othersMove(new MovePosition(WorkerRow, WorkerColumn, moveRow, moveColumn, moveLevel - workerLevel)))
                throw new DivinityPowerException("Fail due to other divinity");
        }


        gd.getCurrentPlayer().setOldLevel(workerLevel);
        gd.getCurrentPlayer().setNewLevel(moveLevel);
        gd.getCell(moveRow, moveColumn).setPlayer(gd.getCell(WorkerRow, WorkerColumn).getPlayer());
        gd.getCell(WorkerRow, WorkerColumn).setPlayer(null);
        gd.getCurrentPlayer().setLastWorkerUsed(moveRow, moveColumn);

        ArrayList<Cell> changedCell = new ArrayList<>();
        changedCell.add((Cell) gd.getCell(WorkerRow, WorkerColumn).clone());
        changedCell.add((Cell) gd.getCell(moveRow, moveColumn).clone());
        gd.notifyObservers(x -> x.changedBoard(changedCell));
        //now, the game board has been modified

        return GameController::requestBuildDome;
    }

    //
    //
    //
    // METHODS FOR BUILDING


    /**
     * @param WorkerColumn          the column where the worker is
     * @param WorkerRow             the row where the worker is
     * @param gameCell              the game board
     * @param otherDivinitiesInGame the other divinites in game
     * @return a list of cell valid for the building of the worker
     * @author Daniele Mammone
     */
    public ArrayList<Position> getValidCellForBuilding(int WorkerColumn, int WorkerRow, ArrayList<Divinity> otherDivinitiesInGame, Cell[][] gameCell) {
        //with the for loop, i'm adding to the arrayList the cell adiacent to the worker
        ArrayList<Cell> validBuild = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && 0 <= WorkerRow + i && WorkerRow + i <= 4 && 0 <= WorkerColumn + j && WorkerColumn + j <= 4) {
                    validBuild.add(gameCell[WorkerRow + i][WorkerColumn + j]);
                }
            }
        }

        //now we have to remove cells where the move is invalid

        validBuild = validBuild.stream()
                .filter(cell -> cell.getPlayer() == null)
                .filter(cell -> !cell.isDomed())
                .filter(cell -> cell.getLevel() != 3)
                .collect(Collectors.toCollection(ArrayList::new));

        //now we have to remove cells due to other divinities powers
        //we have to remove the current divinity from the others, to check if their power can invalid the move

        ArrayList<Cell> notValid = new ArrayList<>();

        for (Cell c : validBuild) {
            for (Divinity d : otherDivinitiesInGame) {
                if (!d.getName().equals(this.getName()) && !d.othersBuilding(new BuildPosition(WorkerRow, WorkerColumn, c.getRow(), c.getColumn(), c.getLevel()))) {
                    notValid.add(c);
                    break;
                }
            }
        }

        for (Cell c : notValid) {
            validBuild.remove(c);
        }

        ArrayList<Position> validPositions = new ArrayList<>();
        validBuild.forEach((Cell c) -> validPositions.add(new Position(c.getRow(), c.getColumn())));
        return validPositions;
    }


    /**
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param buildRow     the row where the player wants to add a level
     * @param buildColumn  the column where the player wants to add a level
     * @param gd           the actual game board state
     * @throws NotAdjacentCellException     if the cell where the player wants to build is not adiacent to the worker's one
     * @throws OccupiedCellException        if the destination cell is occupied by another worker
     * @throws DomedCellException           is the cell is already domed
     * @throws MaximumLevelReachedException if the cell contains a level 3 building
     * @throws DivinityPowerException       if another divinity blocks the increment of the level
     * @author Daniele Mammone
     */
    public Consumer<GameController> build(int workerRow, int workerColumn, int buildRow, int buildColumn, Model gd) throws
            NotAdjacentCellException, OccupiedCellException, DomedCellException, MaximumLevelReachedException, DivinityPowerException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(workerRow, workerColumn, buildRow, buildColumn)))
            throw new NotAdjacentCellException("Celle non adiacenti");
        //second check: the cell must be empty of workers
        if (!(gd.getCell(buildRow, buildColumn).getPlayer() == null)) throw new OccupiedCellException("Cella occupata");
        //third check: the cell must not be domed
        if (gd.getCell(buildRow, buildColumn).isDomed())
            throw new DomedCellException("Stai cercando di costruire su una cella con cupola");
        //fourth check: if we are not already at the maximum level
        if (3 == gd.getCell(buildRow, buildColumn).getLevel())
            throw new MaximumLevelReachedException("Trying to build on level 3");
        //fifth check: if another different divinity doesn't invalid this move

        for (Player p : gd.getPlayersInGame()) {
            if (p != gd.getCurrentPlayer() && !p.getDivinity().othersBuilding(new BuildPosition(workerRow, workerColumn, buildRow, buildColumn, gd.getCell(buildRow, buildColumn).getLevel())))
                throw new DivinityPowerException("Fail due to other divinity");
        }

        //at this point, the move is valid and we must change the state of the game board

        gd.getCell(buildRow, buildColumn).addLevel();

        //now, the game has been modified

        //notify the observers
        ArrayList<Cell> changedCell = new ArrayList<>();
        changedCell.add((Cell) gd.getCell(buildRow, buildColumn).clone());
        gd.notifyObservers(x -> x.changedBoard(changedCell));

        return GameController::turnEnd;
    }


    //
    //
    //
    // METHODS FOR DOME CONSTRUCTION

    /**
     * @param workerColumn the column where the worker is
     * @param workerRow    the row where the worker is
     * @param gameCells    the actual state of the board
     * @return true if it's possible to add the dome
     * @author Daniele Mammone
     */
    public ArrayList<Position> getValidCellsToPutDome(int workerColumn, int workerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        ArrayList<Cell> newCells = new ArrayList<>();
        //with the for loop, i'm adding to the arrayList the cell adiacent to the worker
        ArrayList<Cell> validCells = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && 0 <= workerRow + i && workerRow + i <= 4 && 0 <= workerColumn + j && workerColumn + j <= 4) {
                    validCells.add(gameCells[workerRow + i][workerColumn + j]);
                }
            }
        }

        validCells = validCells.stream()
                .filter(cell -> cell.getPlayer() == null)
                .filter(cell -> cell.getLevel() == 3)
                .filter(cell -> !cell.isDomed())
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Cell> notValid = new ArrayList<>();

        for (Cell c : validCells) {
            for (Divinity d : divinitiesInGame) {
                if (!d.getName().equals(this.getName()) && !d.othersDome(new DomePosition(workerRow, workerColumn, c.getRow(), c.getColumn(), c.getLevel()))) {
                    notValid.add(c);
                    break;
                }
            }
        }

        for (Cell c : notValid) validCells.remove(c);

        ArrayList<Position> validPositions = new ArrayList<>();
        validCells.forEach((Cell c) -> validPositions.add(new Position(c.getRow(), c.getColumn())));
        return validPositions;
    }

    /**
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param domeRow      the row where the player wants to add the dome
     * @param domeColumn   the column where the player wants to add the dome
     * @param gd           the current game board state
     * @throws NotAdjacentCellException        if the cell where the player wants to add the dome is not adiacent to the worker's one
     * @throws OccupiedCellException           if the destination cell is occupied by another worker
     * @throws DomedCellException              is the cell is already domed
     * @throws MaximumLevelNotReachedException if the cell doesn't contain a level 3 building
     * @throws DivinityPowerException          if another divinity blocks the adding of the dome
     * @author Daniele Mammone
     */
    public Consumer<GameController> dome(int workerRow, int workerColumn, int domeRow, int domeColumn, Model gd) throws
            NotAdjacentCellException, OccupiedCellException, DomedCellException, MaximumLevelNotReachedException, DivinityPowerException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(workerRow, workerColumn, domeRow, domeColumn)))
            throw new NotAdjacentCellException("Celle non adiacenti");
        //second check: the cell must be empty of workers
        if (!(gd.getCell(domeRow, domeColumn).getPlayer() == null)) throw new OccupiedCellException("Cella occupata");
        //third check: the cell must not be already domed
        if (gd.getCell(domeRow, domeColumn).isDomed())
            throw new DomedCellException("Stai cercando di costruire su una cella con cupola");
        //fourth check: if we are not already at the maximum level
        if (3 != gd.getCell(domeRow, domeColumn).getLevel())
            throw new MaximumLevelNotReachedException("Trying to add dome on a cell with a lower level than 3");
        //fifth check: if another different divinity doesn't invalid this move


        for (Player p : gd.getPlayersInGame()) {
            if (p != gd.getCurrentPlayer() && !p.getDivinity().othersDome(new DomePosition(workerRow, workerColumn, domeRow, domeColumn, gd.getCell(domeRow, domeColumn).getLevel())))
                throw new DivinityPowerException("Fail due to other divinity");
        }

        //at this point, the move is valid and we must change the state of the game board

        gd.getCell(domeRow, domeColumn).addDome();

        //i must notify the clients that the cell have been modified

        ArrayList<Cell> changedCell = new ArrayList<>();
        changedCell.add((Cell) gd.getCell(domeRow, domeColumn).clone());
        gd.notifyObservers(x -> x.changedBoard(changedCell));

        return GameController::turnEnd;

        //now, the game has been modified
    }


    //
    //
    //
    //
    //METHODS FOR POWER ON OTHER PLAYERS

    /**
     * @author Daniele Mammone
     * @param moveCells the cells where the player wants to move, where the player is and the difference between the two cells
     * @return true if the divinity doesn't affect the other player's move, false if the divinity blocks the move
     */
    public Boolean othersMove(MovePosition moveCells) {
        return true;
    }


    /**
     * @author Daniele Mammone
     * @param buildCells the cells where the player wants to build, where the player is and the old level of the cell
     * @return true if the divinity doesn't affect the other player's move, false if the divinity blocks the move
     */
    public Boolean othersBuilding(BuildPosition buildCells) {
        return true;
    }

    /**
     * @author Daniele Mammone
     * @param domeCells the cells where the player wants to add a dome, the cells where the player is and
     * @return true if the divinity doesn't affect the other player's move, false if the divinity blocks the move
     */
    public Boolean othersDome(DomePosition domeCells) {
        //since this is the base divinity's method, it always returns true
        return true;
    }


    /**
     * don't do anything since without a divinity there isn't a modifier
     */
    public Consumer<GameController> turnEnd() {
        return GameController::turnChange;
    }

    /**
     * don't do anything since without a divinity there isn't a modifier
     */

    public Consumer<GameController> turnBegin(Model gd) {
        ArrayList<Position> playerPositions, workerPositions;
        ArrayList<Divinity> otherDivinities = new ArrayList<>();
        ArrayList<Player> players;
        boolean canComplete = false;

        playerPositions = gd.getPlayerPositionsInMap(gd.getCurrentPlayer().getName()); //we need to get the positions of the current player on the map
        players = gd.getPlayersInGame();

        for (Player p : players) //we need to create the list of other divinities in the game, needed by the function that checks the valid cells for the move action
        {
            if (!p.getDivinity().getName().equals(this.getName())) {
                otherDivinities.add(p.getDivinity());
            }
        }

        for (Position p : playerPositions) {
            workerPositions = this.getValidCellForMove(p.getColumn(), p.getRow(), gd.getGameBoard(), otherDivinities);
            if (!workerPositions.isEmpty()) canComplete = true;
        }

        //if the player can move at least one of the two workers, the turn can be completed (the player can certainly build in the cell he moved from)
        if (canComplete == false) return (GameController::currentPlayerCantEndTurn);
        else return (GameController::requestMove);
    }

    /**
     * @param gd the state of the game
     * @return true if the actual player considererd has won, false if the game must go on
     */
    public boolean winCondition(Model gd) {
        return (gd.getCurrentPlayer().getOldLevel() != gd.getCurrentPlayer().getNewLevel() && gd.getCurrentPlayer().getNewLevel() == 3);
    }


    ///// AUXILIARY METHODS

    /**
     * @author Daniele Mammone
     * @param workerRow the row where the worker is
     * @param workerColumn the column where the worker is
     * @param cellRow the row of a generic cell
     * @param cellColumn the column of a generic cell
     * @return true if the worker is adiacent to the generic cell
     */
    protected boolean adiacentCellVerifier(int workerRow, int workerColumn, int cellRow, int cellColumn) {
        int columnDifference = cellColumn - workerColumn;
        int rowDifference = cellRow - workerRow;
        return (-1 <= columnDifference && columnDifference <= 1 && -1 <= rowDifference && rowDifference <= 1 &&
                !(columnDifference == 0 && rowDifference == 0) && !(workerColumn == cellColumn && workerRow == cellRow));
    }

    public ArrayList<Position> validCellsForInitialPositioning(Cell[][] gameCells) {
        ArrayList<Cell> validCells = new ArrayList<>();
        ArrayList<Position> validPositions = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            validCells.addAll(Arrays.asList(gameCells[i]).subList(0, 5));
        }

        validCells.stream().filter(cell -> cell.getPlayer() == null).forEach((Cell c) -> validPositions.add(new Position(c.getRow(), c.getColumn())));
        return validPositions;
    }

    public Consumer<GameController> putWorkerOnBoard(Position p, Model gd) throws OccupiedCellException, DivinityPowerException {
        //check if the cell is occupied
        if (gd.getCell(p.getRow(), p.getColumn()).getPlayer() != null)
            throw new OccupiedCellException("trying to put worker on an occupied cell");
        gd.getCell(p.getRow(), p.getColumn()).setPlayer(gd.getCurrentPlayer().getName());
        gd.getCurrentPlayer().addWorker();
        //i have to notify board status change
        ArrayList<Cell> modifiedCells = new ArrayList<>();
        modifiedCells.add((Cell) gd.getCell(p.getRow(), p.getColumn()).clone());
        gd.notifyObservers(x -> x.changedBoard(modifiedCells));
        //now i have to manage the number of workers that i have positioned
        if (gd.getCurrentPlayer().getWorkersOnTable() == 2) return GameController::initialPositioningTurnChange;
        else return GameController::requestInitialPositioning;
    }
}
