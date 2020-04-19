package it.polimi.ingsw.PSP48.server.model.virtualViewObservers;

import it.polimi.ingsw.PSP48.server.Observer;

public class cellUpdateObserver implements Observer {
    /**
     * @param o   recieves an arraylist of cells changed in the turn. the observer must create an event in each player
     *            client handler to send the changes
     * @param arg must be edited_cells to do something
     * @author Daniele Mammone
     */
    @Override
    public void update(Object o, String arg) {
        if (arg.equals("edited_cells")) {

        }
    }
}
