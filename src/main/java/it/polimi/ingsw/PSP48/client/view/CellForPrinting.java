package it.polimi.ingsw.PSP48.client.view;

/**
 * class used to represent the cells of the board, we use it to print them on the terminal
 * @author Rebecca Marelli
 */
public class CellForPrinting
{
    private int cellRow;
    private int cellColumn;
    private final String emptySpaceInCell="";
    private ColoursForPrinting cellColour; //it will be white by default and other colours when we highlight choices to the players
    private Player playerOnCell=null; //at the beginning cells are empty so they don't have any players or constructions on them
    private BuildingsForPrinting buildings=null;
    private DomeForPrinting dome=null;

    public CellForPrinting(int row, int column, ColoursForPrinting initialCellColour) //class constructor
    {
        this.cellRow=row;
        this.cellColumn=column;
        this.cellColour=initialCellColour; //when creating a cell, we just need to put the colour and the position (because it's empty and has no other things on it)
    }

    public int getCellRow()
    {
        return(this.cellRow);
    }

    public int getCellColumn()
    {
        return(this.cellColumn);
    }

    public String getEmptySpaceInCell()
    {
        return(this.emptySpaceInCell);
    }

    public ColoursForPrinting getCellColour()
    {
        return(this.cellColour);
    }

    public Player getPlayerOnCell()
    {
        return(this.playerOnCell);
    }

    public void setPlayerOnCell(Player newPlayer)
    {
        this.playerOnCell=newPlayer;
    }

    public BuildingsForPrinting getBuildings()
    {
        return(this.buildings);
    }

    public void setBuildings(BuildingsForPrinting newBuilding)
    {
        this.buildings=newBuilding;
    }

    public DomeForPrinting getDome()
    {
        return(this.dome);
    }

    public void setDome(DomeForPrinting newDome)
    {
        this.dome=newDome;
    }

    @Override
    public String toString()
    {
        String emptyCell, completeCell, builtCell, domedCell;

        if(playerOnCell==null)
        {
            if (buildings==null && dome==null)
            {
                emptyCell=this.getCellColour() + "[" + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getCellColour() + "]" + ColoursForPrinting.reset;
                return(emptyCell);
            }
            else if (buildings!=null && dome!=null)
            {
                completeCell=this.getCellColour() + "[" + this.getBuildings().getBuildingColour() + this.getBuildings().getBuildingSymbol() + this.getEmptySpaceInCell()+ this.getBuildings().getLevel() + this.getEmptySpaceInCell() + this.getDome().getDomeColour() + this.getDome().getDomeSymbol() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getCellColour() + "]" + ColoursForPrinting.reset;
                return(completeCell);
            }
            else if (buildings!=null && dome==null)
            {
                builtCell=this.getCellColour() + "[" + this.getBuildings().getBuildingColour() + this.getBuildings().getBuildingSymbol() + this.getEmptySpaceInCell() + this.getBuildings().getLevel() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getCellColour() + "]" + ColoursForPrinting.reset;
                return(builtCell);
            }
            else
            {
                domedCell=this.getCellColour() + "[" + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getDome().getDomeColour() + this.getDome().getDomeSymbol() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getCellColour() + "]" + ColoursForPrinting.reset;
                return(domedCell);
            }
        }
        else
        {
            if(buildings==null && dome==null)
            {
                emptyCell=this.getCellColour() + "[" + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getPlayerOnCell().getPlayerColour() + this.getPlayerOnCell().getPlayerSymbol() + this.getCellColour() + "]"+ ColoursForPrinting.reset;
                return(emptyCell);
            }
            else if(buildings!=null && dome!=null)
            {
                completeCell=this.getCellColour() + "[" + this.getBuildings().getBuildingColour() + this.getBuildings().getBuildingSymbol() + this.getEmptySpaceInCell()+ this.getBuildings().getLevel() + this.getEmptySpaceInCell() + this.getDome().getDomeColour() + this.getDome().getDomeSymbol() + this.getEmptySpaceInCell() + this.getPlayerOnCell().getPlayerColour() + this.getPlayerOnCell().getPlayerSymbol() + this.getCellColour() + "]" + ColoursForPrinting.reset;
                return(completeCell);
            }
            else if (buildings!=null && dome==null)
            {
                builtCell=this.getCellColour() + "[" + this.getBuildings().getBuildingColour() + this.getBuildings().getBuildingSymbol() + this.getEmptySpaceInCell() + this.getBuildings().getLevel() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getPlayerOnCell().getPlayerColour() + this.getPlayerOnCell().getPlayerSymbol() + this.getCellColour() + "]" + ColoursForPrinting.reset;
                return(builtCell);
            }
            else
            {
                domedCell=this.getCellColour() + "[" + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getEmptySpaceInCell() + this.getDome().getDomeColour() + this.getDome().getDomeSymbol() + this.getEmptySpaceInCell() + this.getPlayerOnCell().getPlayerColour() + this.getPlayerOnCell().getPlayerSymbol() + this.getCellColour() + "]" + ColoursForPrinting.reset;
                return(domedCell);
            }
        }
    }

    public void printCellOnScreen() //method used to print the single cells on the screen
    {
        System.out.print(this.toString());
    }
}
