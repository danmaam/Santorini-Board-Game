package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.server.model.Cell;

import java.util.ArrayList;

public class ChangedBoard extends NetworkMessagesToClient {
    private ArrayList<Cell> changedCell = new ArrayList<>();

    public ChangedBoard(ArrayList<Cell> chengedCells) {
        this.changedCell = (ArrayList<Cell>) chengedCells.clone();
    }

    @Override
    public void doAction(AbstractView v) {
        v.changedBoard(changedCell);
    }
}
