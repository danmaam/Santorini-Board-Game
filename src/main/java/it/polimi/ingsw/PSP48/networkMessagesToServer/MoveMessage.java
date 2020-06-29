package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.server.model.ActionCoordinates;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

/**
 * Notifies the server of a move action
 */
public class MoveMessage extends NetworkMessagesToServer {

    private final ActionCoordinates cordinateOfTheMove;

    /**
     * Initializes the network message
     *
     * @param m the coordinates of the move
     */
    public MoveMessage(ActionCoordinates m) {
        cordinateOfTheMove = m.clone();
    }

    /**
     * Invokes the processing of the move action
     *
     * @param obv the ServerNetworkObserver where the method must be invoked
     */
    @Override
    public void doThings(ServerNetworkObserver obv) {
        obv.move(cordinateOfTheMove);
    }
}
