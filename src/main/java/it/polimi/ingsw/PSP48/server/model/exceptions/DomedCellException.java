package it.polimi.ingsw.PSP48.server.model.exceptions;

/**
 * Exception thrown when an action is blocked because the destination cell contains a dome
 */
public class DomedCellException extends Exception {
    public DomedCellException(String s) {
        super(s);
    }
}
