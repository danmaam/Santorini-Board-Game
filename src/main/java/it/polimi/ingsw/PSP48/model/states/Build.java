package it.polimi.ingsw.PSP48.model.states;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.model.exceptions.*;

import java.util.ArrayList;

/**
 * class that implements the building operation
 * @author Rebecca Marelli
 */
public class Build implements Status
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
     * method that handles the state of the match where a player chooses what to build and then builds
     * @param gameData contains all of the parameters of the match and is used to modify them
     * @param oldRow is the row from where we start building
     * @param oldColumn is the column from where we start building
     * @return the state following the building, it can be an optional building or the end of the turn
     */
    public Status handleRequest (int oldRow, int oldColumn, GameData gameData) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DivinityPowerException, DomedCellException, NotEmptyCellException
    {
        ArrayList<Cell> cellsToBuild= new ArrayList<>();
        ArrayList<Cell> cellsToPutDome= new ArrayList<>();
        ArrayList<Divinity> otherDivinities= new ArrayList<>();

        for (Player pl: gameData.getPlayersInGame()) //array delle altre divinità in gioco da passare alla funzione che trova le celle valide per costruire
        {
            if (!pl.getName().equals(gameData.getCurrentPlayer().getName())) otherDivinities.add(pl.getDivinity());
        }

        //cellsToBuild=gameData.getCurrentPlayer().getDivinity().getValidCellForBuilding(oldColumn, oldRow, otherDivinities, gameData.getGameBoard());
        //cellsToPutDome=gameData.getCurrentPlayer().getDivinity().getValidCellsToPutDome(oldColumn, oldRow, gameData.getGameBoard(), otherDivinities);

        //si mostrano le celle e si chiede al giocatore cosa vuole fare
        //in base alla scelta del giocatore si chiama o il metodo build della divinità oppure il metodo dome, dove costruire ci viene passato in base alla scelta
        //se un giocatore ha la divinità crono può vincere anche se ci sono 5 torri complete, quindi dopo ogni build devo calcolare la sua win condition


        for (Player p: gameData.getPlayersInGame())
        {
            if (p.getDivinity().getName().equals("Chronus"))
            {
                p.getDivinity().winCondition(gameData);
                break;
            }
        }

        //finito di costruire vado nel prossimo stato che può essere o la fine del turno o una costruzione opzionale, dipende dalla divinità

        return(null);
    }
}
