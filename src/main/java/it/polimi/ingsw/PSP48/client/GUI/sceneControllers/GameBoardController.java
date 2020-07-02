package it.polimi.ingsw.PSP48.client.GUI.sceneControllers;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.ViewInterface;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.client.GUI.GUI;
import it.polimi.ingsw.PSP48.server.model.ActionCoordinates;
import it.polimi.ingsw.PSP48.server.model.Cell;
import it.polimi.ingsw.PSP48.server.model.Position;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;

/**
 * class used as a controller for the board, used to update it and modify its status
 * @author Daniele Mammone, Rebecca Marelli
 */
public class GameBoardController {
    /**
     * class constructor, initialising the reference to the gui object
     * @param view is the reference to a GUI object, containing the methods to notify the server
     */
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
    private StackPane multifunctionalPane;
    @FXML
    private ImageView thirdPlayerCard;
    @FXML
    private ImageView thirdPlayerBg;
    @FXML
    private Pane leftPane;

    @FXML
    private ListView gameLog;


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

    private boolean startedGame = false;


    private enum FSM_STATUS {
        positioning, worker_selection_move, worker_selection_build, sendmove, sendbuild
    }

    private Position nextPosition = null;
    private Position workerPosition = null;
    private FSM_STATUS nextState;
    private ArrayList<Position> positionValid;
    private ArrayList<WorkerValidCells> moveValid;
    private ArrayList<WorkerValidCells> buildValid;
    private ArrayList<WorkerValidCells> domeValid;

    private ArrayList<String> playerList;

    private final Image isSelectionImage = new Image("santorini_risorse-grafiche-2/Texture2D/Whirpool.png");
    private final Image workerChoiceImage = new Image("santorini_risorse-grafiche-2/Texture2D/Cloud_01.png");
    private final Image domeSelectionImage = new Image("santorini_risorse-grafiche-2/Texture2D/jeff_cloudpoof.png");
    private final Image buildAndDomeImage = new Image("santorini_risorse-grafiche-2/Texture2D/WindToken.png");
    private final Image skipButtonImage = new Image("santorini_risorse-grafiche-2/Sprite/Buttons/skip.png");
    private final Image buildButtonImage = new Image("santorini_risorse-grafiche-2/Sprite/Buttons/build.png");
    private final Image domeButtonImage = new Image("santorini_risorse-grafiche-2/Sprite/Buttons/dome.png");

    /**
     * attribute used to save the position of the board chosen by the player, and then to clear and update it
     */
    private final EventHandler<MouseEvent> handleOperation = mouseEvent -> {
        nextPosition = new Position((GridPane.getRowIndex((Node) mouseEvent.getSource()) - 1) / 2, (GridPane.getColumnIndex(((Node) mouseEvent.getSource())) - 1) / 2);
        nextActionFSM();
    };

    /**
     * attribute used to save the position chosen to build, to clear the board and then notify the build observer
     */
    private final EventHandler<MouseEvent> handleBuild = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            ArrayList<Node> removeButtons = new ArrayList<>();
            ArrayList<Node> removeSelectionImage = new ArrayList<>();

            for (Node n : boardPane.getChildren()) {
                if (((ImageView) n).getImage() == isSelectionImage || ((ImageView) n).getImage() == buildAndDomeImage || ((ImageView) n).getImage() == domeSelectionImage) {
                    removeSelectionImage.add(n);
                }
            }
            removeSelectionImage.forEach(x -> boardPane.getChildren().remove(x));

            multifunctionalPane.getChildren().clear();

