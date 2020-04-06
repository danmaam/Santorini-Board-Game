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
    public Status handleRequest(Colour colour, String name, GameData data, DivinityChoice divinityChoiceState)
    {
        return(null);
    }

    /**
     * method that handles the assignment of divinities to the respective workers
     * @param chosenDivinity is the divinity chosen by a player and assigned to it
     * @param playerName is the name of the player that has the divinity assigned
     * @param gameData contains some parameters that have to be updated, such as chosenDivinities or the list of players
     * @param gameBeginState new state
     * @return the next state of the game, which is the beginning of the game where players are put on the board
     */
    public Status handleRequest(Divinity chosenDivinity, String playerName, GameData gameData, GameBegin gameBeginState)
    {
        Status nextStatus;

        gameData.getPlayer(playerName).setDivinity(chosenDivinity);
        gameData.getChosenDivinities().remove(chosenDivinity);

        if (gameData.getChosenDivinities().size()==0) nextStatus=gameBeginState;
        else nextStatus=this;

        return(nextStatus);
    }

    /**
     * method handling the status where players have decided their position on the board and have to be put there
     * @return null because the state is not handled by this class
     */
    public Status handleRequest(int row, int column, String name, Player player, GameData gamedata, int playersToPosition) throws NotEmptyCellException, DivinityPowerException
    {
        return (null);
    }

    /**
     * method that checks if a player can complete a turn by moving and then building
     * @return null because the state is not handled by this class
     */
    public Status handleRequest(String name, Player player, GameData data)
    {
        return(null);
    }

    /**
     *method that handles the moves of players during their turn and checks if they have won
     * @return null because it is not handled by this class, thus it does nothing
     */
    public Status handleRequest (int oldRow, int oldColumn, int newRow, int newColumn, Player player, GameData gd) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DivinityPowerException, DomedCellException, NotEmptyCellException
    {
        return(null);
    }

    /**
     * method that checks if a player with a certain divinity can use its power and make a second move and then build
     * @return null because it's not called in this class
     */
    public Status handleRequest(int row, int column, String name, Player player, GameData data)
    {
        return(null);
    }

    /**
     * method handling the two building operations: normal build and dome
     * @return null because it must do nothing in this class
     */
    public Status handleRequest (GameData gd, int oldRow, int oldColumn, Player pl, String name)
    {
        return(null);
    }

    /**
     * method handling a second optional building by the player
     * @return null because it is not handled by this class
     */
    public Status handleRequest (String playerName, Player p, GameData data, int startingRow, int startingColumn)
    {
        return(null);
    }

    /**
     * method handling the end of a turn and setting the right parameters
     * @return null because it is not handled by this class
     */
    public Status handleRequest(Player player)
    {
        return(null);
    }
}
