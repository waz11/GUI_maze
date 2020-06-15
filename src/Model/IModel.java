package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.File;

/**
 * Created by Aviadjo on 6/14/2017.
 */
public interface IModel {
    void generateMaze(int width, int height);
    void solveMaze();
    void moveCharacter(KeyCode movement);
    Maze getMaze();
    int getPlayer_row();
    int getPlayer_col();
    boolean isGameOver();
    Solution getSolution();
    void saveMaze(File dest);
    void loadMaze(File dest);

}
