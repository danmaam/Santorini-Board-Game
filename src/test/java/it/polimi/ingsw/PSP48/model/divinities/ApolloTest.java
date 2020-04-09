package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.divinities.testingDivinities.DivinityFalsePower;
import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ApolloTest {
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
        player1.setDivinity(new Apollo());
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
            game_database.getCell(1, 1).setActualLevel(1);

            game_database.getCell(2, 1).setActualLevel(3);

            game_database.getCell(2, 3).setActualLevel(3);

            game_database.getCell(3, 3).addLevel();
            game_database.getCell(3, 3).addDome();


            game_database.getCell(3, 1).setActualLevel(2);

            game_database.getCell(3, 2).setActualLevel(2);
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

    @Test
    public void validCellsForMovingWithSwapping() {
        ArrayList<Divinity> div = new ArrayList<>();
        div.add(new DivinityFalsePower());
        ArrayList<Cell> validCells = new ArrayList<>();
        validCells.add(game_database.getCell(1, 1));
        assertEquals(validCells, player1.getDivinity().getValidCellForMove(2, 2, game_database.getGameBoard(), div));
    }

    @Test
    public void moveTest() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        ArrayList<Divinity> div = new ArrayList<>();
        div.add(new DivinityFalsePower());
        player1.getDivinity().move(2, 2, 1, 1, game_database);
        assertEquals(new Cell(1, 1, 1, player1.getName(), false), game_database.getCell(1, 1));
        assertEquals(new Cell(2, 2, 0, player2.getName(), false), game_database.getCell(2, 2));
    }

    @org.junit.Test(expected = OccupiedCellException.class)
    public void move_occupiedCell_excpetion() throws OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotEmptyCellException, NotAdiacentCellException, DomedCellException {
        oldCell.setPlayer(null);
        newCell.setPlayer(player1.getName());
        player1.getDivinity().move(2, 2, 3, 1, game_database);
    }

    @org.junit.Test(expected = DomedCellException.class)
    public void move_domedCell_excpetion() throws OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotEmptyCellException, NotAdiacentCellException, DomedCellException {
        oldCell.setPlayer(null);
        newCell.setPlayer(player1.getName());
        player1.getDivinity().move(2, 2, 3, 3, game_database);
    }

    @org.junit.Test(expected = NotAdiacentCellException.class)
    public void move_notAdiacentCell_excpetion() throws OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotEmptyCellException, NotAdiacentCellException, DomedCellException {
        oldCell.setPlayer(null);
        newCell.setPlayer(player1.getName());
        player1.getDivinity().move(2, 2, 0, 4, game_database);
    }

    @org.junit.Test(expected = DivinityPowerException.class)
    public void move_otherDivinityPower_excpetion() throws OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotEmptyCellException, NotAdiacentCellException, DomedCellException {
        player1.getDivinity().move(2, 2, 2, 1, game_database);
    }

    @org.junit.Test(expected = IncorrectLevelException.class)
    public void move_incorrectLevel_excpetion() throws OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotEmptyCellException, NotAdiacentCellException, DomedCellException {
        oldCell.setPlayer(null);
        newCell.setPlayer(player1.getName());
        player1.getDivinity().move(2, 2, 1, 3, game_database);
    }
}