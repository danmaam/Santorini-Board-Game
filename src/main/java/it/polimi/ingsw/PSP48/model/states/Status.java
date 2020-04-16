package it.polimi.ingsw.PSP48.model.states;

import it.polimi.ingsw.PSP48.model.GameData;
import it.polimi.ingsw.PSP48.model.Player;
import it.polimi.ingsw.PSP48.model.exceptions.*;

/**
 * interface used to handle all the different actions associated to each state of the game, using the method handleRequest
 * @author Rebecca Marelli
 */
public interface Status
{
    /**
     * method used for the first states where we need to setup the game, and also for the activities related to the beginning and the end of a turn
     */
    Status handleRequest(GameData data) throws NotEmptyCellException, DivinityPowerException;

    /**
     * method that handles the move and build operations (even the optional ones) of players during their turn and checks if they have won
     */
    Status handleRequest (int oldRow, int oldColumn, GameData gd) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DivinityPowerException, DomedCellException, NotEmptyCellException;
}