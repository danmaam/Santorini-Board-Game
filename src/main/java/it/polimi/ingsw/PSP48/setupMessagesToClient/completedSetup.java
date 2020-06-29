package it.polimi.ingsw.PSP48.setupMessagesToClient;

import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;

/**
 * Notifies the client that the setup phase have been completed
 */
public class completedSetup implements ClientSetupMessages {

    private String message;

    /**
     * Initializes the network message
     *
     * @param message the setup message sent to the client
     */
    public completedSetup(String message) {
        this.message = message;
    }

    /**
     * Invokes the handling of completed setup
     *
     * @param action the ClientNetworkObserver on which the method must be invoked
     */
    @Override
    public void doAction(ClientNetworkObserver action) {
        action.completedSetup(message);
    }
}
