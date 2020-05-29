package it.polimi.ingsw.PSP48.server.model;

import it.polimi.ingsw.PSP48.server.model.exceptions.MaximumLevelReachedException;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class used to represent a cell in the board game; each cell is identified by a column and a row.
 * The actualLevel attribute represents the level (first, second or third floor) of the building on the cell.
 * If zero, it means that there are no buildings on the cell.
 * Also, a building can have a dome over the third floor; isDomed checks if there is a dome on the cell.
 * worker contains the reference to the worker placed on the cell of the board game; it can be set using setWorker.
 */
public class Cell implements Cloneable, Serializable {
    private int column;
    private int row;
    private String player = null;
    private int actualLevel = 0;
    private boolean domed = false;

    /**
     * Class constructor
     * @param row    index of the row of the cell.
     * @param column index of the column of the cell.
     */
    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Class constructor
     * @param row     index of the row of the cell.
     * @param column  index of the column of the cell.
     * @param level   level of the building on the cell.
     * @param player  name of the player on the cell.
     * @param domed   if the cell has a dome or not.
     */
    public Cell(int row, int column, int level, String player, Boolean domed) {
        this.row = row;
        this.column = column;
        this.actualLevel = level;
        this.player = player;
        this.domed = domed;
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
     */
    public void addDome() {
        this.domed=true;
    }

    /**
     * Method that checks if there is a dome on the cell
     * @return true if there is a dome, false otherwise.
     */
    public boolean isDomed(){
        return domed;
    }

    /**
     * This method gets the player on the cell
     * @return the reference to the player placed on the cell
     */
    public String getPlayer() {
        return player;
    }

    /**
     *Getter of actualLevel
     * @return the level of the building currently set on the cell
     */
    public int getLevel() {
        return actualLevel;
    }

    /**
     * method that can change the player that is placed on the cell of the board game
     * @param newPlayer the player you want to place on the cell
     */
    public void setPlayer(String newPlayer) {
        this.player = newPlayer;
    }

    /**
     * Method used to deep copy a cell
     * @return a cloned cell
     */
    @Override
    public Object clone() {
        return new Cell(row, column, actualLevel, player, domed);
    }

    /**
     * Method used to compare two cells
     * @param o the object to compare
     * @return true if the cells are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return column == cell.column &&
                row == cell.row &&
                actualLevel == cell.actualLevel &&
                domed == cell.domed &&
                Objects.equals(player, cell.player);
    }

    /**
     * Method used to set the level of the building set on the cell
     * @param l the level of the building on the cell.
     */
    public void setActualLevel(int l) {
        actualLevel = l;
    }
}


