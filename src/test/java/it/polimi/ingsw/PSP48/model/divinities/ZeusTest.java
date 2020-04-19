package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.server.model.divinities.Zeus;
import it.polimi.ingsw.PSP48.server.model.divinities.testingDivinities.DivFalse;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ZeusTest {
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
        player1.setDivinity(new Zeus());
        player2.setDivinity(new Divinity());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.addPlayer(player1);
        game_database.addPlayer(player2);
        game_database.setCurrentPlayer(0);
        game_database.getCell(4, 4).setPlayer(player1.getName());

        game_database.getCell(3, 3).setActualLevel(1);
        game_database.getCell(4, 3).setActualLevel(3);
        game_database.getCell(3, 4).setActualLevel(2);
        game_database.getCell(3, 4).addDome();
    }

    @Test
    public void validCellForBuilding() {
        game_database.getCell(4, 4).setActualLevel(2);
        ArrayList<Cell> vC = new ArrayList<>();
        vC.add(game_database.getCell(3, 3));
        vC.add(game_database.getCell(4, 4));
        assertEquals(vC, player1.getDivinity().getValidCellForBuilding(4, 4, new ArrayList<>(), game_database.getGameBoard()));
    }

    @Test
    public void validCellForBuildingButPlayerOnLevel3() {
        game_database.getCell(4, 4).setActualLevel(3);
        ArrayList<Cell> vC = new ArrayList<>();
        vC.add(game_database.getCell(3, 3));
        assertEquals(vC, player1.getDivinity().getValidCellForBuilding(4, 4, new ArrayList<>(), game_database.getGameBoard()));
    }

    @Test(expected = MaximumLevelReachedException.class)
    public void buildOnMaximumLevel_exceptionThrown() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().build(4, 4, 4, 3, game_database);
    }

    @Test(expected = MaximumLevelReachedException.class)
    public void buildOnMaximumLevelOnSameCellOfWorker_exceptionThrown() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        game_database.getCell(4, 4).setActualLevel(3);
        player1.getDivinity().build(4, 4, 4, 4, game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void blockedByDivinity_excpetionThrown() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player2.setDivinity(new DivFalse());
        player1.getDivinity().build(4, 4, 3, 3, game_database);
    }

    @Test(expected = OccupiedCellException.class)
    public void buildOnOtherPlayers() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        game_database.getCell(3, 3).setPlayer("kek");
        player1.getDivinity().build(4, 4, 3, 3, game_database);
    }

    @Test(expected = NotAdiacentCellException.class)
    public void notAdiacentCellTest_throwException() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().build(4, 4, 0, 0, game_database);
    }

    @Test(expected = DomedCellException.class)
    public void buildOnDomedCell_exceptionThrown() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().build(4, 4, 3, 4, game_database);
    }


    @Test
    public void correctBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().build(4, 4, 3, 3, game_database);
        assertEquals(2, game_database.getCell(3, 3).getLevel());
    }


}