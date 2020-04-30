package it.polimi.ingsw.PSP48.server.model;

public class Position {
    public int row;
    public int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Position clone() {
        return new Position(row, column);
    }
}
