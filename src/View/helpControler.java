package View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ofir on 19/06/2017.
 */
public class helpControler implements Initializable{

    @FXML
   // public ImageView pizzaImage;
    public ImageView startImage;
    public ImageView endImage;
    public ImageView pizzaManImage;
    public ImageView upImage;
    public ImageView downImage;
    public ImageView rightImage;
    public ImageView leftImage;
    public ImageView roadImage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
       /* File file = new File("resources/Images/pizza3.jpg");
        Image image = new Image(file.toURI().toString());
        pizzaImage.setImage(image);*/

        File startFile = new File("resources/Images/gate.jpg");
        Image imageStart = new Image(startFile.toURI().toString());
        startImage.setImage(imageStart);

        File endFile = new File("resources/Images/gold.jpg");
        Image imageEnd = new Image(endFile.toURI().toString());
        endImage.setImage(imageEnd);

        File pizzaManFile = new File("resources/Images/player.jpg");
        Image imagePizzaMan = new Image(pizzaManFile.toURI().toString());
        pizzaManImage.setImage(imagePizzaMan);

        File roadFile = new File("resources/Images/wall1.jpg");
        Image imageRoad = new Image(roadFile.toURI().toString());
        roadImage.setImage(imageRoad);

        //errows
        File leftFile = new File("resources/Images/left.jpg");
        Image imageLeft = new Image(leftFile.toURI().toString());
        upImage.setImage(imageLeft);
        downImage.setImage(imageLeft);
        leftImage.setImage(imageLeft);
        rightImage.setImage(imageLeft);
    }

    public void menuBar() throws IOException {
        Stage s= new Stage();
        s.setTitle("Help Window");
        AnchorPane show= FXMLLoader.load(getClass().getResource("../View/manuBarHelp.fxml"));
        Scene scen = new Scene(show, 800,400);
        scen.getStylesheets().add(getClass().getResource("ManuBar.css").toExternalForm());
        s.setScene(scen);
        //TODO-EXIT BUUTEN
        s.setResizable(false);
        s.initModality(Modality.WINDOW_MODAL);
        s.initOwner(Main.ps);
        s.show();
    }
}
