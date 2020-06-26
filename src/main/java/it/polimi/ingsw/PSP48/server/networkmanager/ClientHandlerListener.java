package it.polimi.ingsw.PSP48.server.networkmanager;

import it.polimi.ingsw.PSP48.PingMessage;
import it.polimi.ingsw.PSP48.networkMessagesToServer.NetworkMessagesToServer;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;
import javafx.concurrent.ScheduledService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientHandlerListener implements Runnable {

    private Object nextMessage;

    private boolean setNickname = false;
    private boolean setGameMode = false;
    private boolean setClosed = false;

    private final Socket clientSocket;
    private ClientHandler out;

    private final ArrayList<ServerNetworkObserver> observers = new ArrayList<>();

    public void registerObserver(ServerNetworkObserver obv) {
        observers.add(obv);
    }

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

    public void notifyObservers() {
        for (ServerNetworkObserver nO : observers) {
            ((NetworkMessagesToServer) nextMessage).doThings(nO);
        }
    }

    public ClientHandlerListener(Socket client) {
        this.clientSocket = client;
    }

    /**
     * Starts the listener, and put it in listening mode
     */
    @Override
    public void run() {
        try {
            clientSocket.setSoTimeout(15 * 1000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        try {
            waitForMessages();
        } catch (IOException e) {
            if (clientSocket.isConnected()) {
                try {
                    clientSocket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            //means that the connection dropped. i must close the game.
            if (!setClosed) {
                for (ServerNetworkObserver o : observers) {
                    o.destroyGame();
                }
            }
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
                if (!setNickname) {
                    for (ServerNetworkObserver o : observers)
                        executors.submit(() -> o.processNickname((String) nextMessage));
                } else if (!setGameMode) {
                    for (ServerNetworkObserver o : observers)
                        executors.submit(() -> o.processGameMode((String) nextMessage));
                }
            } else if (nextMessage instanceof NetworkMessagesToServer) {
                executors.submit(this::notifyObservers);
            } else if (nextMessage instanceof PingMessage)
                pingExecutor.schedule(() -> out.replyPing(), 5, TimeUnit.SECONDS);
        }
    }

    /**
     * Changes the setup phase status, precisely if the nickname have been set
     *
     * @param value true if the nickname has been set
     */
    public void nicknameSet(boolean value) {
        setNickname = value;
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


}
