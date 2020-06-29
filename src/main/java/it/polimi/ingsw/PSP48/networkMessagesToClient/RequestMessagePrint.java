package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;

/**
 * Network messages that requests the client to show at video a message. It contains the message to be printed
 */
public class RequestMessagePrint extends NetworkMessagesToClient {

    private String messageToPrint;

    /**
     * Invokes the message print
     *
     * @param v the view interface where the method must be invoked
     */
    public void doAction(ViewInterface v) {
        v.printMessage(messageToPrint);
    }

    /**
     * Initializes the network message
     *
     * @param message the nessage to be shown
     */
    public RequestMessagePrint(String message) {
        messageToPrint = message;
    }


}
