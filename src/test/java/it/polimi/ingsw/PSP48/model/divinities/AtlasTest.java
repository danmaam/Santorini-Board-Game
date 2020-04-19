package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.divinities.Atlas;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.server.model.divinities.testingDivinities.DivinityFalsePower;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class AtlasTest {

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
        player1.setDivinity(new Atlas());
        player2.setDivinity(new DivinityFalsePower());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.addPlayer(player1);
        game_database.addPlayer(player2);
        game_database.setCurrentPlayer(0);

        game_database.getCell(1, 3).setPlayer(player2.getName());
        game_database.getCell(2, 2).setPlayer(player1.getName());
        game_database.getCell(3, 3).setPlayer(player2.getName());

        game_database.getCell(2, 1).setActualLevel(2);
        game_database.getCell(3, 1).setActualLevel(1);
        game_database.getCell(3, 2).setActualLevel(2);
        game_database.getCell(3, 2).addDome();
        game_database.getCell(2, 3).setActualLevel(3);
        game_database.getCell(1, 3).setActualLevel(1);


    }

    @Test
    public void validCellsTest() {
        ArrayList<Cell> validCells = new ArrayList<>();
        validCells.add(game_database.getCell(1, 1));
        validCells.add(game_database.getCell(1, 2));
        validCells.add(game_database.getCell(3, 1));
        validCells.add(game_database.getCell(2, 3));

        ArrayList<Divinity> div = new ArrayList<>();
        div.add(player2.getDivinity());
        assertTrue(validCells.containsAll(player1.getDivinity().getValidCellsToPutDome(2, 2, game_database.getGameBoard(), div)));
        assertTrue(validCells.size() == player1.getDivinity().getValidCellsToPutDome(2, 2, game_database.getGameBoard(), div).size());


    }

    @Test(expected = DomedCellException.class)
    public void domingADomedCell_exception() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 3, 2, game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void otherDivinityBlock_exception() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 2, 1, game_database);
    }

    @Test(expected = DomedCellException.class)
    public void domingAnOccupiedCell() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 3, 2, game_database);
    }

    @Test(expected = NotAdiacentCellException.class)
    public void tryingToDomeANotAdjacentCell_throwsException() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 0, 0, game_database);
    }

    @Test
    public void correctDome() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().dome(2, 2, 3, 1, game_database);
        assertTrue(game_database.getCell(3, 1).isDomed());
    }

}