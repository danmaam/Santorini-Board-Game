package it.polimi.ingsw.PSP48.server.model;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row &&
                column == position.column;
    }

}
