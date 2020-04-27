package it.polimi.ingsw.PSP48.client;

import java.util.ArrayList;

/**
 * class containing the methods that will be used to interact with the players and communicate changes to the server
 */
public abstract class ViewInterface
{
    //public abstract void requestMove(ArrayList<WorkerValidCells> validCellsForMove);
    //public abstract void requestDomeOrBuild(ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome);
    public abstract void declareWin();
    public abstract void declareLose();
    public abstract void requestColourSelection(ArrayList<String> availableColours);
    public abstract void requestDivinityList(ArrayList<String> availableDivinities);
    public abstract void requestDivinitySelection(ArrayList<String> availableDivinities);
    //public abstract void requestInitialPositioning(ArrayList<WorkerValidCells> validForInitialPositioning);
}
