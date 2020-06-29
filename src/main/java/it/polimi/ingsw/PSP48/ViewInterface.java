package it.polimi.ingsw.PSP48;

import it.polimi.ingsw.PSP48.observers.ModelObserver;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * The interface of the view of the game.
 * It's also an observer of the model
 */
public interface ViewInterface extends ModelObserver {
    /**
     * Processes the controller's request of a move action
     *
     * @param validCellsForMove the association valid workers-valid cells where the worker can move
     */
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove);

    /**
     * Processes the controller's request of a construction action
     *
     * @param validForBuild the association valid workers-valid cells where a worker can build
     * @param validForDome  the association valid workers-valid cells where a worker can put a dome
     */
    public void requestDomeOrBuild(ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome);

    /**
     * Processes the controller's message of endgame
     *
     * @param messageOfEndGame the reason why the game ended
     */
    public void endgame(String messageOfEndGame);

    /**
     * Processes the controller's request to a player to choose his divinity
     *
     * @param availableDivinities the available divinities
     */
    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities);

    /**
     * Processes the controller's request to the challenger for choosing the first player of the game
     *
     * @param players the lists of players in the match
     */
    public void requestInitialPlayerSelection(ArrayList<String> players);

    /**
     * Processes the controller's request to a player to put a worker on the board
     *
     * @param validCells the positions where a worker can be put
     */
    public void requestInitialPositioning(ArrayList<Position> validCells);

    /**
     * Processes the controller's request to the challenger to choose the divinities available for the game
     *
     * @param div          the available divinities
     * @param playerNumber the number of players of the match
     */
    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber);

    /**
     * Processes the controller's request of showing a message
     *
     * @param s the message to be shown
     */
    public void printMessage(String s);

    /**
     * Processes the controller's request of an optional move action, or to skip it
     *
     * @param validCellsForMove the association valid workers-valid cells where the worker can move
     */
    public void requestOptionalMove(ArrayList<WorkerValidCells> validCellsForMove);

    /**
     * Processes the controller's request of an optional construction action, or to skip it
     *
     * @param build the association valid workers-valid cells where a worker can build
     * @param dome  the association valid workers-valid cells where a worker can put a dome
     */
    public void requestOptionalBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome);

    /**
     * Registers a view observer
     *
     * @param obv the new observer
     */
    public void registerObserver(ViewObserver obv);

    /**
     * Stops an observer from observing the view
     *
     * @param obv the observer to be removed
     */
    public void unregisterObserver(ViewObserver obv);

    /**
     * Notifies all observers to complete an action
     *
     * @param lambda the observer's method to be invoked
     */
    public void notifyObserver(Consumer<ViewObserver> lambda);

}
