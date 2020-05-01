package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

public class FirstPlayerSelectionMessage extends NetworkMessagesToServer {
    private String firstPlayer;

    public FirstPlayerSelectionMessage(String firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    @Override
    public void doThings(ServerNetworkObserver obv) {
        obv.firstPlayerRegistration(firstPlayer);
    }
}
