package View;


import ViewModel.MyViewModel;
import javafx.event.ActionEvent;


import java.io.*;

public class Options {
    private MyViewModel myViewModel;
    public javafx.scene.control.ComboBox solvingAlgo;
    public javafx.scene.control.ComboBox mazeGenAlgo;
    public javafx.scene.control.Button button;

    public javafx.scene.control.Button save_btn;

    public void setMyViewModel(MyViewModel myViewModel) {
        this.myViewModel = myViewModel;
    }

    public void setPropreties(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        String input_searchAlgorithm= "BestFirstSearch";
        if (solvingAlgo.getValue() != null){
             input_searchAlgorithm = solvingAlgo.getValue().toString();
            if (input_searchAlgorithm.equals("Breadth First Search"))
                input_searchAlgorithm = "BreadthFirstSearch";
            else if (input_searchAlgorithm.equals("Depth First Search"))
                input_searchAlgorithm = "DepthFirstSearch";
            else
                input_searchAlgorithm = "BestFirstSearch";
        }

        String input_mazeGen = "MyMazeGenerator";
        if (mazeGenAlgo.getValue() != null){
            input_mazeGen= mazeGenAlgo.getValue().toString();
            if (input_mazeGen.equals("Empty Maze"))
                input_mazeGen = "EmptyMazeGenerator";
            else if (input_mazeGen.equals("Simple Maze"))
                input_mazeGen = "SimpleMazeGenerator";
            else
                input_mazeGen = "MyMazeGenerator";
        }
        myViewModel.setProperties(input_mazeGen, input_searchAlgorithm);
    }

    public void check(){
        myViewModel.check();
    }
}