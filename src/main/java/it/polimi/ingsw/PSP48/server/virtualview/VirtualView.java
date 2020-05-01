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
        playerHandler.requestInitialPlayerSelection(players);
    }

    @Override
    public void requestInitialPositioning(ArrayList<Position> validCells) {
        playerHandler.requestInitialPositioning(validCells);
    }

    @Override
    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber) {
        playerHandler.requestChallengerDivinitiesSelection(div, playerNumber);
    }


    @Override
    public void printMessage(String s) {
        playerHandler.requestMessageSend(null);
    }

    @Override
    public void requestOptionalMove(ArrayList<WorkerValidCells> validCellsForMove) {
        playerHandler.requestOptionalMove(validCellsForMove);
    }

    @Override
    public void requestOptionalBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {
        playerHandler.requestOptionalBuild(build, dome);
    }

    @Override
    public void changedBoard(ArrayList<Cell> newCells) {
        playerHandler.changedBoard(newCells);
    }

    @Override
    public void changedPlayerList(ArrayList<String> newPlayerList) {
        playerHandler.changedPlayerList(newPlayerList);
    }

    @Override
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove) {
        playerHandler.requestMove(validCellsForMove);
    }

    @Override
    public void requestDomeOrBuild(ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome) {
        playerHandler.requestBuild(validForBuild, validForDome);
    }

    @Override
    public void declareWin() {
        playerHandler.declareWin();
    }

    @Override
    public void declareLose() {
        playerHandler.declareLose();
    }


    @Override
    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {
        playerHandler.requestDivinitySelection(availableDivinities);
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

    @Override
    public void firstPlayerRegistration(String player) {
        notifyObserver(c -> c.selectFirstPlayer(player));
    }
}
