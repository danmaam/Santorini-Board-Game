package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

import java.io.Serializable;

public abstract class NetworkMessagesToServer implements Serializable {
    public abstract void doThings(ServerNetworkObserver obv);

    public boolean InitializationMessage() {
        return false;
    }
}
