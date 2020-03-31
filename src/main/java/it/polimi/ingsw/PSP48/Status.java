package it.polimi.ingsw.PSP48;

import java.util.ArrayList;

/**
 * interface used to handle all the different actions associated to each state of the game, using the method handleRequest
 * @author Rebecca Marelli
 */
public interface Status
{
    /**
     * method that handles the first status of the game, the choice of the list of divinities
     */
    public Status handleRequest(Divinity divinity, GameData data, ColourPick colourPickState);

    /**
     * method that assigns the chosen workers to their player
     */
    public Status handleRequest(Colour colour,int turn, PlayerWorkerConnection playersAndWorkers, GameData data, DivinityChoice divinityChoiceState);

    /**
     *method handling the assignment of specific divinities to the respective workers
     */
    public Status handleRequest(Divinity divinity, ArrayList<Worker> workers, int turnOfPlayer, PlayerWorkerConnection connection, GameData gameData, GameBegin beginState);
}
