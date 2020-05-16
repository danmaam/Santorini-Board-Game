package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.MovePosition;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;

import java.util.function.Consumer;

public class Athena extends Divinity {

    private Boolean lastTurnLevelUp = false;

    public static Boolean supportedDivinity(int pNum) {
        switch (pNum) {
            case 2:
                return true;
            case 3:
                return true;
        }
        return false;
    }

    /**
     * Resets the flag if the player went level up on the last turn
     */
    @Override
    public Consumer<GameController> turnBegin(Model gd) {
        lastTurnLevelUp = false;
        return super.turnBegin(gd);
    }

    public Consumer<GameController> move(int workerColumn, int workerRow, int moveColumn, int moveRow, Model gd) throws DivinityPowerException, IncorrectLevelException, OccupiedCellException, NotAdjacentCellException, DomedCellException, NoTurnEndException {
        super.move(workerColumn, workerRow, moveColumn, moveRow, gd);
        if (gd.getCell(moveRow, moveColumn).getLevel() > gd.getCell(workerRow, workerColumn).getLevel())
            lastTurnLevelUp = true;
        return GameController::requestBuildDome;
    }


    @Override
    public Boolean othersMove(MovePosition move) {
        if (move.getDifference() >= 1) return !lastTurnLevelUp;
        return true;
    }


    @Override
    public String getName() {
        return "Athena";
    }
}
