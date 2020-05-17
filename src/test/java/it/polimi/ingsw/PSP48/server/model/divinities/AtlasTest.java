package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.Colour;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.model.divinities.testingDivinities.DivinityFalsePower;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertTrue;

public class AtlasTest {
    Model game_database = new Model(2, true);
    Player player1 = new Player("Pippo", new GregorianCalendar(1998, Calendar.FEBRUARY, 21), true, Colour.BLUE);
    Player player2 = new Player("Paperino", new GregorianCalendar(2010, Calendar.MARCH, 10), true, Colour.WHITE);
    
    @Before
    public void testSetUp() {
        player1.setDivinity(new Atlas());
        player2.setDivinity(new DivinityFalsePower());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.getPlayersInGame().add(player1);
        game_database.getPlayersInGame().add(player2);
        game_database.setNextPlayer(0);

        game_database.getCell(1, 3).setPlayer(player2.getName());
        game_database.getCell(2, 2).setPlayer(player1.getName());
        game_database.getCell(3, 3).setPlayer(player2.getName());

        game_database.getCell(2, 1).setActualLevel(2);
        game_database.getCell(3, 1).setActualLevel(1);
        game_database.getCell(3, 2).setActualLevel(2);
        game_database.getCell(3, 2).addDome();
        game_database.getCell(2, 3).setActualLevel(3);
        game_database.getCell(1, 3).setActualLevel(1);
    }

    @Test
    public void validCellsTest() {
        ArrayList<Position> validCells = new ArrayList<>();
        validCells.add(new Position(1, 1));
        validCells.add(new Position(1, 2));
        validCells.add(new Position(3, 1));
        validCells.add(new Position(2, 3));
        ArrayList<Divinity> div = new ArrayList<>();
        div.add(player2.getDivinity());
        assertTrue(validCells.containsAll(player1.getDivinity().getValidCellsToPutDome(2, 2, game_database.getGameBoard(), div)));
        assertTrue(validCells.size() == player1.getDivinity().getValidCellsToPutDome(2, 2, game_database.getGameBoard(), div).size());
    }

    @Test(expected = DomedCellException.class)
    public void domingADomedCell_exception() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 3, 2, game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void otherDivinityBlock_exception() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 2, 1, game_database);
    }

    @Test(expected = DomedCellException.class)
    public void domingAnOccupiedCell() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 3, 2, game_database);
    }

    @Test(expected = NotAdjacentCellException.class)
    public void tryingToDomeANotAdjacentCell_throwsException() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 0, 0, game_database);
    }

    @Test
    public void correctDome() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 3, 1, game_database);
        assertTrue(game_database.getCell(3, 1).isDomed());
    }
}