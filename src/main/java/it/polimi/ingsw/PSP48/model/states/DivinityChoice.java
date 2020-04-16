package it.polimi.ingsw.PSP48.model.states;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.model.exceptions.*;

import java.util.ArrayList;

/**
 * class that handles the state where players take turns to choose their respective divinity: it will be assigned to their workers
 * @author Rebecca Marelli
 */
public class DivinityChoice implements Status
{
    /**
     * method that handles the assignment of divinities to the respective players
     * @param gameData contains some parameters that have to be updated, such as chosenDivinities or the list of players
     * @return the next state of the game, which is the beginning of the game where players are put on the board
     */
    public Status handleRequest(GameData gameData) throws NotEmptyCellException, DivinityPowerException
    {
        Status nextStatus;
        Divinity chosenDivinity=null; //per ora inizializzata a null, cambia valore quando conosciamo la scelta del giocatore
        ArrayList<Divinity> chosenDivinitiesToShow;

        chosenDivinitiesToShow=gameData.getChosenDivinities(); //lista delle divinità scelte, da cui si estrae quella che si vuole assegnare ai singoli giocatori
        //mostriamo la lista al giocatore corrente, che fa la sua scelta->risultato è messo in chosenDivinity

        gameData.getCurrentPlayer().setDivinity(chosenDivinity);
        gameData.getChosenDivinities().remove(chosenDivinity);

        if (gameData.getChosenDivinities().size()==0) nextStatus= new GameBegin();
        else nextStatus=this;

        return(nextStatus);
    }

    /**
     *method that handles the move and build operations of players during their turn and checks if they have won
     * @return null because it is not handled by this class, thus it does nothing
     */
    public Status handleRequest (int oldRow, int oldColumn, GameData gd) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DivinityPowerException, DomedCellException, NotEmptyCellException
    {
        return(null);
    }
}
