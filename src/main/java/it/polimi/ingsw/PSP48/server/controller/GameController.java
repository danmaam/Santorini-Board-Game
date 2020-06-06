package it.polimi.ingsw.PSP48.server.controller;

import it.polimi.ingsw.PSP48.EndReason;
import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.server.Server;
import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import it.polimi.ingsw.PSP48.observers.ViewObserver;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author Daniele Mammone
 * the entire game controller in his magnificence
 * it's an observer f the view, invokes methods on view and model
 */

public class GameController implements ViewObserver {
    private final Model model;
    private Consumer<GameController> nextAction;
    private final int roomID;

    private final HashMap<String, ViewInterface> playersViews = new HashMap<>();

    public void associateViewWithPlayer(String name, ViewInterface view) {
        playersViews.put(name, view);
    }

    public GameController(Model m, int ID) {
        model = m;
        roomID = ID;
    }

    public ViewInterface getPlayerView(String playerName) {
        return playersViews.get(playerName);
    }

    /**
     * Adds a player in the game when a client connects to the server; if the game's player number is reached, the controller starts the game.
     *
     * @author Daniele Mammone
     */
    @Override
    public void addPlayer(String name, Calendar birthday) {
        System.out.println("adding player");
        model.addPlayer(name, model.getNextColour(), birthday);

        if (model.getPlayersInGame().size() == model.getGamePlayerNumber()) {
            if (model.isGameWithDivinities()) this.startGameWithDivinities();
            else this.startGameWithoutDivinities();
        }
    }

    /**
     * Perform the move requested from the player.
     *
     * @param p the coordinates of the move, including which worker the player wants to move, and the cell where the player wants to move the worker
     * @author Daniele Mammone
     */
    @Override
    public void move(MoveCoordinates p) {
        System.out.println("doing move");
        //i must check if this move allows the player to end the turn
        try {
            nextAction = model.getCurrentPlayer().getDivinity().move(p.getWorkerColumn(), p.getWorkerRow(), p.getMoveColumn(), p.getMoveRow(), model);
        } catch (IncorrectLevelException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Trying to go on a too high level. Retry the move.");
            nextAction();
            return;
        } catch (DivinityPowerException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Trying to go on a cell blocked by other divinity power");
            nextAction();
            return;
        } catch (NotAdjacentCellException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Trying to move on a cell that is not adjacent");
            nextAction();
            return;
        } catch (DomedCellException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Trying to go on a cell that is domed");
            nextAction();
            return;
        } catch (OccupiedCellException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Trying to go on a cell occupied by other worker. Retry.");
            nextAction();
            return;
        } catch (NoTurnEndException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("With this move, you can't end the turn. Retry.");
            nextAction();
            return;
        }
        this.postMove();
    }

    /**
     * Perform the build requested from the player.
     *
     * @param p the coordinates of the build, including the used worker (that's the one that the player moved), and the cell where the player wants to move the worker
     * @author Daniele Mammone
     */
    @Override
    public void build(MoveCoordinates p) {
        System.out.println("build");
        try {
            nextAction = model.getCurrentPlayer().getDivinity().build(p.getWorkerRow(), p.getWorkerColumn(), p.getMoveRow(), p.getMoveColumn(), model);
        } catch (NotAdjacentCellException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Trying to build on a cell not adjacent");
            nextAction();
            return;
        } catch (DivinityPowerException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Trying to build on a cell blocked by a divinity power");
            nextAction();
            return;
        } catch (DomedCellException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Trying to build on a domed cell");
            nextAction();
            return;
        } catch (OccupiedCellException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Trying to build on a cell occupied by another worker");
            nextAction();
            return;
        } catch (MaximumLevelReachedException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Trying to go over level 3");
            nextAction();
            return;
        }
        this.postBuild();
    }

    /**
     * Perform the build requested from the player.
     *
     * @param p the coordinates of the doming, including the used worker (that's the one that the player moved), and the cell where the player wants to put the dome
     * @author Daniele Mammone
     */
    @Override
    public void dome(MoveCoordinates p) {
        System.out.println("dome");
        try {
            nextAction = model.getCurrentPlayer().getDivinity().dome(p.getWorkerRow(), p.getWorkerColumn(), p.getMoveRow(), p.getMoveColumn(), model);
        } catch (NotAdjacentCellException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Trying to build on a cell not adjacent");
            nextAction();
            return;
        } catch (DivinityPowerException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Trying to build on a cell blocked by a divinity power");
            nextAction();
            return;
        } catch (DomedCellException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Trying to build on a domed cell");
            nextAction();
            return;
        } catch (MaximumLevelNotReachedException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Trying to build on a cell that's already at the maximum level");
            nextAction();
            return;
        } catch (OccupiedCellException e) {
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Trying to build on a cell occupied by another worker");
            nextAction();
            return;
        }
        this.postBuild();
    }

