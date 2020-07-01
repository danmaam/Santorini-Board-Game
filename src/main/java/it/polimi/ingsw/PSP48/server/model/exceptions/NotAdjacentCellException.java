package it.polimi.ingsw.PSP48.server.model.exceptions;

/**
 * Exception thrown when some action is blocked because the destination cell isn't adjacent to the worker cell
 */
public class NotAdjacentCellException extends Exception {
    /**
     * Initializes the exception
     *
     * @param s the reason why the exception is thrown
     */
    public NotAdjacentCellException(String s) {
        super(s);
    }

}
