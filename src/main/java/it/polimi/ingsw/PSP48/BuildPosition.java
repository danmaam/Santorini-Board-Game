package it.polimi.ingsw.PSP48;

public class BuildPosition {
    private int workerRow;
    private int workerColumn;
    private int moveRow;
    private int moveColumn;
    private int oldLevel;

    public BuildPosition(int workerRow, int workerColumn, int moveRow, int moveColumn, int oldLevel) {
        this.moveColumn = moveColumn;
        this.moveRow = moveRow;
        this.workerColumn = workerColumn;
        this.workerRow = workerRow;
        this.oldLevel = oldLevel;
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

    public int getOldLevel() {
        return oldLevel;
    }
}
