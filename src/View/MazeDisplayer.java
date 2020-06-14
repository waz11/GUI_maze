package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Aviadjo on 3/9/2017.
 */
public class MazeDisplayer extends Canvas {

    private int[][] maze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;

    public void setMaze(int[][] maze) {
        this.maze = maze;
        redraw();
    }

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double x = canvasHeight / maze.length;
            double y = canvasWidth / maze[0].length;
//            double x = 50;
//            double y = 50;

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image goalImage = new Image(new FileInputStream(ImageFileNameGoal.get()));

                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                int rows = maze.length;
                int cols = maze[0].length;
//                gc.drawImage(wallImage, col * y, row * x, x, y);

//                int row = 1;
//                int col = 4;
//                gc.fillRect(row * x, col * y, x, y);
//                gc.drawImage(wallImage, col * x, row * y, x,y);
                //Draw Maze
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        if (maze[row][col] == 1)
                            gc.drawImage(wallImage, col * x, row * y, x, y);
                    }

                }

                //Draw Character
                //gc.setFill(Color.RED);
//                gc.fillOval(3 * x, 1 * y, x, y);
                gc.drawImage(characterImage, characterPositionColumn * x, characterPositionRow * y, x, y);
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }


    //img: Wall
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }
    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    //img: Player
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }
    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    //img: Goal
    private StringProperty ImageFileNameGoal = new SimpleStringProperty();
    public String getImageFileNameGoal() {
        return ImageFileNameGoal.get();
    }
    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.ImageFileNameGoal.set(imageFileNameGoal);
    }





}
