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

public class ArtemisTest {
    Model game_database = new Model(2, true);
    Player player1 = new Player("Pippo", new GregorianCalendar(1998, Calendar.FEBRUARY, 21), true, Colour.BLUE);
    Player player2 = new Player("Paperino", new GregorianCalendar(2010, Calendar.MARCH, 10), true, Colour.WHITE);

    @Before
    public void testSetUp() {
        player1.setDivinity(new Artemis());
        player2.setDivinity(new Divinity());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.getPlayersInGame().add(player1);
        game_database.getPlayersInGame().add(player2);
        game_database.setNextPlayer(0);
        game_database.getCell(1, 1).setPlayer(player1.getName());

        game_database.getCell(0, 0).addDome();
        game_database.getCell(1, 0).addDome();
        game_database.getCell(2, 0).addDome();
        game_database.getCell(0, 1).addDome();
        game_database.getCell(2, 1).addDome();
        game_database.getCell(2, 2).addDome();

        game_database.getCell(1, 1).setActualLevel(2);
        game_database.getCell(1, 2).setActualLevel(1);
    }

    @Test(expected = DivinityPowerException.class)
    //Tests:
    //1) pre move valid cells
    //2) post move valid cells, doesn't contains the previous cell where he was
    //3) trows an exception trying to move on his previous cell

    public void validCellsTest() throws DomedCellException, OccupiedCellException, DivinityPowerException, IncorrectLevelException, NotAdjacentCellException, NoTurnEndException {
        assertEquals("RequestMove{}", player1.getDivinity().turnBegin(game_database).toString());
        ArrayList<Position> valid = new ArrayList<>();
        valid.add(new Position(0, 2));
        valid.add(new Position(1, 2));
        assertEquals(valid, player1.getDivinity().getValidCellForMove(1, 1, game_database.getGameBoard(), new ArrayList<Divinity>()));

        assertEquals("RequestOptionalMove{}", player1.getDivinity().move(1, 1, 1, 2, game_database).toString());

        valid = new ArrayList<>();
        valid.add(new Position(0, 2));
        valid.add(new Position(2, 3));
        valid.add(new Position(1, 3));
        valid.add(new Position(0, 3));


        assertTrue(valid.containsAll(player1.getDivinity().getValidCellForMove(1, 2, game_database.getGameBoard(), new ArrayList<>())));
        assertEquals(valid.size(), player1.getDivinity().getValidCellForMove(1, 2, game_database.getGameBoard(), new ArrayList<>()).size());

        assertEquals("RequestBuildOrDome{}", player1.getDivinity().move(1, 2, 1, 1, game_database).toString());
    }
}