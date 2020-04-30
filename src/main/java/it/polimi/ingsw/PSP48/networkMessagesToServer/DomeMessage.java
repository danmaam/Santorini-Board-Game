package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

public class DomeMessage extends NetworkMessagesToServer {
    private final MoveCoordinates cordinateOfTheMove;

    public DomeMessage(MoveCoordinates m) {
        cordinateOfTheMove = m.clone();
    }

    @Override
    public void doThings(ServerNetworkObserver obv) {
        obv.dome(cordinateOfTheMove);
    }
}
