package it.polimi.ingsw.PSP48.client.view;

import it.polimi.ingsw.PSP48.client.View;
import it.polimi.ingsw.PSP48.client.ViewObserver;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.Consumer;

/**
 * concrete class that represents all the interactions with the client
 */
public class ClientData extends View
{
    private static final CellForPrinting[][] gameBoard = new CellForPrinting[5][5]; //class attributes
    private ArrayList<Player> playerList= new ArrayList<Player>();

    public ClientData(ColoursForPrinting colour) //class constructor initialising the game board (to do it we just need the colour we want to assign to the cells)
    {
        for(int i=0; i<5; i++)
        {
            for(int j=0; j<5; j++)
            {
                gameBoard[i][j]= new CellForPrinting(i, j, colour);
            }
        }
    }

    public CellForPrinting[][] getGameBoard()
    {
        return(this.gameBoard);
    }

    public CellForPrinting getCellOnBoard (int row, int column) //method used to immediately retrieve a cell from the board
    {
        return(gameBoard[row][column]);
    }

    public void printBoard() //method used to print the board
    {
        for(int i=0; i<5; i++)
        {
            for (int j=0; j<5; j++)
            {
                gameBoard[i][j].printCellOnScreen();
            }
            System.out.print("\n");
        }
    }

    public ArrayList<Player> getPlayerList()
    {
        return(this.playerList);
    }

