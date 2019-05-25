package View;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import java.awt.*;
import java.awt.Canvas;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * Created by ofir on 18/06/2017.
 */
public class winControler implements Initializable {

    @FXML
    public ImageView winImege;
    public MediaPlayer mediaPlayer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        File file = new File("resources/Images/win.jpg");
        Image image = new Image(file.toURI().toString());
        winImege.setImage(image);

    }

}
