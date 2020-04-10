package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.divinities.testingDivinities.DivinityFalsePower;
import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.model.exceptions.*;
import org.junit.Before;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class DivinityBuildingTest {
    GameData game_database = new GameData();
    Player player1 = new Player("Pippo", new Birthday(21, 02, 1998));
    Player player2 = new Player("Paperino", new Birthday(10, 03, 2010));
    Divinity baseDivinity = new Divinity();
    Cell oldCell = new Cell(2, 2);
    Cell newCell = new Cell(1, 2);

    @Before
    public void setUpTest() {
        player1.setColour(Colour.BLUE);
        player2.setColour(Colour.WHITE);
        player1.setDivinity(baseDivinity);
        player2.setDivinity(new DivinityFalsePower());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.addPlayer(player1);
        game_database.addPlayer(player2);
        game_database.setCurrentPlayer(0);
        game_database.getCell(2, 2).setPlayer(player1.getName());
        game_database.getCell(1, 3).setPlayer(player1.getName());
        game_database.getCell(1, 1).setPlayer(player2.getName());
        try {
            game_database.getCell(1, 1).addLevel();

            game_database.getCell(2, 1).addLevel();
            game_database.getCell(2, 1).addLevel();
            game_database.getCell(2, 1).addLevel();

            game_database.getCell(2, 3).addLevel();
            game_database.getCell(2, 3).addLevel();
            game_database.getCell(2, 3).addLevel();

            game_database.getCell(3, 3).addLevel();
            game_database.getCell(3, 3).addDome();


            game_database.getCell(3, 1).addLevel();
            game_database.getCell(3, 1).addLevel();

            game_database.getCell(3, 2).addLevel();
            game_database.getCell(3, 2).addLevel();
            game_database.getCell(3, 2).addDome();

            game_database.getCell(3, 2).addLevel();
            game_database.getCell(3, 2).addDome();
            ;
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
        ArrayList<Cell> validCells = new ArrayList<>();
        validCells.add(game_database.getCell(3, 1));
        ArrayList<Divinity> badDivinity = new ArrayList<>();
        badDivinity.add(new DivinityFalsePower());
        player1.getDivinity().getValidCellForBuilding(2, 2, badDivinity, game_database.getGameBoard());
    }

    @org.junit.Test(expected = NotAdiacentCellException.class)
    public void validBuildingCells_notAdjacentCell_notAdjacentException() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().build(2, 2, 0, 4, game_database);
    }

    @org.junit.Test(expected = MaximumLevelReachedException.class)
    public void validBuildingCells_maximumLevelReached_exception() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().build(2, 2, 2, 1, game_database);
    }

    @org.junit.Test(expected = DivinityPowerException.class)
    public void validBuildingCells_divinityPowerBlock_excpetion() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().build(2, 2, 1, 2, game_database);
    }

    @org.junit.Test(expected = OccupiedCellException.class)
    public void validBuildingCells_occupiedCell_notAdjacentException() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().build(2, 2, 1, 3, game_database);
    }

    @org.junit.Test(expected = DomedCellException.class)
    public void validBuildingCells_alreadyDomedException_exception() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().build(2, 2, 3, 3, game_database);
    }

    @org.junit.Test
    public void correctBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().build(2, 2, 3, 1, game_database);
        assertEquals(3, game_database.getCell(3, 1).getLevel());
    }


}