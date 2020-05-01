package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.WorkerValidCells;

import java.util.ArrayList;

public class RequestMove extends NetworkMessagesToClient {
    ArrayList<WorkerValidCells> validCells;

    @Override
    public void doAction(AbstractView v) {
        v.requestMove(validCells);
    }

    public RequestMove(ArrayList<WorkerValidCells> validCells) {
        this.validCells = (ArrayList<WorkerValidCells>) validCells.clone();
    }
}
