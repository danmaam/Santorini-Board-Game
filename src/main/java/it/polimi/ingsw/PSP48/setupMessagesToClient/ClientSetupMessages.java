package it.polimi.ingsw.PSP48.setupMessagesToClient;

import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Interface of network messages used to setup connection between client and server
 */
public interface ClientSetupMessages extends Serializable {
    /**
     * Invokes some method of a ClientNetworkObserver
     *
     * @param action the ClientNetworkObserver on which the method must be invoked
     */
    public void doAction(ClientNetworkObserver action);
}
