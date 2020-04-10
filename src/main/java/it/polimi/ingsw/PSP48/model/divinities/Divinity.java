package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.model.exceptions.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Divinity {
    private final String name = "Basic";
    private final Boolean threePlayerSupported = true;


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
     * @param WorkerColumn          the column where the worker is
     * @param WorkerRow             the row where the worker is
     * @param gameCells             the actual board state
     * @param otherDivinitiesInGame the other divinities in game
     * @return a list of cells valid for the move of the worker
     * @author Daniele Mammone
     */
    public ArrayList<Cell> getValidCellForMove(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> otherDivinitiesInGame) {
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
                if (!d.othersMove(new MovePosition(WorkerRow, WorkerColumn, c.getRow(), c.getColumn(), gameCells[c.getRow()][c.getColumn()].getLevel() - gameCells[WorkerRow][WorkerColumn].getLevel()))) {
                    cellsToBeRemoved.add(c);
                    break;
                }
            }
        }

        for (Cell c : cellsToBeRemoved) validCells.remove(c);

        //now in valid cells there is the list with compatible moves cells

        return validCells;
    }

    /**
     * @param WorkerColumn the column of the cell where the worker is
     * @param WorkerRow    the row of the cell where the worker is
     * @param moveColumn   the column of the board where the worker wants to move
     * @param moveRow      the row of the board where the worker wants to move
     * @param gd           the actual game state
     * @throws NotAdiacentCellException if the destination cell is not adiacent to the worker
     * @throws IncorrectLevelException  if the destination cell is too high to be reached
     * @throws OccupiedCellException    if the destination cell has another worker on it
     * @throws DomedCellException       if the destination cell has a dome on it
     * @author Daniele Mammone
     */
    public void move(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, GameData gd) throws
            NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DomedCellException, DivinityPowerException, NotEmptyCellException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(WorkerRow, WorkerColumn, moveRow, moveColumn)))
            throw new NotAdiacentCellException("Celle non adiacenti");
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

        //at this point, the move is valid and we must change the state of the game board

        gd.getCurrentPlayer().setOldLevel(workerLevel);
        gd.getCurrentPlayer().setNewLevel(moveLevel);
        gd.getCell(moveRow, moveColumn).setPlayer(gd.getCell(WorkerRow, WorkerColumn).getPlayer());
        gd.getCell(WorkerRow, WorkerColumn).setPlayer(null);

        //now, the game board has been modified
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
    public ArrayList<Cell> getValidCellForBuilding(int WorkerColumn, int WorkerRow, ArrayList<Divinity> otherDivinitiesInGame, Cell[][] gameCell) {
        //with the for loop, i'm adding to the arrayList the cell adiacent to the worker
        ArrayList<Cell> validCells = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && 0 <= WorkerRow + i && WorkerRow + i <= 4 && 0 <= WorkerColumn + j && WorkerColumn + j <= 4) {
                    validCells.add(gameCell[WorkerRow + i][WorkerColumn + j]);
                }
            }
        }

        //now we have to remove cells where the move is invalid

        validCells = validCells.stream()
                .filter(cell -> cell.getPlayer() == null)
                .filter(cell -> !cell.isDomed())
                .filter(cell -> cell.getLevel() != 3)
                .collect(Collectors.toCollection(ArrayList::new));

        //now we have to remove cells due to other divinites powers
        //we have to remove the current divinity from the others, to check if their power can invalid the move

        ArrayList<Cell> notValid = new ArrayList<>();

        for (Cell c : validCells) {
            for (Divinity d : otherDivinitiesInGame) {
                if (d.othersBuilding(new BuildPosition(WorkerRow, WorkerColumn, c.getRow(), c.getColumn(), c.getLevel()))) {
                    notValid.add(c);
                    break;
                }
            }
        }

        for (Cell c : notValid) {
            validCells.remove(c);
        }

        return validCells;
    }


    /**
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param buildRow     the row where the player wants to add a level
     * @param buildColumn  the column where the player wants to add a level
     * @param gd           the actual game board state
     * @throws NotAdiacentCellException     if the cell where the player wants to build is not adiacent to the worker's one
     * @throws OccupiedCellException        if the destination cell is occupied by another worker
     * @throws DomedCellException           is the cell is already domed
     * @throws MaximumLevelReachedException if the cell contains a level 3 building
     * @throws DivinityPowerException       if another divinity blocks the increment of the level
     * @author Daniele Mammone
     */
    public void build(int workerRow, int workerColumn, int buildRow, int buildColumn, GameData gd) throws
            NotAdiacentCellException, OccupiedCellException, DomedCellException, MaximumLevelReachedException, DivinityPowerException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(workerRow, workerColumn, buildRow, buildColumn)))
            throw new NotAdiacentCellException("Celle non adiacenti");
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
                if (!d.othersDome(new DomePosition(workerRow, workerColumn, c.getRow(), c.getColumn(), c.getLevel()))) {
                    notValid.add(c);
                    break;
                }
            }
        }

        for (Cell c : notValid) validCells.remove(c);

        return validCells;
    }

    /**
     * @param workerRow    the row where the worker is
     * @param workerColumn the column where the worker is
     * @param domeRow      the row where the player wants to add the dome
     * @param domeColumn   the column where the player wants to add the dome
     * @param gd           the current game board state
     * @throws NotAdiacentCellException        if the cell where the player wants to add the dome is not adiacent to the worker's one
     * @throws OccupiedCellException           if the destination cell is occupied by another worker
     * @throws DomedCellException              is the cell is already domed
     * @throws MaximumLevelNotReachedException if the cell doesn't contain a level 3 building
     * @throws DivinityPowerException          if another divinity blocks the adding of the dome
     * @author Daniele Mammone
     */
    public void dome(int workerRow, int workerColumn, int domeRow, int domeColumn, GameData gd) throws
            NotAdiacentCellException, OccupiedCellException, DomedCellException, MaximumLevelNotReachedException, DivinityPowerException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(workerRow, workerColumn, domeRow, domeColumn)))
            throw new NotAdiacentCellException("Celle non adiacenti");
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
     * @param row       the row where the player wants to put his worker
     * @param column    the column where the player wants to put his worker
     * @param gameCells the actual state of the board
     * @return true if the positioning is valid
     * @throws NotEmptyCellException is on the cell there is already another worker
     * @author Daniele Mammone
     */
    public void gameSetUp(int row, int column, Cell[][] gameCells, String playerName) throws NotEmptyCellException, DivinityPowerException {
        if (gameCells[row][column].getPlayer() != null) throw new NotEmptyCellException("Cella già occupata");
        gameCells[row][column].setPlayer(playerName);
    }


    /**
     * don't do anything since without a divinity there isn't a modifier
     */
    public void turnEnd() {

    }

    /**
     * don't do anything since without a divinity there isn't a modifier
     */

    public void turnBegin(GameData gd) {

    }

    /**
     * @param gd the state of the game
     * @return true if the actual player considererd has won, false if the game must go on
     */
    public boolean winCondition(GameData gd) {
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

    public ArrayList<Cell> validCellsForInitialPositioning(Cell[][] gameCells) {
        ArrayList<Cell> validCells = new ArrayList<>();
        for (int i = 0; i <= 4; i++) {
            for (int j = 0; j <= 4; j++) {
                validCells.add(gameCells[i][j]);
            }
        }

        return validCells.stream().filter(cell -> cell.getPlayer() == null).collect(Collectors.toCollection(ArrayList::new));
    }

}
