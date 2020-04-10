package it.polimi.ingsw.PSP48.model.states;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.model.exceptions.*;

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

        gamedata.getChosenDivinities().add(gamedata.getChosenDivinities().size(), pickedDivinity); //vado a modificare direttamente l-arrayList presente in gameData aggiungendo un nuovo elemento

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
    public Status handleRequest (Colour colour, String name, GameData gamedata, DivinityChoice choiceOfDivinities)
    {
        return(null);
    }

    /**
     *method that handles the actions related to the assignment of divinities to the players who have chosen them
     * @return null because it is not used by this class, so it does nothing
     */
    public Status handleRequest(Divinity divinity, String name, GameData gameData, GameBegin beginState)
    {
        return(null);
    }

    /**
     * method handling the status where players have decided their position on the board and have to be put there
     * @return null because the state is not handled by this class
     */
    public Status handleRequest(int row, int column, String name, Player player, GameData gamedata, int playersToPosition) throws NotEmptyCellException, DivinityPowerException
    {
        return(null);
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
     * method that checks if a player a certain divinity can use its power and make a second move and then build
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
