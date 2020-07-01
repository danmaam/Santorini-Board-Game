package it.polimi.ingsw.PSP48.server.model.exceptions;

/**
 * Exception thrown when some action is blocked because the destination cell isn't at the maximum level
 */
public class MaximumLevelNotReachedException extends Exception {
    /**
     * Initializes the exception
     *
     * @param s the reason why the exception is thrown
     */
    public MaximumLevelNotReachedException(String s) {
        super(s);
    }
}
