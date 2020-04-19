package it.polimi.ingsw.PSP48.server.model.divinities.testingDivinities;

import it.polimi.ingsw.PSP48.server.model.BuildPosition;
import it.polimi.ingsw.PSP48.server.model.DomePosition;
import it.polimi.ingsw.PSP48.server.model.MovePosition;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;

public class DivinityFalsePower extends Divinity {
    @Override
    public Boolean othersMove(MovePosition moveCells) {
        if (moveCells.getMoveRow() == 1 && moveCells.getMoveColumn() == 2) return false;
        return true;
    }

    @Override
    public Boolean othersBuilding(BuildPosition moveCells) {
        if (moveCells.getMoveRow() == 1 && moveCells.getMoveColumn() == 2) return false;
        return true;
    }

    /**
     * @param domeCells the cells where the player wants to add a dome, the cells where the player is and
     * @return true if the divinity doesn't affect the other player's move, false if the divinity blocks the move
     * @author Daniele Mammone
     */
    @Override
    public Boolean othersDome(DomePosition domeCells) {
        if (domeCells.getMoveRow() == 2 && domeCells.getMoveColumn() == 1) return false;
        return true;
    }
}
