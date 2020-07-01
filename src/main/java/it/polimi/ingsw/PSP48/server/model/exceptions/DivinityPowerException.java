package it.polimi.ingsw.PSP48.server.model.exceptions;

/**
 * Exception thrown when a move isn't allowed due to another divinity power
 */
public class DivinityPowerException extends Exception {
    /**
     * Initializes the exception
     *
     * @param s the reason why the exception is thrown
     */
    public DivinityPowerException(String s) {
        super(s);
    }
}
