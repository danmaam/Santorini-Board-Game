package it.polimi.ingsw.PSP48;

import java.util.ArrayList;
import it.polimi.ingsw.PSP48.divinities.Divinity;

/**
 * interface used to handle all the different actions associated to each state of the game, using the method handleRequest
 * @author Rebecca Marelli
 */
public interface Status
{
    /**
     * method that handles the first status of the game, the choice of the list of divinities
     */
    Status handleRequest(Divinity divinity, GameData data, ColourPick colourPickState);

    /**
     * method that assigns the chosen colours to their player
     */
    Status handleRequest(Colour colour, int turn, GameData data, DivinityChoice divinityChoiceState);

    /**
     *method handling the assignment of specific divinities to the respective players
     */
    Status handleRequest(Divinity divinity, int turnOfPlayer, GameData gameData, GameBegin beginState);
}
