package it.polimi.ingsw.PSP48.setupMessagesToClient;

import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;

public class completedSetup implements ClientSetupMessages {
    private String message;

    public completedSetup(String message) {
        this.message = message;
    }

    @Override
    public void doAction(ClientNetworkObserver action) {
        action.completedSetup(message);
    }
}
