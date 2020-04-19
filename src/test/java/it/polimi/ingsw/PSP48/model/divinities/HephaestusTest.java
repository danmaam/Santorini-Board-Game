package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.server.model.divinities.Hephaestus;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class HephaestusTest {
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
        player1.setDivinity(new Hephaestus());
        player2.setDivinity(new Divinity());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.addPlayer(player1);
        game_database.addPlayer(player2);
        game_database.setCurrentPlayer(0);
        game_database.getCell(1, 3).setPlayer(player1.getName());

        game_database.getCell(0, 3).setActualLevel(2);
        game_database.getCell(1, 4).setActualLevel(1);
        game_database.getCell(0, 4).setActualLevel(3);
    }

    @Test
    public void validCellsAfterDoubleBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        ArrayList<Cell> vB = new ArrayList<>();
        ArrayList<Cell> vD = new ArrayList<>();

        player1.getDivinity().turnBegin(game_database);

        vB.add(game_database.getCell(0, 3));
        vB.add(game_database.getCell(1, 4));
        vB.add(game_database.getCell(0, 2));
        vB.add(game_database.getCell(1, 2));
        vB.add(game_database.getCell(2, 2));
        vB.add(game_database.getCell(2, 3));
        vB.add(game_database.getCell(2, 4));

        vD.add(game_database.getCell(0, 4));


        assertTrue(vB.size() == player1.getDivinity().getValidCellForBuilding(3, 1, new ArrayList<>(), game_database.getGameBoard()).size());
        assertTrue(vB.containsAll(player1.getDivinity().getValidCellForBuilding(3, 1, new ArrayList<>(), game_database.getGameBoard())));


        assertTrue(vD.size() == player1.getDivinity().getValidCellsToPutDome(3, 1, game_database.getGameBoard(), new ArrayList<>()).size());
        assertTrue(vD.containsAll(player1.getDivinity().getValidCellsToPutDome(3, 1, game_database.getGameBoard(), new ArrayList<>())));

        player1.getDivinity().build(1, 3, 1, 4, game_database);

        vB = new ArrayList<>();
        vB.add(game_database.getCell(1, 4));
        vD = new ArrayList<>();

        assertEquals(vB, player1.getDivinity().getValidCellForBuilding(3, 1, new ArrayList<>(), game_database.getGameBoard()));
        assertEquals(vD, player1.getDivinity().getValidCellsToPutDome(3, 1, game_database.getGameBoard(), new ArrayList<>()));
    }

    @Test
    public void validCellsAfterDoubleBuildingTheThirdLevel() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        ArrayList<Cell> vB = new ArrayList<>();
        ArrayList<Cell> vD = new ArrayList<>();

        player1.getDivinity().turnBegin(game_database);

        vB.add(game_database.getCell(0, 3));
        vB.add(game_database.getCell(1, 4));
        vB.add(game_database.getCell(0, 2));
        vB.add(game_database.getCell(1, 2));
        vB.add(game_database.getCell(2, 2));
        vB.add(game_database.getCell(2, 3));
        vB.add(game_database.getCell(2, 4));

        vD.add(game_database.getCell(0, 4));


        assertTrue(vB.size() == player1.getDivinity().getValidCellForBuilding(3, 1, new ArrayList<>(), game_database.getGameBoard()).size());
        assertTrue(vB.containsAll(player1.getDivinity().getValidCellForBuilding(3, 1, new ArrayList<>(), game_database.getGameBoard())));


        assertTrue(vD.size() == player1.getDivinity().getValidCellsToPutDome(3, 1, game_database.getGameBoard(), new ArrayList<>()).size());
        assertTrue(vD.containsAll(player1.getDivinity().getValidCellsToPutDome(3, 1, game_database.getGameBoard(), new ArrayList<>())));

        player1.getDivinity().build(1, 3, 0, 3, game_database);

        vB = new ArrayList<>();
        vD = new ArrayList<>();

        assertEquals(vB, player1.getDivinity().getValidCellForBuilding(3, 1, new ArrayList<>(), game_database.getGameBoard()));
        assertEquals(vD, player1.getDivinity().getValidCellsToPutDome(3, 1, game_database.getGameBoard(), new ArrayList<>()));
    }

    @Test
    public void validCellsAfterDomingTheThirdLevel() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException, MaximumLevelNotReachedException {
        ArrayList<Cell> vB = new ArrayList<>();
        ArrayList<Cell> vD = new ArrayList<>();

        player1.getDivinity().turnBegin(game_database);

        vB.add(game_database.getCell(0, 3));
        vB.add(game_database.getCell(1, 4));
        vB.add(game_database.getCell(0, 2));
        vB.add(game_database.getCell(1, 2));
        vB.add(game_database.getCell(2, 2));
        vB.add(game_database.getCell(2, 3));
        vB.add(game_database.getCell(2, 4));

        vD.add(game_database.getCell(0, 4));


        assertTrue(vB.containsAll(player1.getDivinity().getValidCellForBuilding(3, 1, new ArrayList<>(), game_database.getGameBoard())));
        assertTrue(vB.size() == player1.getDivinity().getValidCellForBuilding(3, 1, new ArrayList<>(), game_database.getGameBoard()).size());


        assertTrue(vD.containsAll(player1.getDivinity().getValidCellsToPutDome(3, 1, game_database.getGameBoard(), new ArrayList<>())));
        assertTrue(vD.size() == player1.getDivinity().getValidCellsToPutDome(3, 1, game_database.getGameBoard(), new ArrayList<>()).size());

        player1.getDivinity().dome(1, 3, 0, 4, game_database);

        vB = new ArrayList<>();
        vD = new ArrayList<>();

        assertEquals(vB, player1.getDivinity().getValidCellForBuilding(3, 1, new ArrayList<>(), game_database.getGameBoard()));
        assertEquals(vD, player1.getDivinity().getValidCellsToPutDome(3, 1, game_database.getGameBoard(), new ArrayList<>()));
    }

    @Test(expected = DivinityPowerException.class)
    public void buildOnDifferentCellThanTheFirst_excpetionThrown() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().build(1, 3, 1, 4, game_database);
        player1.getDivinity().build(1, 3, 0, 3, game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void domeCellBuilt_exception() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException, MaximumLevelNotReachedException {
        player1.getDivinity().build(1, 3, 0, 3, game_database);
        player1.getDivinity().dome(1, 3, 0, 3, game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void addDomeOnOtherCellDifferentFromTheFirst_exceptionThrown() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException, MaximumLevelNotReachedException {
        player1.getDivinity().build(1, 3, 0, 3, game_database);
        player1.getDivinity().dome(1, 3, 0, 4, game_database);
    }


}