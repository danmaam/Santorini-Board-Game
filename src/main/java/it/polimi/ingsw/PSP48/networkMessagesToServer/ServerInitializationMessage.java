package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

public class ServerInitializationMessage extends NetworkMessagesToServer {
    private String message;

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
