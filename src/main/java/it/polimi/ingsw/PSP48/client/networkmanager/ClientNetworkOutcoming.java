package it.polimi.ingsw.PSP48.client.networkmanager;

import it.polimi.ingsw.PSP48.PingMessage;
import it.polimi.ingsw.PSP48.networkMessagesToServer.*;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.model.ActionCoordinates;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Handles message send to the server
 */
public class ClientNetworkOutcoming implements Runnable, ViewObserver {
    private final Socket server;
    private ObjectOutputStream outputStm;


    private boolean shutdownThread = false;

    /**
     * A queue of messages to be sent to the server
     */
    private final Queue<Object> messagesToBeSent = new LinkedList<>();

    /**
     * Initializes the message sender
     *
     * @param s the socket of connection to the server
     */
    public ClientNetworkOutcoming(Socket s) {
        this.server = s;
    }

    /**
     * Adds in queue a move action message
     *
     * @param p the move coordinates
     */
    @Override
    public synchronized void move(ActionCoordinates p) {
        messagesToBeSent.add(new MoveMessage(p));
        notifyAll();
    }

    /**
     * Adds in queue a build action message
     *
     * @param p the build coordinates
     */
    @Override
    public synchronized void build(ActionCoordinates p) {
        messagesToBeSent.add(new BuildMessage(p));
        notifyAll();
    }

    /**
     * Adds in queue a dome action message
     *
     * @param p the dome coordinates
     */
    @Override
    public synchronized void dome(ActionCoordinates p) {
        messagesToBeSent.add(new DomeMessage(p));
        notifyAll();
    }

    /**
     * Adds in queue a message with chosen position for worker initial positioning
     *
     * @param p the position chosen by the player
     */
    @Override
    public synchronized void putWorkerOnTable(Position p) {
        messagesToBeSent.add(new WorkerPositionMessage(p));
        notifyAll();
    }

    /**
     * Adds in queue a divinity selection message
     *
     * @param divinity the chosen divinity
     */
    @Override
    public synchronized void registerPlayerDivinity(String divinity) {
        messagesToBeSent.add(new PlayerDivinityMessage(divinity));
        notifyAll();
    }


    /**
     * Adds in queue the message for initial divinities selection by the challenger
     *
     * @param divinities the selected divinities by the challenger
     */
    @Override
    public synchronized void selectAvailableDivinities(ArrayList<String> divinities) {
        messagesToBeSent.add(new ChallengerDivinitiesMessage(divinities));
        notifyAll();
    }

    /**
     * Adds in queue a first player selection message
     *
     * @param playerName the first player chosen by the challenger
     */
    @Override
    public synchronized void selectFirstPlayer(String playerName) {
        messagesToBeSent.add(new FirstPlayerSelectionMessage(playerName));
        notifyAll();
    }


    /**
     * Initializes the message sender
     */
    @Override
    public synchronized void run() {
        try {
            outputStm = new ObjectOutputStream(server.getOutputStream());
            handleServerConnection();
        } catch (IOException e) {
            System.out.println("server has died");
        } catch (ClassCastException e) {
            System.out.println("protocol violation");
        }
    }

    /**
     * Sends all messages contained in the queue. Waits for new messages to be sent if the queue is empty. If shutdownThread is true,
     * the thread ends.
     *
     * @throws IOException if there's something wrong with the connection
     */
    public synchronized void handleServerConnection() throws IOException {
        while (messagesToBeSent.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (shutdownThread) break;
            outputStm.writeObject(messagesToBeSent.remove());
        }
    }

    /**
     * Adds in queue a message with the chosen nickname
     *
     * @param nickname the chosen nickname
     */
    public synchronized void requestNicknameSend(String nickname) {
        messagesToBeSent.add(nickname);
        notifyAll();
    }

    /**
     * Adds in queue a message with the chosen game mode
     *
     * @param n the chosen game mode
     */
    public synchronized void setGameMode(String n) {
        messagesToBeSent.add(n);
        notifyAll();
    }


    /**
     * Close the server socket, and notify the message sender that the connection closed
     */
    public synchronized void shutDown() {
        shutdownThread = true;
        notifyAll();
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds in queue a ping message
     */
    public synchronized void replyPing() {
        messagesToBeSent.add(new PingMessage());
        notifyAll();
    }
}
