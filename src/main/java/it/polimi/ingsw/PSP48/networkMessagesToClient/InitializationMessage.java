package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;

public class InitializationMessage extends NetworkMessagesToClient {
    private String result;

    public InitializationMessage(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    @Override
    public Boolean initializationMessage() {
        return true;
    }

    @Override
    public void doAction(ViewInterface v) {

    }
}
