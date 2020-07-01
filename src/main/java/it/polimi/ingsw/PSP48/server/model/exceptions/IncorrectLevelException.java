package it.polimi.ingsw.PSP48.server.model.exceptions;

/**
 * Exception thrown when a move isn't allowed due to a wrong level
 */
public class IncorrectLevelException extends Exception {
    /**
     * Initializes the exception
     *
     * @param s the reason why the exception is thrown
     */
    public IncorrectLevelException(String s) {
        super(s);
    }
}
