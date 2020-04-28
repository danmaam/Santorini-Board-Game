package it.polimi.ingsw.PSP48.server;

public class MoveCoordinates {
    private int workerRow;
    private int workerColumn;
    private int moveRow;

    public int getWorkerRow() {
        return workerRow;
    }

    public int getWorkerColumn() {
        return workerColumn;
    }

    public int getMoveRow() {
        return moveRow;
    }

    public int getMoveColumn() {
        return moveColumn;
    }

    public MoveCoordinates(int workerRow, int workerColumn, int moveRow, int moveColumn) {
        this.workerRow = workerRow;
        this.workerColumn = workerColumn;
        this.moveRow = moveRow;
        this.moveColumn = moveColumn;
    }

    private int moveColumn;
}