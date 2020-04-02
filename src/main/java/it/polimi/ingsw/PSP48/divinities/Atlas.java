package it.polimi.ingsw.PSP48.divinities;

import it.polimi.ingsw.PSP48.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Atlas extends Divinity {
    private final String name = "Atlas";
    private final Boolean threePlayerSupported = true;

    /**
     * Redefined method since Atlas can add a dome on each level
     * @param workerColumn     the column where the worker is
     * @param workerRow        the row where the worker is
     * @param gameCells        the actual state of the board
     * @param divinitiesInGame the divinities that are in game
     * @return true if it's possible to add the dome
     * @author Daniele Mammone
     */
    @Override
    public ArrayList<Cell> getValidCellsToPutDome(int workerColumn, int workerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
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

        for (Cell c : validCells) {
            for (Divinity d : divinitiesInGame) {
                if (!d.othersDome(new DomePosition(workerRow, workerColumn, c.getRow(), c.getColumn(), c.getLevel()))) {
                    validCells.remove(c);
                    break;
                }
            }
        }

        return validCells;
    }

    /**
     * Overriden since Atlas can add a dome on each level
     * @param workerRow        the row where the worker is
     * @param workerColumn     the column where the worker is
     * @param domeRow          the row where the player wants to add the dome
     * @param domeColumn       the column where the player wants to add the dome
     * @param gameCells        the actual state of the game board
     * @param divinitiesInGame the divinities in game
     * @throws NotAdiacentCellException        if the cell where the player wants to add the dome is not adiacent to the worker's one
     * @throws OccupiedCellException           if the destination cell is occupied by another worker
     * @throws DomedCellException              is the cell is already domed
     * @throws MaximumLevelNotReachedException if the cell doesn't contain a level 3 building
     * @throws DivinityPowerException          if another divinity blocks the adding of the dome
     * @author Daniele Mammone
     */
    @Override
    public void dome(int workerRow, int workerColumn, int domeRow, int domeColumn, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) throws NotAdiacentCellException, OccupiedCellException, DomedCellException, DivinityPowerException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(workerRow, workerColumn, domeRow, domeColumn))) throw new NotAdiacentCellException("Celle non adiacenti");
        //second check: the cell must be empty of workers
        if (!(gameCells[domeRow][domeColumn].getWorker() == null)) throw new OccupiedCellException("Cella occupata");
        //third check: the cell must not be already domed
        if (gameCells[domeRow][domeColumn].isDomed()) throw new DomedCellException("Stai cercando di costruire su una cella con cupola");
        //fourth check: if another different divinity doesn't invalid this move
        divinitiesInGame.remove(gameCells[workerRow][workerColumn].getWorker().getDivinity());

        for(Divinity d : divinitiesInGame) {
            if (!d.othersDome(new DomePosition(workerRow, workerColumn, domeRow, domeColumn, gameCells[domeRow][domeColumn].getLevel()))) throw new DivinityPowerException("Fail due to other divinity");
        }

        //at this point, the move is valid and we must change the state of the game board

        gameCells[domeRow][domeColumn].addDome();
    }
}
