package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.AbstractView;

public class LoseMessage extends NetworkMessagesToClient {
    @Override
    public void doAction(AbstractView v) {
        v.declareLose();
    }
}
