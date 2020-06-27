package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.divinities.testingDivinities.DivFalse;
import it.polimi.ingsw.PSP48.server.model.divinities.testingDivinities.DivinityFalsePower;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MinotaurTest {

    Model gd = new Model(2, true);
    Player p1 = new Player("Pippo", new GregorianCalendar(1998, Calendar.FEBRUARY, 21), true, Colour.BLUE);
    Player p2 = new Player("Paperino", new GregorianCalendar(2010, Calendar.MARCH, 10), true, Colour.WHITE);

    @Before
    public void testSetUp() {
        p1.setDivinity(new Minotaur());
        p2.setDivinity(new DivinityFalsePower());
        p1.setTempDivinity(null);
        p2.setTempDivinity(null);
        gd.getPlayersInGame().add(p1);
        gd.getPlayersInGame().add(p2);
        gd.setNextPlayer(0);
        gd.getCell(2, 2).setPlayer(p1.getName());

        gd.getCell(1, 1).setPlayer(p2.getName());
        gd.getCell(1, 2).setPlayer(p2.getName());
        gd.getCell(1, 3).setPlayer(p2.getName());
        gd.getCell(2, 3).setPlayer(p2.getName());
        gd.getCell(3, 1).setPlayer(p2.getName());
        gd.getCell(3, 2).setPlayer(p2.getName());
        gd.getCell(3, 3).setPlayer(p2.getName());

        gd.getCell(0, 2).addDome();
        gd.getCell(0, 4).setPlayer("Riku");
        gd.getCell(4, 2).setPlayer("Kairi");
        gd.getCell(4, 1).setActualLevel(2);
        gd.getCell(2, 4).addDome();
        gd.getCell(0, 0).setActualLevel(1);
        gd.getCell(4, 3).setActualLevel(3);
    }

    @Test
    public void validCellsToMoveDifferentCombination() {
        ArrayList<Position> vC = new ArrayList<>();
        vC.add(new Position(2, 1));
        vC.add(new Position(3, 1));
        vC.add(new Position(3, 3));

        ArrayList<Divinity> div = new ArrayList<>();
        div.add(new DivFalse());

        assertEquals(vC, p1.getDivinity().getValidCellForMove(2, 2, gd.getGameBoard(), div));
    }

    @Test(expected = OccupiedCellException.class)
    public void move_pushingCellNotEmpty() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException{
        p1.getDivinity().move(2, 2, 1, 3, gd);
    }

    @Test(expected = OccupiedCellException.class)
    public void move_pushingCellDomed() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException{
        p1.getDivinity().move(2, 2, 1, 2, gd);
    }

    @Test(expected = DivinityPowerException.class)
    public void move_PushOutOfBoard() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException{
        gd.getCell(1, 1).setPlayer(p1.getName());
        gd.getCell(0, 0).setPlayer(p2.getName());
        p1.getDivinity().move(1, 1, 0, 0, gd);
    }

    @Test(expected = DivinityPowerException.class)
    public void move_pushWithColumnOutOfBoard() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException{
        gd.getCell(2, 1).setPlayer(p1.getName());
        gd.getCell(2, 0).setPlayer(p2.getName());
        p1.getDivinity().move(1, 1, 2, 0, gd);
    }

    @Test(expected = DomedCellException.class)
    public void moveToADomedCell() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException{
        gd = new Model(2, true);
        gd.getCell(2, 2).setPlayer(p1.getName());
        gd.getCell(2, 3).addDome();
        p1.getDivinity().move(2, 2, 2, 3, gd);
    }

    @Test(expected = IncorrectLevelException.class)
    public void incorrectLevelException() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException{
        gd = new Model(2, true);
        gd.getCell(2, 2).setPlayer(p1.getName());
        gd.getCell(2, 3).setActualLevel(2);
        p1.getDivinity().move(2, 2, 2, 3, gd);
    }

    @Test(expected = DivinityPowerException.class)
    public void move_movementBlovkedByOtherDivinity() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException{
        gd = new Model(2, true);
        gd.getPlayersInGame().add(p1);
        gd.getPlayersInGame().add(p2);
        gd.setNextPlayer(0);
        gd.getCell(2, 2).setPlayer(p1.getName());
        p1.getDivinity().move(2, 2, 1, 2, gd);
    }

    @Test(expected = NotAdjacentCellException.class)
    public void move_moveOnNotAdjacentCell() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException{
        gd = new Model(2, true);
        gd.getPlayersInGame().add(p1);
        gd.getPlayersInGame().add(p2);
        gd.setNextPlayer(0);
        gd.getCell(2, 2).setPlayer(p1.getName());
        p1.getDivinity().move(2, 2, 0, 0, gd);
    }

    @Test
    public void move_correctPushing() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException{
        p1.getDivinity().move(2, 2, 1, 1, gd);
        assertNull(gd.getCell(2, 2).getPlayer());
        assertEquals(new Cell(1, 1, 0, p1.getName(), false), gd.getCell(1, 1));
        assertEquals(new Cell(0, 0, 1, p2.getName(), false), gd.getCell(0, 0));
    }

    @Test(expected = OccupiedCellException.class)
    public void move_tryingToPushYourOwnWorker() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException{
        gd = new Model(2, true);
        gd.getPlayersInGame().add(p1);
        gd.getPlayersInGame().add(p2);
        gd.setNextPlayer(0);
        gd.getCell(2, 2).setPlayer(p1.getName());
        gd.getCell(3, 3).setPlayer(p1.getName());
        p1.getDivinity().move(2, 2, 3, 3, gd);
    }

    @Test
    public void validCellsWithYourOwnWorker() {
        gd = new Model(2, true);
        gd.getPlayersInGame().add(p1);
        gd.getPlayersInGame().add(p2);
        gd.setNextPlayer(0);
        gd.getCell(0, 0).setPlayer(p1.getName());
        gd.getCell(1, 0).setPlayer(p1.getName());
        gd.getCell(0, 1).setPlayer(p2.getName());

        ArrayList<Position> vC = new ArrayList<>();
        vC.add(new Position(0, 1));
        vC.add(new Position(1, 1));

        assertEquals(vC, p1.getDivinity().getValidCellForMove(0, 0, gd.getGameBoard(), new ArrayList<>()));
    }
}