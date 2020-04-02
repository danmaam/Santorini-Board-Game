package it.polimi.ingsw.PSP48;

/**
 * Class used to represent a cell in the board game; each cell is identified by a column and a row.
 * The actualLevel attribute represents the level (first, second or third floor) of the building on the cell.
 * If zero, it means that there are no buildings on the cell.
 * Also, a bulding can have a dome over the third floor; isDomed checks if there is a dome on the cell.
 * worker contains the reference to the worker placed on the cell of the board game; it can be set using setWorker.
 */
public class Cell {
    private int column;
    private int row;
    private Worker actualWorker=null;
    private int actualLevel=0;
    private boolean domed=false;

    /**
     * Class constructor
     * @param row index of the row of the cell.
     * @param column index of the column of the cell.
     */
    public Cell(int row, int column){
        this.row=row;
        this.column=column;
    }

    /**
     * Getter of column
     * @return the column number
     */
    public int getColumn() {
        return column;
    }

    /**
     * Getter of row
     * @return the row number
     */
    public int getRow() {
        return row;
    }

    /**
     * Method that increases by one the level of the building on the cell
     * @return true if the level has been built
     */
    public boolean addLevel() throws MaximumLevelReachedException {
        if (this.getLevel() == 3) throw new MaximumLevelReachedException("Maximum Level Reached");
        this.actualLevel=actualLevel+1;
        return true;
    }

    /**
     * Method used to build a dome on the building
     * @return true if the dome has been built
     */
    public boolean addDome() {
        this.domed=true;
        return domed;
    }

    /**
     * Method that checks if there is a dome on the cell
     * @return true if there is a dome, false otherwise.
     */
    public boolean isDomed(){
        return domed;
    }

    /**
     * This method gets the worker on the cell
     * @return the reference to the worker placed on the cell
     */
    public Worker getWorker() {
        return actualWorker;            //da clonare
    }

    /**
     *Getter of actualLevel
     * @return the level of the building currently set on the cell
     */
    public int getLevel() {
        return actualLevel;
    }

    /**
     *method that can change the worker that is placed on the cell of the board game
     * @param worker the worker you want to place on the cell
     */
    public void setWorker (Worker worker)
    {
        this.actualWorker=worker;
    }
}

