package View;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Created by ofir on 19/06/2017.
 */
public class propertyControler implements Initializable {

    @FXML
    //  public TextField txt_threads;
//    public ImageView pizzaImage;
    public Button btm_apply;
    public ComboBox cb_algo;
    public ComboBox cb_generator;
    public javafx.scene.control.Button btn_apply;

    ObservableList<String> algorithems = FXCollections.observableArrayList("BreadthFirstSearch", "BestFirstSearch", "DepthFirstSearch");
    ObservableList<String> generators = FXCollections.observableArrayList("MyMazeGenerator", "SimpleMazeGenerator");

    public Properties prop;


    public void initialize(URL location, ResourceBundle resources) {
        /*File file = new File("resources/Images/pizza3.jpg");
        Image image = new Image(file.toURI().toString());
        pizzaImage.setImage(image);*/

        cb_algo.setItems(algorithems);
        cb_generator.setItems(generators);


        try {
            FileInputStream f = new FileInputStream("./config.properties");//TODO
            prop = new Properties();
            prop.load(f);
            f.close();
            cb_algo.setValue(prop.getProperty("Searching Algorithm"));
            cb_generator.setValue(prop.getProperty("Generating Algorithm"));
            //  txt_threads.setText(prop.getProperty("Threads No"));
        } catch (IOException e) {

        }
    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    private void showAlert2(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void apply() {
        try {

            FileOutputStream f = new FileOutputStream("./config.properties");
            prop.setProperty("Searching Algorithm", cb_algo.getSelectionModel().getSelectedItem().toString());
            prop.setProperty("Generating Algorithm", cb_generator.getSelectionModel().getSelectedItem().toString());
            prop.store(f, null);
            f.close();
            showAlert("Properties Saved!");
            Stage s = (Stage) btm_apply.getScene().getWindow();
            s.close();
        } catch (Exception e) {

            showAlert2("Please Choose Searching Algorithm and Generating Algorithm Before Clicking Apply");
            {

            }

        }
    }
}