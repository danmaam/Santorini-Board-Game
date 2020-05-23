package it.polimi.ingsw.PSP48.client.CLI;

/**
 * class used to represent a building for the CLI
 * @author Annalaura Massa
 */
public class BuildingsForPrinting {
    private int level;
    private final String buildingSymbol = "\u25a0";
    private ColoursForPrinting buildingColour;

    /**
     * class constructor
     * @param level is the level we first assign to a building when creating it
     * @param buildingColour is the colour we choose to give to the building
     */
    public BuildingsForPrinting (int level, ColoursForPrinting buildingColour){
        this.level=level;
        this.buildingColour=buildingColour;
    }

    /**
     * getter of the building level attribute
     * @return said attribute
     */
    public int getLevel() {
        return level;
    }

    /**
     * getter of the symbol of a building, used when printing a cell
     * @return the symbol chosen to represent a building
     */
    public String getBuildingSymbol() {
        return buildingSymbol;
    }

    /**
     * getter of the building colour, for its printing
     * @return the colour attribute of the building
     */
    public ColoursForPrinting getBuildingColour() {
        return buildingColour;
    }

    /**
     * method used to change the colour of a building
     * @param newBuildingColour is the new colour we want to give to the building
     */
    public void setBuildingColour(ColoursForPrinting newBuildingColour)
    {
        this.buildingColour = newBuildingColour;
    }

    /**
     * method used to update the level of the buildings on a cell
     * @param level is the updated level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * method converting a building object into a string of the colour of the building
     * @return a string corresponding to the building object
     */
    @Override
    public String toString(){
        return this.buildingColour+this.buildingSymbol+ColoursForPrinting.reset;
    }
}