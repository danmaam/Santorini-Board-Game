package it.polimi.ingsw.PSP48.client.GUI.customTextField;

import javafx.scene.control.TextField;

public class NumericTextField extends TextField {
    @Override
    public void replaceText(int i, int i1, String s) {
        if (s.matches("[0-9]") || s.isEmpty())
            super.replaceText(i, i1, s);
    }

    @Override
    public void replaceSelection(String s) {
        if (s.matches("[0-9]") || s.isEmpty())
            super.replaceSelection(s);
    }
}
