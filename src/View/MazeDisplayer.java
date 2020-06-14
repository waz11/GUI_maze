package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class MazeDisplayer extends Canvas {

    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double x = canvasHeight / maze.length;
            double y = canvasWidth / maze[0].length;

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image goalImage = new Image(new FileInputStream(ImageFileNameGoal.get()));

                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());
                int rows = maze.length;
                int cols = maze[0].length;

                //Draw Maze
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        if (maze[row][col] == 1)
                            gc.drawImage(wallImage, col * x, row * y, x, y);
                    }
                }

                //Draw Character
                gc.drawImage(characterImage, characterPositionColumn * x, characterPositionRow * y, x, y);
                //Draw Goal
//                gc.drawImage(goalImage, characterPositionColumn * x, characterPositionRow * y, x, y);

            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }

    // Maze:
    private int[][] maze;
    public void setMaze(int[][] maze) {
        this.maze = maze;
        redraw();
    }

    // Wall
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }
    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    // Goal
    private StringProperty ImageFileNameGoal = new SimpleStringProperty();
    public String getImageFileNameGoal() {
        return ImageFileNameGoal.get();
    }
    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.ImageFileNameGoal.set(imageFileNameGoal);
    }


    // Player:
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }
    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
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


}
