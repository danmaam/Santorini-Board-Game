package it.polimi.ingsw.PSP48.server.model;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.server.model.divinities.*;
import it.polimi.ingsw.PSP48.observers.ModelObserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * class used to contain all the data of a certain game, like the players and their workers, the current player and the status of the game
 * there is a list of chosen divinities because players first choose a number of divinities according to how many are playing and then each of them chooses his/her divinity from the smaller group they have selected
 * the list of available divinities is used when players are still selecting the smaller group of divinities and they need to know what they can still choose
 *
 * @author Rebecca Marelli
 */
public class Model {
    private ArrayList<Player> playersInGame = new ArrayList<Player>(); //i giocatori sono in ordine fisso secondo l'ordine di gioco
    private Stack<Colour> availableColours = new Stack<Colour>();

    public boolean isGameWithDivinities() {
        return gameWithDivinities;
    }

    public int getGamePlayerNumber() {
        return gamePlayerNumber;
    }

    private String playerWithCirce = null;

    private ArrayList<Divinity> availableDivinities;
    private int currentPlayer = -1; //se siamo in un momento per cui il current player non deve avere un valore possiamo settarlo a -1
    private final Cell[][] boardCell = new Cell[5][5]; //tramite il costruttore di Cell devo inizializzare le celle, qui sono tutte a null
    private final int gamePlayerNumber;
    private final boolean gameWithDivinities;
    private int challengerIndex;

    public void setFirstPlayerIndex(int firstPlayerIndex) {
        this.firstPlayerIndex = firstPlayerIndex;
    }

    private int firstPlayerIndex;

    public int getFirstPlayerIndex() {
        return firstPlayerIndex;
    }

