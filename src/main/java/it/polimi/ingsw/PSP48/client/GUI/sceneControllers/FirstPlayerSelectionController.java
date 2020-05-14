package it.polimi.ingsw.PSP48.client.GUI.sceneControllers;

import it.polimi.ingsw.PSP48.client.GUI.GUI;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class FirstPlayerSelectionController {
    @FXML
    private GridPane playerSelection;
    private int playersInGame;
    private GUI outerController;

    public FirstPlayerSelectionController(GridPane playerSelection, GUI outerController) {
        this.playerSelection = playerSelection;
        this.outerController = outerController;
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
            n.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    System.out.println("Pressed player " + playerSelection.getColumnIndex(n));
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
