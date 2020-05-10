package it.polimi.ingsw.PSP48.client.CLI;

/**
 * @author Annalaura Massa
 */
public class BuildingsForPrinting {
    private int level;
    private final String buildingSymbol = "\u25a0";
    private ColoursForPrinting buildingColour;

    public BuildingsForPrinting (int level, ColoursForPrinting buildingColour){
        this.level=level;
        this.buildingColour=buildingColour;
    }

    public int getLevel() {
        return level;
    }

    public String getBuildingSymbol() {
        return buildingSymbol;
    }

    public ColoursForPrinting getBuildingColour() {
        return buildingColour;
    }

    public void setBuildingColour(ColoursForPrinting newBuildingColour)
    {
        this.buildingColour = newBuildingColour;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String toString(){
        return this.buildingColour+this.buildingSymbol+ColoursForPrinting.reset;
    }
}