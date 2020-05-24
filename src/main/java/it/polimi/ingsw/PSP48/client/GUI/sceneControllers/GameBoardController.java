package it.polimi.ingsw.PSP48.client.GUI.sceneControllers;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.client.GUI.GUI;
import it.polimi.ingsw.PSP48.server.MoveCoordinates;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.math.*;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameBoardController {
    public GameBoardController(GUI view) {
        this.view = view;
    }

    private final GUI view;

    @FXML
    private Text gameMessage;
    @FXML
    private GridPane boardPane;
    @FXML
    private GridPane playersPane;
    @FXML
    private Pane multifunctionalPane;
    @FXML
    private ImageView thirdPlayerCard;
    @FXML
    private ImageView thirdPlayerBg;
    @FXML
    private Pane leftPane;


    @FXML
    private Text firstPlayerName;
    @FXML
    private Text secondPlayerName;
    @FXML
    private Text thirdPlayerName;

    @FXML
    private ImageView firstPlayerDivinity;
    @FXML
    private ImageView secondPlayerDivinity;
    @FXML
    private ImageView thirdPlayerDivinity;

    @FXML
    private BorderPane mainBorderPane;

    private final ArrayList<ImageView> playersDivinity = new ArrayList<>();
    private final ArrayList<Text> playersName = new ArrayList<>();


    private enum FSM_STATUS {
        positioning, worker_selection_move, worker_selection_build, sendmove, sendbuild
    }

    private Position nextPosition = null;
    private Position workerPosition=null;
    private FSM_STATUS nextState;
    private ArrayList<Position> positionValid;
    private ArrayList<WorkerValidCells> moveValid;
    private ArrayList<WorkerValidCells> buildValid;
    private ArrayList<WorkerValidCells> domeValid;

    private ArrayList<String> playerList;

    private final Image isSelectionImage = new Image("santorini_risorse-grafiche-2/Texture2D/Whirpool.png");
    private final Image workerChoiceImage = new Image ("santorini_risorse-grafiche-2/Texture2D/cloud_puff1.png");

    private final EventHandler<MouseEvent> handleOperation = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            nextPosition = new Position((GridPane.getRowIndex((Node) mouseEvent.getSource()) - 1) / 2, (GridPane.getColumnIndex(((Node) mouseEvent.getSource())) - 1) / 2);
            nextActionFSM();
        }
    };


    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {
        final FXMLLoader divinitiesSelectorLoader = new FXMLLoader(getClass().getResource("/divinitySelection.fxml"));
        final GameBoardController thisController = this;
        if (availableDivinities.size() == 1)
            view.notifyObserver((x) -> x.registerPlayerDivinity(availableDivinities.get(0).getName()));
        else {
            Pane selectionPane = null;
            divinitiesSelectorLoader.setController(new DivinityChoiceController(availableDivinities, 1, thisController));
            try {
                selectionPane = divinitiesSelectorLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gameMessage.setText("Select your divinity in the selector at your right.");
            multifunctionalPane.getChildren().add(selectionPane);

        }
    }

    public void sendChallengerDivinities(ArrayList<String> divinities) {
        view.notifyObserver((x) -> x.selectAvailableDivinities(divinities));
        multifunctionalPane.getChildren().clear();
    }

    public void sendPlayerDivinity(String divinity) {
        view.notifyObserver((x) -> x.registerPlayerDivinity(divinity));
        multifunctionalPane.getChildren().clear();
    }

    public void sendFirstPlayerChoice(String playerName) {
        view.notifyObserver((x) -> x.selectFirstPlayer(playerName));
        multifunctionalPane.getChildren().clear();
    }

    /**
     * method implementing the positioning of the workers on the board
     * @param validCells is the list of valid positions for the placing of the workers
     */
    public void requestInitialPositioning(ArrayList<Position> validCells) {

        //we need to show the choice message to the player
        gameMessage.setText("Click on the cell where you want to position");

        //we need to associate the image of a highlighted cell to each node of the grid pane

        nextState = FSM_STATUS.positioning;

        this.positionValid = validCells;
        boardPane.setVisible(true);

        for (Position p : validCells) {
            ImageView choiceImage = new ImageView(isSelectionImage);
            choiceImage.setOpacity(0.4);
            choiceImage.setFitWidth(95);
            choiceImage.setFitHeight(95);

            choiceImage.fitHeightProperty().bind(boardPane.heightProperty().divide(7));
            choiceImage.fitWidthProperty().bind(boardPane.widthProperty().divide(7));

            GridPane.setHalignment(choiceImage, HPos.CENTER);
            GridPane.setValignment(choiceImage, VPos.CENTER);
            choiceImage.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);

            boardPane.add(choiceImage, 1 + 2 * p.getColumn(), 1 + 2 * p.getRow());


        }
    }

    /**
     * method called by the initial positioning scene controller, to notify the server about the player choice
     *
     * @param initialPosition is the position chosen by the player
     */
    public void sendInitialPositioningChoice(Position initialPosition)
    {
        view.notifyObserver(x -> x.putWorkerOnTable(initialPosition));
    }

    /**
     * method implementing the move action of a player during the match
     * @param validCellsForMove is the list of workers that can be moved, together with the positions where they can be moved
     */
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove)
    {
        Node node;

        this.moveValid=validCellsForMove; //we need to copy the input list in order to have it in all of the gui states
        this.nextState=FSM_STATUS.worker_selection_move; //we update the status of the gui

        if (validCellsForMove.size()==1) //we only have one worker so we just need to select the cell where the player wants to move
        {
            this.postWorkerChoiceMove(validCellsForMove.get(0));
        }
        else //the player needs to choose the worker to move
        {
            gameMessage.setText("Click on the worker you want to move");

            boardPane.setVisible(true);
            for (WorkerValidCells w : validCellsForMove)
            {
                //we need to assign a mouse clicked event and a highlight to the worker, so he can be chosen
                node=getNodeFromGridPane(boardPane, 1+2*w.getwC(), 1+2*w.getwR(), true);
                node.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                ImageView workerImage= new ImageView(workerChoiceImage);
                workerImage.setOpacity(0.4);
                boardPane.add(workerImage, 1+2*w.getwC(), 1+2*w.getwR());
                for (Position p : w.getValidPositions())
                {
                    //if the position hasn't already been highlighted, we do this operation
                    //if the cell has already been highlighted we don't need to do anything
                    if (!isAlreadyHighlighted(validCellsForMove, p, w.getwR(), w.getwC()))
                    {
                        ImageView cellChoice= new ImageView(isSelectionImage);
                        cellChoice.setOpacity(0.4);
                        cellChoice.setFitWidth(95);
                        cellChoice.setFitHeight(95);
                        cellChoice.fitHeightProperty().bind(boardPane.heightProperty().divide(7));
                        cellChoice.fitWidthProperty().bind(boardPane.widthProperty().divide(7));
                        GridPane.setHalignment(cellChoice, HPos.CENTER);
                        GridPane.setValignment(cellChoice, VPos.CENTER);
                        boardPane.add(cellChoice, 1+2*p.getColumn(), 1+2*p.getRow());
                    }
                }
            }
        }
    }

    /**
     * support method implementing the part of the move action where the player chooses the cell where to move
     * @param chosenWorker is the worker selected to move, with the valid cells where he can be put on
     */
    public void postWorkerChoiceMove (WorkerValidCells chosenWorker)
    {
        this.nextState=FSM_STATUS.sendmove;
        workerPosition= new Position(chosenWorker.getwR(), chosenWorker.getwC());

        gameMessage.setText("Click on the cell where you want to move your worker");

        boardPane.setVisible(true);
        for (Position p : chosenWorker.getValidPositions())
        {
            ImageView highlight= new ImageView(isSelectionImage);
            highlight.setOpacity(0.4);
            highlight.setFitWidth(95);
            highlight.setFitHeight(95);
            highlight.fitHeightProperty().bind(boardPane.heightProperty().divide(7));
            highlight.fitWidthProperty().bind(boardPane.widthProperty().divide(7));
            GridPane.setHalignment(highlight, HPos.CENTER);
            GridPane.setValignment(highlight, VPos.CENTER);
            highlight.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
            boardPane.add(highlight, 1+2*p.getColumn(), 1+2*p.getRow());
        }
    }

    /**
     * method that notifies the observers about the worker and the cell chosen to move
     * @param moveChoiceCoordinates contains the coordinates of the worker and of the cell
     */
    public void sendMoveChoice(MoveCoordinates moveChoiceCoordinates)
    {
        view.notifyObserver(x->x.move(moveChoiceCoordinates));
    }

    /**
     * support method to check if a certain position on the board has already been highlighted
     * @param listToCheck is the full list of workers with their valid positions
     * @param posToCheck is the position we need to check before highlighting it
     * @param workerRow is the row of the worker who has the position we are checking in his list
     * @param workerColumn is the column of the worker who has the position we are checking in his list
     * @return a boolean which is true if the position has already been highlighted
     */
    public boolean isAlreadyHighlighted(ArrayList<WorkerValidCells> listToCheck, Position posToCheck, int workerRow, int workerColumn)
    {
        boolean found=false;
        boolean highlighted=false;

        for (int i = 0; i<listToCheck.size() && !found; i++)
        {
            if (listToCheck.get(i).getwR()==workerRow && listToCheck.get(i).getwC()==workerColumn)
            {
                found=true;
            }
            else
            {
                if (listToCheck.get(i).getValidPositions().contains(posToCheck))
                {
                    highlighted=true;
                    break;
                }
            }
        }

        return (highlighted);
    }

    public void changedPlayerList(ArrayList<String> newPlayerList) {

        playerList = newPlayerList;

        System.out.println("Changed player list run later");
        System.out.println("updating players list");
        if (view.getPlayersInGame() == 2) {
            thirdPlayerBg.setVisible(false);
            thirdPlayerCard.setVisible(false);
            thirdPlayerName.setVisible(false);
        }
        //first of all, I need to reset all text fields and images
        playersName.forEach(x -> x.setText("Waiting for players"));
        playersDivinity.forEach(x -> x.setImage(null));

        firstPlayerName.setText("Waiting for Players");
        secondPlayerName.setText("Waiting for Players");
        thirdPlayerName.setText("Waiting for Players");


        int i = 0;
        for (String s : newPlayerList) {
            //first I need to parse the arrived strings
            String name = s.split("\\.")[0];
            String colour = s.split("\\.")[1];
            String divinity = s.split("\\.")[2];
            //now i need to set divinity image
            ImageView currentDivinity = playersDivinity.get(i);
            Text currentName = playersName.get(i);
            if (!divinity.equals("Divinity Not Chosen") && !divinity.equals("Base Divinity")) {
                currentDivinity.setImage(new Image("/santorini_risorse-grafiche-2/Sprite/Cards/Full/" + divinity + ".png"));
            }
            currentName.setText(name + "\n" + divinity);
            switch (colour) {
                case "GREY":
                    currentName.setFill(Color.GREY);
                    break;
                case "BLUE":
                    currentName.setFill(Color.BLUE);
                    break;
                case "WHITE":
                    currentName.setFill(Color.WHITE);
                    break;
            }
            i++;
        }


    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row, boolean image) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                if (image && node instanceof ImageView) return node;
                else if (node instanceof Text) return node;
            }
        }
        return null;
    }

    public void printMessage(String s) {
        gameMessage.setText(s);
    }

    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div) {
        final FXMLLoader divinitiesSelectorLoader = new FXMLLoader(getClass().getResource("/divinitySelection.fxml"));
        final GameBoardController thisController = this;

        Pane selectionPane = null;
        divinitiesSelectorLoader.setController(new DivinityChoiceController(div, view.getPlayersInGame(), thisController));
        try {
            selectionPane = divinitiesSelectorLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameMessage.setText("Select divinities in game in the selector at your right.");
        multifunctionalPane.getChildren().add(selectionPane);

    }


    public void requestInitialPlayerSelection(ArrayList<String> players) {
        final FXMLLoader divinitiesSelectorLoader = new FXMLLoader(getClass().getResource("/firstPlayerSelection.fxml"));
        final GameBoardController thisController = this;
        Pane selectionPane = null;
        divinitiesSelectorLoader.setController(new FirstPlayerSelectionController(players.size(), thisController, players));
        try {
            selectionPane = divinitiesSelectorLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gameMessage.setText("Select the first player");
        multifunctionalPane.getChildren().add(selectionPane);

    }

    public void nextActionFSM()
    {
        switch (nextState)
        {
            case positioning:
                ArrayList<Node> tbr = new ArrayList<>();
                //first of all, i must restore the initial board situation

                for (Node n : boardPane.getChildren()) {
                    if (positionValid.contains(new Position((GridPane.getRowIndex(n) - 1) / 2, (GridPane.getColumnIndex(n) - 1) / 2)) && ((ImageView) n).getImage() == isSelectionImage) {
                        n.removeEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                        tbr.add(n);
                    }
                }
                tbr.forEach(x -> boardPane.getChildren().remove(x));
                //restored the board situation, I must send the cell
                sendInitialPositioningChoice(nextPosition);
                break;
            case worker_selection_move:
                Node n;
                //we need to remove the mouse events from the worker cells and to remove the highlighting
                for (WorkerValidCells w : moveValid)
                {
                    n=getNodeFromGridPane(boardPane, 1+2*w.getwC(), 1+2*w.getwR(), true);
                    n.removeEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                    boardPane.getChildren().remove(n);
                    for (Position p : w.getValidPositions())
                    {
                        n=getNodeFromGridPane(boardPane, 1+2*p.getColumn(), 1+2*p.getRow(), true);
                        boardPane.getChildren().remove(n);
                    }
                }
                //after restoring the board we can proceed with the choice of the cell
                WorkerValidCells workerToSend=null;
                for (WorkerValidCells w1 : moveValid)
                {
                    if (w1.getwR()==nextPosition.getRow() && w1.getwC()==nextPosition.getColumn())
                    {
                        workerToSend=w1;
                        break;
                    }
                }
                postWorkerChoiceMove(workerToSend);
            case sendmove:
                // we need to reset the board
                WorkerValidCells temp=null;
                Node tempNode;
                for (WorkerValidCells w : moveValid)
                {
                    if (w.getwR()==workerPosition.getRow() && w.getwC()==workerPosition.getColumn())
                    {
                        temp=w;
                        break;
                    }
                }
                for(Position p : temp.getValidPositions())
                {
                    getNodeFromGridPane(boardPane, 1+2*p.getColumn(), 1+2*p.getRow(), true).removeEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                    tempNode=getNodeFromGridPane(boardPane, 1+2*p.getColumn(), 1+2*p.getRow(), true);
                    boardPane.getChildren().remove(tempNode);
                }
                this.sendMoveChoice(new MoveCoordinates(workerPosition.getRow(), workerPosition.getColumn(), nextPosition.getRow(), nextPosition.getColumn()));
        }
    }


    public void changedBoard(ArrayList<Cell> newCells) {
        //must reset all the new cells, and and recreate these cells with the new informations
        for (Cell c : newCells) {
            boardPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 1 + 2 * c.getRow() && GridPane.getColumnIndex(node) == 1 + 2 * c.getColumn());
            //removed all nodes, i must replace them with the new informations
            Image levelToBeCharged = null;

            switch (c.getLevel()) {

                case 1:
                    levelToBeCharged = new Image("/buildings/basebuilding.png");
                    break;
                case 2:
                    levelToBeCharged = new Image("/buildings/secondlevel.png");
                    break;
                case 3:
                    levelToBeCharged = new Image("/buildings/thirdlevel.png");
                    break;
            }

            if (levelToBeCharged != null) {
                ImageView building = new ImageView(levelToBeCharged);
                building.setFitWidth(95);
                building.setFitHeight(95);
                building.fitHeightProperty().bind(boardPane.heightProperty().divide(7));
                building.fitWidthProperty().bind(boardPane.widthProperty().divide(7));
                GridPane.setValignment(building, VPos.CENTER);
                GridPane.setHalignment(building, HPos.CENTER);
                boardPane.add(building, 1 + 2 * c.getColumn(), 1 + 2 * c.getRow());
            }

            if (c.isDomed()) {
                ImageView dome = new ImageView("/buildings/dome.png");
                dome.setFitHeight(75);
                dome.setFitWidth(75);
                dome.fitHeightProperty().bind(boardPane.heightProperty().divide(7));
                dome.fitWidthProperty().bind(boardPane.widthProperty().divide(7));
                GridPane.setValignment(dome, VPos.CENTER);
                GridPane.setHalignment(dome, HPos.CENTER);
                boardPane.add(dome, 1 + 2 * c.getColumn(), 1 + 2 * c.getRow());
            }

            if (c.getPlayer() != null) {
                Image worker = null;
                String colour = getPlayerColour(c.getPlayer());
                switch (colour) {
                    case "GRAY":
                        worker = new Image("/workers/grey.png");
                        break;
                    case "WHITE":
                        worker = new Image("/workers/white.png");
                        break;
                    case "BLUE":
                        worker = new Image("/workers/blue.png");
                        break;
                }
                if (worker != null) {
                    ImageView toBeAdded = new ImageView(worker);
                    toBeAdded.setFitHeight(75);
                    toBeAdded.setFitWidth(75);
                    toBeAdded.fitHeightProperty().bind(boardPane.heightProperty().divide(7));
                    toBeAdded.fitWidthProperty().bind(boardPane.widthProperty().divide(7));
                    toBeAdded.setVisible(true);
                    GridPane.setValignment(toBeAdded, VPos.CENTER);
                    GridPane.setHalignment(toBeAdded, HPos.CENTER);
                    boardPane.add(toBeAdded, 1 + 2 * c.getColumn(), 1 + 2 * c.getRow());
                }
            }
        }

    }

    public String getPlayerColour(String playerName) {
        for (String s : playerList) {
            if (s.split("\\.")[0].equals(playerName)) return s.split("\\.")[1];
        }
        return null;
    }

    public void resizeElements(double height, double width) {

        double scaleH = (600.0) / (720.0);
        double scaleW = 600.0 / 1280.0;
        boardPane.setPrefHeight(scaleH * height);
        boardPane.setPrefWidth(scaleW * width);
    }

    public void initialize() {
        playersDivinity.add(firstPlayerDivinity);
        playersDivinity.add(secondPlayerDivinity);
        playersDivinity.add(thirdPlayerDivinity);

        playersName.add(firstPlayerName);
        playersName.add(secondPlayerName);
        playersName.add(thirdPlayerName);
        boardPane.setAlignment(Pos.CENTER);


    }

}
