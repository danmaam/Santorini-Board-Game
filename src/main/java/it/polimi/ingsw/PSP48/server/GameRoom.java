package it.polimi.ingsw.PSP48.server;

import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.virtualview.VirtualView;

import java.util.Calendar;

/**
 * A game room. It contains the Controller and the Model of the current game.
 */
public class GameRoom {
    private Model model;
    private GameController controller;
    private int gameRoomID;

    public GameRoom(int playersNumber, boolean AllowedDivinities, int ID) {
        model = new Model(playersNumber, AllowedDivinities);
        controller = new GameController(model, gameRoomID);
        gameRoomID = ID;
    }

    public int getRoomPlayerNumber() {
        return model.getGamePlayerNumber();
    }

    public int getPlayersInTheRoom() {
        return model.getNumberOfPlayers();
    }

    public boolean isGameWithDivinities() {
        return model.isGameWithDivinities();
    }

    public void addPlayerInRoom(String name, Calendar birthday, ViewInterface playerVirtualView) {
        playerVirtualView.registerObserver(controller);
        ((VirtualView) playerVirtualView).setRoomID(gameRoomID);
        model.registerObserver(playerVirtualView);
        controller.associateViewWithPlayer(name, playerVirtualView);
        playerVirtualView.notifyObserver((c) -> c.addPlayer(name, birthday));
    }

    public int getGameRoomID() {
        return gameRoomID;
    }

    public void notifyAllPlayersOfDisconnection(String disconnectedPlayers) {
        for (Player p : model.getPlayersInGame()) {
            if (disconnectedPlayers == null || !p.getName().equals(disconnectedPlayers)) {
                controller.getPlayerView(p.getName()).endgame(disconnectedPlayers + "have been disconnected. Aborting game.");
            }
        }
        //closed the connection with all the players, GameRoom must free nicknames on the server
        for (Player p : model.getPlayersInGame()) {
            Server.removeNickname(p.getName());
        }
        //at this point, the server will destroy the room
    }
}
