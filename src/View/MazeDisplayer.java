package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
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
            double y = canvasHeight / maze.getRows();
            double x = canvasWidth / maze.getCols();

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image goalImage = new Image(new FileInputStream(ImageFileNameGoal.get()));

                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());
                int rows = maze.getRows();
                int cols = maze.getCols();

                //Draw Maze
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        if (maze.isWall(new Position(row, col)))
                            gc.drawImage(wallImage, col * x, row * y, x, y);
                    }
                }

                //Draw Character
                gc.drawImage(characterImage, characterPositionColumn * x, characterPositionRow * y, x, y);
                //Draw Goal
                gc.drawImage(goalImage, maze.getGoalPosition().getColumnIndex() * x, maze.getGoalPosition().getRowIndex() * y, x, y);

            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }

    // Maze:
    private Maze maze;
    public void setMaze(Maze maze) {
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

    // Solution
    private StringProperty imageFileNameSolution = new SimpleStringProperty();
    public String getImageFileNameSolution() {
        return imageFileNameSolution.get();
    }
    public void setImageFileNameSolution(String imageFileNameSolution) {
        this.imageFileNameSolution.set(imageFileNameSolution);
    }

    // Player:
    private int characterPositionRow;
    private int characterPositionColumn;
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
