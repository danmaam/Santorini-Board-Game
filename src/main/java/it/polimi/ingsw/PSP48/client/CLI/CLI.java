package it.polimi.ingsw.PSP48.client.CLI;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.client.networkmanager.ClientNetworkIncoming;
import it.polimi.ingsw.PSP48.client.networkmanager.ClientNetworkOutcoming;
import it.polimi.ingsw.PSP48.observers.ClientNetworkObserver;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import it.polimi.ingsw.PSP48.server.model.ActionCoordinates;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * concrete class that represents all the interactions with the client
 *
 * @author Rebecca Marelli, Annalaura Massa, Daniele Mammone
 */
public class CLI implements Runnable, ViewInterface, ClientNetworkObserver {
    ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    /**
     * private attribute of the CLI class that represents the board of the game and its content
     */
    private final CellForPrinting[][] gameBoard = new CellForPrinting[5][5];
    /**
     * private attribute of the CLI class that contains all the players of a match
     */
    private ArrayList<Player> playerList = new ArrayList<>();
    /**
     * private attribute of the CLI class containing all the observers to notify
     */
    private final ArrayList<ViewObserver> observers = new ArrayList<>();


    /**
     * method used to register a new observer that we'll need to notify
     *
     * @param obv is the observer of the client that we are going to register
     */
    public void registerObserver(ViewObserver obv) {
        observers.add(obv);
    }

    /**
     * method used to remove an already existing observer from the list of view observers
     *
     * @param obv is the observer we need to remove
     */
    public void unregisterObserver(ViewObserver obv) {
        observers.remove(obv);
    }

