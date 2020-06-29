package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

import java.util.ArrayList;

/**
 * Network messages that notifies the divinities chosen by the challenger
 */
public class ChallengerDivinitiesMessage extends NetworkMessagesToServer {
    private final ArrayList<String> chosenDivinities;

    /**
     * Initializes the network message
     *
     * @param m the divinities chosen by the challenger
     */
    public ChallengerDivinitiesMessage(ArrayList<String> m) {
        chosenDivinities = (ArrayList<String>) m.clone();
    }

    /**
     * Invokes the processing of divinities chosen by the challenger
     *
     * @param obv the ServerNetworkObserver where the method must be invoked
     */
    @Override
    public void doThings(ServerNetworkObserver obv) {
        obv.selectAvailableDivinities(chosenDivinities);
    }
}
