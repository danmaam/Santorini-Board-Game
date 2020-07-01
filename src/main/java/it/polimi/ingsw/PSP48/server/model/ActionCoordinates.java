package it.polimi.ingsw.PSP48.server.model;

import java.io.Serializable;

/**
 * The coordinates of some action completed by a player
 */
public class ActionCoordinates implements Serializable {
    private final int workerRow;
    private final int workerColumn;
    private final int moveRow;

    /**
     * Getter of worker row
     *
     * @return the row of the worker chosen for the action
     */
    public int getWorkerRow() {
        return workerRow;
    }

    /**
     * Getter of worker column
     *
     * @return the column of the worker chosen for the action
     */
    public int getWorkerColumn() {
        return workerColumn;
    }

    /**
     * Getter of destination row
     *
     * @return the row where the player wants to do the action
     */
    public int getMoveRow() {
        return moveRow;
    }

    /**
     * Getter of destination column
     * @return the column where the player wants to do the action
     */
    public int getMoveColumn() {
        return moveColumn;
    }


    /**
     * Initializes the object
     *
     * @param workerRow    the row of the worker
     * @param workerColumn the column of the worker
     * @param moveRow      the row of the cell where the action must be completed
     * @param moveColumn   the column of the cell on which the action must be completed
     */
    public ActionCoordinates(int workerRow, int workerColumn, int moveRow, int moveColumn) {
        this.workerRow = workerRow;
        this.workerColumn = workerColumn;
        this.moveRow = moveRow;
        this.moveColumn = moveColumn;
    }

    private final int moveColumn;

    /**
     * Clones the object
     *
     * @return a cloned coordinates object
     */
    public ActionCoordinates clone() {
        return new ActionCoordinates(workerRow, workerColumn, moveRow, moveColumn);
    }
}
