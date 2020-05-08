package it.polimi.ingsw.PSP48.networkMessagesToClient;

import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.WorkerValidCells;

import java.util.ArrayList;

public class RequestOpionalBuild extends NetworkMessagesToClient {
    private ArrayList<WorkerValidCells> build;
    private ArrayList<WorkerValidCells> dome;

    public RequestOpionalBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {
        this.build = (ArrayList<WorkerValidCells>) build.clone();
        this.dome = (ArrayList<WorkerValidCells>) dome.clone();
    }

    @Override
    public void doAction(ViewInterface v) {
        v.requestOptionalBuild(build, dome);
    }
}
