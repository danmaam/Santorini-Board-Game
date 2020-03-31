package it.polimi.ingsw.PSP48;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Hephaestus extends Divinity {
    private final String name = "Basic";
    private final Boolean threePlayerSupported = true;
    private int prevBuildRow = -1;
    private int prevBuildColumn = -1;

    /**
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in the game
     * @return a list of cell valid for the building of the worker
     */
    @Override
    public ArrayList<Cell> getValidCellForBuilding(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        ArrayList<Cell> validCells = super.getValidCellForBuilding(WorkerColumn, WorkerRow, gameCells, divinitiesInGame);
        if (prevBuildRow != -1 && prevBuildColumn != -1) validCells = validCells.stream()
                .filter(cell -> cell.getRow() == prevBuildRow && cell.getColumn() == prevBuildColumn)
                .collect(Collectors.toCollection(ArrayList::new));
        return validCells;
    }

    @Override
    public void build(int buildRow, int buildColumn) {
        prevBuildRow = buildRow;
        prevBuildColumn = buildColumn;
    }

    /**
     * @param workerColumn the column where the worker is
     * @param workerRow    the row where the worker is
     * @param gameCells    the actual state of the board
     * @return true if it's possible to add the dome
     */
    @Override
    public ArrayList<Cell> getValidCellsToPutDome(int workerColumn, int workerRow, Cell[][] gameCells) {
        if (prevBuildColumn == -1 && prevBuildRow == -1)
            return super.getValidCellsToPutDome(workerColumn, workerRow, gameCells);
        else return null;
    }
}
