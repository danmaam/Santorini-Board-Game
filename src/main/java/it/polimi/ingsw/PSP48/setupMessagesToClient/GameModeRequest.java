package it.polimi.ingsw.PSP48.setupMessagesToClient;

import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;

/**
 * Notifies the client of the result of the processing of the sent game mode
 */
public class GameModeRequest implements ClientSetupMessages {
    private final String result;

    /**
     * Initializes the network message
     *
     * @param result the result of game mode processing
     */
    public GameModeRequest(String result) {
        this.result = result;
    }

    @Override
    public void doAction(ClientNetworkObserver action) {
        action.requestGameModeSend(result);
    }


}
