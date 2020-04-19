package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.server.model.Birthday;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.GameData;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DivinityWinConditionTest {
    GameData game_database = new GameData();
    Player player1 = new Player("Pippo", new Birthday(21, 02, 1998));
    Player player2 = new Player("Paperino", new Birthday(10, 03, 2010));
    Divinity baseDivinity = new Divinity();
    Cell oldCell = new Cell(2, 2);
    Cell newCell = new Cell(1, 2);

    @Before
    public void testSetUp() {
        player1.setDivinity(baseDivinity);
        player2.setDivinity(baseDivinity);
        game_database.addPlayer(player1);
        game_database.addPlayer(player2);
        game_database.getCell(0, 1).setPlayer(player1.getName());
        game_database.getCell(3, 0).setPlayer(player1.getName());
        game_database.getCell(4, 4).setPlayer(player2.getName());
        game_database.getCell(0, 3).setPlayer(player2.getName());
        game_database.getCell(1, 3).setActualLevel(1);
        game_database.getCell(2, 3).setActualLevel(2);
        game_database.getCell(3, 4).setActualLevel(3);
        game_database.setCurrentPlayer(1);
    }

    @org.junit.Test
    public void correctWinSituation() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        player2.getDivinity().move(3, 0, 3, 1, game_database);
        player2.getDivinity().move(3, 1, 3, 2, game_database);
        player2.getDivinity().move(3, 2, 4, 3, game_database);
        assertTrue(player2.getDivinity().winCondition(game_database));
    }

    @Test
    public void notWinSituation() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        player2.getDivinity().move(3, 0, 3, 1, game_database);
        player2.getDivinity().move(3, 1, 3, 2, game_database);
        assertTrue(!player2.getDivinity().winCondition(game_database));
    }

    @Test
    public void notWinSituationWithPushing() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        player2.getDivinity().move(3, 0, 3, 1, game_database);
        player2.getDivinity().move(3, 1, 3, 2, game_database);
        game_database.getCell(3, 4).setPlayer(player2.getName());
        game_database.getCell(2, 3).setPlayer(null);
        assertTrue(!player2.getDivinity().winCondition(game_database));
    }

}