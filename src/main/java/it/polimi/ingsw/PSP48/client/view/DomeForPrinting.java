package it.polimi.ingsw.PSP48.client.view;

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

    public String getDomeSymbol()
    {
        return(this.domeSymbol);
    }

    public ColoursForPrinting getDomeColour()
    {
        return(this.domeColour);
    }

    public void setDomeColour(ColoursForPrinting newDomeColour)
    {
        this.domeColour=newDomeColour;
    }

    @Override
    /**
     * method converting this object into a string for its printing
     */
    public String toString()
    {
        return(this.domeColour + domeSymbol + ColoursForPrinting.reset); //after we return the symbol in the right colour, we need to reset the colour of the terminal
    }
}