package it.polimi.ingsw.PSP48.server.model.exceptions;

/**
 * Exception thrown when some action is blocked because the action will block
 * the player from completing the turn
 */
public class NoTurnEndException extends Exception {
    /**
     * Initializes the exception
     *
     * @param message the reason why the exception is thrown
     */
    public NoTurnEndException(String message) {
        super(message);
    }
}
