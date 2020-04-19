package it.polimi.ingsw.PSP48.server.model.virtualViewObservers;

import it.polimi.ingsw.PSP48.server.Observer;

public class playerListChanged implements Observer {
    /**
     * @param o   receive an array list of objects with an association between each player, their colours and divinities.
     * @param arg "player_list_change"
     * @author Daniele Mammone
     */
    @Override
    public void update(Object o, String arg) {
        if (arg.equals("player_list_change")) {
            //communicates with players network handler
        }
    }
}
