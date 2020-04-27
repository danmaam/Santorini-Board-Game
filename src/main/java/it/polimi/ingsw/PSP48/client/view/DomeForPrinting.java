package it.polimi.ingsw.PSP48.client.view;

/**
 * class used to represent the dome on the board of the game
 * @author Rebecca Marelli
 */
public class DomeForPrinting
{
    private final String domeSymbol="◘"; //it's the symbol used to represent the dome when we print the board, it cannot be modified (just its colour)
    private ColoursForPrinting domeColour;

    public DomeForPrinting(ColoursForPrinting initialDomeColour) //class constructor, when creating an object we also initialize its colour
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

    @Override
    public String toString()
    {
        return(this.domeColour + domeSymbol + ColoursForPrinting.reset); //after we return the symbol in the right colour, we need to reset the colour of the terminal
    }
}
