package it.polimi.ingsw.PSP48.client.CLI;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.client.networkmanager.ClientNetworkIncoming;
import it.polimi.ingsw.PSP48.client.networkmanager.ClientNetworkOutcoming;
import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * concrete class that represents all the interactions with the client
 *
 * @author Rebecca Marelli, Annalaura Massa, Daniele Mammone
 */
public class CLI implements Runnable, ViewInterface, ClientNetworkObserver {
    private static final CellForPrinting[][] gameBoard = new CellForPrinting[5][5]; //class attributes
    private ArrayList<Player> playerList = new ArrayList<Player>();

    private ArrayList<ViewObserver> observers = new ArrayList<ViewObserver>();

    public void registerObserver(ViewObserver obv) {
        observers.add(obv);
    }

    public void unregisterObserver(ViewObserver obv) {
        observers.remove(obv);
    }

    public void notifyObserver(Consumer<ViewObserver> lambda) {
        for (ViewObserver obv : observers) lambda.accept(obv);
    }

    boolean completedAction;
    private Socket server;
    private ClientNetworkOutcoming cA;

    /**
     * class constructor initialising the cells of the board
     *
     * @param colour is the colour we assign to the cells
     */
    public CLI(ColoursForPrinting colour) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gameBoard[i][j] = new CellForPrinting(i, j, colour);
            }
        }
    }

    public CellForPrinting[][] getGameBoard() {
        return (gameBoard);
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
        System.out.println("\033[1;1H\033[2J");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                gameBoard[i][j].printCellOnScreen();
            }
            System.out.println("\n");
        }
    }

    /**
     * method used to reset the colour of the board after we finish a move or build action during the turn
     *
     * @param positionsToReset are the cells we need to reset
     */
    public void resetBoard(ArrayList<Position> positionsToReset) {
        for (Position p : positionsToReset) {
            this.getCellOnBoard(p.getRow(), p.getColumn()).setCellColour(ColoursForPrinting.white);
        }
    }

    /**
     * Method used to get the list of the players in the game.
     *
     * @return list of players
     */
    public ArrayList<Player> getPlayerList() {
        return (this.playerList);
    }


    /**
     * method that is called whenever there is a change on the board and we need to update the cells
     *
     * @param newCells represents the list of the cells that have been changed during the turn
     */
    @Override
    public void changedBoard(ArrayList<Cell> newCells) //method applying changes to the game board after every action by the player
    {
        CellForPrinting tempCell;
        ArrayList<Player> list;
        Player tempPlayer = null;

        for (Cell c : newCells) {
            tempCell = this.getCellOnBoard(c.getRow(), c.getColumn()); //retrieved cell from the board on the client side, now we have to update its values

            //first we check the dome field of the cell
            if (c.isDomed() && tempCell.getDome() == null)
                tempCell.setDome(new DomeForPrinting(ColoursForPrinting.blue));

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
                    if (p.getName().equals(c.getPlayer())) {
                        tempPlayer = p;
                        break;
                    }
                }
                tempCell.setPlayerOnCell(tempPlayer);
            }
        }
        this.printBoard(); //after we modified all the cells we need to println the whole updated board
    }

    @Override
    public void changedPlayerList(ArrayList<String> newPlayerList) {
        ArrayList<Player> newList = new ArrayList<Player>();
        char separator = '.';
        for (String str : newPlayerList) {
            int n = str.indexOf(separator);
            String playerName = str.substring(0, n);
            int k = str.indexOf(separator, n + 1);
            String playerColour = str.substring(n + 1, k);
            ColoursForPrinting ansiPlayerColour;
            if (playerColour.equals("BLUE")) {
                ansiPlayerColour = ColoursForPrinting.blue;
            } else if (playerColour.equals("GRAY")) {
                ansiPlayerColour = ColoursForPrinting.gray;
            } else {
                ansiPlayerColour = ColoursForPrinting.white;
            }
            String playerDivinity = str.substring(k + 1);
            newList.add(new Player(playerName, ansiPlayerColour, playerDivinity));
        }

        playerList = newList;
        System.out.println("Players in game:");
        for (Player p : playerList) {
            System.out.println(p.getName() + " " + p.toString() + " " + p.getDivinity());
        }
    }


    /**
     * method used to request a move action to the player, showing the valid options and then taking the choice of the player
     *
     * @param validCellsForMove contains the positions of both workers and for each of them the valid cells for the move action
     */
    @Override
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove) {
        ArrayList<Position> tempList = new ArrayList<>();
        int workerRow = -1, workerColumn = -1;
        int chosenRow = -1, chosenColumn = -1;

        for (WorkerValidCells c : validCellsForMove) {
            tempList = c.getValidPositions(); //each time we retrieve the list of valid positions for the move action
            for (Position p : tempList) // for each of the valid positions, we need to highlight the cells on the board
            {
                this.getCellOnBoard(p.getRow(), p.getColumn()).setCellColour(ColoursForPrinting.red);
            }
        }

        //first we ask the player which one of his workers he wants to move
        Scanner s = new Scanner(System.in);
        String workerCoordinate;
        boolean inputSet = false;
        if (validCellsForMove.size() > 1) {
            do {
                this.printBoard();
                System.out.println("Choose the worker you want to move, in the format row,column");
                workerCoordinate = s.nextLine();
                if (workerCoordinate.split(",").length == 2) {
                    workerRow = Integer.parseInt(workerCoordinate.split(",")[0]) - 1;
                    workerColumn = Integer.parseInt(workerCoordinate.split(",")[1]) - 1;
                    if (!containsWorker(validCellsForMove, workerRow, workerColumn)) {
                        System.out.println("Invalid choice");
                    } else inputSet = true;
                } else {
                    System.out.println("Invalid input format");
                }
            } while (!inputSet);
        } else {
            workerRow = validCellsForMove.get(0).getwR();
            workerColumn = validCellsForMove.get(0).getwC();
        }

        //after we make sure that the player has chosen a valid worker to move, we highlight again the board, but only with the cells of that worker
        if (validCellsForMove.size() > 1) //if the player can only move one worker, the cells of that worker have already been highlighted
        {
            for (WorkerValidCells c : validCellsForMove) {
                if (c.getwR() == workerRow && c.getwC() == workerColumn) {
                    tempList = c.getValidPositions();
                    break;
                }
            }
            for (WorkerValidCells cell : validCellsForMove) {
                if (cell.getwR() != workerRow || cell.getwC() != workerColumn) {
                    for (Position p : cell.getValidPositions()) {
                        if (!tempList.contains(p)) {
                            this.getCellOnBoard(p.getRow(), p.getColumn()).setCellColour(ColoursForPrinting.white);
                        }
                    }
                }
            }
        }


        WorkerValidCells validCellsMove = null;

        for (WorkerValidCells c : validCellsForMove) {
            if (c.getwR() == workerRow && c.getwC() == workerColumn) {
                validCellsMove = c;
                break;
            }
        }

        inputSet = false;
        String cellCoordinate;

        do {
            this.printBoard();
            System.out.println("Choose the cell where you want to move the worker, in the format row,column");
            cellCoordinate = s.nextLine();
            if (cellCoordinate.split(",").length == 2) {
                chosenRow = Integer.parseInt(cellCoordinate.split(",")[0]) - 1;
                chosenColumn = Integer.parseInt(cellCoordinate.split(",")[1]) - 1;
                if (!validCellsMove.contains(chosenRow, chosenColumn)) {
                    System.out.println("Invalid choice");
                } else inputSet = true;
            } else {
                System.out.println("Invalid input format");
            }
        } while (!inputSet);

        //after checking that the move is valid, we just need to bring the colours of the board to their original state and notify the controller about the choice
        this.resetBoard(validCellsMove.getValidPositions());
        this.printBoard();

        MoveCoordinates chosenCoordinates = new MoveCoordinates(workerRow, workerColumn, chosenRow, chosenColumn);
        this.notifyObserver(x -> {
            x.move(chosenCoordinates);
        });
    }


    /**
     * method handling the building actions during the turn
     *
     * @param validForBuild represents the valid cells for the build action of each worker
     * @param validForDome  represents the valid cells to put the dome for each worker
     */
    @Override
    public void requestDomeOrBuild(ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome) {
        int workerRow = -1, workerColumn = -1;
        int chosenRow = -1, chosenColumn = -1;
        ArrayList<Position> tempPositionsBuild = new ArrayList<>();
        ArrayList<Position> tempPositionsDome = new ArrayList<>();

        for (WorkerValidCells c1 : validForBuild) //we highlight the cells of the worker that can only build or that can build and put a dome
        {
            tempPositionsBuild = c1.getValidPositions();
            for (WorkerValidCells c2 : validForDome) {
                if (c2.getwR() == c1.getwR() && c2.getwC() == c1.getwC()) {
                    tempPositionsDome = c2.getValidPositions();
                    break;
                }
            }
            for (Position p1 : tempPositionsBuild) {
                this.getCellOnBoard(p1.getRow(), p1.getColumn()).setCellColour(ColoursForPrinting.red);
            }
            for (Position p2 : tempPositionsDome) {
                if (this.getCellOnBoard(p2.getRow(), p2.getColumn()).getCellColour() == ColoursForPrinting.red)
                    this.getCellOnBoard(p2.getRow(), p2.getColumn()).setCellColour(ColoursForPrinting.green);
                else this.getCellOnBoard(p2.getRow(), p2.getColumn()).setCellColour(ColoursForPrinting.yellow);
            }
            tempPositionsDome = null;
        }

        boolean bothActions = false;
        //now we have to highlight the cells of the remaining workers, the ones who can only put a dome on a cell
        for (WorkerValidCells w1 : validForDome) {
            for (WorkerValidCells w2 : validForBuild) {
                if (w2.getwR() == w1.getwR() && w2.getwC() == w1.getwC()) {
                    bothActions = true;
                    break;
                }
            }
            if (bothActions == false) {
                for (Position pos1 : w1.getValidPositions()) {
                    this.getCellOnBoard(pos1.getRow(), pos1.getColumn()).setCellColour(ColoursForPrinting.yellow);
                }
            }
        }

        Scanner s = new Scanner(System.in);

        boolean inputSet = false;
        String workerCoordinate;
        if (validForBuild.size() > 1 || validForDome.size() > 1 || !(validForBuild.get(0).getwR() == validForDome.get(0).getwR() && validForBuild.get(0).getwC() == validForDome.get(0).getwC())) {
            do {
                this.printBoard();
                System.out.println("Red cells are for building, yellow cells can be domed, green cells can take both actions");
                System.out.println("Choose the worker you want to do the build, in the format row,column");
                workerCoordinate = s.nextLine();
                if (workerCoordinate.split(",").length == 2) {
                    workerRow = Integer.parseInt(workerCoordinate.split(",")[0]) - 1;
                    workerColumn = Integer.parseInt(workerCoordinate.split(",")[1]) - 1;
                    if (!containsWorker(validForBuild, workerRow, workerColumn) && !containsWorker(validForBuild, workerRow, workerColumn)) {
                        System.out.println("Invalid choice");
                    } else inputSet = true;
                } else {
                    System.out.println("Invalid input format");
                }
            } while (!inputSet);
        } else {
            workerRow = validForBuild.get(0).getwR();
            workerColumn = validForBuild.get(0).getwC();
        }


        //now we get the workers that haven't been chosen and colour their cells back to white, only if those cells do not belong to the chosen worker too
        WorkerValidCells validCellsBuild = null;
        WorkerValidCells validCellsDome = null;
        for (WorkerValidCells c : validForBuild) //with these two loops we are looking for the chosen worker in the two lists
        {
            if (c.getwR() == workerRow && c.getwC() == workerColumn) {
                validCellsBuild = c;
                break;
            }
        }

        for (WorkerValidCells c : validForDome) {
            if (c.getwR() == workerRow && c.getwC() == workerColumn) {
                validCellsDome = c;
                break;
            }
        }
        ArrayList<Position> positionsToBuild;
        ArrayList<Position> positionsToDome;
        positionsToBuild = validCellsBuild.getValidPositions(); //we get the valid positions of the chosen worker, which have to remain highlighted
        positionsToDome = validCellsDome.getValidPositions();


        for (WorkerValidCells c : validForBuild) {
            if (c.getwR() != workerRow || c.getwC() != workerColumn) {
                for (Position po : c.getValidPositions()) {
                    if (!positionsToBuild.contains(po) && !positionsToDome.contains(po))
                        this.getCellOnBoard(po.getRow(), po.getColumn()).setCellColour(ColoursForPrinting.white);
                }
            }
        }
        for (WorkerValidCells c : validForDome) {
            if (c.getwR() != workerRow || c.getwC() != workerColumn) {
                for (Position po : c.getValidPositions()) {
                    if (!positionsToBuild.contains(po) && !positionsToDome.contains(po))
                        this.getCellOnBoard(po.getRow(), po.getColumn()).setCellColour(ColoursForPrinting.white);
                }
            }
        }

        //now we ask the player the action he wants to make and check if it is compatible with the chosen worker
        String chosenAction = null;
        String cellCoordinate;

        do {
            this.printBoard();
            System.out.println("Red cells are for building, yellow cells can be domed, green cells can take both actions");
            System.out.println("Choose the cell where you want to do the build, in the format row,column");
            cellCoordinate = s.nextLine();
            if (cellCoordinate.split(",").length == 2) {
                chosenRow = Integer.parseInt(cellCoordinate.split(",")[0]) - 1;
                chosenColumn = Integer.parseInt(cellCoordinate.split(",")[1]) - 1;
                if (!validCellsBuild.contains(chosenRow, chosenColumn) && !validCellsDome.contains(chosenRow, chosenColumn)) {
                    System.out.println("Invalid choice");
                } else inputSet = true;
            } else {
                System.out.println("Invalid input format");
            }
        } while (!inputSet);


        //check if he can both dome and build, or only one of them
        boolean build = false;
        boolean dome = false;

        if (validCellsBuild != null) {
            for (Position p : validCellsBuild.getValidPositions()) {
                if (p.getRow() == chosenRow && p.getColumn() == chosenColumn) {
                    build = true;
                    break;
                }
            }
        }

        if (validCellsBuild != null) {
            for (Position p : validCellsDome.getValidPositions()) {
                if (p.getRow() == chosenRow && p.getColumn() == chosenColumn) {
                    dome = true;
                    break;
                }
            }
        }

        String input;
        action whatToDO = null;
        if (build && dome) {
            boolean completed = false;
            do {
                System.out.println("Enter d if you want to dome on the selected cell, or b if you want to build");
                input = s.nextLine();
                if (!input.equals("d") && !input.equals("b")) System.out.println("Invalid input. Retry");
                else {
                    if (input.equals("d")) whatToDO = action.dome;
                    else whatToDO = action.build;
                    completed = true;
                }
            } while (!completed);

        } else if (build) whatToDO = action.build;
        else if (dome) whatToDO = action.dome;

        this.resetBoard(positionsToBuild);
        this.resetBoard(positionsToDome);
        this.printBoard();

        MoveCoordinates move = new MoveCoordinates(workerRow, workerColumn, chosenRow, chosenColumn);
        if (whatToDO == action.build) notifyObserver((x) -> x.build(move));
        else notifyObserver((x) -> x.dome(move));
    }

    @Override
    public void endgame(String messageOfEndGame) {
        System.out.println(messageOfEndGame);
        cA.shutDown();
    }


    /**
     * Method used to request a player to choose a divinity.
     *
     * @param availableDivinities the list of the available divinities.
     */
    @Override
    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {
        String input;
        Scanner s = new Scanner(System.in);
        boolean validDivinity = false;
        do {
            System.out.println("Choose your divinity out of the following list: ");
            for (DivinitiesWithDescription d : availableDivinities) {
                System.out.println(d.getName() + ": " + d.getDescription());
            }
            input = s.nextLine();
            for (DivinitiesWithDescription d : availableDivinities) {
                if (input.equals(d.getName())) {
                    validDivinity = true;
                    availableDivinities.remove(d);
                    break;
                }
            }
            if (!validDivinity) {
                System.out.println("Divinity not valid or no more available, please retry.");
            }
        } while (!validDivinity);
        System.out.println("You selected: " + input);
        String selectedDivinity = input;
        notifyObserver(x -> x.registerPlayerDivinity(selectedDivinity));
    }

    /**
     * Method used to request the selection of the initial player
     *
     * @param players the list of the available players
     */
    @Override
    public void requestInitialPlayerSelection(ArrayList<String> players) {
        Scanner s = new Scanner(System.in);
        String input;
        boolean validName = false;
        do {
            System.out.println("Select a player out of the following: ");
            for (String str : players) {
                System.out.println(str);
            }
            input = s.nextLine();
            if (players.contains(input)) {
                validName = true;
            } else {
                System.out.println("Player not valid, please retry.");
            }
        } while (!validName);
        System.out.println("You selected: " + input);
        String firstPlayer = input;
        notifyObserver(x -> x.selectFirstPlayer(firstPlayer));
    }


    /**
     * method that handles the initial positioning of each worker on the board, showing the valid cells to the players and taking their choices
     *
     * @param validCells is the list of valid cells for the worker we are currently positioning
     */
    @Override
    public void requestInitialPositioning(ArrayList<Position> validCells) {
        int chosenRow = -1, chosenColumn = -1;

        //first we need to highlight each of the valid cells to the player
        for (Position p : validCells) {
            this.getCellOnBoard(p.getRow(), p.getColumn()).setCellColour(ColoursForPrinting.red);
        }

        //after that, we need to ask the player their choice
        Scanner s = new Scanner(System.in);
        String workerCoordinate;
        boolean inputSet = false;
        do {
            this.printBoard();
            System.out.println("Choose the cell where you want to put your worker on, in the format row,column");
            workerCoordinate = s.nextLine();
            if (workerCoordinate.split(",").length == 2) {
                chosenRow = Integer.parseInt(workerCoordinate.split(",")[0]) - 1;
                chosenColumn = Integer.parseInt(workerCoordinate.split(",")[1]) - 1;
                if (!validCells.contains(new Position(chosenRow, chosenColumn))) {
                    System.out.println("Invalid choice");
                } else inputSet = true;
            } else {
                System.out.println("Invalid input format");
            }
        } while (!inputSet);

        //after checking that the player has chosen a valid cell, we need to bring the board back to its original colours and notify the observers
        this.resetBoard(validCells);
        this.printBoard();

        Position notifiedPosition = new Position(chosenRow, chosenColumn);
        this.notifyObserver(x -> {
            x.putWorkerOnTable(notifiedPosition);
        });
    }

    /**
     * method used to request a player to pick the divinities for other players
     *
     * @param div          the list of the available divinities
     * @param playerNumber the number of the players in the game
     */
    @Override
    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber) {
        String input;
        Scanner s = new Scanner(System.in);
        ArrayList<DivinitiesWithDescription> selectedDivinities = new ArrayList<DivinitiesWithDescription>();
        boolean validDivinity = false;
        int n = playerNumber;
        do {
            System.out.println("Choose " + n + "divinities out of the following:");
            for (DivinitiesWithDescription d : div) {
                System.out.println(d.getName() + ": " + d.getDescription());
            }
            input = s.nextLine();
            for (DivinitiesWithDescription d : div) {
                if (input.equals(d.getName())) {
                    validDivinity = true;
                    selectedDivinities.add(d);
                    div.remove(d);
                    n--;
                    break;
                }
            }
            if (!validDivinity) {
                System.out.println("Divinity not valid or no more available, please retry.");
            }
        } while (!validDivinity || n > 0);
        System.out.println("You selected: ");
        for (DivinitiesWithDescription d : selectedDivinities) {
            System.out.println(d.getName() + " ");
        }
        ArrayList<String> selected = new ArrayList<>();
        selectedDivinities.forEach(d -> selected.add(d.getName()));
        notifyObserver(x -> x.selectAvailableDivinities(selected));
    }

    @Override
    public void printMessage(String s) {
        System.out.println(s);
    }


    /**
     * method handling the optional move actions of the player
     *
     * @param validCellsForMove is the list of cells where the player can choose to make another optional move
     */
    @Override
    public void requestOptionalMove(ArrayList<WorkerValidCells> validCellsForMove) {
        ArrayList<Position> tempList;
        int workerRow = -1, workerColumn = -1;
        int chosenRow = -1, chosenColumn = -1;

        for (WorkerValidCells c : validCellsForMove) {
            tempList = c.getValidPositions(); //each time we retrieve the list of valid positions for the move action
            for (Position p : tempList) // for each of the valid positions, we need to highlight the cells on the board
            {
                this.getCellOnBoard(p.getRow(), p.getColumn()).setCellColour(ColoursForPrinting.red);
            }
        }
        this.printBoard();

        //after highlighting the cells to the player, we ask him if he wants to move or to skip the action
        String chosenAction = null;
        Scanner s = new Scanner(System.in);
        boolean completed = false;
        do {
            System.out.println("Write skip if you want to skip the optional move action, move if you want to do the optional move: ");
            if (s.hasNext()) {
                chosenAction = s.nextLine();
            }

            if (chosenAction.equals("skip")) //the player has chosen to skip so we can just notify the server
            {
                MoveCoordinates chosenCoordinates = new MoveCoordinates(workerRow, workerColumn, chosenRow, chosenColumn);
                this.notifyObserver(x -> {
                    x.move(chosenCoordinates);
                });
                completed = true;
            } else if (chosenAction.equals("move")) {
                this.requestMove(validCellsForMove);
                completed = true;
            } else {
                System.out.println("Invalid choice");
            }
        } while (!completed);
    }


    /**
     * method handling the optional construction operations during the turn
     *
     * @param build is the list of valid cells for the building operations
     * @param dome  is the list of valid cells for the dome operations
     */
    @Override
    public void requestOptionalBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {
        int workerRow = -1, workerColumn = -1;
        int chosenRow = -1, chosenColumn = -1;
        ArrayList<Position> tempPositionsBuild = new ArrayList<>();
        ArrayList<Position> tempPositionsDome = new ArrayList<>();

        for (WorkerValidCells c1 : build) {
            tempPositionsBuild = c1.getValidPositions();
            for (WorkerValidCells c2 : dome) {
                if (c2.getwR() == c1.getwR() && c2.getwC() == c1.getwC()) {
                    tempPositionsDome = c2.getValidPositions();
                    break;
                }
            }
            for (Position p1 : tempPositionsBuild) {
                this.getCellOnBoard(p1.getRow(), p1.getColumn()).setCellColour(ColoursForPrinting.red);
            }
            for (Position p2 : tempPositionsDome) {
                if (this.getCellOnBoard(p2.getRow(), p2.getColumn()).getCellColour() == ColoursForPrinting.red)
                    this.getCellOnBoard(p2.getRow(), p2.getColumn()).setCellColour(ColoursForPrinting.green);
                else this.getCellOnBoard(p2.getRow(), p2.getColumn()).setCellColour(ColoursForPrinting.yellow);
            }
            tempPositionsDome = null;
        }

        boolean bothActions = false;
        for (WorkerValidCells w1 : dome) {
            for (WorkerValidCells w2 : build) {
                if (w2.getwR() == w1.getwR() && w2.getwC() == w1.getwC()) {
                    bothActions = true;
                    break;
                }
            }
            if (bothActions == false) {
                for (Position pos1 : w1.getValidPositions()) {
                    this.getCellOnBoard(pos1.getRow(), pos1.getColumn()).setCellColour(ColoursForPrinting.yellow);
                }
            }
        }
        this.printBoard();

        boolean completed = false;

        do {
            String chosenAction = null;
            Scanner s = new Scanner(System.in);
            System.out.println("Write skip if you want to skip the optional construction actions, build if you want to do the build: ");
            if (s.hasNext()) {
                chosenAction = s.nextLine();
            }

            if (chosenAction.equals("skip")) //the player has chosen to skip so we can just notify the server
            {
                MoveCoordinates chosenCoordinates = new MoveCoordinates(workerRow, workerColumn, chosenRow, chosenColumn);
                this.notifyObserver(x -> {
                    x.build(chosenCoordinates);
                });
                completed = true;
            } else if (chosenAction.equals("build")) {
                this.requestDomeOrBuild(build, dome);
                completed = true;
            } else {
                System.out.println("Invalid choice");
            }
        } while (!completed);
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


        try {
            server = new Socket(serverIP, 7777);
        } catch (IOException e) {
            System.out.println("Can't connect to the Server. Aborting.");
            return;
        }

        cA = new ClientNetworkOutcoming(server);
        Thread cAThread = new Thread(cA);
        cAThread.start();
        this.registerObserver(cA);

        ClientNetworkIncoming cI = new ClientNetworkIncoming(this, server);
        cI.addObserver(this);
        Thread cIThread = new Thread(cI);
        cIThread.start();


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
            System.out.println("logged to the server. initializing message listener");
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
        if (result.equals("Not valid mode. Retry") || result.equals("Missing Birthday. Retry")) completedAction = false;
        else completedAction = true;
        notifyAll();
    }

    public boolean containsWorker(ArrayList<WorkerValidCells> arr, int row, int column) {
        for (WorkerValidCells v : arr) {
            if (v.getwR() == row && v.getwC() == column) return true;
        }
        return false;
    }

    private enum action {
        build, dome;
    }
}