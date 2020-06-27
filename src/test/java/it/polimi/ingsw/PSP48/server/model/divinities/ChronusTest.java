package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.Colour;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ChronusTest {
    Model game_database = new Model(2, true);
    Player player1 = new Player("Pippo", new GregorianCalendar(1998, Calendar.FEBRUARY, 21), true, Colour.BLUE);
    Player player2 = new Player("Paperino", new GregorianCalendar(2010, Calendar.MARCH, 10), true, Colour.WHITE);

    @Before
    public void testSetUp() {
        player1.setDivinity(new Chronus());
        player2.setDivinity(new Divinity());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.getPlayersInGame().add(player1);
        game_database.getPlayersInGame().add(player2);
        game_database.setNextPlayer(0);
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
        assertFalse(player1.getDivinity().postMoveWinCondition(game_database));
        game_database.getCell(4, 4).setActualLevel(3);
        game_database.getCell(4, 4).addDome();

        assertTrue((player1.getDivinity().postBuildWinCondition(game_database)));

        game_database.getCell(4, 0).setActualLevel(3);
        game_database.getCell(4, 0).addDome();

        assertTrue((player1.getDivinity().postBuildWinCondition(game_database)));
    }

    @Test
    public void testWithGrowingUp() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException {
        game_database.getCell(0, 1).setActualLevel(2);
        game_database.getCell(0, 2).setActualLevel(3);
        player1.getDivinity().move(0, 1, 0, 2, game_database);
        assertTrue(player1.getDivinity().postMoveWinCondition(game_database));
    }
}