package it.polimi.ingsw.PSP48.model.states;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.model.exceptions.*;

/**
 * class that implements the end-of-turn operations
 * @author Rebecca Marelli
 */
public class TurnEnd implements Status
{
    /**
     * method that handles the activities related to the end of the turn
     * @param gamedata contains the current player that we use to call the divinity power functions
     * @return the beginning of the new turn, which is related to the divinity of the next player
     */
    public Status handleRequest(GameData gamedata) throws NotEmptyCellException, DivinityPowerException
    {
        gamedata.getCurrentPlayer().getDivinity().turnEnd(); //chiamo il metodo per le operazioni di fine turno

        //il prossimo stato sarà l'inizio del turno, che però dipende dalla divinità del prossimo giocatore

        return(null);
    }

    /**
     *method that handles the move and build actions  of players during their turn and checks if they have won
     * @return null because it is not handled by this class, thus it does nothing
     */
    public Status handleRequest (int oldRow, int oldColumn, GameData gd) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DivinityPowerException, DomedCellException, NotEmptyCellException
    {
        return(null);
    }
}