package it.polimi.ingsw.PSP48;

import java.util.ArrayList;
import it.polimi.ingsw.PSP48.divinities.Divinity;

/**
 * class that implements that status of the game where a player is choosing the divinities that will later be distributed
 * @author Rebecca Marelli
 */
public class PickDivinities implements Status
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
        Status nextState;

        gamedata.getChosenDivinities().add(gamedata.getChosenDivinities().size(), new Divinity()); //vado a modificare direttamente l-arrayList presente in gameData aggiungendo un nuovo elemento

        gamedata.getAvailableDivinities().remove(pickedDivinity); //eliminiamo divinità scelta da quelle disponibili

        if(gamedata.getChosenDivinities().size()<gamedata.getNumberOfPlayers()) //controlliamo che non ci siano altre divinit' da scegliere, in tal caso rimaniamo in questo stato
        {
            nextState=this;
            return(nextState);
        }
        else nextState=colourPick;

        return(nextState);
    }

    /**
     * method used to handle the actions associated to state where players choose their colour
     * @return null because it must not be used by this class, it will be implemented and explained in the corresponding class
     */
    public Status handleRequest (Colour colour, int turnNumber, GameData gamedata, DivinityChoice choiceOfDivinities)
    {
        return(null);
    }

    /**
     *method that handles the actions related to the assignment of divinities to the players who have chosen them
     * @return null because it is not used by this class, so it does nothing
     */
    public Status handleRequest(Divinity divinity, int turnOfPlayer, GameData gameData, GameBegin beginState)
    {
        return(null);
    }
}
