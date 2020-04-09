package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PrometheusTest {
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
        player1.setDivinity(new Prometheus());
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
    }

    @Test
    public void validMoveCellsBeforeBuilding() {
        ArrayList<Cell> validMoveCells = new ArrayList<>();

        player1.getDivinity().turnBegin(game_database);
        validMoveCells.add(game_database.getCell(3, 3));

        assertEquals(validMoveCells, player1.getDivinity().getValidCellForMove(4, 4, game_database.getGameBoard(), new ArrayList<>()));
    }

    @Test
    public void validMoveCellsAfterBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().build(4, 4, 3, 4, game_database);
        assertEquals(new ArrayList<Cell>(), player1.getDivinity().getValidCellForMove(4, 4, game_database.getGameBoard(), new ArrayList<>()));
    }

    @Test(expected = DivinityPowerException.class)
    public void tryingToLevelUpAfterBuilding_excpetionThrown() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException, NotEmptyCellException, IncorrectLevelException {
        player1.getDivinity().build(4, 4, 3, 4, game_database);
        player1.getDivinity().move(4, 4, 3, 3, game_database);
    }

    @Test
    public void validMoveCellsAfterDoming() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException, MaximumLevelNotReachedException {
        player1.getDivinity().dome(4, 4, 4, 3, game_database);
        assertEquals(new ArrayList<Cell>(), player1.getDivinity().getValidCellForMove(4, 4, game_database.getGameBoard(), new ArrayList<>()));
    }

    @Test
    public void correctMovingWithoutBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException, MaximumLevelNotReachedException, NotEmptyCellException, IncorrectLevelException {
        player1.getDivinity().move(4, 4, 3, 3, game_database);
    }

    @Test
    public void correctMovingWithoutLevelUpAfterBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException, NotEmptyCellException, IncorrectLevelException {
        game_database.getCell(3, 3).setActualLevel(0);
        player1.getDivinity().build(4, 4, 3, 4, game_database);
        player1.getDivinity().move(4, 4, 3, 3, game_database);

    }

    @Test
    public void correctMovingWithoutLevelUpAfterDoming() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdiacentCellException, DomedCellException, NotEmptyCellException, IncorrectLevelException, MaximumLevelNotReachedException {
        game_database.getCell(3, 3).setActualLevel(0);
        player1.getDivinity().dome(4, 4, 4, 3, game_database);
        player1.getDivinity().move(4, 4, 3, 3, game_database);

    }


}