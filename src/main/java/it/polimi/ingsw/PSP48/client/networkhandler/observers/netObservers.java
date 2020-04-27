package it.polimi.ingsw.PSP48.client.networkhandler.observers;

import it.polimi.ingsw.PSP48.client.networkHandlerObserver;
import it.polimi.ingsw.PSP48.networkMessages.receivedObject;
import it.polimi.ingsw.PSP48.server.observers.ModelObserver;

public class netObservers implements networkHandlerObserver {
    public ModelObserver playerView;

    @Override
    public void update(receivedObject obj) {
        obj.updateView(playerView);
    }
}
