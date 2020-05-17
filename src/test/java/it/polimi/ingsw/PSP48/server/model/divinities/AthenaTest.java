package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class AthenaTest {
    Model game_database = new Model(2, true);
    Player player1 = new Player("Pippo", new GregorianCalendar(1998, Calendar.FEBRUARY, 21), true, Colour.BLUE);
    Player player2 = new Player("Paperino", new GregorianCalendar(2010, Calendar.MARCH, 10), true, Colour.WHITE);
    
    @Before
    public void testSetUp() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, MaximumLevelReachedException, NoTurnEndException {
        player1.setDivinity(new Divinity());
        player2.setDivinity(new Athena());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.getPlayersInGame().add(player1);
        game_database.getPlayersInGame().add(player2);
        game_database.setNextPlayer(1);

        game_database.getCell(2, 1).setPlayer(player1.getName());
        game_database.getCell(4, 0).setPlayer(player1.getName());
        game_database.getCell(4, 4).setPlayer(player2.getName());
        game_database.getCell(2, 3).setPlayer(player2.getName());


        game_database.getCell(0, 0).addDome();
        game_database.getCell(1, 0).addDome();
        game_database.getCell(2, 0).addDome();
        game_database.getCell(3, 0).addDome();
        game_database.getCell(0, 2).addDome();
        game_database.getCell(1, 2).addDome();
        game_database.getCell(2, 2).addDome();
        game_database.getCell(3, 2).addDome();
        game_database.getCell(0, 1).addDome();

        game_database.getCell(1, 1).setActualLevel(3);
        game_database.getCell(2, 1).setActualLevel(2);

        game_database.getCell(2, 3).setActualLevel(1);
        game_database.getCell(2, 4).setActualLevel(2);

        player2.getDivinity().turnBegin(game_database);
        player2.getDivinity().move(3, 2, 4, 2, game_database);
        player2.getDivinity().build(2, 4, 1, 4, game_database);
        game_database.setNextPlayer(0);
        player1.getDivinity().turnBegin(game_database);
    }

    @Test
    public void otherPlayerValidCellsTest() {
        ArrayList<Position> validCells = new ArrayList<>();
        validCells.add(new Position(3, 1));
        ArrayList<Divinity> div = new ArrayList<>();
        for (Player p : game_database.getPlayersInGame()) {
            if (!p.getName().equals(game_database.getCurrentPlayer().getName())) div.add(p.getDivinity());
        }
        assertEquals(validCells, player1.getDivinity().getValidCellForMove(1, 2, game_database.getGameBoard(), div));
    }

    @Test(expected = DivinityPowerException.class)
    public void otherPlayer_tryingToGroupUp_DivinityPowerExcpetion() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException{
        player1.getDivinity().move(1, 2, 1, 1, game_database);
    }

    @Test
    public void correctMoving() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException{
        Cell newCell = new Cell(3, 1, 0, player1.getName(), false);
        Cell oldCell = new Cell(2, 1, 2, null, false);

        player1.getDivinity().move(1, 2, 1, 3, game_database);

        assertEquals(newCell, game_database.getCell(3, 1));
        assertEquals(oldCell, game_database.getCell(2, 1));
    }
}