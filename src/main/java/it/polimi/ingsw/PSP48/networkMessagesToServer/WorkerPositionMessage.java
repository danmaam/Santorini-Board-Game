package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

public class WorkerPositionMessage extends NetworkMessagesToServer {
    private final Position workerPosition;

    public WorkerPositionMessage(Position m) {
        workerPosition = m.clone();
    }

    @Override
    public void doThings(ServerNetworkObserver obv) {
        obv.putWorkerOnTable(workerPosition);
    }
}
