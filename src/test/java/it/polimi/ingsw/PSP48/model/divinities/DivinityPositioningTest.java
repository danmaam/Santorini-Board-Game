package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.Birthday;
import it.polimi.ingsw.PSP48.model.Cell;
import it.polimi.ingsw.PSP48.model.GameData;
import it.polimi.ingsw.PSP48.model.Player;
import it.polimi.ingsw.PSP48.model.exceptions.DivinityPowerException;
import it.polimi.ingsw.PSP48.model.exceptions.NotEmptyCellException;
import org.junit.Before;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DivinityPositioningTest {
    GameData game_database = new GameData();
    Player player1 = new Player("Pippo", new Birthday(21, 02, 1998));
    Player player2 = new Player("Paperino", new Birthday(10, 03, 2010));
    Player player3 = new Player("Sora", new Birthday(5, 01, 1999));
    Divinity baseDivinity = new Divinity();
    Cell oldCell = new Cell(2, 2);
    Cell newCell = new Cell(1, 2);

    @Before
    public void testSetUp() {
        game_database.getCell(0, 0).setPlayer(player1.getName());
        game_database.getCell(0, 3).setPlayer(player1.getName());
        game_database.getCell(4, 2).setPlayer(player2.getName());
        game_database.getCell(4, 4).setPlayer(player2.getName());
        game_database.getCell(3, 1).setPlayer(player3.getName());
        player1.setDivinity(baseDivinity);
        player2.setDivinity(baseDivinity);
        player3.setDivinity(baseDivinity);
    }

    @org.junit.Test
    public void correctValidCells() {
        ArrayList<Cell> validCell = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (game_database.getCell(i, j).getPlayer() == null) validCell.add(game_database.getCell(i, j));
            }
        }
        assertEquals(validCell, player3.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()));
    }

    @org.junit.Test
    public void correctPositioningCell() throws NotEmptyCellException, DivinityPowerException {
        player3.getDivinity().gameSetUp(0, 2, game_database.getGameBoard(), player3.getName());
        Cell newCell = new Cell(0, 2);
        newCell.setPlayer(player3.getName());
        assertEquals(newCell, game_database.getCell(0, 2));
    }

    @org.junit.Test(expected = NotEmptyCellException.class)
    public void positioningCell_occupiedCell_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player3.getDivinity().gameSetUp(0, 0, game_database.getGameBoard(), player3.getName());
    }
}