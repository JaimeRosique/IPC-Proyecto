/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Navigation;

/**
 * FXML Controller class
 *
 * @author apari
 */
public class ModificarSesionController implements Initializable {

    
    
    private String dest;
    private Image[] imgArray = new Image[12];
    private Navigation nav;
    private int i;
    @FXML
    private TextField nickname;
    @FXML
    private PasswordField pswrd;
    @FXML
    private DatePicker bdate;
    @FXML
    private TextField email;
    @FXML
    private ImageView avatar_izq;
    @FXML
    private ImageView avatar_img;
    @FXML
    private ImageView avatar_der;
    @FXML
    private PasswordField pswrd_check;
    @FXML
    private Label user_error;
    @FXML
    private Label pswrd_error;
    @FXML
    private Label pswrd_check_error;
    @FXML
    private Label email_error;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
