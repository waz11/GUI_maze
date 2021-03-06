package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class MazeDisplayer extends Canvas {
    private Maze maze;
    private double canvasHeight;
    private double canvasWidth;
    private double y;
    private double x;
    private int rows;
    private int cols;
    Image wallImage;
    Image characterImage;
    Image goalImage;
    Image solutionImg;
    Solution solution;
    private StringProperty ImageFileNameWall= new SimpleStringProperty();;
    private StringProperty ImageFileNameGoal= new SimpleStringProperty();;
    private StringProperty imageFileNameSolution= new SimpleStringProperty();;
    private StringProperty ImageFileNameCharacter= new SimpleStringProperty();;
    private int characterPositionRow;
    private int characterPositionColumn;
    private static boolean showSolution = false;

    public void setMaze(Maze maze) {
        this.maze = maze;
        redraw();
    }

    public void redraw() {
        setSizes();
        if (maze == null) return;
        drawMaze();
        if (showSolution)
            drawSolution();
        drawGoal();
        drawPlayer();
    }

    public void setSizes(){
        rows = maze.getRows();
        cols = maze.getCols();
        canvasHeight = getHeight();
        canvasWidth = getWidth();
        y = canvasHeight / maze.getRows();
        x = canvasWidth / maze.getCols();
    }


    public void drawMaze() {
        try {
            wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
            solutionImg = new Image(new FileInputStream(imageFileNameSolution.get()));
        } catch (FileNotFoundException e) {
        }
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Position p = new Position(row, col);
                if (maze.isWall(p))
                    gc.drawImage(wallImage, col * x, row * y, x, y);
            }
        }
    }

    private void drawPlayer() {
        try {
            characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
        } catch (FileNotFoundException e) {
        }
        GraphicsContext gc = getGraphicsContext2D();
        gc.drawImage(characterImage, characterPositionColumn * x, characterPositionRow * y, x, y);
    }

    private void drawGoal() {
        try {
            goalImage = new Image(new FileInputStream(ImageFileNameGoal.get()));
        } catch (FileNotFoundException e) { }
        GraphicsContext gc = getGraphicsContext2D();
        gc.drawImage(goalImage, maze.getGoalPosition().getColumnIndex() * x, maze.getGoalPosition().getRowIndex() * y, x, y);
    }

    private void drawSolution(){
        try {
            solutionImg = new Image(new FileInputStream(imageFileNameSolution.get()));
        } catch (FileNotFoundException e) { }
        GraphicsContext gc = getGraphicsContext2D();
        for (int i = 0; i < solution.getSolutionPath().size(); i++) {
            MazeState position = (MazeState) solution.getSolutionPath().get(i);
            if (!(position.getState().getColumnIndex() == characterPositionColumn && position.getState().getRowIndex() == characterPositionRow))
                gc.drawImage(solutionImg, position.getState().getColumnIndex() * x, position.getState().getRowIndex() * y, x, y);
        }
    }

    public void showSolution(Solution sol) {
        solution = sol;
        showSolution = true;
    }

    public void cancelSolution(){
        showSolution = false;
    }

    public void newMaze(){
        showSolution = false;

    }

    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameGoal() {
        return ImageFileNameGoal.get();
    }

    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.ImageFileNameGoal.set(imageFileNameGoal);
    }

    public String getImageFileNameSolution() {
        return imageFileNameSolution.get();
    }

    public void setImageFileNameSolution(String imageFileNameSolution) {
        this.imageFileNameSolution.set(imageFileNameSolution);
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }


}
