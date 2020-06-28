package it.polimi.ingsw.PSP48.server.model.exceptions;

/**
 * Exception thrown when some action is blocked because the destination cell is occupied
 */
public class OccupiedCellException extends Exception {
    public OccupiedCellException(String s) {
        super(s);
    }
}
