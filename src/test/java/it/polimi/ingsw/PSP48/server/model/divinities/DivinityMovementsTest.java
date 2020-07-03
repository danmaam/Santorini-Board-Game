package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.ActionCoordinates;
import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class DivinityMovementsTest {

    Model game_database = new Model(2, true);
    Player player1 = new Player("Pippo", new GregorianCalendar(1998, Calendar.FEBRUARY, 21), true, Colour.BLUE);
    Player player2 = new Player("Paperino", new GregorianCalendar(2010, Calendar.MARCH, 10), true, Colour.WHITE);
    Divinity baseDivinity = new Divinity();
    Cell oldCell = new Cell(2, 2);
    Cell newCell = new Cell(1, 2);

    @Before
    public void testSetUp() {
        player1.setDivinity(baseDivinity);
        player2.setDivinity(new DivinityFalsePower());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.getPlayersInGame().add(player1);
        game_database.getPlayersInGame().add(player2);
        game_database.setNextPlayer(0);
        game_database.getCell(2, 2).setPlayer(player1.getName());
        game_database.getCell(1, 3).setPlayer(player1.getName());
        game_database.getCell(1, 1).setPlayer(player2.getName());
        try {
            game_database.getCell(1, 1).setActualLevel(1);

            game_database.getCell(2, 1).setActualLevel(3);

            game_database.getCell(2, 3).setActualLevel(3);

            game_database.getCell(3, 3).setActualLevel(1);
            game_database.getCell(3, 3).addDome();

            game_database.getCell(3, 1).setActualLevel(2);

            game_database.getCell(3, 2).setActualLevel(2);
            game_database.getCell(3, 2).addDome();
            game_database.getCell(3, 2).addLevel();
            game_database.getCell(3, 2).addDome();
        } catch (MaximumLevelReachedException e) {
            e.printStackTrace();
        }
        game_database.getCell(0, 0).setPlayer(player2.getName());
    }

    @org.junit.Test
    public void getValidCellForMoveTest() {
        ArrayList<Position> finalArrayList = new ArrayList<>();
        finalArrayList.add(new Position(1,2));
        ArrayList<Divinity> div = new ArrayList<>();
        div.add(baseDivinity);
        div.add(new DivinityFalsePower());
        assertEquals(finalArrayList, player1.getDivinity().getValidCellForMove(2, 2, game_database.getGameBoard(), div));
    }

    @org.junit.Test
    public void validMoveTest() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException{
        oldCell.setPlayer(null);
        newCell.setPlayer(player1.getName());
        player2.setDivinity(baseDivinity);
        player1.getDivinity().move(2, 2, 1, 2, game_database);
        assertEquals(newCell, game_database.getCell(1, 2));
        assertEquals(oldCell, game_database.getCell(2, 2));
    }

    @org.junit.Test(expected = OccupiedCellException.class)
    public void move_occupiedCell_excpetion() throws OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, DomedCellException, NoTurnEndException{
        oldCell.setPlayer(null);
        newCell.setPlayer(player1.getName());
        player1.getDivinity().move(2, 2, 1, 3, game_database);
    }

    @org.junit.Test(expected = DomedCellException.class)
    public void move_domedCell_excpetion() throws OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, DomedCellException, NoTurnEndException{
        oldCell.setPlayer(null);
        newCell.setPlayer(player1.getName());
        player1.getDivinity().move(2, 2, 3, 3, game_database);
    }

    @org.junit.Test(expected = NotAdjacentCellException.class)
    public void move_notAdiacentCell_excpetion() throws OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, DomedCellException, NoTurnEndException{
        oldCell.setPlayer(null);
        newCell.setPlayer(player1.getName());
        player1.getDivinity().move(2, 2, 4, 0, game_database);
    }

    @org.junit.Test(expected = DivinityPowerException.class)
    public void move_otherDivinityPower_excpetion() throws OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, DomedCellException, NoTurnEndException{
        oldCell.setPlayer(null);
        newCell.setPlayer(player1.getName());
        player1.getDivinity().move(2, 2, 1, 2, game_database);
    }

    @org.junit.Test(expected = IncorrectLevelException.class)
    public void move_incorrectLevel_excpetion() throws OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, DomedCellException, NoTurnEndException{
        oldCell.setPlayer(null);
        newCell.setPlayer(player1.getName());
        player1.getDivinity().move(2, 2, 3, 1, game_database);
    }

    @org.junit.Test
    public void othersMoveTest() {
        assertEquals(true, player1.getDivinity().othersMove(new ActionCoordinates(0, 0, 0, 0), game_database.getGameBoard()));
    }
}