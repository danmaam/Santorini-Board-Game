package it.polimi.ingsw.PSP48;

import java.util.ArrayList;
import it.polimi.ingsw.PSP48.divinities.Divinity;

/**
 * class that implements the Status interface, handling the state where players get their workers assigned
 * @author Rebecca Marelli
 */
public class ColourPick implements Status
{
    /**
     * method that implements the choice of divinities, it is not used by this class
     * @return null because this class does not handle this state
     */
    public Status handleRequest(Divinity divinity, GameData data, ColourPick colourState)
    {
        return(null);
    }

    /**
     * method that assigns the chosen colour to its player
     * @param pickedColour represents the colour picked by the player
     * @param playerTurn is the position of the player in the list of all players
     * @param gameData contains all the data of the game, it needs to be updated after every choice
     * @param divinityChoice is the next state
     * @return the status that will follow the current one
     */
    public Status handleRequest(Colour pickedColour, int playerTurn, GameData gameData, DivinityChoice divinityChoice)
    {
        Status followingState;

        gameData.getPlayer((playerTurn-1)).setColour(pickedColour); //settiamo il colore scelto all'interno del player che lo ha selezionato
        gameData.getAvailableColours().remove(pickedColour); //rimuoviamo il colore scelto dalla lista di quelli disponibili

        if (gameData.getAvailableColours().size()==0) followingState=divinityChoice;
        else followingState=this;

        return(followingState);
    }

    /**
     *method that handles the actions related to the assignment of divinities to the players
     * @return null because it is not used by this class, so it does nothing
     */
    public Status handleRequest(Divinity divinity, int turnOfPlayer, GameData gameData, GameBegin beginState)
    {
        return(null);
    }
}
