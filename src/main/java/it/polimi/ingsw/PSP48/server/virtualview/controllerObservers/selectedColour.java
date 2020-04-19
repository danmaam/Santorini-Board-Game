package it.polimi.ingsw.PSP48.server.virtualview.controllerObservers;

import it.polimi.ingsw.PSP48.server.Observer;

public class selectedColour implements Observer {
    /**
     * @param o   the colour selected by the players
     * @param arg "selected_colour"
     */
    @Override
    public void update(Object o, String arg) {
        if (arg.equals("selected_colour")) {

        }
    }
}
