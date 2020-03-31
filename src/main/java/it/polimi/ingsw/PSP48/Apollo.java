package it.polimi.ingsw.PSP48;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Apollo extends Divinity {
    private final String name = "Apollo";
    private final Boolean threePlayerSupported = true;

    private int lastWorkerMoveID = 0;
    private int oldLevel;
    private int newLevel;

    /**
     * reimplements getValidCellForMove since also occupied Cells are valid
     *
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in game
     * @return a list of cells valid for the move of the worker
     */
    @Override
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
     * reimplements move since it has to swap workers if there is a worker on the move cell
     *
     * @param WorkerColumn the column of the cell where the worker is
     * @param WorkerRow    the row of the cell where the worker is
     * @param moveColumn   the column of the cell where the player wants to move
     * @param moveRow      the row of the cell where the player wants to move
     * @param gameCells    the actual state of the board
     * @return the updated cells state
     */
    @Override
    public ArrayList<Cell> move(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, Cell[][] gameCells) {
        lastWorkerMoveID = gameCells[WorkerRow][WorkerColumn].getWorker().getId();
        oldLevel = gameCells[WorkerRow][WorkerColumn].getLevel();
        newLevel = gameCells[moveRow][moveColumn].getLevel();
        Cell cellWhereToMove = gameCells[moveRow][moveColumn];
        Worker tempWorker = cellWhereToMove.getWorker();
        cellWhereToMove.setWorker(gameCells[WorkerRow][WorkerColumn].getWorker());
        gameCells[WorkerRow][WorkerColumn].setWorker(tempWorker);
        ArrayList<Cell> modifiedCells = new ArrayList<>();
        modifiedCells.add(cellWhereToMove);
        modifiedCells.add(gameCells[WorkerRow][WorkerColumn]);

        return modifiedCells;
    }
}
