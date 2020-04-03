package it.polimi.ingsw.PSP48;

import java.util.ArrayList;
import it.polimi.ingsw.PSP48.divinities.Divinity;

/**
 * class that handles the state where players take turns to choose their respective divinity: it will be assigned to their workers
 * @author Rebecca Marelli
 */
public class DivinityChoice implements Status
{
    /**
     * method that implements the choice of divinities, it is not used by this class
     * @return null because this class does not handle this state
     */
    public Status handleRequest(Divinity divinity, GameData data, ColourPick colourPickState)
    {
        return(null);
    }

    /**
     * method used to handle the actions associated to state where players choose their workers (by selecting a colour)
     * @return null because it must not be used by this class, it will be implemented and explained in the corresponding class
     */
    public Status handleRequest(Colour colour,int turn, GameData data, DivinityChoice divinityChoiceState)
    {
        return(null);
    }

    /**
     * method that handles the assignment of divinities to the respective workers
     * @param chosenDivinity is the divinity chosen by a player and assigned to it
     * @param turnOfPlayer position of the player in the list contained in game data
     * @param gameData contains some parameters that have to be updated, such as chosenDivinities or the list of players
     * @param gameBeginState new state
     * @return the next state of the game, which is the beginning of the game where players are put on the board
     */
    public Status handleRequest(Divinity chosenDivinity, int turnOfPlayer, GameData gameData, GameBegin gameBeginState)
    {
        Status nextStatus;

        gameData.getPlayer((turnOfPlayer-1)).setDivinity(chosenDivinity);
        gameData.getChosenDivinities().remove(chosenDivinity);

        if (gameData.getChosenDivinities().size()==0) nextStatus=gameBeginState;
        else nextStatus=this;

        return(nextStatus);
    }
}
