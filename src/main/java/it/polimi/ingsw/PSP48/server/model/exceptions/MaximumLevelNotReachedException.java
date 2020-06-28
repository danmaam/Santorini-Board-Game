package it.polimi.ingsw.PSP48.server.model.exceptions;

/**
 * Exception thrown when some action is blocked because the destination cell isn't at the maximum level
 */
public class MaximumLevelNotReachedException extends Exception {
    public MaximumLevelNotReachedException (String s) {
        super(s);
    }
}
