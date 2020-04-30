package it.polimi.ingsw.PSP48.server.networkmanager;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.networkMessagesToClient.NetworkMessagesToClient;
import it.polimi.ingsw.PSP48.networkMessagesToClient.requestMessagePrint;
import it.polimi.ingsw.PSP48.server.Server;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.virtualview.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.function.Consumer;

/**
 * Handles the connection of a player
 */
public class ClientHandler implements Runnable {

    private enum nextAction {
        requestAction;
    }

    private nextAction toDO;


    public ClientHandler(Socket client) {
        this.client = client;
    }

    ObjectOutputStream output;
    ObjectInputStream input;

    private String playerDatas = null;
    private String nextMessage;
    private String playerName;
    private NetworkMessagesToClient nextObject;


    private final Socket client;
    private VirtualView playerVirtualView;

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(client.getInputStream());
            output = new ObjectOutputStream(client.getOutputStream());
            System.out.println("Connected to " + client.getInetAddress());

            boolean done = false;

            while (!done) {
                try {
                    playerDatas = (String) input.readObject();
                    String nextMessage;
                    try {
                        Server.addNickname(playerDatas);
                        playerName = playerDatas;
                        done = true;
                        nextMessage = "Valid Nickname. Welcome to the game";
                    } catch (IllegalArgumentException e) {
                        nextMessage = "Invalid nickname. Retry";
                    }
                    output.writeObject(nextMessage);
                } catch (ClassNotFoundException e) {
                    System.out.println("Error from the client");
                }
            }

            //at this point, the player has logged to the server, and he must choose the preferred game mode

            done = false;
            nextMessage = null;
            Calendar c = Calendar.getInstance();
            String[] data;
            while (!done) {
                try {
                    playerDatas = (String) input.readObject();
                    String mode = playerDatas.split(" ")[0];
                    if (!(mode.equals("3ND") || mode.equals("2ND") || mode.equals("3D") || mode.equals("2D")))
                        nextMessage = "Not valid mode. Retry";
                    else playerVirtualView = new VirtualView(this);

                    if (mode.equals("3ND")) {
                        if (playerDatas.split(" ").length == 1) {
                            nextMessage = "Missing Birthday. Retry";
                        }
                        data = playerDatas.split(" ")[1].split("-");
                        c.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]), Integer.parseInt(data[0]));
                        Server.insertPlayerInGameRoom(3, false, playerName, c, playerVirtualView);
                        nextMessage = "You're in Game Room now! 3 Players, without divinities. The game will begin soon";
                        done = true;
                    } else if (mode.equals("2ND")) {
                        if (playerDatas.split(" ").length == 1) {
                            nextMessage = "Missing Birthday. Retry";
                        } else {
                            data = playerDatas.split(" ")[1].split("-");
                            c.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]), Integer.parseInt(data[0]));
                            Server.insertPlayerInGameRoom(3, false, playerName, c, playerVirtualView);
                            nextMessage = "You're in Game Room now! 2 Players, without divinities. The game will begin soon";
                            done = true;
                        }
                    } else if (mode.equals("3D")) {
                        Server.insertPlayerInGameRoom(3, true, playerName, null, playerVirtualView);
                        nextMessage = "You are in the game room! 3 players with divinities. The game will begin soon";
                        done = true;
                    } else if (mode.equals("2D")) {
                        Server.insertPlayerInGameRoom(2, true, playerName, null, playerVirtualView);
                        nextMessage = "You are in the game room! 2 players with divinities. The game will begin soon";
                        done = true;
                    }
                    output.writeObject(nextMessage);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            //at this point, the server is set-up, and will be the controller to start the game
            //but, we must instantiate an handler for incoming messages
            ClientHandlerListener incomingMessagesHandler = new ClientHandlerListener(client);
            incomingMessagesHandler.registerObserver(playerVirtualView);
            Thread listenerThread = new Thread(incomingMessagesHandler);
            listenerThread.start();
            handleGamePhases();
        } catch (IOException e) {
            System.out.println("Disconnected from " + client.getInetAddress());
            if (playerDatas != null) Server.removeNickname(playerDatas);
        }
    }

    private synchronized void handleGamePhases() throws IOException {
        while (true) {
            toDO = null;
            try {
                wait();
            } catch (InterruptedException e) {

            }
            if (toDO == null) continue;

            switch (toDO) {
                case requestAction:
                    sendNetworkMessage();
                    toDO = null;
            }
        }
    }

    public synchronized void sendNetworkMessage() throws IOException {
        output.writeObject(nextObject);
    }

    public synchronized void requestMessageSend(Consumer<AbstractView> lambda) {
        toDO = nextAction.requestAction;
        nextObject = new requestMessagePrint(null);
        notifyAll();
    }

    public synchronized void requestInitialPlayerSelection(ArrayList<String> players) {

    }

    public synchronized void requestInitialPositioning(ArrayList<Position> validCells) {

    }

    public synchronized void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber) {

    }


}
