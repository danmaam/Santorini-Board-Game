package it.polimi.ingsw.PSP48.setupMessagesToClient;

import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;

public class GameModeRequest implements ClientSetupMessages {
    private String feffe;

    public GameModeRequest(String feffe) {
        this.feffe = feffe;
    }

    @Override
    public void doAction(ClientNetworkObserver action) {
        action.requestGameModeSend(feffe);
    }


}
