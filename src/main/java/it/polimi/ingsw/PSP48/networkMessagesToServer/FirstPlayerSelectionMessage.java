package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

/**
 * Notifies the server of the first player selected by the challenger
 */
public class FirstPlayerSelectionMessage extends NetworkMessagesToServer {
    private final String firstPlayer;

    /**
     * Initializes the network message
     *
     * @param firstPlayer the first player chosen by the challenger
     */
    public FirstPlayerSelectionMessage(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    /**
     * Invokes the processing of the first player
     *
     * @param obv the ServerNetworkObserver where the method must be invoked
     */
    @Override
    public void doThings(ServerNetworkObserver obv) {
        obv.selectFirstPlayer(firstPlayer);
    }
}
