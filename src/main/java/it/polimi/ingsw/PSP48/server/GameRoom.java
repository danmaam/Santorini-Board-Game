package it.polimi.ingsw.PSP48.server;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.Model;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A game room. It contains the Controller and the Model of the current game.
 */
public class GameRoom {
    private Model model;
    private GameController controller;

    public GameRoom(int playersNumber, boolean AllowedDivinities) {
        model = new Model(playersNumber, AllowedDivinities);
        controller = new GameController(model);
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

    public void addPlayerInRoom(String name, Calendar birthday, AbstractView playerVirtualView) {
        playerVirtualView.registerObserver(controller);
        model.registerObserver(playerVirtualView);
        controller.associateViewWithPlayer(name, playerVirtualView);
        playerVirtualView.notifyObserver((c) -> c.addPlayer(name, birthday));
    }
}
