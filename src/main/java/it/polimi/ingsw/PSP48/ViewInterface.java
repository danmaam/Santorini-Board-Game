package it.polimi.ingsw.PSP48;

import it.polimi.ingsw.PSP48.observers.ModelObserver;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;
import java.util.function.Consumer;

public interface ViewInterface extends ModelObserver {
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove);

    public void requestDomeOrBuild(ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome);

    public void endgame(String messageOfEndGame);

    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities);

    public void requestInitialPlayerSelection(ArrayList<String> players);

    public void requestInitialPositioning(ArrayList<Position> validCells);

    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber);

    public void printMessage(String s);

    public void requestOptionalMove(ArrayList<WorkerValidCells> validCellsForMove);

    public void requestOptionalBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome);

    public void registerObserver(ViewObserver obv);

    public void unregisterObserver(ViewObserver obv);

    public void notifyObserver(Consumer<ViewObserver> lambda);

}
