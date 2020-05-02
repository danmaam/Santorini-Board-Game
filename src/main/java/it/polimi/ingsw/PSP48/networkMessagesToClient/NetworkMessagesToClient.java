package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.AbstractView;

import java.io.Serializable;
import java.util.function.Consumer;

public abstract class NetworkMessagesToClient implements Serializable {

    public abstract void doAction(AbstractView v);

    public Boolean initializationMessage() {
        return false;
    }

}
