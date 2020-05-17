package it.polimi.ingsw.PSP48. server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Colour;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.model.divinities.testingDivinities.DivinityFalsePower;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertTrue;

public class DivinityDomingTest {
    Model game_database = new Model(2, true);
    Player player1 = new Player("Pippo", new GregorianCalendar(1998, Calendar.FEBRUARY, 21), true, Colour.BLUE);
    Player player2 = new Player("Paperino", new GregorianCalendar(2010, Calendar.MARCH, 10), true, Colour.WHITE);
    Divinity baseDivinity = new Divinity();
    Cell newCell = new Cell(1, 2);

    @Before
    public void setUpTest() {
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
            game_database.getCell(1, 3).setActualLevel(3);

            game_database.getCell(3, 1).setActualLevel(3);
            game_database.getCell(3, 1).addDome();

            game_database.getCell(3, 2).setActualLevel(2);
            game_database.getCell(3, 2).addDome();
            game_database.getCell(3, 2).addLevel();
            game_database.getCell(3, 2).addDome();
        } catch (MaximumLevelReachedException e) {
            e.printStackTrace();
        }
        game_database.getCell(0, 0).setPlayer(player2.getName());
        try {
            newCell.addLevel();
        } catch (MaximumLevelReachedException e) {
            e.printStackTrace();
        }
    }

    @org.junit.Test
    public void domingCells() {
        ArrayList<Divinity> badDivinity = new ArrayList<>();
        badDivinity.add(new DivinityFalsePower());
        player1.getDivinity().getValidCellsToPutDome(2, 2, game_database.getGameBoard(), badDivinity);
    }

    @org.junit.Test(expected = NotAdjacentCellException.class)
    public void doming_cellNotAdjacent_Exception() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 0, 4, game_database);
    }

    @org.junit.Test(expected = DivinityPowerException.class)
    public void doming_otherDivinitiesBlock_Exception() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 2, 1, game_database);
    }

    @org.junit.Test(expected = OccupiedCellException.class)
    public void doming_OccupiedCell_Exception() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 1, 3, game_database);
    }

    @org.junit.Test(expected = MaximumLevelNotReachedException.class)
    public void doming_MaximumLevelNotReached_Exception() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 1, 2, game_database);
    }

    @org.junit.Test(expected = DomedCellException.class)
    public void doming_alreadyDomed_Exception() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 3, 1, game_database);
    }

    @org.junit.Test
    public void correctDomingTest() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 2, 3, game_database);
        assertTrue(game_database.getCell(2, 3).isDomed());
    }


}