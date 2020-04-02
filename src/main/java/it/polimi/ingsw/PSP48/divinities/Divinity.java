package it.polimi.ingsw.PSP48.divinities;

import it.polimi.ingsw.PSP48.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Divinity {
    private final String name = "Basic";
    private final Boolean threePlayerSupported = true;

    private int oldLevel;
    private int newLevel;

    //Methods

    /**
     * @return the name of the divinity
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return if the divinity supports the three player gaming
     */
    public Boolean getThreePlayerSupported() {
        return threePlayerSupported;
    }

    //
    //
    //
    //METHODS FOR MOVING

    /**
     * @author Daniele Mammone
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in game
     * @return a list of cells valid for the move of the worker
     */
    public ArrayList<Cell> getValidCellForMove(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        Cell actualWorkerCell = gameCells[WorkerRow][WorkerColumn];
        ArrayList<Cell> validCells = new ArrayList<>();

        //with the for loop, i'm adding to the arrayList the cell adiacent to the worker
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != WorkerRow && j != WorkerColumn && 0 <= WorkerRow + i && WorkerRow + i <= 4 && 0 <= WorkerColumn + j && WorkerColumn + j <= 4) {
                    validCells.add(gameCells[i][j]);
                }
            }
        }

        //we have to remove the current divinity from the others, to check if their power can invalid the move
        divinitiesInGame.remove(gameCells[WorkerRow][WorkerColumn].getWorker().getDivinity());

        validCells = validCells.stream()
                .filter(cell -> cell.getWorker() != null) // deletes from the valid cells ones where there's a worker on
                //delets from the valid the cell which are too high or too low to be reached
                .filter(cell -> -3 <= cell.getLevel() - actualWorkerCell.getLevel() && cell.getLevel() - actualWorkerCell.getLevel() <= 1)
                //deletes the domed cells
                .filter(cell -> !cell.isDomed())
                .collect(Collectors.toCollection(ArrayList::new));

        //now we have to remove cells where the move is impossible due to other divinity powers

        for (Cell c : validCells) {
            for (Divinity d : divinitiesInGame) {
                if (!d.othersMove(new MovePosition(WorkerRow, WorkerColumn, c.getRow(), c.getColumn(), 0))) {
                    validCells.remove(c);
                    break;
                }
            }
        }

        //now in valid cells there is the list with compatible moves cells

        return validCells;
    }

    /**
     * @author Daniele Mammone
     * @param WorkerColumn the column of the cell where the worker is
     * @param WorkerRow the row of the cell where the worker is
     * @param moveColumn the column of the board where the worker wants to move
     * @param moveRow the row of the board where the worker wants to move
     * @param gameCells the game board
     * @throws NotAdiacentCellException if the destination cell is not adiacent to the worker
     * @throws IncorrectLevelException if the destination cell is too high to be reached
     * @throws OccupiedCellException if the destination cell has another worker on it
     * @throws DomedCellException if the destination cell has a dome on it
     */
    public void move(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame ) throws
            NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DomedCellException, DivinityPowerException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(WorkerRow, WorkerColumn, moveRow, moveColumn))) throw new NotAdiacentCellException("Celle non adiacenti");
        //second check: the two levels must be compatible
        int workerLevel = gameCells[WorkerRow][WorkerColumn].getLevel();
        int moveLevel = gameCells[moveRow][moveColumn].getLevel();
        if (!(moveLevel-workerLevel <= 1)) throw new IncorrectLevelException("Stai cerando di salire a un livello troppo alto");
        //third check: the cell must not be occupied
        if (!(gameCells[moveRow][moveColumn].getWorker() == null)) throw new OccupiedCellException("Cella occupata");
        //fourth check: the cell must not be domed
        if (gameCells[moveRow][moveColumn].isDomed()) throw new DomedCellException("Stai cercando di salire su una cella con cupola");
        //fifth check: if another different divinity doesn't invalid this move
        divinitiesInGame.remove(gameCells[WorkerRow][WorkerColumn].getWorker().getDivinity());

        for(Divinity d : divinitiesInGame) {
            if (!d.othersMove(new MovePosition(WorkerRow, WorkerColumn, moveRow, moveColumn, moveLevel - moveColumn))) throw new DivinityPowerException("Fail due to other divinity");
        }

        //at this point, the move is valid and we must change the state of the game board

        oldLevel = workerLevel;
        newLevel = moveLevel;
        gameCells[moveRow][moveColumn].setWorker(gameCells[WorkerRow][WorkerColumn].getWorker());
        gameCells[WorkerRow][WorkerColumn].setWorker(null);

        //now, the game board has been modified
    }

    //
    //
    //
    // METHODS FOR BUILDING


    /**
     * @author Daniele Mammone
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in the game
     * @return a list of cell valid for the building of the worker
     */
    public ArrayList<Cell> getValidCellForBuilding(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        //with the for loop, i'm adding to the arrayList the cell adiacent to the worker
        ArrayList<Cell> validCells = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != WorkerRow && j != WorkerColumn && 0 <= WorkerRow + i && WorkerRow + i <= 4 && 0 <= WorkerColumn + j && WorkerColumn + j <= 4) {
                    validCells.add(gameCells[i][j]);
                }
            }
        }

        //now we have to remove cells where the move is invalid

        validCells = validCells.stream()
                .filter(cell -> cell.getWorker() == null)
                .filter(cell -> !cell.isDomed())
                .filter(cell -> cell.getLevel() != 3)
                .collect(Collectors.toCollection(ArrayList::new));

        //now we have to remove cells due to other divinites powers
        //we have to remove the current divinity from the others, to check if their power can invalid the move
        divinitiesInGame.remove(gameCells[WorkerRow][WorkerColumn].getWorker().getDivinity());

        for (Cell c : validCells) {
            for (Divinity d : divinitiesInGame) {
                if (!d.othersBuilding(new BuildPosition(WorkerRow, WorkerColumn, c.getRow(), c.getColumn(), c.getLevel()))) {
                    validCells.remove(c);
                    break;
                }
            }
        }

        return validCells;
    }


    /**
     * @author Daniele Mammone
     * @param workerRow the row where the worker is
     * @param workerColumn the column where the worker is
     * @param buildRow the row where the player wants to add a level
     * @param buildColumn the column where the player wants to add a level
     * @param gameCells the actual state of the game board
     * @param divinitiesInGame the divinities in game
     * @throws NotAdiacentCellException if the cell where the player wants to build is not adiacent to the worker's one
     * @throws OccupiedCellException if the destination cell is occupied by another worker
     * @throws DomedCellException is the cell is already domed
     * @throws MaximumLevelReachedException if the cell contains a level 3 building
     * @throws DivinityPowerException if another divinity blocks the increment of the level
     */
    public void build(int workerRow, int workerColumn, int buildRow, int buildColumn, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) throws
            NotAdiacentCellException, OccupiedCellException, DomedCellException, MaximumLevelReachedException, DivinityPowerException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(workerRow, workerColumn, buildRow, buildColumn))) throw new NotAdiacentCellException("Celle non adiacenti");
        //second check: the cell must be empty of workers
        if (!(gameCells[buildRow][buildColumn].getWorker() == null)) throw new OccupiedCellException("Cella occupata");
        //third check: the cell must not be domed
        if (gameCells[buildRow][buildColumn].isDomed()) throw new DomedCellException("Stai cercando di costruire su una cella con cupola");
        //fourth check: if we are not already at the maximum level
        if (3 == gameCells[buildRow][buildColumn].getLevel()) throw new MaximumLevelReachedException("Trying to build on level 3");
        //fifth check: if another different divinity doesn't invalid this move
        divinitiesInGame.remove(gameCells[workerRow][workerColumn].getWorker().getDivinity());

        for(Divinity d : divinitiesInGame) {
            if (!d.othersBuilding(new BuildPosition(workerRow, workerColumn, buildRow, buildColumn, gameCells[buildRow][buildColumn].getLevel()))) throw new DivinityPowerException("Fail due to other divinity");
        }

        //at this point, the move is valid and we must change the state of the game board

        gameCells[buildRow][buildColumn].addLevel();

        //now, the game has been modified
    }


    //
    //
    //
    // METHODS FOR DOME CONSTRUCTION

    /**
     * @author Daniele Mammone
     * @param workerColumn the column where the worker is
     * @param workerRow    the row where the worker is
     * @param gameCells    the actual state of the board
     * @return true if it's possible to add the dome
     */
    public ArrayList<Cell> getValidCellsToPutDome(int workerColumn, int workerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        ArrayList<Cell> newCells = new ArrayList<>();
        //with the for loop, i'm adding to the arrayList the cell adiacent to the worker
        ArrayList<Cell> validCells = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != workerRow && j != workerColumn && 0 <= workerRow + i && workerRow + i <= 4 && 0 <= workerColumn + j && workerColumn + j <= 4) {
                    validCells.add(gameCells[i][j]);
                }
            }
        }

        validCells = validCells.stream()
                .filter(cell -> cell.getWorker() == null)
                .filter(cell -> cell.getLevel() == 3)
                .filter(cell -> !cell.isDomed())
                .collect(Collectors.toCollection(ArrayList::new));

        for (Cell c : validCells) {
            for (Divinity d : divinitiesInGame) {
                if (!d.othersDome(new DomePosition(workerRow, workerColumn, c.getRow(), c.getColumn(), c.getLevel()))) {
                    validCells.remove(c);
                    break;
                }
            }
        }

        return validCells;
    }

    /**
     * @author Daniele Mammone
     * @param workerRow the row where the worker is
     * @param workerColumn the column where the worker is
     * @param domeRow the row where the player wants to add the dome
     * @param domeColumn the column where the player wants to add the dome
     * @param gameCells the actual state of the game board
     * @param divinitiesInGame the divinities in game
     * @throws NotAdiacentCellException if the cell where the player wants to add the dome is not adiacent to the worker's one
     * @throws OccupiedCellException if the destination cell is occupied by another worker
     * @throws DomedCellException is the cell is already domed
     * @throws MaximumLevelNotReachedException if the cell doesn't contain a level 3 building
     * @throws DivinityPowerException if another divinity blocks the adding of the dome
     */
    public void dome(int workerRow, int workerColumn, int domeRow, int domeColumn, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) throws
            NotAdiacentCellException, OccupiedCellException, DomedCellException, MaximumLevelNotReachedException, DivinityPowerException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(workerRow, workerColumn, domeRow, domeColumn))) throw new NotAdiacentCellException("Celle non adiacenti");
        //second check: the cell must be empty of workers
        if (!(gameCells[domeRow][domeColumn].getWorker() == null)) throw new OccupiedCellException("Cella occupata");
        //third check: the cell must not be already domed
        if (gameCells[domeRow][domeColumn].isDomed()) throw new DomedCellException("Stai cercando di costruire su una cella con cupola");
        //fourth check: if we are not already at the maximum level
        if (3 != gameCells[domeRow][domeColumn].getLevel()) throw new MaximumLevelNotReachedException("Trying to add dome on a cell with a lower level than 3");
        //fifth check: if another different divinity doesn't invalid this move
        divinitiesInGame.remove(gameCells[workerRow][workerColumn].getWorker().getDivinity());

        for(Divinity d : divinitiesInGame) {
            if (!d.othersDome(new DomePosition(workerRow, workerColumn, domeRow, domeColumn, gameCells[domeRow][domeColumn].getLevel()))) throw new DivinityPowerException("Fail due to other divinity");
        }

        //at this point, the move is valid and we must change the state of the game board

        gameCells[domeRow][domeColumn].addDome();
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

    //////
    /**
     * @author Daniele Mammone
     * @param row       the row where the player wants to put his worker
     * @param column    the column where the player wants to put his worker
     * @param gameCells the actual state of the board
     * @return true if the positioning is valid
     * @throws NotEmptyCellException is on the cell there is already another worker
     */
    public boolean gameSetUp(int row, int column, Cell[][] gameCells) throws NotEmptyCellException {
        if (gameCells[row][column].getWorker() != null) throw new NotEmptyCellException("Cella già occupata");
        return true;
    }



    /**
     * don't do anything since without a divinity there isn't a modifier
     */
    public void turnEnd() {

    }

    /**
     * don't do anything since without a divinity there isn't a modifier
     */
    public void turnBegin() {

    }

    /**
     * @param playerWorkers the workers of the actual player
     * @return true if the actual player considererd has won, false if the game must go on
     */
    public boolean winCondition(ArrayList<Worker> playerWorkers) {
        return (oldLevel != newLevel && newLevel == 3);
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
                !(columnDifference == 0 && rowDifference == 0));
    }

}
