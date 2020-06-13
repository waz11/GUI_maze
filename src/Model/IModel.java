package Model;

import javafx.scene.input.KeyCode;

public interface IModel {
    void generateMaze(int width, int height); //Lielle: is it the opposite from the original generate maze?
    int[][] getMaze();

    void moveCharacter(KeyCode movement);

    int getCharacterPositionRow();
    int getCharacterPositionCol();
}
