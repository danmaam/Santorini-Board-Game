package it.polimi.ingsw.PSP48;

import java.util.ArrayList;

/**
 * class used to contain all the data of a certain game, like the players and their workers, the current player and the status of the game
 *there is a list of chosen divinities because players first choose a number of divinities according to how many are playing and then each of them chooses his/her divinity from the smaller group they have selected
 *the list of available divinities is used when players are still selecting the smaller group of divinities and they need to know what they can still choose
 *  @author Rebecca Marelli
 */
public class GameData
{
    private ArrayList<PlayerWorkerConnection> playersInGame= new ArrayList<PlayerWorkerConnection>(); //i giocatori sono in ordine fisso secondo l'ordine di gioco
    private ArrayList<Colour> availableColours= new ArrayList<Colour>();
    private ArrayList<Divinity> availableDivinities= new ArrayList<Divinity>();
    private ArrayList<Divinity> chosenDivinities= new ArrayList<Divinity>();
    private int currentPlayer; //se siamo in un momento per cui il current player non deve avere un valore possiamo settarlo a -1
    private final Cell[][] boardCell=new Cell[5][5]; //tramite il costruttore di Cell devo inizializzare le celle, qui sono tutte a null (RIVEDERE)
    private Status gameState;

    /**
     * method used to obtain how many players are in a certain game (they can be 2 or 3)
     * @return an int that is the total number of players in the game
     */
    public int getNumberOfPlayers()
    {
        int index;
        int totalPlayers;

        totalPlayers=0;
        for(index=0; index<this.playersInGame.size(); index++)
        {
            totalPlayers++;
        }
        return(totalPlayers);
    }

    /**
     * method used to get a certain player that is participating in the game, using the arrayList playersInGame where each player is also associated to his/her workers
     * @param playerNumber is used to look for a player in the arrayList called playersInGame, where each player is stored at a certain index that never changes during the game
     * @return a reference to the needed player
     */
    public Player getPlayer(int playerNumber) //potrebbe lanciare una eccezione se per esempio si chiede un giocatore che abbia un indice maggiore di due (fuori dall'arraylist) oppure se giocatore è null (?)
    {
        Player neededPlayer;

        neededPlayer=this.playersInGame.get(playerNumber).getPlayer(); //NB è il metodo get player della classe playerworkerconnection perchè l'ho chiamato su un oggetto di quel tipo

        return(neededPlayer);
    }

    /**
     * method used to retrieve a player from gameData and to have direct access to his/her workers
     * @param playerIndex is used to get the correct player from the arrayList playersInGame
     * @return a reference to the object containing a player and the list of the workers
     */
    public PlayerWorkerConnection getPlayersAndWorkers(int playerIndex) //eccezione se riceve un indice al di fuori dei limiti dell'arrayList (o null(?))
    {
        PlayerWorkerConnection neededConnection;

        neededConnection=this.playersInGame.get(playerIndex);

        return(neededConnection);
    }

    /**
     * method that updates an element of the playersInGame list
     * @param newConnection is the new element that we substitutes one in the list
     * @param position is the position where we put the new element
     */
    public void setConnection(PlayerWorkerConnection newConnection, int position)
    {
        this.playersInGame.set(position, newConnection);
    }

    /**
     * method that retrieves all the players of the game, together with the list of their workers
     * @return the list of PlayerWorkerConnection elements
     */
    public ArrayList<PlayerWorkerConnection> getPlayersInGame()
    {
        ArrayList<PlayerWorkerConnection> neededList;

        neededList=this.playersInGame;

        return(neededList);
    }

    /**
     * method used to set the value containing all of the players of the game and their workers
     * @param newPlayersInGame contains the arrayList of PlayerWorkerConnection variables that has to be assigned
     */
    public void setPlayersInGame(ArrayList<PlayerWorkerConnection> newPlayersInGame)
    {
        this.playersInGame=newPlayersInGame;
    }

    /**
     * method used during the state where players are still choosing their workers and they need to know what colours are still available
     * @return an arrayList of the colours that can still be chosen, which is part of this class
     */
    public ArrayList<Colour> getAvailableColours()
    {
        ArrayList<Colour> coloursToPick;

        coloursToPick=this.availableColours;

        return(coloursToPick);
    }

    /**
     * method used to change the list of available colours while players are making their choice
     * @param newAvailableColours is the new list of available colours
     */
    public void setAvailableColours(ArrayList<Colour> newAvailableColours)
    {
        this.availableColours=newAvailableColours;
    }

    /**
     * method used to retrieve the divinities still available to be picked by a player
     * @return an arrayList containing said divinities
     */
    public ArrayList<Divinity> getAvailableDivinities()
    {
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

        neededCurrentPlayer=this.getPlayer(currentPlayer); //ho usato un metodo della classe stessa per restituire il giocatore corrente

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
     * method used to assign divinities to their respective workers after players make their choice
     * @param playerChoice represents the divinity chosen by the player (it is communicated by the view to the controller and then added to change the state of the model)
     * @param listOfWorkers is used to know to which worker the divinity must be assigned
     */
    public void addSelectedDivinity(Divinity playerChoice, ArrayList<Worker> listOfWorkers) //eccezione se divinità non è fra quelle chosen o se indice del giocatore è sbagliato
    {
        int j; //indice usato per scorrere la lista di lavoratori dove inserire la divinità

        for(j=0; j<listOfWorkers.size(); j++)
        {
            listOfWorkers.get(j).setDivinity(playerChoice);
        }
    }

    /**
     * method used to do the actions associated to a certain state, by calling the correct handler method
     */
    public void stateAction()
    {
        this.gameState.handleRequest();
    }

    /**
     * method used to know the status of a certain game
     * @return the state of the game
     */
    public Status getGameState()
    {
        Status neededState;

        neededState=this.gameState;

        return(neededState);
    }

    /**
     * method that updates the state of the game with the correct one
     * @param newState is the updated state that has to be assigned to GameData
     */
    public void setStatus(Status newState)
    {
        this.gameState=newState;
    }
}
