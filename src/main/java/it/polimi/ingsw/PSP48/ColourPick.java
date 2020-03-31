package it.polimi.ingsw.PSP48;

import java.util.ArrayList;

/**
 * class that implements the Status interface, handling the state where players get their workers assigned
 * @author Rebecca Marelli
 */
public class ColourPick
{
    /**
     * method that implements the choice of divinities, it is not used by this class
     * @return null because this class does not handle this state
     */
    public Status handleRequest(Divinity divinity, GameData data, ColourPick colourState)
    {
        return(null);
    }

    /**
     * method that assigns the chosen workers to their player
     * @param pickedColour represents the colour picked by the player
     * @param newPlayersAndWorkers needs to be updated by adding the list of workers to the respective players
     * @param gameData contains all the data of the game, it needs to be updated after every choice
     * @param divinityChoice is the next state
     * @return the status that will follow the current one
     */
    public Status handleRequest(Colour pickedColour, int playerTurn, PlayerWorkerConnection newPlayersAndWorkers, GameData gameData, DivinityChoice divinityChoice)
    {
        int i;
        ArrayList<Worker> newWorkers=new ArrayList<Worker>();
        ArrayList<PlayerWorkerConnection> newPlayersInGame= new ArrayList<PlayerWorkerConnection>();
        ArrayList<Colour> newColours= new ArrayList<Colour>();
        Status followingState;

        for(i=0; i<2; i++)
        {
            newWorkers=newWorkers.add(i, new Worker(pickedColour, i+1)); //costruttore non di default definito nella classe worker
        }

        newPlayersAndWorkers.setWorker(newWorkers); //creata la lista di lavoratori, la assegno alla player worker connection del player che li ha scelti
        newPlayersInGame=gameData.getPlayersInGame();//devo aggiungere alle player worker connection già presenti in game data quella nuova e poi aggiornare la lista
        newPlayersInGame.add((playerTurn-1), newPlayersAndWorkers);
        gameData.setPlayersInGame(newPlayersInGame);

        newColours=gameData.getAvailableColours(); //rimuovo dalla lista di colori disponibili quello che è appena stato scelto
        newColours.remove(pickedColour);
        gameData.setAvailableColours(newColours);

        if (newColours.size()==0) followingState=divinityChoice;
        else followingState=this;

        return(followingState);
    }

    /**
     *method that handles the actions related to the assignment of divinities to the workers
     * @return null because it is not used by this class, so it does nothing
     */
    public Status handleRequest(Divinity divinity, ArrayList<Worker> workers, int turnOfPlayer, PlayerWorkerConnection connection, GameData gameData, GameBegin beginState)
    {
        return(null);
    }
}
