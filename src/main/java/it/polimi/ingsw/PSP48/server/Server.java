package it.polimi.ingsw.PSP48.server;

import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.server.networkmanager.ClientHandler;
import it.polimi.ingsw.PSP48.server.networkmanager.ClientHandlerListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * The game server class
 */
public class Server {
    private static final ArrayList<String> playersConnectedToTheGame = new ArrayList<>();
    /**
     * The port on which the server is hosted
     */
    public final static int TCP_PORT = 7777;
    private static final ArrayList<GameRoom> roomsOnTheServer = new ArrayList<>();
    private static int nextRoomID = 0;

    /**
     * Starts the server, and puts it in wait for connections for clients. When a client connects,
     * generates two handler for it: to send messages, and to receive messages.
     */
    public static void run() {

        System.out.println("Santorini Server V.1.0");
        ServerSocket socket;
        try {
            socket = new ServerSocket(TCP_PORT);
        } catch (IOException e) {
            System.out.println("Can't start server. Aborting");
            System.exit(1);
            return;
        }

        while (true) {
            try {
                Socket client = socket.accept();
                ClientHandlerListener incomingMessagesHandler;
                incomingMessagesHandler = new ClientHandlerListener(client);
                ClientHandler cH = new ClientHandler(client, incomingMessagesHandler);
                Thread th = new Thread(cH);


                incomingMessagesHandler.setUploader(cH);
                Thread listenerThread = new Thread(incomingMessagesHandler);
                listenerThread.start();
                th.start();
            } catch (IOException e) {
                System.out.println("connection error");
            }
        }
    }

    /**
     * Adds a player nickname to the server
     *
     * @param s the chosen nickname
     * @throws IllegalArgumentException if the nickname is already in use
     */
    public synchronized static void addNickname(String s) throws IllegalArgumentException {
        if (playersConnectedToTheGame.contains(s)) throw new IllegalArgumentException();
        playersConnectedToTheGame.add(s);
    }

    /**
     * Frees a nickname from the server
     *
     * @param s the nickname to be removed
     */
    public synchronized static void removeNickname(String s) {
        playersConnectedToTheGame.remove(s);
    }


    /**
     * Inserts a player in a game room. Is a game room is available, adds the player in it, otherwise creates a new game room.
     *
     * @param playerNumber      the number of players of the chosen game mode
     * @param allowedDivinities if divinities are allowed in the game
     * @param name              the name of the added player
     * @param Birthday          the player's birthday
     * @param playerVirtualView the players' virtualview
     */
    public static synchronized void insertPlayerInGameRoom(int playerNumber, boolean allowedDivinities, String name, Calendar Birthday, ViewInterface playerVirtualView) {
        System.out.println("Adding in the game room");
        boolean added = false;
        //tries to found a free game room compatible with player's game mode
        for (GameRoom g : roomsOnTheServer) {
            if (g.isGameWithDivinities() == allowedDivinities && g.getRoomPlayerNumber() == playerNumber && g.getPlayersInTheRoom() < g.getRoomPlayerNumber()) {
                g.addPlayerInRoom(name, Birthday, playerVirtualView);
                added = true;
                break;
            }
        }

        //if there isn't an available game room, it's created
        if (!added) {
            GameRoom newGameRoom = new GameRoom(playerNumber, allowedDivinities, nextRoomID);
            roomsOnTheServer.add(newGameRoom);
            newGameRoom.addPlayerInRoom(name, Birthday, playerVirtualView);
            nextRoomID++;
        }
    }

    /**
     * Destroys a game room due to game end. Notifies all the players of the game end, with its reason.
     *
     * @param roomID             the ID of the room to be deleted
     * @param incriminatedPlayer the player that provoked the game end
     * @param reason             the reason why the game ended
     */
    public static synchronized void destroyGameRoom(int roomID, String incriminatedPlayer, EndReason reason) {
        //i must find the game room
        if (roomID != -1) {
            //it must be different -1, since we must handle disconnection after a player entered in a game room
            GameRoom tbd = null;
            for (GameRoom g : roomsOnTheServer) {
                if (g.getGameRoomID() == roomID) {
                    tbd = g;
                    break;
                }
            }
            //found the game room, notify all the players to shutdown connection
            switch (reason) {
                case disconnection:
                    tbd.notifyAllPlayersOfDisconnection(incriminatedPlayer);
                    break;
                case win:
                    tbd.notifyAllPlayersOfWinner(incriminatedPlayer);
                    break;
                case lose:
                    tbd.notifyAllPlayersOfLoser(incriminatedPlayer);
                    break;
            }
            roomsOnTheServer.remove(tbd);
        } else Server.removeNickname(incriminatedPlayer);
    }

}
