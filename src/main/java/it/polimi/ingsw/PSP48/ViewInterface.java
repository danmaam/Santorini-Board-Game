package it.polimi.ingsw.PSP48;

import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;

public interface ViewInterface {
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove);

    public void requestDomeOrBuild(ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome);

    public void declareWin();

    public void declareLose();

    public void requestColourSelection(ArrayList<String> availableColours);

    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities);

    public void requestInitialPlayerSelection(ArrayList<String> players);

    public void requestInitialPositioning(ArrayList<Position> validCells);

    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber);

    public void printMessage(String s);

    public void requestOptionalMove(WorkerValidCells validCellsForMove);

    public void requestOptionalBuild(WorkerValidCells build, WorkerValidCells dome);
}
