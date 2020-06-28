package it.polimi.ingsw.PSP48.observers;

/**
 * An observer of server incoming messages handler
 */
public interface ServerNetworkObserver extends ViewObserver {

    /**
     * Requests the observer to destroy a game room
     */
    void destroyGame();

}
