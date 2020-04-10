package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class HestiaTest {

    GameData game_database = new GameData();
    Player player1 = new Player("Pippo", new Birthday(21, 02, 1998));
    Player player2 = new Player("Paperino", new Birthday(10, 03, 2010));
    Divinity baseDivinity = new Divinity();
    Cell oldCell = new Cell(2, 2);
    Cell newCell = new Cell(1, 2);

    @Before
    public void testSetUp() {
        player1.setColour(Colour.BLUE);
        player2.setColour(Colour.WHITE);
        player1.setDivinity(new Hestia());
        player2.setDivinity(new Divinity());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.addPlayer(player1);
        game_database.addPlayer(player2);
        game_database.setCurrentPlayer(0);
        game_database.getCell(1, 3).setPlayer(player1.getName());
    }

    @Test
    public void validCellsForBuildingandDoming_firstBuildingOrDoming() {
        ArrayList<Cell> validCells = new ArrayList<>();

        player1.getDivinity().turnBegin(game_database);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0)) {
                    validCells.add(game_database.getCell(3 + i, 3 + j));
                }
            }
        }
        assertEquals(validCells, player1.getDivinity().getValidCellForBuilding(3, 3, new ArrayList<>(), game_database.getGameBoard()));

        game_database.getCell(3, 4).setActualLevel(3);
        game_database.getCell(4, 3).setActualLevel(3);
        game_database.getCell(4, 4).setActualLevel(3);

        ArrayList<Cell> validCellsDome = new ArrayList<>();
        validCellsDome.add(game_database.getCell(3, 4));
        validCellsDome.add(game_database.getCell(4, 3));
        validCellsDome.add(game_database.getCell(4, 4));

        assertEquals(validCellsDome, player1.getDivinity().getValidCellsToPutDome(3, 3, game_database.getGameBoard(), new ArrayList<>()));

    }

    @Test
    public void validCellsForBuildingAndDoming_afterBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().turnBegin(game_database);
        player1.getDivinity().build(3, 3, 3, 4, game_database);

        ArrayList<Cell> vB = new ArrayList<>();
        vB.add(game_database.getCell(2, 2));
        vB.add(game_database.getCell(2, 3));
        vB.add(game_database.getCell(3, 2));

        assertEquals(vB, player1.getDivinity().getValidCellForBuilding(3, 3, new ArrayList<>(), game_database.getGameBoard()));

        assertTrue(player1.getDivinity().getValidCellsToPutDome(3, 3, game_database.getGameBoard(), new ArrayList<>()).isEmpty());
    }

    @Test
    public void validCellsForBuildingAndDoming_afterDoming() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException, MaximumLevelNotReachedException {
        player1.getDivinity().turnBegin(game_database);

        game_database.getCell(3, 4).setActualLevel(3);
        game_database.getCell(4, 3).setActualLevel(3);
        game_database.getCell(4, 4).setActualLevel(3);

        player1.getDivinity().dome(3, 3, 3, 4, game_database);

        ArrayList<Cell> vB = new ArrayList<>();
        vB.add(game_database.getCell(2, 2));
        vB.add(game_database.getCell(2, 3));
        vB.add(game_database.getCell(3, 2));

        assertEquals(vB, player1.getDivinity().getValidCellForBuilding(3, 3, new ArrayList<>(), game_database.getGameBoard()));

        assertTrue(player1.getDivinity().getValidCellsToPutDome(3, 3, game_database.getGameBoard(), new ArrayList<>()).isEmpty());
    }

    @Test(expected = DivinityPowerException.class)
    public void buildSecondBuildingOnPerimetralCell_throwsException() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().build(3, 3, 3, 4, game_database);
        player1.getDivinity().build(3, 3, 4, 4, game_database);

    }

    @Test(expected = DivinityPowerException.class)
    public void buildSecondDomeOnPerimetralCell_throwsException() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        game_database.getCell(4, 3).setActualLevel(3);
        player1.getDivinity().build(3, 3, 3, 4, game_database);
        player1.getDivinity().build(3, 3, 4, 3, game_database);

    }

    @Test(expected = DivinityPowerException.class)
    public void buildSecondBuildingOnPerimetralCellAfterFirstDoming_throwsException() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException, MaximumLevelNotReachedException {
        game_database.getCell(4, 3).setActualLevel(3);
        player1.getDivinity().dome(3, 3, 4, 3, game_database);
        player1.getDivinity().build(3, 3, 4, 4, game_database);

    }

    @Test(expected = DivinityPowerException.class)
    public void buildSecondDomeOnPerimetralCellAfterBUilding_throwsException() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException, MaximumLevelNotReachedException {
        game_database.getCell(4, 3).setActualLevel(3);
        player1.getDivinity().build(3, 3, 3, 4, game_database);
        player1.getDivinity().dome(3, 3, 4, 3, game_database);
    }

    @Test
    public void correctSecondBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().build(3, 3, 3, 4, game_database);
        player1.getDivinity().build(3, 3, 2, 2, game_database);
    }

    @Test
    public void correctSecondDoming() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException, MaximumLevelNotReachedException {
        game_database.getCell(3, 4).setActualLevel(3);
        game_database.getCell(2, 2).setActualLevel(3);

        player1.getDivinity().dome(3, 3, 3, 4, game_database);
        player1.getDivinity().dome(3, 3, 2, 2, game_database);
    }

    @Test
    public void correctSecondDomingAfterFirstBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException, MaximumLevelNotReachedException {
        game_database.getCell(2, 2).setActualLevel(3);

        player1.getDivinity().build(3, 3, 3, 4, game_database);
        player1.getDivinity().dome(3, 3, 2, 2, game_database);
    }

    @Test
    public void correctSecondBuildingAfterFirstDoming() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException, MaximumLevelNotReachedException {
        game_database.getCell(3, 4).setActualLevel(3);

        player1.getDivinity().dome(3, 3, 3, 4, game_database);
        player1.getDivinity().build(3, 3, 2, 2, game_database);
    }


}