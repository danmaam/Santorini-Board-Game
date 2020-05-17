package it.polimi.ingsw.PSP48.server.model.divinities;

import it.polimi.ingsw.PSP48.server.model.Colour;
import it.polimi.ingsw.PSP48.server.model.Model;
import it.polimi.ingsw.PSP48.server.model.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class CirceTest {
    Model game_database = new Model(2, true);
    Player player1 = new Player("Pippo", new GregorianCalendar(1998, Calendar.FEBRUARY, 21), true, Colour.BLUE);
    Player player2 = new Player("Paperino", new GregorianCalendar(2010, Calendar.MARCH, 10), true, Colour.WHITE);
    
    @Before
    public void testSetUp() {
        player1.setDivinity(new Circe());
        player2.setDivinity(new Apollo());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.getPlayersInGame().add(player1);
        game_database.getPlayersInGame().add(player2);
        game_database.setNextPlayer(0);
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