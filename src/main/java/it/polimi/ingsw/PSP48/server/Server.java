package it.polimi.ingsw.PSP48.server;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.server.virtualview.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

public class Server {
    private static ArrayList<String> playersConnectedToTheGame = new ArrayList<>();
    public final static int TCP_PORT = 7777;
    public static ArrayList<GameRoom> roomsOnTheServer = new ArrayList<>();

    public static void main(String[] args) {
        ObjectOutputStream out;
        ObjectInputStream in;

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

    public static void addNickname(String s) throws IllegalArgumentException {
        synchronized (playersConnectedToTheGame) {
            if (playersConnectedToTheGame.contains(s)) throw new IllegalArgumentException();
            playersConnectedToTheGame.add(s);
        }
    }

    public static void removeNickname(String s) {
        synchronized (playersConnectedToTheGame) {
            if (playersConnectedToTheGame.contains(s)) playersConnectedToTheGame.remove(s);
        }
    }


    public static void insertPlayerInGameRoom(int playerNumber, boolean allowedDivinities, String name, Calendar Birthday, AbstractView playerVirtualView) {
        boolean added = false;
        for (GameRoom g : roomsOnTheServer) {
            if (g.isGameWithDivinities() == allowedDivinities && g.getRoomPlayerNumber() == playerNumber && g.getPlayersInTheRoom() < g.getRoomPlayerNumber()) {
                g.addPlayerInRoom(name, Birthday, playerVirtualView);
                added = true;
                break;
            }
        }
        if (!added) {
            GameRoom newGameRoom = new GameRoom(playerNumber, allowedDivinities);
            roomsOnTheServer.add(newGameRoom);
            newGameRoom.addPlayerInRoom(name, Birthday, playerVirtualView);
        }
    }

}
