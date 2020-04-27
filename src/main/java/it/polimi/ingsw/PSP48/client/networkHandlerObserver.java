package it.polimi.ingsw.PSP48.client;

import it.polimi.ingsw.PSP48.networkMessages.receivedObject;

public interface networkHandlerObserver {
    public void update(receivedObject obj);
}
