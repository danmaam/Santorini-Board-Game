package it.polimi.ingsw.PSP48;

import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;

public class WorkerValidCells {
    private ArrayList<Position> validPositions;
    private int wR;
    private int wC;

    public ArrayList<Position> getValidPositions() {
        return validPositions;
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
}