    /**
     * Put a player's worker on the board
     *
     * @param p the cell where the player wants to put the worker.
     */
    @Override
    public void putWorkerOnTable(Position p) {
        System.out.println("positioning worker");
        try {
            model.getCurrentPlayer().getDivinity().putWorkerOnBoard(p, model).accept(this);
        } catch (OccupiedCellException e) {
            System.out.println("Positioning on occupied cell");
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Can't put worker here, occupied cell. Retry operation");
            this.requestInitialPositioning();
        } catch (DivinityPowerException e) {
            System.out.println("Positioning on a wrong cell due to divinity power");
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Can't put worker here, cell not available due to divinity power. Retry operation");
            this.requestInitialPositioning();
        }
    }


    /**
     * @param divinity the name of the divinity chosen by the current player
     * @author Daniele Mammone
     * method to associate current player with his chosen divinity
     */
    //@ requires (\exists int x; 0 <= x && x<= model.getAvailableDivinities().size(); model.getAvailableDivinities().get(x).getName().equals(divinity))
    @Override
    public void registerPlayerDivinity(String divinity) {
        System.out.println("associating player divinity");
        model.setPlayerDivinity(model.getCurrentPlayer().getName(), divinity);
        //if the current player is the chosen, all divinities has benn selected, and the game must start
        if (model.getPlayersInGame().indexOf(model.getCurrentPlayer()) == model.getChallengerIndex())
            nextAction = GameController::requestFirstPlayerSelection;
        else {
            model.setNextPlayer();
            nextAction = GameController::requestDivinitySelection;
        }
        this.nextAction();
    }


    /**
     * @param divinities the divinities chosen by the challenger
     * @author Daniele Mammone
     * method invoked when the challenger has chosen his divinities
     */
    @Override
    public void selectAvailableDivinities(ArrayList<String> divinities) {
        System.out.println("arrived divinities from challenger");
        model.challengerDivinityChoice(divinities);
        model.setNextPlayer();
        this.requestDivinitySelection();
    }

    /**
     * method that sets the first player and starts the game
     *
     * @param playerName the name of the player
     */
    @Override
    public void selectFirstPlayer(String playerName) {
        System.out.println("setting first player");
        model.setNextPlayer(playerName);
        model.setFirstPlayerIndex(model.getPlayersInGame().indexOf(model.getPlayer(playerName)));
        this.requestInitialPositioning();
    }


    /**
     * Request to the current player to perform a build
     *
     * @author Daniele Mammone
     */
    public void requestBuildDome() {
        System.out.println("requesting build or dome");
        ArrayList<Divinity> otherDivinities = new ArrayList<>();
        for (Player p : model.getPlayersInGame()) {
            if (!p.getName().equals(model.getCurrentPlayer().getName())) otherDivinities.add(p.getDivinity());
        }
        Position lW = model.getCurrentPlayer().getLastWorkerMoved();
        ArrayList<WorkerValidCells> build = new ArrayList<>();
        ArrayList<WorkerValidCells> dome = new ArrayList<>();
        build.add(new WorkerValidCells(model.getCurrentPlayer().getDivinity().getValidCellForBuilding(lW.getColumn(), lW.getRow(), otherDivinities, model.getGameBoard()), lW.getRow(), lW.getColumn()));
        dome.add(new WorkerValidCells(model.getCurrentPlayer().getDivinity().getValidCellsToPutDome(lW.getColumn(), lW.getRow(), model.getGameBoard(), otherDivinities), lW.getRow(), lW.getColumn()));
        getPlayerView(model.getCurrentPlayer().getName()).requestDomeOrBuild(build, dome);
    }

    /**
     * @author Daniele Mammone
     * Obtains valid cells for worker's moving, and requires the player to move his player
     */
    public void requestMove() {
        System.out.println("requesting build or move");
        ArrayList<WorkerValidCells> validCells = new ArrayList<>();
        ArrayList<Position> workersPosition = model.getPlayerPositionsInMap(model.getCurrentPlayer().getName());
        ArrayList<Divinity> otherDivinities = new ArrayList<>();
        for (Player p : model.getPlayersInGame()) {
            if (!p.getName().equals(model.getCurrentPlayer().getName())) otherDivinities.add(p.getDivinity());
        }
        for (Position p : workersPosition) {
            validCells.add(new WorkerValidCells(new ArrayList<>(model.getCurrentPlayer().getDivinity().getValidCellForMove(p.getColumn(), p.getRow(), model.getGameBoard(), otherDivinities)), p.getRow(), p.getColumn()));
        }
        validCells.removeIf(x-> x.getValidPositions().size()==0); //removes elements that have no valid positions
        getPlayerView(model.getCurrentPlayer().getName()).requestMove(validCells);
    }

