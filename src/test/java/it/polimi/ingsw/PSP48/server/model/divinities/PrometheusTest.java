package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class PrometheusTest
{
    Model game_database = new Model(2, true);
    Divinity baseDivinity = new Divinity();
    Cell oldCell = new Cell(2, 2);
    Cell newCell = new Cell(1, 2);

    @Before
    public void testSetUp()
    {
        game_database.addPlayer("Pippo", Colour.BLUE, new GregorianCalendar(1998, Calendar.FEBRUARY, 21));
        game_database.addPlayer("Paperino", Colour.WHITE,  new GregorianCalendar(2010, Calendar.MARCH, 10));
        game_database.getPlayer("Pippo").setDivinity(new Prometheus());
        game_database.getPlayer("Paperino").setDivinity(new Divinity());
        game_database.getPlayer("Pippo").setTempDivinity(null);
        game_database.getPlayer("Paperino").setTempDivinity(null);
        game_database.setNextPlayer("Pippo");
        game_database.getCell(4, 4).setPlayer(game_database.getCurrentPlayer().getName());
        game_database.getCell(1,3).setPlayer(game_database.getCurrentPlayer().getName());
        game_database.getCell(2, 3).setActualLevel(2);
        game_database.getCell(1,4).setActualLevel(2);
        game_database.getCell(3, 3).setActualLevel(1);
        game_database.getCell(4, 3).setActualLevel(3);
        game_database.getCell(3, 4).setActualLevel(2);
    }

    @Test
    public void validMoveCellsBeforeBuilding()
    {
        ArrayList<Position> validMoveCells = new ArrayList<>();

        game_database.getCurrentPlayer().getDivinity().turnBegin(game_database);
        validMoveCells.add(new Position(game_database.getCell(3, 3).getRow(), game_database.getCell(3,3).getColumn()));

        assertTrue(validMoveCells.containsAll(game_database.getCurrentPlayer().getDivinity().getValidCellForMove(4, 4, game_database.getGameBoard(), new ArrayList<>())));
    }

    @Test
    public void validMoveCellsAfterBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException
    {
        game_database.getCurrentPlayer().getDivinity().build(4, 4, 3, 4, game_database);
        assertEquals(new ArrayList<Position>(), game_database.getCurrentPlayer().getDivinity().getValidCellForMove(4, 4, game_database.getGameBoard(), new ArrayList<>()));
    }

    @Test
    public void validMoveCellsWithoutBuilding()
    {
        ArrayList<Position> moveCells=new ArrayList<>();
        ArrayList<Divinity> otherDiv= new ArrayList<>();

        moveCells.add(new Position(0,2));
        moveCells.add(new Position(0,3));
        moveCells.add(new Position(0,4));
        moveCells.add(new Position (1,2));
        moveCells.add(new Position(2,2));
        moveCells.add(new Position(2,4));

        otherDiv.add(new Divinity());

        assertEquals(moveCells, game_database.getCurrentPlayer().getDivinity().getValidCellForMove(3,1, game_database.getGameBoard(), otherDiv));
    }

    @Test(expected = DivinityPowerException.class)
    public void tryingToLevelUpAfterBuilding_exceptionThrown() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException, IncorrectLevelException, NoTurnEndException
    {
        game_database.getCurrentPlayer().getDivinity().build(4, 4, 3, 4, game_database);
        game_database.getCurrentPlayer().getDivinity().move(4, 4, 3, 3, game_database);
    }

    @Test
    public void validMoveCellsAfterDoming() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException, MaximumLevelNotReachedException
    {
        game_database.getCurrentPlayer().getDivinity().dome(4, 4, 4, 3, game_database);
        assertEquals(new ArrayList<Position>(), game_database.getCurrentPlayer().getDivinity().getValidCellForMove(4, 4, game_database.getGameBoard(), new ArrayList<>()));
    }

    @Test
    public void correctMovingWithoutBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException, MaximumLevelNotReachedException, IncorrectLevelException, NoTurnEndException
    {
        game_database.getCurrentPlayer().getDivinity().move(4, 4, 3, 3, game_database);
    }

    @Test
    public void correctMovingWithoutLevelUpAfterBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException, IncorrectLevelException, NoTurnEndException
    {
        game_database.getCell(3, 3).setActualLevel(0);
        game_database.getCurrentPlayer().getDivinity().build(4, 4, 3, 4, game_database);
        game_database.getCurrentPlayer().getDivinity().move(4, 4, 3, 3, game_database);
    }

    @Test
    public void correctMovingWithoutLevelUpAfterDoming() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException, IncorrectLevelException, MaximumLevelNotReachedException, NoTurnEndException
    {
        game_database.getCell(3, 3).setActualLevel(0);
        game_database.getCurrentPlayer().getDivinity().dome(4, 4, 4, 3, game_database);
        game_database.getCurrentPlayer().getDivinity().move(4, 4, 3, 3, game_database);
    }
}