package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.exceptions.DivinityPowerException;
import it.polimi.ingsw.PSP48.server.model.exceptions.OccupiedCellException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class DivinityPositioningTest {
    Model game_database = new Model(2, true);
    Player player1 = new Player("Pippo", new GregorianCalendar(1998, Calendar.FEBRUARY, 21), true, Colour.BLUE);
    Player player2 = new Player("Paperino", new GregorianCalendar(2010, Calendar.MARCH, 10), true, Colour.WHITE);
    Player player3 = new Player("Sora", new GregorianCalendar(1999, Calendar.JANUARY, 5), true, Colour.GRAY);
    Divinity baseDivinity = new Divinity();

    @Before
    public void testSetUp() {
        player1.setDivinity(baseDivinity);
        player2.setDivinity(baseDivinity);
        player3.setDivinity(baseDivinity);
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        player3.setTempDivinity(null);
        game_database.getPlayersInGame().add(player1);
        game_database.getPlayersInGame().add(player2);
        game_database.getPlayersInGame().add(player3);
        game_database.setNextPlayer(2);
        game_database.getCell(0, 0).setPlayer(player1.getName());
        game_database.getCell(0, 3).setPlayer(player1.getName());
        game_database.getCell(4, 2).setPlayer(player2.getName());
        game_database.getCell(4, 4).setPlayer(player2.getName());
        game_database.getCell(3, 1).setPlayer(player3.getName());
    }

    @org.junit.Test
    public void correctValidCells() {
        ArrayList<Position> validCell = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (game_database.getCell(i, j).getPlayer() == null) validCell.add(new Position(i, j));
            }
        }
        assertEquals(validCell, player3.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()));
    }
    @org.junit.Test
    public void correctPositioningCell() throws OccupiedCellException, DivinityPowerException {
        player3.getDivinity().putWorkerOnBoard(new Position(0, 2), game_database);
        Cell newCell = new Cell(0, 2);
        newCell.setPlayer(player3.getName());
        assertEquals(newCell, game_database.getCell(0, 2));
    }

    @org.junit.Test(expected = OccupiedCellException.class)
    public void positioningCell_occupiedCell_throwsException() throws OccupiedCellException, DivinityPowerException {
        player3.getDivinity().putWorkerOnBoard(new Position(0, 0), game_database);
    }

    @Test
    public void requestsNewPlayerPositioning() throws DivinityPowerException, OccupiedCellException {
        assertEquals("RequestInitialPositioning{}", player3.getDivinity().putWorkerOnBoard(new Position(4, 3), game_database).toString());
        assertEquals("InitialPositioningTurnChange{}", player3.getDivinity().putWorkerOnBoard(new Position(1, 1), game_database).toString());
    }
}