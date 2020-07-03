package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.ActionCoordinates;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;

/**
 * Divinity used for testing purposes
 */
 class DivinityFalsePower extends Divinity {
    /**
     * Blocks the move on certain cells (with row = 1 or column = 2)
     *
     * @param moveCells the cells where the player wants to move, where the player is and the difference between the two cells
     * @param gameBoard the game board
     * @return true if the move is allowed
     */
    @Override
    public Boolean othersMove(ActionCoordinates moveCells, Cell[][] gameBoard) {
        return moveCells.getMoveRow() != 1 || moveCells.getMoveColumn() != 2;
    }

    /**
     * Blocks the move on certain cells (with row = 1 or column = 2)
     *
     * @param moveCells the cells where the player wants to build, where the player is and the difference between the two cells
     * @return true if the move is allowed
     */
    @Override
    public Boolean othersBuilding(ActionCoordinates moveCells) {
        return moveCells.getMoveRow() != 1 || moveCells.getMoveColumn() != 2;
    }

    /**
     * @param domeCells the cells where the player wants to add a dome, the cells where the player is and
     * @return true if the divinity doesn't affect the other player's move, false if the divinity blocks the move
     * @author Daniele Mammone
     */
    @Override
    public Boolean othersDome(ActionCoordinates domeCells) {
        return domeCells.getMoveRow() != 2 || domeCells.getMoveColumn() != 1;
    }
}
