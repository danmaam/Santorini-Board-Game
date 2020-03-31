package it.polimi.ingsw.PSP48;

import java.util.ArrayList;

/**
 * class that handles the state where players take turns to choose their respective divinity: it will be assigned to their workers
 * @author Rebecca Marelli
 */
public class DivinityChoice
{
    /**
     * method that implements the choice of divinities, it is not used by this class
     * @return null because this class does not handle this state
     */
    public Status handleRequest(Divinity divinity, GameData data, ColourPick colourPickState)
    {
        return(null);
    }

    /**
     * method used to handle the actions associated to state where players choose their workers (by selecting a colour)
     * @return null because it must not be used by this class, it will be implemented and explained in the corresponding class
     */
    public Status handleRequest(Colour colour,int turn, PlayerWorkerConnection playersAndWorkers, GameData data, DivinityChoice divinityChoiceState)
    {
        return(null);
    }

    /**
     * method that handles the assignment of divinities to the respective workers
     * @param chosenDivinity is the divinity chosen by a player and assigned to the workers
     * @param targetWorkers workers that have to be updated with the chosen divinity
     * @param turnOfPlayer position of the player in the list contained in game data, which needs to be updated after the workers have been updated too
     * @param playersConnection object containing players and their workers, which is updated with the new workers
     * @param gameData contains some parameters that have to be updated, such as chosenDivinities or the list of connections
     * @param gameBeginState new state
     * @return the next state of the game, which is the beginning of the game where workers are put on the board
     */
    public Status handleRequest(Divinity chosenDivinity, ArrayList<Worker> targetWorkers, int turnOfPlayer, PlayerWorkerConnection playersConnection, GameData gameData, GameBegin gameBeginState)
    {
        ArrayList<Divinity> tempChosen;
        Status nextStatus;

        gameData.addSelectedDivinity(chosenDivinity, targetWorkers); //cambio i worker della lista aggiungendo le divinità

        playersConnection.setWorkers(targetWorkers); //sistemo la connection mettendo la lista con i worker aggiornati, uso metodo di playerWorkerConnection

        gameData.setConnection(playersConnection, (turnOfPlayer-1)); //aggiorno la connection contenuta nella lista presente in game data

        tempChosen=gameData.getChosenDivinities();
        tempChosen.remove(chosenDivinity);
        gameData.setChosenDivinities(tempChosen);

        if (tempChosen.size()==0) nextStatus=gameBeginState;

        else nextStatus=this;

        return(nextStatus);
    }
}
