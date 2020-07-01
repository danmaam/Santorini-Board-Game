package it.polimi.ingsw.PSP48.server.model.exceptions;

/**
 * Exception thrown when some action is blocked because the destination cell is at the maximum level
 */
public class MaximumLevelReachedException extends Exception {
    /**
     * Initializes the exception
     *
     * @param s the reason why the exception is thrown
     */
    public MaximumLevelReachedException(String s) {
        super(s);
    }
}
