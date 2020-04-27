package it.polimi.ingsw.PSP48.client;

import it.polimi.ingsw.PSP48.server.model.Cell;
import java.util.ArrayList;

/**
 * interface containing methods that will be called when there is an update in the game and we need to change something in the client
 */
public interface ModelObserver
{
    void changedBoard(ArrayList<Cell> newCells);

    void changedPlayerList(ArrayList<String> newPlayerList);
}
