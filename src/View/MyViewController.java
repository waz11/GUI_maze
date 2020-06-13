package View;


import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MyViewController implements IView {
    private int[][] maze;
    private int row;
    private int col;
    private StringProperty img_wall = new SimpleStringProperty();
    private StringProperty img_player = new SimpleStringProperty();



    @Override
    public void displayMaze(int[][] maze) {
        this.maze = maze;

        mazeDisplayer.setMaze(maze);

        int characterPositionRow = MyViewModel.getCharacterPositionRow();
        int characterPositionColumn = MyViewModel.getCharacterPositionColumn();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionColumn.set(characterPositionColumn + "");
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



}
