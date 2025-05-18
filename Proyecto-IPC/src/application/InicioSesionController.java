/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import model.*;

/**
 * FXML Controller class
 *
 * @author jrosi
 */
public class InicioSesionController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private String dest;
    Navigation nav;
    @FXML
    private TextField user_id;
    @FXML
    private Label userError;
    @FXML
    private Label pswrdError;
    @FXML
    private TextField pswrd_id;
    @FXML
    private ImageView userErrorImg;
    @FXML
    private ImageView pswrdErrorImg;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            nav = Navigation.getInstance();
        }catch(Exception e){
            System.err.println(e.toString());
        }
        
        // No incluir espacio en el nickname
        user_id.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.contains(" ")) {
                user_id.setText(oldValue);
            }
        });
        
        // Error cuando el nickname no ha sido registrado
        user_id.setOnKeyTyped(event -> userNoReg());
        
        // Error cuando el nickname esté vacio
        user_id.setOnKeyTyped(event -> userVacio());
        
        // Error cuando el nickname cambie de textField
        user_id.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                userVacio();
            }
        });
        
        // Permitir unicamente numeros y letras en la contraseña
        pswrd_id.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9]*")) {
                pswrd_id.setText(oldValue);
            }
        });
        
        // Error password cambie de textField
        pswrd_id.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                passwordVacio();
            }
        });
    }
    // Metodo cuando usuario mete un nick no registrado
    private void userNoReg() {
        boolean nickVal = nav.exitsNickName(user_id.getText());

        if (!nickVal) {
            userErrorImg.setVisible(true);
            userError.setStyle("-fx-text-fill: #fc0000;");
            userError.setText("No existe usuario con ese nombre");
        } else {
            userErrorImg.setVisible(false);
            userError.setStyle("-fx-text-fill: #FFFFFF;");
            userError.setText("Inicia con tus credenciales");
        }
    }

    // Metodo cuando usuario no rellena nickname (vacio)
    private void userVacio() {
        String nickTxt = user_id.getText();
        boolean nickVal = !nickTxt.isEmpty();

        if (!nickVal) {
            userErrorImg.setVisible(true);
            userError.setStyle("-fx-text-fill: #fc0000;");
            userError.setText("Campo usuario vacío");
        } else {
            userErrorImg.setVisible(false);
            userError.setStyle("-fx-text-fill: #FFFFFF;");
            userError.setText("Inicia con tus credenciales");
        }
    }

    // Metodo cuando el usuario no rellena la contraseña (vacio)
    private void passwordVacio() {
        String passwordTxt = pswrd_id.getText();
        boolean pwdVal = !passwordTxt.isEmpty();

        if (!pwdVal) {
            pswrdErrorImg.setVisible(true);
            pswrdError.setStyle("-fx-text-fill: #fc0000;");
            pswrdError.setText("Campo contraseña vacío");
        } else {
            pswrdErrorImg.setVisible(false);
            pswrdError.setStyle("-fx-text-fill: #ffffff;");
            pswrdError.setText("Inicia con tus credenciales");
        }
    }
    
    //RESETEAR LA VISTA ENTERA
    private void reiniciar(){
        user_id.setText("");
        pswrd_id.setText("");
        userErrorImg.setVisible(false);
        userError.setText("Inicia con tus credenciales");
        userError.setStyle("-fx-text-fill: #ffffff;");
    }
    
    // Metodo para dejar todos los datos en blanco de nuevo
    private void blanc() {
        user_id.setText("");
        pswrd_id.setText("");
        pswrdErrorImg.setVisible(false);
        pswrdError.setText("Inicia con tus credenciales");
        pswrdError.setStyle("-fx-text-fill: #ffffff;");
        //registriarse.setStyle("-fx-text-fill: #ffffff");
    }
    
    public void setDst(String s) {
        dest = s;
    }
    
    public void setFocus() {
        user_id.requestFocus();
    }

    @FXML
    private void log_in(ActionEvent event) {
        if(!user_id.getText().isBlank() && !pswrd_id.getText().isBlank()){ 
            if(nav.exitsNickName(user_id.getText())){
                User usuario = nav.authenticate(user_id.getText(), pswrd_id.getText());
                if(usuario != null){ 
                    reiniciar();
                    String nickname = usuario.getNickName();
                    //JavaFXMLApplication.setRoot("Menu");
                    //Menu controller = (Menu) ProyAplication.getController("Menu");
                    //controller.cambiarUser(finanza);
                }else{
                    pswrdErrorImg.setVisible(true);
                    pswrdError.setStyle("-fx-text-fill: #fc0000;");
                    pswrdError.setText("Contraseña incorrecta para el usuario");
                    pswrd_id.requestFocus();
                }
            }else{
                userErrorImg.setVisible(true);
                userError.setStyle("-fx-text-fill: #fc0000;");
                userError.setText("No existe usuario con ese nombre");
                user_id.requestFocus();
            }
                
        }else{
            userErrorImg.setVisible(true);
            userError.setStyle("-fx-text-fill: #fc0000;");
            if(user_id.getText().isEmpty()){userError.setText("Campo usuario vacío");}
            else{pswrdError.setText("Campo contraseña vacío");}
            if(user_id.getText().isBlank()){ user_id.requestFocus();}
            else{pswrd_id.requestFocus();}
        }
    }

    @FXML
    private void register(ActionEvent event) {
        blanc();
        RegistroController controller = (RegistroController) JavaFXMLApplication.getController("Registro");
        controller.setDst("IniciarSesion");
        JavaFXMLApplication.setRoot("Registro");
        controller.setFocus();
    }
}    
