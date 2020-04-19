package it.polimi.ingsw.PSP48.server.virtualview.controllerObservers;

import it.polimi.ingsw.PSP48.server.Observer;

public class buildObservers implements Observer {
    /**
     * @param o   the next position for the build or doming
     * @param arg "dome" or "build"
     * @author Daniele Mammone
     */
    @Override
    public void update(Object o, String arg) {
        if (arg.equals("build")) {

        } else if (arg.equals("dome")) {

        }
    }
}
