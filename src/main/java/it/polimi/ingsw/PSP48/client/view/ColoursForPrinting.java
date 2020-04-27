package it.polimi.ingsw.PSP48.client.view;

/**
 * enum class containing the ansi codes used to colour the board, so that we can use the names of colours instead of their code
 * @author Rebecca Marelli, Annalaura Massa
 */
public enum ColoursForPrinting
{
    reset ("\033[0m"),
    black ("\033[90m"), //codes for the brighter version of the colours, to better see them on the terminal
    red ("\033[91m"),
    green ("\033[92m"),
    yellow ("\033[93m"),
    blue ("\033[94m"),
    gray ("\033[95m"), //we print the gray worker as purple, but we associate the code of purple to colour gray
    cyan ("\033[96m"),
    white ("\033[97m");

    private String colour;

    ColoursForPrinting(String initialColour) //class constructor
    {
        this.colour=initialColour;
    }

    public String getColour()
    {
        return(this.colour);
    }

    public void setColour(String changedColour) //we need this method because some elements might change colour during the game, for example to highlight them to the player
    {
        this.colour=changedColour;
    }

    @Override
    public String toString() //we need to have a string in order to print coloured symbols and letters
    {
        return(this.colour);
    }
}
