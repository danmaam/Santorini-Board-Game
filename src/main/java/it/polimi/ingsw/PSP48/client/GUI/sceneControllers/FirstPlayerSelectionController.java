package it.polimi.ingsw.PSP48.client.GUI.sceneControllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

/**
 * Controller used for the selection of the first player of the match
 * @author Daniele Mammone
 */
public class FirstPlayerSelectionController {
    @FXML
    ImageView first;
    @FXML
    ImageView second;
    @FXML
    ImageView third;
    private final ArrayList<ImageView> playersButton = new ArrayList<>();
    private final int playersInGame;
    private final GameBoardController outerController;
    private final ArrayList<String> players;

    public FirstPlayerSelectionController(int playersInGame, GameBoardController outerController, ArrayList<String> players) {
        this.outerController = outerController;
        this.playersInGame = playersInGame;
        this.players = players;
    }

    /**
     * Initializes the player selection pane.
     * Adds to each button:
     * - the handler of mouse pressing/releasing, to implement pressed button effect
     * - the handler of mouse click, that sends to the board controller the selected player
     */
    public void initialize() {
        playersButton.add(first);
        playersButton.add(second);
        playersButton.add(third);
        for (int i = 0; i < 3; i++) {
            if (i < playersInGame) {
                playersButton.get(i).setDisable(false);
                playersButton.get(i).setVisible(true);
            } else {
                playersButton.get(i).setDisable(true);
                playersButton.get(i).setVisible(false);
            }
        }
        for (Node n : playersButton) {
            n.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> outerController.sendFirstPlayerChoice(players.get(playersButton.indexOf(n))));

            n.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> ((ImageView) n).setImage(new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_green_pressed.png")));

            n.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseEvent -> ((ImageView) n).setImage(new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_green.png")));
        }


    }

}
