package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;

public class InitialPlayerRequestMessage extends NetworkMessagesToClient {
    private ArrayList<Position> validPositions;

    @Override
    public void doAction(AbstractView v) {
        v.requestInitialPositioning(validPositions);
    }

    public InitialPlayerRequestMessage(ArrayList<Position> validPositions) {
        this.validPositions = (ArrayList<Position>) validPositions.clone();
    }
}
