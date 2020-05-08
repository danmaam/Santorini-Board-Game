package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;

public class requestMessagePrint extends NetworkMessagesToClient {

    private String messageToPrint;

    public void doAction(ViewInterface v) {
        v.printMessage(messageToPrint);
    }

    public requestMessagePrint(String message) {
        messageToPrint = message;
    }


}
