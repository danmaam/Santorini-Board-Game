package it.polimi.ingsw.PSP48;

import it.polimi.ingsw.PSP48.server.model.Position;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Data type representing an association between a worker, and the valid cells where it can complete a certain action (eg. move, build, dome)
 */
public class WorkerValidCells implements Serializable {
    private final ArrayList<Position> validPositions;
    private final int wR;
    private final int wC;

    /**
     * @return the valid positions where the contained worker can complete the action
     */
    public ArrayList<Position> getValidPositions() {
        if (validPositions != null) return validPositions;
        else return new ArrayList<Position>();
    }

    /**
     * @return the row of the contained worker
     */
    public int getwR() {
        return wR;
    }

    /**
     * @return the column of the contained worker
     */
    public int getwC() {
        return wC;
    }

    /**
     * Initializes the object
     *
     * @param validPositions the valid positions where the worker can complete the action
     * @param wR             the worker's row
     * @param wC             the worker's column
     */
    public WorkerValidCells(ArrayList<Position> validPositions, int wR, int wC) {
        this.validPositions = validPositions;
        this.wR = wR;
        this.wC = wC;
    }

    /**
     * Checks if the worker can complete an action in a certain cell
     *
     * @param wR the row of the action
     * @param wC the column of the action
     * @return if in the desired cell the worker can do the action
     */
    public boolean contains(int wR, int wC) {
        return validPositions.contains(new Position(wR, wC));
    }
}
