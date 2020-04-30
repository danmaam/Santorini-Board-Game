package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.observers.ModelObserver;

import java.io.Serializable;

public abstract class receivedObject implements Serializable {
    private String messageType;

    public String getMessageType() {
        return messageType;
    }

    public void updateView(ModelObserver view) {

    }
}
