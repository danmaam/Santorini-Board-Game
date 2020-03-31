package it.polimi.ingsw.PSP48;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Atlas extends Divinity {
    private final String name = "Atlas";
    private final Boolean threePlayerSupported = true;

    /**
     * reimplements the superclass method since Atlas can build dome on level 0
     *
     * @param workerColumn the column where the worker is
     * @param workerRow    the row where the worker is
     * @param gameCells    the actual state of the board
     * @return true if it's possible to add the dome
     */
    @Override
    public ArrayList<Cell> getValidCellsToPutDome(int workerColumn, int workerRow, Cell[][] gameCells) {
        ArrayList<Cell> newCells = new ArrayList<>();
        //with the for loop, i'm adding to the arrayList the cell adiacent to the worker
        ArrayList<Cell> validCells = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != workerRow && j != workerColumn && 0 <= workerRow + i && workerRow + i <= 4 && 0 <= workerColumn + j && workerColumn + j <= 4) {
                    validCells.add(gameCells[i][j]);
                }
            }
        }

        validCells = validCells.stream()
                .filter(cell -> cell.getWorker() == null)
                .filter(cell -> !cell.isDomed())
                .collect(Collectors.toCollection(ArrayList::new));

        return validCells;
    }
}
