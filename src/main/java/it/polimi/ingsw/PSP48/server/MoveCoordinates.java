package it.polimi.ingsw.PSP48.server;

import java.io.Serializable;

/**
 * The coordinates of some action completed by a player
 */
public class MoveCoordinates implements Serializable {
    private final int workerRow;
    private final int workerColumn;
    private final int moveRow;

    /**
     * @return the row of the worker chosen for the action
     */
    public int getWorkerRow() {
        return workerRow;
    }

    /**
     * @return the column of the worker chosen for the action
     */
    public int getWorkerColumn() {
        return workerColumn;
    }

    /**
     * @return the row where the player wants to do the action
     */
    public int getMoveRow() {
        return moveRow;
    }

    /**
     * @return the column where the player wants to do the action
     */
    public int getMoveColumn() {
        return moveColumn;
    }


    public MoveCoordinates(int workerRow, int workerColumn, int moveRow, int moveColumn) {
        this.workerRow = workerRow;
        this.workerColumn = workerColumn;
        this.moveRow = moveRow;
        this.moveColumn = moveColumn;
    }

    private final int moveColumn;

    public MoveCoordinates clone() {
        return new MoveCoordinates(workerRow, workerColumn, moveRow, moveColumn);
    }
}
