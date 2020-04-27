package it.polimi.ingsw.PSP48.server.virtualview;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.server.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;

/**
 * class used to implements a model observer as view
 */
public class VirtualView extends AbstractView {


    @Override
    public void requestInitialPlayerSelection(ArrayList<String> players) {

    }

    @Override
    public void requestInitialPositioning(ArrayList<Position> validCells) {

    }

    @Override
    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber) {

    }


    @Override
    public void printMessage(String s) {

    }

    @Override
    public void requestOptionalMove(WorkerValidCells validCellsForMove) {

    }

    @Override
    public void requestOptionalBuild(WorkerValidCells build, WorkerValidCells dome) {

    }

    @Override
    public void changedBoard(ArrayList<Cell> newCells) {
        //cose da far fare al network handler
    }

    @Override
    public void changedPlayerList(ArrayList<String> newPlayerList) {
        //cose che deve fare il network handler
    }

    @Override
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove) {
        //cose che deve fare il network handler
        //dopo aver ricevuto la mossa, notifico al controller la ricezione di questa
        //placeholder per scrivere il codice di connessione al controller
        MoveCoordinates mc = new MoveCoordinates(2, 2, 4, 4);
        try {
            notifyObserver((ViewObserver c, Object o) -> {
                MoveCoordinates p = (MoveCoordinates) o;
                c.move(p);
            }, mc);
        } catch (Exception e) {
            System.out.println("Errore");
        }
    }

    @Override
    public void requestDomeOrBuild(ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome) {
        //cose da far fare al network handler
    }

    @Override
    public void declareWin() {
        //cose da far fare al network handler
    }

    @Override
    public void declareLose() {
        //cose da far fare al network handler
    }

    @Override
    public void requestColourSelection(ArrayList<String> availableColours) {
        //cose da far fare al network handler
    }

    @Override
    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {

    }


}
