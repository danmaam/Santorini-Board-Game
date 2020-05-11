package it.polimi.ingsw.PSP48.setupMessagesToClient;

import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;

import java.io.Serializable;
import java.util.function.Consumer;

public interface ClientSetupMessages extends Serializable {
    public void doAction(ClientNetworkObserver action);
}
