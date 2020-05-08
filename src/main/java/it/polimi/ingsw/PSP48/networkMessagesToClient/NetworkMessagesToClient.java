package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;

import java.io.Serializable;

public abstract class NetworkMessagesToClient implements Serializable {

    public abstract void doAction(ViewInterface v);

    public Boolean initializationMessage() {
        return false;
    }

}
