package it.polimi.ingsw.PSP48.server;

import it.polimi.ingsw.PSP48.server.Server;
import it.polimi.ingsw.PSP48.server.virtualview.VirtualView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Calendar;

/**
 * Handles the connection of a player
 */
public class ClientHandler implements Runnable {

    public ClientHandler(Socket client) {
        this.client = client;
    }

    private String playerDatas = null;
    private String nextMessage;
    private String playerName;


    private final Socket client;
    private VirtualView playerVirtualView;

    @Override
    public void run() {
        try {
            handleClientConnection();
        } catch (IOException e) {
            System.out.println("Disconnected from " + client.getInetAddress());
            if (playerDatas != null) Server.removeNickname(playerDatas);
        }
    }

    private void handleClientConnection() throws IOException {
        System.out.println("Connected to " + client.getInetAddress());
        ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream input = new ObjectInputStream(client.getInputStream());
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

                else if (mode.equals("3ND")) {
                    data = playerDatas.split(" ")[1].split("-");
                    c.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]), Integer.parseInt(data[0]));
                    Server.insertPlayerInGameRoom(3, false, playerName, c, playerVirtualView);
                    nextMessage = "You're in Game Room now! 3 Players, without divinities. The game will begin soon";
                    done = true;
                } else if (mode.equals("2ND")) {
                    data = playerDatas.split(" ")[1].split("-");
                    c.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]), Integer.parseInt(data[0]));
                    Server.insertPlayerInGameRoom(3, false, playerName, c, playerVirtualView);
                    nextMessage = "You're in Game Room now! 2 Players, without divinities. The game will begin soon";
                    done = true;
                } else if (mode.equals("3D")) {
                    playerVirtualView = new VirtualView();
                    Server.insertPlayerInGameRoom(3, true, playerName, null, playerVirtualView);
                    nextMessage = "You are in the game room! 3 players with divinities. The game will begin soon";
                    done = true;
                } else {
                    playerVirtualView = new VirtualView();
                    Server.insertPlayerInGameRoom(2, true, playerName, null, playerVirtualView);
                    nextMessage = "You are in the game room! 2 players with divinities. The game will begin soon";
                    done = true;
                }
                output.writeObject(nextMessage);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }


    }

}