    /**
     * Perform controller's next action.
     */
    public void nextAction() {
        nextAction.accept(this);
    }

    /**
     * Checks if at the end of a move the current player won the game. If yes, put the controller in endGame state; else, changes the turn.
     *
     * @author Daniele Mammone
     */
    public void postMove() {
        boolean endGame = model.getCurrentPlayer().getDivinity().winCondition(model);
        if (endGame) {
            nextAction = (GameController::gameEnd);
        }
        this.nextAction();
    }

    public void gameEnd() {
        Server.destroyGameRoom(roomID, model.getCurrentPlayer().getName(), EndReason.win);
    }


    /**
     * @author Rebecca Marelli
     */
    public void postBuild() {
        boolean win = false;
        String playerWithChronus = null;
        for (Player p : model.getPlayersInGame()) {
            if (p.getDivinity().getName().equals("Chronus")) {
                win = p.getDivinity().winCondition(model);
                playerWithChronus = p.getName();
                break;
            }
        }

        if (win && playerWithChronus != null) {
            Server.destroyGameRoom(roomID, playerWithChronus, EndReason.win);
        } else this.nextAction();
    }

    /**
     * @author Rebecca Marelli
     * method that handles the activities related to the end of the turn
     */
    public void turnEnd() {
        nextAction = model.getCurrentPlayer().getDivinity().turnEnd();
        nextAction();
    }

    public void turnChange() {
        model.setNextPlayer();
        if (model.getPlayerWithCirce() != null && model.getCurrentPlayer().getName().equals(model.getPlayerWithCirce()) && model.getCurrentPlayer().getTempDivinity() != null)
            nextAction = model.getCurrentPlayer().getTempDivinity().turnBegin(model);
        else nextAction = model.getCurrentPlayer().getDivinity().turnBegin(model);
        this.nextAction();
    }


    /**
     * @author Daniele Mammone
     * Starts the game, so, randomically, chose the "chosen", sets the chosen as last player to chose divinities, and asks the chosen to select a set of divinities
     */
    public void startGameWithDivinities() {
        System.out.println("starting game");
        //i must choose randomically the challenger, than request him to choose divinities
        int i = new Random().nextInt(model.getNumberOfPlayers());
        model.setChallengerIndex(i);
        model.setNextPlayer(i);
        //set the challenger, i must ask him to select divinities
        this.requestChallengerDivinitiesSelection();
    }

    /**
     * @author Daniele Mammone
     * starts the game without divinites, so set the first player the younger player, and then requests it's first move
     */
    public void startGameWithoutDivinities() {
        for (Player p : model.getPlayersInGame()) {
            getPlayerView(p.getName()).printMessage("Game started. Waiting for your turn to put your workers on the board");
        }

        Player firstPlayer = model.getPlayersInGame().get(0);
        for (Player p : model.getPlayersInGame()) {
            if (p.getBirthday().compareTo(firstPlayer.getBirthday()) < 0) firstPlayer = p;
        }
        //found the younger player, I need to set him as the first player
        model.setNextPlayer(model.getPlayersInGame().indexOf(firstPlayer));
        model.setFirstPlayerIndex(model.getPlayersInGame().indexOf(firstPlayer));

        //set the basic moves on players, i start the game requesting the initial positioning
        this.requestInitialPositioning();
    }

    /**
     * @author Daniele Mammone
     * Request the current player to choose his divinity
     */
    public void requestDivinitySelection() {
        for (Player p : model.getPlayersInGame()) {
            if (p != model.getCurrentPlayer())
                getPlayerView(p.getName()).printMessage(model.getCurrentPlayer().getName() + " is choosing his divinity!");
        }
        getPlayerView(model.getCurrentPlayer().getName()).requestDivinitySelection(model.getAvailableDivinities());
    }

