package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;

public class PositioningRequest extends NetworkMessagesToClient {
    private ArrayList<Position> validPositions;

    public PositioningRequest(ArrayList<Position> validPositions) {
        this.validPositions = validPositions;
    }

    @Override
    public void doAction(AbstractView v) {
        v.requestInitialPositioning(validPositions);
    }
}
