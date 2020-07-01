package it.polimi.ingsw.PSP48.server.networkmanager;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.PingMessage;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.networkMessagesToClient.*;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.setupMessagesToClient.ClientSetupMessages;
import it.polimi.ingsw.PSP48.setupMessagesToClient.NicknameRequest;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Handles the message send to a player
 * To avoid synchronization problems, a new send can't be requested until the previous one is completed.
 * All the request send methods wait for the previous message to be send
 */
public class ClientHandler implements Runnable {

    /**
     * Next action to be completed by the handler
     */


    private boolean closeThread = false;
    private final Queue<Object> messagesToBeSent = new LinkedList<>();


    /**
     * Initializes the handler object
     *
     * @param client the socket of the remote client
     * @param i      the handler that waits for client messages
     *               Initializes the handler
     */
    public ClientHandler(Socket client, ClientHandlerListener i) {
        this.client = client;
        this.incomingMessagesHandler = i;
    }

    ObjectOutputStream output;

    private final ClientHandlerListener incomingMessagesHandler;


    private final Socket client;

    /**
     * Starts the network handler
     */
    @Override
    public synchronized void run() {

        try {
            handleGamePhases();
        } catch (IOException e) {
            System.out.println("Completed disconnection");
        }

        System.out.println("Disconnected from " + client.getInetAddress());
    }

    /**
     * Handles the sending of messages to the client, dependently to the nextAction param:
     * requestAction: requests the client to do something
     * setupmessage: sends setup message to the client
     * closegame: for some reason, the game ended: notify the client of this
     * replyping: replies to a ping message
     * After a message is sended, the nextAction paramter is initialized to null to allow new message requests.
     *
     * @throws IOException if something goes wrong with the connection
     */
    private synchronized void handleGamePhases() throws IOException {
        output = new ObjectOutputStream(client.getOutputStream());
        System.out.println("Connected to " + client.getInetAddress());
        output.writeObject(new PingMessage());

        setUpMessage(new NicknameRequest("Please choose a nickname without dots and press enter"));

        while (true) {
            while (messagesToBeSent.isEmpty()) {
                if (closeThread) throw new IOException("The game has ended");
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            output.writeObject(messagesToBeSent.remove());
        }
    }

    /**
     * Requests the send of a message that notify the client to display a message
     *
     * @param message the message the client has to show
     */
    public synchronized void requestMessageSend(String message) {
        messagesToBeSent.add(new RequestMessagePrint(message));
        notifyAll();
    }

    /**
     * Requests the send of a message that notify the challenger's client to request the player the first player of the game
     *
     * @param players the list of players in game
     */
    public synchronized void requestInitialPlayerSelection(ArrayList<String> players) {
        System.out.println("Sending request for initial player selection");
        messagesToBeSent.add(new InitialPlayerRequestMessage(players));
        notifyAll();
    }

    /**
     * Requests the send of a message that notify a client to request its player to put his worker on the board
     *
     * @param validCells the cells where the positioning is valid
     */
    public synchronized void requestInitialPositioning(ArrayList<Position> validCells) {
        System.out.println("Sending request for Initial Positioning");
        messagesToBeSent.add(new PositioningRequest(validCells));
        notifyAll();
    }


    /**
     * Requests the send of a message that notify the challenger's client to ask the player to select the divinities available for the game
     *
     * @param div          the available divinities
     * @param playerNumber the game's players number
     */
    public synchronized void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber) {
        messagesToBeSent.add(new ChallengerDivinitiesSelectionRequest(div, playerNumber));
        notifyAll();

    }

    /**
     * Requests the send of a message that notify  the client to request the player to do an optional move, or to skip it
     *
     * @param validCellsForMove the list of workers that can complete the move, associated with the cells where it can move
     */
    public synchronized void requestOptionalMove(ArrayList<WorkerValidCells> validCellsForMove) {
        messagesToBeSent.add(new OptionalMoveRequest(validCellsForMove));
        notifyAll();

    }

    /**
     * Requests the send of a message that notify  the client to request the player to do an optional build, or to skip it
     *
     * @param build the list of workers that can complete the build, associated with the cells where it can build
     * @param dome  the list of workers that can complete the dome, associated with the cells where it can dome
     */
    public synchronized void requestOptionalBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {
        messagesToBeSent.add(new RequestOpionalBuild(build, dome));
        notifyAll();

    }

    /**
     * Requests the send of a message that notify the client that some cells' content has changed
     *
     * @param newCells the cells that have been modified
     */
    public synchronized void changedBoard(ArrayList<Cell> newCells) {
        messagesToBeSent.add(new ChangedBoard(newCells));
        notifyAll();

    }

    /**
     * Requests the send of a message that notify the client the player list changed, even if a player'd divinity changed
     *
     * @param newPlayerList the new player list
     */
    public synchronized void changedPlayerList(ArrayList<String> newPlayerList) {
        messagesToBeSent.add(new UpdatedPlayerList(newPlayerList));
        notifyAll();
    }

    /**
     * Requests the send of a message that notify to request the player to do a move action
     *
     * @param validCellsForMove a list of workers that can complete the move, and cells where a worker can move
     */
    public synchronized void requestMove(ArrayList<WorkerValidCells> validCellsForMove) {
        messagesToBeSent.add(new RequestMove(validCellsForMove));
        notifyAll();

    }

    /**
     * Requests the send of a message that notify the client to request the player to do a construction action
     *
     * @param build a list of workers that can complete the build, and cells where a worker can build
     * @param dome  a list of workers that can complete the dome, and cells where a worker can dome
     */
    public synchronized void requestBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {
        messagesToBeSent.add(new RequestBuild(build, dome));
        notifyAll();

    }


    /**
     * Requests the send of a message that notify the client to request its player to select his divinity
     *
     * @param availableDivinities the list of available divinities
     */
    public synchronized void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {
        messagesToBeSent.add(new DivinitySelectionRequest(availableDivinities));
        notifyAll();

    }

    /**
     * Request the send of a setup message
     *
     * @param message the message
     */
    public synchronized void setUpMessage(ClientSetupMessages message) {
        messagesToBeSent.add(message);
        notifyAll();

    }

    /**
     * Request the send of a message that notify the client the game ended
     *
     * @param message the reason because the game ended
     */
    public synchronized void gameEndMessage(String message) {
        messagesToBeSent.add(new EndGameMessage(message));
        incomingMessagesHandler.setClosed();
        closeThread = true;
        notifyAll();
    }

    /**
     * Requests the reply to a ping message
     */
    public synchronized void replyPing() {
        messagesToBeSent.add(new PingMessage());
        notifyAll();
    }

    /**
     * Handles the disconnection of a client setting the message sender in a shutdown state and waking up the thread form waiting
     * for messages to be sent
     */
    public synchronized void handleClientDisconnection() {
        closeThread = true;
        notifyAll();
    }

}
