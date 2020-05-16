package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Apollo extends Divinity {

    public static Boolean supportedDivinity(int pNum) {
        switch (pNum) {
            case 2:
                return true;
            case 3:
                return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Apollo";
    }


    /**
     * re-implements getValidCellForMove since also occupied Cells are valid
     *
     * @param WorkerColumn     the column where the worker is
     * @param WorkerRow        the row where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in game
     * @return a list of cells valid for the move of the worker
     */
    @Override
    public ArrayList<Position> getValidCellForMove(int WorkerColumn, int WorkerRow, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        Cell actualWorkerCell = gameCells[WorkerRow][WorkerColumn];
        ArrayList<Cell> validCells = new ArrayList<>();

        //with the for loop, i'm adding to the arrayList the cell adjacent to the worker
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && 0 <= WorkerRow + i && WorkerRow + i <= 4 && 0 <= WorkerColumn + j && WorkerColumn + j <= 4) {
                    validCells.add(gameCells[WorkerRow + i][WorkerColumn + j]);
                }
            }
        }


        validCells = validCells.stream()
                //deletes from the valid the cell which are too high or too low to be reached
                .filter(cell -> cell.getLevel() - actualWorkerCell.getLevel() <= 1)
                //deletes the domed cells
                .filter(cell -> cell.getPlayer() == null || (cell.getPlayer() != null && !cell.getPlayer().equals(gameCells[WorkerRow][WorkerColumn].getPlayer())))
                .filter(cell -> !cell.isDomed())
                .collect(Collectors.toCollection(ArrayList::new));

        //now we have to remove cells where the move is impossible due to other divinity powers

        ArrayList<Cell> nV = new ArrayList<>();

        for (Cell c : validCells) {
            for (Divinity d : divinitiesInGame) {
                if (!d.getName().equals(this.getName()) && !d.othersMove(new MovePosition(WorkerRow, WorkerColumn, c.getRow(), c.getColumn(), gameCells[c.getRow()][c.getColumn()].getLevel() - gameCells[WorkerRow][WorkerColumn].getLevel()))) {
                    nV.add(c);
                    break;
                }
            }
        }
        for (Cell c : nV) validCells.remove(c);

        //now in valid cells there is the list with compatible moves cells, but i must check that moving in these cells doesn't
        //block the worker from building, possible since apollo can swap workers, while without apollo these situations
        //aren't possible

        nV = new ArrayList<>();

        //i must clone the game-board to simulate the move, and than check the valid cells for building
        Cell[][] clonedBoard = new Cell[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                clonedBoard[i][j] = (Cell) gameCells[i][j].clone();
            }
        }


        for (Cell c : validCells) {
            if (!checkIfCanBuildAfterTheMove(WorkerRow, WorkerColumn, c.getRow(), c.getColumn(), clonedBoard, divinitiesInGame))
                nV.add(c);
        }

        //we need to remove from the not valid cells the ones where the player cannot build from, but that can still make the player win if he moves on them
        for (Cell c : nV) {
            if (checkIfWinsAfterMove(WorkerRow, WorkerColumn, c.getRow(), c.getColumn(), clonedBoard)) nV.remove(c);
        }

        //now we can finally remove from the valid cells the ones where the player cannot build from
        for (Cell cell : nV) {
            validCells.remove(cell);
        }

        ArrayList<Position> validPositions = new ArrayList<>();
        validCells.forEach((Cell c) -> validPositions.add(new Position(c.getRow(), c.getColumn())));
        return validPositions;
    }

    /**
     * Redefined since Apollo allows to move on an occupied Cell, swapping the two workers
     *
     * @param WorkerColumn the column of the cell where the worker is
     * @param WorkerRow    the row of the cell where the worker is
     * @param moveColumn   the column of the board where the worker wants to move
     * @param moveRow      the row of the board where the worker wants to move
     * @param gd           the Game status
     * @return the next action of the controller
     * @throws NotAdjacentCellException if the destination cell is not adiacent to the worker
     * @throws IncorrectLevelException  if the destination cell is too high to be reached
     * @throws OccupiedCellException    if the destination cell has another worker on it
     * @throws DomedCellException       if the destination cell has a dome on it
     * @author Daniele Mammone
     */
    @Override
    public Consumer<GameController> move(int WorkerColumn, int WorkerRow, int moveColumn, int moveRow, Model gd) throws NotAdjacentCellException, IncorrectLevelException, DomedCellException, DivinityPowerException, OccupiedCellException, NoTurnEndException {
        //first check: the two cells must be adiacent
        if (!(adiacentCellVerifier(WorkerRow, WorkerColumn, moveRow, moveColumn)))
            throw new NotAdjacentCellException("Celle non adiacenti");
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


        //at this point, the move is valid, but we must check that the player can continue the turn
        ArrayList<Divinity> otherDiv = new ArrayList<>();
        for (Player p : gd.getPlayersInGame()) {
            if (!p.getName().equals(gd.getCurrentPlayer().getName())) otherDiv.add(p.getDivinity());
        }
        if (!checkIfCanBuildAfterTheMove(WorkerRow, WorkerColumn, moveRow, moveColumn, gd.getClonedGameBoard(), otherDiv))
            throw new NoTurnEndException("WIth this move, you can't end the turn");

        String tempWorker = gd.getCell(moveRow, moveColumn).getPlayer();
        gd.getCell(moveRow, moveColumn).setPlayer(gd.getCell(WorkerRow, WorkerColumn).getPlayer());
        gd.getCell(WorkerRow, WorkerColumn).setPlayer(tempWorker);
        gd.getCurrentPlayer().setOldLevel(workerLevel);
        gd.getCurrentPlayer().setNewLevel(moveLevel);
        gd.getCurrentPlayer().setLastWorkerUsed(moveRow, moveColumn);

        ArrayList<Cell> changedCell = new ArrayList<>();
        changedCell.add((Cell) gd.getCell(WorkerRow, WorkerColumn).clone());
        changedCell.add((Cell) gd.getCell(moveRow, moveColumn).clone());
        gd.notifyObservers(x -> x.changedBoard(changedCell));

        //now, the game board has been modified
        return GameController::requestBuildDome;
    }

    private boolean checkIfCanBuildAfterTheMove(int wR, int wC, int mR, int mC, Cell[][] gameBoard, ArrayList<Divinity> otherDiv) {
        //here i must simulate the move and then calculate the valid cells for building and doming
        boolean canBuild;
        String tempPlayer = gameBoard[mR][mC].getPlayer();
        String currentPlayer = gameBoard[wR][wC].getPlayer();
        gameBoard[mR][mC].setPlayer(currentPlayer);
        gameBoard[wR][wC].setPlayer(tempPlayer);
        canBuild = !getValidCellForBuilding(mC, mR, otherDiv, gameBoard).isEmpty() || !getValidCellsToPutDome(mC, mR, gameBoard, otherDiv).isEmpty();
        gameBoard[mR][mC].setPlayer(tempPlayer);
        gameBoard[wR][wC].setPlayer(currentPlayer);
        return canBuild;
    }

    private boolean checkIfWinsAfterMove(int wR, int wC, int mR, int mC, Cell[][] gameBoard)
    {
        boolean wins;

        if (gameBoard[mR][mC].getLevel() == 3 && gameBoard[mR][mC].getLevel() > gameBoard[wR][wC].getLevel())
            wins = true;
        else wins = false;

        return wins;
    }
}
