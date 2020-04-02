package it.polimi.ingsw.PSP48.divinities;

import it.polimi.ingsw.PSP48.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Apollo extends Divinity {
    private final String name = "Apollo";
    private final Boolean threePlayerSupported = true;

    private int lastWorkerMoveID = 0;
    private int oldLevel;
    private int newLevel;

    /**
     * reimplements getValidCellForMove since also occupied Cells are valid
     *
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in game
     * @return a list of cells valid for the move of the worker
     */
    @Override
    public ArrayList<Cell> getValidCellForMove(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        Cell actualWorkerCell = gameCells[WorkerRow][WorkerColumn];
        ArrayList<Cell> validCells = new ArrayList<>();

        //with the for loop, i'm adding to the arrayList the cell adiacent to the worker
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != WorkerRow && j != WorkerColumn && 0 <= WorkerRow + i && WorkerRow + i <= 4 && 0 <= WorkerColumn + j && WorkerColumn + j <= 4) {
                    validCells.add(gameCells[i][j]);
                }
            }
        }

        //we have to remove the current divinity from the others, to check if their power can invalid the move
        divinitiesInGame.remove(gameCells[WorkerRow][WorkerColumn].getWorker().getDivinity());

        validCells = validCells.stream()
                //delets from the valid the cell which are too high or too low to be reached
                .filter(cell -> -3 <= cell.getLevel() - actualWorkerCell.getLevel() && cell.getLevel() - actualWorkerCell.getLevel() <= 1)
                //deletes the domed cells
                .filter(cell -> !cell.isDomed())
                .collect(Collectors.toCollection(ArrayList::new));

        //now we have to remove cells where the move is impossible due to other divinity powers

        for (Cell c : validCells) {
            for (Divinity d : divinitiesInGame) {
                if (!d.othersMove(new MovePosition(WorkerRow, WorkerColumn, c.getRow(), c.getColumn(), 0))) {
                    validCells.remove(c);
                    break;
                }
            }
        }

        //now in valid cells there is the list with compatible moves cells

        return validCells;
    }

    /**
     * Redefined since Apollo allows to move on an occupied Cell, swapping the two workers
     * @param WorkerColumn     the column of the cell where the worker is
     * @param WorkerRow        the row of the cell where the worker is
     * @param moveColumn       the column of the board where the worker wants to move
     * @param moveRow          the row of the board where the worker wants to move
     * @param gameCells        the game board
     * @param divinitiesInGame the divinities in game
     * @throws NotAdiacentCellException if the destination cell is not adiacent to the worker
     * @throws IncorrectLevelException  if the destination cell is too high to be reached
     * @throws OccupiedCellException    if the destination cell has another worker on it
     * @throws DomedCellException       if the destination cell has a dome on it
     * @author Daniele Mammone
     */
    @Override
    public void move(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) throws NotAdiacentCellException, IncorrectLevelException, DomedCellException, DivinityPowerException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(WorkerRow, WorkerColumn, moveRow, moveColumn))) throw new NotAdiacentCellException("Celle non adiacenti");
        //second check: the two levels must be compatible
        int workerLevel = gameCells[WorkerRow][WorkerColumn].getLevel();
        int moveLevel = gameCells[moveRow][moveColumn].getLevel();
        if (!(moveLevel-workerLevel <= 1)) throw new IncorrectLevelException("Stai cerando di salire a un livello troppo alto");
        //third check: the cell must not be domed
        if (gameCells[moveRow][moveColumn].isDomed()) throw new DomedCellException("Stai cercando di salire su una cella con cupola");
        //fourth check: if another different divinity doesn't invalid this move
        divinitiesInGame.remove(gameCells[WorkerRow][WorkerColumn].getWorker().getDivinity());

        for(Divinity d : divinitiesInGame) {
            if (!d.othersMove(new MovePosition(WorkerRow, WorkerColumn, moveRow, moveColumn, moveLevel - moveColumn))) throw new DivinityPowerException("Fail due to other divinity");
        }

        //at this point, the move is valid and we must change the state of the game board

        oldLevel = workerLevel;
        newLevel = moveLevel;
        Worker tempWorker = gameCells[moveRow][moveColumn].getWorker();
        gameCells[moveRow][moveColumn].setWorker(gameCells[WorkerRow][WorkerColumn].getWorker());
        gameCells[WorkerRow][WorkerColumn].setWorker(tempWorker);

        //now, the game board has been modified
    }
}
