package it.polimi.ingsw.PSP48.networkMessages;

import it.polimi.ingsw.PSP48.server.observers.ModelObserver;

import java.io.Serializable;

public abstract class receivedObject implements Serializable {
    private String messageType;

    public String getMessageType() {
        return messageType;
    }

    public void updateView(ModelObserver view) {

    }
}
