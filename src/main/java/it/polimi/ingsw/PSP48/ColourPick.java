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
     * @param name is the name of the player
     * @param gameData contains all the data of the game, it needs to be updated after every choice
     * @param divinityChoice is the next state
     * @return the status that will follow the current one
     */
    public Status handleRequest(Colour pickedColour, String name, GameData gameData, DivinityChoice divinityChoice)
    {
        Status followingState=null;

        gameData.getCurrentPlayer().setColour(pickedColour); //settiamo il colore scelto all'interno del player che lo ha selezionato
        gameData.getAvailableColours().remove(pickedColour); //rimuoviamo il colore scelto dalla lista di quelli disponibili

        for(Player p: gameData.getPlayersInGame())
        {
            if(p.getColour()==null) //se c'è un giocatore senza colore devo proseguire con la scelta e rimanere in questo stato
            {
                followingState=this;
                return (followingState);
            }
            else followingState=divinityChoice; //se trovo un colore nel giocatore setto temporaneamente lo stato al prossimo valore, ma proseguo il controllo
        }

        return(followingState);
    }

    /**
     *method that handles the actions related to the assignment of divinities to the players
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
