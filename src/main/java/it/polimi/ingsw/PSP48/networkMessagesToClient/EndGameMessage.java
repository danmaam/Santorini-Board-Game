package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;

/**
 * Network message that notifies the client that the game ended. It contains the reason of the end of the game
 */
public class EndGameMessage extends NetworkMessagesToClient {

    private final String message;

    /**
     * Initializes the network message
     *
     * @param message the message of end game
     */
    public EndGameMessage(String message) {
        this.message = message;
    }

    /**
     * Invokes the handling of a game end
     *
     * @param v the view interface where the method must be invoked
     */
    @Override
    public void doAction(ViewInterface v) {
        v.endgame(message);
    }
}
