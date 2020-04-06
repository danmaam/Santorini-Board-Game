package it.polimi.ingsw.PSP48;

import java.util.ArrayList;
import it.polimi.ingsw.PSP48.divinities.Divinity;

/**
 * class that implements the state of the match where the game is beginning and players choose a position on the board
 * @author Rebecca Marelli
 */
public class GameBegin implements Status
{
    /**
     * method that handles the first status of the game, the choice of the list of divinities, which is not handled by this class
     * @return null because the method mustn't be called in this class, so it does nothing
     */
    public Status handleRequest(Divinity divinity, GameData data, ColourPick colourPickState)
    {
        return(null);
    }

    /**
     *method handling the second state of the game, the choice of colours by the players
     * @return null because it is not necessary to call this method, it must do nothing
     */
    public Status handleRequest(Colour colour, String name, GameData data, DivinityChoice divinityChoiceState)
    {
        return(null);
    }

    /**
     * method handling the assignment of specific divinities to the respective players
     * @return null because it must do nothing, it's not the one called to handle the state implemented by this class
     */
    public Status handleRequest(Divinity divinity, String name, GameData gameData, GameBegin beginState)
    {
        return(null);
    }

    /**
     * method handling the status where players have decided their position on the board and have to be put there
     * @param row line chosen by the player
     * @param column column chosen by the player
     * @param nameOfPlayer player who has chosen the position and needs to be set on the cell
     * @param selectingPlayer contains the divinity power that allows us to put the player on the cells
     * @param gamedata contains the reference to the board and the status, which needs to be checked and updated
     * @param playersToPosition is the number of players that still need to be positioned, it helps to decide the next status of the game
     * @return the updated status of the match
     * @throws NotEmptyCellException if the player has chosen an already occupied cell
     * @throws DivinityPowerException if the power of the divinity is not respected
     */
    public Status handleRequest(int row, int column, String nameOfPlayer, Player selectingPlayer, GameData gamedata, int playersToPosition) throws NotEmptyCellException, DivinityPowerException
    {
        //anche per questa funzione cercare di capire se devo illuminare le celle dalla view oppure prendere all'interno di questo metodo le celle valide per il posizionamento iniziale

        Status newState;

        selectingPlayer.getDivinity().gameSetUp(row, column, gamedata.getGameBoard(), nameOfPlayer); //dal player accedo alla divinità, che contiene la funzione che mi permette di posizionare il giocatore sul tabellone

        if (playersToPosition>0) newState=this;
        //else devo andare al prossimo stato che è l'inizio del turno, dipende da quali divinità ci sono in gioco

        return(newState);
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


