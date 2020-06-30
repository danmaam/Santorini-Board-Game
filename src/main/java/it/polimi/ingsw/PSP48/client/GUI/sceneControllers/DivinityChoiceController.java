package it.polimi.ingsw.PSP48.client.GUI.sceneControllers;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * The controller for divinity chooser Pane
 * @author Daniele Mammone
 */
public class DivinityChoiceController {
    @FXML
    private ImageView prev;
    @FXML
    private ImageView next;
    @FXML
    private ImageView choose;
    @FXML
    private ImageView divinityCard;
    @FXML
    private Text divinityDescription;
    @FXML
    private Text divinityName;
    @FXML
    private Text nextName;
    @FXML
    private Text prevName;
    @FXML
    private Text infoText;

    private int currentDivinity = 0;
    private final GameBoardController outerController;

    private final ArrayList<DivinitiesWithDescription> divinities;
    private final ArrayList<String> chosenDivinities = new ArrayList<>();
    private final int numberToBeChosen;


    /**
     * Initialized the controller
     *
     * @param divinityList     the list of divinities from which the player can choose
     * @param numberToBeChosen the number of divinities to be chosen
     * @param outerController  the controller of the game board's scene
     */
    public DivinityChoiceController(ArrayList<DivinitiesWithDescription> divinityList, int numberToBeChosen, GameBoardController outerController) {
        this.divinities = divinityList;
        this.numberToBeChosen = numberToBeChosen;
        this.outerController = outerController;
    }

    /**
     * Initializer method for FXML load, sets visible the first divinity, and then updates the next button with the next divinity (if
     * there are more than one divinities)
     */
    public void initialize() {
        //must import first divinity
        currentDivinity = 0;
        Image card = new Image("santorini_risorse-grafiche-2/Sprite/Cards/Full/" + divinities.get(0).getName() + ".png");
        divinityCard.setImage(card);
        divinityName.setText(divinities.get(0).getName());
        divinityDescription.setText(divinities.get(0).getDescription());
        if (divinities.size() > 1) nextName.setText(divinities.get(1).getName());
        else {
            nextName.setVisible(false);
            next.setVisible(false);
        }
        prevName.setText("");
        prevName.setVisible(false);
        prev.setVisible(false);
        if (numberToBeChosen > 1)
            infoText.setText("Remaining " + (numberToBeChosen - chosenDivinities.size()) + " divinities to be chosen");
        else infoText.setText("Choose your divinity!");
    }

    /**
     * Set the next divinity in the divinity list as the visible, and:
     * -if the new current divinity isn't neither the last or the first, sets the new previous and new next divinities name on next and previous buttons;
     * -if the new current divinity is the last divinity in the list, updates only the previous button, hiding the next button
     */
    public void nextDivinity() {
        if (currentDivinity + 1 < divinities.size()) {
            currentDivinity++;
            Image card = new Image("santorini_risorse-grafiche-2/Sprite/Cards/Full/" + divinities.get(currentDivinity).getName() + ".png");
            divinityCard.setImage(card);
            divinityName.setText(divinities.get(currentDivinity).getName());
            divinityDescription.setText(divinities.get(currentDivinity).getDescription());
            prevName.setText(divinities.get(currentDivinity - 1).getName());
            prevName.setVisible(true);
            prev.setVisible(true);
            if (currentDivinity != divinities.size() - 1) {
                nextName.setText(divinities.get(currentDivinity + 1).getName());
                next.setVisible(true);
                nextName.setVisible(true);
            } else {
                nextName.setText("");
                next.setVisible(false);
                nextName.setVisible(false);
            }
        }
    }

