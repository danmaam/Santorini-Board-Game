package it.polimi.ingsw.PSP48.client.GUI.sceneControllers;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.client.GUI.GUI;
import it.polimi.ingsw.PSP48.server.model.Position;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.IOException;
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

    public void requestDivinitySelection(ArrayList<DivinitiesWithDescription> availableDivinities) {
        final FXMLLoader divinitiesSelectorLoader = new FXMLLoader(getClass().getResource("/divinitySelection.fxml"));
        final GameBoardController thisController = this;
        if (availableDivinities.size() == 1)
            view.notifyObserver((x) -> x.registerPlayerDivinity(availableDivinities.get(0).getName()));
        else {
            Platform.runLater(() -> {
                Pane selectionPane = null;
                divinitiesSelectorLoader.setController(new DivinityChoiceController(availableDivinities, 1, thisController));
                try {
                    selectionPane = divinitiesSelectorLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gameMessage.setText("Select your divinity in the selector at your right.");
                multifunctionalPane.getChildren().add(selectionPane);
            });
        }
    }

    public void sendChallengerDivinities(ArrayList<String> divinities) {
        view.notifyObserver((x) -> x.selectAvailableDivinities(divinities));
        Platform.runLater(() -> multifunctionalPane.getChildren().clear());
    }

    public void sendPlayerDivinity(String divinity) {
        view.notifyObserver((x) -> x.registerPlayerDivinity(divinity));
        Platform.runLater(() -> multifunctionalPane.getChildren().clear());
    }

    public void sendFirstPlayerChoice(String playerName) {
        view.notifyObserver((x) -> x.selectFirstPlayer(playerName));
        Platform.runLater(() -> multifunctionalPane.getChildren().clear());
    }

    public void requestInitialPositioning(ArrayList<Position> validCells)
    {
        ArrayList<ImageView> imagesList= new ArrayList<>();

        //we need to show the choice message to the player
        gameMessage.setText("Click on the cell where you want to position");

        //we need to associate the image of a highlighted cell to each node of the grid pane
        Image illuminatedCell = new Image("santorini_risorse-grafiche-2/Texture2D/Toggle_Checkmark.png");
        for (Position p : validCells)
        {
            ImageView colouredCell= new ImageView(illuminatedCell);
            imagesList.add(colouredCell);
            GridPane.setRowIndex(colouredCell, p.getRow());
            GridPane.setColumnIndex(colouredCell, p.getColumn());
            boardPane.getChildren().add(colouredCell);
            Node tempNode=getNodeFromGridPane(boardPane, p.getColumn(), p.getRow()); //we also establish a mouse event for the correct nodes
            tempNode.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    for (ImageView i : imagesList)
                    {
                        boardPane.getChildren().remove(i);
                    }
                    sendInitialPositioningChoice(new Position(p.getRow(), p.getColumn()));
                }
            });
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

    public void changedPlayerList(ArrayList<String> newPlayerList) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
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
        });

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
        Platform.runLater(() -> gameMessage.setText(s));
    }

    public void requestChallengerDivinitiesSelection(ArrayList<DivinitiesWithDescription> div) {
        final FXMLLoader divinitiesSelectorLoader = new FXMLLoader(getClass().getResource("/divinitySelection.fxml"));
        final GameBoardController thisController = this;
        Platform.runLater(() -> {
            Pane selectionPane = null;
            divinitiesSelectorLoader.setController(new DivinityChoiceController(div, view.getPlayersInGame(), thisController));
            try {
                selectionPane = divinitiesSelectorLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gameMessage.setText("Select divinities in game in the selector at your right.");
            multifunctionalPane.getChildren().add(selectionPane);
        });
    }


    public void requestInitialPlayerSelection(ArrayList<String> players) {
        final FXMLLoader divinitiesSelectorLoader = new FXMLLoader(getClass().getResource("/firstPlayerSelection.fxml"));
        final GameBoardController thisController = this;
        Platform.runLater(() -> {
            Pane selectionPane = null;
            divinitiesSelectorLoader.setController(new FirstPlayerSelectionController(players.size(), thisController, players));
            try {
                selectionPane = divinitiesSelectorLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            gameMessage.setText("Select the first player");
            multifunctionalPane.getChildren().add(selectionPane);
        });
    }


}
