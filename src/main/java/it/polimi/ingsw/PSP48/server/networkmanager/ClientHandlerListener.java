package it.polimi.ingsw.PSP48.server.networkmanager;

import it.polimi.ingsw.PSP48.PingMessage;
import it.polimi.ingsw.PSP48.networkMessagesToServer.NetworkMessagesToServer;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;
import it.polimi.ingsw.PSP48.server.Server;
import it.polimi.ingsw.PSP48.server.virtualview.VirtualView;
import it.polimi.ingsw.PSP48.setupMessagesToClient.ClientSetupMessages;
import it.polimi.ingsw.PSP48.setupMessagesToClient.GameModeRequest;
import it.polimi.ingsw.PSP48.setupMessagesToClient.NicknameRequest;
import it.polimi.ingsw.PSP48.setupMessagesToClient.completedSetup;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The server's message listener for a certain player.. Waits for messages from the client, and when one arrives, notifies its observers to complete some action.
 * If detects a connection lost, stops from listening messages and put the message sender of the same player in shutdown mode.
 */
public class ClientHandlerListener implements Runnable {

    private Object nextMessage;

    private String playerNickname = null;
    private boolean setGameMode = false;
    private boolean setClosed = false;

    private final Socket clientSocket;
    private ClientHandler out;

    private final ArrayList<ServerNetworkObserver> observers = new ArrayList<>();

    /**
     * Register an observer of network messages
     *
     * @param obv the observer to be registered
     */
    public void registerObserver(ServerNetworkObserver obv) {
        observers.add(obv);
    }

    /**
     * Unregisters an observer of network messages
     *
     * @param obv the observer to be removed
     */
    public void unregisterObserver(ServerNetworkObserver obv) {
        observers.remove(obv);
    }

    /**
     * Single thread executors used to schedule next action
     */
    private final ExecutorService executors = Executors.newSingleThreadExecutor();
    /**
     * Single thread executor used to schedule ping reply message
     */
    private final ScheduledExecutorService pingExecutor = Executors.newScheduledThreadPool(1);

    /**
     * Notifies the observers to complete the action contained in the network message that arrived
     */
    public void notifyObservers() {
        for (ServerNetworkObserver nO : observers) {
            ((NetworkMessagesToServer) nextMessage).doThings(nO);
        }
    }

    /**
     * Initializes the listener object
     *
     * @param client the socket of the client
     */
    public ClientHandlerListener(Socket client) {
        this.clientSocket = client;
    }

    /**
     * Starts the listener, and put it in listening mode
     */
    @Override
    public synchronized void run() {
        try {
            clientSocket.setSoTimeout(15 * 1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            waitForMessages();
        } catch (IOException e) {
            //requests the game closing only there was a disconnection, and not in case of losing
            if (!setClosed) {
                for (ServerNetworkObserver o : observers) {
                    o.destroyGame();
                }
                if (observers.isEmpty() && playerNickname != null) Server.removeNickname(playerNickname);
            }
            pingExecutor.shutdown();
            executors.shutdown();
            out.handleClientDisconnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Wait for a message from the client and do different actions depending to the type of the message:
     * A String is a setup message. When received, asks to process the nickname or the game-mode, depending on the setup phase;
     * A PingMessage means that the client is still alive; schedules a reply in 5 seconds;
     * a NetworkMessageToServer is some action from the player; notifies the view to process the action.
     *
     * @throws IOException            if something is wrong with connection
     * @throws ClassNotFoundException if is received an unexpected class
     */
    public synchronized void waitForMessages() throws IOException, ClassNotFoundException {
        ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
        while (true) {
            nextMessage = input.readObject();
            if (nextMessage instanceof String) {
                if (playerNickname == null) {
                    executors.submit(() -> this.processNickname((String) nextMessage));
                } else if (!setGameMode) {
                    executors.submit(() -> this.processGameMode((String) nextMessage));
                }
            } else if (nextMessage instanceof NetworkMessagesToServer) {
                executors.submit(this::notifyObservers);
            } else if (nextMessage instanceof PingMessage)
                pingExecutor.schedule(() -> out.replyPing(), 5, TimeUnit.SECONDS);
        }
    }


    /**
     * Changes the setup phase status, precisely if the game mode have been set
     *
     * @param value true if the game mode has been set
     */
    public void setGameMode(boolean value) {
        setGameMode = value;
    }

    /**
     * Used by the first listener when the player is out the game, to avoid the destruction of the game room
     * when it mustn't be destroyed
     */
    public void setClosed() {
        executors.shutdown();
        pingExecutor.shutdown();
        setClosed = true;
    }

    /**
     * Class constructor
     *
     * @param h the handler that sends messages to the client
     */
    public void setUploader(ClientHandler h) {
        out = h;
    }

    /**
     * Process the player's nickname. Verify if it's available, and notify the result to the client
     *
     * @param nickname the nickname chosen by the player
     */
    public void processNickname(String nickname) {
        ClientSetupMessages nextMessage;
        try {
            Server.addNickname(nickname);
            nextMessage = new GameModeRequest("Valid Nickname. Welcome to the game");
            playerNickname = nickname;
        } catch (IllegalArgumentException e) {
            nextMessage = new NicknameRequest("Invalid nickname. Retry");
        }
        out.setUpMessage(nextMessage);
    }

    /**
     * Process the game mode chosen by the player. If the player sent a wrong message, notify it of the problem, otherwise add
     * it in a game room.
     *
     * @param gameMode the game mode sent by the player
     */
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
        if (!nextMessage.equals("Missing Birthday. Retry") && !nextMessage.equals("Not valid mode. Retry")) {
            setGameMode = true;
            out.setUpMessage(new completedSetup(nextMessage));

            VirtualView playerVirtualView = new VirtualView(out, this, playerNickname);
            this.registerObserver(playerVirtualView);

            Server.insertPlayerInGameRoom(playerNumber, divinities, playerNickname, c, playerVirtualView);
        } else {
            out.setUpMessage(new GameModeRequest("Invalid game mode. Please retry."));
        }

    }


}
