package it.polimi.ingsw.PSP48;

import it.polimi.ingsw.PSP48.divinities.Divinity;

import java.util.ArrayList;

/**
 * class that implements an optional move by the player
 * @author Rebecca Marelli
 */
public class SecondMove implements Status
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
     *method that handles the moves of players during their turn and checks if they have won
     * @return null because it is not handled by this class, thus it does nothing
     */
    public Status handleRequest (int oldRow, int oldColumn, int newRow, int newColumn, Player player, GameData gd) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DivinityPowerException, DomedCellException, NotEmptyCellException
    {
        return(null);
    }

    /**
     * method that checks if a player with a certain  divinity can use its power and make a second move and then build
     * @param row is the row where the player is situated
     * @param column is the column where the player is situated
     * @param name is the name of the player
     * @param player is the object containing all the data of the player, including his divinity and its methods
     * @param data contains the board
     * @return new state
     */
    public Status handleRequest(int row, int column, String name, Player player, GameData data)
    {
        ArrayList<Cell> cellsToMove;
        ArrayList<Cell> cellsToBuild;
        ArrayList<Cell> cellsWithDome;
        ArrayList<Divinity> others= new ArrayList<>();
        boolean completeTurn=false;

        for (Player pl: data.getPlayersInGame()) //inizializzo array contenente le divinità diverse da quelle del mio player, da passare alle funzioni che restituiscono le celle valide
        {
            if (!pl.getName().equals(name)) others.add(pl.getDivinity());
        }

        cellsToMove=player.getDivinity().getValidCellForMove(column, row, data.getGameBoard(), others);

        if (cellsToMove!=null)
        {
            for (Cell cell: cellsToMove)
            {
                cellsToBuild=player.getDivinity().getValidCellForBuilding(column, row, others, data.getGameBoard());
                cellsWithDome=player.getDivinity().getValidCellsToPutDome(column, row, data.getGameBoard(), others);
                //if ((cellsToBuild!=null)||(cellsWithDome!=null)) completeTurn=true;
            }
        }

        //se non posso completare il turno allora non faccio fare la seconda mossa, altrimenti chiedo al giocatore se vuole farla e fa la move (gli illumino le caselle)

        //se giocatore sceglie di muovere lo sposto e calcolo nuovamente la win condition come per la move normale

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