    /**
     * method used to notify a change in the client status to its observers (and so to the server)
     *
     * @param lambda is the lambda function we use to notify the observer
     */
    public void notifyObserver(Consumer<ViewObserver> lambda) {
        for (ViewObserver obv : observers) lambda.accept(obv);
    }

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
        threadExecutor.submit(() -> {
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
        });
    }

    /**
     * method used to update the player list in the client, whenever it is changed
     *
     * @param newPlayerList is the updated player list
     */
    @Override
    public void changedPlayerList(ArrayList<String> newPlayerList) {
        threadExecutor.submit(() -> {
            ArrayList<Player> newList = new ArrayList<>();
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
        });

    }


    /**
     * method used to request a move action to the player, showing the valid options and then taking the choice of the player
     *
     * @param validCellsForMove contains the positions of both workers and for each of them the valid cells for the move action
     */
    @Override
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove) {
        threadExecutor.submit(() -> {
            ArrayList<Position> tempList;
            int workerRow = -1, workerColumn = -1;
            int chosenRow = -1, chosenColumn = -1;
            WorkerValidCells validCellsMove = null;

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
                        validCellsMove = containsWorker(validCellsForMove, workerRow, workerColumn);
                        if (validCellsMove == null) {
                            System.out.println("Invalid choice");
                        } else inputSet = true;
                    } else {
                        System.out.println("Invalid input format");
                    }
                } while (!inputSet);
            } else {
                workerRow = validCellsForMove.get(0).getwR();
                workerColumn = validCellsForMove.get(0).getwC();
                validCellsMove = validCellsForMove.get(0);
            }

            //after we make sure that the player has chosen a valid worker to move, we highlight again the board, but only with the cells of that worker
            if (validCellsForMove.size() > 1) //if the player can only move one worker, the cells of that worker have already been highlighted
            {
                for (WorkerValidCells c : validCellsForMove) {
                    resetBoard(c.getValidPositions()); //we reset the colour of all the cells of the workers
                }
                for (WorkerValidCells cell : validCellsForMove) //after that, we highlight again the cells, but only the ones belonging to the chosen worker
                {
                    if (cell.getwR() == workerRow && cell.getwC() == workerColumn) {
                        for (Position p : cell.getValidPositions()) {
                            this.getCellOnBoard(p.getRow(), p.getColumn()).setCellColour(ColoursForPrinting.red);
                        }
                        break; //after finding the correct worker and highlighting his cells, we can interrupt our search
                    }
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

            ActionCoordinates chosenCoordinates = new ActionCoordinates(workerRow, workerColumn, chosenRow, chosenColumn);
            this.notifyObserver(x -> x.move(chosenCoordinates));
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
        threadExecutor.submit(() -> {
            WorkerValidCells validCellsBuild = null;
            WorkerValidCells validCellsDome = null;
            int workerRow = -1, workerColumn = -1;
            int chosenRow = -1, chosenColumn = -1;
            ArrayList<Position> tempPositionsBuild;
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

                if (tempPositionsDome != null) for (Position p2 : tempPositionsDome) {
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
                if (!bothActions) {
                    for (Position pos1 : w1.getValidPositions()) {
                        this.getCellOnBoard(pos1.getRow(), pos1.getColumn()).setCellColour(ColoursForPrinting.yellow);
                    }
                }
            }

            Scanner s = new Scanner(System.in);

            boolean inputSet = false;
            String workerCoordinate;


            if (validForBuild.size() > 1 || validForDome.size() > 1 || (validForBuild.size() > 0 && validForDome.size() > 0 && !(validForBuild.get(0).getwR() == validForDome.get(0).getwR() && validForBuild.get(0).getwC() == validForDome.get(0).getwC()))) {
                do {
                    this.printBoard();
                    System.out.println("Red cells are for building, yellow cells can be domed, green cells can take both actions");
                    System.out.println("Choose the worker you want to do the build, in the format row,column");
                    workerCoordinate = s.nextLine();
                    if (workerCoordinate.split(",").length == 2) {
                        workerRow = Integer.parseInt(workerCoordinate.split(",")[0]) - 1;
                        workerColumn = Integer.parseInt(workerCoordinate.split(",")[1]) - 1;
                        validCellsBuild = containsWorker(validForBuild, workerRow, workerColumn);
                        validCellsDome = containsWorker(validForDome, workerRow, workerColumn);

                        if (validCellsBuild == null && validCellsDome == null) {
                            System.out.println("Invalid choice");
                        } else inputSet = true;
                    } else {
                        System.out.println("Invalid input format");
                    }
                } while (!inputSet);
            } else {
                workerRow = validForBuild.get(0).getwR();
                workerColumn = validForBuild.get(0).getwC();
                validCellsBuild = containsWorker(validForBuild, workerRow, workerColumn);
                validCellsDome = containsWorker(validForDome, workerRow, workerColumn);
            }


            //now we get the workers that haven't been chosen and colour their cells back to white, only if those cells do not belong to the chosen worker too
            for (WorkerValidCells c : validForBuild) {
                resetBoard(c.getValidPositions()); //we reset the colour of all the cells, then we proceed to only highlight the ones of the chosen worker
            }
            for (WorkerValidCells c : validForDome) {
                resetBoard(c.getValidPositions());
            }

            for (WorkerValidCells c1 : validForBuild) //we first need to look for the chosen worker in both lists
            {
                if (c1.getwR() == workerRow && c1.getwC() == workerColumn) {
                    tempPositionsBuild = c1.getValidPositions();
                    for (WorkerValidCells c2 : validForDome) {
                        if (c2.getwR() == workerRow && c2.getwC() == workerColumn) {
                            tempPositionsDome = c2.getValidPositions();
                            break;
                        }
                    }
                    for (Position p1 : tempPositionsBuild) {
                        this.getCellOnBoard(p1.getRow(), p1.getColumn()).setCellColour(ColoursForPrinting.red);
                    }
                    if (tempPositionsDome != null) for (Position p2 : tempPositionsDome) {
                        if (this.getCellOnBoard(p2.getRow(), p2.getColumn()).getCellColour() == ColoursForPrinting.red)
                            this.getCellOnBoard(p2.getRow(), p2.getColumn()).setCellColour(ColoursForPrinting.green);
                        else this.getCellOnBoard(p2.getRow(), p2.getColumn()).setCellColour(ColoursForPrinting.yellow);
                    }

                    break;
                }
            }

            bothActions = false;
            for (WorkerValidCells c1 : validForDome) //the worker might only be able to do the dome, so we have to make this check to highlight the cells correctly
            {
                if (c1.getwR() == workerRow && c1.getwC() == workerColumn) {
                    tempPositionsDome = c1.getValidPositions();
                    for (WorkerValidCells c2 : validForBuild) {
                        if (c2.getwR() == workerRow && c2.getwC() == workerColumn) {
                            bothActions = true;
                            break;
                        }
                    }
                    if (!bothActions) {
                        for (Position p : tempPositionsDome) {
                            this.getCellOnBoard(p.getRow(), p.getColumn()).setCellColour(ColoursForPrinting.yellow);
                        }
                        break;
                    } else {
                        break;
                    }
                }
            }

            //now we ask the player the action he wants to make and check if it is compatible with the chosen worker
            String cellCoordinate;


            inputSet = false;
            do {
                this.printBoard();
                System.out.println("Red cells are for building, yellow cells can be domed, green cells can take both actions");
                System.out.println("Choose the cell where you want to do the build, in the format row,column");
                cellCoordinate = s.nextLine();
                if (cellCoordinate.split(",").length == 2) {
                    chosenRow = Integer.parseInt(cellCoordinate.split(",")[0]) - 1;
                    chosenColumn = Integer.parseInt(cellCoordinate.split(",")[1]) - 1;
                    if (!(validCellsBuild != null && validCellsBuild.contains(chosenRow, chosenColumn) || (validCellsDome != null && validCellsDome.contains(chosenRow, chosenColumn)))) {
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

            if (validCellsDome != null) {
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

            if (validCellsBuild != null) this.resetBoard(validCellsBuild.getValidPositions());
            if (validCellsDome != null) this.resetBoard(validCellsDome.getValidPositions());
            this.printBoard();

            ActionCoordinates move = new ActionCoordinates(workerRow, workerColumn, chosenRow, chosenColumn);
            if (whatToDO == action.build) notifyObserver((x) -> x.build(move));
            else notifyObserver((x) -> x.dome(move));
        });


    }

    /**
     * method printing a message to the client notifying the end of a match
     *
     * @param messageOfEndGame is the message we need to print
     */
    @Override
    public void endgame(String messageOfEndGame) {
        if (!threadExecutor.isShutdown()) threadExecutor.submit(() -> {
            System.out.println(messageOfEndGame);
            System.exit(0);
        });

    }


    /**
     * Method used to request a player to choose a divinity.
     *
     * @param availableDivinities the list of the available divinities.
     */
    @Override
    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {
        threadExecutor.submit(() -> {
            String input;
            Scanner s = new Scanner(System.in);
            boolean validDivinity = false;
            if (availableDivinities.size() == 1) {
                notifyObserver(x -> x.registerPlayerDivinity(availableDivinities.get(0).getName()));
                return;
            }
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
        });

    }

    /**
     * Method used to request the selection of the initial player
     *
     * @param players the list of the available players
     */
    @Override
    public void requestInitialPlayerSelection(ArrayList<String> players) {
        threadExecutor.submit(() -> {
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
        });

    }


    /**
     * method that handles the initial positioning of each worker on the board, showing the valid cells to the players and taking their choices
     *
     * @param validCells is the list of valid cells for the worker we are currently positioning
     */
    @Override
    public void requestInitialPositioning(ArrayList<Position> validCells) {
        threadExecutor.submit(() -> {
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
            this.notifyObserver(x -> x.putWorkerOnTable(notifiedPosition));
        });

    }

    /**
     * method used to request to a player the selection of the divinities for all players
     *
     * @param div          the list of the available divinities
     * @param playerNumber the number of the players in the game
     */
    @Override
    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div, int playerNumber) {
        threadExecutor.submit(() -> {
            String input;
            Scanner s = new Scanner(System.in);
            ArrayList<DivinitiesWithDescription> selectedDivinities = new ArrayList<>();
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
        });

    }

    /**
     * method printing a certain message for a player during the game
     *
     * @param s is the string containing the message we need to print
     */
    @Override
    public void printMessage(String s) {
        threadExecutor.submit(() -> System.out.println(s));
    }


    /**
     * method handling the optional move actions of the player
     *
     * @param validCellsForMove is the list of cells where the player can choose to make another optional move
     */
    @Override
    public void requestOptionalMove(ArrayList<WorkerValidCells> validCellsForMove) {
        threadExecutor.submit(() -> {
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

            Scanner s = new Scanner(System.in);
            boolean completed = false;
            do {
                System.out.println("Write skip if you want to skip the optional move action, move if you want to do the optional move: ");

                String chosenAction = s.nextLine();


                if (chosenAction.equals("skip")) //the player has chosen to skip so we can just notify the server
                {
                    ActionCoordinates chosenCoordinates = new ActionCoordinates(workerRow, workerColumn, chosenRow, chosenColumn);
                    for (WorkerValidCells c : validCellsForMove) {
                        resetBoard(c.getValidPositions());
                    }
                    this.printBoard();
                    this.notifyObserver(x -> x.move(chosenCoordinates));
                    completed = true;
                } else if (chosenAction.equals("move")) {
                    this.requestMove(validCellsForMove);
                    completed = true;
                } else {
                    System.out.println("Invalid choice");
                }
            } while (!completed);
        });

    }


    /**
     * method handling the optional construction operations during the turn
     *
     * @param build is the list of valid cells for the building operations
     * @param dome  is the list of valid cells for the dome operations
     */
    @Override
    public void requestOptionalBuild(ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {
        threadExecutor.submit(() -> {
            int workerRow = -1, workerColumn = -1;
            int chosenRow = -1, chosenColumn = -1;
            ArrayList<Position> tempPositionsBuild;
            ArrayList<Position> tempPositionsDome = null;

            for (WorkerValidCells c1 : build) {
                tempPositionsBuild = c1.getValidPositions();
                for (WorkerValidCells c2 : dome) {
                    if (c2.getwR() == c1.getwR() && c2.getwC() == c1.getwC()) {
                        tempPositionsDome = c2.getValidPositions();
                        break;
                    }
                }
                if (tempPositionsBuild != null) for (Position p1 : tempPositionsBuild) {
                    this.getCellOnBoard(p1.getRow(), p1.getColumn()).setCellColour(ColoursForPrinting.red);
                }
                if (tempPositionsDome != null) for (Position p2 : tempPositionsDome) {
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
                if (!bothActions) {
                    for (Position pos1 : w1.getValidPositions()) {
                        this.getCellOnBoard(pos1.getRow(), pos1.getColumn()).setCellColour(ColoursForPrinting.yellow);
                    }
                }
            }
            this.printBoard();

            boolean completed = false;

            do {
                Scanner s = new Scanner(System.in);
                System.out.println("Write skip if you want to skip the optional construction actions, build if you want to do the build: ");
                String chosenAction = s.nextLine();


                if (chosenAction.equals("skip")) //the player has chosen to skip so we can just notify the server
                {
                    ActionCoordinates chosenCoordinates = new ActionCoordinates(workerRow, workerColumn, chosenRow, chosenColumn);
                    for (WorkerValidCells c1 : build) {
                        resetBoard(c1.getValidPositions());
                    }
                    for (WorkerValidCells c2 : dome) {
                        resetBoard(c2.getValidPositions());
                    }
                    this.printBoard();
                    this.notifyObserver(x ->
                            x.build(chosenCoordinates)
                    );
                    completed = true;
                } else if (chosenAction.equals("build")) {
                    this.requestDomeOrBuild(build, dome);
                    completed = true;
                } else {
                    System.out.println("Invalid choice");
                }
            } while (!completed);
        });

    }

    /**
     * Starts The Player Client, and initialize the network connection
     *
     * @author Daniele Mammone
     */
    @Override
    public void run() {
        String logo = "   _____                 _________         _____   _             _\n" +
                "  /     \\    __    _   _|___    __|__    /   _   \\|_|  _   ___  |_|\n" +
                " |    /\\/   /  \\  |  \\/   \\ |  | /___ \\  |  |_)  | _  |  \\/   \\  _\n" +
                "  \\   \\    / /\\ \\ |   /\\   ||  |/ __ \\ \\ |      / | | |   /\\   || |\n" +
                "/\\ \\   \\  / /__\\ \\\\  |  |  ||  | | |_/| ||  |\\  \\ | | |  |  |  || |\n" +
                "\\ \\_|   |/   __   \\\\ |  |  ||  |\\ \\__/  /|  | \\  \\| |_|  |  |  || |_\n" +
                " \\      |   /  \\   \\||  |__/\\  | \\_____/ |__\\  \\  \\__/|__|  |__/\\__/\n" +
                "  \\____/|__|    \\___/        \\_|                \\__/";
        System.out.println(logo);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Santorini CLI Mode\nPlease insert the Server IP and press ENTER.");
        String serverIP = scanner.nextLine();


        Socket server;
        try {
            server = new Socket(serverIP, 7777);
        } catch (IOException e) {
            System.out.println("Can't connect to the Server. Aborting.");
            return;
        }

        cA = new ClientNetworkOutcoming(server);
        Thread cAThread = new Thread(cA);

        this.registerObserver(cA);

        ClientNetworkIncoming cI = new ClientNetworkIncoming(this, server);
        cI.setOutHandler(cA);
        cI.addObserver(this);
        Thread cIThread = new Thread(cI);

        cIThread.start();
        cAThread.start();

        System.out.println("Connected to the Santorini Server");
    }


    /**
     * method requesting to a player to write a nickname in order to login and play
     *
     * @param message is the message we print to make our request
     */
    @Override
    public void requestNicknameSend(String message) {
        threadExecutor.submit(() -> {
            String nextMessage;
            System.out.println(message);
            while (true) {
                Scanner scanner = new Scanner(System.in);
                nextMessage = scanner.nextLine();
                if (nextMessage.length() == 0 || nextMessage.contains(".")) {
                    System.out.println("Invalid nickname, since it's empty or contains dots.");
                    System.out.println(message);
                } else break;
            }
            cA.requestNicknameSend(nextMessage);
        });

    }

    /**
     * method sending the request to select a game mode, showing all the possible choices
     *
     * @param message contains the request we make
     */
    @Override
    public void requestGameModeSend(String message) {
        threadExecutor.submit(() -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Choose a game mode");
            System.out.println("2D for two player game with divinities;");
            System.out.println("3D for two player game with divinities;");
            System.out.println("2ND for two player game without divinities");
            System.out.println("3ND for two player game without divinities");
            String nextMessage = scanner.nextLine();
            if (nextMessage.contains("ND")) {
                while (true) {
                    System.out.println("Insert your birthday in the format dd-mm-yyyy");
                    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
                    String birthday = scanner.nextLine();

                    try {
                        Date date = dateformat.parse(birthday);
                        nextMessage = nextMessage + " " + birthday;
                        break;
                    } catch (ParseException e) {
                        System.out.println("The date is invalid. Retry");
                    }
                }

            }
            cA.setGameMode(nextMessage);
        });

    }

    /**
     * Notifies the client the setup phase has been completed.
     *
     * @param message the message of completed setup
     */
    @Override
    public void completedSetup(String message) {
        threadExecutor.submit(() -> System.out.println(message));
    }

    /**
     * method used in the functions handling the game board, to see if a certain selected position is among the valid ones
     *
     * @param arr    is the list containing the valid worker positions
     * @param row    is the row selected by the player for the worker to use
     * @param column is the column selected by the player for the worker to use
     * @return true if the worker is valid, else false
     */
    public WorkerValidCells containsWorker(ArrayList<WorkerValidCells> arr, int row, int column) {
        for (WorkerValidCells v : arr) {
            if (v.getwR() == row && v.getwC() == column && !v.getValidPositions().isEmpty()) return v;
        }
        return null;
    }

    /**
     * attribute used to handle the building operations on the board
     */
    private enum action {
        build, dome
    }
}