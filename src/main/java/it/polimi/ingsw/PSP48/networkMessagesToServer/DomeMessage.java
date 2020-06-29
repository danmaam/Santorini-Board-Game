package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.server.model.ActionCoordinates;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

/**
 * Network messages that notifies a dome put action
 */
public class DomeMessage extends NetworkMessagesToServer {
    private final ActionCoordinates cordinateOfTheMove;

    /**
     * Initializes the network message
     *
     * @param m the coordinates of the dome action
     */
    public DomeMessage(ActionCoordinates m) {
        cordinateOfTheMove = m.clone();
    }

    /**
     * Invokes the processing of the dome action
     *
     * @param obv the ServerNetworkObserver where the method must be invoked
     */
    @Override
    public void doThings(ServerNetworkObserver obv) {
        obv.dome(cordinateOfTheMove);
    }
}
