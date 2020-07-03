package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.*;

public class ApolloTest {
    Model game_database = new Model(2, true);
    Divinity baseDivinity = new Divinity();
    Cell oldCell = new Cell(2, 2);
    Cell newCell = new Cell(1, 2);

    @Before
    public void testSetUp() {
        game_database.addPlayer("Pippo", Colour.BLUE, new GregorianCalendar(1998, Calendar.FEBRUARY, 21));
        game_database.addPlayer("Paperino", Colour.WHITE, new GregorianCalendar(2010, Calendar.MARCH, 10));
        game_database.getPlayer("Pippo").setDivinity(new Apollo());
        game_database.getPlayer("Paperino").setDivinity(new DivinityFalsePower());
        game_database.getPlayer("Pippo").setTempDivinity(null);
        game_database.getPlayer("Paperino").setTempDivinity(null);
        game_database.setNextPlayer("Pippo");
        game_database.getCell(2, 2).setPlayer(game_database.getCurrentPlayer().getName());
        game_database.getCell(1, 3).setPlayer(game_database.getCurrentPlayer().getName());
        game_database.getCell(1, 1).setPlayer("Paperino");
        try {
            game_database.getCell(1, 1).setActualLevel(1);

            game_database.getCell(2, 1).setActualLevel(3);

            game_database.getCell(2, 3).setActualLevel(3);

            game_database.getCell(3, 3).addLevel();
            game_database.getCell(3, 3).addDome();


            game_database.getCell(3, 1).setActualLevel(2);

            game_database.getCell(3, 2).setActualLevel(2);
            game_database.getCell(3, 2).addDome();

            ;
        } catch (MaximumLevelReachedException e) {
            e.printStackTrace();
        }
        game_database.getCell(0, 0).setPlayer("Paperino");
        try {
            newCell.addLevel();
        } catch (MaximumLevelReachedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void validCellsForMovingWithSwapping() {
        ArrayList<Divinity> div = new ArrayList<>();
        div.add(new DivinityFalsePower());
        ArrayList<Position> validCells = new ArrayList<>();
        validCells.add(new Position(game_database.getCell(1, 1).getRow(), game_database.getCell(1, 1).getColumn()));
        assertEquals(validCells, game_database.getCurrentPlayer().getDivinity().getValidCellForMove(2, 2, game_database.getGameBoard(), div));
    }

    @Test
    public void moveTest() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException {
        ArrayList<Divinity> div = new ArrayList<>();
        div.add(new DivinityFalsePower());
        game_database.getCurrentPlayer().getDivinity().move(2, 2, 1, 1, game_database);
        assertEquals(new Cell(1, 1, 1, game_database.getCurrentPlayer().getName(), false), game_database.getCell(1, 1));
        assertEquals(new Cell(2, 2, 0, game_database.getPlayer("Paperino").getName(), false), game_database.getCell(2, 2));
    }

    @org.junit.Test(expected = OccupiedCellException.class)
    public void move_occupiedCell_exception() throws OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, DomedCellException, NoTurnEndException {
        oldCell.setPlayer(null);
        newCell.setPlayer(game_database.getCurrentPlayer().getName());
        game_database.getCurrentPlayer().getDivinity().move(2, 2, 1, 3, game_database);
    }

    @org.junit.Test(expected = DomedCellException.class)
    public void move_domedCell_exception() throws OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, DomedCellException, NoTurnEndException {
        oldCell.setPlayer(null);
        newCell.setPlayer(game_database.getCurrentPlayer().getName());
        game_database.getCurrentPlayer().getDivinity().move(2, 2, 3, 3, game_database);
    }

    @org.junit.Test(expected = NotAdjacentCellException.class)
    public void move_notAdjacentCell_exception() throws OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, DomedCellException, NoTurnEndException {
        oldCell.setPlayer(null);
        newCell.setPlayer(game_database.getCurrentPlayer().getName());
        game_database.getCurrentPlayer().getDivinity().move(2, 2, 4, 0, game_database);
    }

    @org.junit.Test(expected = DivinityPowerException.class)
    public void move_otherDivinityPower_exception() throws OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, DomedCellException, NoTurnEndException {
        game_database.getCurrentPlayer().getDivinity().move(2, 2, 1, 2, game_database);
    }

    @org.junit.Test(expected = IncorrectLevelException.class)
    public void move_incorrectLevel_exception() throws OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, DomedCellException, NoTurnEndException {
        oldCell.setPlayer(null);
        newCell.setPlayer(game_database.getCurrentPlayer().getName());
        game_database.getCurrentPlayer().getDivinity().move(2, 2, 3, 1, game_database);
    }

    @Test
    public void validMoveCells_cantBuildAfterMove_doesntContainsCells() {
        ArrayList<Position> valid;
        ArrayList<Divinity> div = new ArrayList<>();
        div.add(new DivinityFalsePower());
        game_database.getCell(0, 1).addDome();
        game_database.getCell(0, 2).addDome();
        game_database.getCell(1, 0).addDome();
        game_database.getCell(2, 0).addDome();
        game_database.getCell(0, 0).setActualLevel(3);
        game_database.getCell(1, 3).setPlayer(null);
        game_database.getCell(1, 3).setActualLevel(3);
        valid = game_database.getCurrentPlayer().getDivinity().getValidCellForMove(2, 2, game_database.getGameBoard(), div);
        assertTrue(valid.isEmpty());
    }

    @Test(expected = NoTurnEndException.class)
    public void move_tryToMoveOnACellThatCantEndHisTurn_throwsNoTurnEndExcpetion() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException {
        ArrayList<Position> valid;
        ArrayList<Divinity> div = new ArrayList<>();
        div.add(new DivinityFalsePower());
        game_database.getCell(0, 1).addDome();
        game_database.getCell(0, 2).addDome();
        game_database.getCell(1, 0).addDome();
        game_database.getCell(2, 0).addDome();
        game_database.getCell(0, 0).setActualLevel(3);
        game_database.getCell(1, 3).setPlayer(null);
        game_database.getCell(1, 3).setActualLevel(3);
        game_database.getCurrentPlayer().getDivinity().move(2, 2, 1, 1, game_database);
    }

    @Test
    public void validMoveCells_cantBuildAfterMoveButWins_containsThisCell() {
        ArrayList<Position> valid;
        ArrayList<Divinity> div = new ArrayList<>();
        div.add(new DivinityFalsePower());
        game_database.getCell(2, 2).setActualLevel(2);
        game_database.getCell(1, 1).setActualLevel(3);

        game_database.getCell(0, 1).addDome();
        game_database.getCell(0, 2).addDome();
        game_database.getCell(1, 0).addDome();
        game_database.getCell(2, 0).addDome();


        game_database.getCell(0, 0).setActualLevel(3);

        valid = game_database.getCurrentPlayer().getDivinity().getValidCellForMove(2, 2, game_database.getGameBoard(), div);
        ArrayList<Position> validExcpected = new ArrayList<>();


        validExcpected.add(new Position(1, 1));
        validExcpected.add(new Position(2, 1));
        validExcpected.add(new Position(2, 3));
        validExcpected.add(new Position(3, 1));

        valid.forEach(x -> System.out.println(x.getRow() + ", " + x.getColumn()));

        assertEquals(validExcpected.size(), valid.size());
        assertTrue(valid.containsAll(validExcpected));
    }
}