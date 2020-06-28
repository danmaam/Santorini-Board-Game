package it.polimi.ingsw.PSP48.server.model.divinities.testingDivinities;

import it.polimi.ingsw.PSP48.server.model.MoveCoordinates;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;

public class DivFalse extends Divinity {

    private final String name = "False";
    private final Boolean threePlayerSupported = true;

    private int oldLevel;
    private int newLevel;

    @Override
    public Boolean othersMove(MoveCoordinates moveCells, Cell[][] gameBoard) {
        return moveCells.getMoveRow() != 1 || moveCells.getMoveColumn() != 1;
    }

    @Override
    public Boolean othersBuilding(MoveCoordinates moveCells) {
        return moveCells.getMoveRow() != 3 || moveCells.getMoveColumn() != 3;
    }

    /**
     * @param domeCells the cells where the player wants to add a dome, the cells where the player is and
     * @return true if the divinity doesn't affect the other player's move, false if the divinity blocks the move
     * @author Daniele Mammone
     */
    @Override
    public Boolean othersDome(MoveCoordinates domeCells) {
        return domeCells.getMoveRow() != 3 || domeCells.getMoveColumn() != 3;
    }
}