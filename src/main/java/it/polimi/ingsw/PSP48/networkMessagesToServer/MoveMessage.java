package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

public class MoveMessage extends NetworkMessagesToServer {

    private final MoveCoordinates cordinateOfTheMove;

    public MoveMessage(MoveCoordinates m) {
        cordinateOfTheMove = m.clone();
    }

    @Override
    public void doThings(ServerNetworkObserver obv) {
        obv.move(cordinateOfTheMove);
    }
}
