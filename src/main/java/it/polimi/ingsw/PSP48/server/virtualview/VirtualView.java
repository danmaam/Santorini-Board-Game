package it.polimi.ingsw.PSP48.server.virtualview;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.server.networkmanager.ClientHandler;
import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;

import java.util.ArrayList;

/**
 * class used to implements a model observer as view
 */
public class VirtualView extends AbstractView implements ServerNetworkObserver {

    ClientHandler playerHandler;

    public VirtualView(ClientHandler p) {
        playerHandler = p;
    }

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
        playerHandler.requestMessageSend(null);
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
            notifyObserver((ViewObserver c) -> c.move(mc));
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
    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {

    }


    @Override
    public void move(MoveCoordinates p) {
        notifyObserver(c -> c.move(p));
    }

    @Override
    public void build(MoveCoordinates p) {
        notifyObserver(c -> c.build(p));
    }

    @Override
    public void dome(MoveCoordinates p) {
        notifyObserver(c -> c.dome(p));
    }

    @Override
    public void putWorkerOnTable(Position p) {
        notifyObserver(c -> c.putWorkerOnTable(p));
    }

    @Override
    public void registerPlayerDivinity(String divinity) {
        notifyObserver(c -> c.registerPlayerDivinity(divinity));
    }

    @Override
    public void selectAvailableDivinities(ArrayList<String> divinities) {
        notifyObserver(c -> c.selectAvailableDivinities(divinities));
    }
}
