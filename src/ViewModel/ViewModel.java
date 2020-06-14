package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Aviadjo on 6/14/2017.
 */
public class ViewModel extends Observable implements Observer {

    private IModel model;

    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;

    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding

    public Solution solution;

    private boolean isGameOver = false;

    public ViewModel(IModel model){
        this.model = model;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o==model) {
            if (arg != null && arg == "solution") {
                solution = model.getSolution();
                updatePlayerPosition();
                setChanged();
                notifyObservers("solution");
            } else {
                updatePlayerPosition();
                setChanged();
                notifyObservers();
            }
        }
    }

    public Solution getSolution(){
        return solution;
    }

    public void updatePlayerPosition(){
        characterPositionRowIndex = model.getPlayer_row();
        //characterPositionRow.set(characterPositionRowIndex + "");
        characterPositionColumnIndex = model.getPlayer_col();
        //characterPositionColumn.set(characterPositionColumnIndex + "");
        isGameOver = model.isGameOver();
    }

    public void generateMaze(int width, int height){
        model.generateMaze(width, height);
        isGameOver = false;
    }

    public void solveMaze(){
        model.solveMaze();
    }

    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }

    public Maze getMaze() {
        return model.getMaze();
    }

    public int getCharacterPositionRow() {
        return characterPositionRowIndex;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumnIndex;
    }

}
