package it.polimi.ingsw.PSP48.networkMessagesToServer;

import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

import java.util.ArrayList;

public class ChallengerDivinitiesMessage extends NetworkMessagesToServer {
    private final ArrayList<String> chosenDivinities;

    public ChallengerDivinitiesMessage(ArrayList<String> m) {
        chosenDivinities = (ArrayList<String>) m.clone();
    }

    @Override
    public void doThings(ServerNetworkObserver obv) {
        obv.selectAvailableDivinities(chosenDivinities);
    }
}
