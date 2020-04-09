package it.polimi.ingsw.PSP48.model;

import it.polimi.ingsw.PSP48.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.model.exceptions.*;

import java.util.ArrayList;

/**
 * class that implements a second optional building operation
 * @author Rebecca Marelli
 */
public class OptionalBuilding implements Status
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
     * method handling the two building operations: normal build and dome
     * @return null because it must do nothing in this class
     */
    public Status handleRequest (GameData gd, int oldRow, int oldColumn, Player pl, String name)
    {
        return(null);
    }

    /**
     * method handling a second optional construction by the player, it lists the possible cells then the player can decide what to do
     * @param playerName is the name of the player of the current turn
     * @param p is a reference to the object player, containing the divinity that we use to build
     * @param data contains the board
     * @param startingRow is the starting position
     * @param startingColumn is the starting position
     * @return next state, which can be the end of the game or of the turn
     */
    public Status handleRequest (String playerName, Player p, GameData data, int startingRow, int startingColumn)
    {
        ArrayList<Cell> cellsToBuild= new ArrayList<>();
        ArrayList<Cell> cellsToPutDome= new ArrayList<>();
        ArrayList<Divinity> otherDivinities= new ArrayList<>();

        for (Player pl: data.getPlayersInGame()) //array delle altre divinità in gioco da passare alla funzione che trova le celle valide per costruire
        {
            if (!pl.getName().equals(playerName)) otherDivinities.add(pl.getDivinity());
        }

        //cellsToBuild=p.getDivinity().getValidCellForBuilding(startingColumn, startingRow, otherDivinities, data.getGameBoard());
        //cellsToPutDome=p.getDivinity().getValidCellsToPutDome(startingColumn, startingRow, data.getGameBoard(), otherDivinities);

        //mi sono presa tutte le celle valide per costruire
        //se sono vuote passo al nuovo stato, tanto è una operazione opzionale
        //se non sono vuote chiedo al giocatore se vuole costruire o se vuole saltare questa operazione
        //se sceglie di costruire chiedo quale delle due operazioni di costruzione vuole fare e chiamo la funzione apposita contenuta nella divinità

        for (Player pl: data.getPlayersInGame()) //win condition della divinità crono, da calcolare solo se ho scelto di costruire
        {
            if (pl.getDivinity().getName().equals("Chronus"))
            {
                pl.getDivinity().winCondition(data);
                break;
            }
        }

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
