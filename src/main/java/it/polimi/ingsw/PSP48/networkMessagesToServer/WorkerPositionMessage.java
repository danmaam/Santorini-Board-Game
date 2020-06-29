package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

/**
 * Notifies that player completed the request to choose a cell where to put one of his workers
 */
public class WorkerPositionMessage extends NetworkMessagesToServer {
    private final Position workerPosition;

    /**
     * Initializes the network message
     *
     * @param m the position where the player wants to put one of his workers
     */
    public WorkerPositionMessage(Position m) {
        workerPosition = m.clone();
    }

    /**
     * Invokes the positioning processing
     *
     * @param obv the ServerNetworkObserver where the method must be invoked
     */
    @Override
    public void doThings(ServerNetworkObserver obv) {
        obv.putWorkerOnTable(workerPosition);
    }
}