    public Model(int number, boolean divinities) {
        if (divinities) {
            availableDivinities = new ArrayList<>();
            if (Apollo.supportedDivinity(number)) availableDivinities.add(new Apollo());
            if (Artemis.supportedDivinity(number)) availableDivinities.add(new Artemis());
            if (Athena.supportedDivinity(number)) availableDivinities.add(new Athena());
            if (Chronus.supportedDivinity(number)) availableDivinities.add(new Chronus());
            if (Circe.supportedDivinity(number)) availableDivinities.add(new Circe());
            if (Demeter.supportedDivinity(number)) availableDivinities.add(new Demeter());
            if (Eros.supportedDivinity(number)) availableDivinities.add(new Eros());
            if (Hephaestus.supportedDivinity(number)) availableDivinities.add(new Hephaestus());
            if (Hestia.supportedDivinity(number)) availableDivinities.add(new Hestia());
            if (Minotaur.supportedDivinity(number)) availableDivinities.add(new Minotaur());
            if (Pan.supportedDivinity(number)) availableDivinities.add(new Pan());
            if (Prometheus.supportedDivinity(number)) availableDivinities.add(new Prometheus());
            if (Zeus.supportedDivinity(number)) availableDivinities.add(new Zeus());
        }
        availableColours.add(Colour.BLUE);
        availableColours.add(Colour.GRAY);
        availableColours.add(Colour.WHITE);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boardCell[i][j] = new Cell(i, j);
            }
        }
        gamePlayerNumber = number;
        gameWithDivinities = divinities;

    }

    //OBSERVER METHODS

    private ArrayList<ModelObserver> observers = new ArrayList<ModelObserver>();

    public void registerObserver(ModelObserver obv) {
        observers.add(obv);
    }

    public void unregisterObserver(ModelObserver obv) {
        observers.remove(obv);
    }

    public void notifyObservers(Consumer<ModelObserver> lambda) {
        for (ModelObserver o : observers) {
            lambda.accept(o);
        }
    }


    /**
     * method used to obtain how many players are in a certain game (they can be 2 or 3)
     *
     * @return an int that is the total number of players in the game
     */
    public int getNumberOfPlayers() {
        return (this.playersInGame.size());
    }

    /**
     * method that retrieves all the players of the game, together with the list of their workers
     *
     * @return the list of PlayerWorkerConnection elements
     */
    public ArrayList<Player> getPlayersInGame() {
        return playersInGame;
    }

    public void addPlayer(String playerName, Colour playerColour, Calendar playerBirthday) {
        playersInGame.add(new Player(playerName, playerBirthday, gameWithDivinities, playerColour));
        ArrayList<String> newPlayerString = new ArrayList<>();
        for (Player p : playersInGame) {
            if (p.getDivinity() == null && gameWithDivinities) {
                newPlayerString.add(p.getName() + '.' + p.getColour().toString() + ".Divinity Not Chosen");
            } else if (p.getDivinity() == null && !gameWithDivinities) {
                newPlayerString.add(p.getName() + '.' + p.getColour().toString() + ".Base Divinity");
            } else {
                newPlayerString.add(p.getName() + '.' + p.getColour().toString() + "." + p.getDivinity().getName());
            }
        }
        notifyObservers(x -> x.changedPlayerList(newPlayerString));
    }

    /**
     * Returns the next available colours, since players' colours are set by the controller when a player connects to the game room.
     *
     * @return the next available colour
     */
    public Colour getNextColour() {
        return availableColours.pop();
    }


    /**
     * Returns a collection of Divinities, with their description, available to be chosen by players.
     *
     * @return ArrayList of type DivinitiesWithDescription cainting available divinities to be chosen
     */
    public ArrayList<DivinitiesWithDescription> getAvailableDivinities() {
        ArrayList<DivinitiesWithDescription> ret_a = new ArrayList<>();
        availableDivinities.forEach((Divinity d) -> ret_a.add(new DivinitiesWithDescription(d.getName(), d.getDescription())));
        return ret_a;
    }

    /**
     * method used to retrieve the player of the current turn
     *
     * @return a reference to the current player
     */
    public Player getCurrentPlayer() //eccezione se il player è null (?)
    {
        Player neededCurrentPlayer;

        neededCurrentPlayer = this.playersInGame.get(currentPlayer); //ho usato un metodo della classe stessa per restituire il giocatore corrente

        return (neededCurrentPlayer);
    }

    /**
     * method used to change the current player at the end of a certain turn
     * since players are stored in playersInGame according to their turns, we just need to increase by one the int parameter currentPlayer
     */
    public void setNextPlayer() {
        if (this.currentPlayer == -1 || this.currentPlayer == (getNumberOfPlayers() - 1)) {
            this.currentPlayer = 0;
        } else this.currentPlayer++;

    }

    /**
     * method used to retrieve a certain cell from the board, to then get its state (for example if it is occupied by a worker)
     *
     * @param row    gives the row of the cell in the board
     * @param column gives the column of the cell in the board
     * @return a reference to the needed cell
     */
    public Cell getCell(int row, int column) //eccezione se riceve indici al di fuori della dimensione della matrice
    {
        Cell neededCell;

        neededCell = this.boardCell[row][column];

        return (neededCell);
    }

    /**
     * @param playerName the name of the player
     * @return the cells' cordinates of player
     * @author Daniele Mammone
     */
    public ArrayList<Position> getPlayerPositionsInMap(String playerName) {
        ArrayList<Position> returnArray = new ArrayList<Position>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (boardCell[i][j].getPlayer() != null && boardCell[i][j].getPlayer().equals(playerName))
                    returnArray.add(new Position(i, j));
            }
        }
        return returnArray;
    }

    public Player getPlayer(String playerName) {
        for (Player p : playersInGame) {
            if (p.getName().equals(playerName)) return p;
        }
        return null;
    }

    public Cell[][] getGameBoard() {
        return boardCell;
    }

    public void setNextPlayer(int i) {
        currentPlayer = i;
    }

    public void removePlayer(String pName) {
        playersInGame.remove(getPlayer(pName));
        ArrayList<String> newPlayerString = new ArrayList<>();
        for (Player p : playersInGame) {
            if (p.getDivinity() == null && gameWithDivinities) {
                newPlayerString.add(p.getName() + '.' + p.getColour().toString() + ".Divinity Not Chosen");
            } else if (p.getDivinity() == null && !gameWithDivinities) {
                newPlayerString.add(p.getName() + '.' + p.getColour().toString() + ".Base Divinity");
            } else {
                newPlayerString.add(p.getName() + '.' + p.getColour().toString() + "." + p.getDivinity().getName());
            }
        }
        notifyObservers(x -> x.changedPlayerList(newPlayerString));
    }

    public void setChallengerIndex(int challengerIndex) {
        this.challengerIndex = challengerIndex;
    }

    public int getChallengerIndex() {
        return challengerIndex;
    }

    /**
     * Associate the received player with the received Divinity
     *
     * @param playerName The player name
     * @param divinity   The divinity to be associated to the player
     * @author Daniele Mammone
     */
    public void setPlayerDivinity(String playerName, String divinity) throws IllegalArgumentException {
        //first i must find the divinity
        Divinity actualDivinity = null;
        if (divinity.equals("Basic")) actualDivinity = new Divinity();
        else {
            for (Divinity d : availableDivinities) {
                if (d.getName().equals(divinity)) actualDivinity = d;
            }
        }
        if (actualDivinity == null) throw new IllegalArgumentException("Divinity selection not corrected");
        else {
            this.getPlayer(playerName).setDivinity(actualDivinity);
            if (divinity.equals("Circe")) playerWithCirce = playerName;
            availableDivinities.remove(actualDivinity);
        }

    }

    /**
     * This method deletes from the available divinities the ones that the challenger hasn't choose.
     *
     * @param s Divinities chosen by the challenger.
     * @author Daniele Mammone
     */
    public void challengerDivinityChoice(ArrayList<String> s) {
        ArrayList<Divinity> toBeDeleted = new ArrayList<>();
        for (Divinity d : availableDivinities) {
            boolean tbd = true;
            for (String k : s) {
                if (d.getName().equals(k)) tbd = false;
                break;
            }
            if (tbd) toBeDeleted.add(d);
        }
        for (Divinity d : toBeDeleted) availableDivinities.remove(d);
    }

    /**
     * Sets the next player using his name
     *
     * @param playerName The Player Name
     * @author Daniele Mammone
     */
    public void setNextPlayer(String playerName) {
        for (Player p : playersInGame) {
            if (p.getName().equals(playerName)) {
                currentPlayer = playersInGame.indexOf(p);
                break;
            }
        }
    }

    public Cell[][] getClonedGameBoard() {
        Cell[][] clonedBoard = new Cell[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                clonedBoard[i][j] = (Cell) boardCell[i][j].clone();
            }
        }

        return clonedBoard;
    }

    public String getPlayerWithCirce() {
        return playerWithCirce;
    }
}