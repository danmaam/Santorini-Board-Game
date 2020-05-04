package it.polimi.ingsw.PSP48.server;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.networkmanager.ClientHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

public class Server {
    private static ArrayList<String> playersConnectedToTheGame = new ArrayList<>();
    public final static int TCP_PORT = 7777;
    private static ArrayList<GameRoom> roomsOnTheServer = new ArrayList<>();
    private static int nextRoomID = 0;

    public static void main(String[] args) {

        System.out.println("Santorini Server V.0.1 Alpha.");
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
                ClientHandler cH = new ClientHandler(client);
                Thread th = new Thread(cH);
                th.start();
            } catch (IOException e) {
                System.out.println("connection error");
            }
        }
    }

    public synchronized static void addNickname(String s) throws IllegalArgumentException {
        if (playersConnectedToTheGame.contains(s)) throw new IllegalArgumentException();
        playersConnectedToTheGame.add(s);
    }

    public synchronized static void removeNickname(String s) {
        if (playersConnectedToTheGame.contains(s)) playersConnectedToTheGame.remove(s);
    }


    public static synchronized void insertPlayerInGameRoom(int playerNumber, boolean allowedDivinities, String name, Calendar Birthday, AbstractView playerVirtualView) {
        System.out.println("Adding in the game room");
        boolean added = false;
        for (GameRoom g : roomsOnTheServer) {
            if (g.isGameWithDivinities() == allowedDivinities && g.getRoomPlayerNumber() == playerNumber && g.getPlayersInTheRoom() < g.getRoomPlayerNumber()) {
                g.addPlayerInRoom(name, Birthday, playerVirtualView);
                added = true;
                break;
            }
        }
        if (!added) {
            GameRoom newGameRoom = new GameRoom(playerNumber, allowedDivinities, nextRoomID);
            roomsOnTheServer.add(newGameRoom);
            newGameRoom.addPlayerInRoom(name, Birthday, playerVirtualView);
            nextRoomID++;
        }
    }

    public static synchronized void destroyGameRoom(int roomID, String disconnectedPlayer) {
        //i must find the game room
        GameRoom tbd = null;
        for (GameRoom g : roomsOnTheServer) {
            if (g.getGameRoomID() == roomID) {
                tbd = g;
            }
        }
        //found the game room, notify all the players to shutdown connection
        tbd.notifyAllPlayersOfDisconnection(disconnectedPlayer);
        //notified all the players,
        roomsOnTheServer.remove(tbd);
    }
}
