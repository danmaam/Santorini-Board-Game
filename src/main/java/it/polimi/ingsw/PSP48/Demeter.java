package it.polimi.ingsw.PSP48;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Demeter extends Divinity {
    private final String name = "Basic";
    private final Boolean threePlayerSupported = true;

    private int oldRowBuild = -1;
    private int oldColumnBuild = -1;


    /**
     * Reset the coordinate of first building
     */
    @Override
    public void turnBegin() {
        oldRowBuild = -1;
        oldColumnBuild = -1;
    }

    /**
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in the game
     * @return a list of cell valid for the building of the worker
     */
    @Override
    public ArrayList<Cell> getValidCellForBuilding(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        return super.getValidCellForBuilding(WorkerColumn, WorkerRow, gameCells, divinitiesInGame).stream()
                .filter(cell -> cell.getColumn() != oldColumnBuild && cell.getRow() != oldRowBuild)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * @param workerColumn the column where the worker is
     * @param workerRow    the row where the worker is
     * @param gameCells    the actual state of the board
     * @return true if it's possible to add the dome
     */
    @Override
    public ArrayList<Cell> getValidCellsToPutDome(int workerColumn, int workerRow, Cell[][] gameCells) {
        return super.getValidCellsToPutDome(workerColumn, workerRow, gameCells).stream()
                .filter(cell -> cell.getColumn() != oldColumnBuild && cell.getRow() != oldRowBuild)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void build(int buildRow, int buildColumn) {
        oldRowBuild = buildRow;
        oldColumnBuild = buildColumn;
    }
}
