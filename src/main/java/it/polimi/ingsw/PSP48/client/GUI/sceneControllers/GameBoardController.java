package it.polimi.ingsw.PSP48.client.GUI.sceneControllers;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.WorkerValidCells;
import it.polimi.ingsw.PSP48.client.GUI.GUI;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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

    private enum FSM_STATUS {
        positioning, worker_selection_move, worker_selection_build, sendmove, sendbuild
    }

    private Position nextPosition = null;
    private FSM_STATUS nextState;
    private ArrayList<Position> positionValid;
    private ArrayList<WorkerValidCells> moveValid;
    private ArrayList<WorkerValidCells> buildValid;
    private ArrayList<WorkerValidCells> domeValid;

    private ArrayList<String> playerList;

    private final Image isSelectionImage = new Image("santorini_risorse-grafiche-2/Texture2D/Whirpool.png");

    private final EventHandler<MouseEvent> handleOperation = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            nextPosition = new Position(GridPane.getRowIndex((Node) mouseEvent.getSource()), GridPane.getColumnIndex(((Node) mouseEvent.getSource())));
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
            GridPane.setHalignment(choiceImage, HPos.CENTER);
            GridPane.setValignment(choiceImage, VPos.CENTER);
            choiceImage.addEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);

            boardPane.add(choiceImage, p.getColumn(), p.getRow());

            Node temp = getNodeFromGridPane(boardPane, p.getColumn(), p.getRow());


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

    public void changedPlayerList(ArrayList<String> newPlayerList) {

        playerList = newPlayerList;

        System.out.println("Changed player list run later");
        System.out.println("updating players list");
        if (view.getPlayersInGame() == 2) {
            thirdPlayerBg.setVisible(false);
            thirdPlayerCard.setVisible(false);
            getNodeFromGridPane(playersPane, 2, 4).setVisible(false);
        }
        //first of all, I need to reset all text fields and images
        for (int i = 0; i < 5; i = i + 2) {
            ((Text) getNodeFromGridPane(playersPane, 2, i)).setText("Waiting for Players");
            ((ImageView) getNodeFromGridPane(playersPane, 0, 4)).setImage(null);
        }
        int i = 0;
        for (String s : newPlayerList) {
            //first I need to parse the arrived strings
            String name = s.split("\\.")[0];
            String colour = s.split("\\.")[1];
            String divinity = s.split("\\.")[2];
            //now i need to set divinity image
            if (!divinity.equals("Divinity Not Chosen") && !divinity.equals("Base Divinity")) {
                ((ImageView) getNodeFromGridPane(playersPane, 0, i)).setImage(new Image("/santorini_risorse-grafiche-2/Sprite/Cards/Full/" + divinity + ".png"));
            }
            ((Text) getNodeFromGridPane(playersPane, 2, i)).setText(name + "\n" + divinity);
            switch (colour) {
                case "GREY":
                    ((Text) getNodeFromGridPane(playersPane, 2, i)).setFill(Color.GREY);
                    break;
                case "BLUE":
                    ((Text) getNodeFromGridPane(playersPane, 2, i)).setFill(Color.BLUE);
                    break;
                case "WHITE":
                    ((Text) getNodeFromGridPane(playersPane, 2, i)).setFill(Color.WHITE);
                    break;
            }
            i = i + 2;
        }


    }

    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
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

    public void nextActionFSM() {
        switch (nextState) {
            case positioning:
                ArrayList<Node> tbr = new ArrayList<>();
                //first of all, i must restore the initial board situation

                for (Node n : boardPane.getChildren()) {
                    if (positionValid.contains(new Position(GridPane.getRowIndex(n), GridPane.getColumnIndex(n))) && ((ImageView) n).getImage() == isSelectionImage) {
                        n.removeEventFilter(MouseEvent.MOUSE_CLICKED, handleOperation);
                        tbr.add(n);
                    }
                }
                tbr.forEach(x -> boardPane.getChildren().remove(x));
                //restored the board situation, I must send the cell
                sendInitialPositioningChoice(nextPosition);
                break;
        }
    }

    public void changedBoard(ArrayList<Cell> newCells) {
        //must reset all the new cells, and and recreate these cells with the new informations
        for (Cell c : newCells) {
            boardPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == c.getRow() && GridPane.getColumnIndex(node) == c.getColumn());
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
                GridPane.setValignment(building, VPos.CENTER);
                GridPane.setHalignment(building, HPos.CENTER);
                boardPane.add(building, c.getColumn(), c.getRow());
            }

            if (c.isDomed()) {
                ImageView dome = new ImageView("/buildings/dome.png");
                dome.setFitHeight(75);
                dome.setFitWidth(75);
                GridPane.setValignment(dome, VPos.CENTER);
                GridPane.setHalignment(dome, HPos.CENTER);
                boardPane.add(dome, c.getColumn(), c.getRow());
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
                    toBeAdded.setVisible(true);
                    GridPane.setValignment(toBeAdded, VPos.CENTER);
                    GridPane.setHalignment(toBeAdded, HPos.CENTER);
                    boardPane.add(toBeAdded, c.getColumn(), c.getRow());
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


}
