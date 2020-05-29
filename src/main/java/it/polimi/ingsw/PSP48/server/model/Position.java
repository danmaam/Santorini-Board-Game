package it.polimi.ingsw.PSP48.server.model;

import java.io.Serializable;

/**
 * Class used to represent a position on the game board.
 */
public class Position implements Serializable {
    public int row;
    public int column;

    /**
     * Class constructor
     * @param row
     * @param column
     */
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Getter of row
     * @return the row of the position on the game board
     */
    public int getRow() {
        return row;
    }

    /**
     * Getter of column
     * @return the column of the position on the game board
     */
    public int getColumn() {
        return column;
    }

    /**
     * Method used to deep copy a position
     * @return a cloned position
     */
    public Position clone() {
        return new Position(row, column);
    }

    /**
     * Method used to compare two positions
     * @param o the object to compare
     * @return true if the position are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row &&
                column == position.column;
    }
}
