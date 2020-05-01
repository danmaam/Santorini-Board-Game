package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.DivinitiesWithDescription;

import java.util.ArrayList;

public class ChallengerDivinitiesSelectionRequest extends NetworkMessagesToClient {
    private ArrayList<DivinitiesWithDescription> div;
    private int playerNumber;

    @Override
    public void doAction(AbstractView v) {
        v.requestChallengerDivinitiesSelection(div, playerNumber);
    }

    public ChallengerDivinitiesSelectionRequest(ArrayList<DivinitiesWithDescription> div, int playerNumber) {
        this.div = (ArrayList<DivinitiesWithDescription>) div.clone();
        this.playerNumber = playerNumber;
    }
}