    public void requestFirstPlayerSelection() {
        for (Player p : model.getPlayersInGame()) {
            if (model.getPlayersInGame().indexOf(p) != model.getChallengerIndex())
                getPlayerView(p.getName()).printMessage("Challenger is choosing the first player!");
        }
        ArrayList<String> players = new ArrayList<>();
        model.getPlayersInGame().forEach((Player p) -> players.add(p.getName()));
        getPlayerView(model.getPlayersInGame().get(model.getChallengerIndex()).getName()).requestInitialPlayerSelection(players);
    }

    public void requestInitialPositioning() {
        for (Player p : model.getPlayersInGame()) {
            if (p != model.getCurrentPlayer())
                getPlayerView(p.getName()).printMessage(model.getCurrentPlayer().getName() + " is putting his workers on the board!");
        }
        System.out.println("requesting initial positioning");
        getPlayerView(model.getCurrentPlayer().getName()).requestInitialPositioning(model.getCurrentPlayer().getDivinity().validCellsForInitialPositioning(model.getGameBoard()));
    }

    /**
     * @author Daniele Mammone
     * If all players put their workers on the board, starts the game requesting the first player to do something depending on
     * his divinity; else, changes the current player and asks the next to perform initial positioning
     */
    public void initialPositioningTurnChange() {

        model.setNextPlayer();
        //Must check if all the players completed the initial positioning
        if (model.getPlayersInGame().indexOf(model.getCurrentPlayer()) == model.getFirstPlayerIndex()) {
            //it means all the workers are on the board, so I must request the turn begin to the first player
            System.out.println("starting game");
            nextAction = model.getCurrentPlayer().getDivinity().turnBegin(model);
        } else {
            System.out.println("changing initial positioning turn");
            nextAction = GameController::requestInitialPositioning;
        }
        nextAction();
    }

    /**
     * Request the challenger to choose divinities
     *
     * @author Daniele Mammone
     */
    public void requestChallengerDivinitiesSelection() {
        //game started: I advice other players
        for (Player p : model.getPlayersInGame()) {
            if (model.getPlayersInGame().indexOf(p) != model.getChallengerIndex())
                getPlayerView(p.getName()).printMessage("Game strated! Challenger is choosing divinities!");
        }
        //in the model there is a list of available divinities, I send this to the challenger, so he can choose from them
        getPlayerView(model.getPlayersInGame().get(model.getChallengerIndex()).getName()).requestChallengerDivinitiesSelection(model.getAvailableDivinities(), model.getNumberOfPlayers());
    }


    /**
     * @author Daniele Mammone
     * Checks if the optional building is possible. If yes, asks for the player to perform (or skip) the optional building; else, requests the next thing to do
     */
    public void requestOptionalBuilding() {
        Position lastWorker = model.getCurrentPlayer().getLastWorkerMoved();
        ArrayList<Divinity> otherDivinities = new ArrayList<>();
        for (Player p : model.getPlayersInGame()) {
            if (!p.getName().equals(model.getCurrentPlayer().getName())) otherDivinities.add(p.getDivinity());
        }
        ArrayList<Position> validForBuilding = model.getCurrentPlayer().getDivinity().getValidCellForBuilding(lastWorker.getColumn(), lastWorker.getRow(), otherDivinities, model.getClonedGameBoard());
        ArrayList<Position> validForDoming = model.getCurrentPlayer().getDivinity().getValidCellsToPutDome(lastWorker.getColumn(), lastWorker.getRow(), model.getClonedGameBoard(), otherDivinities);
        if (validForBuilding.isEmpty() && validForDoming.isEmpty()) {
            try {
                nextAction = model.getCurrentPlayer().getDivinity().build(-1, -1, -1, -1, null);
                nextAction();
            } catch (Exception e) {
                System.out.println("Fatal error");
            }
        } else {
            ArrayList<WorkerValidCells> build = new ArrayList<>();
            ArrayList<WorkerValidCells> dome = new ArrayList<>();
            build.add(new WorkerValidCells(validForBuilding, model.getCurrentPlayer().getLastWorkerMoved().getRow(), model.getCurrentPlayer().getLastWorkerMoved().getColumn()));
            dome.add(new WorkerValidCells(validForDoming, model.getCurrentPlayer().getLastWorkerMoved().getRow(), model.getCurrentPlayer().getLastWorkerMoved().getColumn()));

            getPlayerView(model.getCurrentPlayer().getName()).requestOptionalBuild(build, dome);
        }
    }

    //SPECIFIC DIVINITY BEHAVIOUR

