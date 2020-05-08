package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;

import java.util.ArrayList;

public class requestForMoveAction extends NetworkMessagesToClient {
    private ArrayList<String> players;

    public requestForMoveAction(ArrayList<String> s) {
        players = (ArrayList<String>) s.clone();
    }

    @Override
    public void doAction(ViewInterface v) {
        v.requestInitialPlayerSelection(players);
    }
}
