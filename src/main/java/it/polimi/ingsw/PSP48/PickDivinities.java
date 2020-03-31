package it.polimi.ingsw.PSP48;

import java.util.ArrayList;

/**
 * class that implements that status of the game where a player is choosing the divinities that will later be distributed
 * @author Rebecca Marelli
 */
public class PickDivinities
{
    /**
     * method that handles all the activities linked to the state represented by the class, it's the one that will be called by state action
     * @param pickedDivinity represents the divinity picked by a player
     * @param gamedata is the object that contains all the data of the game, where we put the updated lists
     * @param colourPick is the status that follows PickDivinities
     * @return the status following the current one, only if we have completed the activities associated to it
     */
    public Status handleRequest(Divinity pickedDivinity, GameData gamedata, ColourPick colourPick)
    {
        ArrayList<Divinity> updatedChosenDivinities, updatedAvailableDivinities;
        Status nextState;

        updatedChosenDivinities=gamedata.getChosenDivinities(); //parte del codice in cui aggiungo la divinità scelta e aggiorno la lista
        updatedChosenDivinities.add(updatedChosenDivinities.size(), new Divinity());
        gamedata.setChosenDivinities(updatedChosenDivinities);

        updatedAvailableDivinities=gamedata.getAvailableDivinities(); //eliminiamo divinità scelta da quelle disponibili
        updatedAvailableDivinities.remove(pickedDivinity);
        gamedata.setAvailableDivinities(updatedAvailableDivinities);

        if(updatedChosenDivinities.size()<gamedata.getNumberOfPlayers())
        {
            nextState=this;
            return(nextState);
        }
        else nextState=colourPick;

        return(nextState);
    }

    /**
     * method used to handle the actions associated to state where players choose their workers (by selecting a colour)
     * @return null because it must not be used by this class, it will be implemented and explained in the corresponding class
     */
    public Status handleRequest (Colour colour, int turnNumber, PlayerWorkerConnection connection, GameData gamedata, DivinityChoice choiceOfDivinities)
    {
        return(null);
    }

    /**
     *method that handles the actions related to the assignment of divinities to the workers
     * @return null because it is not used by this class, so it does nothing
     */
    public Status handleRequest(Divinity divinity, ArrayList<Worker> workers, int turnOfPlayer, PlayerWorkerConnection connection, GameData gameData, GameBegin beginState)
    {
        return(null);
    }
}
