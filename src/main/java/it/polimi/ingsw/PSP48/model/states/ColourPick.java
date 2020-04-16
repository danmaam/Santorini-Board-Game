package it.polimi.ingsw.PSP48.model.states;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.model.exceptions.*;

import java.util.ArrayList;

/**
 * class that implements the Status interface, handling the state where players get their workers assigned
 * @author Rebecca Marelli
 */
public class ColourPick implements Status
{
    /**
     * method that assigns the chosen colour to its player
     * @param gameData contains all the data of the game, it needs to be updated after every choice
     * @return the status that will follow the current one
     */
    public Status handleRequest(GameData gameData) throws NotEmptyCellException, DivinityPowerException
    {
        Status followingState=null;
        Colour pickedColour=null;
        ArrayList<Colour> coloursToShow;

        coloursToShow=gameData.getAvailableColours(); //prendo la lista di colori disponibili per farli scegliere al giocatore corrente
        //una volta scelto il colore, questo viene messo in pickedColour e si aggiornano le liste, assieme al giocatore che ha scelto il colore

        gameData.getCurrentPlayer().setColour(pickedColour); //settiamo il colore scelto all'interno del player che lo ha selezionato
        gameData.getAvailableColours().remove(pickedColour); //rimuoviamo il colore scelto dalla lista di quelli disponibili

        for(Player p: gameData.getPlayersInGame())
        {
            if(p.getColour()==null) //se c'è un giocatore senza colore devo proseguire con la scelta e rimanere in questo stato
            {
                followingState=this;
                return (followingState);
            }
            else followingState=new DivinityChoice(); //se trovo un colore nel giocatore setto temporaneamente lo stato al prossimo valore, ma proseguo il controllo
        }

        return(followingState);
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
