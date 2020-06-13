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



    public String getImg_wall() {
        return img_wall.get();
    }

    public StringProperty img_wallProperty() {
        return img_wall;
    }

    public void setImg_wall(String img_wall) {
        this.img_wall.set(img_wall);
    }

    public String getImg_player() {
        return img_player.get();
    }

    public StringProperty img_playerProperty() {
        return img_player;
    }

    public void setImg_player(String img_player) {
        this.img_player.set(img_player);
    }
}
