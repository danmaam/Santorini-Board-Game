package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.model.*;
import it.polimi.ingsw.PSP48.model.exceptions.DivinityPowerException;
import it.polimi.ingsw.PSP48.model.exceptions.NotEmptyCellException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ErosTest {
    GameData game_database = new GameData();
    Player player1 = new Player("Pippo", new Birthday(21, 02, 1998));
    Player player2 = new Player("Paperino", new Birthday(10, 03, 2010));
    Player player3 = new Player("Sora", new Birthday(23, 10, 1999));
    Divinity baseDivinity = new Divinity();
    Cell oldCell = new Cell(2, 2);
    Cell newCell = new Cell(1, 2);

    @Before
    public void testSetUp() {
        player1.setColour(Colour.BLUE);
        player2.setColour(Colour.WHITE);
        player1.setDivinity(new Eros());
        player2.setDivinity(new Divinity());
        player3.setDivinity(new Divinity());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.addPlayer(player1);
        game_database.addPlayer(player2);
        game_database.setCurrentPlayer(0);
        game_database.getCell(0, 2).setPlayer(player2.getName());
        game_database.getCell(4, 2).setPlayer(player2.getName());
        game_database.getCell(2, 0).setPlayer(player3.getName());
        game_database.getCell(2, 4).setPlayer(player3.getName());
    }

    @Test
    public void initialPositioningTest() {
        ArrayList<Cell> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 0 || i == 4 || j == 0 || j == 4) validCell.add(game_database.getCell(i, j));
            }
        }

        validCell.remove(game_database.getCell(0, 2));
        validCell.remove(game_database.getCell(4, 2));
        validCell.remove(game_database.getCell(2, 0));
        validCell.remove(game_database.getCell(2, 4));

        player1.getDivinity().turnBegin(game_database);

        assertEquals(validCell, player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()));
    }

    @Test
    public void secondPositioningDown() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 3, game_database.getGameBoard(), player1.getName());

        ArrayList<Cell> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(0, i).getPlayer() == null) validCell.add(game_database.getCell(0, i));
        }

        assertEquals(validCell, player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()));
    }

    @Test
    public void secondPositioningUp() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 3, game_database.getGameBoard(), player1.getName());

        ArrayList<Cell> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(4, i).getPlayer() == null) validCell.add(game_database.getCell(0, i));
        }

        assertEquals(validCell, player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()));
    }

    @Test
    public void secondPositioningLeft() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(3, 0, game_database.getGameBoard(), player1.getName());

        ArrayList<Cell> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(i, 4).getPlayer() == null) validCell.add(game_database.getCell(0, i));
        }

        assertEquals(validCell, player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()));
    }

    @Test
    public void secondPositioningRight() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(3, 4, game_database.getGameBoard(), player1.getName());

        ArrayList<Cell> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(0, i).getPlayer() == null) validCell.add(game_database.getCell(0, i));
        }

        assertEquals(validCell, player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()));
    }

    @Test
    public void secondPositioningRightDown() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 4, game_database.getGameBoard(), player1.getName());

        ArrayList<Cell> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(i, 0).getPlayer() == null) validCell.add(game_database.getCell(0, i));
        }

        for (int i = 1; i < 5; i++) {
            if (game_database.getCell(0, i).getPlayer() == null) validCell.add(game_database.getCell(0, i));

        }

        assertEquals(validCell, player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()));
    }

    @Test
    public void secondPositioningRightUp() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 4, game_database.getGameBoard(), player1.getName());

        ArrayList<Cell> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(i, 0).getPlayer() == null) validCell.add(game_database.getCell(0, i));
        }

        for (int i = 1; i < 5; i++) {
            if (game_database.getCell(4, i).getPlayer() == null) validCell.add(game_database.getCell(0, i));

        }

        assertEquals(validCell, player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()));
    }

    @Test
    public void secondPositioningLeftDown() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 0, game_database.getGameBoard(), player1.getName());

        ArrayList<Cell> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(0, i).getPlayer() == null) validCell.add(game_database.getCell(0, i));
        }

        for (int i = 1; i < 5; i++) {
            if (game_database.getCell(i, 4).getPlayer() == null) validCell.add(game_database.getCell(0, i));

        }

        assertEquals(validCell, player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()));
    }

    @Test
    public void secondPositioningLeftUp() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 0, game_database.getGameBoard(), player1.getName());

        ArrayList<Cell> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(4, i).getPlayer() == null) validCell.add(game_database.getCell(0, i));
        }

        for (int i = 1; i < 5; i++) {
            if (game_database.getCell(i, 4).getPlayer() == null) validCell.add(game_database.getCell(0, i));

        }

        assertEquals(validCell, player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()));
    }

    @Test(expected = DivinityPowerException.class)
    public void tryingToSetAtTheCenter_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(2, 2, game_database.getGameBoard(), player1.getName());
    }

    //down positioning tests
    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningDownTryToPositionOnRight_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 3, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(1, 4, game_database.getGameBoard(), player1.getName());
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningDownTryToPositionOnLeft_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 3, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(3, 0, game_database.getGameBoard(), player1.getName());
    }

    @Test
    public void gameSetUp_InitialPositioningDownTryToPositionOnUp() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 3, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(0, 0, game_database.getGameBoard(), player1.getName());
    }


}