    /**
     * @author Daniele Mammone
     * request the player to perform an optional move, but first checks if it's possible
     */
    public void requestOptionalMove() {
        //first, I must check if the optional move is possible
        //the player must use the already moved worker
        ArrayList<Divinity> otherDivinities = new ArrayList<>();
        for (Player p : model.getPlayersInGame()) {
            if (!p.getName().equals(model.getCurrentPlayer().getName())) otherDivinities.add(p.getDivinity());
        }
        ArrayList<Position> validPositionsForMove = model.getCurrentPlayer().getDivinity().getValidCellForMove(model.getCurrentPlayer().getLastWorkerMoved().getColumn(), model.getCurrentPlayer().getLastWorkerMoved().getRow(), model.getClonedGameBoard(), otherDivinities);
        if (validPositionsForMove.isEmpty()) {
            try {
                nextAction = model.getCurrentPlayer().getDivinity().move(-1, -1, -1, -1, model);
                nextAction();
            } catch (Exception e) {
                System.out.println("fatal error");
            }
        } else {
            ArrayList<WorkerValidCells> move = new ArrayList<>();
            move.add(new WorkerValidCells(validPositionsForMove, model.getCurrentPlayer().getLastWorkerMoved().getRow(), model.getCurrentPlayer().getLastWorkerMoved().getColumn()));
            getPlayerView(model.getCurrentPlayer().getName()).requestOptionalMove(move);
        }

    }

    public void PrometheusInitialOptionalBuild() {
        ArrayList<WorkerValidCells> build = new ArrayList<>();
        ArrayList<WorkerValidCells> dome = new ArrayList<>();

        ArrayList<Divinity> otherDivinities = new ArrayList<>();
        for (Player p : model.getPlayersInGame()) {
            if (!p.getName().equals(model.getCurrentPlayer().getName())) otherDivinities.add(p.getDivinity());
        }

        ArrayList<Position> workersPosition = model.getPlayerPositionsInMap(model.getCurrentPlayer().getName());

        for (Position p : workersPosition) {
            WorkerValidCells b = new WorkerValidCells(new ArrayList<>(model.getCurrentPlayer().getDivinity().getValidCellForBuilding(p.getColumn(), p.getRow(), otherDivinities, model.getGameBoard())), p.getRow(), p.getColumn());
            WorkerValidCells d = new WorkerValidCells(new ArrayList<>(model.getCurrentPlayer().getDivinity().getValidCellsToPutDome(p.getColumn(), p.getRow(), model.getGameBoard(), otherDivinities)), p.getRow(), p.getColumn());
            if (!b.getValidPositions().isEmpty()) build.add(b);
            if (!d.getValidPositions().isEmpty()) dome.add(d);
        }
        getPlayerView(model.getCurrentPlayer().getName()).requestOptionalBuild(build, dome);
    }

    public void PrometheusMovePostOptionalBuild() {
        ArrayList<Divinity> otherDivinities = new ArrayList<>();
        for (Player p : model.getPlayersInGame()) {
            if (!p.getName().equals(model.getCurrentPlayer().getName())) otherDivinities.add(p.getDivinity());
        }
        ArrayList<WorkerValidCells> move = new ArrayList<>();
        move.add(new WorkerValidCells(model.getCurrentPlayer().getDivinity().getValidCellForMove(model.getCurrentPlayer().getLastWorkerMoved().getColumn(), model.getCurrentPlayer().getLastWorkerMoved().getRow(), model.getGameBoard(), otherDivinities), model.getCurrentPlayer().getLastWorkerMoved().getRow(), model.getCurrentPlayer().getLastWorkerMoved().getColumn()));
        getPlayerView(model.getCurrentPlayer().getName()).requestMove(move);
    }

    /**
     * method handling the elimination of a player when he loses the game cause he can't complete the turn
     */
    public void currentPlayerCantEndTurn() {
        if (model.getPlayersInGame().size() == 2) {

            Server.destroyGameRoom(roomID, model.getCurrentPlayer().getName(), EndReason.lose);
            return;
        } else {
            model.setNumberOfPlayer(model.getGamePlayerNumber() - 1);
            getPlayerView(model.getCurrentPlayer().getName()).endgame("You lose cause you won't be able to end the turn");
            for (Player p : model.getPlayersInGame()) {
                if (!p.getName().equals(model.getCurrentPlayer().getName()))
                    getPlayerView(p.getName()).printMessage(model.getCurrentPlayer().getName() + "lost cause he can't end his turn");
            }
            Server.removeNickname(model.getCurrentPlayer().getName());
            model.unregisterObserver(getPlayerView(model.getCurrentPlayer().getName()));
            model.removePlayer(model.getCurrentPlayer().getName());
            nextAction = GameController::turnChange;
        }
        nextAction();
    }
}