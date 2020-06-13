package View;

import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.util.Observable;


public class MyViewController implements IView {
    private int[][] maze;
    private int row;
    private int col;
    private StringProperty img_wall = new SimpleStringProperty();
    private StringProperty img_player = new SimpleStringProperty();

    //BUTTONS:
    public javafx.scene.control.Button btn_solveMaze;
    public javafx.scene.control.Button btn_generateMaze;

    public MazeDisplayer mazeDisplayer;
    private MyViewModel viewModel;

    public TextField rowsInput;
    public TextField colsInput;

    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            displayMaze(viewModel.getMaze());
            btn_generateMaze.setDisable(false);
        }
    }



    @FXML
    public void generateMaze(ActionEvent actionEvent) {
//        check the input
//                try
//                {
//                    Integer.parseInt(rowsInput.getText());
//                    Integer.parseInt(colsInput.getText());
//                }
        int rows = Integer.valueOf(rowsInput.getText());
        int cols = Integer.valueOf(colsInput.getText());
        viewModel.generateMaze(rows, cols);
        btn_solveMaze.setVisible(true);

    }


    @Override
    public void displayMaze(int[][] maze) {
        this.maze = maze;
//
        mazeDisplayer.setMaze(maze);
//
//        int characterPositionRow = MyViewModel.getCharacterPositionRow();
//        int characterPositionColumn = MyViewModel.getCharacterPositionColumn();
//        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
//        this.characterPositionRow.set(characterPositionRow + "");
//        this.characterPositionColumn.set(characterPositionColumn + "");
    }

    public void draw(){
        
    }

    //region String Property for Binding
    public StringProperty characterPositionRow = new SimpleStringProperty();

    public StringProperty characterPositionColumn = new SimpleStringProperty();

    public String getCharacterPositionRow() {
        return characterPositionRow.get();
    }

    public StringProperty characterPositionRowProperty() {
        return characterPositionRow;
    }

    public String getCharacterPositionColumn() {
        return characterPositionColumn.get();
    }

    public StringProperty characterPositionColumnProperty() {
        return characterPositionColumn;
    }


    public void solveMaze(ActionEvent actionEvent) {
    }
}
