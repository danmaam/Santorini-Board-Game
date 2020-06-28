package it.polimi.ingsw.PSP48.observers;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * The interface of an observer of the view
 */
public interface ViewObserver {
    /**
     * Request the observer to process a move action
     *
     * @param p the coordinates of the move
     */
    public void move(MoveCoordinates p);

    /**
     * Request the observer to process a build action
     *
     * @param p the coordinates of the build
     */
    public void build(MoveCoordinates p);

    /**
     * Request the observer to process a dome action
     *
     * @param p the coordinates of the dome
     */
    public void dome(MoveCoordinates p);

    /**
     * Requests the observer to process a worker positioning on the board
     *
     * @param p the position where the player wants to put one of his workers on the board
     */
    public void putWorkerOnTable(Position p);

    /**
     * Requests the observer to process the choose of a divinity by the player
     *
     * @param divinity the divinity choose by the player
     */
    public void registerPlayerDivinity(String divinity);

    /**
     * Requests the observer to process the divinity chosen by the challenger
     *
     * @param divinities the divinities chosen by the challenger
     */
    public void selectAvailableDivinities(ArrayList<String> divinities);

    /**
     * Requests the observer to process the challenger's first player choose
     *
     * @param playerName the player chosen by the challenger
     */
    public void selectFirstPlayer(String playerName);
}
