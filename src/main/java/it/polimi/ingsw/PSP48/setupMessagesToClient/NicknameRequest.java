package it.polimi.ingsw.PSP48.setupMessagesToClient;

import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;

/**
 * Message used to request the client to send the chosen nickname
 */
public class NicknameRequest implements ClientSetupMessages {
    private final String result;

    /**
     * Initializes the network message
     *
     * @param result the result of nickname processing
     */
    public NicknameRequest(String result) {
        this.result = result;
    }

    /**
     * Invokes the handling of nickname request result
     *
     * @param action the ClientNetworkObserver on which the method must be invoked
     */
    @Override
    public void doAction(ClientNetworkObserver action) {
        action.requestNicknameSend(result);
    }
}
