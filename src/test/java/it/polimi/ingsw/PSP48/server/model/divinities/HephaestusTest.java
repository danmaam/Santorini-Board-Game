package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.Colour;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HephaestusTest {
    Model game_database = new Model(2, true);
    Player player1 = new Player("Pippo", new GregorianCalendar(1998, Calendar.FEBRUARY, 21), true, Colour.BLUE);
    Player player2 = new Player("Paperino", new GregorianCalendar(2010, Calendar.MARCH, 10), true, Colour.WHITE);

    @Before
    public void testSetUp() {
        player1.setColour(Colour.BLUE);
        player2.setColour(Colour.WHITE);
        player1.setDivinity(new Hephaestus());
        player2.setDivinity(new Divinity());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.getPlayersInGame().add(player1);
        game_database.getPlayersInGame().add(player2);
        game_database.setNextPlayer(0);
        game_database.getCell(1, 3).setPlayer(player1.getName());

        game_database.getCell(0, 3).setActualLevel(1);
        game_database.getCell(1, 4).setActualLevel(1);
        game_database.getCell(0, 4).setActualLevel(3);
    }

    @Test
    public void validCellsAfterDoubleBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        ArrayList<Position> vB = new ArrayList<>();
        ArrayList<Position> vD = new ArrayList<>();

        player1.getDivinity().turnBegin(game_database);

        vB.add(new Position(0, 3));
        vB.add(new Position(1, 4));
        vB.add(new Position(0, 2));
        vB.add(new Position(1, 2));
        vB.add(new Position(2, 2));
        vB.add(new Position(2, 3));
        vB.add(new Position(2, 4));

        vD.add(new Position(0, 4));


        assertTrue(vB.size() == player1.getDivinity().getValidCellForBuilding(1, 3, new ArrayList<>(), game_database.getGameBoard()).size());
        assertTrue(vB.containsAll(player1.getDivinity().getValidCellForBuilding(1, 3, new ArrayList<>(), game_database.getGameBoard())));


        assertTrue(vD.size() == player1.getDivinity().getValidCellsToPutDome(1, 3, game_database.getGameBoard(), new ArrayList<>()).size());
        assertTrue(vD.containsAll(player1.getDivinity().getValidCellsToPutDome(1, 3, game_database.getGameBoard(), new ArrayList<>())));

        player1.getDivinity().build(1, 3, 1, 4, game_database);

        vB = new ArrayList<>();
        vB.add(new Position(1, 4));
        vD = new ArrayList<>();

        assertEquals(vB, player1.getDivinity().getValidCellForBuilding(1, 3, new ArrayList<>(), game_database.getGameBoard()));
        assertEquals(vD, player1.getDivinity().getValidCellsToPutDome(1, 3, game_database.getGameBoard(), new ArrayList<>()));
    }

    @Test
    public void validCellsAfterDoubleBuildingTheThirdLevel() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        ArrayList<Position> vB = new ArrayList<>();
        ArrayList<Position> vD = new ArrayList<>();

        player1.getDivinity().turnBegin(game_database);

        vB.add(new Position(0, 3));
        vB.add(new Position(1, 4));
        vB.add(new Position(0, 2));
        vB.add(new Position(1, 2));
        vB.add(new Position(2, 2));
        vB.add(new Position(2, 3));
        vB.add(new Position(2, 4));

        vD.add(new Position(0, 4));


        assertTrue(vB.size() == player1.getDivinity().getValidCellForBuilding(1, 3, new ArrayList<>(), game_database.getGameBoard()).size());
        assertTrue(vB.containsAll(player1.getDivinity().getValidCellForBuilding(1, 3, new ArrayList<>(), game_database.getGameBoard())));


        assertTrue(vD.size() == player1.getDivinity().getValidCellsToPutDome(1, 3, game_database.getGameBoard(), new ArrayList<>()).size());
        assertTrue(vD.containsAll(player1.getDivinity().getValidCellsToPutDome(1, 3, game_database.getGameBoard(), new ArrayList<>())));

        player1.getDivinity().build(1, 3, 0, 3, game_database);

        vB = new ArrayList<>();
        vB.add(new Position(0, 3));
        vD = new ArrayList<>();

        assertEquals(vB, player1.getDivinity().getValidCellForBuilding(1, 3, new ArrayList<>(), game_database.getGameBoard()));
        assertEquals(vD, player1.getDivinity().getValidCellsToPutDome(1, 3, game_database.getGameBoard(), new ArrayList<>()));
    }


    @Test(expected = DivinityPowerException.class)
    public void buildOnDifferentCellThanTheFirst_excpetionThrown() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().build(1, 3, 1, 4, game_database);
        player1.getDivinity().build(1, 3, 0, 3, game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void domeCellBuilt_exception() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException, MaximumLevelNotReachedException {
        player1.getDivinity().build(1, 3, 0, 3, game_database);
        player1.getDivinity().dome(1, 3, 0, 3, game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void addDomeOnOtherCellDifferentFromTheFirst_exceptionThrown() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException, MaximumLevelNotReachedException {
        player1.getDivinity().build(1, 3, 0, 3, game_database);
        player1.getDivinity().dome(1, 3, 0, 4, game_database);
    }

    @Test
    public void buildForTheSecondTime() throws MaximumLevelReachedException, OccupiedCellException, NotAdjacentCellException, DomedCellException, DivinityPowerException {
        player1.getDivinity().build(1, 3, 0, 3, game_database);
        player1.getDivinity().build(1, 3, 0, 3, game_database);
        assertEquals(game_database.getCell(0, 3).getLevel(), 3);
    }

    @Test
    public void buildOnThirdLevel_requestTurnChange() throws MaximumLevelReachedException, OccupiedCellException, NotAdjacentCellException, DomedCellException, DivinityPowerException {
        game_database.getCell(0, 3).setActualLevel(2);
        game_database.getCurrentPlayer().getDivinity().build(1, 3, 0, 3, game_database);
        assertEquals(game_database.getCell(0, 3).getLevel(), 3);
    }

    @Test
    public void dome() throws OccupiedCellException, NotAdjacentCellException, MaximumLevelNotReachedException, DomedCellException, DivinityPowerException {
        game_database.getCell(2, 3).setActualLevel(3);
        player1.getDivinity().dome(1, 3, 2, 3, game_database);
        assertTrue(game_database.getCell(2, 3).isDomed());
    }
}