package it.polimi.ingsw.PSP48.client.CLI;

/**
 * class used to represent the cells of the board, we use it to print them on the terminal
 * @author Rebecca Marelli
 */
public class CellForPrinting
{
    private int cellRow;
    private int cellColumn;
    private final String emptySpaceInCell = " ";
    private ColoursForPrinting cellColour; //it will be white by default and other colours when we highlight choices to the players
    private Player playerOnCell=null; //at the beginning cells are empty so they don't have any players or constructions on them
    private BuildingsForPrinting buildings=null;
    private DomeForPrinting dome=null;

    /**
     * class constructor initialising row, column and colour of a cell
     * @param row is the row of each cell of the board
     * @param column is the column of each cell of the board
     * @param initialCellColour is the colour of each cell, it only changes when we highlight particular cells to the players
     */
    public CellForPrinting(int row, int column, ColoursForPrinting initialCellColour)
    {
        this.cellRow=row;
        this.cellColumn=column;
        this.cellColour=initialCellColour; //when creating a cell, we just need to put the colour and the position (because it's empty and has no other things on it)
    }

    /**
     * getter of the row of a cell
     * @return an int attribute, the row of the cell we are using
     */
    public int getCellRow()
    {
        return(this.cellRow);
    }

    /**
     * getter of the column of a cell
     * @return the number of the column
     */
    public int getCellColumn()
    {
        return(this.cellColumn);
    }

    /**
     * getter of the empty space symbol contained in the cell
     * @return said symbol (represented using a string)
     */
    public String getEmptySpaceInCell()
    {
        return(this.emptySpaceInCell);
    }

    /**
     * getter of the colour of a cell, used when we are printing it
     * @return said colour
     */
    public ColoursForPrinting getCellColour()
    {
        return(this.cellColour);
    }

    /**
     * method used to change the colour of a cell, in order to highlight or reset the colour of the board
     * @param newCellColour is the colour we want to give to the cell
     */
    public void setCellColour(ColoursForPrinting newCellColour)
    {
        this.cellColour = newCellColour;
    }

    /**
     * getter of the player that may be on a cell
     * @return said player
     */
    public Player getPlayerOnCell()
    {
        return(this.playerOnCell);
    }

    /**
     * method used to add a player on a cell
     * @param newPlayer is the player we are assigning to the cell
     */
    public void setPlayerOnCell(Player newPlayer)
    {
        this.playerOnCell=newPlayer;
    }

    /**
     * getter of the building attribute of a cell
     * @return said building
     */
    public BuildingsForPrinting getBuildings()
    {
        return(this.buildings);
    }

    /**
     * method creating a building on a certain cell
     * @param newBuilding is the building we are putting on the cell
     */
    public void setBuildings(BuildingsForPrinting newBuilding)
    {
        this.buildings=newBuilding;
    }

    /**
     * getter of the dome attribute of a cell
     * @return the dome attribute of the cell
     */
    public DomeForPrinting getDome()
    {
        return(this.dome);
    }

    /**
     * method used to put a dome on a cell
     * @param newDome is the dome attribute we are assigning to the cell
     */
    public void setDome(DomeForPrinting newDome)
    {
        this.dome=newDome;
    }


    /**
     * this method takes a cell object (we have different types of it, based on the other things that can be on a cell) and transforms it into a string in order to print it
     */
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

    /**
     * method used to print a single cell on the screen
     */
    public void printCellOnScreen() //method used to print the single cells on the screen
    {
        System.out.print(this.toString());
    }
}