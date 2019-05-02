package Model;


import javafx.scene.input.KeyCode;

/**
 * Created by ofir on 04/06/2017.
 */
public interface IModel {
    public void getSolution();
    public void generateBord(int rowNum, int colNum);
   // void generateMaze(int width, int height);
    void moveCharacter(KeyCode movement);
    int[][] getMaze();
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    boolean getWinStatus();
    int [][] getMazeSolution();
    int getStartRow();
    int getStartcol();
    int getGoalRow();
    int getGoalcol();
    void exitGame();
    public void setNewData(int[][] arr, int cRow, int cCol,int goalR,int goalC);

}
