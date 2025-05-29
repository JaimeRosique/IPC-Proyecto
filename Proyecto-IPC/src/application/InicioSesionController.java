/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package application;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
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
    private PasswordField pswrdField;
    @FXML
    private TextField pswrdTextField;
    @FXML
    private ImageView userErrorImg;
    @FXML
    private ImageView pswrdErrorImg;
    @FXML
    private Hyperlink hyperlink;
    @FXML
    private VBox rootPane;
    
    @FXML
    private void cambiarTema() {
        Scene scene = rootPane.getScene();

        if (scene != null) {
            ThemeManager.toggleTheme(scene);

            // ⚠️ Forzar la re-asignación del root
            Parent currentRoot = scene.getRoot();
            scene.setRoot(new Group());  // cambiar temporalmente
            scene.setRoot(currentRoot);  // volver al root real

            currentRoot.applyCss();
            currentRoot.layout();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{
            nav = Navigation.getInstance();
        }catch(Exception e){
            System.err.println(e.toString());
        }
        
        // Espera a que el nodo esté en escena para obtener el Stage
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Image icono = new Image(getClass().getResourceAsStream("/Proyecto-IPC/src/resources/compas"));
                stage.getIcons().add(icono);
            }
        });
        
        Platform.runLater(() -> {
            Scene scene = rootPane.getScene();
            if (scene != null) {
                if (!rootPane.getStyleClass().contains("root")) {
                rootPane.getStyleClass().add("root");
            }

                // Volver a aplicar el estilo actual
                scene.getStylesheets().clear();
                scene.getStylesheets().add(ThemeManager.getEstiloActual());
            } 
        });
        
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
        
        /*
        // Permitir unicamente numeros y letras en la contraseña
        pswrd_id.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9]*")) {
                pswrd_id.setText(oldValue);
            }
        });
        */
        // Error password cambie de textField
        pswrdField.focusedProperty().addListener((observable, oldValue, newValue) -> {
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
            userError.setStyle("-fx-text-fill: #cc3333;");
            userError.setText("No existe usuario con ese nombre");
        } else {
            userErrorImg.setVisible(false);
            userError.setStyle("-fx-text-fill: #4d88b3;");
            userError.setText("Inicia con tus credenciales");
        }
    }

    // Metodo cuando usuario no rellena nickname (vacio)
    private void userVacio() {
        String nickTxt = user_id.getText();
        boolean nickVal = !nickTxt.isEmpty();

        if (!nickVal) {
            userErrorImg.setVisible(true);
            userError.setStyle("-fx-text-fill: #cc3333;");
            userError.setText("Campo usuario vacío");
        } else {
            userErrorImg.setVisible(false);
            userError.setStyle("-fx-text-fill: #4d88b3;");
            userError.setText("Inicia con tus credenciales");
        }
    }

    // Metodo cuando el usuario no rellena la contraseña (vacio)
    private void passwordVacio() {
        String passwordTxt = pswrdField.getText();
        boolean pwdVal = !passwordTxt.isEmpty();

        if (!pwdVal) {
            pswrdErrorImg.setVisible(true);
            pswrdError.setStyle("-fx-text-fill: #cc3333;");
            pswrdError.setText("Campo contraseña vacío");
        } else {
            pswrdErrorImg.setVisible(false);
            pswrdError.setStyle("-fx-text-fill: #4d88b3;");
            pswrdError.setText("Inicia con tus credenciales");
        }
    }
    
    //RESETEAR LA VISTA ENTERA
    private void reiniciar(){
        user_id.setText("");
        pswrdField.setText("");
        userErrorImg.setVisible(false);
        userError.setText("Inicia con tus credenciales");
        userError.setStyle("-fx-text-fill: #4d88b3;");
    }
    
    // Metodo para dejar todos los datos en blanco de nuevo
    private void blanc() {
        user_id.setText("");
        pswrdField.setText("");
        pswrdErrorImg.setVisible(false);
        pswrdError.setText("Inicia con tus credenciales");
        pswrdError.setStyle("-fx-text-fill: #4d88b3;");
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
        if(!user_id.getText().isBlank() && !pswrdField.getText().isBlank()){ 
            if(nav.exitsNickName(user_id.getText())){
                User usuario = nav.authenticate(user_id.getText(), pswrdField.getText());
                if(usuario != null){ 
                    reiniciar();
                    String nickname = usuario.getNickName();
                    ProblemaController controller = (ProblemaController) JavaFXMLApplication.getController("Problema");
                    controller.setDatos(nav, usuario);
                    JavaFXMLApplication.setRoot("Problema");
                }else{
                    pswrdErrorImg.setVisible(true);
                    pswrdError.setStyle("-fx-text-fill: #cc3333;");
                    pswrdError.setText("Contraseña incorrecta para el usuario");
                    pswrdField.requestFocus();
                }
            }else{
                userErrorImg.setVisible(true);
                userError.setStyle("-fx-text-fill: #cc3333;");
                userError.setText("No existe usuario con ese nombre");
                user_id.requestFocus();
            }
                
        }else{
            userErrorImg.setVisible(true);
            userError.setStyle("-fx-text-fill: #cc3333;");
            if(user_id.getText().isEmpty()){userError.setText("Campo usuario vacío");}
            else{pswrdError.setText("Campo contraseña vacío");}
            if(user_id.getText().isBlank()){ user_id.requestFocus();}
            else{pswrdField.requestFocus();}
        }
    }

    @FXML
    private void register(ActionEvent event) {
        blanc();
        RegistroController controller = (RegistroController) JavaFXMLApplication.getController("Registro");
        controller.setDst("IniciarSesion");
        JavaFXMLApplication.setRoot("Registro");
        controller.setFocus();
        hyperlink.setVisited(false); // mantiene estilo intacto
    }
}    
