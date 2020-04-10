package it.polimi.ingsw.PSP48.model;

import it.polimi.ingsw.PSP48.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.model.exceptions.*;
import it.polimi.ingsw.PSP48.model.states.ColourPick;
import it.polimi.ingsw.PSP48.model.states.DivinityChoice;

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
    Status handleRequest(Colour colour, String name, GameData data, DivinityChoice divinityChoiceState);

    /**
     *method handling the assignment of specific divinities to the respective players
     */
    Status handleRequest(Divinity divinity, String name, GameData gameData, GameBegin beginState);

    /**
     * method handling the status where players have decided their position on the board and have to be put there
     */
    Status handleRequest(int row, int column, String name, Player player, GameData gamedata, int playersToPosition) throws NotEmptyCellException, DivinityPowerException;

    /**
     * method that checks if a player can complete a turn by moving and then building
     */
    Status handleRequest(String name, Player player, GameData data);

    /**
     * method that handles the moves of players during their turn and checks if they have won
     */
    Status handleRequest (int oldRow, int oldColumn, int newRow, int newColumn, Player player, GameData gd) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DivinityPowerException, DomedCellException, NotEmptyCellException;

    /**
     * method that checks if a player with a certain divinity can use its power and make a second move and then build
     */
    Status handleRequest(int row, int column, String name, Player player, GameData data);

    /**
     * method that handles the building operations
     */
    Status handleRequest (GameData gd, int oldRow, int oldColumn, Player pl, String name);

    /**
     * method handling a second optional building by the player
     */
    Status handleRequest (String playerName, Player p, GameData data, int startingRow, int startingColumn);

    /**
     * method that handles the activities related to the end of a turn
     */
    Status handleRequest(Player player);
}
