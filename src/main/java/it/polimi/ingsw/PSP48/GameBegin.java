package it.polimi.ingsw.PSP48;

import it.polimi.ingsw.PSP48.divinities.Divinity;

public class GameBegin implements Status {
    /**
     * method that handles the first status of the game, the choice of the list of divinities
     *
     * @param divinity
     * @param data
     * @param colourPickState
     */
    @Override
    public Status handleRequest(Divinity divinity, GameData data, ColourPick colourPickState) {
        return null;
    }

    /**
     * method that assigns the chosen colours to their player
     *
     * @param colour
     * @param turn
     * @param data
     * @param divinityChoiceState
     */
    @Override
    public Status handleRequest(Colour colour, int turn, GameData data, DivinityChoice divinityChoiceState) {
        return null;
    }

    /**
     * method handling the assignment of specific divinities to the respective players
     *
     * @param divinity
     * @param turnOfPlayer
     * @param gameData
     * @param beginState
     */
    @Override
    public Status handleRequest(Divinity divinity, int turnOfPlayer, GameData gameData, GameBegin beginState) {
        return null;
    }
}
