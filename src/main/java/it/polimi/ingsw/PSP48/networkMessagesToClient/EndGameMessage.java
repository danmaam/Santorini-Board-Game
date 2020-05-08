package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;

public class EndGameMessage extends NetworkMessagesToClient {

    private String message;

    public EndGameMessage(String message) {
        this.message = message;
    }

    @Override
    public void doAction(ViewInterface v) {
        v.endgame(message);
    }
}
