package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChronusTest {
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
        player1.setDivinity(new Chronus());
        player2.setDivinity(new Divinity());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.addPlayer(player1);
        game_database.addPlayer(player2);
        game_database.setCurrentPlayer(0);
        game_database.getCell(1, 1).setPlayer(player1.getName());

        game_database.getCell(0, 0).addDome();
        game_database.getCell(0, 4).addDome();
        game_database.getCell(2, 1).addDome();
        game_database.getCell(3, 2).addDome();

        game_database.getCell(0, 0).setActualLevel(3);
        game_database.getCell(0, 4).setActualLevel(3);
        game_database.getCell(2, 1).setActualLevel(3);
        game_database.getCell(3, 2).setActualLevel(3);

    }

    @Test
    public void testWithFiveOrMoreTowers() {
        assertTrue(!(player1.getDivinity().winCondition(game_database)));
        game_database.getCell(4, 4).setActualLevel(3);
        game_database.getCell(4, 4).addDome();

        assertTrue((player1.getDivinity().winCondition(game_database)));

        game_database.getCell(4, 0).setActualLevel(3);
        game_database.getCell(4, 0).addDome();

        assertTrue((player1.getDivinity().winCondition(game_database)));

    }

    @Test
    public void testWithGrowingUp() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        game_database.getCell(0, 1).setActualLevel(2);
        game_database.getCell(0, 2).setActualLevel(3);
        player1.getDivinity().move(1, 0, 2, 0, game_database);
        assertTrue(player1.getDivinity().winCondition(game_database));
    }


}