package it.polimi.ingsw.PSP48.server.virtualview;

import java.net.Socket;

public class PlayerSocketAssociation {
    private String playerName;
    private Socket playerSocket;

    public PlayerSocketAssociation(String playerName, Socket playerSocket) {
        this.playerName = playerName;
        this.playerSocket = playerSocket;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Socket getPlayerSocket() {
        return playerSocket;
    }
}
