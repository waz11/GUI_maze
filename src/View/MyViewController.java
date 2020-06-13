package View;


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

        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionColumn = viewModel.getCharacterPositionColumn();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionColumn.set(characterPositionColumn + "");
    }

    public void draw(){
        
    }



}
