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

public class DemeterTest {
    Model game_database = new Model(2, true);
    Player player1 = new Player("Pippo", new GregorianCalendar(1998, Calendar.FEBRUARY, 21), true, Colour.BLUE);
    Player player2 = new Player("Paperino", new GregorianCalendar(2010, Calendar.MARCH, 10), true, Colour.WHITE);
    
    @Before
    public void testSetUp() {
        player1.setDivinity(new Demeter());
        player2.setDivinity(new Divinity());
        player1.setTempDivinity(null);
        player2.setTempDivinity(null);
        game_database.getPlayersInGame().add(player1);
        game_database.getPlayersInGame().add(player2);
        game_database.setNextPlayer(0);
        game_database.getCell(0, 0).setPlayer(player1.getName());

        game_database.getCell(0, 1).setActualLevel(2);
        game_database.getCell(1, 0).setActualLevel(1);
        game_database.getCell(1, 1).setActualLevel(3);
    }

    @Test
    public void domeBuildValidCellTestAfterBuilding() throws MaximumLevelReachedException, DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException {
        ArrayList<Position> validForBuild = new ArrayList<>();
        ArrayList<Position> validForDoming = new ArrayList<>();

        validForBuild.add(new Position(0, 1));
        validForBuild.add(new Position(1, 0));

        validForDoming.add(new Position(1, 1));

        player1.getDivinity().turnBegin(game_database);
        player1.getDivinity().turnBegin(game_database);
        assertEquals(validForDoming, player1.getDivinity().getValidCellsToPutDome(0, 0, game_database.getGameBoard(), new ArrayList<Divinity>()));
        assertEquals(validForBuild, player1.getDivinity().getValidCellForBuilding(0, 0, new ArrayList<Divinity>(), game_database.getGameBoard()));

        player1.getDivinity().build(0, 0, 1, 0, game_database);

        validForBuild.removeIf(x->(x.getRow()==1 && x.getColumn()==0));

        assertEquals(validForDoming, player1.getDivinity().getValidCellsToPutDome(0, 0, game_database.getGameBoard(), new ArrayList<Divinity>()));
        assertEquals(validForBuild, player1.getDivinity().getValidCellForBuilding(0, 0, new ArrayList<Divinity>(), game_database.getGameBoard()));
    }

    @Test
    public void domeBuildValidCellTestAfterDome() throws DivinityPowerException, OccupiedCellException, NotAdjacentCellException, DomedCellException, MaximumLevelNotReachedException {
        ArrayList<Position> validForBuild = new ArrayList<>();
        ArrayList<Position> validForDoming = new ArrayList<>();

        validForBuild.add(new Position(0, 1));
        validForBuild.add(new Position(1, 0));

        validForDoming.add(new Position(1, 1));
        
        player1.getDivinity().turnBegin(game_database);
        player1.getDivinity().turnBegin(game_database);
        assertEquals(validForDoming, player1.getDivinity().getValidCellsToPutDome(0, 0, game_database.getGameBoard(), new ArrayList<Divinity>()));
        assertEquals(validForBuild, player1.getDivinity().getValidCellForBuilding(0, 0, new ArrayList<Divinity>(), game_database.getGameBoard()));

        player1.getDivinity().dome(0, 0, 1, 1, game_database);

        validForDoming.removeIf(x->(x.getRow()==1 && x.getColumn()==1));

        assertEquals(validForDoming, player1.getDivinity().getValidCellsToPutDome(0, 0, game_database.getGameBoard(), new ArrayList<Divinity>()));
        assertEquals(validForBuild, player1.getDivinity().getValidCellForBuilding(0, 0, new ArrayList<Divinity>(), game_database.getGameBoard()));
    }

    @Test(expected = DivinityPowerException.class)
    public void reBuildOnTheSameCell_throwException() throws DivinityPowerException, OccupiedCellException,NotAdjacentCellException, DomedCellException, MaximumLevelReachedException {

        player1.getDivinity().build(0, 0, 1, 0, game_database);
        player1.getDivinity().build(0, 0, 1, 0, game_database);
    }

    @Test(expected = DivinityPowerException.class)
    public void reDomeOnTheSameCell_throwException() throws DivinityPowerException, OccupiedCellException, MaximumLevelNotReachedException, NotAdjacentCellException, DomedCellException {

        player1.getDivinity().dome(0, 0, 1, 1, game_database);
        player1.getDivinity().dome(0, 0, 1, 1, game_database);
    }


}