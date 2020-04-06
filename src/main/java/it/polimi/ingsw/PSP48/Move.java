package it.polimi.ingsw.PSP48;

import it.polimi.ingsw.PSP48.divinities.Divinity;

/**
 * class that implements the move operation by a player
 * @author Rebecca Marelli
 */
public class Move implements Status
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
     * method that handles the moves of players during their turn and checks if they have won
     * @param oldRow is the row of the starting position
     * @param oldColumn is the column of the starting position
     * @param newRow is the row where the player needs to be put
     * @param newColumn is the column where the player needs to be put
     * @param playerToMove is the player we are moving
     * @param gd is an object of the class GameData, which contains the board
     * @return the state following the moving of the player, it depends on the divinity we are using in the turn
     * @throws NotAdiacentCellException if we are trying to move the player in a cell that is not adiacent to him
     * @throws IncorrectLevelException if we are trying to go up more than a level
     * @throws OccupiedCellException if the cell we are moving the player to isa already occupied
     * @throws DivinityPowerException if we don't follow the power of the divinity
     * @throws DomedCellException if the cell we are moving the player to has a dome on it
     * @throws NotEmptyCellException  if the cell is not empty
     */
    public Status handleRequest (int oldRow, int oldColumn, int newRow, int newColumn, Player playerToMove, GameData gd) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DivinityPowerException, DomedCellException, NotEmptyCellException
    {
        //quando è fatta la view cercare di capire se la parte che illumina le casella va dentro quella oppure qui

        playerToMove.getDivinity().move(oldColumn, oldRow, newColumn, newRow, gd);

        playerToMove.getDivinity().winCondition(gd);

        //manca la parte in cui decido lo stato nuovo a seconda della divinità->è questa che me lo deve restituire

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
