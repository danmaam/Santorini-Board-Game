package it.polimi.ingsw.PSP48.model.states;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.model.exceptions.*;

import java.util.ArrayList;

/**
 * class that implements that status of the game where a player is choosing the divinities that will later be distributed
 * @author Rebecca Marelli
 */
public class PickDivinities implements Status
{
    /**
     * method that handles the choice of the list of divinities, which represents the status of this specific class
     * @param gamedata is the object that contains all the data of the game, where we put the updated lists of divinities
     * @return the status following the current one, only if we have completed the activities associated to it
     */
    public Status handleRequest(GameData gamedata) throws NotEmptyCellException, DivinityPowerException
    {
        Status nextState;
        Divinity pickedDivinity=null; //per ora la inizializziamo a null, poi viene cambiata quando il giocatore fa la sua scelta
        ArrayList<Divinity> divinitiesToShow;

        divinitiesToShow=gamedata.getAvailableDivinities(); //prendo la lista di divinità disponibili da mostrare al giocatore

        //quando giocatore fa la sua scelta, il risultato viene messo in pickedDivinity, che viene poi usata per aggiornare la lista di divinità scelte

        gamedata.getChosenDivinities().add(gamedata.getChosenDivinities().size(), pickedDivinity); //vado a modificare direttamente l-arrayList presente in gameData aggiungendo un nuovo elemento

        gamedata.getAvailableDivinities().remove(pickedDivinity); //eliminiamo divinità scelta da quelle disponibili

        if(gamedata.getChosenDivinities().size()<gamedata.getNumberOfPlayers()) //controlliamo che non ci siano altre divinit' da scegliere, in tal caso rimaniamo in questo stato
        {
            nextState=this;
            return(nextState);
        }
        else nextState=new ColourPick();

        return(nextState);
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
