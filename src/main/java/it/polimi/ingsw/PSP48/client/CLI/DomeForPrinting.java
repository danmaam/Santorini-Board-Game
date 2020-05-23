package it.polimi.ingsw.PSP48.client.CLI;

import it.polimi.ingsw.PSP48.client.CLI.ColoursForPrinting;

/**
 * class used to represent the dome on the board of the game
 * @author Rebecca Marelli
 */
public class DomeForPrinting
{
    private final String domeSymbol="◘"; //it's the symbol used to represent the dome when we print the board, it cannot be modified (just its colour)
    private ColoursForPrinting domeColour;

    /**
     * class constructor
     * @param initialDomeColour is the colour we assign to the dome when we are creating it
     */
    public DomeForPrinting(ColoursForPrinting initialDomeColour)
    {
        this.domeColour=initialDomeColour;
    }

    /**
     * getter of the dome symbol, used to print a cell and its content
     * @return the string containing the symbol
     */
    public String getDomeSymbol()
    {
        return(this.domeSymbol);
    }

    /**
     * getter of the dome colour, for printing purposes
     * @return the colour attribute of a dome object
     */
    public ColoursForPrinting getDomeColour()
    {
        return(this.domeColour);
    }

    /**
     * method used to change the colour of a dome
     * @param newDomeColour is the updated colour
     */
    public void setDomeColour(ColoursForPrinting newDomeColour)
    {
        this.domeColour=newDomeColour;
    }

    /**
     * method converting this object into a string for its printing
     */
    @Override
    public String toString()
    {
        return(this.domeColour + domeSymbol + ColoursForPrinting.reset); //after we return the symbol in the right colour, we need to reset the colour of the terminal
    }
}