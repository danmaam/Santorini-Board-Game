package it.polimi.ingsw.PSP48.server.virtualview;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.server.Server;
import it.polimi.ingsw.PSP48.server.networkmanager.ClientHandler;
import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;
import it.polimi.ingsw.PSP48.server.networkmanager.ClientHandlerListener;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * class used to implements a model observer as view
 */
public class VirtualView extends AbstractView implements ServerNetworkObserver {

    private String playerName;
    ClientHandler playerHandler;
    ClientHandlerListener playerListener;

    public VirtualView(ClientHandler p, ClientHandlerListener l) {
        playerHandler = p;
        playerListener = l;
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
        System.out.println("invoking network handler");
        playerHandler.requestChallengerDivinitiesSelection(div, playerNumber);
    }


    @Override
    public void printMessage(String s) {
        playerHandler.requestMessageSend(s);
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

    @Override
    public void processNickname(String nickname) {
        String nextMessage;
        try {
            Server.addNickname(nickname);
            playerName = nickname;
            nextMessage = "Valid Nickname. Welcome to the game";
            playerListener.nicknameSet(true);
        } catch (IllegalArgumentException e) {
            nextMessage = "Invalid nickname. Retry";
        }

        playerHandler.setUpMessage(nextMessage);

    }

    @Override
    public void processGameMode(String gameMode) {
        int playerNumber = 0;
        Calendar c = null;
        String nextMessage = null;
        boolean divinities = false;
        String[] data;
        String mode = gameMode.split(" ")[0];
        if (!(mode.equals("3ND") || mode.equals("2ND") || mode.equals("3D") || mode.equals("2D")))
            nextMessage = "Not valid mode. Retry";

        else if (mode.equals("3ND")) {
            if (gameMode.split(" ").length == 1) {
                nextMessage = "Missing Birthday. Retry";
            } else {
                playerNumber = 3;
                divinities = false;
                c = Calendar.getInstance();
                data = gameMode.split(" ")[1].split("-");
                c.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]), Integer.parseInt(data[0]));
                nextMessage = "You're in Game Room now! 3 Players, without divinities. The game will begin soon";
            }
        } else if (mode.equals("2ND")) {
            if (gameMode.split(" ").length == 1) {
                nextMessage = "Missing Birthday. Retry";
            } else {
                playerNumber = 2;
                divinities = false;
                c = Calendar.getInstance();
                data = gameMode.split(" ")[1].split("-");
                c.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]), Integer.parseInt(data[0]));
                nextMessage = "You're in Game Room now! 2 Players, without divinities. The game will begin soon";
            }
        } else if (mode.equals("3D")) {
            playerNumber = 3;
            divinities = true;
            nextMessage = "You are in the game room! 3 players with divinities. The game will begin soon";
        } else {
            playerNumber = 2;
            divinities = true;
            nextMessage = "You are in the game room! 2 players with divinities. The game will begin soon";
        }
        playerHandler.setUpMessage(nextMessage);
        if (!nextMessage.equals("Missing Birthday. Retry") && !nextMessage.equals("Not valid mode. Retry")) {
            playerListener.setGameMode(true);
            Server.insertPlayerInGameRoom(playerNumber, divinities, playerName, c, this);
        }
    }
}
