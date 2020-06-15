package View;

import ViewModel.ViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class View implements Observer, IView {

    @FXML
    private ViewModel viewModel;
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;
    public javafx.scene.control.MenuItem btn_saveMaze;
    public boolean isGameOver = false;
    public boolean haveMaze = false;
    public boolean showSolution = false;

    public static int counter = 0;


    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            isGameOver = viewModel.isGameOver();
            if (arg != null && arg == "solution") {
                mazeDisplayer.showSolution(viewModel.getSolution());
            } else {
                displayMaze(viewModel.getMaze());
            }
            btn_generateMaze.setDisable(false);
        }
    }

    @Override
    public void displayMaze(Maze maze) {
        if (isGameOver) {
            Alert alert_gameOver = new Alert(Alert.AlertType.INFORMATION);
            alert_gameOver.setContentText(String.format("Great! Game Completed!"));
            alert_gameOver.show();
        }

        if(showSolution)
            mazeDisplayer.showSolution(viewModel.getSolution());
        else
            mazeDisplayer.setMaze(maze);
        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionColumn = viewModel.getCharacterPositionColumn();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionColumn.set(characterPositionColumn + "");

    }

    public void generateMaze() {
        haveMaze = true;
        int rows = Integer.valueOf(txtfld_rowsNum.getText());
        int cols = Integer.valueOf(txtfld_columnsNum.getText());

        btn_generateMaze.setDisable(true);
        isGameOver = false;
        showSolution = false;
        viewModel.generateMaze(rows, cols);
        btn_solveMaze.setDisable(false);
    }

    public void solveMaze(ActionEvent actionEvent) {
        if(haveMaze){
            viewModel.solveMaze();
            showSolution = true;
        }
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void KeyPressed(KeyEvent keyEvent) {
        if(haveMaze) {
            viewModel.moveCharacter(keyEvent.getCode());
            keyEvent.consume();
        }
    }

    //region String Property for Binding
    public StringProperty characterPositionRow = new SimpleStringProperty();
    public StringProperty characterPositionColumn = new SimpleStringProperty();

    public String getCharacterPositionRow() {
        return characterPositionRow.get();
    }
    public String getCharacterPositionColumn() {
        return characterPositionColumn.get();
    }

    public void setResizeEvent(Scene scene) {
        long width = 0;
        long height = 0;
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
//                System.out.println("Width: " + newSceneWidth);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
//                System.out.println("Height: " + newSceneHeight);
            }
        });
    }

    public void About(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("About");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 400, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {

        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        this.mazeDisplayer.requestFocus();
    }

    public void saveMaze(ActionEvent actionEvent) {
        if(!haveMaze) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(String.format("Please start a maze first."));
            alert.show();
        }
        else {
            FileChooser fc = new FileChooser();
            fc.setTitle("Save Maze");
            fc.setInitialFileName("maze_" + counter);
            counter++;
            File file = new File("./SavedMazes/");
            if (!file.exists())
                file.mkdir();
            fc.setInitialDirectory(file);
            File dest = fc.showSaveDialog(mazeDisplayer.getScene().getWindow());
            if (dest != null)
                viewModel.saveMaze(dest);
        }
    }

    public void loadMaze(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Load a Maze");

        File file = new File("./SavedMazes/");
        if (!file.exists())
            file = new File("./");

        fc.setInitialDirectory(file);
        File dest = fc.showOpenDialog(mazeDisplayer.getScene().getWindow());
        if (dest != null) {
            viewModel.loadMaze(dest);
            haveMaze = true;
        }

    }
}
