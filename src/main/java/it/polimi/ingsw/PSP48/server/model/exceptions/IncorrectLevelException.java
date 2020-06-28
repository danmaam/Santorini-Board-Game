package it.polimi.ingsw.PSP48.server.model.exceptions;

/**
 * Exception thrown when a move isn't allowed due to a wrong level
 */
public class IncorrectLevelException extends Exception {
    public IncorrectLevelException(String s) {
        super(s);
    }
}
