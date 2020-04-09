package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PanTest {
    GameData game_database = new GameData();
    Player player1 = new Player("Pippo", new Birthday(21, 02, 1998));
    Player player2 = new Player("Paperino", new Birthday(10, 03, 2010));
    Divinity baseDivinity = new Divinity();
    Cell oldCell = new Cell(2, 2);
    Cell newCell = new Cell(1, 2);

    @Before
    public void testSetUp() {
        player1.setColour(Colour.BLUE);
        player2.setColour(Colour.WHITE);
        player1.setDivinity(new Pan());
        player2.setDivinity(new Divinity());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.addPlayer(player1);
        game_database.addPlayer(player2);
        game_database.setCurrentPlayer(0);
        game_database.getCell(4, 4).setPlayer(player1.getName());

        game_database.getCell(3, 3).setActualLevel(0);
        game_database.getCell(4, 3).setActualLevel(3);
        game_database.getCell(4, 4).setActualLevel(2);
        game_database.getCell(3, 4).addDome();
    }

    @Test
    public void normalWin() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        player1.getDivinity().move(4, 4, 3, 4, game_database);
        assertTrue(player1.getDivinity().winCondition(game_database));
    }

    @Test
    public void winWithFaithJump() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        player1.getDivinity().move(4, 4, 3, 3, game_database);
        assertTrue(player1.getDivinity().winCondition(game_database));
    }

    @Test
    public void notWin() {
        assertTrue(!player1.getDivinity().winCondition(game_database));
    }

}