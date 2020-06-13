package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
//
    private int player_row;
    private int player_col;
//
//    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
//    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding
//
    public MyViewModel(IModel model){
        this.model = model;
    }

    public int[][] getMaze(){
        return model.getMaze();
    }
//
//    @Override
//    public void update(Observable o, Object arg) {
//        if (o==model){
//            characterPositionRowIndex = model.getCharacterPositionRow();
//            characterPositionRow.set(characterPositionRowIndex + "");
//            characterPositionColumnIndex = model.getCharacterPositionColumn();
//            characterPositionColumn.set(characterPositionColumnIndex + "");
//            setChanged();
//            notifyObservers();
//        }
//    }
//
    public void generateMaze(int rows, int cols){
        model.generateMaze(rows, cols);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
//
//    public void moveCharacter(KeyCode movement){
//        model.moveCharacter(movement);
//    }
//
//    public int[][] getMaze() {
//        return model.getMaze();
//    }
//
//    public int getCharacterPositionRow() {
//        return characterPositionRowIndex;
//    }
//
//    public int getCharacterPositionColumn() {
//        return characterPositionColumnIndex;
//    }



}
