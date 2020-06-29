package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;

/**
 * A network message sent to the client to requests the selection of the first player to the challenger. It contains the list of players in game.
 */
public class InitialPlayerRequestMessage extends NetworkMessagesToClient {
    private ArrayList<String> players;

    @Override
    public void doAction(ViewInterface v) {
        v.requestInitialPlayerSelection(players);
    }

    /**
     * * Initializes the network message
     *
     * @param players the players in game
     */
    public InitialPlayerRequestMessage(ArrayList<String> players) {
        this.players = (ArrayList<String>) players.clone();
    }
}
