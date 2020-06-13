package Model;

import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import Client.*;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyModel extends Observable implements IModel {
    private Maze maze;
    private int playerRow;
    private int playerCol;
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private Server severGenerate;
    private Server serverSolve;
    private Solution solution;


    //Lielle: constructor?
    public MyModel(){
        severGenerate = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        serverSolve = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    }

    public void startServers() {
        severGenerate.start();
        serverSolve.start();
    }

    public void stopServers() {
        severGenerate.stop();
        serverSolve.stop();
    }

    // GENERATE MAZE:
    @Override
    public void generateMaze(int rows, int cols) {
        //Generate maze
        startServers();
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();

                        int[] mazeDimensions = new int[]{rows, cols};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();

                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[rows*cols+12 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        stopServers();

    }

    // LIELlE@gmail.com YOU HAVE TO COMPLETE THIS BY YOURSELF!
    @Override
    public int[][] getMaze() {
        return new int[0][0];
    }

    public boolean isLegal(int row, int col){
        return maze.isLegalPosition(new Position(row, col)) && !maze.isWall(new Position(row, col));
    }

    //SOLVING MAZE:
    public void solveMaze(Maze maze){
        startServers();

        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();

                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();

                        solution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        stopServers();
    }

    //THE PLAYER:

    @Override
    public void moveCharacter(KeyCode movement) {

        switch (movement) {
            case UP:
            case NUMPAD8:
                if(isLegal(playerRow-1, playerCol))
                    playerRow--;
                break;
            case DOWN:
            case NUMPAD2:
                if(isLegal(playerRow+1, playerCol))
                    playerRow++;
                break;
            case RIGHT:
            case NUMPAD6:
                if(isLegal(playerRow, playerCol+1))
                playerCol++;
                break;
            case LEFT:
            case NUMPAD4:
                if(isLegal(playerRow, playerCol-1))
                playerCol--;
                break;
            case NUMPAD1:
                if(isLegal(playerRow+1, playerCol-1)) {
                    playerRow++;
                    playerCol--;
                }
                break;
            case NUMPAD3:
                if(isLegal(playerRow+1, playerCol+1)) {
                    playerRow++;
                    playerCol++;
                }
                break;
            case NUMPAD7:
                if(isLegal(playerRow-1, playerCol-1)) {
                    playerRow--;
                    playerCol--;
                }
                break;
            case NUMPAD9:
                if(isLegal(playerRow-1, playerCol+1)) {
                    playerRow--;
                    playerCol++;
                }
                break;



        }
        setChanged();
        notifyObservers();
    }



    @Override
    public int getCharacterPositionRow() {
        return playerRow;
    }

    @Override
    public int getCharacterPositionCol() {
        return playerCol;
    }





}
