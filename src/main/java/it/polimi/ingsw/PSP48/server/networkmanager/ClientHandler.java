package it.polimi.ingsw.PSP48.server.networkmanager;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.networkMessagesToClient.*;
import it.polimi.ingsw.PSP48.server.Server;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.virtualview.VirtualView;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles the connection of a player
 */
public class ClientHandler implements Runnable {

    private enum nextAction {
        requestAction, setupmessage, closegame;
    }

    private nextAction toDO = null;
    Object toDOLOCK = new Object();

    public ClientHandler(Socket client) {
        this.client = client;
    }

    ObjectOutputStream output;

    private ClientHandlerListener incomingMessagesHandler;
    private String playerDatas = null;
    private String setUpMessage;
    private NetworkMessagesToClient nextObject;
    private Thread listenerThread;


    private final Socket client;

    @Override
    public void run() {

        incomingMessagesHandler = new ClientHandlerListener(client);
        VirtualView playerVirtualView = new VirtualView(this, incomingMessagesHandler);
        incomingMessagesHandler.registerObserver(playerVirtualView);
        listenerThread = new Thread(incomingMessagesHandler);
        listenerThread.start();
        try {
            handleGamePhases();
        } catch (IOException e) {

        }

        System.out.println("Disconnected from " + client.getInetAddress());
        if (playerDatas != null) Server.removeNickname(playerDatas);
    }

    private void handleGamePhases() throws IOException {
        output = new ObjectOutputStream(client.getOutputStream());
        System.out.println("Connected to " + client.getInetAddress());
        while (true) {
            synchronized (toDOLOCK) {
                while (toDO == null) {
                    try {
                        toDOLOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                switch (toDO) {
                    case requestAction:
                        output.writeObject(nextObject);
                        toDO = null;
                        break;
                    case setupmessage:
                        output.writeObject(setUpMessage);
                        toDO = null;
                        break;
                    case closegame:
                        output.writeObject(nextObject);
                        incomingMessagesHandler.setClosed();
                        client.close();
                        return;
                }
            }
        }
    }


    public void requestMessageSend(String lambda) {
        synchronized (toDOLOCK) {
            toDO = nextAction.requestAction;
            nextObject = new requestMessagePrint(lambda);
            toDOLOCK.notifyAll();
        }
    }

    public void requestInitialPlayerSelection(ArrayList<String> players) {
        System.out.println("Sending request for initial player selection");
        synchronized (toDOLOCK) {
            nextObject = new requestForMoveAction(players);
            toDO = nextAction.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    public void requestInitialPositioning(ArrayList<Position> validCells) {
        System.out.println("Sending request for Initial Positioning");
        synchronized (toDOLOCK) {
            System.out.println("requesting initial positioning");
            nextObject = new PositioningRequest(validCells);
            toDO = nextAction.requestAction;
            toDOLOCK.notifyAll();
        }

    }

    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber) {
        synchronized (toDOLOCK) {
            System.out.println("instantiating divinity list message, and requesting it's send");
            nextObject = new ChallengerDivinitiesSelectionRequest(div, playerNumber);
            toDO = nextAction.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    public void requestOptionalMove(ArrayList<WorkerValidCells> validCellsForMove) {
        System.out.println("sending an optional move request");
        synchronized (toDOLOCK) {
            nextObject = new OptionalMoveRequest(validCellsForMove);
            toDO = nextAction.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    public void requestOptionalBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {
        synchronized (toDOLOCK) {
            nextObject = new RequestOpionalBuild(build, dome);
            toDO = nextAction.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    public void changedBoard(ArrayList<Cell> newCells) {
        System.out.println("Sending changed board");
        synchronized (toDOLOCK) {
            nextObject = new ChangedBoard(newCells);
            toDO = nextAction.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    public void changedPlayerList(ArrayList<String> newPlayerList) {
        synchronized (toDOLOCK) {
            System.out.println("sending new player list");
            nextObject = new UpdatedPlayerList(newPlayerList);
            toDO = nextAction.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove) {
        System.out.println("Sending move request");
        synchronized (toDOLOCK) {
            nextObject = new RequestMove(validCellsForMove);
            toDO = nextAction.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    public synchronized void requestBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {
        System.out.println("Sending build request");
        synchronized (toDOLOCK) {
            nextObject = new RequestBuild(build, dome);
            toDO = nextAction.requestAction;
            toDOLOCK.notifyAll();
        }
    }


    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {
        System.out.println("Sending request for divinity selection");
        synchronized (toDOLOCK) {
            nextObject = new DivinitySelectionRequest(availableDivinities);
            toDO = nextAction.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    public void setUpMessage(String message) {
        System.out.println("Sending a setup message");
        synchronized (toDOLOCK) {
            setUpMessage = message;
            toDO = nextAction.setupmessage;
            toDOLOCK.notifyAll();
        }
    }

    public void gameEndMessage(String message) {
        synchronized (toDOLOCK) {
            nextObject = new EndGameMessage(message);
            toDO = nextAction.closegame;
            toDOLOCK.notifyAll();
        }
    }


}
