package it.polimi.ingsw.PSP48.observers;

import it.polimi.ingsw.PSP48.server.model.ActionCoordinates;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;

/**
 * The interface of an observer of the view
 */
public interface ViewObserver {
    /**
     * Request the observer to process a move action
     *
     * @param p the coordinates of the move
     */
    void move(ActionCoordinates p);

    /**
     * Request the observer to process a build action
     *
     * @param p the coordinates of the build
     */
    void build(ActionCoordinates p);

    /**
     * Request the observer to process a dome action
     *
     * @param p the coordinates of the dome
     */
    void dome(ActionCoordinates p);

    /**
     * Requests the observer to process a worker positioning on the board
     *
     * @param p the position where the player wants to put one of his workers on the board
     */
    void putWorkerOnTable(Position p);

    /**
     * Requests the observer to process the choose of a divinity by the player
     *
     * @param divinity the divinity choose by the player
     */
    void registerPlayerDivinity(String divinity);

    /**
     * Requests the observer to process the divinity chosen by the challenger
     *
     * @param divinities the divinities chosen by the challenger
     */
    void selectAvailableDivinities(ArrayList<String> divinities);

    /**
     * Requests the observer to process the challenger's first player choose
     *
     * @param playerName the player chosen by the challenger
     */
    void selectFirstPlayer(String playerName);
}
