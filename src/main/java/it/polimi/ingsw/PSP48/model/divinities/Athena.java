package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.GameData;
import it.polimi.ingsw.PSP48.model.MovePosition;
import it.polimi.ingsw.PSP48.model.exceptions.*;

public class Athena extends Divinity {
    private final String name = "Athena";
    private final Boolean threePlayerSupported = true;
    private Boolean lastTurnLevelUp = false;

    private int lastWorkerMoveID = 0;


    /**
     * Resets the flag if the player went level up on the last turn
     */
    @Override
    public void turnBegin(GameData gd) {
        lastTurnLevelUp = false;
    }

    public void move(int workerColumn, int workerRow, int moveColumn, int moveRow, GameData gd) throws NotEmptyCellException, DivinityPowerException, IncorrectLevelException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        super.move(workerColumn, workerRow, moveColumn, moveRow, gd);
        if (gd.getCell(moveRow, moveColumn).getLevel() > gd.getCell(workerRow, workerColumn).getLevel())
            lastTurnLevelUp = true;
    }


    @Override
    public Boolean othersMove(MovePosition move) {
        if (move.getDifference() >= 1) return !lastTurnLevelUp;
        return true;
    }


    @Override
    public String getName() {
        return name;
    }
}
