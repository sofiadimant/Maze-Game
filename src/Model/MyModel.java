package Model;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.*;
import algorithms.search.*;
import Client.*;
import Server.*;
import javafx.scene.input.KeyCode;

import javax.swing.text.Position;


/**
 * Created by ofir on 04/06/2017.
 */
public class MyModel extends Observable implements IModel{

    Maze m_maze=null;
    int [][] MazeSolution;//TODO INITIAT
    Server ServerMazeGenerate;
    Server serverMazeSolver;
    boolean win;
    private int startRow;
    private int startcol;
    private int goalRow;
    private int goalcol;

    public void startServers(){
       ServerMazeGenerate = new Server(5400,1000, new ServerStrategyGenerateMaze());
       serverMazeSolver = new Server(5401,1000, new ServerStrategySolveSearchProblem());
       ServerMazeGenerate.start();
       serverMazeSolver.start();
    }


    public void exitGame(){
        System.out.println("server is closed");
        ServerMazeGenerate.stop();
        serverMazeSolver.stop();
    }

    public int getStartRow() { return startRow; }

    public int getStartcol() { return startcol; }

    public int getGoalRow() {return goalRow;}

    public int getGoalcol() {return goalcol;}


    //TODO
    private int characterPositionRow = 0;
    private int characterPositionColumn = 0;

    public int[][] getMazeSolution(){
        return  MazeSolution;
    }

    public int[][] getMaze() {return m_maze.getmMaze();}

    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public  boolean getWinStatus(){ return win;}

    public void moveCharacter(KeyCode movement) {
        if(movement == KeyCode.UP){
            if(checkIfLegal(characterPositionRow-1,characterPositionColumn)){
                characterPositionRow = characterPositionRow-1;
                setChanged();
                checkIfWin();
                notifyObservers(1);
            }
        }
        if(movement== KeyCode.DOWN){
            if(checkIfLegal(characterPositionRow+1,characterPositionColumn)){
                characterPositionRow = characterPositionRow+1;
                setChanged();
                checkIfWin();
                notifyObservers(1);
            }
        }
        if(movement == KeyCode.RIGHT){
            if(checkIfLegal(characterPositionRow,characterPositionColumn+1)) {
                characterPositionColumn = characterPositionColumn +1;
                checkIfWin();
                setChanged();
                notifyObservers(1);
            }
        }
        if(movement== KeyCode.LEFT){
            if(checkIfLegal(characterPositionRow,characterPositionColumn-1)){
                characterPositionColumn = characterPositionColumn -1;
                checkIfWin();
                setChanged();
                notifyObservers(1);
            }
        }
    }

    private boolean checkIfLegal(int row, int col){
        if(m_maze.getmMaze().length <= row || 0 > row || m_maze.getmMaze()[0].length <= col || 0 > col){
            return false;
        }
        if(m_maze.getmMaze()[row][col]==1){
            return false;
        }
        return true;
    }

    private void checkIfWin(){
        if(characterPositionRow == m_maze.getGoalPosition().getRowIndex())
            if (characterPositionColumn == m_maze.getGoalPosition().getColumnIndex()){
            win=true;
            }
    }

    public void generateBord(int rowNum, int colNum) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rowNum, colNum};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])((byte[])fromServer.readObject());
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[2600];
                        is.read(decompressedMaze);
                        m_maze= new Maze(decompressedMaze);
                        characterPositionRow=0;
                        characterPositionColumn=0;
                        startRow= m_maze.getStartPosition().getRowIndex();
                        startcol = m_maze.getStartPosition().getColumnIndex();
                        goalRow= m_maze.getGoalPosition().getRowIndex();
                        goalcol = m_maze.getGoalPosition().getColumnIndex();
                        win = false;
                        setChanged();
                        notifyObservers(2);
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }

    }



    public void getSolution() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(m_maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        ArrayList<MazeState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                        translateSol(mazeSolutionSteps);
                        setChanged();
                        notifyObservers(3);
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

    private void translateSol(ArrayList<MazeState> mazeSolSteps){
        MazeSolution = m_maze.getmMaze();
        for(int i=0; i<mazeSolSteps.size(); i++){
            int r= mazeSolSteps.get(i).getM_myPosition().getRowIndex();
            int c= mazeSolSteps.get(i).getM_myPosition().getColumnIndex();
            MazeSolution[r][c] = 3;
        }
    }

    public void setNewData(int[][] arr, int cRow, int cCol,int goalR,int goalC)//so
    {
        int row=arr.length;
        int col=arr[0].length;

        generateBord(row,col);
        int [][]maze=new int[row][col];

        for(int i=0; i< maze.length;i++)
            for(int j=0; j< maze[0].length;j++)
            {
                if(arr[i][j]==2)
                    arr[i][j]=0;

                m_maze.getmMaze()[i][j]=arr[i][j];
            }
        characterPositionRow=cRow;
        characterPositionColumn=cCol;
        m_maze.getGoalPosition().setRowIndex(goalR);
        m_maze.getGoalPosition().setColumIndex(goalC);
        goalRow=goalR;
        goalcol=goalC;
        setChanged();
        notifyObservers(4);
    }

}
