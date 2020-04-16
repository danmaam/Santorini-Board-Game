package it.polimi.ingsw.PSP48.model.states;

import it.polimi.ingsw.PSP48.model.*;
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
     * method handling the setup, turn begin and turn end states
     * @return null because the state is not handled by this class
     */
    public Status handleRequest(GameData gamedata) throws NotEmptyCellException, DivinityPowerException
    {
        return(null);
    }

    /**
     * method handling a second optional construction by the player, it lists the possible cells then the player can decide what to do
     * @param data contains the board
     * @param startingRow is the starting position
     * @param startingColumn is the starting position
     * @return next state, which can be the end of the game or of the turn
     */
    public Status handleRequest (int startingRow, int startingColumn, GameData data) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DivinityPowerException, DomedCellException, NotEmptyCellException
    {
        ArrayList<Cell> cellsToBuild= new ArrayList<>();
        ArrayList<Cell> cellsToPutDome= new ArrayList<>();
        ArrayList<Divinity> otherDivinities= new ArrayList<>();

        for (Player pl: data.getPlayersInGame()) //array delle altre divinità in gioco da passare alla funzione che trova le celle valide per costruire
        {
            if (!pl.getName().equals(data.getCurrentPlayer().getName())) otherDivinities.add(pl.getDivinity());
        }

        //cellsToBuild=data.getCurrentPlayer().getDivinity().getValidCellForBuilding(startingColumn, startingRow, otherDivinities, data.getGameBoard());
        //cellsToPutDome=data.getCurrentPlayer().getDivinity().getValidCellsToPutDome(startingColumn, startingRow, data.getGameBoard(), otherDivinities);

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
}
