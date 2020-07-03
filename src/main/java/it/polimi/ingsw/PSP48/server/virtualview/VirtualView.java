package it.polimi.ingsw.PSP48.server.virtualview;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.server.EndReason;
import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.observers.ServerNetworkObserver;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.model.ActionCoordinates;
import it.polimi.ingsw.PSP48.server.Server;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.networkmanager.ClientHandler;
import it.polimi.ingsw.PSP48.server.networkmanager.ClientHandlerListener;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * The player's remote view on the server.
 * @author Daniele Mammone
 */
public class VirtualView implements ViewInterface, ServerNetworkObserver {

    private final ArrayList<ViewObserver> observers = new ArrayList<>();


    /**
     * Registers an observer of the virtual view
     *
     * @param obv the observer to be registered
     */
    public void registerObserver(ViewObserver obv) {
        observers.add(obv);
    }

    /**
     * Stops an observer from observing the virtual view
     *
     * @param obv the observer to be unregistered
     */
    public void unregisterObserver(ViewObserver obv) {
        observers.remove(obv);
    }

    /**
     * Notifies the observers to complete a certain action
     *
     * @param lambda the observer's method that must be called
     */
    public void notifyObserver(Consumer<ViewObserver> lambda) {
        for (ViewObserver obv : observers) lambda.accept(obv);
    }


    private final String playerName;
    ClientHandler playerHandler;
    ClientHandlerListener playerListener;
    private int roomID = -1;

    /**
     * Sets the game room where the player is
     *
     * @param roomID the ID of the assigned game room
     */
    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    /**
     * Initializes the virtual view object
     *
     * @param p          the network message sender
     * @param l          the message listener
     * @param playerName the name of the player
     */
    public VirtualView(ClientHandler p, ClientHandlerListener l, String playerName) {
        playerHandler = p;
        playerListener = l;
        this.playerName = playerName;
    }

    /**
     * Requests via network the challenger to choose the first player
     *
     * @param players the list of players in game
     */
    @Override
    public void requestInitialPlayerSelection(ArrayList<String> players) {
        playerHandler.requestInitialPlayerSelection(players);
    }

    /**
     * Requests via network the player to put one of his workers on the board
     *
     * @param validCells the list of valid cells where he can put his worker
     */
    @Override
    public void requestInitialPositioning(ArrayList<Position> validCells) {
        playerHandler.requestInitialPositioning(validCells);
    }

    /**
     * Requests via network the challenger to choose the divinities available in game
     *
     * @param div          the divinities from which the challenger can choose
     * @param playerNumber the number of players in the game
     */
    @Override
    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber) {
        playerHandler.requestChallengerDivinitiesSelection(div, playerNumber);
    }


    /**
     * Requests via network the client to print a message
     *
     * @param s the message to be printed
     */
    @Override
    public void printMessage(String s) {
        playerHandler.requestMessageSend(s);
    }

    /**
     * Requests via network the player to do an optional move action, or to skip it
     *
     * @param validCellsForMove the association between workers and the valid cells where them can move
     */
    @Override
    public void requestOptionalMove(ArrayList<WorkerValidCells> validCellsForMove) {
        playerHandler.requestOptionalMove(validCellsForMove);
    }

    /**
     * Requests via network the player to do an optional construction action, or to skip it
     *
     * @param build the association between workers and the valid cells where them can build
     * @param dome  the association between workers and the valid cells where them can put a dome
     */
    @Override
    public void requestOptionalBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {
        playerHandler.requestOptionalBuild(build, dome);
    }


    /**
     * Notifies the client via network that some cells changed their content
     *
     * @param newCells the cells that changed their content
     */
    @Override
    public void changedBoard(ArrayList<Cell> newCells) {
        playerHandler.changedBoard(newCells);
    }

    /**
     * Notifies the client that the player list, or the association player-divinity, changed
     *
     * @param newPlayerList the new player and player-divinity association list
     */
    @Override
    public void changedPlayerList(ArrayList<String> newPlayerList) {
        playerHandler.changedPlayerList(newPlayerList);
    }


    /**
     * Requests the player, via network, to complete a move action
     *
     * @param validCellsForMove the list of association between workers and cells where they can move
     */
    @Override
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove) {
        playerHandler.requestMove(validCellsForMove);
    }

    /**
     * Requests the player, via network, to complete a construction action
     *
     * @param validForBuild the list of association between workers and cells where they can build
     * @param validForDome  the list of association between workers and cells where they can put a dome
     */
    @Override
    public void requestDomeOrBuild(ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome) {
        playerHandler.requestBuild(validForBuild, validForDome);
    }

    /**
     * Notify the client that the game ended for some reason
     *
     * @param messageOfEndGame the reason of the end of the game
     */
    @Override
    public void endgame(String messageOfEndGame) {
        playerHandler.gameEndMessage(messageOfEndGame);
    }

    /**
     * Requests, via network, the player to select his divinity
     *
     * @param availableDivinities the list of divinity from which the player can choose his divinity
     */
    @Override
    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {
        playerHandler.requestDivinitySelection(availableDivinities);
    }

    /**
     * Requests the controller to process a move action
     *
     * @param p the coordinates of the move action
     */
    @Override
    public void move(ActionCoordinates p) {
        notifyObserver(c -> c.move(p));
    }

    /**
     * Requests the controller to process a build action
     *
     * @param p the coordinates of the build action
     */
    @Override
    public void build(ActionCoordinates p) {
        notifyObserver(c -> c.build(p));
    }

    /**
     * Requests the controller to process a dome action
     *
     * @param p the coordinates of the dome action
     */
    @Override
    public void dome(ActionCoordinates p) {
        notifyObserver(c -> c.dome(p));
    }

    /**
     * Requests the controller to process an initial positioning
     *
     * @param p the position where the player wants to position the worker
     */
    @Override
    public void putWorkerOnTable(Position p) {
        notifyObserver(c -> c.putWorkerOnTable(p));
    }

    /**
     * Requests the controller to associate the player with the chosen divinity
     *
     * @param divinity the chosen divinity
     */
    @Override
    public void registerPlayerDivinity(String divinity) {
        notifyObserver(c -> c.registerPlayerDivinity(divinity));
    }

    /**
     * Requests the controller to press the available divinities chosen by the challenger
     *
     * @param divinities the divinities chosen by the challenger
     */
    @Override
    public void selectAvailableDivinities(ArrayList<String> divinities) {
        notifyObserver(c -> c.selectAvailableDivinities(divinities));
    }

    /**
     * Requests the controller to set the first player, chosen by the challenger
     *
     * @param player the first player chosen
     */
    @Override
    public void selectFirstPlayer(String player) {
        notifyObserver(c -> c.selectFirstPlayer(player));
    }

    /**
     * Requests the server to destroy the game room where the player is in
     */
    @Override
    public void destroyGame() {
        Server.destroyGameRoom(roomID, playerName, EndReason.disconnection);
    }


}
