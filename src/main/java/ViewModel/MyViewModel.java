package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.spec.RSAOtherPrimeInfo;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Aviadjo on 6/14/2017.
 */
public class MyViewModel extends Observable implements Observer {

    private IModel model;

    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;

    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding

    public Solution solution;

    private boolean isGameOver = false;

    public MyViewModel(IModel model){
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
        characterPositionColumnIndex = model.getPlayer_col();
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

    public void saveMaze(File dest){
        model.saveMaze(dest);
    }

    public void loadMaze(File dest){
        model.loadMaze(dest);
    }


    public void setProperties(String generate, String solving){
        try {
            model.setProperties(generate,solving);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void hideSolution(){
        model.hideSolution();
    }

    public void check(){

    }

    public void exit(){
        model.exit();
    }

    public String[] getInfo() {
        return model.getInfo();
    }
}
