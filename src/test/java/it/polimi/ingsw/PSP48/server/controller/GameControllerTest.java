package it.polimi.ingsw.PSP48.server.controller;

import it.polimi.ingsw.PSP48.client.CLI.CLI;
import it.polimi.ingsw.PSP48.server.model.ActionCoordinates;
import it.polimi.ingsw.PSP48.server.model.Colour;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Player;
import it.polimi.ingsw.PSP48.server.model.divinities.Apollo;
import it.polimi.ingsw.PSP48.server.model.divinities.Artemis;
import it.polimi.ingsw.PSP48.server.model.divinities.Zeus;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import it.polimi.ingsw.PSP48.server.virtualview.VirtualView;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameControllerTest {
    Model gameModel = new Model(3, true);
    GameController gameController = new GameController(gameModel, 1);

    @Before
    public void testSetUp() {

        gameController.associateViewWithPlayer("Sora", new FakeViewForTesting());
        gameController.associateViewWithPlayer("Riku", new FakeViewForTesting());
        gameController.associateViewWithPlayer("Kairi", new FakeViewForTesting());

        gameModel.addPlayer("Sora", Colour.GRAY, null);
        gameModel.addPlayer("Riku", Colour.BLUE, null);
        gameModel.addPlayer("Kairi", Colour.WHITE, null);


        gameModel.setPlayerDivinity("Sora", "Atlas");
        gameModel.setPlayerDivinity("Riku", "Athena");
        gameModel.setPlayerDivinity("Kairi", "Zeus");

        gameModel.setFirstPlayerIndex(0);

    }

    @Test
    public void secondPlayerLoses() {
        gameModel.setNextPlayer("Sora");
        gameModel.getCell(3, 2).setPlayer("Kairi");
        gameModel.getCell(4, 2).setPlayer("Kairi");
        gameModel.getCell(3, 3).setPlayer("Sora");
        gameModel.getCell(3, 4).setPlayer("Sora");
        gameModel.getCell(4, 3).setPlayer("Riku");
        gameModel.getCell(4, 4).setPlayer("Riku");

        gameController.turnChange();

        assertEquals(2, gameModel.getGamePlayerNumber());
        assertEquals("Kairi", gameModel.getCurrentPlayer().getName());
        gameModel.getPlayersInGame().forEach(x -> System.out.println(x.getName()));
    }

    @Test
    public void thirdPlayerLoses() {
        gameModel.setNextPlayer("Riku");
        gameModel.getCell(3, 2).setPlayer("Riku");
        gameModel.getCell(4, 2).setPlayer("Riku");
        gameModel.getCell(3, 3).setPlayer("Sora");
        gameModel.getCell(3, 4).setPlayer("Sora");
        gameModel.getCell(4, 3).setPlayer("Kairi");
        gameModel.getCell(4, 4).setPlayer("Kairi");

        gameController.turnChange();

        assertEquals(2, gameModel.getGamePlayerNumber());
        assertEquals("Sora", gameModel.getCurrentPlayer().getName());
        gameModel.getPlayersInGame().forEach(x -> System.out.println(x.getName()));

    }

    @Test
    public void firstPlayerLoses() {
        gameModel.setNextPlayer("Kairi");
        gameModel.getCell(3, 2).setPlayer("Riku");
        gameModel.getCell(4, 2).setPlayer("Riku");
        gameModel.getCell(3, 3).setPlayer("Kairi");
        gameModel.getCell(3, 4).setPlayer("Kairi");
        gameModel.getCell(4, 3).setPlayer("Sora");
        gameModel.getCell(4, 4).setPlayer("Sora");

        gameController.turnChange();

        assertEquals(2, gameModel.getGamePlayerNumber());
        assertEquals("Riku", gameModel.getCurrentPlayer().getName());
        gameModel.getPlayersInGame().forEach(x -> System.out.println(x.getName()));
    }

    @Test
    public void onePlayerLosesTwoPlayersGame() {
        gameModel = new Model(2, true);
        gameController = new GameController(gameModel, -1);

        gameController.associateViewWithPlayer("Sora", new FakeViewForTesting());
        gameController.associateViewWithPlayer("Riku", new FakeViewForTesting());

        gameModel.addPlayer("Sora", Colour.GRAY, null);
        gameModel.addPlayer("Riku", Colour.BLUE, null);


        gameModel.setPlayerDivinity("Sora", "Atlas");
        gameModel.setPlayerDivinity("Riku", "Athena");

        gameModel.setFirstPlayerIndex(0);

        gameModel.setNextPlayer("Sora");

        gameModel.getCell(3, 2).addDome();
        gameModel.getCell(4, 2).addDome();
        gameModel.getCell(3, 3).setPlayer("Sora");
        gameModel.getCell(3, 4).setPlayer("Sora");
        gameModel.getCell(4, 3).setPlayer("Riku");
        gameModel.getCell(4, 4).setPlayer("Riku");

        gameController.turnChange();
        assertEquals("EndGame{}", gameController.nextState().toString());
    }

    @Test
    public void ChronusWinsForBuildings() {
        gameModel = new Model(2, true);
        gameController = new GameController(gameModel, -1);

        gameController.associateViewWithPlayer("Sora", new FakeViewForTesting());
        gameController.associateViewWithPlayer("Riku", new FakeViewForTesting());

        gameModel.addPlayer("Sora", Colour.GRAY, null);
        gameModel.addPlayer("Riku", Colour.BLUE, null);


        gameModel.setPlayerDivinity("Sora", "Atlas");
        gameModel.setPlayerDivinity("Riku", "Chronus");

        gameModel.setFirstPlayerIndex(0);

        gameModel.setNextPlayer("Riku");

        gameModel.getCell(3, 2).addDome();
        gameModel.getCell(4, 2).addDome();
        gameModel.getCell(4, 3).setPlayer("Sora");
        gameModel.getCell(3, 3).setPlayer("Sora");
        gameModel.getCell(0, 3).setPlayer("Riku");
        gameModel.getCell(0, 4).setPlayer("Riku");

        gameController.turnChange();

        gameModel.getCell(0, 0).addDome();
        gameModel.getCell(0, 4).addDome();
        gameModel.getCell(2, 1).addDome();
        gameModel.getCell(3, 2).addDome();

        gameModel.getCell(0, 0).setActualLevel(3);
        gameModel.getCell(0, 4).setActualLevel(3);
        gameModel.getCell(2, 1).setActualLevel(3);
        gameModel.getCell(3, 2).setActualLevel(3);

        gameModel.getCell(4, 4).setActualLevel(3);
        gameController.dome(new ActionCoordinates(4, 3, 4, 4));
        assertEquals("EndGame{}", gameController.nextState().toString());
    }

    @Test
    public void optionalMoveNotAllowed() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException {
        gameModel.getCell(0, 0).setPlayer("Sora");
        gameModel.getCell(0, 1).addDome();
        gameModel.getCell(1, 0).addDome();
        gameModel.getCell(1, 1).addDome();

        gameModel.getCell(2, 3).setPlayer("Sora");
        gameModel.getCell(3, 3).setActualLevel(2);
        gameModel.getCell(2, 3).addDome();

        gameModel.getCell(4, 3).addDome();
        gameModel.getCell(3, 4).addDome();

        gameModel.getCell(3, 2).addDome();
        gameModel.getCell(4, 2).addDome();
        gameModel.getCell(2, 2).addDome();
        gameModel.getCell(2, 4).addDome();

        gameModel.setNextPlayer("Sora");
        gameModel.setPlayerDivinity("Sora", "Artemis");
        gameModel.getCurrentPlayer().getDivinity().move(3, 3, 4, 4, gameModel);
        gameController.requestOptionalMove();
        assertEquals("RequestBuildDome{}", gameController.nextState().toString());
    }
}