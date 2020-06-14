package Model;

import javafx.scene.input.KeyCode;

/**
 * Created by Aviadjo on 6/14/2017.
 */
public interface IModel {
    void generateMaze(int width, int height);
    void moveCharacter(KeyCode movement);
    int[][] getMaze();
    int getPlayer_row();
    int getPlayer_col();
}
