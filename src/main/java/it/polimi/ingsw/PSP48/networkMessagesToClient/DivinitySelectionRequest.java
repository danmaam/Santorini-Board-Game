package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.DivinitiesWithDescription;

import java.util.ArrayList;

public class DivinitySelectionRequest extends NetworkMessagesToClient {
    private ArrayList<DivinitiesWithDescription> availableDivinities;

    @Override
    public void doAction(AbstractView v) {
        v.requestDivinitySelection(availableDivinities);
    }

    public DivinitySelectionRequest(ArrayList<DivinitiesWithDescription> availableDivinities) {
        this.availableDivinities = (ArrayList<DivinitiesWithDescription>) availableDivinities.clone();
    }
}