            sendBuildChoice(new ActionCoordinates(workerPosition.getRow(), workerPosition.getColumn(), nextPosition.getRow(), nextPosition.getColumn()));
        }
    };

    /**
     * handler of the actions following a dome operation: it saves the chosen cell, clears the board and notifies the observer
     */
    private final EventHandler<MouseEvent> handleDome = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            ArrayList<Node> removeButtons = new ArrayList<>();
            ArrayList<Node> removeSelectionImage = new ArrayList<>();

            for (Node n : boardPane.getChildren()) {
                if (((ImageView) n).getImage() == isSelectionImage || ((ImageView) n).getImage() == buildAndDomeImage || ((ImageView) n).getImage() == domeSelectionImage) {
                    removeSelectionImage.add(n);
                }
            }
            removeSelectionImage.forEach(x -> boardPane.getChildren().remove(x));

            multifunctionalPane.getChildren().clear();

            sendDomeChoice(new ActionCoordinates(workerPosition.getRow(), workerPosition.getColumn(), nextPosition.getRow(), nextPosition.getColumn()));
        }
    };

    /**
     * handler of the skip of the optional move action
     * this attribute clears the board and the board pane (removing the skip button), and notifies the server about the choice
     */
    private final EventHandler<MouseEvent> handleOptionalMoveSkip = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            ArrayList<Node> removeNodes = new ArrayList<>();

            for (Node n : boardPane.getChildren()) {
                if (((ImageView) n).getImage() == isSelectionImage || ((ImageView) n).getImage() == workerChoiceImage) {
                    n.removeEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                    removeNodes.add(n);
                }
            }
            removeNodes.forEach(x -> boardPane.getChildren().remove(x));

            multifunctionalPane.getChildren().clear();

            sendMoveChoice(new ActionCoordinates(-1, -1, -1, -1));
        }
    };

    /**
     * handler of the skip of the optional build operation
     * it acts similarly to the one for the move operation, and notifies the correct observer
     */
    private final EventHandler<MouseEvent> handleOptionalBuildSkip = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            ArrayList<Node> removeNodes = new ArrayList<>();
            ArrayList<Node> removeFromPane = new ArrayList<>();

            for (Node n : boardPane.getChildren()) {
                if (((ImageView) n).getImage() == isSelectionImage || ((ImageView) n).getImage() == buildAndDomeImage || ((ImageView) n).getImage() == domeSelectionImage || ((ImageView) n).getImage() == workerChoiceImage) {
                    n.removeEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                    removeNodes.add(n);
                }
            }
            removeNodes.forEach(x -> boardPane.getChildren().remove(x));

            multifunctionalPane.getChildren().clear();

            sendBuildChoice(new ActionCoordinates(-1, -1, -1, -1));
        }
    };

    /**
     * Loads and makes visible divinities selector controller, in order to request player to select his own divinity
     *
     * @param availableDivinities the list of available divinities to choose
     */
    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {
        startedGame = true;
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

    /**
     * Notifies the game controller that challenger selected the available divinities in the game
     *
     * @param divinities the chosen divinities
     */
    public void sendChallengerDivinities(ArrayList<String> divinities) {
        view.notifyObserver((x) -> x.selectAvailableDivinities(divinities));
        multifunctionalPane.getChildren().clear();
    }

    /**
     * Notifies the game controller of the divinity chosen by the player
     *
     * @param divinity the chosen divinities
     */
    public void sendPlayerDivinity(String divinity) {
        view.notifyObserver((x) -> x.registerPlayerDivinity(divinity));
        multifunctionalPane.getChildren().clear();
    }

    /**
     * Notifies the game controller that the challenger chosen the first player of the game
     *
     * @param playerName the name of the first player
     */
    public void sendFirstPlayerChoice(String playerName) {
        view.notifyObserver((x) -> x.selectFirstPlayer(playerName));
        multifunctionalPane.getChildren().clear();
    }

    /**
     * method implementing the positioning of the workers on the board
     *
     * @param validCells is the list of valid positions for the placing of the workers
     */
    public void requestInitialPositioning(ArrayList<Position> validCells) {
        startedGame = true;
        //we need to show the choice message to the player
        gameMessage.setText("Click on the cell where you want to position");

        //we need to associate the image of a highlighted cell to each node of the grid pane

        nextState = FSM_STATUS.positioning;

        this.positionValid = validCells;
        boardPane.setVisible(true);

        for (Position p : validCells) {
            ImageView choiceImage = new ImageView(isSelectionImage);
            choiceImage.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> choiceImage.setOpacity(0.8));
            choiceImage.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> choiceImage.setOpacity(0.4));
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
    public void sendInitialPositioningChoice(Position initialPosition) {
        view.notifyObserver(x -> x.putWorkerOnTable(initialPosition));
    }

    /**
     * method implementing the move action of a player during the match
     *
     * @param validCellsForMove is the list of workers that can be moved, together with the positions where they can be moved
     */
    public void requestMove(ArrayList<WorkerValidCells> validCellsForMove)
    {
        this.moveValid = validCellsForMove; //we need to copy the input list in order to have it in all of the gui states
        this.nextState = FSM_STATUS.worker_selection_move; //we update the status of the gui

        if (validCellsForMove.size() == 1) //we only have one worker so we just need to select the cell where the player wants to move
        {
            this.postWorkerChoiceMove(validCellsForMove.get(0));
        } else //the player needs to choose the worker to move
        {
            gameMessage.setText("Click on the worker you want to move");

            boardPane.setVisible(true);
            for (WorkerValidCells w : validCellsForMove) {
                //we need to assign a mouse clicked event and a highlight to the worker, so he can be chosen

                ImageView workerImage = new ImageView(workerChoiceImage);
                workerImage.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                workerImage.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> workerImage.setOpacity(1));
                workerImage.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> workerImage.setOpacity(0.9));
                workerImage.setOpacity(0.9);
                workerImage.setFitWidth(95);
                workerImage.setFitHeight(95);
                boardPane.add(workerImage, 1 + 2 * w.getwC(), 1 + 2 * w.getwR());
                for (Position p : w.getValidPositions()) {
                    //if the position hasn't already been highlighted, we do this operation
                    //if the cell has already been highlighted we don't need to do anything
                    if (!isAlreadyHighlighted(validCellsForMove, p, w.getwR(), w.getwC())) {
                        ImageView cellChoice = new ImageView(isSelectionImage);
                        cellChoice.setOpacity(0.4);
                        cellChoice.setFitWidth(95);
                        cellChoice.setFitHeight(95);
                        cellChoice.fitHeightProperty().bind(boardPane.heightProperty().divide(7));
                        cellChoice.fitWidthProperty().bind(boardPane.widthProperty().divide(7));
                        GridPane.setHalignment(cellChoice, HPos.CENTER);
                        GridPane.setValignment(cellChoice, VPos.CENTER);
                        boardPane.add(cellChoice, 1 + 2 * p.getColumn(), 1 + 2 * p.getRow());
                    }
                }
            }
        }
    }

    /**
     * support method implementing the part of the move action where the player chooses the cell where to move
     *
     * @param chosenWorker is the worker selected to move, with the valid cells where he can be put on
     */
    public void postWorkerChoiceMove(WorkerValidCells chosenWorker) {
        this.nextState = FSM_STATUS.sendmove;
        workerPosition = new Position(chosenWorker.getwR(), chosenWorker.getwC());

        gameMessage.setText("Click on the cell where you want to move your worker");

        boardPane.setVisible(true);
        for (Position p : chosenWorker.getValidPositions()) {
            ImageView highlight = new ImageView(isSelectionImage);
            highlight.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> highlight.setOpacity(0.8));
            highlight.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> highlight.setOpacity(0.4));
            highlight.setOpacity(0.4);
            highlight.setFitWidth(95);
            highlight.setFitHeight(95);
            highlight.fitHeightProperty().bind(boardPane.heightProperty().divide(7));
            highlight.fitWidthProperty().bind(boardPane.widthProperty().divide(7));
            GridPane.setHalignment(highlight, HPos.CENTER);
            GridPane.setValignment(highlight, VPos.CENTER);
            highlight.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
            boardPane.add(highlight, 1 + 2 * p.getColumn(), 1 + 2 * p.getRow());
        }
    }

    /**
     * method that notifies the observers about the worker and the cell chosen to move
     *
     * @param moveChoiceCoordinates contains the coordinates of the worker and of the cell
     */
    public void sendMoveChoice(ActionCoordinates moveChoiceCoordinates) {
        view.notifyObserver(x -> x.move(moveChoiceCoordinates));
    }

    /**
     * support method to check if a certain position on the board has already been highlighted
     *
     * @param listToCheck  is the full list of workers with their valid positions
     * @param posToCheck   is the position we need to check before highlighting it
     * @param workerRow    is the row of the worker who has the position we are checking in his list
     * @param workerColumn is the column of the worker who has the position we are checking in his list
     * @return a boolean which is true if the position has already been highlighted
     */
    public boolean isAlreadyHighlighted(ArrayList<WorkerValidCells> listToCheck, Position posToCheck, int workerRow, int workerColumn) {
        boolean found = false;
        boolean highlighted = false;

        for (int i = 0; i < listToCheck.size() && !found; i++) {
            if (listToCheck.get(i).getwR() == workerRow && listToCheck.get(i).getwC() == workerColumn) {
                found = true;
            } else {
                if (listToCheck.get(i).getValidPositions().contains(posToCheck)) {
                    highlighted = true;
                    break;
                }
            }
        }

        return (highlighted);
    }

    /**
     * method handling the building actions during a player's turn
     * @param validForBuild is the list of workers who can do the build, with their valid cells for this operation
     * @param validForDome is the list of workers who can do the dome, with their valid cells for this action
     */
    public void requestDomeOrBuild (ArrayList<WorkerValidCells> validForBuild, ArrayList<WorkerValidCells> validForDome)
    {
        ArrayList<Position> tempList=null;

        this.nextState=FSM_STATUS.worker_selection_build;
        this.buildValid=validForBuild;
        this.domeValid=validForDome;

        if (validForBuild.size() > 1 || validForDome.size() > 1 || (validForBuild.size() > 0 && validForDome.size() > 0 && !(validForBuild.get(0).getwR() == validForDome.get(0).getwR() && validForBuild.get(0).getwC() == validForDome.get(0).getwC()))) {
            //the player can choose the worker, so we need to highlight the cells
            gameMessage.setText("Click on the worker you want to use for the building actions");
            boardPane.setVisible(true);
            for (WorkerValidCells w1 : validForBuild) {
                //first of all we get both lists of cells for each worker (there can also be just one)
                for (WorkerValidCells w2 : validForDome) {
                    if (w2.getwR() == w1.getwR() && w2.getwC() == w1.getwC()) {
                        tempList = w2.getValidPositions();
                        break;
                    }
                }
                //we assign an highlight and a mouse event to the worker
                ImageView workerImage = new ImageView(workerChoiceImage);
                workerImage.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> workerImage.setOpacity(1));
                workerImage.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> workerImage.setOpacity(0.9));
                workerImage.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                workerImage.setOpacity(0.9);
                workerImage.setFitWidth(95);
                workerImage.setFitHeight(95);
                boardPane.add(workerImage, 1 + 2 * w1.getwC(), 1 + 2 * w1.getwR());
                //then we need to highlight all the cells of the worker
                boolean buildHighlighted;
                for (Position p1 : w1.getValidPositions()) {
                    //the worker can only do the build action on a certain cell
                    if (tempList == null || !tempList.contains(p1)) {
                        buildHighlighted = isAlreadyHighlighted(validForBuild, p1, w1.getwR(), w1.getwC());
                        if (!buildHighlighted) {
                            ImageView buildChoice = new ImageView(isSelectionImage);
                            buildChoice.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> buildChoice.setOpacity(0.8));
                            buildChoice.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> buildChoice.setOpacity(0.4));
                            buildChoice.setOpacity(0.4);
                            buildChoice.setFitWidth(95);
                            buildChoice.setFitHeight(95);
                            buildChoice.fitHeightProperty().bind(boardPane.heightProperty().divide(7));
                            buildChoice.fitWidthProperty().bind(boardPane.widthProperty().divide(7));
                            GridPane.setHalignment(buildChoice, HPos.CENTER);
                            GridPane.setValignment(buildChoice, VPos.CENTER);
                            boardPane.add(buildChoice, 1 + 2 * p1.getColumn(), 1 + 2 * p1.getRow());
                        }
                    }
                    //the worker can both build or put a dome on the cell we are considering
                    else
                    {
                        buildHighlighted=isAlreadyHighlighted(validForBuild, p1, w1.getwR(), w1.getwC());
                        if (!buildHighlighted)
                        {
                            ImageView buildHighlight = new ImageView(buildAndDomeImage);
                            buildHighlight.setOpacity(0.4);
                            buildHighlight.setFitWidth(95);
                            buildHighlight.setFitHeight(95);
                            boardPane.add(buildHighlight, 1 + 2 * p1.getColumn(), 1 + 2 * p1.getRow());
                        }
                    }
                }
                //we have to highlight the position where the worker can only put a dome
                boolean canDome;
                if (tempList!=null)
                {
                    for (Position p2 : tempList)
                    {
                        if (!w1.getValidPositions().contains(p2))
                        {
                            canDome=isAlreadyHighlighted(validForDome, p2, w1.getwR(), w1.getwC());
                            if (!canDome)
                            {
                                ImageView dome = new ImageView(domeSelectionImage);
                                dome.setOpacity(0.4);
                                dome.setFitWidth(95);
                                dome.setFitHeight(95);
                                boardPane.add(dome, 1 + 2 * p2.getColumn(), 1 + 2 * p2.getRow());
                            }
                        }
                    }
                }
                tempList=null;
            }

            //we have to highlight the cells of the workers that can only do the dome action
            boolean bothActions=false;
            for (WorkerValidCells w : validForDome)
            {
                for (WorkerValidCells w1 : validForBuild)
                {
                    if (w1.getwR()==w.getwR() && w1.getwC()==w.getwC())
                    {
                        bothActions=true;
                        break;
                    }
                }
                if (bothActions) break; //we have already highlighted the workers who can do build and dome
                else
                {
                    //we highlight the worker first
                    ImageView workerHighlight = new ImageView(workerChoiceImage);
                    workerHighlight.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> workerHighlight.setOpacity(1));
                    workerHighlight.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> workerHighlight.setOpacity(0.9));
                    workerHighlight.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                    workerHighlight.setOpacity(0.9);
                    workerHighlight.setFitWidth(95);
                    workerHighlight.setFitHeight(95);
                    boardPane.add(workerHighlight, 1 + 2 * w.getwC(), 1 + 2 * w.getwR());
                    //then we need to highlight the valid cells for the dome action
                    boolean domeHighlighted;
                    for (Position p : w.getValidPositions())
                    {
                        domeHighlighted=isAlreadyHighlighted(validForDome, p, w.getwR(), w.getwC());
                        if (!domeHighlighted)
                        {
                            ImageView domeHighlight = new ImageView(domeSelectionImage);
                            domeHighlight.setOpacity(0.4);
                            domeHighlight.setFitWidth(95);
                            domeHighlight.setFitHeight(95);
                            boardPane.add(domeHighlight, 1 + 2 * p.getColumn(), 1 + 2 * p.getRow());
                        }
                    }
                }
            }
        }
        else
        {
            //the player can't choose the worker so we proceed to next phase
            this.postWorkerChoiceBuild(validForBuild, validForDome);
        }
    }

    /**
     * method implementing the part of the build action after the player has chosen the worker to use (if there is a choice)
     * @param buildWorker contains the chosen worker with the list of cells where to build
     * @param domeWorker contains the chosen worker with the list of cells that can be domed
     */
    public void postWorkerChoiceBuild (ArrayList<WorkerValidCells> buildWorker, ArrayList<WorkerValidCells> domeWorker)
    {
        ArrayList<Position> domeList=null;

        //we have to change the state of the gui and save the position of the chosen worker
        this.nextState=FSM_STATUS.sendbuild;
        if (buildWorker.isEmpty())
        {
            workerPosition=new Position(domeWorker.get(0).getwR(), domeWorker.get(0).getwC());
        }
        else workerPosition= new Position(buildWorker.get(0).getwR(), buildWorker.get(0).getwC());

        gameMessage.setText("Click on the cell where you want to do the build action");

        //after setting the message for the player, we highlight the cells only belonging to the chosen worker
        for (WorkerValidCells w1 : buildWorker)
        {
            for (WorkerValidCells w2 : domeWorker)
            {
                if (w2.getwR()==w1.getwR() && w2.getwC()==w1.getwC())
                {
                    domeList=w2.getValidPositions();
                    break;
                }
            }
            //now we need to highlight the positions of the worker
            boolean buildHighlighted;
            for (Position p1 : w1.getValidPositions()) {
                //the worker can only do the build action on a certain cell
                if (domeList == null || !domeList.contains(p1)) {
                    buildHighlighted = isAlreadyHighlighted(buildWorker, p1, w1.getwR(), w1.getwC());
                    if (!buildHighlighted) {
                        ImageView buildChoice = new ImageView(isSelectionImage);
                        buildChoice.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                        buildChoice.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> buildChoice.setOpacity(0.8));
                        buildChoice.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> buildChoice.setOpacity(0.4));
                        buildChoice.setOpacity(0.4);
                        buildChoice.setFitWidth(95);
                        buildChoice.setFitHeight(95);
                        buildChoice.fitHeightProperty().bind(boardPane.heightProperty().divide(7));
                        buildChoice.fitWidthProperty().bind(boardPane.widthProperty().divide(7));
                        GridPane.setHalignment(buildChoice, HPos.CENTER);
                        GridPane.setValignment(buildChoice, VPos.CENTER);
                        boardPane.add(buildChoice, 1 + 2 * p1.getColumn(), 1 + 2 * p1.getRow());
                    }
                }
                //the worker can both build or put a dome on the cell we are considering
                else
                {
                    buildHighlighted=isAlreadyHighlighted(buildWorker, p1, w1.getwR(), w1.getwC());
                    if (!buildHighlighted)
                    {
                        ImageView buildHighlight = new ImageView(buildAndDomeImage);
                        buildHighlight.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                        buildHighlight.setOpacity(0.4);
                        buildHighlight.setFitWidth(95);
                        buildHighlight.setFitHeight(95);
                        boardPane.add(buildHighlight, 1 + 2 * p1.getColumn(), 1 + 2 * p1.getRow());
                    }
                }
            }
            //we have to highlight the position where the worker can only put a dome
            boolean canDome;
            if (domeList!=null)
            {
                for (Position p2 : domeList)
                {
                    if (!w1.getValidPositions().contains(p2))
                    {
                        canDome=isAlreadyHighlighted(domeWorker, p2, w1.getwR(), w1.getwC());
                        if (!canDome)
                        {
                            ImageView dome = new ImageView(domeSelectionImage);
                            dome.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                            dome.setOpacity(0.4);
                            dome.setFitWidth(95);
                            dome.setFitHeight(95);
                            boardPane.add(dome, 1 + 2 * p2.getColumn(), 1 + 2 * p2.getRow());
                        }
                    }
                }
            }
            domeList=null;
        }
        //the chosen worker may only be able to do the dome action, we have to take care of this case
        boolean bothActions=false;
        for (WorkerValidCells w : domeWorker)
        {
            for (WorkerValidCells w1 : buildWorker)
            {
                if (w1.getwR()==w.getwR() && w1.getwC()==w.getwC())
                {
                    bothActions=true;
                    break;
                }
            }
            if (bothActions) break; //we have already highlighted the workers who can do build and dome
            else
            {
                boolean domeHighlighted;
                for (Position p : w.getValidPositions())
                {
                    domeHighlighted=isAlreadyHighlighted(domeWorker, p, w.getwR(), w.getwC());
                    if (!domeHighlighted)
                    {
                        ImageView domeHighlight = new ImageView(domeSelectionImage);
                        domeHighlight.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                        domeHighlight.setOpacity(0.4);
                        domeHighlight.setFitWidth(95);
                        domeHighlight.setFitHeight(95);
                        boardPane.add(domeHighlight, 1 + 2 * p.getColumn(), 1 + 2 * p.getRow());
                    }
                }
            }
        }
    }

    /**
     * method used to notify the server about the build action coordinates
     *
     * @param buildActionCoordinates contains the chosen worker and the chosen cell to do the build
     */
    public void sendBuildChoice(ActionCoordinates buildActionCoordinates) {
        view.notifyObserver(x -> x.build(buildActionCoordinates));
    }

    /**
     * method used to notify the client observers about the dome action coordinates
     *
     * @param domeActionCoordinates contains the worker and the cell to do the dome action
     */
    public void sendDomeChoice(ActionCoordinates domeActionCoordinates) {
        view.notifyObserver(x -> x.dome(domeActionCoordinates));
    }

    /**
     * method handling the optional move action during a turn
     * @param validCellsForMove is the list of valid cells for this action
     */
    public void requestOptionalMove (ArrayList<WorkerValidCells> validCellsForMove)
    {
        this.moveValid=validCellsForMove;
        this.nextState=FSM_STATUS.worker_selection_move;

        gameMessage.setText("Click on the blue button on the right if you want to skip the optional move, else click on your worker");

        boardPane.setVisible(true);
        for (WorkerValidCells w : validCellsForMove)
        {
            ImageView workerImage = new ImageView(workerChoiceImage);
            workerImage.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> workerImage.setOpacity(1));
            workerImage.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> workerImage.setOpacity(0.9));
            workerImage.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
            workerImage.setOpacity(0.9);
            workerImage.setFitWidth(95);
            workerImage.setFitHeight(95);
            boardPane.add(workerImage, 1 + 2 * w.getwC(), 1 + 2 * w.getwR());
            for (Position p : w.getValidPositions())
            {
                if (!isAlreadyHighlighted(validCellsForMove, p, w.getwR(), w.getwC()))
                {
                    ImageView cellChoice = new ImageView(isSelectionImage);
                    cellChoice.addEventFilter(MouseEvent.MOUSE_ENTERED, mouseEvent -> cellChoice.setOpacity(0.8));
                    cellChoice.addEventFilter(MouseEvent.MOUSE_EXITED, mouseEvent -> cellChoice.setOpacity(0.4));
                    cellChoice.setOpacity(0.4);
                    cellChoice.setFitWidth(95);
                    cellChoice.setFitHeight(95);
                    cellChoice.fitHeightProperty().bind(boardPane.heightProperty().divide(7));
                    cellChoice.fitWidthProperty().bind(boardPane.widthProperty().divide(7));
                    GridPane.setHalignment(cellChoice, HPos.CENTER);
                    GridPane.setValignment(cellChoice, VPos.CENTER);
                    boardPane.add(cellChoice, 1 + 2 * p.getColumn(), 1 + 2 * p.getRow());
                }
            }
        }

        ImageView skipChoice = new ImageView(skipButtonImage);
        StackPane.setAlignment(skipChoice, Pos.BOTTOM_CENTER);
        StackPane.setMargin(skipChoice, new Insets(450, 0, 0, 60));

        skipChoice.addEventFilter(MouseEvent.MOUSE_PRESSED, ((e) -> ((ImageView) e.getSource()).setImage(new Image("santorini_risorse-grafiche-2/Sprite/Buttons/skip_pressed.png"))));
        skipChoice.addEventFilter(MouseEvent.MOUSE_RELEASED, ((e) -> ((ImageView) e.getSource()).setImage(new Image("santorini_risorse-grafiche-2/Sprite/Buttons/skip.png"))));

        multifunctionalPane.getChildren().add(skipChoice);
        skipChoice.setFitHeight(60);
        skipChoice.setFitWidth(237);

        skipChoice.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOptionalMoveSkip);
        multifunctionalPane.getChildren().add(skipChoice);
    }

    /**
     * method handling the optional building actions of a player
     * @param build is the list of valid cells for the build action
     * @param dome is the list of valid cells for the dome action
     */
    public void requestOptionalBuild (ArrayList<WorkerValidCells> build, ArrayList<WorkerValidCells> dome) {

        ImageView skipChoice = new ImageView(skipButtonImage);
        skipChoice.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOptionalBuildSkip);
        StackPane.setAlignment(skipChoice, Pos.BOTTOM_CENTER);
        StackPane.setMargin(skipChoice, new Insets(450, 0, 0, 60));

        skipChoice.addEventFilter(MouseEvent.MOUSE_PRESSED, ((e) -> ((ImageView) e.getSource()).setImage(new Image("santorini_risorse-grafiche-2/Sprite/Buttons/skip_pressed.png"))));
        skipChoice.addEventFilter(MouseEvent.MOUSE_RELEASED, ((e) -> ((ImageView) e.getSource()).setImage(new Image("santorini_risorse-grafiche-2/Sprite/Buttons/skip.png"))));

        multifunctionalPane.getChildren().add(skipChoice);
        skipChoice.setFitHeight(60);
        skipChoice.setFitWidth(237);

        requestDomeOrBuild(build, dome);
    }

    /**
     * Updates the player list shown at video when it changed
     *
     * @param newPlayerList the new player-divinity association list
     */
    public void changedPlayerList(ArrayList<String> newPlayerList) {

        playerList = newPlayerList;
        if (startedGame) view.setNumberOfPlayers(newPlayerList.size());
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
            if (!divinity.equals("Basic") && !divinity.equals("Divinity Not Chosen")) {
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
        if (i == 1 && startedGame) {
            thirdPlayerName.setVisible(false);
            thirdPlayerBg.setVisible(false);
            thirdPlayerCard.setVisible(false);
            thirdPlayerDivinity.setVisible(false);
        }


    }

    /**
     * Shows a message in the left bottom corner of the window
     *
     * @param s the message to be shown
     */
    public void printMessage(String s) {
        gameLog.getItems().add(s);
        gameMessage.setText(s);

    }

    /**
     * Loads and makes visible the divinity selector, in order to request the player to choose his own divinity
     *
     * @param div the available divinities to choose
     */
    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div) {
        startedGame = true;
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

    /**
     * Loads and makes visible the first player pane, to request the challenger to choose game's first player
     *
     * @param players the list of players in game
     */
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

    /**
     * Helper method to complete player's action, dependently to tha completed actions; invokes some helper methods to
     * notify the game controller of the action, or to request the player to complete the second part of some action.
     * Restores than the original board state.
     */
    public void nextActionFSM() {
        ArrayList<Node> tbr = new ArrayList<>();
        switch (nextState) {

            case positioning:
                //first of all, i must restore the initial board situation
                for (Node n : boardPane.getChildren()) {
                    if (positionValid.contains(new Position((GridPane.getRowIndex(n) - 1) / 2, (GridPane.getColumnIndex(n) - 1) / 2)) && ((ImageView) n).getImage() == isSelectionImage) {
                        n.removeEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                        tbr.add(n);
                    }
                }
                tbr.forEach(x -> boardPane.getChildren().remove(x));
                //restored the board situation, I must send the cell
                multifunctionalPane.getChildren().clear();
                sendInitialPositioningChoice(nextPosition);
                break;


            case worker_selection_move:
                //we need to remove the mouse events from the worker cells and to remove the highlighting
                for (Node n : boardPane.getChildren()) {
                    if (((ImageView) n).getImage() == workerChoiceImage || ((ImageView) n).getImage() == isSelectionImage) {
                        n.removeEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                        tbr.add(n);
                    }
                }
                tbr.forEach(x -> boardPane.getChildren().remove(x));

                for (Node n : multifunctionalPane.getChildren()) {
                    if (((ImageView)n).getImage()==skipButtonImage) tbr.add(n);

                }
                tbr.forEach(x->multifunctionalPane.getChildren().remove(x));
                multifunctionalPane.getChildren().clear();

                //after restoring the board we can proceed with the choice of the cell

                for (WorkerValidCells w1 : moveValid) {
                    if (w1.getwR() == nextPosition.getRow() && w1.getwC() == nextPosition.getColumn()) {
                        postWorkerChoiceMove(w1);
                        break;
                    }
                }
                break;

            case sendmove:
                // we need to reset the board
                for (Node n : boardPane.getChildren()) {
                    if (((ImageView) n).getImage() == isSelectionImage) {
                        n.removeEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                        tbr.add(n);
                    }
                }
                tbr.forEach(x -> boardPane.getChildren().remove(x));
                multifunctionalPane.getChildren().clear();

                //restored the board situation, I must send the cell
                this.sendMoveChoice(new ActionCoordinates(workerPosition.getRow(), workerPosition.getColumn(), nextPosition.getRow(), nextPosition.getColumn()));
                break;

            case worker_selection_build:
                //we remove all the images and mouse events from the board
                for (Node n : boardPane.getChildren()) {
                    if (((ImageView)n).getImage()==workerChoiceImage || ((ImageView)n).getImage()==isSelectionImage ||((ImageView)n).getImage()==buildAndDomeImage || ((ImageView)n).getImage()==domeSelectionImage) {
                        n.removeEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                        tbr.add(n);
                    }
                }
                tbr.forEach(x->boardPane.getChildren().remove(x));
                multifunctionalPane.getChildren().clear();

                //now we need to call the method for the choice of the cell
                ArrayList<WorkerValidCells> chosenBuildWorker= new ArrayList<>();
                ArrayList<WorkerValidCells> chosenDomeWorker= new ArrayList<>();
                for (WorkerValidCells w1 : buildValid) {
                    if (w1.getwR()==nextPosition.getRow() && w1.getwC()==nextPosition.getColumn()) {
                        chosenBuildWorker.add(new WorkerValidCells(w1.getValidPositions(), w1.getwR(), w1.getwC()));
                    }
                }
                for (WorkerValidCells w2 : domeValid) {
                    if (w2.getwR()==nextPosition.getRow() && w2.getwC()==nextPosition.getColumn()) {
                        chosenDomeWorker.add(new WorkerValidCells(w2.getValidPositions(), w2.getwR(), w2.getwC()));
                    }
                }
                this.postWorkerChoiceBuild(chosenBuildWorker, chosenDomeWorker);
                break;

            case sendbuild:
                //if the player has chosen a cell and cannot choose what action to do, we notify the observers and clear the board
                //we need to get both lists of positions of the worker to check if he can choose the action
                ArrayList<Position> buildPositions=null, domePositions=null;
                for (WorkerValidCells w : buildValid) {
                    if (w.getwR()==workerPosition.getRow() && w.getwC()==workerPosition.getColumn()) {
                        buildPositions=w.getValidPositions();
                        break;
                    }
                }
                for (WorkerValidCells w1 : domeValid) {
                    if (w1.getwR()==workerPosition.getRow() && w1.getwC()==workerPosition.getColumn()) {
                        domePositions=w1.getValidPositions();
                    }
                }

                if (buildPositions==null || domePositions==null || !buildPositions.contains(nextPosition) || !domePositions.contains(nextPosition)) {
                    //the player can't choose the action so we clear the board and notify the observers
                    for (Node n : boardPane.getChildren()) {
                        if (((ImageView) n).getImage() == isSelectionImage || ((ImageView) n).getImage() == buildAndDomeImage || ((ImageView) n).getImage() == domeSelectionImage) {
                            n.removeEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                            tbr.add(n);
                        }
                    }
                    tbr.forEach(x -> boardPane.getChildren().remove(x));
                    multifunctionalPane.getChildren().clear();
                    //board reset, now we just need to check which observer to notify
                    if (buildPositions != null && buildPositions.contains(nextPosition))
                        this.sendBuildChoice(new ActionCoordinates(workerPosition.getRow(), workerPosition.getColumn(), nextPosition.getRow(), nextPosition.getColumn()));
                    else
                        this.sendDomeChoice(new ActionCoordinates(workerPosition.getRow(), workerPosition.getColumn(), nextPosition.getRow(), nextPosition.getColumn()));
                } else //the player has chosen a cell that can take both building and doming actions, we have to ask about his choice
                {
                    //the cell has already been chosen so we remove the mouse events from the board
                    for (Node n : boardPane.getChildren()) {
                        if (((ImageView)n).getImage()==isSelectionImage ||((ImageView)n).getImage()==buildAndDomeImage || ((ImageView)n).getImage()==domeSelectionImage) {
                            n.removeEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                            if((1+2*nextPosition.getRow())!=GridPane.getRowIndex(n) || (1+2*nextPosition.getColumn())!=GridPane.getColumnIndex(n)) {
                                tbr.add(n);
                            }
                        }
                    }
                    tbr.forEach(x -> boardPane.getChildren().remove(x));
                    multifunctionalPane.getChildren().clear();
                    //now we have to set the buttons and their handler for the choice of the action
                    gameMessage.setText("Click on the red button on the right to build, on the green one to put a dome");
                    ImageView buildBtn = new ImageView(buildButtonImage);
                    ImageView domeBtn = new ImageView(domeButtonImage);
                    buildBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, handleBuild);
                    domeBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, handleDome);


                    StackPane.setAlignment(buildBtn, Pos.BOTTOM_CENTER);
                    StackPane.setMargin(buildBtn, new Insets(455, 0, 0, 60));

                    buildBtn.addEventFilter(MouseEvent.MOUSE_PRESSED, ((e) -> ((ImageView) e.getSource()).setImage(new Image("santorini_risorse-grafiche-2/Sprite/Buttons/build_pressed.png"))));
                    buildBtn.addEventFilter(MouseEvent.MOUSE_RELEASED, ((e) -> ((ImageView) e.getSource()).setImage(new Image("santorini_risorse-grafiche-2/Sprite/Buttons/build.png"))));

                    buildBtn.setFitHeight(60);
                    buildBtn.setFitWidth(237);

                    StackPane.setAlignment(domeBtn, Pos.BOTTOM_CENTER);
                    StackPane.setMargin(domeBtn, new Insets(0, 0, 60, 60));

                    domeBtn.addEventFilter(MouseEvent.MOUSE_PRESSED, ((e) -> ((ImageView) e.getSource()).setImage(new Image("santorini_risorse-grafiche-2/Sprite/Buttons/dome_pressed.png"))));
                    domeBtn.addEventFilter(MouseEvent.MOUSE_RELEASED, ((e) -> ((ImageView) e.getSource()).setImage(new Image("santorini_risorse-grafiche-2/Sprite/Buttons/dome.png"))));

                    domeBtn.setFitHeight(60);
                    domeBtn.setFitWidth(237);


                    multifunctionalPane.getChildren().add(buildBtn);
                    multifunctionalPane.getChildren().add(domeBtn);
                }
                break;
        }
    }

    /**
     * Updates the board cells which content has changed
     *
     * @param newCells the cells that have been updated
     */
    public void changedBoard(ArrayList<Cell> newCells) {
        //must reset all the new cells, and and recreate these cells with the new information
        for (Cell c : newCells) {
            boardPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 1 + 2 * c.getRow() && GridPane.getColumnIndex(node) == 1 + 2 * c.getColumn());
            //removed all nodes, i must replace them with the new information
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

    /**
     * Gets the player workers' colour parsing the string arrived from the controller
     *
     * @param playerName the player name
     * @return the player workers' colour
     */
    public String getPlayerColour(String playerName) {
        for (String s : playerList) {
            if (s.split("\\.")[0].equals(playerName)) return s.split("\\.")[1];
        }
        return null;
    }

    /**
     * Initializes the board controller, and some helper structure
     */
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
