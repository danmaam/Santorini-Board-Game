package it.polimi.ingsw.PSP48;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Artemis extends Divinity {
    private final String name = "Artemis";
    private final Boolean threePlayerSupported = true;

    private int oldRowMove = -1;
    private int oldColumnMove = -1;

    private int lastWorkerMoveID = 0;
    private int oldLevel;
    private int newLevel;

    /**
     * reset the last move coordinate
     */
    @Override
    public void turnBegin() {
        oldColumnMove = -1;
        oldRowMove = -1;
    }

    /**
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in game
     * @return a list of cells valid for the move of the worker
     */
    @Override
    public ArrayList<Cell> getValidCellForMove(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        return super.getValidCellForMove(WorkerColumn, WorkerRow, gameCells, divinitiesInGame).stream()
                .filter(cell -> cell.getColumn() != oldColumnMove && cell.getRow() != oldRowMove)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * reimplements the method since it has to set the last move
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
        cellWhereToMove.setWorker(gameCells[WorkerRow][WorkerColumn].getWorker());
        gameCells[WorkerRow][WorkerColumn].setWorker(null);
        ArrayList<Cell> modifiedCells = new ArrayList<>();
        modifiedCells.add(cellWhereToMove);
        modifiedCells.add(gameCells[WorkerRow][WorkerColumn]);

        return modifiedCells;
    }
}
