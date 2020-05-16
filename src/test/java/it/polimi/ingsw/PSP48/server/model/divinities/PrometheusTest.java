package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.controller.GameController;
import it.polimi.ingsw.PSP48.server.model.Colour;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.model.divinities.testingDivinities.DivinityFalsePower;
import org.junit.Before;
import org.junit.Test;

import java.util.function.Consumer;

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
    }

    @Test
    public void turnBegin_cannotPerformOptionalBuild_baseDivinityBehaviour() {
        //Consumer<GameController> f = GameController::CheckIfCanEndTurnBaseDivinity;

        model.getCell(0, 2).addDome();
        //assertTrue(p1.getDivinity().turnBegin(model).equals(f));

    }

    @Test
    public void turnBegin_cannotPerformOptionalDome_baseDivinityBehaviour() {
        model.getCell(0, 1).setActualLevel(2);
        model.getCell(0, 2).setActualLevel(3);
        model.getCell(1, 1).addDome();
        assertTrue(p1.getDivinity().turnBegin(model).equals(true));
    }

    @Test
    public void turnBegin_canPerformOptionalBuild() {
        model.getCell(0, 1).setActualLevel(2);
        model.getCell(0, 2).setActualLevel(1);
        model.getCell(1, 1).addDome();
        assertTrue(p1.getDivinity().turnBegin(model).equals(true));
    }


}