package it.polimi.ingsw.PSP48.server.observers;


import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;

/**
 * view is observed by something: in virtualview, vv is observed by controller, in vlient view it's observed by network handler
 */
public interface ModelObserver {
    public void changedBoard(ArrayList<Cell> newCells);

    public void changedPlayerList(ArrayList<String> newPlayerList);
}
