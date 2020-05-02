package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

public class ServerInitializationMessage extends NetworkMessagesToServer {
    private String message;

    @Override
    public boolean InitializationMessage() {
        return true;
    }

    public String getMessage() {
        return message;
    }

    public ServerInitializationMessage(String message) {
        this.message = message;
    }

    @Override
    public void doThings(ServerNetworkObserver obv) {

    }
}
