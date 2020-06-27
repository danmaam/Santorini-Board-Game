package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.divinities.testingDivinities.DivinityFalsePower;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PrometheusTest {

    private Model model;
    private Player p1;
    private Player p2;

    @Before
    public void testSetUp() {
        model = new Model(2, true);

        //initialize players

        p1 = new Player("Sora", null, true, Colour.BLUE);
        p2 = new Player("Riku", null, true, Colour.GRAY);

        model.getPlayersInGame().add(p1);
        model.getPlayersInGame().add(p2);

        model.setNextPlayer("Sora");

        p1.setDivinity(new Prometheus());
        p2.setDivinity(new DivinityFalsePower());

        //initialize game board
        model.getCell(0, 0).addDome();
        model.getCell(1, 0).addDome();
        model.getCell(2, 0).addDome();


        model.getCell(0, 1).setPlayer(p1.getName());
        model.getCell(2, 1).setPlayer(p2.getName());

        model.getCell(1, 1).setActualLevel(1);
        model.getCell(0,1 ).setActualLevel(0);
    }

    @Test
    public void turnBegin_cannotPerformOptionalBuild_baseDivinityBehaviour() {
        model.getCell(0, 2).addDome();
        model.getCell(1, 2).addDome();
        model.getCell(1, 1).addDome();
        ArrayList<Divinity> div = new ArrayList<>();
        div.add(p2.getDivinity());
        p1.getDivinity().turnBegin(model);
        assertTrue(p1.getDivinity().getValidCellForBuilding(0, 1, div, model.getGameBoard()).isEmpty());

    }

    @Test
    public void turnBegin_cannotPerformOptionalDome_baseDivinityBehaviour() {
        ArrayList<Divinity> div = new ArrayList<>();
        div.add(p2.getDivinity());
        p1.getDivinity().turnBegin(model);
        assertTrue(p1.getDivinity().getValidCellsToPutDome(0, 1, model.getGameBoard(), div).isEmpty());
    }

    @Test
    public void turnBegin_canPerformOptionalBuild() {
        model.getCell(1, 1).addDome();
        model.getCell(1, 2).addDome();
        ArrayList<Divinity> div = new ArrayList<>();
        div.add(p2.getDivinity());
        ArrayList<Position> valid = new ArrayList<>();
        p1.getDivinity().turnBegin(model);
        assertEquals(p1.getDivinity().getValidCellForBuilding(0, 1, div, model.getGameBoard()), valid);
    }

    @Test
    public void turnBegin_canPerformOptionalDome() {
        model.getCell(0, 2).setActualLevel(3);
        model.getCell(1, 2).addDome();
        model.getCell(1, 1).addDome();
        ArrayList<Divinity> div = new ArrayList<>();
        div.add(p2.getDivinity());
        ArrayList<Position> valid = new ArrayList<>();
        p1.getDivinity().turnBegin(model);
        assertEquals(p1.getDivinity().getValidCellsToPutDome(0, 1, model.getGameBoard(), div), valid);
    }

    @Test
    public void moveNotGrowingUpWithoutPreviousBuildingOrDome_baseDivinityBehaviour() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException {
        p1.getDivinity().turnBegin(model);
        model.getCell(1, 2).addDome();
        model.getCell(1, 1).addDome();
        model.getCell(0, 2).setActualLevel(0);
        p1.getDivinity().move(0, 1, 0, 2, model);
        assertEquals(model.getCell(0, 1).getLevel(), model.getCell(0, 2).getLevel());
        assertTrue((model.getCell(0, 1).getPlayer() == null) && (model.getCell(0, 2).getPlayer().equals(p1.getName())));
    }

    @Test
    public void moveGrowingUp_WithoutPreviousBuildingOrDome_baseDivinityBehaviour() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException {
        p1.getDivinity().turnBegin(model);
        model.getCell(1, 2).addDome();
        model.getCell(1, 1).addDome();
        model.getCell(0, 2).setActualLevel(1);
        p1.getDivinity().move(0, 1, 0, 2, model);
        assertTrue(model.getCell(0, 1).getLevel() < model.getCell(0, 2).getLevel());
        assertTrue((model.getCell(0, 1).getPlayer() == null) && (model.getCell(0, 2).getPlayer().equals(p1.getName())));
    }

    @Test(expected = DivinityPowerException.class)
    public void moveGrowingUpWithPreviousBuilding() throws MaximumLevelReachedException, OccupiedCellException, NotAdjacentCellException, DomedCellException, DivinityPowerException, NoTurnEndException, IncorrectLevelException {
        model.getCell(0, 2).setActualLevel(0);
        p1.getDivinity().turnBegin(model);
        p1.getDivinity().build(0, 1, 0, 2, model);
        p1.getDivinity().move(0, 1, 0, 2, model);
    }

    @Test
    public void moveNotGrowingUpWithPreviousBuilding() throws MaximumLevelReachedException, OccupiedCellException, NotAdjacentCellException, DomedCellException, DivinityPowerException, NoTurnEndException, IncorrectLevelException {
        model.getCell(0, 2).setActualLevel(0);
        model.getCell(0,1).setActualLevel(1);
        model.getCell(1, 1).setActualLevel(1);
        p1.getDivinity().turnBegin(model);
        p1.getDivinity().build(0, 1, 0, 2, model);
        p1.getDivinity().move(0, 1, 0, 2, model);
        assertEquals(model.getCell(0, 1).getLevel(), model.getCell(0, 2).getLevel());
        assertTrue((model.getCell(0, 1).getPlayer() == null) && (model.getCell(0, 2).getPlayer().equals(p1.getName())));
    }

    @Test(expected = DivinityPowerException.class)
    public void moveGrowingUpWithPreviousDome() throws OccupiedCellException, NotAdjacentCellException, DomedCellException, DivinityPowerException, NoTurnEndException, IncorrectLevelException, MaximumLevelNotReachedException {
        model.getCell(0, 2).setActualLevel(1);
        model.getCell(1, 1).setActualLevel(3);
        p1.getDivinity().turnBegin(model);
        p1.getDivinity().dome(0, 1, 1, 1, model);
        p1.getDivinity().move(0, 1, 0, 2, model);
    }

    @Test
    public void moveNotGrowingUpWithPreviousDome() throws OccupiedCellException, NotAdjacentCellException, DomedCellException, DivinityPowerException, NoTurnEndException, IncorrectLevelException, MaximumLevelNotReachedException {
        model.getCell(0, 2).setActualLevel(0);
        model.getCell(1, 1).setActualLevel(3);
        p1.getDivinity().turnBegin(model);
        p1.getDivinity().dome(0, 1, 1, 1, model);
        p1.getDivinity().move(0, 1, 0, 2, model);
        assertEquals(model.getCell(0, 1).getLevel(), model.getCell(0, 2).getLevel());
        assertTrue((model.getCell(0, 1).getPlayer() == null) && (model.getCell(0, 2).getPlayer().equals(p1.getName())));
    }

    @Test
    public void validCellsOptionalBuild() {
        model.getCell(0, 2).setActualLevel(0);
        model.getCell(1, 2).setActualLevel(2);
        model.getCell(2, 2).setActualLevel(3);
        model.getCell(0, 1).setActualLevel(0);

        ArrayList<Position> v = new ArrayList<>();
        v.add(new Position(1, 2));
        v.add(new Position(1, 2));

        ArrayList<Position> valid = p1.getDivinity().getValidCellForBuilding(0, 1, new ArrayList(), model.getGameBoard());

        assertTrue(valid.containsAll(v));
        assertEquals(valid.size(), v.size());
    }

    @Test
    public void doubleBuild() throws MaximumLevelReachedException, OccupiedCellException, NotAdjacentCellException, DomedCellException, DivinityPowerException, NoTurnEndException, IncorrectLevelException {
        p2.setDivinity(new Divinity());
        model.getCurrentPlayer().getDivinity().build(0, 1, 0, 2, model);
        model.getCurrentPlayer().getDivinity().move(0, 1, 1, 2, model);
        model.getCurrentPlayer().getDivinity().build(1, 2, 1, 3, model);
        assertEquals(model.getCell(0, 2).getLevel(), 1);
        assertEquals(model.getCell(1, 3).getLevel(), 1);
        assertNull(model.getCell(0, 1).getPlayer());
        assertEquals(model.getCell(1, 2).getPlayer(), "Sora");
    }

    @Test
    public void doubleBuildAndDome() throws MaximumLevelReachedException, OccupiedCellException, NotAdjacentCellException, DomedCellException, DivinityPowerException, NoTurnEndException, IncorrectLevelException, MaximumLevelNotReachedException {
        p2.setDivinity(new Divinity());
        model.getCell(1, 3).setActualLevel(3);
        model.getCurrentPlayer().getDivinity().build(0, 1, 0, 2, model);
        model.getCurrentPlayer().getDivinity().move(0, 1, 1, 2, model);
        model.getCurrentPlayer().getDivinity().dome(1, 2, 1, 3, model);
        assertEquals(model.getCell(0, 2).getLevel(), 1);
        assertTrue(model.getCell(1, 3).isDomed());
        assertNull(model.getCell(0, 1).getPlayer());
        assertEquals(model.getCell(1, 2).getPlayer(), "Sora");
    }


}