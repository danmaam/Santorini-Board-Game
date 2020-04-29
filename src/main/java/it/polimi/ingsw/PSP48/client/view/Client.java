package it.polimi.ingsw.PSP48.client.view;

import it.polimi.ingsw.PSP48.AbstractView;
import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.networkMessages.receivedObject;
import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * concrete class that represents all the interactions with the client
 *
 * @author Rebecca Marelli, Annalaura Massa
 */
public class Client extends AbstractView implements Runnable, NetworkObserver {
    private static final CellForPrinting[][] gameBoard = new CellForPrinting[5][5]; //class attributes
    private ArrayList<Player> playerList = new ArrayList<Player>();

    public static void main(String[] args) {
        Client playerClient = new Client(ColoursForPrinting.white);
        playerClient.run();
    }

    private String serverResponse = null;
    private boolean serverAnswer = false;
    boolean completedAction;


    /**
     * class constructor initialising the cells of the board
     *
     * @param colour is the colour we assign to the cells
     */
    public Client(ColoursForPrinting colour) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gameBoard[i][j] = new CellForPrinting(i, j, colour);
            }
        }
    }

    public CellForPrinting[][] getGameBoard() {
        return (this.gameBoard);
    }

    /**
     * method used to retrieve a specific cell on the board
     *
     * @param row    is the row of the cell we need
     * @param column is the column of the cell we need
     * @return a reference to the cell once we find it
     */
    public CellForPrinting getCellOnBoard(int row, int column) {
        return (gameBoard[row][column]);
    }

    /**
     * method that scans the board and prints each cell on the screen
     */
    public void printBoard() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gameBoard[i][j].printCellOnScreen();
            }
            System.out.print("\n");
        }
    }

    public ArrayList<Player> getPlayerList() {
        return (this.playerList);
    }

    @Override
    /**
     * method that is called whenever there is a change on the board and we need to update the cells
     * @param newCells represents the list of the cells that have been changed during the turn
     */
    public void changedBoard(ArrayList<Cell> newCells) //method applying changes to the game board after every action by the player
    {
        CellForPrinting tempCell;
        ArrayList<Player> list;
        Player tempPlayer = null;

        for (Cell c : newCells) {
            tempCell = this.getCellOnBoard(c.getRow(), c.getColumn()); //retrieved cell from the board on the client side, now we have to update its values

            //first we check the dome field of the cell
            if (c.isDomed() == false) tempCell.setDome(null);
            else {
                if (tempCell.getDome() == null) tempCell.setDome(new DomeForPrinting(ColoursForPrinting.blue));
            }
            //then we check the building field of the cell
            if (c.getLevel() == 0) tempCell.setBuildings(null);
            else {
                if (tempCell.getBuildings() == null)
                    tempCell.setBuildings(new BuildingsForPrinting(c.getLevel(), ColoursForPrinting.blue));

                else tempCell.getBuildings().setLevel(c.getLevel());
            }

            //lastly, we check the player field of the cell
            if (c.getPlayer() == null) tempCell.setPlayerOnCell(null);
            else {
                list = this.getPlayerList(); //we have to search for the right player in the list, in order to have the parameters to put in the cell
                for (Player p : list) {
                    if (p.getName() == c.getPlayer()) {
                        tempPlayer = p;
                        break;
                    }
                }

                if (tempCell.getPlayerOnCell() == null)
                    tempCell.setPlayerOnCell(new Player(tempPlayer.getName(), tempPlayer.getPlayerColour(), tempPlayer.getDivinity()));
                else {
                    tempCell.getPlayerOnCell().setName(tempPlayer.getName());
                    tempCell.getPlayerOnCell().setPlayerColour(tempPlayer.getPlayerColour());
                    tempCell.getPlayerOnCell().setDivinity(tempPlayer.getDivinity());
                }
            }
        }
        this.printBoard(); //after we modified all the cells we need to print the whole updated board
    }

    @Override
    public void changedPlayerList(ArrayList<String> newPlayerList) {

    }

    @Override
    /**
     * method used to request a move action to the player, showing the valid options and then taking the choice of the player
     * @param validCellsForMove contains the positions of both workers and for each of them the valid cells for the move action
     */
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove) {
        ArrayList<Position> tempList;
        WorkerValidCells notChosenPosition = null, temp = null;
        int workerRow = -1, workerColumn = -1;
        int chosenRow = -1, chosenColumn = -1;

        for (WorkerValidCells c : validCellsForMove) {
            tempList = c.getValidPositions(); //each time we retrieve the list of valid positions for the move action

            for (Position p : tempList) // for each of the valid positions, we need to highlight the cells on the board
            {
                this.getCellOnBoard(p.getRow(), p.getColumn()).setCellColour(ColoursForPrinting.red);
            }
        }
        this.printBoard(); //after highlighting all the cells, we need to print the whole board

        //first we ask the player which one of his workers he wants to move
        Scanner s = new Scanner(System.in);
        System.out.println("Choose the row of the worker you want to move: ");
        while (s.hasNext()) {
            workerRow = ((s.nextInt()) - 1);
        }
        System.out.println("Choose the column of the worker you want to move: ");
        while (s.hasNext()) {
            workerColumn = ((s.nextInt()) - 1);
        }

        //after reading the commands, we need to check if the player has chosen a valid worker
        boolean valid = false;
        do {
            for (int i = 0; (i < validCellsForMove.size() && valid == false); i++) {
                if (workerRow == validCellsForMove.get(i).getwR() && workerColumn == validCellsForMove.get(i).getwC()) {
                    valid = true;
                }
            }
            if (valid == false) {
                System.out.println("You need to choose a valid worker!!");
                this.printBoard();
                System.out.println("Choose the row of the worker you want to move: ");
                while (s.hasNext()) {
                    workerRow = ((s.nextInt()) - 1);
                }
                System.out.println("Choose the column of the worker you want to move: ");
                while (s.hasNext()) {
                    workerColumn = ((s.nextInt()) - 1);
                }
            }

        } while (valid == false);

        //after we make sure that the player has chosen a valid worker to move, we highlight again the board, but only with the cells of that worker
        for (WorkerValidCells c : validCellsForMove) {
            if (c.getwR() != workerRow && c.getwC() != workerColumn) {
                notChosenPosition = c;
                break;
            }
        }
        for (Position po : notChosenPosition.getValidPositions()) {
            this.getCellOnBoard(po.getRow(), po.getColumn()).setCellColour(ColoursForPrinting.white);
        }
        this.printBoard();

        //now we ask to the player the move he wants to make
        System.out.println("Choose the row where you want to move, among the red ones: ");
        while (s.hasNext()) {
            chosenRow = ((s.nextInt()) - 1);
        }
        System.out.println("Choose the column where you want to move, among the red ones: ");
        while (s.hasNext()) {
            chosenColumn = ((s.nextInt()) - 1);
        }

        //we have to check if the player has chosen a valid cell
        boolean validMove = false;
        do {
            for (WorkerValidCells c : validCellsForMove) {
                if (c != notChosenPosition) {
                    temp = c;
                    break;
                }
            }
            tempList = temp.getValidPositions();
            for (int i = 0; (i < tempList.size() && validMove == false); i++) {
                if (chosenRow == tempList.get(i).getRow() && chosenColumn == tempList.get(i).getColumn()) {
                    validMove = true;
                }
            }
            if (validMove == false) {
                System.out.println("You need to choose a valid cell!!");
                this.printBoard();
                System.out.println("Choose the row where you want to move, among the red ones: ");
                while (s.hasNext()) {
                    chosenRow = ((s.nextInt()) - 1);
                }
                System.out.println("Choose the column where you want to move, among the red ones: ");
                while (s.hasNext()) {
                    chosenColumn = ((s.nextInt()) - 1);
                }
            }
        } while (validMove = false);

        //after checking that the move is valid, we just need to bring the colours of the board to their original state and notify the controller about the choice
        for (WorkerValidCells c : validCellsForMove) {
            if (c != notChosenPosition) {
                temp = c;
                break;
            }
        }
        tempList = temp.getValidPositions();
        for (Position p : tempList) {
            this.getCellOnBoard(p.getRow(), p.getColumn()).setCellColour(ColoursForPrinting.white);
        }
        this.printBoard();

        MoveCoordinates chosenCoordinates = new MoveCoordinates(workerRow, workerColumn, chosenRow, chosenColumn);
        this.notifyObserver(x -> {
            x.move(chosenCoordinates);
        });
    }

    @Override
    public void requestDomeOrBuild(ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome) {

    }

    @Override
    public void declareWin() {

    }

    @Override
    /**
     * method communicating to a player that the game has been lost
     */
    public void declareLose() {
        System.out.println(" You lost the game! :( ");
    }

    @Override
    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {

    }

    @Override
    public void requestInitialPlayerSelection(ArrayList<String> players) {

    }

    @Override
    /**
     * method that handles the initial positioning of each worker on the board, showing the valid cells to the players and taking their choices
     * @param validCells is the list of valid cells for the worker we are currently positioning
     */
    public void requestInitialPositioning(ArrayList<Position> validCells) {
        int chosenRow = -1, chosenColumn = -1;

        //first we need to highlight each of the valid cells to the player
        for (Position p : validCells) {
            this.getCellOnBoard(p.getRow(), p.getColumn()).setCellColour(ColoursForPrinting.red);
        }
        this.printBoard();

        //after that, we need to ask the player their choice
        Scanner s = new Scanner(System.in);
        System.out.println("Choose the row where you want to position: ");
        while (s.hasNext()) {
            chosenRow = ((s.nextInt()) - 1);
        }
        System.out.println("Choose the column where you want to position: ");
        while (s.hasNext()) {
            chosenColumn = ((s.nextInt()) - 1);
        }

        //now we need to check if the choice is valid
        boolean validChoice = false;
        do {
            for (int i = 0; (i < validCells.size() && validChoice == false); i++) {
                if (validCells.get(i).getRow() == chosenRow && validCells.get(i).getColumn() == chosenColumn) //it means that we have found the chosen position in the list of the valid ones
                {
                    validChoice = true;
                }
            }
            if (validChoice == false) {
                System.out.println("You need to choose a valid cell!!");
                this.printBoard();
                System.out.println("Choose the row where you want to position: ");
                while (s.hasNext()) {
                    chosenRow = ((s.nextInt()) - 1);
                }
                System.out.println("Choose the column where you want to position: ");
                while (s.hasNext()) {
                    chosenColumn = ((s.nextInt()) - 1);
                }
            }
        } while (validChoice == false);

        //after checking that the player has chosen a valid cell, we need to bring the board back to its original colours and notify the observers
        for (Position p : validCells) {
            this.getCellOnBoard(p.getRow(), p.getColumn()).setCellColour(ColoursForPrinting.white);
        }
        this.printBoard();

        Position chosenPosition = new Position(chosenRow, chosenColumn);
        this.notifyObserver(x -> {
            x.putWorkerOnTable(chosenPosition);
        });
    }

    @Override
    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber) {

    }

    @Override
    public void printMessage(String s) {

    }

    @Override
    public void requestOptionalMove(WorkerValidCells validCellsForMove) {

    }

    @Override
    public void requestOptionalBuild(WorkerValidCells build, WorkerValidCells dome) {

    }

    /**
     * @author Daniele Mammone
     * Starts The Player Client, and initialize the network connection
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Santorini CLI Mode\nPlease insert the Server IP and press ENTER.");
        String serverIP = scanner.nextLine();
        ObjectOutputStream outputStm;
        ObjectInputStream inputStm;

        Socket server;
        try {
            server = new Socket(serverIP, 7777);
        } catch (IOException e) {
            System.out.println("Can't connect to the Server. Aborting.");
            return;
        }
        ClientNetworkAdapter cA = new ClientNetworkAdapter(server);
        cA.addObserver(this);
        Thread cAThread = new Thread(cA);
        cAThread.start();

        System.out.println("Connected to the Santorini Server");
        String nextMessage;

        synchronized (this) {
            /* reset the variable that contains the next string to be consumed
             * from the server */
            completedAction = false;

            while (!completedAction) {
                System.out.println("Choose a nickname to login");
                nextMessage = scanner.nextLine();
                cA.requestNicknameSend(nextMessage);
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }

            //logged into the server, we must choose the game mode

            completedAction = false;
            while (!completedAction) {
                System.out.println("Choose a game mode");
                System.out.println("2D for two player game with divinities;");
                System.out.println("3D for two player game with divinities;");
                System.out.println("2ND for two player game without divinities, followed by your birthday in the form dd-mm-aaaa;");
                System.out.println("3ND for two player game without divinities, followed by your birthday in the form dd-mm-aaaa;");
                nextMessage = scanner.nextLine();
                cA.setGameMode(nextMessage);
                try {
                    wait();
                } catch (InterruptedException e) {

                }
            }


            /* we have the response, print it */
        }
    }


    @Override
    public synchronized void nicknameResult(String result) {
        System.out.println(result);
        if (result.equals("Invalid nickname. Retry")) completedAction = false;
        else completedAction = true;
        notifyAll();
    }

    @Override
    public synchronized void gameModeResult(String result) {
        System.out.println(result);
        if (result.equals("Not valid mode. Retry")) completedAction = false;
        else completedAction = true;
        notifyAll();
    }

    @Override
    public void update(receivedObject obj) {

    }
}

