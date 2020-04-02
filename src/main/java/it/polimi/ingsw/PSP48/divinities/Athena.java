package it.polimi.ingsw.PSP48.divinities;

import it.polimi.ingsw.PSP48.Cell;
import it.polimi.ingsw.PSP48.MovePosition;

import java.util.ArrayList;

public class Athena extends Divinity {
    private final String name = "Athena";
    private final Boolean threePlayerSupported = true;
    private Boolean lastTurnLevelUp = false;

    private int lastWorkerMoveID = 0;
    private int oldLevel;
    private int newLevel;

    /**
     * Resets the flag if the player went level up on the last turn
     */
    @Override
    public void turnBegin() {
        lastTurnLevelUp = false;
    }

    public ArrayList<Cell> move(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, Cell[][] gameCells) {
        lastWorkerMoveID = gameCells[WorkerRow][WorkerColumn].getWorker().getId();
        oldLevel = gameCells[WorkerRow][WorkerColumn].getLevel();
        newLevel = gameCells[moveRow][moveColumn].getLevel();
        if (newLevel > oldLevel) lastTurnLevelUp = true;
        Cell cellWhereToMove = gameCells[moveRow][moveColumn];
        cellWhereToMove.setWorker(gameCells[WorkerRow][WorkerColumn].getWorker());
        gameCells[WorkerRow][WorkerColumn].setWorker(null);
        ArrayList<Cell> modifiedCells = new ArrayList<>();
        modifiedCells.add(cellWhereToMove);
        modifiedCells.add(gameCells[WorkerRow][WorkerColumn]);

        return modifiedCells;
    }


    @Override
    public Boolean othersMove(MovePosition move) {
        return lastTurnLevelUp;
    }


}
