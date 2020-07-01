package it.polimi.ingsw.PSP48.client.networkmanager;

import it.polimi.ingsw.PSP48.PingMessage;
import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.networkMessagesToClient.NetworkMessagesToClient;
import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;
import it.polimi.ingsw.PSP48.setupMessagesToClient.ClientSetupMessages;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The client's message listener. Waits for messages from the server, and notifies its observers to complete some action
 * when a message arrives, using the command pattern.
 * If a connection lost is detected, notifies the player view that the game ended.
 */
public class ClientNetworkIncoming implements Runnable {
    private final ViewInterface playerView;
    private ObjectInputStream in;
    private final Socket server;
    private ClientNetworkOutcoming o;

    private boolean closed;

    ScheduledExecutorService pingScheduler = Executors.newScheduledThreadPool(1);

    private final ArrayList<ClientNetworkObserver> observers = new ArrayList<>();

    /**
     * Registers an observer of message listener
     *
     * @param n the observer to be registered
     */
    public void addObserver(ClientNetworkObserver n) {
        observers.add(n);
    }

    /**
     * Stops an observer from observing the message handler
     *
     * @param n the observer to be unregistered
     */
    public void removeObserver(ClientNetworkObserver n) {
        observers.remove(n);
    }

    /**
     * Initializes the message listener
     */
    @Override
    public synchronized void run() {
        try {
            in = new ObjectInputStream(server.getInputStream());
            try {
                server.setSoTimeout(15 * 1000);
            } catch (SocketException e) {
                e.printStackTrace();
            }
            retrieveMessage();

        } catch (IOException e) {
            if (!closed) playerView.endgame("Connection to the server lost");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for new message from the server, and:
     * - if it's a setup message, asks the view to process it
     * - if it's a PingMessage, schedules a reply in 5 seconds
     * - if it's a NetworkMessagesToClient, the server is requiring an action to the player; the view is notified
     *
     * @throws IOException            if there's something wrong with the connection
     * @throws ClassNotFoundException if we receive an unexpected class
     */
    public synchronized void retrieveMessage() throws IOException, ClassNotFoundException {
        while (true) {
            Object newMessage = in.readObject();
            if (newMessage instanceof NetworkMessagesToClient) {
                ((NetworkMessagesToClient) newMessage).doAction(playerView);
            } else if (newMessage instanceof ClientSetupMessages) {
                for (ClientNetworkObserver o : observers) ((ClientSetupMessages) newMessage).doAction(o);
            } else if (newMessage instanceof PingMessage) {
                pingScheduler.schedule(() -> o.replyPing(), 5, TimeUnit.SECONDS);
            }
        }

    }

    /**
     * Sets up the message listener
     *
     * @param playerView the player view
     * @param server     the server socket
     */
    public ClientNetworkIncoming(ViewInterface playerView, Socket server) {
        this.playerView = playerView;
        this.server = server;
    }

    /**
     * Sets the message sender
     *
     * @param out the message sender
     */
    public void setOutHandler(ClientNetworkOutcoming out) {
        this.o = out;
    }

    /**
     * Shutdowns the message listener. Sets closed to true to avoid false connection errors.
     */
    public void shutdown() {
        pingScheduler.shutdown();
        closed = true;
    }
}
