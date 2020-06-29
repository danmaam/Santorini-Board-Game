package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.WorkerValidCells;

import java.util.ArrayList;

/**
 * Network messages sent to client to request the player to complete an optional build action, or to skip it. It contains the coordinates of possible
 * construction action
 */
public class RequestBuild extends NetworkMessagesToClient {
    private ArrayList<WorkerValidCells> build;
    private ArrayList<WorkerValidCells> dome;

    /**
     * Initializes the network message
     *
     * @param build the association valid worker-valid cells to build
     * @param dome  the association valid workers-valid cells to put a dome
     */
    public RequestBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {
        this.build = (ArrayList<WorkerValidCells>) build.clone();
        this.dome = (ArrayList<WorkerValidCells>) dome.clone();
    }

    /**
     * Invokes the request of a construction action
     *
     * @param v the view interface where the method must be invoked
     */
    @Override
    public void doAction(ViewInterface v) {
        v.requestDomeOrBuild(build, dome);
    }
}
