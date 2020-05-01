package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.AbstractView;

public class WinMessage extends NetworkMessagesToClient {
    @Override
    public void doAction(AbstractView v) {
        v.declareWin();
    }
}
