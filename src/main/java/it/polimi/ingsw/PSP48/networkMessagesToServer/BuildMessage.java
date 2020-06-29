package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.server.model.ActionCoordinates;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

/**
 * Netowkr message used to notify the server of a move
 */
public class BuildMessage extends NetworkMessagesToServer {
    private final ActionCoordinates cordinateOfTheMove;

    /**
     * Initializes the network message
     *
     * @param m the move coordinates
     */
    public BuildMessage(ActionCoordinates m) {
        cordinateOfTheMove = m.clone();
    }

    /**
     * Invokes the move processing
     *
     * @param obv the ServerNetworkObserver where the method must be invoked
     */
    @Override
    public void doThings(ServerNetworkObserver obv) {
        obv.build(cordinateOfTheMove);
    }
}
