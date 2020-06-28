package it.polimi.ingsw.PSP48.server.model.exceptions;

/**
 * Exception thrown when some action is blocked because the destination cell is at the maximum level
 */
public class MaximumLevelReachedException extends Exception {
    public MaximumLevelReachedException(String s) {
        super(s);
    }
}
