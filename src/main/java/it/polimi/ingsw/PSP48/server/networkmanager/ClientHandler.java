package it.polimi.ingsw.PSP48.server.networkmanager;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.PingMessage;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.networkMessagesToClient.*;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.setupMessagesToClient.ClientSetupMessages;
import it.polimi.ingsw.PSP48.setupMessagesToClient.nicknameRequest;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Handles the message send to a player
 * To avoid synchronization problems, a new send can't be requested until the previous one is completed.
 * All the request send methods wait for the previous message to be send
 */
public class ClientHandler implements Runnable {

    /**
     * Next action to be completed by the handler
     */
    private enum states {
        requestAction, setupmessage, closegame, replyPing
    }

    private states nextAction = null;

    final Object toDOLOCK = new Object();

    /**
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
    private ClientSetupMessages setUpMessage;
    private NetworkMessagesToClient nextObject;


    private final Socket client;

    /**
     * Starts the network handler
     */
    @Override
    public void run() {

        try {
            handleGamePhases();
        } catch (IOException e) {
            e.printStackTrace();
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
    private void handleGamePhases() throws IOException {
        output = new ObjectOutputStream(client.getOutputStream());
        System.out.println("Connected to " + client.getInetAddress());
        output.writeObject(new PingMessage());

        setUpMessage(new nicknameRequest("Please choose a nickname without dots and press enter"));

        while (true) {
            synchronized (toDOLOCK) {
                while (nextAction == null) {
                    try {
                        toDOLOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                switch (nextAction) {
                    case requestAction:
                        System.out.println("Sending " + nextObject);
                        output.writeObject(nextObject);
                        nextAction = null;
                        toDOLOCK.notifyAll();
                        break;
                    case setupmessage:
                        output.writeObject(setUpMessage);
                        nextAction = null;
                        toDOLOCK.notifyAll();
                        break;
                    case closegame:
                        incomingMessagesHandler.setClosed();
                        output.writeObject(nextObject);
                        nextAction = null;
                        toDOLOCK.notifyAll();
                        return;
                    case replyPing:
                        output.writeObject(new PingMessage());
                        nextAction = null;
                        toDOLOCK.notifyAll();
                        break;
                }
            }
        }
    }

    /**
     * Requests the send of a message that notify the client to display a message
     *
     * @param message the message the client has to show
     */
    public void requestMessageSend(String message) {
        synchronized (toDOLOCK) {
            while (nextAction != null) {
                try {
                    toDOLOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nextAction = states.requestAction;
            nextObject = new requestMessagePrint(message);
            toDOLOCK.notifyAll();
        }
    }

    /**
     * Requests the send of a message that notify the challenger's client to request the player the first player of the game
     *
     * @param players the list of players in game
     */
    public void requestInitialPlayerSelection(ArrayList<String> players) {
        System.out.println("Sending request for initial player selection");
        synchronized (toDOLOCK) {
            while (nextAction != null) {
                try {
                    toDOLOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nextObject = new InitialPlayerRequestMessage(players);
            nextAction = states.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    /**
     * Requests the send of a message that notify a client to request its player to put his worker on the board
     *
     * @param validCells the cells where the positioning is valid
     */
    public void requestInitialPositioning(ArrayList<Position> validCells) {
        System.out.println("Sending request for Initial Positioning");
        synchronized (toDOLOCK) {
            while (nextAction != null) {
                try {
                    toDOLOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("requesting initial positioning");
            nextObject = new PositioningRequest(validCells);
            nextAction = states.requestAction;
            toDOLOCK.notifyAll();
        }

    }

    /**
     * Requests the send of a message that notify the challenger's client to ask the player to select the divinities available for the game
     *
     * @param div          the available divinities
     * @param playerNumber the game's players number
     */
    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber) {
        synchronized (toDOLOCK) {
            while (nextAction != null) {
                try {
                    toDOLOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("instantiating divinity list message, and requesting it's send");
            nextObject = new ChallengerDivinitiesSelectionRequest(div, playerNumber);
            nextAction = states.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    /**
     * Requests the send of a message that notify  the client to request the player to do an optional move, or to skip it
     *
     * @param validCellsForMove the list of workers that can complete the move, associated with the cells where it can move
     */
    public void requestOptionalMove(ArrayList<WorkerValidCells> validCellsForMove) {
        System.out.println("sending an optional move request");
        synchronized (toDOLOCK) {
            while (nextAction != null) {
                try {
                    toDOLOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nextObject = new OptionalMoveRequest(validCellsForMove);
            nextAction = states.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    /**
     * Requests the send of a message that notify  the client to request the player to do an optional build, or to skip it
     *
     * @param build the list of workers that can complete the build, associated with the cells where it can build
     * @param dome  the list of workers that can complete the dome, associated with the cells where it can dome
     */
    public void requestOptionalBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {
        synchronized (toDOLOCK) {
            while (nextAction != null) {
                try {
                    toDOLOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nextObject = new RequestOpionalBuild(build, dome);
            nextAction = states.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    /**
     * Requests the send of a message that notify the client that some cells' content has changed
     *
     * @param newCells the cells that have been modified
     */
    public void changedBoard(ArrayList<Cell> newCells) {
        System.out.println("Sending changed board");
        synchronized (toDOLOCK) {
            while (nextAction != null) {
                try {
                    toDOLOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nextObject = new ChangedBoard(newCells);
            nextAction = states.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    /**
     * Requests the send of a message that notify the client the player list changed, even if a player'd divinity changed
     *
     * @param newPlayerList the new player list
     */
    public void changedPlayerList(ArrayList<String> newPlayerList) {
        synchronized (toDOLOCK) {
            while (nextAction != null) {
                try {
                    toDOLOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("sending new player list");
            nextObject = new UpdatedPlayerList(newPlayerList);
            nextAction = states.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    /**
     * Requests the send of a message that notify to request the player to do a move action
     *
     * @param validCellsForMove a list of workers that can complete the move, and cells where a worker can move
     */
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove) {
        System.out.println("Sending move request");
        synchronized (toDOLOCK) {
            while (nextAction != null) {
                try {
                    toDOLOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nextObject = new RequestMove(validCellsForMove);
            nextAction = states.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    /**
     * Requests the send of a message that notify the client to request the player to do a construction action
     *
     * @param build a list of workers that can complete the build, and cells where a worker can build
     * @param dome  a list of workers that can complete the dome, and cells where a worker can dome
     */
    public synchronized void requestBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {
        System.out.println("Sending build request");
        synchronized (toDOLOCK) {
            while (nextAction != null) {
                try {
                    toDOLOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nextObject = new RequestBuild(build, dome);
            nextAction = states.requestAction;
            toDOLOCK.notifyAll();
        }
    }


    /**
     * Requests the send of a message that notify the client to request its player to select his divinity
     *
     * @param availableDivinities the list of available divinities
     */
    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {
        System.out.println("Sending request for divinity selection");
        synchronized (toDOLOCK) {
            while (nextAction != null) {
                try {
                    toDOLOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nextObject = new DivinitySelectionRequest(availableDivinities);
            nextAction = states.requestAction;
            toDOLOCK.notifyAll();
        }
    }

    /**
     * Request the send of a setup message
     *
     * @param message the message
     */
    public void setUpMessage(ClientSetupMessages message) {
        System.out.println("Sending " + message.toString());
        synchronized (toDOLOCK) {
            while (nextAction != null) {
                try {
                    toDOLOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            setUpMessage = message;
            nextAction = states.setupmessage;
            toDOLOCK.notifyAll();
        }
    }

    /**
     * Request the send of a message that notify the client the game ended
     *
     * @param message the reason because the game ended
     */
    public void gameEndMessage(String message) {
        synchronized (toDOLOCK) {
            while (nextAction != null) {
                try {
                    toDOLOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nextObject = new EndGameMessage(message);
            nextAction = states.closegame;
            toDOLOCK.notifyAll();
        }
    }

    /**
     * Requests the reply to a ping message
     */
    public void replyPing() {
        synchronized (toDOLOCK) {
            while (nextAction != null) {
                try {
                    toDOLOCK.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            nextAction = states.replyPing;
            toDOLOCK.notifyAll();
        }
    }


}
