package it.polimi.ingsw.PSP48.model.states;

import it.polimi.ingsw.PSP48.model.Cell;
import it.polimi.ingsw.PSP48.model.GameData;
import it.polimi.ingsw.PSP48.model.Player;
import it.polimi.ingsw.PSP48.model.exceptions.*;

import java.util.ArrayList;

/**
 * class that implements the positioning of players on the board
 * @author Rebecca Marelli
 */
public class GameBegin implements Status
{
    /**
     * method handling the setting of players on the board
     * @param gamedata contains all the parameters of the game
     * @return new state, it can still be the setting of players or the beginning of someone's turn
     * @throws NotEmptyCellException if the cell we chose is already occupied
     * @throws DivinityPowerException if we don't respect the power of the divinity and we position the player in the wrong way
     */
    public Status handleRequest(GameData gamedata) throws NotEmptyCellException, DivinityPowerException
    {
        Status nextState=null;
        ArrayList<Cell> cellsForInitialPositioning= new ArrayList<>();
        int row, column;

        cellsForInitialPositioning=gamedata.getCurrentPlayer().getDivinity().validCellsForInitialPositioning(gamedata.getGameBoard());
        //si mostrano le celle valide e si chiede al giocatore quale cella vuole per posizionarsi, i suoi indici vengono messi in row e column

        //gamedata.getCurrentPlayer().getDivinity().gameSetUp(row, column, gamedata.getGameBoard(), gamedata.getCurrentPlayer().getName());

        if (gamedata.getPlayersInGame().indexOf(gamedata.getCurrentPlayer())<(gamedata.getPlayersInGame().size()-1)) nextState=this;

        //else verrà chiamata la funzione della divinità che restituisce lo stato giusto

        return(null);
    }

    /**
     *method that handles the move and build actions of players during their turn and checks if they have won
     * @return null because it is not handled by this class, thus it does nothing
     */
    public Status handleRequest (int oldRow, int oldColumn, GameData gd) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DivinityPowerException, DomedCellException, NotEmptyCellException
    {
        return(null);
    }
}