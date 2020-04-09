package it.polimi.ingsw.PSP48.model;

import it.polimi.ingsw.PSP48.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.model.exceptions.*;

import java.util.ArrayList;

/**
 * class that implements the beginning of a turn, where we check if the player can complete it
 * @author Rebecca Marelli
 */
public class TurnBegin  implements Status
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
     * @param playerName is the name of the player in the turn
     * @param playerInTurn is an element of the class player, which gives us access to the power of the divinity
     * @param gameData contains the boardcell and the status that needs to be updated
     * @return the next status of the game, which can be a normal move or one that relies on a special divinity power
     */
    public Status handleRequest(String playerName, Player playerInTurn, GameData gameData)
    {
        ArrayList<Position> playerPositions;
        Position position1, position2;
        ArrayList<Divinity> otherDivinities= new ArrayList<>();
        ArrayList<Cell> cellsForMove1;
        ArrayList<Cell> cellsForMove2;
        ArrayList<Cell> cellsForBuild1;
        ArrayList<Cell> cellsForBuild2;
        ArrayList<Cell> cellsForDome1;
        ArrayList<Cell> cellsForDome2;
        boolean position1Move=false;
        boolean position2Move=false;

        playerInTurn.getDivinity().turnBegin(gameData); //chiama anche questa funzione che all'inizio di ogni turno mi resetta i parametri

        for (Player pl: gameData.getPlayersInGame()) //inizializzo array contenente le divinità diverse da quelle del mio player, da passare alle funzioni che restituiscono le celle valide
        {
            if (!pl.getName().equals(playerName)) otherDivinities.add(pl.getDivinity());
        }

        playerPositions=gameData.getPlayerPositionsInMap(playerName); //ottenuta la lista con le due posizioni
        position1=playerPositions.get(0); //prendo la prima e mi faccio la lista con le celle adiacenti dove posso muovere
        cellsForMove1=playerInTurn.getDivinity().getValidCellForMove(position1.getRow(), position1.getColumn(), gameData.getGameBoard(), otherDivinities);
        position2=playerPositions.get(1); //faccio la stessa cosa con la seconda posizione
        cellsForMove2=playerInTurn.getDivinity().getValidCellForMove(position2.getRow(), position2.getColumn(), gameData.getGameBoard(), otherDivinities);

        if (cellsForMove1!=null)
        {
            for (Cell c: cellsForMove1) //per ogni cella valida per lo spostamento, guardo se si può fare una qualunque costruzione
            {
                cellsForBuild1=playerInTurn.getDivinity().getValidCellForBuilding(c.getRow(), c.getColumn(), otherDivinities, gameData.getGameBoard());
                cellsForDome1=playerInTurn.getDivinity().getValidCellsToPutDome(c.getRow(), c.getColumn(), gameData.getGameBoard(), otherDivinities);
                //if (cellsForBuild1!=null || cellsForDome1!=null) position1Move=true;
            }
        }

        if (cellsForMove2!=null)
        {
            for (Cell c: cellsForMove2)
            {
                cellsForBuild2=playerInTurn.getDivinity().getValidCellForBuilding(c.getRow(), c.getColumn(), otherDivinities, gameData.getGameBoard());
                cellsForDome2=playerInTurn.getDivinity().getValidCellsToPutDome(c.getRow(), c.getColumn(), gameData.getGameBoard(), otherDivinities);
                //if (cellsForBuild2!=null || cellsForDome2!=null) position2Move=true;
            }
        }

        //se da nessuna delle due position si può muovere e poi costruire il giocatore ha perso, altrimenti devo far selezionare da dove vuole muovere


        return(null); //va sistemato con il next state corretto che viene restituito dalla divinità del giocatore
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
