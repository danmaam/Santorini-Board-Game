package it.polimi.ingsw.PSP48.server.model.exceptions;

/**
 * Exception thrown when a move isn't allowed due to another divinity power
 */
public class DivinityPowerException extends Exception {
    public DivinityPowerException(String s) {
        super(s);
    }
}
