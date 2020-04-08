package it.polimi.ingsw.PSP48;

import it.polimi.ingsw.PSP48.divinities.Divinity;

import java.util.ArrayList;

/**
 * class used to contain all the data of a certain game, like the players and their workers, the current player and the status of the game
 * there is a list of chosen divinities because players first choose a number of divinities according to how many are playing and then each of them chooses his/her divinity from the smaller group they have selected
 * the list of available divinities is used when players are still selecting the smaller group of divinities and they need to know what they can still choose
 *
 * @author Rebecca Marelli
 */
public class GameData {
    private ArrayList<Player> playersInGame = new ArrayList<Player>(); //i giocatori sono in ordine fisso secondo l'ordine di gioco
    private ArrayList<Colour> availableColours = new ArrayList<Colour>();
    private ArrayList<Divinity> availableDivinities = new ArrayList<Divinity>();
    private ArrayList<Divinity> chosenDivinities = new ArrayList<Divinity>();
    private int currentPlayer=-1; //se siamo in un momento per cui il current player non deve avere un valore possiamo settarlo a -1
    private final Cell[][] boardCell = new Cell[5][5]; //tramite il costruttore di Cell devo inizializzare le celle, qui sono tutte a null
    private Status gameState;

    public GameData() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boardCell[i][j] = new Cell(i, j);
            }
        }
    }

    /**
     * method used to obtain how many players are in a certain game (they can be 2 or 3)
     *
     * @return an int that is the total number of players in the game
     */
    public int getNumberOfPlayers() {
        int index;
        int totalPlayers;

        totalPlayers = 0;
        for (index = 0; index < this.playersInGame.size(); index++) {
            totalPlayers++;
        }
        return (totalPlayers);
    }


    /**
     * method that retrieves all the players of the game, together with the list of their workers
     *
     * @return the list of PlayerWorkerConnection elements
     */
    public ArrayList<Player> getPlayersInGame() {
        return playersInGame;
    }

    public void addPlayer(Player p) {
        playersInGame.add(p);
    }

    /**
     * method used during the state where players are still choosing their workers and they need to know what colours are still available
     *
     * @return an arrayList of the colours that can still be chosen, which is part of this class
     */
    public ArrayList<Colour> getAvailableColours() {
        ArrayList<Colour> coloursToPick;

        coloursToPick = this.availableColours;

        return (coloursToPick);
    }

    public void removeColour(Colour c) {
        availableColours.remove(c);
    }

    /**
     * method used to retrieve the divinities still available to be picked by a player
     *
     * @return an arrayList containing said divinities
     */
    public ArrayList<Divinity> getAvailableDivinities() {
        ArrayList<Divinity> divinitiesToPick;

        divinitiesToPick=this.availableDivinities;

        return(divinitiesToPick);
    }

    /**
     * method used to change the list of available divinities that players can still pick
     * @param newAvailableDivinities is the updated list
     */
    public void setAvailableDivinities(ArrayList<Divinity> newAvailableDivinities)
    {
        this.availableDivinities=newAvailableDivinities;
    }

    /**
     * method used to retrieve the list of divinities chosen to be distributed among the players
     * @return said list of divinities using an arrayList
     */
    public ArrayList<Divinity> getChosenDivinities()
    {
        ArrayList<Divinity> neededDivinities;

        neededDivinities=this.chosenDivinities;

        return(neededDivinities);
    }

    /**
     * methos that updates the list of divinities that still need to be distributed among players
     * @param newChosenDivinities represents the updated list
     */
    public void setChosenDivinities(ArrayList<Divinity> newChosenDivinities)
    {
        this.chosenDivinities=newChosenDivinities;
    }

    /**
     * method used to retrieve the player of the current turn
     * @return a reference to the current player
     */
    public Player getCurrentPlayer() //eccezione se il player è null (?)
    {
        Player neededCurrentPlayer;

        neededCurrentPlayer = this.playersInGame.get(currentPlayer); //ho usato un metodo della classe stessa per restituire il giocatore corrente

        return(neededCurrentPlayer);
    }

    /**
     * method used to change the current player at the end of a certain turn
     * since players are stored in playersInGame according to their turns, we just need to increase by one the int parameter currentPlayer
     */
    public void setCurrentPlayer()
    {
        if (this.currentPlayer==-1 || this.currentPlayer==(getNumberOfPlayers()-1))
        {
            this.currentPlayer=0;
        }
        else this.currentPlayer++;

    }

    /**
     * method used to retrieve a certain cell from the board, to then get its state (for example if it is occupied by a worker)
     * @param row gives the row of the cell in the board
     * @param column gives the column of the cell in the board
     * @return a reference to the needed cell
     */
    public Cell getCell(int row, int column) //eccezione se riceve indici al di fuori della dimensione della matrice
    {
        Cell neededCell;

        neededCell=this.boardCell[row][column];

        return(neededCell);
    }


    /**
     * method used to do the actions associated to a certain state, by calling the correct handler method
     */
    public void stateAction() {
        //this.gameState.handleRequest();
    }

    /**
     * method used to know the status of a certain game
     * @return the state of the game
     */
    public Status getGameState()
    {
        Status neededState;

        neededState=this.gameState;

        return (neededState);
    }

    /**
     * method that updates the state of the game with the correct one
     *
     * @param newState is the updated state that has to be assigned to GameData
     */
    public void setStatus(Status newState) {
        this.gameState = newState;
    }

    /**
     * @param playerName the name of the player
     * @return the cells' cordinates of player
     */
    public ArrayList<Position> getPlayerPositionsInMap(String playerName) {
        ArrayList<Position> returnArray = new ArrayList<Position>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (boardCell[i][j].getPlayer().equals(playerName)) returnArray.add(new Position(i, j));
            }
        }
        return returnArray;
    }

    public Divinity getPlayerDivinity(String playerName) {
        for (Player p : playersInGame) {
            if (p.getName() == playerName) return p.getDivinity();
        }
        return null;
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


}
