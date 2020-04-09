package it.polimi.ingsw.PSP48.model;

public class MovePosition {
    private int workerRow;
    private int workerColumn;
    private int moveRow;
    private int moveColumn;
    private int difference;

    public MovePosition(int workerRow, int workerColumn, int moveRow, int moveColumn, int difference) {
        this.moveColumn = moveColumn;
        this.moveRow = moveRow;
        this.workerColumn = workerColumn;
        this.workerRow = workerRow;
        this.difference = difference;
    }

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

    public int getDifference() {
        return difference;
    }
}
