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

    public ArrayList<Position> getValidPositions() {
        if (validPositions != null) return validPositions;
        else return new ArrayList<Position>();
    }

    public int getwR() {
        return wR;
    }

    public int getwC() {
        return wC;
    }

    public WorkerValidCells(ArrayList<Position> validPositions, int wR, int wC) {
        this.validPositions = validPositions;
        this.wR = wR;
        this.wC = wC;
    }

    public boolean contains(int wR, int wC) {
        return validPositions.contains(new Position(wR, wC));
    }
}
