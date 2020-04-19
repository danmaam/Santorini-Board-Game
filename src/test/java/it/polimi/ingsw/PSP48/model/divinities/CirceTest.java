package it.polimi.ingsw.PSP48.model.divinities;

import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.divinities.Apollo;
import it.polimi.ingsw.PSP48.server.model.divinities.Circe;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CirceTest {
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
        player1.setDivinity(new Circe());
        player2.setDivinity(new Apollo());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.addPlayer(player1);
        game_database.addPlayer(player2);
        game_database.setCurrentPlayer(0);
    }

    @Test
    public void turnBegin_otherPlayerOnlyOneWorkerInGame() {
        game_database.getCell(2, 3).setPlayer(player2.getName());
        game_database.getCell(0, 0).setPlayer(player1.getName());
        game_database.getCell(3, 4).setPlayer(player1.getName());

        player1.getDivinity().turnBegin(game_database);

    }

    @Test
    public void turnBegin_otherPlayerTwoWorkersNotAdjacent() {
        game_database.getCell(2, 3).setPlayer(player2.getName());
        game_database.getCell(0, 0).setPlayer(player1.getName());
        game_database.getCell(3, 4).setPlayer(player1.getName());
        game_database.getCell(4, 4).setPlayer(player2.getName());


        player1.getDivinity().turnBegin(game_database);

        assertEquals("Basic", player2.getDivinity().getName());
        assertEquals("Apollo", player1.getDivinity().getName());


    }

    @Test
    public void turnBegin_otherPlayerTwoWorkersAdjacent() {
        game_database.getCell(0, 0).setPlayer(player1.getName());
        game_database.getCell(3, 4).setPlayer(player1.getName());
        game_database.getCell(3, 3).setPlayer(player2.getName());
        game_database.getCell(4, 4).setPlayer(player2.getName());

        player1.getDivinity().turnBegin(game_database);

        assertEquals("Apollo", player2.getDivinity().getName());
        assertEquals("Circe", player1.getDivinity().getName());

    }


}