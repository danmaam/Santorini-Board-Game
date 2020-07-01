package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.WorkerValidCells;

import java.util.ArrayList;

/**
 * Network messages sent to client to request the player to complete a move action. It contains the coordinates of possible
 * move action
 */

public class RequestMove extends NetworkMessagesToClient {
    ArrayList<WorkerValidCells> validCells;

    /**
     * Invokes the request of a move action
     *
     * @param v the view interface where the method must be invoked
     */
    @Override
    public void doAction(ViewInterface v) {
        v.requestMove(validCells);
    }

    /**
     * Initializes the network message
     *
     * @param validCells the association valid workers-valid cells where the worker can be moved
     */
    public RequestMove(ArrayList<WorkerValidCells> validCells) {
        this.validCells = (ArrayList<WorkerValidCells>) validCells.clone();
    }
}
