package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.ViewInterface;

import java.util.ArrayList;

/**
 * Network message used to request the player to choose his divinity. It contains the available divinities.
 */
public class DivinitySelectionRequest extends NetworkMessagesToClient {
    private ArrayList<DivinitiesWithDescription> availableDivinities;

    /**
     * Invokes the request of divinity selection
     *
     * @param v the view interface where the method must be invoked
     */
    @Override
    public void doAction(ViewInterface v) {
        v.requestDivinitySelection(availableDivinities);
    }

    /**
     * * Initializes the network message
     *
     * @param availableDivinities the available divinities
     */
    public DivinitySelectionRequest(ArrayList<DivinitiesWithDescription> availableDivinities) {
        this.availableDivinities = (ArrayList<DivinitiesWithDescription>) availableDivinities.clone();
    }
}
