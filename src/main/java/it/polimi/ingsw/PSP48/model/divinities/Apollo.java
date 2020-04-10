package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.Cell;
import it.polimi.ingsw.PSP48.model.GameData;
import it.polimi.ingsw.PSP48.model.MovePosition;
import it.polimi.ingsw.PSP48.model.Player;
import it.polimi.ingsw.PSP48.model.exceptions.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Apollo extends Divinity {
    private final String name = "Apollo";
    private final Boolean threePlayerSupported = true;

    @Override
    public String getName() {
        return name;
    }

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
                if (!(i == 0 && j == 0) && 0 <= WorkerRow + i && WorkerRow + i <= 4 && 0 <= WorkerColumn + j && WorkerColumn + j <= 4) {
                    validCells.add(gameCells[WorkerRow + i][WorkerColumn + j]);
                }
            }
        }


        validCells = validCells.stream()
                //delets from the valid the cell which are too high or too low to be reached
                .filter(cell -> cell.getLevel() - actualWorkerCell.getLevel() <= 1)
                //deletes the domed cells
                .filter(cell -> cell.getPlayer() == null || (cell.getPlayer() != null && cell.getPlayer() != gameCells[WorkerRow][WorkerColumn].getPlayer()))
                .filter(cell -> !cell.isDomed())
                .collect(Collectors.toCollection(ArrayList::new));

        //now we have to remove cells where the move is impossible due to other divinity powers

        ArrayList<Cell> nV = new ArrayList<>();

        for (Cell c : validCells) {
            for (Divinity d : divinitiesInGame) {
                if (!d.othersMove(new MovePosition(WorkerRow, WorkerColumn, c.getRow(), c.getColumn(), gameCells[c.getRow()][c.getColumn()].getLevel() - gameCells[WorkerRow][WorkerColumn].getLevel()))) {
                    nV.add(c);
                    break;
                }
            }
        }
        for (Cell c : nV) validCells.remove(c);

        //now in valid cells there is the list with compatible moves cells

        return validCells;
    }

    /**
     * Redefined since Apollo allows to move on an occupied Cell, swapping the two workers
     *
     * @param WorkerColumn the column of the cell where the worker is
     * @param WorkerRow    the row of the cell where the worker is
     * @param moveColumn   the column of the board where the worker wants to move
     * @param moveRow      the row of the board where the worker wants to move
     * @param gd           the Game status
     * @throws NotAdiacentCellException if the destination cell is not adiacent to the worker
     * @throws IncorrectLevelException  if the destination cell is too high to be reached
     * @throws OccupiedCellException    if the destination cell has another worker on it
     * @throws DomedCellException       if the destination cell has a dome on it
     * @author Daniele Mammone
     */
    @Override
    public void move(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, GameData gd) throws NotAdiacentCellException, IncorrectLevelException, DomedCellException, DivinityPowerException, OccupiedCellException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(WorkerRow, WorkerColumn, moveRow, moveColumn)))
            throw new NotAdiacentCellException("Celle non adiacenti");
        //second check: the two levels must be compatible
        int workerLevel = gd.getCell(WorkerRow, WorkerColumn).getLevel();
        int moveLevel = gd.getCell(moveRow, moveColumn).getLevel();
        if (!(moveLevel - workerLevel <= 1))
            throw new IncorrectLevelException("Stai cerando di salire a un livello troppo alto");
        //fourth check: the cell must not be domed
        if (gd.getCell(moveRow, moveColumn).isDomed())
            throw new DomedCellException("Stai cercando di salire su una cella con cupola");
        //fifth check: if another different divinity doesn't invalid this move
        //Apollo can exchange position with other players, but not with its workers
        if (gd.getCell(moveRow, moveColumn).getPlayer() != null && gd.getCell(moveRow, moveColumn).getPlayer().equals(gd.getCurrentPlayer().getName()))
            throw new OccupiedCellException("trying to switch with another your worker");

        for (Player p : gd.getPlayersInGame()) {
            if (p != gd.getCurrentPlayer() && !p.getDivinity().othersMove(new MovePosition(WorkerRow, WorkerColumn, moveRow, moveColumn, moveLevel - moveColumn)))
                throw new DivinityPowerException("Fail due to other divinity");
        }
        //at this point, the move is valid and we must change the state of the game board

        String tempWorker = gd.getCell(moveRow, moveColumn).getPlayer();
        gd.getCell(moveRow, moveColumn).setPlayer(gd.getCell(WorkerRow, WorkerColumn).getPlayer());
        gd.getCell(WorkerRow, WorkerColumn).setPlayer(tempWorker);
        gd.getCurrentPlayer().setOldLevel(workerLevel);
        gd.getCurrentPlayer().setNewLevel(moveLevel);

        //now, the game board has been modified
    }
}
