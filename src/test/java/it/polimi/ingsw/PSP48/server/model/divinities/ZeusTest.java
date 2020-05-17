package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.Colour;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.model.divinities.testingDivinities.DivFalse;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class ZeusTest {
    Model game_database = new Model(2, true);
    Player player1 = new Player("Pippo", new GregorianCalendar(1998, Calendar.FEBRUARY, 21), true, Colour.BLUE);
    Player player2 = new Player("Paperino", new GregorianCalendar(2010, Calendar.MARCH, 10), true, Colour.WHITE);

    @Before
    public void testSetUp() {
        player1.setDivinity(new Zeus());
        player2.setDivinity(new Divinity());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.getPlayersInGame().add(player1);
        game_database.getPlayersInGame().add(player2);
        game_database.setNextPlayer(0);
        game_database.getCell(4, 4).setPlayer(player1.getName());

        game_database.getCell(3, 3).setActualLevel(1);
        game_database.getCell(4, 3).setActualLevel(3);
        game_database.getCell(3, 4).setActualLevel(2);
        game_database.getCell(3, 4).addDome();
    }

    @Test
    public void validCellForBuilding() {
        game_database.getCell(4, 4).setActualLevel(2);
        ArrayList<Position> vC = new ArrayList<>();
        vC.add(new Position(3, 3));
        vC.add(new Position(4, 4));
        assertEquals(vC, player1.getDivinity().getValidCellForBuilding(4, 4, new ArrayList<>(), game_database.getGameBoard()));
    }

    @Test
    public void validCellForBuildingButPlayerOnLevel3() {
        game_database.getCell(4, 4).setActualLevel(3);
        ArrayList<Position> vC = new ArrayList<>();
        vC.add(new Position(3, 3));
        assertEquals(vC, player1.getDivinity().getValidCellForBuilding(4, 4, new ArrayList<>(), game_database.getGameBoard()));
    }

    @Test(expected = MaximumLevelReachedException.class)
    public void buildOnMaximumLevel_exceptionThrown() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().build(4, 4, 4, 3, game_database);
    }

    @Test(expected = MaximumLevelReachedException.class)
    public void buildOnMaximumLevelOnSameCellOfWorker_exceptionThrown() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        game_database.getCell(4, 4).setActualLevel(3);
        player1.getDivinity().build(4, 4, 4, 4, game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void blockedByDivinity_excpetionThrown() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        player2.setDivinity(new DivFalse());
        player1.getDivinity().build(4, 4, 3, 3, game_database);
    }

    @Test(expected = OccupiedCellException.class)
    public void buildOnOtherPlayers() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        game_database.getCell(3, 3).setPlayer("kek");
        player1.getDivinity().build(4, 4, 3, 3, game_database);
    }

    @Test(expected = NotAdjacentCellException.class)
    public void notAdiacentCellTest_throwException() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().build(4, 4, 0, 0, game_database);
    }

    @Test(expected = DomedCellException.class)
    public void buildOnDomedCell_exceptionThrown() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().build(4, 4, 3, 4, game_database);
    }
    
    @Test
    public void correctBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().build(4, 4, 3, 3, game_database);
        assertEquals(2, game_database.getCell(3, 3).getLevel());
    }
}