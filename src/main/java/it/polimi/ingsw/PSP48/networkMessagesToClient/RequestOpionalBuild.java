package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.WorkerValidCells;

import java.util.ArrayList;

/**
 * Network messages sent to client to request the player to complete an optional build action, or to skip it. It contains the coordinates of possiblee
 * construction action
 */
public class RequestOpionalBuild extends NetworkMessagesToClient {
    private final ArrayList<WorkerValidCells> build;
    private final ArrayList<WorkerValidCells> dome;


    /**
     * Initializes the network message
     *
     * @param build the association valid workers-valid cells where they can build
     * @param dome  the association valid workers-valid cells where they can put a dome
     */
    public RequestOpionalBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {
        this.build = (ArrayList<WorkerValidCells>) build.clone();
        this.dome = (ArrayList<WorkerValidCells>) dome.clone();
    }

    /**
     * Invokes the request of an optional construction action
     *
     * @param v the view interface where the method must be invoked
     */
    @Override
    public void doAction(ViewInterface v) {
        v.requestOptionalBuild(build, dome);
    }
}
