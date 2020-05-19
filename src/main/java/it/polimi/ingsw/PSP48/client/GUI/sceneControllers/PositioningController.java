package it.polimi.ingsw.PSP48.client.GUI.sceneControllers;

import it.polimi.ingsw.PSP48.client.GUI.GUI;
import it.polimi.ingsw.PSP48.server.model.Position;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import java.util.ArrayList;

/**
 * class used to handle the scene that the player uses for the initial positioning of each worker
 */
public class PositioningController
{
    private ArrayList<Position> cells;
    private GUI otherController;
    @FXML
    GridPane boardGridPane;
    @FXML
    Text choiceMessage;

    /**
     * class constructor for the initial positioning scene controller
     * @param positions is the list of positions we need to highlight for the player to choose them
     * @param controller is the outer class calling the scene controller
     */
    public PositioningController(ArrayList<Position> positions, GUI controller)
    {
        this.cells=positions;
        this.otherController=controller;
    }

    /**
     * method used to retrieve a specific node from the grid pane, to get the nodes of the board we need to initialize
     * @param boardGridPane is the grid pane from where we get the nodes
     * @param row is the row of the node
     * @param column is the column of the node
     * @return a reference to the searched node
     */
    private Node getNodeFromGridPane (GridPane boardGridPane, int row, int column)
    {
        for (Node n : boardGridPane.getChildren())
        {
            if (GridPane.getRowIndex(n)==row && GridPane.getColumnIndex(n)==column)
            {
                return (n);
            }
        }
        return (null); //after the loop we have inspected every element of the board, we can just return a null reference
    }

    /**
     * method used for the setup of the initial positioning scene
     */
    public void initialize()
    {
        Node tempNode;
        ImageView cellHighlight=new ImageView();

        choiceMessage.setText("Click on the cell where you want to position your worker");

        Image cell= new Image("santorini_risorse-grafiche-2/Texture2D/Toggle_Checkmark.png");
        cellHighlight.setImage(cell);

        for (Position p : cells) //we assign a mouse event to each node that corresponds to a position in the list given by the server
        {
            GridPane.setConstraints(cellHighlight, p.getRow(), p.getColumn());
            tempNode=this.getNodeFromGridPane(boardGridPane, p.getRow(), p.getColumn());
            tempNode.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    otherController.sendInitialPositioningChoice(new Position(p.getRow(), p.getColumn())); //the handler calls a method in the gui class that notifies the correct observer
                }
            });
        }
        boardGridPane.getChildren().addAll(cellHighlight);
    }
}