package it.polimi.ingsw.PSP48.client;

import it.polimi.ingsw.PSP48.server.model.Cell;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * class that contains both methods to update the status of the client and to interact with the players
 */
public abstract class View extends ViewInterface implements ModelObserver
{
    private ArrayList<ViewObserver> observers;

    public abstract void changedBoard(ArrayList<Cell> newCells);
    public abstract void changedPlayerList(ArrayList<String> newPlayerList);

    public abstract void requestMove(ArrayList<WorkerValidCells> validCellsForMove);
    public abstract void requestDomeOrBuild(ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome);
    public abstract void declareWin();
    public abstract void declareLose();
    public abstract void requestColourSelection(ArrayList<String> availableColours);
    public abstract void requestDivinityList(ArrayList<String> availableDivinities);
    public abstract void requestDivinitySelection(ArrayList<String> availableDivinities);
    public abstract void requestInitialPositioning(ArrayList<WorkerValidCells> validForInitialPositioning);

    public abstract void registerObserver(ViewObserver obv);
    public abstract void unregisterObserver(ViewObserver obv);
    public abstract void notifyObservers(Consumer<ViewObserver> lambda);
}
