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
    private final Model model;
    private final GameController controller;
    private final int gameRoomID;

    /**
     * Initializes a game room object
     *
     * @param playersNumber     the number of players allowed in the game room
     * @param AllowedDivinities if the match in the game room allows divinities
     * @param ID                the univocal ID of the game room
     */
    public GameRoom(int playersNumber, boolean AllowedDivinities, int ID) {
        model = new Model(playersNumber, AllowedDivinities);
        controller = new GameController(model, ID);
        gameRoomID = ID;
    }

    /**
     * Getter of the maximum number of players allowed in the match
     *
     * @return the maximum number of player allowed in this room
     */
    public int getRoomPlayerNumber() {
        return model.getGamePlayerNumber();
    }

    /**
     * Getter of the players actually in the room
     *
     * @return the number of players actually in the room
     */
    public int getPlayersInTheRoom() {
        return model.getNumberOfPlayers();
    }

    /**
     * Checks if the game is with divinities
     *
     * @return true if the room contains a game with divinities, false otherwise
     */
    public boolean isGameWithDivinities() {
        return model.isGameWithDivinities();
    }

    /**
     * Adds a player in the game room
     *
     * @param name              the name of the player to be added
     * @param birthday          the birthday of the player
     * @param playerVirtualView the virtualview of the player
     */
    public void addPlayerInRoom(String name, Calendar birthday, ViewInterface playerVirtualView) {
        playerVirtualView.registerObserver(controller);
        ((VirtualView) playerVirtualView).setRoomID(gameRoomID);
        model.registerObserver(playerVirtualView);
        controller.associateViewWithPlayer(name, playerVirtualView);
        controller.addPlayer(name, birthday);
    }

    /**
     * Getter of game room id
     * @return the ID of the game room
     */
    public int getGameRoomID() {
        return gameRoomID;
    }

    /**
     * Notifies all the players in the game room that one player disconnected, and remove from the server the nicknames of all players in the game room
     *
     * @param disconnectedPlayers the player that disconnected from the server
     */
    public void notifyAllPlayersOfDisconnection(String disconnectedPlayers) {
        for (Player p : model.getPlayersInGame()) {
            //all the players different from the one who disconnected, are notified of the player that disconnected from the game
            if (!p.getName().equals(disconnectedPlayers)) {
                controller.getPlayerView(p.getName()).endgame(disconnectedPlayers + " disconnected. Aborting game.");
            }
        }
        //closed the connection with all the players, GameRoom must free nicknames on the server
        for (Player p : model.getPlayersInGame()) {
            Server.removeNickname(p.getName());
        }
    }

    /**
     * Notifies all the players that someone won the game; the winner is notified of his success, other players are notified
     * of their failure.
     * In the end, free all game room players' nickname from the server
     *
     * @param winner the player that won the game
     */
    public void notifyAllPlayersOfWinner(String winner) {
        for (Player p : model.getPlayersInGame()) {
            //all the players are notified that the winner won the game
            if (!p.getName().equals(winner)) {
                //the losers are notified of their lose
                controller.getPlayerView(p.getName()).endgame(winner + " won the game. You lost. Anlaki :(");
            } else {
                //the winner is notifies of their win
                controller.getPlayerView(p.getName()).endgame("You win the game!");
            }
        }
        //every player's nickname are deleted from the server
        for (Player p : model.getPlayersInGame()) {
            Server.removeNickname(p.getName());
        }
    }

    /**
     * Notifies all the players that someone lost the game and the game is ended; the winner is notified of his success, other players are notified
     * of their failure.
     * In the end, free all game room players' nickname from the server
     *
     * @param loser the player that lost the game
     */
    public void notifyAllPlayersOfLoser(String loser) {
        controller.getPlayerView(loser).endgame("You lose cause you won't be able to end the turn");
        for (Player p : model.getPlayersInGame()) {
            if (!p.getName().equals(loser))
                //all the other players are notified that the loser has lost the game
                controller.getPlayerView(p.getName()).endgame("You win cause " + model.getCurrentPlayer().getName() + "can't end his turn");
        }
        //all the player's nicknames are removed from the server
        for (Player p : model.getPlayersInGame()) {
            Server.removeNickname(p.getName());
        }
    }
}
