package it.polimi.ingsw.PSP48.client.GUI.sceneControllers;

import it.polimi.ingsw.PSP48.DivinitiesWithDescription;
import it.polimi.ingsw.PSP48.client.GUI.GUI;
import it.polimi.ingsw.PSP48.observers.ViewObserver;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Iterator;

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
    private GameBoardController outerController;

    private ArrayList<DivinitiesWithDescription> divinities;
    private final ArrayList<String> chosenDivinities = new ArrayList<>();
    private int numberToBeChosen;
    private Boolean free = false;


    public DivinityChoiceController(ArrayList<DivinitiesWithDescription> divinityList, int numberToBeChosen, GameBoardController outerController) {
        this.divinities = divinityList;
        this.numberToBeChosen = numberToBeChosen;
        this.outerController = outerController;
    }

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

    public void clickedNext() {
        Image pressedButton = new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_blue_pressed.png");
        next.setImage(pressedButton);
    }

    public void releasedNext() {
        Image pressedButton = new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_blue.png");
        next.setImage(pressedButton);
    }

    public void clickedPrev() {
        Image pressedButton = new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_coral_pressed.png");
        prev.setImage(pressedButton);
    }

    public void releasedPrev() {
        Image pressedButton = new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_coral.png");
        prev.setImage(pressedButton);
    }

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
            } else if (currentDivinity == divinities.size()) {
                lastDivinity();
            } else nextDivinity();
            infoText.setText("Remaining " + (numberToBeChosen - chosenDivinities.size()) + " divinities to be chosen");
        }

    }

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
        nextName.setVisible(false);
    }


    public void clickedChoose() {
        Image pressedButton = new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_green_pressed.png");
        choose.setImage(pressedButton);
    }

    public void releasedChoose() {
        Image pressedButton = new Image("/santorini_risorse-grafiche-2/Sprite/Buttons/btn_green.png");
        choose.setImage(pressedButton);
    }

}
