package it.polimi.ingsw.PSP48.model.states;

        import it.polimi.ingsw.PSP48.model.*;
        import it.polimi.ingsw.PSP48.model.divinities.Divinity;
        import it.polimi.ingsw.PSP48.model.exceptions.*;

        import java.util.ArrayList;

/**
 * class that implements an optional move by the player
 * @author Rebecca Marelli
 */
public class SecondMove implements Status
{

    /**
     * method handling the setup states of the game and the beginning and end of a turn by a player
     * @return null because the state is not handled by this class
     */
    public Status handleRequest(GameData gamedata) throws NotEmptyCellException, DivinityPowerException
    {
        return(null);
    }

    /**
     * method that checks if a player with a certain  divinity can use its power and make a second move and then build
     * @param row is the row where the player is situated
     * @param column is the column where the player is situated
     * @param data contains the board
     * @return new state
     */
    public Status handleRequest(int row, int column, GameData data) throws NotAdiacentCellException, IncorrectLevelException, OccupiedCellException, DivinityPowerException, DomedCellException, NotEmptyCellException
    {
        ArrayList<Cell> cellsToMove;
        ArrayList<Cell> cellsToBuild;
        ArrayList<Cell> cellsWithDome;
        ArrayList<Divinity> others= new ArrayList<>();
        boolean completeTurn=false;

        for (Player pl: data.getPlayersInGame()) //inizializzo array contenente le divinità diverse da quelle del mio player, da passare alle funzioni che restituiscono le celle valide
        {
            if (!pl.getName().equals(data.getCurrentPlayer().getName())) others.add(pl.getDivinity());
        }

        cellsToMove=data.getCurrentPlayer().getDivinity().getValidCellForMove(column, row, data.getGameBoard(), others);

        if (cellsToMove!=null)
        {
            for (Cell cell: cellsToMove)
            {
                cellsToBuild=data.getCurrentPlayer().getDivinity().getValidCellForBuilding(column, row, others, data.getGameBoard());
                cellsWithDome=data.getCurrentPlayer().getDivinity().getValidCellsToPutDome(column, row, data.getGameBoard(), others);
                //if ((cellsToBuild!=null)||(cellsWithDome!=null)) completeTurn=true;
            }
        }

        //se non posso completare il turno allora non faccio fare la seconda mossa, altrimenti chiedo al giocatore se vuole farla e fa la move (gli illumino le caselle)

        //se giocatore sceglie di muovere lo sposto e calcolo nuovamente la win condition come per la move normale

        return(null);
    }
}
