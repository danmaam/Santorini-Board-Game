package it.polimi.ingsw.PSP48.server.model.divinities.testingDivinities;

import it.polimi.ingsw.PSP48.server.model.MoveCoordinates;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;

public class DivinityFalsePower extends Divinity {
    @Override
    public Boolean othersMove(MoveCoordinates moveCells, Cell[][] gameBoard) {
        return moveCells.getMoveRow() != 1 || moveCells.getMoveColumn() != 2;
    }

    @Override
    public Boolean othersBuilding(MoveCoordinates moveCells) {
        return moveCells.getMoveRow() != 1 || moveCells.getMoveColumn() != 2;
    }

    /**
     * @param domeCells the cells where the player wants to add a dome, the cells where the player is and
     * @return true if the divinity doesn't affect the other player's move, false if the divinity blocks the move
     * @author Daniele Mammone
     */
    @Override
    public Boolean othersDome(MoveCoordinates domeCells) {
        return domeCells.getMoveRow() != 2 || domeCells.getMoveColumn() != 1;
    }
}
