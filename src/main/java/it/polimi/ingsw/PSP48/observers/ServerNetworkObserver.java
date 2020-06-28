package it.polimi.ingsw.PSP48.observers;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;

/**
 * An observer of server incoming messages handler
 */
public interface ServerNetworkObserver extends ViewObserver {

    /**
     * Requests the observer to destroy a game room
     */
    public void destroyGame();

}
