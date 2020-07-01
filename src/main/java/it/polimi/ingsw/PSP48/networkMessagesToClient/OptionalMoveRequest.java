package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.WorkerValidCells;

import java.util.ArrayList;

/**
 * Network messages sent to client to request the player to complete an optional move action, or to skip it. It contains the coordinates of possible
 * move action
 */
public class OptionalMoveRequest extends NetworkMessagesToClient {
    ArrayList<WorkerValidCells> validCells;

    /**
     * Invokes the request of an optional move action
     *
     * @param v the view interface where the method must be invoked
     */
    @Override
    public void doAction(ViewInterface v) {
        v.requestOptionalMove(validCells);
    }

    /**
     * Initializes the network message
     *
     * @param validCells The association valid workers-cells for the move
     */
    public OptionalMoveRequest(ArrayList<WorkerValidCells> validCells) {
        this.validCells = (ArrayList<WorkerValidCells>) validCells.clone();
    }
}
