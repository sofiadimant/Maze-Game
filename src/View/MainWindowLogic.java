package View;

import ViewModel.MyViewModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Observable;
import java.util.Observer;


/**
 * Created by ofir on 06/06/2017.
 */
public class MainWindowLogic implements Observer {
    @FXML
    private MyViewModel viewModel;
    public MazeDisplayer mazeDisplayer;
    public javafx.scene.control.TextField rowsNum;
    public javafx.scene.control.TextField columnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solution;
    public javafx.scene.control.Button btn_hide;
    private boolean winStatus = false;
    private boolean gameMusic = false;
    public MediaPlayer mediaPlayer;


    public void setView(MyViewModel viewModel) {
        this.viewModel = viewModel;
        btn_solution.setDisable(true);
        btn_hide.setDisable(true);
    }
    @Override
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            mazeDisplayer.setStartRow(viewModel.getStartRow());
            mazeDisplayer.setStartcol(viewModel.getStartcol());
            mazeDisplayer.setGoalRow(viewModel.getGoalRow());
            mazeDisplayer.setGoalcol(viewModel.getGoalcol());

            int characterPositionRow = viewModel.getCharacterPositionRow();
            int characterPositionColumn = viewModel.getCharacterPositionColumn();
            CharacterRow.set(characterPositionRow + "");
            CharacterColumn.set(characterPositionColumn + "");

