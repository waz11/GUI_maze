package Model;

import Client.*;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import Server.*;
import algorithms.mazeGenerators.EmptyMazeGenerator;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyModel extends Observable implements IModel {
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private int player_row = 0;
    private int player_col = 0;
    private int goal_row = 0;
    private int goal_col = 1;

    private Server severGenerate;
    private Server serverSolve;
    private boolean isGameOver = false;
    private boolean solveMaze = false;
    private Maze maze;

    Position startPosition;
    Position goalPosition;

    public MyModel() {
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


    @Override
    public void generateMaze(int rows, int cols) {
        threadPool.execute(() -> {
            getMazeFromServer(rows, cols);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers();
        });
        solveMaze = false;
    }


    private Maze getMazeFromServer(int rows, int cols) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
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
                        byte[] decompressedMaze = new byte[rows * cols + 12 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        player_row = maze.getStartPosition().getRowIndex();
                        player_col = maze.getStartPosition().getColumnIndex();

                        goal_row = maze.getGoalPosition().getRowIndex();
                        goal_col = maze.getGoalPosition().getColumnIndex();
                        isGameOver = false;

                        toServer.close();
                        fromServer.close();
                    } catch (Exception e) {
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
        }

        return maze;
    }

    public void solveMaze() {
        threadPool.execute(() -> {
            solveMazeServer();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setChanged();
            notifyObservers("solution");
        });
        solveMaze = true;
    }

    public Solution solution;

    public void solveMazeServer() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        maze.setStart(new Position(player_row, player_col));
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

    }

    public Solution getSolution() {
        return solution;
    }


    @Override
    public Maze getMaze() {
        return maze;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    @Override
    public void moveCharacter(KeyCode movement) {
        if (!isGameOver) {
            int rows = maze.getRows();
            int cols = maze.getCols();
            int row = player_row;
            int col = player_col;
            switch (movement) {
                case UP:
                case NUMPAD8:
                    if (isLegal(player_row - 1, player_col))
                        player_row--;
                    break;
                case DOWN:
                case NUMPAD2:
                    if (isLegal(player_row + 1, player_col))
                        player_row++;
                    break;
                case RIGHT:
                case NUMPAD6:
                    if (isLegal(player_row, player_col + 1))
                        player_col++;
                    break;
                case LEFT:
                case NUMPAD4:
                    if (isLegal(player_row, player_col - 1))
                        player_col--;
                    break;
                case NUMPAD1:
                    if (isLegal(player_row + 1, player_col - 1)) {
                        player_row++;
                        player_col--;
                    }
                    break;
                case NUMPAD3:
                    if (isLegal(player_row + 1, player_col + 1)) {
                        player_row++;
                        player_col++;
                    }
                    break;
                case NUMPAD7:
                    if (isLegal(player_row - 1, player_col - 1)) {
                        player_row--;
                        player_col--;
                    }
                    break;
                case NUMPAD9:
                    if (isLegal(player_row - 1, player_col + 1)) {
                        player_row--;
                        player_col++;
                    }
                    break;
            }


            if (player_row == maze.getGoalPosition().getRowIndex() && player_col == maze.getGoalPosition().getColumnIndex())
                isGameOver = true;
            if (solveMaze)
                solveMaze();
            setChanged();
            notifyObservers();
        }
    }

    @Override
    public int getPlayer_row() {
        return player_row;
    }

    @Override
    public int getPlayer_col() {
        return player_col;
    }

    private boolean isLegal(int row, int col) {
        boolean ans = maze.isLegalPosition(new Position(row, col)) && maze.isPath(new Position(row, col));
        return maze.isLegalPosition(new Position(row, col)) && maze.isPath(new Position(row, col));
    }

    public int getGoal_row() {
        return goal_row;
    }

    public void setGoal_row(int goal_row) {
        this.goal_row = goal_row;
    }

    public int getGoal_col() {
        return goal_col;
    }

    public void setGoal_col(int goal_col) {
        this.goal_col = goal_col;
    }


    public void saveMaze(File file) {
        try {
            FileOutputStream outFile = new FileOutputStream(file);
            ObjectOutputStream returnFile = new ObjectOutputStream(outFile);
            Position start = maze.getStartPosition();
            Position goal = maze.getGoalPosition();
            maze.setStart(new Position(player_row, player_col));
            returnFile.writeObject(maze);
            returnFile.flush();
            maze.setStart(start);
            maze.setGoal(goal);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void loadMaze(File dest) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(dest);
            ObjectInputStream returnFile = new ObjectInputStream(in);
            Maze savedMaze = (Maze) returnFile.readObject();
            returnFile.close();
            maze = savedMaze;

            int[][] maze_arr = new int[savedMaze.getRows()][savedMaze.getCols()];
            for (int row = 0; row < savedMaze.getRows(); row++) {
                for (int column = 0; column < savedMaze.getCols(); column++)
                    if (maze.isPath(new Position(row, column)))
                        maze_arr[row][column] = 0;
                    else
                        maze_arr[row][column] = 1;
            }
            startPosition = maze.getStartPosition();
            goalPosition = maze.getGoalPosition();
            isGameOver = false;
            player_row = startPosition.getRowIndex();
            player_col = startPosition.getColumnIndex();
            setChanged();
            notifyObservers();
        } catch (ClassNotFoundException e) {
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

    }

    public void setProperties(String generate, String solving) throws IOException {
        File file = new File("resources/config.properties");
        Properties properties = new Properties();

//        if(file.length() == 0)
//            Server.Configurations.configurations();

        InputStream input = new FileInputStream("resources/config.properties");
        OutputStream out = new FileOutputStream("./resources/config.properties");

        properties.load(input);
        properties.setProperty("generator", generate);
        properties.setProperty("searchAlgorithm", solving);
        properties.setProperty("threads", "5");
        properties.store(out, null);


    }


}


