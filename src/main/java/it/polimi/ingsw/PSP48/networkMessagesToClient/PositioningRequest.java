package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;

/**
 * Network message to requests the player to put a worker on the game board
 */
public class PositioningRequest extends NetworkMessagesToClient {
    private ArrayList<Position> validPositions;

    /**
     * Initializes the network message
     *
     * @param validPositions the cells where a worker can be put
     */
    public PositioningRequest(ArrayList<Position> validPositions) {
        this.validPositions = validPositions;
    }

    /**
     * Invokes the request of worker positioning
     *
     * @param v the view interface where the method must be invoked
     */
    @Override
    public void doAction(ViewInterface v) {
        v.requestInitialPositioning(validPositions);
    }
}
