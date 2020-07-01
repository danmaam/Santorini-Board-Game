package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;

import java.util.ArrayList;

/**
 * Messages containing a new player-divinity association list
 */
public class UpdatedPlayerList extends NetworkMessagesToClient {
    /**
     * Network messages sent to client containing the new player list. Requests the client to update the local player list
     */
    private final ArrayList<String> newPlayerList;

    /**
     * Invokes the update of the local player list
     *
     * @param v the view interface where the method must be invoked
     */
    @Override
    public void doAction(ViewInterface v) {
        v.changedPlayerList(newPlayerList);
    }

    /**
     * Initializes the network message
     *
     * @param newPlayerList the updated player list
     */
    public UpdatedPlayerList(ArrayList<String> newPlayerList) {
        this.newPlayerList = (ArrayList<String>) newPlayerList.clone();
    }
}
