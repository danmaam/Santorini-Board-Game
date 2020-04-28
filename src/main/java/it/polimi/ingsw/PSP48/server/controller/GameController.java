package it.polimi.ingsw.PSP48.server.controller;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.server.model.*;
import it.polimi.ingsw.PSP48.server.model.divinities.Divinity;
import it.polimi.ingsw.PSP48.server.model.exceptions.*;
import it.polimi.ingsw.PSP48.server.observers.ViewObserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.function.Consumer;

/**
 * @author Daniele Mammone
 * the entire game controller in his magnificence
 * it's an observer f the view, invokes methods on view and model
 */
public class GameController implements ViewObserver {
    private Model model;
    private Consumer<GameController> nextAction;

    private HashMap<String, AbstractView> playersViews = new HashMap<>();

    public AbstractView getPlayerView(String playerName) {
        return playersViews.get(playerName);
    }

    /**
     * Adds a player in the game when a client connects to the server; if the game's player number is reached, the controller starts the game.
     *
     * @param playerDetails a string containing player's bio, formatted as nickname.dd.mm.aaaa
     * @author Daniele Mammone
     */
    @Override
    public void addPlayer(String playerDetails) {
        String[] datas = playerDetails.split(".");
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(datas[3]), Integer.parseInt(datas[2]), Integer.parseInt(datas[1]));
        String col = model.getNextColour();
        model.addPlayer(datas[0], col, c);

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
        try {
            nextAction = model.getCurrentPlayer().getDivinity().build(p.getWorkerRow(), p.getWorkerColumn(), p.getMoveRow(), p.getMoveColumn(), model);
        } catch (Exception e) {
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
        try {
            model.getCurrentPlayer().getDivinity().putWorkerOnBoard(p, model).accept(this);
        } catch (OccupiedCellException e) {
            System.out.println("Positioning on occupied cell");
            getPlayerView(model.getCurrentPlayer().getName()).printMessage("Can't put worker here, occupied cell. Retry operation");
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
        Divinity a = null;
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
     * @param divinities
     * @author Daniele Mammone
     * method invoked when the chosen one has chosen his divinities
     */
    @Override
    public void selectAvailableDivinities(ArrayList<DivinitiesWithDescription> divinities) {
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
        model.setNextPlayer(playerName);
        nextAction = model.getCurrentPlayer().getDivinity().turnBegin(model);
        this.nextAction();
    }


    /**
     * Request to the current player to perform a build
     *
     * @author Daniele Mammone
     */
    public void requestBuildDome() {
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
        ArrayList<WorkerValidCells> validCells = new ArrayList<>();
        ArrayList<Position> workersPosition = model.getPlayerPositionsInMap(model.getCurrentPlayer().getName());
        ArrayList<Divinity> otherDivinities = new ArrayList<>();
        for (Player p : model.getPlayersInGame()) {
            if (!p.getName().equals(model.getCurrentPlayer().getName())) otherDivinities.add(p.getDivinity());
        }
        for (Position p : workersPosition) {
            validCells.add(new WorkerValidCells(new ArrayList<>(model.getCurrentPlayer().getDivinity().getValidCellForMove(p.getRow(), p.getColumn(), model.getGameBoard(), otherDivinities)), p.getRow(), p.getColumn()));
            getPlayerView(model.getCurrentPlayer().getName()).requestMove(validCells);
        }
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
            ArrayList<String> players = new ArrayList<String>();
            playersViews.forEach((k, v) -> players.add(k));
            for (String p : players) {
                if (p.equals(model.getCurrentPlayer().getName())) getPlayerView(p).declareWin();
                else getPlayerView(p).declareLose();
            }
            nextAction = (GameController::gameEnd);
        } else {
            this.nextAction();
        }
    }

    public void gameEnd() {

    }


    /**
     * @author Rebecca Marelli
     */
    public void postBuild() {
        boolean win = false;
        for (Player p : model.getPlayersInGame()) {
            if (p.getDivinity().getName().equals("Chronus")) {
                win = p.getDivinity().winCondition(model);
                break;
            }
        }

        if (win) {
            for (Player p : model.getPlayersInGame()) {
                if (p.getDivinity().getName().equals("Chronus")) getPlayerView(p.getName()).declareWin();
                else getPlayerView(p.getName()).declareLose();
            }
            nextAction = (GameController::gameEnd);
        }
        this.nextAction();
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
        if (model.getCurrentPlayer().getName().equals(model.getPlayerWithCirce()) && model.getCurrentPlayer().getTempDivinity() != null)
            nextAction = model.getCurrentPlayer().getTempDivinity().turnBegin(model);
        else nextAction = model.getCurrentPlayer().getDivinity().turnBegin(model);
        this.nextAction();
    }

    /**
     * @author Rebecca Marelli
     * method that checks if a player can complete a turn by moving and then building
     */
    public void CheckIfCanEndTurnBaseDivinity() {
        ArrayList<Position> playerPositions;
        Position position1, position2;
        ArrayList<Divinity> otherDivinities = new ArrayList<>();
        ArrayList<Position> cellsForMove1;
        ArrayList<Position> cellsForMove2;
        ArrayList<Position> cellsForBuild1;
        ArrayList<Position> cellsForBuild2;
        ArrayList<Position> cellsForDome1;
        ArrayList<Position> cellsForDome2;
        boolean position1Move = false;
        boolean position2Move = false;

        for (Player pl : model.getPlayersInGame()) //inizializzo array contenente le divinità diverse da quelle del mio player, da passare alle funzioni che restituiscono le celle valide
        {
            if (!pl.getName().equals(model.getCurrentPlayer().getName())) otherDivinities.add(pl.getDivinity());
        }

        playerPositions = model.getPlayerPositionsInMap(model.getCurrentPlayer().getName()); //ottenuta la lista con le due posizioni


        position1 = playerPositions.get(0); //prendo la prima e mi faccio la lista con le celle adiacenti dove posso muovere
        cellsForMove1 = model.getCurrentPlayer().getDivinity().getValidCellForMove(position1.getRow(), position1.getColumn(), model.getGameBoard(), otherDivinities);
        if (playerPositions.size() >= 1) {
            position2 = playerPositions.get(1); //faccio la stessa cosa con la seconda posizione
            cellsForMove2 = model.getCurrentPlayer().getDivinity().getValidCellForMove(position2.getRow(), position2.getColumn(), model.getGameBoard(), otherDivinities);
        } else cellsForMove2 = null;


        if (cellsForMove1 != null) {
            for (Position c : cellsForMove1) //per ogni cella valida per lo spostamento, guardo se si può fare una qualunque costruzione
            {
                cellsForBuild1 = model.getCurrentPlayer().getDivinity().getValidCellForBuilding(c.getRow(), c.getColumn(), otherDivinities, model.getGameBoard());
                cellsForDome1 = model.getCurrentPlayer().getDivinity().getValidCellsToPutDome(c.getRow(), c.getColumn(), model.getGameBoard(), otherDivinities);
                if (cellsForBuild1 != null || cellsForDome1 != null) position1Move = true;
            }
        }

        if (cellsForMove2 != null) {
            for (Position c : cellsForMove2) {
                cellsForBuild2 = model.getCurrentPlayer().getDivinity().getValidCellForBuilding(c.getRow(), c.getColumn(), otherDivinities, model.getGameBoard());
                cellsForDome2 = model.getCurrentPlayer().getDivinity().getValidCellsToPutDome(c.getRow(), c.getColumn(), model.getGameBoard(), otherDivinities);
                if (cellsForBuild2 != null || cellsForDome2 != null) position2Move = true;
            }
        }

        //se da nessuna delle due position si può muovere e poi costruire il giocatore ha perso, altrimenti devo far selezionare da dove vuole muovere

        if (!position1Move && !position2Move) {
            //bisogna eliminare il giocatore dalla partita, e porre il controller in modalità gioco finito nel caso in cui vi siano solo due giocatori ancora in gioco
            if (model.getPlayersInGame().size() == 2) {
                getPlayerView(model.getCurrentPlayer().getName()).declareLose();
                for (Player p : model.getPlayersInGame()) {
                    if (!p.getName().equals(model.getCurrentPlayer().getName()))
                        getPlayerView(p.getName()).declareWin();
                }
                nextAction = GameController::gameEnd;
            } else {
                model.removePlayer(model.getCurrentPlayer().getName());
                nextAction = GameController::turnChange;
            }
        } else nextAction = GameController::requestMove;
        nextAction();
    }

    /**
     * @author Daniele Mammone
     * Starts the game, so, randomically, chose the "chosen", sets the chosen as last player to chose divinities, and asks the chosen to select a set of divinities
     */
    public void startGameWithDivinities() {
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
        Player firstPlayer = model.getPlayersInGame().get(0);
        for (Player p : model.getPlayersInGame()) {
            if (p.getBirthday().compareTo(firstPlayer.getBirthday()) < 0) firstPlayer = p;
        }
        //found the younger player, I need to set him as the first player
        model.setNextPlayer(model.getPlayersInGame().indexOf(firstPlayer));
        model.setFirstPlayerIndex(model.getPlayersInGame().indexOf(firstPlayer));
        //set the first player, i must set the base divinity for every player
        for (Player p : model.getPlayersInGame()) {
            model.setPlayerDivinity(p.getName(), "Basic");
        }
        //set the basic moves on players, i start the game requesting the initial positioning
        this.requestInitialPositioning();
    }

    /**
     * @author Daniele Mammone
     * Request the current player to choose his divinity
     */
    public void requestDivinitySelection() {
        getPlayerView(model.getCurrentPlayer().getName()).requestDivinitySelection(model.getAvailableDivinities());
    }

    public void requestFirstPlayerSelection() {
        ArrayList<String> players = new ArrayList<>();
        model.getPlayersInGame().forEach((Player p) -> players.add(p.getName()));
        getPlayerView(model.getPlayersInGame().get(model.getChallengerIndex()).getName()).requestInitialPlayerSelection(players);
    }

    public void requestInitialPositioning() {
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
            nextAction = model.getCurrentPlayer().getDivinity().turnBegin(model);
        } else {
            nextAction = GameController::requestInitialPositioning;
        }
    }

    /**
     * Request the challenger to choose divinities
     *
     * @author Daniele Mammone
     */
    public void requestChallengerDivinitiesSelection() {
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
            } catch (Exception e) {
                System.out.println("Fatal error");
            }
        } else
            getPlayerView(model.getCurrentPlayer().getName()).requestOptionalBuild(new WorkerValidCells(validForBuilding, model.getCurrentPlayer().getLastWorkerMoved().getRow(), model.getCurrentPlayer().getLastWorkerMoved().getColumn()),
                    new WorkerValidCells(validForDoming, model.getCurrentPlayer().getLastWorkerMoved().getRow(), model.getCurrentPlayer().getLastWorkerMoved().getColumn()));
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
                nextAction = model.getCurrentPlayer().getDivinity().move(-1, -1, -1, -1, null);
                nextAction();
            } catch (Exception e) {
                System.out.println("fatal error");
            }
        } else
            getPlayerView(model.getCurrentPlayer().getName()).requestOptionalMove(new WorkerValidCells(validPositionsForMove, model.getCurrentPlayer().getLastWorkerMoved().getRow(), model.getCurrentPlayer().getLastWorkerMoved().getColumn()));
    }

}