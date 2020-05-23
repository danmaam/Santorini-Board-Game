package it.polimi.ingsw.PSP48.client.CLI;

import it.polimi.ingsw.PSP48.client.CLI.ColoursForPrinting;

/**
 * method representing a player for the CLI
 * @author Annalaura Massa
 */
public class Player {
    private String name;
    private String divinity;
    private final String playerSymbol= "\u263a";
    private ColoursForPrinting playerColour;

    /**
     * class constructor initialising all the parameters of a player (used when we are putting it on a cell)
     * @param name is the nickname of the player
     * @param playerColour is the colour assigned to the player
     * @param divinity is the divinity he is playing with
     */
    public Player(String name, ColoursForPrinting playerColour, String divinity){
        this.name=name;
        this.divinity=divinity;
        this.playerColour=playerColour;
    }

    /**
     * class constructor only initialising the player nickname
     * this constructor is used when creating the player list, when the player hasn't chosen other game elements
     * @param name is the nickname of the player
     */
    public Player (String name){
        this.name = name;
    }

    /**
     * getter of the nickname attribute of a player
     * @return the string containing the name
     */
    public String getName() {
        return name;
    }

    /**
     * getter of the divinity of a certain player
     * @return a string with the name of the divinity
     */
    public String getDivinity() {
        return divinity;
    }

    /**
     * getter of the symbol used to represent a player, used when printing the board or player list
     * @return a string containing the symbol
     */
    public String getPlayerSymbol() {
        return playerSymbol;
    }

    /**
     * getter of the colour attribute of a player, for printing purposes
     * @return the colour assigned to a player
     */
    public ColoursForPrinting getPlayerColour() {
        return playerColour;
    }

    /**
     * method used to set the name of a player
     * @param name is the string with the nickname
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * method used to assign a new colour to a player
     * @param playerColour is the new colour
     */
    public void setPlayerColour(ColoursForPrinting playerColour) {
        this.playerColour = playerColour;
    }

    /**
     * method assigning a certain divinity to a player, after he makes his choice
     * @param divinity is the string with the name of the divinity
     */
    public void setDivinity(String divinity) {
        this.divinity = divinity;
    }

    /**
     * method converting a player object into a string, with the player symbol of the right colour
     * @return a string representing the player object
     */
    @Override
    public String toString(){
        return this.playerColour+this.playerSymbol+ColoursForPrinting.reset;
    }
}

