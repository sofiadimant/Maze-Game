package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyViewModle extends Observable implements Observer{

    IModel m_model;
    public ArrayList<Position> SolutionSteps;
    public int[][] m_bord;

    public MyViewModle(IModel model){
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
        if(o==m_model){

            m_model.getSolution();
        }



    }
}
