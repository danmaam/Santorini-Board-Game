package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.AbstractView;

public class EndGameMessage extends NetworkMessagesToClient {

    private String message;

    public EndGameMessage(String message) {
        this.message = message;
    }

    @Override
    public void doAction(AbstractView v) {
        v.endgame(message);
    }
}
