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
    }

    public void draw(){
        
    }


}
