package it.polimi.ingsw.PSP48.server.controller;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.GameData;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.util.ArrayList;

/**
 * class that implements the beginning of a turn, where we check if the player can complete it
 * @author Rebecca Marelli
 */
public class TurnBegin  implements Status
{
    /**
     * method that checks if a player can complete a turn by moving and then building
     * @param gameData contains the boardcell and the status that needs to be updated
     * @return the next status of the game, which can be a normal move or one that relies on a special divinity power
     */
    public Status handleRequest(GameData gameData) throws NotEmptyCellException, DivinityPowerException
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

        gameData.getCurrentPlayer().getDivinity().turnBegin(gameData); //chiama anche questa funzione che all'inizio di ogni turno mi resetta i parametri

        for (Player pl: gameData.getPlayersInGame()) //inizializzo array contenente le divinità diverse da quelle del mio player, da passare alle funzioni che restituiscono le celle valide
        {
            if (!pl.getName().equals(gameData.getCurrentPlayer().getName())) otherDivinities.add(pl.getDivinity());
        }

        playerPositions=gameData.getPlayerPositionsInMap(gameData.getCurrentPlayer().getName()); //ottenuta la lista con le due posizioni
        position1=playerPositions.get(0); //prendo la prima e mi faccio la lista con le celle adiacenti dove posso muovere
        cellsForMove1=gameData.getCurrentPlayer().getDivinity().getValidCellForMove(position1.getRow(), position1.getColumn(), gameData.getGameBoard(), otherDivinities);
        position2=playerPositions.get(1); //faccio la stessa cosa con la seconda posizione
        cellsForMove2=gameData.getCurrentPlayer().getDivinity().getValidCellForMove(position2.getRow(), position2.getColumn(), gameData.getGameBoard(), otherDivinities);

        if (cellsForMove1!=null)
        {
            for (Cell c: cellsForMove1) //per ogni cella valida per lo spostamento, guardo se si può fare una qualunque costruzione
            {
                cellsForBuild1=gameData.getCurrentPlayer().getDivinity().getValidCellForBuilding(c.getRow(), c.getColumn(), otherDivinities, gameData.getGameBoard());
                cellsForDome1=gameData.getCurrentPlayer().getDivinity().getValidCellsToPutDome(c.getRow(), c.getColumn(), gameData.getGameBoard(), otherDivinities);
                //if (cellsForBuild1!=null || cellsForDome1!=null) position1Move=true;
            }
        }

        if (cellsForMove2!=null)
        {
            for (Cell c: cellsForMove2)
            {
                cellsForBuild2=gameData.getCurrentPlayer().getDivinity().getValidCellForBuilding(c.getRow(), c.getColumn(), otherDivinities, gameData.getGameBoard());
                cellsForDome2=gameData.getCurrentPlayer().getDivinity().getValidCellsToPutDome(c.getRow(), c.getColumn(), gameData.getGameBoard(), otherDivinities);
                //if (cellsForBuild2!=null || cellsForDome2!=null) position2Move=true;
            }
        }

        //se da nessuna delle due position si può muovere e poi costruire il giocatore ha perso, altrimenti devo far selezionare da dove vuole muovere


        return(null); //va sistemato con il next state corretto che viene restituito dalla divinità del giocatore
    }

    /**
     *method that handles the move and build operations of players during their turn and checks if they have won
     * @return null because it is not handled by this class, thus it does nothing
     */
    public Status handleRequest (int oldRow, int oldColumn, GameData gd) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DivinityPowerException, DomedCellException, NotEmptyCellException
    {
        return(null);
    }
}