    /**
     * Set the previous divinity in the divinity list as the visible, and:
     * -if the new current divinity isn't neither the last or the first, sets the new previous and new next divinities name on next and previous buttons;
     * -if the new current divinity is the first divinity in the list, updates only the next button, hiding the previous button
     */
    public void prevDivinity() {
        if (currentDivinity > 0) {
            currentDivinity--;
            Image card = new Image("santorini_risorse-grafiche-2/Sprite/Cards/Full/" + divinities.get(currentDivinity).getName() + ".png");
            divinityCard.setImage(card);
            divinityName.setText(divinities.get(currentDivinity).getName());
            divinityDescription.setText(divinities.get(currentDivinity).getDescription());
            nextName.setText(divinities.get(currentDivinity + 1).getName());
            nextName.setVisible(true);
            next.setVisible(true);
            if (currentDivinity != 0) {
                prevName.setText(divinities.get(currentDivinity - 1).getName());
                prevName.setVisible(true);
                prev.setVisible(true);
            } else {
                prevName.setVisible(false);
                prev.setVisible(false);
                prevName.setText("");
            }
        }
    }

    /**
     * Changes next button image until mouse isn't released, to make pressed button effect
     */
    public void clickedNext() {
        Image pressedButton = new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_blue_pressed.png");
        next.setImage(pressedButton);
    }

    /**
     * Restore next button background when the mouse is released
     */
    public void releasedNext() {
        Image pressedButton = new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_blue.png");
        next.setImage(pressedButton);
    }

    /**
     * Changes prev button image until mouse isn't released, to make pressed button effect
     */
    public void clickedPrev() {
        Image pressedButton = new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_coral_pressed.png");
        prev.setImage(pressedButton);
    }

    /**
     * Restore prev button background when the mouse is released
     */
    public void releasedPrev() {
        Image pressedButton = new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_coral.png");
        prev.setImage(pressedButton);
    }

    /**
     * Saves in a list the selected divinity, deletes the chosen divinity from the list of available divinities and
     * - if there are other divinities to be selected, changes the visible divinity and asks player to choose another divinity
     * - else notify the view that the player completed divinities selection
     */
    public void choose() {
        String chosenDivinity = divinityName.getText();
        chosenDivinities.add(chosenDivinity);
        divinities.remove(currentDivinity);

        if (chosenDivinities.size() == numberToBeChosen) {
            if (numberToBeChosen > 1) outerController.sendChallengerDivinities(chosenDivinities);
            else outerController.sendPlayerDivinity(chosenDivinity);
        } else {
            if (currentDivinity == 0) {
                firstDivinity();
            } else if (currentDivinity == divinities.size() || currentDivinity == divinities.size() - 1) {
                lastDivinity();
            } else nextDivinity();
            infoText.setText("Remaining " + (numberToBeChosen - chosenDivinities.size()) + " divinities to be chosen");
        }

    }

    /**
     * Set as visible the first divinity in the list, hides prev button and updates next button
     */
    public void firstDivinity() {
        Image card = new Image("santorini_risorse-grafiche-2/Sprite/Cards/Full/" + divinities.get(0).getName() + ".png");
        divinityCard.setImage(card);
        divinityName.setText(divinities.get(0).getName());
        divinityDescription.setText(divinities.get(0).getDescription());
        if (divinities.size() > 1) nextName.setText(divinities.get(1).getName());
        else {
            nextName.setVisible(false);
            next.setVisible(false);
        }
        prevName.setText("");
        prevName.setVisible(false);
        prev.setVisible(false);
    }

    /**
     * Set as visible the last divinity in the list, hides next button and updates prev button
     */
    public void lastDivinity() {
        currentDivinity = divinities.size() - 1;
        Image card = new Image("santorini_risorse-grafiche-2/Sprite/Cards/Full/" + divinities.get(currentDivinity).getName() + ".png");
        divinityCard.setImage(card);
        divinityName.setText(divinities.get(currentDivinity).getName());
        divinityDescription.setText(divinities.get(currentDivinity).getDescription());
        if (divinities.size() > 1) prevName.setText(divinities.get(currentDivinity - 1).getName());
        else {
            prevName.setVisible(false);
            prev.setVisible(false);
        }
        nextName.setText("");
        nextName.setVisible(false);
        next.setVisible(false);
    }


    /**
     * Changes choose button image until mouse isn't released, to make pressed button effect
     */
    public void clickedChoose() {
        Image pressedButton = new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_green_pressed.png");
        choose.setImage(pressedButton);
    }

    /**
     * Restore choose button background when the mouse is released
     */
    public void releasedChoose() {
        Image pressedButton = new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_green.png");
        choose.setImage(pressedButton);
    }

}
