package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.server.model.divinities.Minotaur;
import it.polimi.ingsw.PSP48.server.model.divinities.testingDivinities.DivFalse;
import it.polimi.ingsw.PSP48.server.model.divinities.testingDivinities.DivinityFalsePower;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MinotaurTest {

    GameData gd = new GameData();
    Player p1 = new Player("Pippo", new Birthday(21, 02, 1998));
    Player p2 = new Player("Paperino", new Birthday(10, 03, 2010));
    Divinity baseDivinity = new Divinity();
    Cell oldCell = new Cell(2, 2);
    Cell newCell = new Cell(1, 2);

    @Before
    public void testSetUp() {
        p1.setColour(Colour.BLUE);
        p2.setColour(Colour.WHITE);
        p1.setDivinity(new Minotaur());
        p2.setDivinity(new DivinityFalsePower());
        p1.setTempDivinity(null);
        p2.setTempDivinity(null);
        gd.addPlayer(p1);
        gd.addPlayer(p2);
        gd.setCurrentPlayer(0);
        gd.getCell(2, 2).setPlayer(p1.getName());

        gd.getCell(1, 1).setPlayer(p2.getName());
        gd.getCell(1, 2).setPlayer(p2.getName());
        gd.getCell(1, 3).setPlayer(p2.getName());
        gd.getCell(2, 3).setPlayer(p2.getName());
        gd.getCell(3, 1).setPlayer(p2.getName());
        gd.getCell(3, 2).setPlayer(p2.getName());
        gd.getCell(3, 3).setPlayer(p2.getName());

        gd.getCell(0, 2).addDome();
        gd.getCell(0, 4).setPlayer("Riku");
        gd.getCell(4, 2).setPlayer("Kairi");
        gd.getCell(4, 1).setActualLevel(2);
        gd.getCell(2, 4).addDome();
        gd.getCell(0, 0).setActualLevel(1);
        gd.getCell(4, 3).setActualLevel(3);
    }

    @Test
    public void validCellsToMoveDifferentCombination() {


        ArrayList<Cell> vC = new ArrayList<>();

        //vC.add(gd.getCell(1,1));
        vC.add(gd.getCell(2, 1));
        vC.add(gd.getCell(3, 1));
        vC.add(gd.getCell(3, 3));

        ArrayList<Divinity> div = new ArrayList<>();
        div.add(new DivFalse());

        assertEquals(vC, p1.getDivinity().getValidCellForMove(2, 2, gd.getGameBoard(), div));
    }

    @Test(expected = OccupiedCellException.class)
    public void move_pushingCellNotEmpty() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        p1.getDivinity().move(2, 2, 3, 1, gd);
    }

    @Test(expected = OccupiedCellException.class)
    public void move_pushingCellDomed() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        p1.getDivinity().move(2, 2, 2, 1, gd);
    }

    @Test(expected = DivinityPowerException.class)
    public void move_PushOutOfBoard() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        gd.getCell(1, 1).setPlayer(p1.getName());
        gd.getCell(0, 0).setPlayer(p2.getName());
        p1.getDivinity().move(1, 1, 0, 0, gd);
    }

    @Test(expected = DivinityPowerException.class)
    public void move_pushWithColumnOutOfBoard() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        gd.getCell(2, 1).setPlayer(p1.getName());
        gd.getCell(2, 0).setPlayer(p2.getName());
        p1.getDivinity().move(1, 1, 0, 2, gd);
    }

    @Test(expected = DomedCellException.class)
    public void moveToADomedCell() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        gd = new GameData();
        gd.getCell(2, 2).setPlayer(p1.getName());
        gd.getCell(2, 3).addDome();
        p1.getDivinity().move(2, 2, 3, 2, gd);
    }

    @Test(expected = IncorrectLevelException.class)
    public void incorrectLevelException() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        gd = new GameData();
        gd.getCell(2, 2).setPlayer(p1.getName());
        gd.getCell(2, 3).setActualLevel(2);
        p1.getDivinity().move(2, 2, 3, 2, gd);
    }

    @Test(expected = DivinityPowerException.class)
    public void move_movementBlovkedByOtherDivinity() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        gd = new GameData();
        gd.addPlayer(p1);
        gd.addPlayer(p2);
        gd.setCurrentPlayer(0);
        gd.getCell(2, 2).setPlayer(p1.getName());
        p1.getDivinity().move(2, 2, 2, 1, gd);
    }

    @Test(expected = NotAdiacentCellException.class)
    public void move_moveOnNotAdjacentCell() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        gd = new GameData();
        gd.addPlayer(p1);
        gd.addPlayer(p2);
        gd.setCurrentPlayer(0);
        gd.getCell(2, 2).setPlayer(p1.getName());
        p1.getDivinity().move(2, 2, 0, 0, gd);
    }

    @Test
    public void move_correctPushing() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        p1.getDivinity().move(2, 2, 1, 1, gd);
        assertNull(gd.getCell(2, 2).getPlayer());
        assertEquals(new Cell(1, 1, 0, p1.getName(), false), gd.getCell(1, 1));
        assertEquals(new Cell(0, 0, 1, p2.getName(), false), gd.getCell(0, 0));
    }

    @Test(expected = OccupiedCellException.class)
    public void move_tryingToPushYourOwnWorker() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdiacentCellException, NotEmptyCellException {
        gd = new GameData();
        gd.addPlayer(p1);
        gd.addPlayer(p2);
        gd.setCurrentPlayer(0);
        gd.getCell(2, 2).setPlayer(p1.getName());
        gd.getCell(3, 3).setPlayer(p1.getName());
        p1.getDivinity().move(2, 2, 3, 3, gd);
    }

    @Test
    public void validCellsWithYourOwnWorker() {
        gd = new GameData();
        gd.addPlayer(p1);
        gd.addPlayer(p2);
        gd.setCurrentPlayer(0);
        gd.getCell(0, 0).setPlayer(p1.getName());
        gd.getCell(1, 0).setPlayer(p1.getName());
        gd.getCell(0, 1).setPlayer(p2.getName());

        ArrayList<Cell> vC = new ArrayList<>();
        vC.add(gd.getCell(0, 1));
        vC.add(gd.getCell(1, 1));

        assertEquals(vC, p1.getDivinity().getValidCellForMove(0, 0, gd.getGameBoard(), new ArrayList<>()));
    }


}