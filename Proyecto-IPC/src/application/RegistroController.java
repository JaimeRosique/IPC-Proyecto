/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.*;

/**
 * FXML Controller class
 *
 * @author jrosi
 */
public class RegistroController implements Initializable {

    private Navigation nav;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            nav = Navigation.getInstance();
        }catch(Exception e) {
            System.err.println(e.toString());
        }
        imgSetUp();
    }    
    
    // Metodo que inicializa las 12 imagenes de imgArray 
    private void imgSetUp() {
        /*
        lF = new Image("images/Flecha-Izq.png", 140, 150, false, true);
        dF= new Image("images/Flecha-Der.png", 140, 150, false, true);
        imgArray[0] = new Image("avatars/default.png", 140, 150, false, true);
        imgArray[1] = new Image("avatars/men.png", 140, 150, false, true);
        imgArray[2] = new Image("avatars/men2.png", 140, 150, false, true);
        imgArray[3] = new Image("avatars/men3.png", 140, 150, false, true);
        imgArray[4] = new Image("avatars/men4.png", 140, 150, false, true);
        imgArray[5] = new Image("avatars/men5.png", 140, 150, false, true);
        imgArray[6] = new Image("avatars/woman.png", 140, 150, false, true);
        imgArray[7] = new Image("avatars/woman2.png", 140, 150, false, true);
        imgArray[8] = new Image("avatars/woman3.png", 140, 150, false, true);
        imgArray[9] = new Image("avatars/woman4.png", 140, 150, false, true);
        imgArray[10] = new Image("avatars/woman5.png", 140, 150, false, true);
        imgArray[11] = new Image("avatars/woman6.png", 140, 150, false, true);
        imgUser.setImage(imgArray[0]);
        lArrow.setImage(lF);
        dArrow.setImage(dF);
        i = 0;
        */
    }
    
    //Método para comprobar los campos
    private boolean checkCampos() {
        boolean mandado = false;
        
        String user_name = nickname.getText().trim();
        String emailText = email.getText();
        
        boolean validUser = !user_name.isEmpty();
        boolean validEmail = !emailText.isEmpty() && !emailText.contains(" ");
        boolean nickNoUsado = !nav.exitsNickName(nickname.getText());
        boolean pwd1Valida = passwrd1.getText().length() >= 6;
        boolean pwd2Valida = passwrd2.getText().equals(passwrd1.getText());
        
        nombreErrImg.setVisible(!nombreValido);
        nombreErr.setStyle(nombreValido ? "-fx-text-fill: #7c7c7c;" : "-fx-text-fill: #fc0000;");
        if(!mandado && !nombreValido){nombre.requestFocus(); mandado = true;}
        
        apellidosErrImg.setVisible(!apellidosValidos);
        apellidoErr.setStyle(apellidosValidos ? "-fx-text-fill: #7c7c7c;" : "-fx-text-fill: #fc0000;");
        if(!mandado && !apellidosValidos){apellidos.requestFocus(); mandado = true;}
        
        nikErrImg.setVisible(nick.getText().isEmpty());
        nikErr.setStyle(nick.getText().isEmpty() ?  "-fx-text-fill: #fc0000;" : "-fx-text-fill: #7c7c7c;");
        if(!mandado && !nickValido){nick.requestFocus(); mandado = true;}
        
        passwrdErrImg1.setVisible(passwrd1.getText().isEmpty());
        passwrdErr1.setStyle(passwrd1.getText().isEmpty() ?  "-fx-text-fill: #fc0000;" : "-fx-text-fill: #7c7c7c;");
        if(!mandado && !pwd1Valida){passwrd1.requestFocus(); mandado = true;}
        
        passwrdErrImg2.setVisible(!passwrd2.getText().equals(passwrd1.getText()));
        passwrdErr2.setStyle(passwrd2.getText().equals(passwrd1.getText()) ?  "-fx-text-fill: #fc0000;" : "-fx-text-fill: #7c7c7c;");
        if(!mandado && !pwd2Valida){passwrd2.requestFocus(); mandado = true;}
        
        emailErrImg.setVisible(email.getText().isEmpty());
        emailErr.setStyle(email.getText().isEmpty() ?  "-fx-text-fill: #fc0000;" : "-fx-text-fill: #7c7c7c;");
        if(!mandado && !emailValido){email.requestFocus(); mandado = true;}
        
        return nombreValido && apellidosValidos && emailValido && nickValido && nickNoUsado && pwd1Valida && pwd2Valida;
    }
}
