package it.polimi.ingsw.PSP48.observers;


import it.polimi.ingsw.PSP48.server.model.Cell;

import java.util.ArrayList;

/**
 * Interface of a model observer.
 */
public interface ModelObserver {
    /**
     * Notifies the observer that some cells on the board changed
     *
     * @param newCells the changed cells
     */
    public void changedBoard(ArrayList<Cell> newCells);

    /**
     * Notifies the observer that the player list and/or player-divinity associations changed
     *
     * @param newPlayerList the new player-divinity association list
     */
    public void changedPlayerList(ArrayList<String> newPlayerList);
}
