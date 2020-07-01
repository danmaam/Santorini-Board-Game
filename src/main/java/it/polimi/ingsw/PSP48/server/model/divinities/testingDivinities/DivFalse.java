package it.polimi.ingsw.PSP48.server.model.divinities.testingDivinities;

import it.polimi.ingsw.PSP48.server.model.ActionCoordinates;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;

/**
 * Divinity used for testing purposes
 */
public class DivFalse extends Divinity {

    private final String name = "False";
    private final Boolean threePlayerSupported = true;

    private int oldLevel;
    private int newLevel;

    /**
     * Denies the action on a certain number of cells (with row = 1 or column = 1)
     *
     * @param moveCells the cells where the player wants to move and where the player
     * @param gameBoard the game board
     * @return if the move is allowed or not
     */
    @Override
    public Boolean othersMove(ActionCoordinates moveCells, Cell[][] gameBoard) {
        return moveCells.getMoveRow() != 1 || moveCells.getMoveColumn() != 1;
    }

    /**
     * Denies the action on a certain number of cells (with row = 3 or column = 3)
     *
     * @param moveCells the cells where the player wants to move and where the player is
     * @return if the building is allowed or not
     */
    @Override
    public Boolean othersBuilding(ActionCoordinates moveCells) {
        return moveCells.getMoveRow() != 3 || moveCells.getMoveColumn() != 3;
    }

    /**
     * @param domeCells the cells where the player wants to add a dome, the cells where the player is and
     * @return true if the divinity doesn't affect the other player's move, false if the divinity blocks the move
     * @author Daniele Mammone
     */
    @Override
    public Boolean othersDome(ActionCoordinates domeCells) {
        return domeCells.getMoveRow() != 3 || domeCells.getMoveColumn() != 3;
    }
}