            if (arg != null) {
                if ((int) arg == 1) {
                    mazeDisplayer.setCharacterPosition(viewModel.getCharacterPositionRow(), viewModel.getCharacterPositionColumn());
                    winStatus = viewModel.getWinStatus();
                    CheckWinStatus();
                }
                if ((int) arg == 2) {
                    mazeDisplayer.setCharacterPosition(viewModel.getCharacterPositionRow(), viewModel.getCharacterPositionColumn());
                    mazeDisplayer.setMazeData(viewModel.getMaze());
                    btn_generateMaze.setDisable(false);
                }
                if ((int) arg == 3) {
                    mazeDisplayer.setMazeSolution(viewModel.getMazeSolution());
                    btn_solution.setDisable(false);
                }
                if ((int) arg == 4) {
                    mazeDisplayer.setCharacterPosition(viewModel.getCharacterPositionRow(), viewModel.getCharacterPositionColumn());
                    mazeDisplayer.setMazeData(viewModel.getMaze());
                    btn_generateMaze.setDisable(false);
                }
            }

        }
    }

    public void startMusic() {
        Media music = new Media(Paths.get("resources/music/Ween - Where'd The Cheese Go.mp3").toUri().toString());
        if(!gameMusic)
            gameMusic=true;
        else{
            mediaPlayer.stop();
             music=new Media(Paths.get("resources/music/ItsGoingDownForRealChorus.mp3").toUri().toString()) ;
             gameMusic=false;
        }
        mediaPlayer=new MediaPlayer(music);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();
    }

    private void  winShow() throws IOException {
        Stage s = new Stage();
        s.setTitle("YOU SOLVE THE MAZE!!");
        AnchorPane show = FXMLLoader.load(getClass().getResource("../View/win.fxml"));
        Scene scen = new Scene(show, 550, 330);
        s.setScene(scen);
        //TODO-EXIT BUUTEN
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.ps);
        setOnCloseRequest(s);
        startMusic();
        s.show();
        s.requestFocus();
    }

    public void setOnCloseRequest(Stage s){
        s.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                mediaPlayer.stop();
            }
        });
    }

    public void generateMaze() {
        try {
            Integer.parseInt(rowsNum.getText());
        } catch (NumberFormatException ex) {
            showAlert("Row Number - Please Enter Only Numbers");
            return;
        }
        try {
            Integer.parseInt(columnsNum.getText());
        } catch (NumberFormatException ex) {
            showAlert("Column Number - Please Enter Only Numbers");
            return;
        }
        try {
            int r = Integer.parseInt(rowsNum.getText());
            if (r <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            showAlert("Row Number - Please Enter Only  Positive Numbers");
            return;
        }
        try {
            int c = Integer.parseInt(columnsNum.getText());
            if (c <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            showAlert("Column Number - Please Enter Only  Positive Numbers");
            return;
        }
        try {
            int r = Integer.parseInt(rowsNum.getText());
            int c = Integer.parseInt(columnsNum.getText());
            if (r <= 3 || c <= 3) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            showAlert("To Easy - Please Enter Number Above 3");
            return;
        }
        try {
            int r = Integer.parseInt(rowsNum.getText());
            int c = Integer.parseInt(columnsNum.getText());
            if (r >50 || c >50) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            showAlert("Please Enter Numbers between 4 to 50");
            return;
        }
        int rows = Integer.valueOf(rowsNum.getText());
        int columns = Integer.valueOf(columnsNum.getText());
        btn_generateMaze.setDisable(true);
        viewModel.generateMaze(rows, columns);
        btn_solution.setDisable(false);
        if (!gameMusic)
            startMusic();

    }

    public void solveMaze() {
        mazeDisplayer.setHide(false);
        btn_solution.setDisable(true);
        viewModel.solveMaze();
        btn_hide.setDisable(false);
        btn_solution.setDisable(true);
    }

    public void BorderPaneKeyPress(KeyEvent keyEvent) {
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
    }

    public StringProperty CharacterRow = new SimpleStringProperty();

    public StringProperty CharacterColumn = new SimpleStringProperty();

    public String getCharacterRow() {
        return CharacterRow.get();
    }

    public StringProperty characterRowProperty() {
        return CharacterRow;
    }

    public String getCharacterColumn() {
        return CharacterColumn.get();
    }

    public StringProperty characterColumnProperty() {
        return CharacterColumn;
    }

    public void setResizeEvent(Scene scene, Stage primaryStage) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldSceneWidth, Number newSceneWidth) {
                if (mazeDisplayer.getMazeData() != null) {
                    mazeDisplayer.setWidth((double) newSceneWidth - 280);
                    mazeDisplayer.redraw();

                }
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newValue) {
                if (mazeDisplayer.getMazeData() != null) {
                    mazeDisplayer.setHeight(scene.getHeight() - 100);
                    mazeDisplayer.redraw();
                }
            }
        });
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void loadMaze() throws IOException, ClassNotFoundException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open resource file");
        FileChooser.ExtensionFilter fileExtension = new FileChooser.ExtensionFilter("Maze File (*.Maze)", "*.Maze");
        fileChooser.getExtensionFilters().add(fileExtension);
        File file = fileChooser.showOpenDialog((Stage) rowsNum.getScene().getWindow());
        if (file != null) {
            String fileAsString = file.toString();

            try (BufferedReader reader = new BufferedReader(new FileReader(fileAsString))) {
                String line;
                line = reader.readLine();
                String[] data = line.split("/");
                int row = Integer.valueOf(data[0]);

                int col = Integer.valueOf(data[1]);
                int charchterRow = Integer.valueOf(data[2]);
                int charchterCol = Integer.valueOf(data[3]);
                int goalRow = Integer.valueOf(data[4]);
                int goalCol = Integer.valueOf(data[5]);
                int mazeArr[][] = new int[row][col];

                int index = 1;
                line = line.substring(line.lastIndexOf("/"), line.length());

                String[] maze = line.split("");
                maze[1] = "0";
                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        mazeArr[i][j] = Integer.valueOf(maze[index]);
                        index++;
                    }
                }
                viewModel.setNewData(mazeArr, charchterRow, charchterCol, goalRow, goalCol);
                if (!gameMusic)
                    startMusic();//so
                mazeDisplayer.requestFocus();

            }
        }
    }

    public void SaveMaze() throws FileNotFoundException {

        try {
            int r = Integer.valueOf(rowsNum.getText());
            int c = Integer.valueOf(columnsNum.getText());
            String string = r + "/" + c;
            string = string + "/" + mazeDisplayer.getcRow() + "/" + mazeDisplayer.getcCol();
            string = string + "/" + mazeDisplayer.getGoalRow() + "/" + mazeDisplayer.getGoalcol() + "/";

            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    string += mazeDisplayer.getMazeData()[i][j];
                }
            }

            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Maze File (*.Maze)", "*.Maze");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog((Stage) rowsNum.getScene().getWindow());

            if (file != null) {
                try {
                    FileWriter fileWriter = null;
                    fileWriter = new FileWriter(file);
                    fileWriter.write(string);
                    fileWriter.close();
                } catch (IOException ex) {
                    System.out.println("Error");
                }
            }
        } catch (Exception e) {
            showAlert("The Maze is Empty! Please Push Start Game Before Saving");
        }
    }


    public void clearGame() {
        mazeDisplayer.clear();
        rowsNum.clear();
        columnsNum.clear();
        btn_solution.setDisable(true);
        btn_hide.setDisable(true);
        showAlert("READY TO START A NEW GAME");
    }

    public void exitGame() {
        viewModel.exitGame();
        Platform.exit();
    }

    private void CheckWinStatus() {
        if (winStatus == true) {
            try {
                clearAfterWin();
                winShow();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
    }

    public void clearAfterWin() {
        mazeDisplayer.clear();
        rowsNum.clear();
        columnsNum.clear();
        btn_solution.setDisable(true);
        btn_hide.setDisable(true);
    }


    public void About() throws IOException {
        Stage s = new Stage();
        s.setTitle("About Window");
        AnchorPane show = FXMLLoader.load(getClass().getResource("../View/About.fxml"));
        Scene scen = new Scene(show, 600, 400);
        scen.getStylesheets().add(getClass().getResource("AboutStyle.css").toExternalForm());
        s.setScene(scen);
        //TODO-EXIT BUTTEN
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.ps);
        s.show();
    }

    public void property() throws IOException {
        Stage s = new Stage();
        s.setTitle("Property Window");
        AnchorPane show = FXMLLoader.load(getClass().getResource("../View/property.fxml"));
        Scene scen = new Scene(show, 600, 400);
        scen.getStylesheets().add(getClass().getResource("ProStyle.css").toExternalForm());
        s.setScene(scen);
        //TODO-EXIT BUTTEN
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.ps);
        s.show();
    }

    public void help() throws IOException {
        Stage s = new Stage();
        s.setTitle("Help Window");
        AnchorPane show = FXMLLoader.load(getClass().getResource("../View/Help.fxml"));
        Scene scen = new Scene(show, 1000, 800);
        scen.getStylesheets().add(getClass().getResource("HelpStyle.css").toExternalForm());
        s.setScene(scen);
        //TODO-EXIT BUTTEN
        s.setResizable(false);
        s.initOwner(Main.ps);
        s.show();
    }


    public void hide() {
        mazeDisplayer.setHide(true);
        mazeDisplayer.redraw();
        btn_hide.setDisable(true);
        btn_solution.setDisable(false);
        mazeDisplayer.requestFocus();//so
    }

    public void onMouseDrag(MouseEvent movement) {
        if (mazeDisplayer.getMazeData() == null)
            return;

        int mouseX = (int) ((movement.getX() - 250) / (mazeDisplayer.getCellHight()));
        int mouseY = (int) ((movement.getY() - 34) / (mazeDisplayer.getCellWidth()));

        if (Math.abs(viewModel.getCharacterPositionRow() - mouseY) < 2 || (Math.abs(viewModel.getCharacterPositionColumn() - mouseX) < 2)) {
            if (mouseY < viewModel.getCharacterPositionRow()) {
                viewModel.moveCharacter(KeyCode.UP);
            }
            if (mouseY > viewModel.getCharacterPositionRow()) {
                viewModel.moveCharacter(KeyCode.DOWN);
            }
            if (mouseX < viewModel.getCharacterPositionColumn()) {
                viewModel.moveCharacter(KeyCode.LEFT);
            }
            if (mouseX > viewModel.getCharacterPositionColumn()) {
                viewModel.moveCharacter(KeyCode.RIGHT);
            }
        }
        mazeDisplayer.requestFocus();

    }

    public void setSceneEvents(final Scene scene) {
        //handles mouse scrolling
        scene.setOnScroll(new EventHandler<ScrollEvent>() {
            Stage stage = (Stage) btn_generateMaze.getScene().getWindow();

            @Override
            public void handle(ScrollEvent event) {
                double zoomFactor = 1.05;
                double deltaY = event.getDeltaY();
                if (deltaY < 0) {
                    zoomFactor = 2.0 - zoomFactor;
                }
                mazeDisplayer.setScaleX(mazeDisplayer.getScaleX() * zoomFactor);
                mazeDisplayer.setScaleY(mazeDisplayer.getScaleY() * zoomFactor);
                event.consume();
            }
        });

    }
}

