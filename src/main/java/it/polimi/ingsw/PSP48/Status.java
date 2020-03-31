package it.polimi.ingsw.PSP48;

import java.util.ArrayList;

/**
 * interface used to handle all the different actions associated to each state of the game, using the method handleRequest
 * @author Rebecca Marelli
 */
public interface Status
{
    public Status handleRequest(Divinity divinity, GameData data, ColourPick colourPickState);
    public Status handleRequest(Colour colour,int turn, PlayerWorkerConnection playersAndWorkers, GameData data, DivinityChoice divinityChoiceState);
    public Status handleRequest(Divinity divinity, ArrayList<Worker> workers, int turnOfPlayer, PlayerWorkerConnection connection, GameData gameData, GameBegin beginState);
}
