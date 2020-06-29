package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;

import java.io.Serializable;

/**
 * A network message to the client. It contains a method that must be invoked on the client when the message arrives and is processed;
 */
public abstract class NetworkMessagesToClient implements Serializable {

    /**
     * Invokes some method on an object implementing a ViewInterface
     *
     * @param v the view interface where the method must be invoked
     */
    public abstract void doAction(ViewInterface v);

}
