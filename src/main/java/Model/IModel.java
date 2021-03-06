package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface IModel {
    Maze generateMaze(int width, int height);
    void solveMaze();
    void moveCharacter(KeyCode movement);
    Maze getMaze();
    int getPlayer_row();
    int getPlayer_col();
    boolean isGameOver();
    Solution getSolution();
    void saveMaze(File dest);
    void loadMaze(File dest);
    public void setProperties(String generate, String solving) throws IOException;
    public void exit();
    public void hideSolution();
    public String[] getInfo();
}
