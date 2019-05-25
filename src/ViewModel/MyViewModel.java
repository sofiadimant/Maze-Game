package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Position;
import javafx.scene.input.KeyCode;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer{

    IModel m_model;
    private int characterPositionRow; //For Binding
    private int characterPositionColumn; //For Binding

    public ArrayList<Position> SolutionSteps;
    public int[][] m_bord;

    public MyViewModel(IModel model){
        m_model=model;
        SolutionSteps = new ArrayList<Position>();
       // m_bord = new int[][];
    }

    public void getSolution(){
        m_model.getSolution();
    }

    public void generateBord(int rowNum, int colNum){
        m_model.generateBord(rowNum,colNum);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o==m_model){
            setChanged();
            notifyObservers(arg);
            //characterPositionRow = model.getCharacterPositionRow();
            //characterPositionColumn = model.getCharacterPositionColumn();
        }
    }

    public void generateMaze(int width, int height){m_model.generateBord(width, height);}

    public void moveCharacter(KeyCode movement){
        m_model.moveCharacter(movement);
    }

    public int[][] getMaze() {
        return m_model.getMaze();
    }

    public int getCharacterPositionRow() {
        return m_model.getCharacterPositionRow();
    }

    public int getCharacterPositionColumn() { return m_model.getCharacterPositionColumn(); }

    public void solveMaze(){ m_model.getSolution();}

    public int[][] getMazeSolution(){ return m_model.getMazeSolution();}

    public boolean getWinStatus(){ return m_model.getWinStatus();}

    public int getStartRow(){ return  m_model.getStartRow();}

    public int getStartcol(){ return m_model.getStartcol();}

    public int getGoalRow(){ return m_model.getGoalRow();}

    public int getGoalcol(){ return m_model.getGoalcol();}

    public void exitGame(){ m_model.exitGame();}

    public void setNewData(int[][] arr, int cRow,int cCol,int goalR,int goalC) {m_model.setNewData(arr,cRow,cCol,goalR,goalC);}
}
