package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.server.model.divinities.Eros;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
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
        game_database.addPlayer(player3);
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
            if (game_database.getCell(4, i).getPlayer() == null) validCell.add(game_database.getCell(4, i));
        }

        assertTrue(validCell.size() == player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()).size());
        assertTrue(validCell.containsAll(player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard())));
    }

    @Test
    public void secondPositioningLeft() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(3, 0, game_database.getGameBoard(), player1.getName());

        ArrayList<Cell> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(i, 4).getPlayer() == null) validCell.add(game_database.getCell(i, 4));
        }

        assertTrue(validCell.size() == player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()).size());
        assertTrue(validCell.containsAll(player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard())));
    }

    @Test
    public void secondPositioningRight() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(3, 4, game_database.getGameBoard(), player1.getName());

        ArrayList<Cell> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(i, 0).getPlayer() == null) validCell.add(game_database.getCell(i, 0));
        }

        assertTrue(validCell.size() == player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()).size());
        assertTrue(validCell.containsAll(player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard())));
    }

    @Test
    public void secondPositioningRightDown() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 4, game_database.getGameBoard(), player1.getName());

        ArrayList<Cell> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(i, 0).getPlayer() == null) validCell.add(game_database.getCell(i, 0));
        }

        for (int i = 1; i < 5; i++) {
            if (game_database.getCell(0, i).getPlayer() == null) validCell.add(game_database.getCell(0, i));

        }

        assertTrue(validCell.size() == player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()).size());
        assertTrue(validCell.containsAll(player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard())));
    }

    @Test
    public void secondPositioningRightUp() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 4, game_database.getGameBoard(), player1.getName());

        ArrayList<Cell> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(i, 0).getPlayer() == null) validCell.add(game_database.getCell(i, 0));
        }

        for (int i = 1; i < 5; i++) {
            if (game_database.getCell(4, i).getPlayer() == null) validCell.add(game_database.getCell(4, i));

        }

        assertTrue(validCell.size() == player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()).size());
        assertTrue(validCell.containsAll(player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard())));
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
            if (game_database.getCell(i, 4).getPlayer() == null) validCell.add(game_database.getCell(i, 4));

        }

        assertTrue(validCell.size() == player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()).size());
        assertTrue(validCell.containsAll(player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard())));
    }

    @Test
    public void secondPositioningLeftUp() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 0, game_database.getGameBoard(), player1.getName());

        ArrayList<Cell> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(4, i).getPlayer() == null) validCell.add(game_database.getCell(4, i));
        }

        for (int i = 0; i < 4; i++) {
            if (game_database.getCell(i, 4).getPlayer() == null) validCell.add(game_database.getCell(i, 4));

        }

        assertTrue(validCell.size() == player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()).size());
        assertTrue(validCell.containsAll(player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard())));
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

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningDownTryToPositionOnDown_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 3, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(4, 0, game_database.getGameBoard(), player1.getName());
    }

    @Test
    public void gameSetUp_InitialPositioningDownTryToPositionOnUp() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 3, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(0, 0, game_database.getGameBoard(), player1.getName());
    }

    //up positioning tests
    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningUpTryToPositionOnRight_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 1, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(1, 4, game_database.getGameBoard(), player1.getName());
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningUpTryToPositionOnUp_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 1, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(0, 4, game_database.getGameBoard(), player1.getName());
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningUpTryToPositionOnLeft_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 1, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(3, 0, game_database.getGameBoard(), player1.getName());
    }

    @Test
    public void gameSetUp_InitialPositioningUpTryToPositionOnDown() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 1, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(4, 0, game_database.getGameBoard(), player1.getName());
    }

    //left positioning tests
    @Test
    public void gameSetUp_InitialPositioningLeftTryToPositionOnRight_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(1, 0, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(1, 4, game_database.getGameBoard(), player1.getName());
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningLeftTryToPositionOnDown_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(1, 0, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(4, 1, game_database.getGameBoard(), player1.getName());
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningLeftTryToPositionOnUp_throwException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(1, 0, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(0, 3, game_database.getGameBoard(), player1.getName());
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningLeftTryToPositionOnLeft_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(1, 0, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(3, 0, game_database.getGameBoard(), player1.getName());
    }

    //right positioning tests
    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningRightTryToPositionOnRight_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(1, 4, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(3, 4, game_database.getGameBoard(), player1.getName());
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningRightTryToPositionOnDown_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(1, 4, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(4, 1, game_database.getGameBoard(), player1.getName());
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningRightTryToPositionOnUp_throwException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(1, 4, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(0, 3, game_database.getGameBoard(), player1.getName());
    }

    @Test
    public void gameSetUp_InitialPositioningRightTryToPositionOnLeft_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(1, 4, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(3, 0, game_database.getGameBoard(), player1.getName());
    }

    //RIGHT DOWN positioning tests
    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningRightDownTryToPositionOnRight_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 4, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(3, 4, game_database.getGameBoard(), player1.getName());
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningRightDownTryToPositionOnDown_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 4, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(4, 1, game_database.getGameBoard(), player1.getName());
    }

    @Test
    public void gameSetUp_InitialPositioningRightDownTryToPositionOnUp_throwException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 4, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(0, 3, game_database.getGameBoard(), player1.getName());
    }

    @Test
    public void gameSetUp_InitialPositioningRightDownTryToPositionOnLeft_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 4, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(3, 0, game_database.getGameBoard(), player1.getName());
    }

    //LEFTDOWN positioning tests
    @Test
    public void gameSetUp_InitialPositioningLeftDownTryToPositionOnRight_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 0, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(3, 4, game_database.getGameBoard(), player1.getName());
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningLeftDownTryToPositionOnDown_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 0, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(4, 1, game_database.getGameBoard(), player1.getName());
    }

    @Test
    public void gameSetUp_InitialPositioningLeftDownTryToPositionOnUp_throwException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 0, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(0, 3, game_database.getGameBoard(), player1.getName());
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningLeftDownTryToPositionOnLeft_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(4, 0, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(3, 0, game_database.getGameBoard(), player1.getName());
    }

    //UPRIGHT positioning tests
    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningRightUpTryToPositionOnRight_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 4, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(3, 4, game_database.getGameBoard(), player1.getName());
    }

    @Test
    public void gameSetUp_InitialPositioningRightUpTryToPositionOnDown_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 4, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(4, 1, game_database.getGameBoard(), player1.getName());
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningRightUpTryToPositionOnUp_throwException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 4, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(0, 3, game_database.getGameBoard(), player1.getName());
    }

    @Test
    public void gameSetUp_InitialPositioningRightUpTryToPositionOnLeft_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 4, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(3, 0, game_database.getGameBoard(), player1.getName());
    }

    //UPLEFT positioning tests
    @Test
    public void gameSetUp_InitialPositioningUpLeftTryToPositionOnRight_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 0, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(3, 4, game_database.getGameBoard(), player1.getName());
    }

    @Test
    public void gameSetUp_InitialPositioningUpLeftTryToPositionOnDown_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 0, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(4, 1, game_database.getGameBoard(), player1.getName());
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningUpLeftTryToPositionOnUp_throwException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 0, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(0, 3, game_database.getGameBoard(), player1.getName());
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningUpLeftTryToPositionOnLeft_throwsException() throws NotEmptyCellException, DivinityPowerException {
        player1.getDivinity().gameSetUp(0, 0, game_database.getGameBoard(), player1.getName());

        //test to position right
        player1.getDivinity().gameSetUp(3, 0, game_database.getGameBoard(), player1.getName());
    }

    @Test
    public void winCondition_ThreePlayersInGame_player1Wins() {
        game_database.getCell(2, 2).setPlayer(player1.getName());
        game_database.getCell(2, 3).setPlayer(player1.getName());


        game_database.getCell(2, 2).setActualLevel(3);
        game_database.getCell(2, 3).setActualLevel(3);

        assertTrue(player1.getDivinity().winCondition(game_database));

        game_database.getCell(2, 2).setActualLevel(2);
        game_database.getCell(2, 3).setActualLevel(2);

        assertTrue(player1.getDivinity().winCondition(game_database));

        game_database.getCell(2, 2).setActualLevel(1);
        game_database.getCell(2, 3).setActualLevel(1);

        assertTrue(player1.getDivinity().winCondition(game_database));

        game_database.getCell(2, 2).setActualLevel(0);
        game_database.getCell(2, 3).setActualLevel(0);

        assertTrue(player1.getDivinity().winCondition(game_database));
    }

    @Test
    public void winCondition_TwoPlayersInGame_player1Wins() {
        game_database.getPlayersInGame().remove(player3);

        game_database.getCell(2, 2).setPlayer(player1.getName());
        game_database.getCell(2, 3).setPlayer(player1.getName());


        game_database.getCell(2, 2).setActualLevel(1);
        game_database.getCell(2, 3).setActualLevel(1);

        assertTrue(player1.getDivinity().winCondition(game_database));

    }

    @Test
    public void winCondition_TwoPlayersInGame_player1SameLevelsNotWin() {
        game_database.getPlayersInGame().remove(player3);

        game_database.getCell(2, 2).setPlayer(player1.getName());
        game_database.getCell(2, 3).setPlayer(player1.getName());


        game_database.getCell(2, 2).setActualLevel(0);
        game_database.getCell(2, 3).setActualLevel(0);

        assertTrue(!player1.getDivinity().winCondition(game_database));

        game_database.getCell(2, 2).setActualLevel(2);
        game_database.getCell(2, 3).setActualLevel(2);

        assertTrue(!player1.getDivinity().winCondition(game_database));

        game_database.getCell(2, 2).setActualLevel(3);
        game_database.getCell(2, 3).setActualLevel(3);

        assertTrue(!player1.getDivinity().winCondition(game_database));
    }

    @Test
    public void winCondition_forAllPlayerNumbers_player1OnDifferentLevels() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        game_database.getCell(0, 0).setPlayer(player1.getName());
        game_database.getCell(2, 3).setPlayer(player1.getName());


        game_database.getCell(0, 0).setActualLevel(2);
        game_database.getCell(1, 1).setActualLevel(3);

        player1.getDivinity().move(0, 0, 1, 1, game_database);

        assertTrue(player1.getDivinity().winCondition(game_database));
    }

    @Test
    public void winCondition_winsWithNormalRules() {

    }


}