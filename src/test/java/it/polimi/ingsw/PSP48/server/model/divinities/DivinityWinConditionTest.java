package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.Colour;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertTrue;

public class DivinityWinConditionTest {
    Model game_database = new Model(2, true);
    Player player1 = new Player("Pippo", new GregorianCalendar(1998, Calendar.FEBRUARY, 21), true, Colour.BLUE);
    Player player2 = new Player("Paperino", new GregorianCalendar(2010, Calendar.MARCH, 10), true, Colour.WHITE);
    Divinity baseDivinity = new Divinity();
    
    @Before
    public void testSetUp() {
        player1.setDivinity(baseDivinity);
        player2.setDivinity(baseDivinity);
        game_database.getPlayersInGame().add(player1);
        game_database.getPlayersInGame().add(player2);
        game_database.getCell(0, 1).setPlayer(player1.getName());
        game_database.getCell(3, 0).setPlayer(player1.getName());
        game_database.getCell(4, 4).setPlayer(player2.getName());
        game_database.getCell(0, 3).setPlayer(player2.getName());
        game_database.getCell(1, 3).setActualLevel(1);
        game_database.getCell(2, 3).setActualLevel(2);
        game_database.getCell(3, 4).setActualLevel(3);
        game_database.setNextPlayer(1);
    }

    @org.junit.Test
    public void correctWinSituation() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException {
        player2.getDivinity().move(0, 3, 1, 3, game_database);
        player2.getDivinity().move(1, 3, 2, 3, game_database);
        player2.getDivinity().move(2, 3, 3, 4, game_database);
        assertTrue(player2.getDivinity().winCondition(game_database));
    }

    @Test
    public void notWinSituation() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException {
        player2.getDivinity().move(0, 3, 1, 3, game_database);
        player2.getDivinity().move(1, 3, 2, 3, game_database);
        assertTrue(!player2.getDivinity().winCondition(game_database));
    }

    @Test
    public void notWinSituationWithPushing() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException {
        player2.getDivinity().move(0, 3, 1, 3, game_database);
        player2.getDivinity().move(1, 3, 2, 3, game_database);
        game_database.getCell(3, 4).setPlayer(player2.getName());
        game_database.getCell(2, 3).setPlayer(null);
        assertTrue(!player2.getDivinity().winCondition(game_database));
    }
}