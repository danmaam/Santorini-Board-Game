package it.polimi.ingsw.PSP48;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.lang.Math;
import java.util.stream.Collectors;

public class Divinity {
    private final String name = "Basic";
    private final Boolean threePlayerSupported = true;

    private int lastWorkerMoveID = 0;
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


    /**
     * @param workerColumn the column where the worker is
     * @param workerRow    the row where the worker is
     * @param gameCells    the actual state of the board
     * @return true if it's possible to add the dome
     */
    public ArrayList<Cell> getValidCellsToPutDome(int workerColumn, int workerRow, Cell[][] gameCells) {
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

        return validCells;
    }

    /**
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
                if (!d.othersBuilding(new MovePosition(WorkerRow, WorkerColumn, c.getRow(), c.getColumn(), 0))) {
                    validCells.remove(c);
                    break;
                }
            }
        }

        return validCells;
    }

    public void build(int buildRow, int buildColumn) {

    }

    /**
     * @param WorkerColumn the column of the cell where the worker is
     * @param WorkerRow    the row of the cell where the worker is
     * @param moveColumn   the column of the cell where the player wants to move
     * @param moveRow      the row of the cell where the player wants to move
     * @param gameCells    the actual state of the board
     * @return the updated cells state
     */
    public ArrayList<Cell> move(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, Cell[][] gameCells) {
        lastWorkerMoveID = gameCells[WorkerRow][WorkerColumn].getWorker().getId();
        oldLevel = gameCells[WorkerRow][WorkerColumn].getLevel();
        newLevel = gameCells[moveRow][moveColumn].getLevel();
        Cell cellWhereToMove = gameCells[moveRow][moveColumn];
        cellWhereToMove.setWorker(gameCells[WorkerRow][WorkerColumn].getWorker());
        gameCells[WorkerRow][WorkerColumn].setWorker(null);
        ArrayList<Cell> modifiedCells = new ArrayList<>();
        modifiedCells.add(cellWhereToMove);
        modifiedCells.add(gameCells[WorkerRow][WorkerColumn]);

        return modifiedCells;
    }

    /**
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
     * @return a predicate always true, since without a divinity there is not a modifier on other players' actions
     */
    public Boolean othersMove(MovePosition move) {
        return true;
    }

    /**
     * @return a predicate always true, since without a divinity there is not a modifier on other players' actions
     */
    public Boolean othersBuilding(MovePosition move) {
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


}
