package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.Colour;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.model.Position;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ErosTest {
    Model game_database = new Model(2, true);
    Player player1 = new Player("Pippo", new GregorianCalendar(1998, Calendar.FEBRUARY, 21), true, Colour.BLUE);
    Player player2 = new Player("Paperino", new GregorianCalendar(2010, Calendar.MARCH, 10), true, Colour.WHITE);
    Player player3 = new Player("Sora", new GregorianCalendar(1999, Calendar.OCTOBER, 10), true, Colour.GRAY);

    @Before
    public void testSetUp() {
        player1.setDivinity(new Eros());
        player2.setDivinity(new Divinity());
        player3.setDivinity(new Divinity());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.getPlayersInGame().add(player1);
        game_database.getPlayersInGame().add(player2);
        game_database.getPlayersInGame().add(player3);
        game_database.setNextPlayer(0);
        game_database.getCell(0, 2).setPlayer(player2.getName());
        game_database.getCell(4, 2).setPlayer(player2.getName());
        game_database.getCell(2, 0).setPlayer(player3.getName());
        game_database.getCell(2, 4).setPlayer(player3.getName());
    }

    @Test
    public void initialPositioningTest() {
        ArrayList<Position> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 0 || i == 4 || j == 0 || j == 4) validCell.add(new Position(i, j));
            }
        }

        validCell.removeIf(x->(x.getRow()==0 && x.getColumn()==2));
        validCell.removeIf(x->(x.getRow()==4 && x.getColumn()==2));
        validCell.removeIf(x->(x.getRow()==2 && x.getColumn()==0));
        validCell.removeIf(x->(x.getRow()==2 && x.getColumn()==4));

        player1.getDivinity().turnBegin(game_database);

        assertEquals(validCell, player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()));
    }

    @Test
    public void secondPositioningDown() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position (4, 3), game_database);

        ArrayList<Position> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(0, i).getPlayer() == null) validCell.add(new Position(0, i));
        }

        assertEquals(validCell, player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()));
    }

    @Test
    public void secondPositioningUp() throws OccupiedCellException, DivinityPowerException{
        player1.getDivinity().putWorkerOnBoard(new Position (0, 3), game_database);

        ArrayList<Position> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(4, i).getPlayer() == null) validCell.add(new Position(4, i));
        }

        assertTrue(validCell.size() == player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()).size());
        assertTrue(validCell.containsAll(player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard())));
    }

    @Test
    public void secondPositioningLeft() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position (3, 0), game_database);

        ArrayList<Position> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(i, 4).getPlayer() == null) validCell.add(new Position(i, 4));
        }

        assertTrue(validCell.size() == player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()).size());
        assertTrue(validCell.containsAll(player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard())));
    }

    @Test
    public void secondPositioningRight() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position (3, 4), game_database);

        ArrayList<Position> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(i, 0).getPlayer() == null) validCell.add(new Position(i, 0));
        }

        assertTrue(validCell.size() == player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()).size());
        assertTrue(validCell.containsAll(player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard())));
    }

    @Test
    public void secondPositioningRightDown() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(4,4), game_database);

        ArrayList<Position> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(i, 0).getPlayer() == null) validCell.add(new Position(i, 0));
        }

        for (int i = 1; i < 5; i++) {
            if (game_database.getCell(0, i).getPlayer() == null) validCell.add(new Position(0, i));
        }

        assertTrue(validCell.size() == player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()).size());
        assertTrue(validCell.containsAll(player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard())));
    }

    @Test
    public void secondPositioningRightUp() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(0, 4), game_database);

        ArrayList<Position> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(i, 0).getPlayer() == null) validCell.add(new Position(i, 0));
        }

        for (int i = 1; i < 5; i++) {
            if (game_database.getCell(4, i).getPlayer() == null) validCell.add(new Position(4, i));
        }

        assertTrue(validCell.size() == player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()).size());
        assertTrue(validCell.containsAll(player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard())));
    }

    @Test
    public void secondPositioningLeftDown() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(4, 0), game_database);

        ArrayList<Position> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(0, i).getPlayer() == null) validCell.add(new Position(0, i));
        }

        for (int i = 1; i < 5; i++) {
            if (game_database.getCell(i, 4).getPlayer() == null) validCell.add(new Position(i, 4));
        }

        assertTrue(validCell.size() == player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()).size());
        assertTrue(validCell.containsAll(player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard())));
    }

    @Test
    public void secondPositioningLeftUp() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(0,0), game_database);

        ArrayList<Position> validCell = new ArrayList<>();

        //get perimetral cell
        for (int i = 0; i < 5; i++) {
            if (game_database.getCell(4, i).getPlayer() == null) validCell.add(new Position(4, i));
        }

        for (int i = 0; i < 4; i++) {
            if (game_database.getCell(i, 4).getPlayer() == null) validCell.add(new Position(i, 4));
        }

        assertTrue(validCell.size() == player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard()).size());
        assertTrue(validCell.containsAll(player1.getDivinity().validCellsForInitialPositioning(game_database.getGameBoard())));
    }

    @Test(expected = DivinityPowerException.class)
    public void tryingToSetAtTheCenter_throwsException() throws OccupiedCellException, DivinityPowerException{
        player1.getDivinity().putWorkerOnBoard(new Position(2,2), game_database);
    }

    //down positioning tests
    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningDownTryToPositionOnRight_throwsException() throws OccupiedCellException, DivinityPowerException{
        player1.getDivinity().putWorkerOnBoard(new Position(4,3), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(1,4), game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningDownTryToPositionOnLeft_throwsException() throws OccupiedCellException, DivinityPowerException{
        player1.getDivinity().putWorkerOnBoard(new Position(4, 3), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(3, 0), game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningDownTryToPositionOnDown_throwsException() throws OccupiedCellException, DivinityPowerException{
        player1.getDivinity().putWorkerOnBoard(new Position(4, 3), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position (4, 0), game_database);
    }

    @Test
    public void gameSetUp_InitialPositioningDownTryToPositionOnUp() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position (4, 3), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(0, 0), game_database);
    }

    //up positioning tests
    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningUpTryToPositionOnRight_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(0,1), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(1, 4), game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningUpTryToPositionOnUp_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position (0,1), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(0, 4), game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningUpTryToPositionOnLeft_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(0, 1), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(3, 0), game_database);
    }

    @Test
    public void gameSetUp_InitialPositioningUpTryToPositionOnDown() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(0, 1), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(4, 0), game_database);
    }

    //left positioning tests
    @Test
    public void gameSetUp_InitialPositioningLeftTryToPositionOnRight_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(1, 0), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(1, 4), game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningLeftTryToPositionOnDown_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(1,0), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(4, 1), game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningLeftTryToPositionOnUp_throwException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(1, 0), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(0, 3), game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningLeftTryToPositionOnLeft_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(1, 0), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(3, 0), game_database);
    }

    //right positioning tests
    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningRightTryToPositionOnRight_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(1, 4), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(3, 4), game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningRightTryToPositionOnDown_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(1, 4), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(4, 1), game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningRightTryToPositionOnUp_throwException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(1, 4), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(0, 3), game_database);
    }

    @Test
    public void gameSetUp_InitialPositioningRightTryToPositionOnLeft_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(1, 4), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(3, 0), game_database);
    }

    //RIGHT DOWN positioning tests
    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningRightDownTryToPositionOnRight_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(4, 4), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(3, 4), game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningRightDownTryToPositionOnDown_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(4, 4), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(4, 1), game_database);
    }

    @Test
    public void gameSetUp_InitialPositioningRightDownTryToPositionOnUp_throwException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(4, 4), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(0, 3), game_database);
    }

    @Test
    public void gameSetUp_InitialPositioningRightDownTryToPositionOnLeft_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(4, 4), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(3, 0), game_database);
    }

    //LEFTDOWN positioning tests
    @Test
    public void gameSetUp_InitialPositioningLeftDownTryToPositionOnRight_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(4, 0), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(3, 4), game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningLeftDownTryToPositionOnDown_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(4, 0), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(4, 1), game_database);
    }

    @Test
    public void gameSetUp_InitialPositioningLeftDownTryToPositionOnUp_throwException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(4, 0), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(0, 3), game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningLeftDownTryToPositionOnLeft_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(4, 0), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(3, 0), game_database);
    }

    //UPRIGHT positioning tests
    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningRightUpTryToPositionOnRight_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(0, 4), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position (3, 4), game_database);
    }

    @Test
    public void gameSetUp_InitialPositioningRightUpTryToPositionOnDown_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(0, 4), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(4, 1), game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningRightUpTryToPositionOnUp_throwException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(0, 4), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(0, 3), game_database);
    }

    @Test
    public void gameSetUp_InitialPositioningRightUpTryToPositionOnLeft_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(0, 4), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(3, 0), game_database);
    }

    //UPLEFT positioning tests
    @Test
    public void gameSetUp_InitialPositioningUpLeftTryToPositionOnRight_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(0, 0), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(3, 4), game_database);
    }

    @Test
    public void gameSetUp_InitialPositioningUpLeftTryToPositionOnDown_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(0, 0), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(4, 1), game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningUpLeftTryToPositionOnUp_throwException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(0, 0), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(0, 3), game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void gameSetUp_InitialPositioningUpLeftTryToPositionOnLeft_throwsException() throws OccupiedCellException, DivinityPowerException {
        player1.getDivinity().putWorkerOnBoard(new Position(0, 0), game_database);

        //test to position right
        player1.getDivinity().putWorkerOnBoard(new Position(3, 0), game_database);
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
    public void winCondition_forAllPlayerNumbers_player1OnDifferentLevels() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException {
        game_database.getCell(0, 0).setPlayer(player1.getName());
        game_database.getCell(2, 3).setPlayer(player1.getName());


        game_database.getCell(0, 0).setActualLevel(2);
        game_database.getCell(1, 1).setActualLevel(3);

        player1.getDivinity().move(0, 0, 1, 1, game_database);

        assertTrue(player1.getDivinity().winCondition(game_database));
    }
}
