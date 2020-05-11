package it.polimi.ingsw.PSP48.client.GUI;

/**
 * class that represents a cell of the board in the gui
 */
public class GUICell
{
    private int row;
    private int column;
    String player;
    boolean highlighting;
    int cellLevel;
    boolean dome;

    /**
     * class constructor initialising cells, it will be used to initialise the board
     * @param cellRow is the row of the cell we are creating
     * @param cellColumn is the column of the cell we are creating
     * @param colouring tells us if the cell has to be highlighted or not, it has a false value in the beginning
     */
    GUICell(int cellRow, int cellColumn, boolean colouring)
    {
        this.row=cellRow;
        this.column=cellColumn;
        this.highlighting=colouring;
    }

    public int getRow()
    {
        return row;
    }

    public int getColumn()
    {
        return column;
    }

    public String getPlayer()
    {
        return player;
    }

    public int getCellLevel()
    {
        return cellLevel;
    }

    public boolean isHighlighted()
    {
        return highlighting;
    }

    public boolean isDomed()
    {
        return dome;
    }

    public void setPlayer(String newPlayer)
    {
        this.player = newPlayer;
    }

    public void setCellLevel(int newLevel)
    {
        this.cellLevel = newLevel;
    }

    public void setDome(boolean value)
    {
        this.dome = value;
    }

    public void setHighlighting(boolean highlighting)
    {
        this.highlighting = highlighting;
    }
}
