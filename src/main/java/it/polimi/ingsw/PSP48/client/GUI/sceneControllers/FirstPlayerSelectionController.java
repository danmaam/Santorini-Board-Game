package it.polimi.ingsw.PSP48.client.GUI.sceneControllers;

import it.polimi.ingsw.PSP48.client.GUI.GUI;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class FirstPlayerSelectionController {
    @FXML
    private GridPane playerSelection;
    private final int playersInGame;
    private final GameBoardController outerController;
    private final ArrayList<String> players;

    public FirstPlayerSelectionController(int playersInGame, GameBoardController outerController, ArrayList<String> players) {
        this.outerController = outerController;
        this.playersInGame = playersInGame;
        this.players = players;
    }

    public void initialize() {
        for (int i = 0; i < 3; i++) {
            if (i < playersInGame) {
                getNodeFromGridPane(playerSelection, 0, i).setDisable(false);
                getNodeFromGridPane(playerSelection, 0, i).setVisible(true);
            } else {
                getNodeFromGridPane(playerSelection, 0, i).setDisable(true);
                getNodeFromGridPane(playerSelection, 0, i).setVisible(false);
            }
        }
        for (Node n : playerSelection.getChildren()) {
            n.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    outerController.sendFirstPlayerChoice(players.get(GridPane.getRowIndex(n)));
                }
            });

            n.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    ((ImageView) n).setImage(new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_green_pressed.png"));
                }
            });

            n.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    ((ImageView) n).setImage(new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_green.png"));
                }
            });
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
}
