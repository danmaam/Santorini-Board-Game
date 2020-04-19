package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.divinities.Demeter;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DemeterTest {
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
        player1.setDivinity(new Demeter());
        player2.setDivinity(new Divinity());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.addPlayer(player1);
        game_database.addPlayer(player2);
        game_database.setCurrentPlayer(0);
        game_database.getCell(0, 0).setPlayer(player1.getName());

        game_database.getCell(0, 1).setActualLevel(2);
        game_database.getCell(1, 0).setActualLevel(1);
        game_database.getCell(1, 1).setActualLevel(3);
    }

    @Test
    public void domeBuildValidCellTestAfterBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        ArrayList<Cell> validForBuild = new ArrayList<>();
        ArrayList<Cell> validForDoming = new ArrayList<>();

        validForBuild.add(game_database.getCell(0, 1));
        validForBuild.add(game_database.getCell(1, 0));

        validForDoming.add(game_database.getCell(1, 1));

        player1.getDivinity().turnBegin(game_database);
        player1.getDivinity().turnBegin(game_database);
        assertEquals(validForDoming, player1.getDivinity().getValidCellsToPutDome(0, 0, game_database.getGameBoard(), new ArrayList<Divinity>()));
        assertEquals(validForBuild, player1.getDivinity().getValidCellForBuilding(0, 0, new ArrayList<Divinity>(), game_database.getGameBoard()));

        player1.getDivinity().build(0, 0, 1, 0, game_database);

        validForBuild.remove(game_database.getCell(1, 0));

        assertEquals(validForDoming, player1.getDivinity().getValidCellsToPutDome(0, 0, game_database.getGameBoard(), new ArrayList<Divinity>()));
        assertEquals(validForBuild, player1.getDivinity().getValidCellForBuilding(0, 0, new ArrayList<Divinity>(), game_database.getGameBoard()));
    }

    @Test
    public void domeBuildValidCellTestAfterDome() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException, MaximumLevelNotReachedException {
        ArrayList<Cell> validForBuild = new ArrayList<>();
        ArrayList<Cell> validForDoming = new ArrayList<>();

        validForBuild.add(game_database.getCell(0, 1));
        validForBuild.add(game_database.getCell(1, 0));

        validForDoming.add(game_database.getCell(1, 1));

        player1.getDivinity().turnBegin(game_database);
        player1.getDivinity().turnBegin(game_database);
        assertEquals(validForDoming, player1.getDivinity().getValidCellsToPutDome(0, 0, game_database.getGameBoard(), new ArrayList<Divinity>()));
        assertEquals(validForBuild, player1.getDivinity().getValidCellForBuilding(0, 0, new ArrayList<Divinity>(), game_database.getGameBoard()));

        player1.getDivinity().dome(0, 0, 1, 1, game_database);

        validForDoming.remove(game_database.getCell(1, 1));

        assertEquals(validForDoming, player1.getDivinity().getValidCellsToPutDome(0, 0, game_database.getGameBoard(), new ArrayList<Divinity>()));
        assertEquals(validForBuild, player1.getDivinity().getValidCellForBuilding(0, 0, new ArrayList<Divinity>(), game_database.getGameBoard()));
    }

    @Test(expected = DivinityPowerException.class)
    public void reBuildOnTheSameCell_throwException() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdiacentCellException, DomedCellException, MaximumLevelReachedException {

        player1.getDivinity().build(0, 0, 1, 0, game_database);
        player1.getDivinity().build(0, 0, 1, 0, game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void reDomeOnTheSameCell_throwException() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdiacentCellException, DomedCellException, MaximumLevelReachedException {

        player1.getDivinity().dome(0, 0, 1, 1, game_database);
        player1.getDivinity().dome(0, 0, 1, 1, game_database);
    }


}