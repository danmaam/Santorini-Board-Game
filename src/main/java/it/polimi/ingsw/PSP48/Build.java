package it.polimi.ingsw.PSP48;

import it.polimi.ingsw.PSP48.divinities.Divinity;

import java.util.ArrayList;

/**
 * class that implements the building operation
 * @author Rebecca Marelli
 */
public class Build implements Status
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
     * method that checks if a player with a certain divinity can use its power and make a second move and then build
     * @return null because it's not called in this class
     */
    public Status handleRequest(int row, int column, String name, Player player, GameData data)
    {
        return(null);
    }

    /**
     * method that handles the state of the match where a player chooses what to build and then builds
     * @param gameData contains all of the parameters of the match and is used to modify them
     * @param oldRow is the row from where we start building
     * @param oldColumn is the column from where we start building
     * @param playerInTurn is the player that has to complete the turn
     * @param name is the name of said player
     * @return the state following the building, it can be an optional building or the end of the turn
     */
    public Status handleRequest (GameData gameData, int oldRow, int oldColumn, Player playerInTurn, String name)
    {
        ArrayList<Cell> cellsToBuild= new ArrayList<Cell>();
        ArrayList<Cell> cellsToPutDome= new ArrayList<Cell>();
        ArrayList<Divinity> otherDivinities= new ArrayList<Divinity>();

        for (Player pl: gameData.getPlayersInGame()) //array delle altre divinità in gioco da passare alla funzione che trova le celle valide per costruire
        {
            if (!pl.getName().equals(name)) otherDivinities.add(pl.getDivinity());
        }

        cellsToBuild=playerInTurn.getDivinity().getValidCellForBuilding(oldColumn, oldRow, otherDivinities, gameData.getGameBoard());
        cellsToPutDome=playerInTurn.getDivinity().getValidCellsToPutDome(oldColumn, oldRow, gameData.getGameBoard(), otherDivinities);

        //si mostrano le celle e si chiede al giocatore cosa vuole fare
        //in base alla scelta del giocatore si chiama o il metodo build della divinità oppure il metodo dome, dove costruire ci viene passato in base alla scelta
        //se un giocatore ha la divinità crono può vincere anche se ci sono 5 torri complete, quindi dopo ogni build devo calcolare la sua win condition


        for (Player p: gameData.getPlayersInGame())
        {
            if (p.getDivinity().getName() == "Chronus")
            {
                p.getDivinity().winCondition(gameData);
                break;
            }
        }

        //finito di costruire vado nel prossimo stato che può essere o la fine del turno o una costruzione opzionale, dipende dalla divinità
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
