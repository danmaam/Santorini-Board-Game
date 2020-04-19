package it.polimi.ingsw.PSP48.server.controller;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.GameData;
import it.polimi.ingsw.PSP48.server.model.Player;

import java.util.ArrayList;

/**
 * class that implements the move operation by a player
 * @author Rebecca Marelli
 */
public class Move implements Status
{
    /**
     * method handling the setup states of the game and the beginning of a turn, together with the end of a turn
     * @return null because the state is not handled by this class
     */
    public Status handleRequest(GameData gamedata) throws NotEmptyCellException, DivinityPowerException
    {
        return(null);
    }

    /**
     * method that handles the moves of players during their turn and checks if they have won
     * @param oldRow is the row of the starting position
     * @param oldColumn is the column of the starting position
     * @param gd is an object of the class GameData, which contains the board
     * @return the state following the moving of the player, it depends on the divinity we are using in the turn
     * @throws NotAdiacentCellException if we are trying to move the player in a cell that is not adiacent to him
     * @throws IncorrectLevelException if we are trying to go up more than a level
     * @throws OccupiedCellException if the cell we are moving the player to isa already occupied
     * @throws DivinityPowerException if we don't follow the power of the divinity
     * @throws DomedCellException if the cell we are moving the player to has a dome on it
     * @throws NotEmptyCellException  if the cell is not empty
     */
    public Status handleRequest (int oldRow, int oldColumn, GameData gd) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DivinityPowerException, DomedCellException, NotEmptyCellException
    {
        int newRow, newColumn; //ci mettiamo le nuove posizioni quando chiediamo al giocatore dove si vuole spostare
        ArrayList<Cell> cellsForMoving= new ArrayList<>();
        ArrayList<Divinity> otherDivinities= new ArrayList<>();

        for (Player pl: gd.getPlayersInGame()) //array di altre divinità in gioco, da passare alla funzione che mi fa vedere quali sono le celle valide per la mossa
        {
            if (!pl.getName().equals(gd.getCurrentPlayer().getName())) otherDivinities.add(pl.getDivinity());
        }
        cellsForMoving=gd.getCurrentPlayer().getDivinity().getValidCellForMove(oldColumn, oldRow, gd.getGameBoard(), otherDivinities);
        //abbiamo preso le celle valide per muoversi, ora chiediamo al giocatore corrente in quale si vuole spostare

        //gd.getCurrentPlayer().getDivinity().move(oldColumn, oldRow, newColumn, newRow, gd);
        //quando sappiamo dove si vuole spostare chiamiamo la funzione move

        gd.getCurrentPlayer().getDivinity().winCondition(gd); //alla fine dello spostamento dobbiamo controllare se ha vinto salendo di livello

        //manca la parte in cui decido lo stato nuovo a seconda della divinità->è questa che me lo deve restituire

        return(null);
    }
}
