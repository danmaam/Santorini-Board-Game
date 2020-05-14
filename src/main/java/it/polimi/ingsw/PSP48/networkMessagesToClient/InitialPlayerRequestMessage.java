package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;

public class InitialPlayerRequestMessage extends NetworkMessagesToClient {
    private ArrayList<String> players;

    @Override
    public void doAction(ViewInterface v) {
        v.requestInitialPlayerSelection(players);
    }

    public InitialPlayerRequestMessage(ArrayList<String> players) {
        this.players = (ArrayList<String>) players.clone();
    }
}
