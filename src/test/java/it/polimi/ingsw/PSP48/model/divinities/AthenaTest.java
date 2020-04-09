package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AthenaTest {

    GameData game_database = new GameData();
    Player player1 = new Player("Pippo", new Birthday(21, 02, 1998));
    Player player2 = new Player("Paperino", new Birthday(10, 03, 2010));
    Divinity baseDivinity = new Divinity();
    Cell oldCell = new Cell(2, 2);
    Cell newCell = new Cell(1, 2);

    @Before
    public void testSetUp() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException, MaximumLevelReachedException {

        player1.setColour(Colour.BLUE);
        player2.setColour(Colour.WHITE);
        player1.setDivinity(new Divinity());
        player2.setDivinity(new Athena());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.addPlayer(player1);
        game_database.addPlayer(player2);
        game_database.setCurrentPlayer(1);

        game_database.getCell(2, 1).setPlayer(player1.getName());
        game_database.getCell(4, 0).setPlayer(player1.getName());
        game_database.getCell(4, 4).setPlayer(player2.getName());
        game_database.getCell(2, 3).setPlayer(player2.getName());


        game_database.getCell(0, 0).addDome();
        game_database.getCell(1, 0).addDome();
        game_database.getCell(2, 0).addDome();
        game_database.getCell(3, 0).addDome();
        game_database.getCell(0, 2).addDome();
        game_database.getCell(1, 2).addDome();
        game_database.getCell(2, 2).addDome();
        game_database.getCell(3, 2).addDome();
        game_database.getCell(0, 1).addDome();

        game_database.getCell(1, 1).setActualLevel(3);
        game_database.getCell(2, 1).setActualLevel(2);

        game_database.getCell(2, 3).setActualLevel(1);
        game_database.getCell(2, 4).setActualLevel(2);

        player2.getDivinity().turnBegin(game_database);
        player2.getDivinity().move(3, 2, 4, 2, game_database);
        player2.getDivinity().build(2, 4, 1, 4, game_database);
        game_database.setCurrentPlayer(0);
        player1.getDivinity().turnBegin(game_database);
    }

    @Test
    public void otherPlayerValidCellsTest() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException, MaximumLevelReachedException {


        ArrayList<Cell> validCells = new ArrayList<>();
        validCells.add(game_database.getCell(3, 1));
        ArrayList<Divinity> div = new ArrayList<>();
        for (Player p : game_database.getPlayersInGame()) {
            if (p.getName() != game_database.getCurrentPlayer().getName()) div.add(p.getDivinity());
        }
        assertEquals(validCells, player1.getDivinity().getValidCellForMove(1, 2, game_database.getGameBoard(), div));

    }

    @Test(expected = DivinityPowerException.class)
    public void otherPlayer_tryingToGroupUp_DivinityPowerExcpetion() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        player1.getDivinity().move(1, 2, 1, 1, game_database);
    }

    @Test
    public void correctMoving() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        Cell newCell = new Cell(3, 1, 0, player1.getName(), false);
        Cell oldCell = new Cell(2, 1, 2, null, false);

        player1.getDivinity().move(1, 2, 1, 3, game_database);

        assertEquals(newCell, game_database.getCell(3, 1));
        assertEquals(oldCell, game_database.getCell(2, 1));
    }


}