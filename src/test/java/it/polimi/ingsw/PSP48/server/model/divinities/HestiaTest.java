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

public class HestiaTest {
    Model game_database = new Model(2, true);
    Player player1 = new Player("Pippo", new GregorianCalendar(1998, Calendar.FEBRUARY, 21), true, Colour.BLUE);
    Player player2 = new Player("Paperino", new GregorianCalendar(2010, Calendar.MARCH, 10), true, Colour.WHITE);

    @Before
    public void testSetUp() {
        player1.setDivinity(new Hestia());
        player2.setDivinity(new Divinity());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.getPlayersInGame().add(player1);
        game_database.getPlayersInGame().add(player2);
        game_database.setNextPlayer(0);
        game_database.getCell(1, 3).setPlayer(player1.getName());
    }

    @Test
    public void validCellsForBuildingAndDoming_firstBuildingOrDoming() {
        ArrayList<Position> validCells = new ArrayList<>();

        player1.getDivinity().turnBegin(game_database);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) {
                    validCells.add(new Position(3 + i, 3 + j));
                }
            }
        }
        assertEquals(validCells, player1.getDivinity().getValidCellForBuilding(3, 3, new ArrayList<>(), game_database.getGameBoard()));

        game_database.getCell(3, 4).setActualLevel(3);
        game_database.getCell(4, 3).setActualLevel(3);
        game_database.getCell(4, 4).setActualLevel(3);

        ArrayList<Position> validCellsDome = new ArrayList<>();
        validCellsDome.add(new Position(3, 4));
        validCellsDome.add(new Position(4, 3));
        validCellsDome.add(new Position(4, 4));

        assertEquals(validCellsDome, player1.getDivinity().getValidCellsToPutDome(3, 3, game_database.getGameBoard(), new ArrayList<>()));
    }

    @Test
    public void validCellsForBuildingAndDoming_afterBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException{
        player1.getDivinity().turnBegin(game_database);
        assertEquals("RequestOptionalBuild{}", player1.getDivinity().build(3, 3, 3, 4, game_database).toString());

        ArrayList<Position> vB = new ArrayList<>();
        vB.add(new Position(2, 2));
        vB.add(new Position(2, 3));
        vB.add(new Position(3, 2));

        assertEquals(vB, player1.getDivinity().getValidCellForBuilding(3, 3, new ArrayList<>(), game_database.getGameBoard()));
        assertTrue(player1.getDivinity().getValidCellsToPutDome(3, 3, game_database.getGameBoard(), new ArrayList<>()).isEmpty());
    }

    @Test
    public void validCellsForBuildingAndDoming_afterDoming() throws DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException, MaximumLevelNotReachedException{
        player1.getDivinity().turnBegin(game_database);

        game_database.getCell(3, 4).setActualLevel(3);
        game_database.getCell(4, 3).setActualLevel(3);
        game_database.getCell(4, 4).setActualLevel(3);

        assertEquals("RequestOptionalBuild{}", player1.getDivinity().dome(3, 3, 3, 4, game_database).toString());

        ArrayList<Position> vB = new ArrayList<>();
        vB.add(new Position(2, 2));
        vB.add(new Position(2, 3));
        vB.add(new Position(3, 2));

        assertEquals(vB, player1.getDivinity().getValidCellForBuilding(3, 3, new ArrayList<>(), game_database.getGameBoard()));

        assertTrue(player1.getDivinity().getValidCellsToPutDome(3, 3, game_database.getGameBoard(), new ArrayList<>()).isEmpty());
    }

    @Test(expected = DivinityPowerException.class)
    public void buildSecondBuildingOnPerimetralCell_throwsException() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException{
        assertEquals("RequestOptionalBuild{}", player1.getDivinity().build(3, 3, 3, 4, game_database).toString());
        player1.getDivinity().build(3, 3, 4, 4, game_database);

    }

    @Test(expected = DivinityPowerException.class)
    public void buildSecondDomeOnPerimetralCell_throwsException() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException{
        game_database.getCell(4, 3).setActualLevel(3);
        assertEquals("RequestOptionalBuild{}", player1.getDivinity().build(3, 3, 3, 4, game_database).toString());
        player1.getDivinity().build(3, 3, 4, 3, game_database);

    }

    @Test(expected = DivinityPowerException.class)
    public void buildSecondBuildingOnPerimetralCellAfterFirstDoming_throwsException() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException, MaximumLevelNotReachedException{
        game_database.getCell(4, 3).setActualLevel(3);
        player1.getDivinity().dome(3, 3, 4, 3, game_database);
        player1.getDivinity().build(3, 3, 4, 4, game_database);

    }

    @Test(expected = DivinityPowerException.class)
    public void buildSecondDomeOnPerimetralCellAfterBUilding_throwsException() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException, MaximumLevelNotReachedException{
        game_database.getCell(4, 3).setActualLevel(3);
        player1.getDivinity().build(3, 3, 3, 4, game_database);
        player1.getDivinity().dome(3, 3, 4, 3, game_database);
    }

    @Test
    public void correctSecondBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        player1.getDivinity().build(3, 3, 3, 4, game_database);
        player1.getDivinity().build(3, 3, 2, 2, game_database);
    }

    @Test
    public void correctSecondDoming() throws DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException, MaximumLevelNotReachedException {
        game_database.getCell(3, 4).setActualLevel(3);
        game_database.getCell(2, 2).setActualLevel(3);

        assertEquals("RequestOptionalBuild{}", player1.getDivinity().dome(3, 3, 3, 4, game_database).toString());
        assertEquals("TurnEnd{}", player1.getDivinity().dome(3, 3, 2, 2, game_database).toString());
    }

    @Test
    public void correctSecondDomingAfterFirstBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException, MaximumLevelNotReachedException {
        game_database.getCell(2, 2).setActualLevel(3);

        assertEquals("RequestOptionalBuild{}", player1.getDivinity().build(3, 3, 3, 4, game_database).toString());
        assertEquals("TurnEnd{}", player1.getDivinity().dome(3, 3, 2, 2, game_database).toString());
    }

    @Test
    public void correctSecondBuildingAfterFirstDoming() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException, MaximumLevelNotReachedException {
        game_database.getCell(3, 4).setActualLevel(3);

        assertEquals("RequestOptionalBuild{}", player1.getDivinity().dome(3, 3, 3, 4, game_database).toString());
        assertEquals("TurnEnd{}", player1.getDivinity().build(3, 3, 2, 2, game_database).toString());
    }
}