package it.polimi.ingsw.PSP48.setupMessagesToClient;

import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;

import java.util.function.Consumer;

public class nicknameRequest implements ClientSetupMessages {
    private String feffe;

    public nicknameRequest(String feffe) {
        this.feffe = feffe;
    }

    @Override
    public void doAction(ClientNetworkObserver action) {
        action.requestNicknameSend(feffe);
    }
}
