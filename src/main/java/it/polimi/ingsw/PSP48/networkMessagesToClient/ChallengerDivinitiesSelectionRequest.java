package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.ViewInterface;

import java.util.ArrayList;

/**
 * Network message used to request the challenger to choose the available divinities in the game.
 * It contains the divinities available for the specific type of game in progress;
 */
public class ChallengerDivinitiesSelectionRequest extends NetworkMessagesToClient {
    private ArrayList<DivinitiesWithDescription> div;
    private int playerNumber;

    /**
     * Invokes the request of challenger's divinities selection
     *
     * @param v the view interface where the method must be invoked
     */
    @Override
    public void doAction(ViewInterface v) {
        v.requestChallengerDivinitiesSelection(div, playerNumber);
    }

    /**
     * Initializes the network message
     *
     * @param div          the available divinities
     * @param playerNumber the number of players
     */
    public ChallengerDivinitiesSelectionRequest(ArrayList<DivinitiesWithDescription> div, int playerNumber) {
        this.div = (ArrayList<DivinitiesWithDescription>) div.clone();
        this.playerNumber = playerNumber;
    }
}
