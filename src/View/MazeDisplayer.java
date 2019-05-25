package View;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.text.View;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by ofir on 09/06/2017.
 */
public class MazeDisplayer extends Canvas {


    //1 is a wall
    private int[][] mazeData;
    private int[][] mazeSolution;
    private int cRow = 0;
    private int cCol = 0;

    private StringProperty wallFileName;
    private StringProperty rockFileName;
    private StringProperty gateFileName;
    private StringProperty goldFileName;
    private StringProperty playFileName;
    private StringProperty solutionFileName;
    private StringProperty winFileName;

    private int startRow;
    private int startcol;
    private int goalRow;
    private int goalcol;
    public boolean hide;

    public int getGoalRow() {return goalRow;}

    public int getGoalcol() { return goalcol;}

    public void setStartRow(int startRow) {this.startRow = startRow; }

    public void setStartcol(int startcol) {this.startcol = startcol;}

    public void setGoalRow(int goalRow) {this.goalRow = goalRow;}

    public void setGoalcol(int goalcol) {this.goalcol = goalcol;}

    public int[][] getMazeData() {
        return mazeData;
    }

    public void setMazeData(int[][] mazeData){
        this.mazeData = mazeData;
      //  fixMazeArray();
        redraw();
    }

    public void setMazeSolution(int[][] mazeSolution){
        this.mazeData = mazeData;
        redraw();
    }


    public void setCharacterPosition(int row, int col){
        this.cRow = row;
        this.cCol = col;
        redraw();
    }

    public void setHide(boolean hide) {this.hide = hide;}

    public MazeDisplayer(){
        wallFileName = new SimpleStringProperty();
        rockFileName = new SimpleStringProperty();
        gateFileName = new SimpleStringProperty();
        goldFileName = new SimpleStringProperty();
        playFileName = new SimpleStringProperty();
        solutionFileName = new SimpleStringProperty();
        winFileName = new SimpleStringProperty();
        //initiate cRow and cCol at start and end position
        cRow=0;
        cCol=0;
        hide=true;

    }

    //TODO- change to position
    public int getcRow() {return cRow;}

    public int getcCol() {return cCol;}

    public double getCellHight(){
        double H = getHeight();
        double h = H/mazeData.length;
        return h;
    }

    public double getCellWidth(){
        double W = getWidth();
        double w = W/mazeData[0].length;
        return w;
    }


    public void setWallFileName(String wallFileName) {
        this.wallFileName.set(wallFileName);
    }

    public void setRockFileName(String rockFileName) {
        this.rockFileName.set(rockFileName);
    }

    public void setGateFileName(String gateFileName) {
        this.gateFileName.set(gateFileName);
    }

    public void setGoldFileName(String goldFileName) {
        this.goldFileName.set(goldFileName);
    }

    public void setPlayFileName(String playFileName) {this.playFileName.set(playFileName);}

    public void setSolutionFileName(String solutionFileName) {this.solutionFileName.set(solutionFileName);}

    public void setWinFileName(String winFileName) {this.winFileName.set(winFileName);}

    public String getWinFileName() {
        return winFileName.get();
    }

    public String getSolutionFileName() {
        return solutionFileName.get();
    }

    public String getWallFileName() {
        return wallFileName.get();
    }

    public String getRockFileName() {
        return rockFileName.get();
    }

    public String getGateFileName() {
        return gateFileName.get();
    }

    public String getGoldFileName() {
        return goldFileName.get();
    }

    public String getPlayFileName() {return  playFileName.get(); }


    public void redraw() {
        if(mazeData!=null){
            double W = getWidth();
            double H = getHeight();
            double w = W/mazeData[0].length;
            double h = H/mazeData.length;

            GraphicsContext gc = getGraphicsContext2D();
            Image wall= null;
            try {
                wall = new Image(new FileInputStream(wallFileName.get()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Image win= null;
            try {
                win = new Image(new FileInputStream(winFileName.get()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            Image rock= null;
            try {
                rock = new Image(new FileInputStream(rockFileName.get()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Image solution= null;
            try {
                solution = new Image(new FileInputStream(solutionFileName.get()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Image gate= null;
            try {
                gate = new Image(new FileInputStream(gateFileName.get()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Image gold= null;
            try {
                gold = new Image(new FileInputStream(goldFileName.get()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Image player= null;
            try {
                player = new Image(new FileInputStream(playFileName.get()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


            gc.clearRect(0,0,W,H);

            for(int i=0; i<mazeData.length; i++){
                for(int j=0; j<mazeData[i].length; j++){
                    if(mazeData[i][j]==1){
                        if(wall==null){
                            gc.fillRect(j*w, i*h, w, h);
                        }
                       else
                        gc.drawImage(wall,j*w, i*h, w, h);
                    }

                        if (mazeData[i][j] == 0) {
                            if (rock == null) {
                                gc.fillRect(j * w, i * h, w, h);
                            } else
                                gc.drawImage(rock, j * w, i * h, w, h);
                        }

                        if(mazeData[i][j]==3){
                            if(solution==null){
                                gc.fillRect(j*w, i*h, w, h);
                            }
                            else
                                gc.drawImage(solution,j*w, i*h, w, h);
                        }
                    if(hide == true){
                        if (mazeData[i][j] == 3) {
                            if (rock == null) {
                                gc.fillRect(j * w, i * h, w, h);
                            } else
                                gc.drawImage(rock, j * w, i * h, w, h);
                        }
                    }

                    if (i == startRow && j == startcol){
                        if(gate==null){
                            gc.fillRect(j*w, i*h, w, h);
                        }
                        else
                            gc.drawImage(gate,j*w, i*h, w, h);
                    }

                    if(i == goalRow && j == goalcol){
                        if(gold==null){
                            gc.fillRect(j*w, i*h, w, h);
                        }
                        else
                            gc.drawImage(gold,j*w, i*h, w, h);
                    }
                }
            }
            if(player==null){
                gc.fillRect(cCol*w, cRow*h, w, h);
            }
            else
                gc.drawImage(player,cCol*w, cRow*h, w, h);

        }


    }

  public void clear(){
      double W = getWidth();
      double H = getHeight();
      GraphicsContext gc = getGraphicsContext2D();
      gc.clearRect(0,0,W,H);
  }


}
