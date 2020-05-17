package it.polimi.ingsw.PSP48.server.model.divinities;

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

import static org.junit.Assert.assertEquals;

public class DivinityBuildingTest {
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
            game_database.getCell(3, 1).setActualLevel(2);

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
    public void buildingCells() {
        ArrayList<Divinity> badDivinity = new ArrayList<>();
        badDivinity.add(new DivinityFalsePower());
        player1.getDivinity().getValidCellForBuilding(2, 2, badDivinity, game_database.getGameBoard());
    }

    @org.junit.Test(expected = NotAdjacentCellException.class)
    public void validBuildingCells_notAdjacentCell_notAdjacentException() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().build(2, 2, 0, 4, game_database);
    }

    @org.junit.Test(expected = MaximumLevelReachedException.class)
    public void validBuildingCells_maximumLevelReached_exception() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().build(2, 2, 2, 1, game_database);
    }

    @org.junit.Test(expected = DivinityPowerException.class)
    public void validBuildingCells_divinityPowerBlock_excpetion() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().build(2, 2, 1, 2, game_database);
    }

    @org.junit.Test(expected = OccupiedCellException.class)
    public void validBuildingCells_occupiedCell_notAdjacentException() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().build(2, 2, 1, 3, game_database);
    }

    @org.junit.Test(expected = DomedCellException.class)
    public void validBuildingCells_alreadyDomedException_exception() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().build(2, 2, 3, 3, game_database);
    }

    @org.junit.Test
    public void correctBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().build(2, 2, 3, 1, game_database);
        assertEquals(3, game_database.getCell(3, 1).getLevel());
    }


}