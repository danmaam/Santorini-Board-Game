package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.server.model.Cell;

import java.util.ArrayList;

/**
 * Network message that triggers an update board on the client, when on server some cells changed their content. It contains the
 * changed cells
 */
public class ChangedBoard extends NetworkMessagesToClient {
    private ArrayList<Cell> changedCell = new ArrayList<>();

    /**
     * Initializes the network message
     *
     * @param changed the changed cells
     */
    public ChangedBoard(ArrayList<Cell> changed) {
        this.changedCell = (ArrayList<Cell>) changed.clone();
    }

    /**
     * Invokes the cell update
     *
     * @param v the view interface where the method must be invoked
     */
    @Override
    public void doAction(ViewInterface v) {
        v.changedBoard(changedCell);
    }
}
