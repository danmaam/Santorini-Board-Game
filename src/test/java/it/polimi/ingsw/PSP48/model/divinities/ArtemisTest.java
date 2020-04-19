package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.divinities.Artemis;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ArtemisTest {
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
        player1.setDivinity(new Artemis());
        player2.setDivinity(new Divinity());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.addPlayer(player1);
        game_database.addPlayer(player2);
        game_database.setCurrentPlayer(0);
        game_database.getCell(1, 1).setPlayer(player1.getName());

        game_database.getCell(0, 0).addDome();
        game_database.getCell(1, 0).addDome();
        game_database.getCell(2, 0).addDome();
        game_database.getCell(0, 1).addDome();
        game_database.getCell(2, 1).addDome();
        game_database.getCell(2, 2).addDome();

        game_database.getCell(1, 1).setActualLevel(2);
        game_database.getCell(1, 2).setActualLevel(1);
    }

    @Test(expected = DivinityPowerException.class)
    public void validCellsTest() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        player1.getDivinity().turnBegin(game_database);
        ArrayList<Cell> valid = new ArrayList<>();
        valid.add(game_database.getCell(0, 2));
        valid.add(game_database.getCell(1, 2));
        assertEquals(valid, player1.getDivinity().getValidCellForMove(1, 1, game_database.getGameBoard(), new ArrayList<Divinity>()));
        player1.getDivinity().move(1, 1, 2, 1, game_database);
        valid = new ArrayList<>();
        valid.add(game_database.getCell(0, 2));
        valid.add(game_database.getCell(2, 3));
        valid.add(game_database.getCell(1, 3));
        valid.add(game_database.getCell(0, 3));

        assertTrue(valid.containsAll(player1.getDivinity().getValidCellForMove(2, 1, game_database.getGameBoard(), new ArrayList<>())));
        assertTrue(valid.size() == player1.getDivinity().getValidCellForMove(2, 1, game_database.getGameBoard(), new ArrayList<>()).size());

        player1.getDivinity().move(2, 1, 1, 1, game_database);
    }

}