package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

public class PlayerDivinityMessage extends NetworkMessagesToServer {
    private final String divinity;

    public PlayerDivinityMessage(String m) {
        divinity = m;
    }

    @Override
    public void doThings(ServerNetworkObserver obv) {
        obv.registerPlayerDivinity(divinity);
    }
}
