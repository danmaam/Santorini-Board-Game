package it.polimi.ingsw.PSP48.divinities;

import it.polimi.ingsw.PSP48.*;

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
    public void turnBegin(GameData gd) {
        lastTurnLevelUp = false;
    }

    public void move(int workerColumn, int workerRow, int moveRow, int moveColumn, GameData gd) throws NotEmptyCellException, DivinityPowerException, IncorrectLevelException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        super.move(workerColumn, workerRow, moveRow, moveColumn, gd);
        if (gd.getCell(moveRow, moveColumn).getLevel() > gd.getCell(workerColumn, workerColumn).getLevel())
            lastTurnLevelUp = true;
    }


    @Override
    public Boolean othersMove(MovePosition move) {
        return lastTurnLevelUp;
    }


}
