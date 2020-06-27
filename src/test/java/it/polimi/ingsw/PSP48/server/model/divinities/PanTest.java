package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class PanTest {
    Model game_database = new Model(2, true);
    Divinity baseDivinity = new Divinity();
    Cell oldCell = new Cell(2, 2);
    Cell newCell = new Cell(1, 2);

    @Before
    public void testSetUp() {
        game_database.addPlayer("Pippo", Colour.BLUE, new GregorianCalendar(1998, Calendar.FEBRUARY, 21));
        game_database.addPlayer("Paperino", Colour.WHITE, new GregorianCalendar(2010, Calendar.MARCH, 10));
        game_database.getPlayer("Pippo").setDivinity(new Pan());
        game_database.getPlayer("Paperino").setDivinity(new Divinity());
        game_database.getPlayer("Pippo").setTempDivinity(null);
        game_database.getPlayer("Paperino").setTempDivinity(null);
        game_database.setNextPlayer("Pippo");

        game_database.getCell(4, 4).setPlayer(game_database.getCurrentPlayer().getName());

        game_database.getCell(3, 3).setActualLevel(0);
        game_database.getCell(4, 3).setActualLevel(3);
        game_database.getCell(4, 4).setActualLevel(2);
        game_database.getCell(3, 4).addDome();
    }

    @Test
    public void normalWin() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException {
        game_database.getCurrentPlayer().getDivinity().move(4, 4, 4, 3, game_database);
        assertTrue(game_database.getCurrentPlayer().getDivinity().winCondition(game_database));
    }

    @Test
    public void winWithFaithJump() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException {
        game_database.getCurrentPlayer().getDivinity().move(4, 4, 3, 3, game_database);
        assertTrue(game_database.getCurrentPlayer().getDivinity().winCondition(game_database));
    }

    @Test
    public void notWin() {
        assertTrue(!game_database.getCurrentPlayer().getDivinity().winCondition(game_database));
    }

}