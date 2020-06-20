package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;

public class MyViewController implements Observer, IView {

    @FXML
    private MyViewModel myViewModel;
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Label lbl_rowsNum;
    public javafx.scene.control.Label lbl_columnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;
    public javafx.scene.control.MenuItem btn_newMaze;
    public javafx.scene.control.MenuItem btn_saveMaze;
    public javafx.scene.control.MenuItem btn_loadMaze;
    public javafx.scene.control.MenuItem btn_options;
    public javafx.scene.control.MenuItem btn_help;


    public boolean isGameOver = false;
    public boolean haveMaze = false;
    public boolean showSolution = false;
    public boolean ctrlON = false;

    public static int counter = 0;
    private MediaPlayer mediaPlayer;

    public void setMyViewModel(MyViewModel myViewModel) {
        this.myViewModel = myViewModel;
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o == myViewModel) {
            isGameOver = myViewModel.isGameOver();
            if(isGameOver)
                playMusic();
            if (arg != null && arg == "solution") {
                mazeDisplayer.showSolution(myViewModel.getSolution());
            }
            displayMaze(myViewModel.getMaze());
            btn_generateMaze.setDisable(false);
        }
    }

    @Override
    public void displayMaze(Maze maze) {
        if (isGameOver) {
            mazeDisplayer.cancelSolution();
            Alert alert_gameOver = new Alert(Alert.AlertType.INFORMATION);
            alert_gameOver.setContentText(String.format("Great! Game Completed!"));
            alert_gameOver.show();
        }
        int characterPositionRow = myViewModel.getCharacterPositionRow();
        int characterPositionColumn = myViewModel.getCharacterPositionColumn();
        mazeDisplayer.setCharacterPosition(characterPositionRow, characterPositionColumn);
        this.characterPositionRow.set(characterPositionRow + "");
        this.characterPositionColumn.set(characterPositionColumn + "");
        mazeDisplayer.setMaze(maze);
    }

    public void generateMaze() {
        haveMaze = true;
        btn_solveMaze.setText("Solve Maze");
        String input_row = txtfld_rowsNum.getText();
        String input_col = txtfld_columnsNum.getText();
        try {
            int rows = Integer.parseInt(input_row);
            int cols = Integer.parseInt(input_col);
            if (rows < 2 || cols < 2) {
                showAlert("Please choose valid dimensions. (rows and columns bigger than 1)");
            } else {
                btn_generateMaze.setDisable(true);
                isGameOver = false;
                showSolution = false;
                myViewModel.generateMaze(rows, cols);
                btn_solveMaze.setDisable(false);
                mazeDisplayer.newMaze();

                playMusic();
            }
        } catch (Exception e) {
            showAlert("Please enter numbers.");
        }

    }

    public void playMusic(){
        String musicFile = "resources/sound/audio.mp3";
        if(mediaPlayer != null)
            mediaPlayer.stop();
        if(isGameOver){
            mediaPlayer.stop();
            musicFile = "resources/sound/winning.mp3";
        }
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    public void solveMaze(ActionEvent actionEvent) {
        if (haveMaze && !isGameOver && !showSolution) {
            myViewModel.solveMaze();
            showSolution = true;
            btn_solveMaze.setText("Hide Solution");
        }
        else if (haveMaze && !isGameOver && showSolution){
            showSolution = false;
            btn_solveMaze.setText("Solve Maze");
            myViewModel.hideSolution();
            mazeDisplayer.cancelSolution();
            mazeDisplayer.redraw();
        }
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void releaseCTRL(KeyEvent keyEvent){
        if (keyEvent.isControlDown())
            ctrlON = false;
    }

    public void KeyPressed(KeyEvent keyEvent) {
        if (keyEvent.isControlDown()){
            ctrlON = true;
        }
        else if (haveMaze) {
            myViewModel.moveCharacter(keyEvent.getCode());
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


    public void About(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("About");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 566, 100);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
        }
    }

    public void Options(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Options");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Options.fxml").openStream());

            Options options = fxmlLoader.getController();
            options.setMyViewModel(myViewModel);
            Scene scene = new Scene(root, 350, 200);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();

        } catch (Exception e) {

        }
    }

    public void Help(ActionEvent actionEvent) {
        Pane pane = new Pane();
        Stage newStage = new Stage();
        String path = "resources/Images/help.png";
        Image help = new Image(Paths.get(path).toUri().toString());
        pane.getChildren().add(new ImageView(help));
        Scene scene = new Scene(pane, 630, 300);
        newStage.setScene(scene);
        newStage.show();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        this.mazeDisplayer.requestFocus();
    }

    public void saveMaze(ActionEvent actionEvent) {
        if (!haveMaze) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(String.format("Please start a maze first."));
            alert.show();
        } else {
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
                myViewModel.saveMaze(dest);
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
            myViewModel.loadMaze(dest);
            haveMaze = true;
        }
    }


    // windows size:
    public void setResizeEvent(Scene scene) {
        long width = 0;
        long height = 0;
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                mazeDisplayer.redraw();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                mazeDisplayer.redraw();
            }
        });
    }

    //Zoom:
    public void zoomScreen(ScrollEvent event){
        if (ctrlON){
            double zoomScale;
            if (event.isControlDown()) {
                zoomScale = 1.5;
                double deltaY = event.getDeltaY();
                if(deltaY < 0){
                    zoomScale = 1/ zoomScale;
                }
                zoom(mazeDisplayer, zoomScale, event.getSceneX(), event.getSceneY());
                mazeDisplayer.setScaleX(mazeDisplayer.getScaleX() * zoomScale);
                mazeDisplayer.setScaleY(mazeDisplayer.getScaleY() * zoomScale);
                event.consume();
            }
        }
    }
    private Timeline timeline = new Timeline(60);
    public void zoom(Node node, double factor, double x, double y) {
        // determine scale
        double oldScale = node.getScaleX();
        double scale = oldScale * factor;
        double f = (scale / oldScale) - 1;

        // determine offset that we will have to move the node
        Bounds bounds = node.localToScene(node.getBoundsInLocal());
        double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
        double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));

        // timeline that scales and moves the node
        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), node.getTranslateX() - f * dx)),
                new KeyFrame(Duration.millis(200), new KeyValue(node.translateYProperty(), node.getTranslateY() - f * dy)),
                new KeyFrame(Duration.millis(200), new KeyValue(node.scaleXProperty(), scale)),
                new KeyFrame(Duration.millis(200), new KeyValue(node.scaleYProperty(), scale))
        );
        timeline.play();
    }

    public void Exit(ActionEvent actionEvent) {
        myViewModel.exit();
        Platform.exit();

    }


    //options:


}
