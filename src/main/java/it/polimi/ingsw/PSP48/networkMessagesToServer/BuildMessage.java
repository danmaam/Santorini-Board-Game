package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.server.model.MoveCoordinates;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

public class BuildMessage extends NetworkMessagesToServer {
    private final MoveCoordinates cordinateOfTheMove;

    public BuildMessage(MoveCoordinates m) {
        cordinateOfTheMove = m.clone();
    }

    @Override
    public void doThings(ServerNetworkObserver obv) {
        obv.build(cordinateOfTheMove);
    }
}
