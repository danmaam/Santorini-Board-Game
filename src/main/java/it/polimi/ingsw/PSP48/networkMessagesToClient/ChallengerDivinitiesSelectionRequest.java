package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.ViewInterface;

import java.util.ArrayList;

public class ChallengerDivinitiesSelectionRequest extends NetworkMessagesToClient {
    private ArrayList<DivinitiesWithDescription> div;
    private int playerNumber;

    @Override
    public void doAction(ViewInterface v) {
        v.requestChallengerDivinitiesSelection(div, playerNumber);
    }

    public ChallengerDivinitiesSelectionRequest(ArrayList<DivinitiesWithDescription> div, int playerNumber) {
        this.div = (ArrayList<DivinitiesWithDescription>) div.clone();
        this.playerNumber = playerNumber;
    }
}