    @Override
    public void changedBoard(ArrayList<Cell> newCells) //method applying changes to the game board after every action by the player
    {
        CellForPrinting tempCell;
        ArrayList<Player> list;
        Player tempPlayer;

        for (Cell c : newCells)
        {
            tempCell=this.getCellOnBoard(c.getRow(), c.getColumn()); //retrieved cell from the board on the client side, now we have to update its values

            //first we check the dome field of the cell
            if (c.isDomed()==false) tempCell.setDome(null);
            else
            {
                if (tempCell.getDome()==null) tempCell.setDome(new DomeForPrinting(ColoursForPrinting.blue));
            }
            //then we check the building field of the cell
            if (c.getLevel()==0) tempCell.setBuildings(null);
            else
            {
                if (tempCell.getBuildings()==null) tempCell.setBuildings(new BuildingsForPrinting(c.getLevel(), ColoursForPrinting.blue));

                else tempCell.getBuildings().setLevel(c.getLevel());
            }

            //lastly, we check the player field of the cell
            if (c.getPlayer()==null) tempCell.setPlayerOnCell(null);
            else
            {
                list=this.getPlayerList(); //we have to search for the right player in the list, in order to have the parameters to put in the cell
                for (Player p : list)
                {
                    if (p.getName()==c.getPlayer())
                    {
                        tempPlayer=p;
                        break;
                    }
                }

                if (tempCell.getPlayerOnCell()==null) tempCell.setPlayerOnCell(new Player(tempPlayer.getName(), tempPlayer.getPlayerColour(), tempPlayer.getDivinity()));
                else
                {
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
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove)
    {
        ArrayList<Position> tempList;
        ColoursForPrinting cellColour;
        WorkerValidCells notChosenPosition, temp;
        int workerRow, workerColumn;
        int chosenRow, chosenColumn;

        for (WorkerValidCells c : validCellsForMove)
        {
            tempList=c.getValidPositions(); //each time we retrieve the list of valid positions for the move action

            for(Position p : tempList) // for each of the valid positions, we need to highlight the cells on the board
            {
                cellColour=this.getCellOnBoard(p.getRow(), p.getColumn()).getCellColour();
                cellColour=ColoursForPrinting.red;
            }
        }
        this.printBoard(); //after highlighting all the cells, we need to print the whole board

        //first we ask the player which one of his workers he wants to move
        Scanner s = new Scanner(System.in);
        System.out.println("Choose the row of the worker you want to move: ");
        while(s.hasNext())
        {
            workerRow=((s.nextInt())-1);
        }
        System.out.println("Choose the column of the worker you want to move: ");
        while(s.hasNext())
        {
            workerColumn=((s.nextInt())-1);
        }

        //after reading the commands, we need to check if the player has chosen a valid worker
        boolean valid=false;
        do {
            for(int i=0; (i<validCellsForMove.size() && valid==false); i++)
            {
                if(workerRow==validCellsForMove.get(i).getwr() && workerColumn==validCellsForMove.get(i).getwC())
                {
                    valid=true;
                }
            }
            if (valid==false)
            {
                System.out.println("You need to choose a valid worker!!");
                System.out.println("Choose the row of the worker you want to move: ");
                while(s.hasNext())
                {
                    workerRow=((s.nextInt())-1);
                }
                System.out.println("Choose the column of the worker you want to move: ");
                while(s.hasNext())
                {
                    workerColumn=((s.nextInt())-1);
                }
            }

        } while(valid==false);

        //after we make sure that the player has chosen a valid worker to move, we highlight again the board, but only with the cells of that worker
        for (WorkerValidCells c : validCellsForMove)
        {
            if (c.getwR()!=workerRow && c.getwC()!=workerColumn)
            {
                notChosenPosition=c;
                break;
            }
        }
        for (Position po : notChosenPosition.getValidPositions())
        {
            cellColour=this.getCellOnBoard(po.getRow(), po.getColumn()).getCellColour();
            cellColour=ColoursForPrinting.white;
        }
        this.printBoard();

        //now we ask to the player the move he wants to make
        System.out.println("Choose the row where you want to move, among the red ones: ");
        while(s.hasNext())
        {
            chosenRow=((s.nextInt())-1);
        }
        System.out.println("Choose the column where you want to move, among the red ones: ");
        while(s.hasNext())
        {
            chosenColumn=((s.nextInt())-1);
        }

        //we have to check if the player has chosen a valid cell
        boolean validMove=false;
        do {
            for (WorkerValidCells c : validCellsForMove)
            {
                if (c!=notChosenPosition)
                {
                    temp=c;
                    break;
                }
            }
            tempList=temp.getValidPositions();
            for(int i=0; (i<tempList.size() && validMove==false); i++)
            {
                if (chosenRow==tempList.get(i).getRow() && chosenColumn==tempList.get(i).getColumn())
                {
                    validMove=true;
                }
            }
            if (validMove==false)
            {
                System.out.println("You need to choose a valid cell!!");
                System.out.println("Choose the row where you want to move, among the red ones: ");
                while(s.hasNext())
                {
                    workerRow=((s.nextInt())-1);
                }
                System.out.println("Choose the column where you want to move, among the red ones: ");
                while(s.hasNext())
                {
                    workerColumn=((s.nextInt())-1);
                }
            }
        } while(validMove=false);

        //after checking that the move is valid, we just need to bring the colours of the board to their original state and notify the controller about the choice
        for (WorkerValidCells c : validCellsForMove)
        {
            if (c!=notChosenPosition)
            {
                temp=c;
                break;
            }
        }
        tempList=temp.getValidPositions();
        for(Position p : tempList)
        {
            cellColour=this.getCellOnBoard(p.getRow(), p.getColumn()).getCellColour();
            cellColour=ColoursForPrinting.white;
        }
        this.printBoard();
    }

    @Override
    public void requestDomeOrBuild(ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome) {

    }

    @Override
    public void declareWin() {

    }

    @Override
    public void declareLose()
    {
        System.out.println(" You lost the game! :( ");
    }

    @Override
    public void requestColourSelection(ArrayList<String> availableColours) {

    }

    @Override
    public void requestDivinityList(ArrayList<String> availableDivinities) {

    }

    @Override
    public void requestDivinitySelection(ArrayList<String> availableDivinities) {

    }

    @Override
    public void requestInitialPositioning(ArrayList<WorkerValidCells> validForInitialPositioning) {

    }

    @Override
    public void registerObserver(ViewObserver obv) {

    }

    @Override
    public void unregisterObserver(ViewObserver obv) {

    }

    @Override
    public void notifyObservers(Consumer<ViewObserver> lambda) {

    }
}
