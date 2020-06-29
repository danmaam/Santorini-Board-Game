package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

import java.io.Serializable;

/**
 * Serializable class used as network messages to the server.
 * Used to notify some actions to the server.
 */
public abstract class NetworkMessagesToServer implements Serializable {
    /**
     * Invokes a certain method on a ServerNetworkObserver
     *
     * @param obv the ServerNetworkObserver where the method must be invoked
     */
    public abstract void doThings(ServerNetworkObserver obv);

}
