package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.ControllerState.GameControllerState;
import it.polimi.ingsw.PSP48.server.controller.ControllerState.RequestBuildDome;
import it.polimi.ingsw.PSP48.server.model.ActionCoordinates;
import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Implements Apollo Divinity
 * @author Daniele Mammone
 */
public class Apollo extends Divinity {

    /**
     * Checks if Apollo is allowed for a certain number of players
     *
     * @param pNum the number of players
     * @return if the divinity is allowed for the specified number of players
     * @author Daniele Mammone
     */
    public static Boolean supportedDivinity(int pNum) {
        switch (pNum) {
            case 2:
                return true;
            case 3:
                return true;
        }
        return false;
    }

    /**
     * Getter of name
     *
     * @return the divinity's name
     */
    @Override
    public String getName() {
        return "Apollo";
    }


    /**
     * re-implements getValidCellForMove since also occupied Cells are valid
     *
     * @author Daniele Mammone, Rebecca Marelli
     * @param workerRow        the row where the worker is
     * @param workerColumn     the column where the worker is
     * @param gameCells        the actual board state
     * @param divinitiesInGame the divinities in game
     * @return a list of cells valid for the move of the worker
     */
    @Override
    public ArrayList<Position> getValidCellForMove(int workerRow, int workerColumn, Cell[][] gameCells, ArrayList<Divinity> divinitiesInGame) {
        Cell actualWorkerCell = gameCells[workerRow][workerColumn];
        ArrayList<Cell> validCells = new ArrayList<>();

        //with the for loop, i'm adding to the arrayList the cell adjacent to the worker
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && 0 <= workerRow + i && workerRow + i <= 4 && 0 <= workerColumn + j && workerColumn + j <= 4) {
                    validCells.add(gameCells[workerRow + i][workerColumn + j]);
                }
            }
        }


        validCells = validCells.stream()
                //deletes from the valid the cell which are too high or too low to be reached
                .filter(cell -> cell.getLevel() - actualWorkerCell.getLevel() <= 1)
                //deletes the cells occupied by the same player
                .filter(cell -> cell.getPlayer() == null || !cell.getPlayer().equals(gameCells[workerRow][workerColumn].getPlayer()))
                //deletes the domed cells
                .filter(cell -> !cell.isDomed())
                .collect(Collectors.toCollection(ArrayList::new));

        //now we have to remove cells where the move is impossible due to other divinity powers

        for (Divinity d : divinitiesInGame) {
            validCells.removeIf(c -> !d.getName().equals(this.getName()) && !d.othersMove(new ActionCoordinates(workerRow, workerColumn, c.getRow(), c.getColumn()), gameCells));

        }

        //now in valid cells there is the list with compatible moves cells, but i must check that moving in these cells doesn't
        //block the worker from ending the turn, possible since apollo can swap workers, while without apollo these situations
        //aren't possible


        validCells.removeIf(c -> !checkIfCanBuildAfterTheMove(workerRow, workerColumn, c.getRow(), c.getColumn(), gameCells, divinitiesInGame) && !checkIfWinsAfterMove(workerRow, workerColumn, c.getRow(), c.getColumn(), gameCells));

        ArrayList<Position> validPositions = new ArrayList<>();
        validCells.forEach((Cell c) -> validPositions.add(new Position(c.getRow(), c.getColumn())));
        return validPositions;
    }

    /**
     * Process a move according to Apollo's power, since Apollo allows to move on an occupied Cell, swapping the two workers
     *
     * @param workerRow    the row of the cell where the worker is
     * @param workerColumn the column of the cell where the worker is
     * @param moveRow      the row of the board where the worker wants to move
     * @param moveColumn   the column of the board where the worker wants to move
     * @param gd           the Game status
     * @return the next action of the controller
     * @throws NotAdjacentCellException if the destination cell is not adiacent to the worker
     * @throws IncorrectLevelException  if the destination cell is too high to be reached
     * @throws OccupiedCellException    if the destination cell has another worker on it
     * @throws DomedCellException       if the destination cell has a dome on it
     * @author Daniele Mammone, Rebecca Marelli
     */
    @Override
    public GameControllerState move(int workerRow, int workerColumn, int moveRow, int moveColumn, Model gd) throws NotAdjacentCellException, IncorrectLevelException, DomedCellException, DivinityPowerException, OccupiedCellException, NoTurnEndException {
        //first check: the two cells must be adjacent
        if (!(adjacentCellVerifier(workerRow, workerColumn, moveRow, moveColumn)))
            throw new NotAdjacentCellException("Celle non adiacenti");
        //second check: the two levels must be compatible
        int workerLevel = gd.getCell(workerRow, workerColumn).getLevel();
        int moveLevel = gd.getCell(moveRow, moveColumn).getLevel();
        if (!(moveLevel - workerLevel <= 1))
            throw new IncorrectLevelException("Stai cerando di salire a un livello troppo alto");
        //third check: the cell must not be domed
        if (gd.getCell(moveRow, moveColumn).isDomed())
            throw new DomedCellException("Stai cercando di salire su una cella con cupola");
        //fourth check: Apollo can exchange position with other players, but not with its workers
        if (gd.getCell(moveRow, moveColumn).getPlayer() != null && gd.getCell(moveRow, moveColumn).getPlayer().equals(gd.getCurrentPlayer().getName()))
            throw new OccupiedCellException("trying to switch with another your worker");
        //fifth check: if another different divinity doesn't invalid this move
        for (Player p : gd.getPlayersInGame()) {
            if (p != gd.getCurrentPlayer() && !p.getDivinity().othersMove(new ActionCoordinates(workerRow, workerColumn, moveRow, moveColumn), gd.getGameBoard()))
                throw new DivinityPowerException("Fail due to other divinity");
        }


        //at this point, the move is valid, but we must check that the player can continue the turn
        ArrayList<Divinity> otherDiv = new ArrayList<>();
        for (Player p : gd.getPlayersInGame()) {
            if (!p.getName().equals(gd.getCurrentPlayer().getName())) otherDiv.add(p.getDivinity());
        }
        if (!checkIfCanBuildAfterTheMove(workerRow, workerColumn, moveRow, moveColumn, gd.getGameBoard(), otherDiv))
            throw new NoTurnEndException("WIth this move, you can't end the turn");

        String tempWorker = gd.getCell(moveRow, moveColumn).getPlayer();
        gd.getCell(moveRow, moveColumn).setPlayer(gd.getCell(workerRow, workerColumn).getPlayer());
        gd.getCell(workerRow, workerColumn).setPlayer(tempWorker);
        gd.getCurrentPlayer().setOldLevel(workerLevel);
        gd.getCurrentPlayer().setNewLevel(moveLevel);
        gd.getCurrentPlayer().setLastWorkerUsed(moveRow, moveColumn);

        ArrayList<Cell> changedCell = new ArrayList<>();
        changedCell.add((Cell) gd.getCell(workerRow, workerColumn).clone());
        changedCell.add((Cell) gd.getCell(moveRow, moveColumn).clone());
        gd.notifyObservers(x -> x.changedBoard(changedCell));

        //now, the game board has been modified
        return new RequestBuildDome();
    }

    /**
     * Simulates a move, and checks if the player can complete the turn after.     *
     *
     * @param wR        the row where the worker is
     * @param wC        the column where the worker is
     * @param mR        the row where the worker wants to move
     * @param mC        the column where the worker wants to move
     * @param gameBoard the game board
     * @param otherDiv  the other divinities in game
     * @return true if the move doesn't block the player from completing the turn, false otherwise
     * @author Daniele Mammone
     */
    private boolean checkIfCanBuildAfterTheMove(int wR, int wC, int mR, int mC, Cell[][] gameBoard, ArrayList<Divinity> otherDiv) {
        //here i must simulate the move and then calculate the valid cells for building and doming
        boolean canBuild;
        //save the previous board status
        String tempPlayer = gameBoard[mR][mC].getPlayer();
        String currentPlayer = gameBoard[wR][wC].getPlayer();
        //simulates the move
        gameBoard[mR][mC].setPlayer(currentPlayer);
        gameBoard[wR][wC].setPlayer(tempPlayer);
        //checks if the move allows the player to complete the turn
        canBuild = !getValidCellForBuilding(mR, mC, otherDiv, gameBoard).isEmpty() || !getValidCellsToPutDome(mR, mC, gameBoard, otherDiv).isEmpty();
        //restores the game board original situation
        gameBoard[mR][mC].setPlayer(tempPlayer);
        gameBoard[wR][wC].setPlayer(currentPlayer);
        return canBuild;
    }

    /**
     * Checks if after a move the player wins
     *
     * @param wR        the row where the worker is
     * @param wC        the column where the worker is
     * @param mR        the row where the worker wants to move
     * @param mC        the column where the worker wants to move
     * @param gameBoard the game bord
     * @return true is the player wins after this move, false otherwise
     * @author Daniele Mammone
     */
    private boolean checkIfWinsAfterMove(int wR, int wC, int mR, int mC, Cell[][] gameBoard) {
        return gameBoard[mR][mC].getLevel() == 3 && gameBoard[mR][mC].getLevel() > gameBoard[wR][wC].getLevel();
    }

    /**
     * Getter of divinity's description
     *
     * @return the description of the divinity power
     * @author Annalaura Massa
     */
    @Override
    public String getDescription() {
        return "Your Worker may move into an opponent Worker’s space by forcing their Worker to the space yours just vacated.";
    }
}
