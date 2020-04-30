package it.polimi.ingsw.PSP48.client;

/**
 * enum class containing the ansi codes used to colour the board, so that we can use the names of colours instead of their code
 *
 * @author Rebecca Marelli, Annalaura Massa
 */
public enum ColoursForPrinting {
    reset("\033[0m"),
    black("\033[30m"), //codes for the brighter version of the colours, to better see them on the terminal
    red("\033[91m"),
    green("\033[92m"),
    yellow("\033[93m"),
    blue("\033[94m"),
    gray("\033[95m"), //we print the gray worker as purple, but we associate the code of purple to colour gray
    cyan("\033[96m"),
    white("\033[37m");

    private String colour;

    /**
     * class constructor
     * @param initialColour is the field we need to initialise (the codes of the colours)
     */
    ColoursForPrinting(String initialColour) {
        this.colour = initialColour;
    }

    public String getColour()
    {
        return(this.colour);
    }

    public void setColour(String changedColour) //we need this method because some elements might change colour during the game, for example to highlight them to the player
    {
        this.colour=changedColour;
    }


    /**
     * we need to convert objects into strings in order to print them
     */
    @Override
    public String toString()
    {
        return(this.colour);
    }
}