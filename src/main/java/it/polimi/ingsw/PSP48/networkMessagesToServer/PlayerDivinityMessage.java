package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

/**
 * Notifies the server of the divinity chosen by a plater
 */
public class PlayerDivinityMessage extends NetworkMessagesToServer {
    private final String divinity;

    /**
     * Initializes the network message
     *
     * @param m the divinity chosen by a player
     */
    public PlayerDivinityMessage(String m) {
        divinity = m;
    }

    /**
     * Invokes the divinity processing by the server
     *
     * @param obv the ServerNetworkObserver where the method must be invoked
     */
    @Override
    public void doThings(ServerNetworkObserver obv) {
        obv.registerPlayerDivinity(divinity);
    }
}
