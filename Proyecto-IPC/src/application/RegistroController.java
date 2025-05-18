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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.*;

/**
 * FXML Controller class
 *
 * @author jrosi
 */
public class RegistroController implements Initializable {

    private String dest;
    private Image[] imgArray = new Image[12];
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
    //TODO-- CAMBIAR CAMPOS PARA QUE SEAN VALIDOS (CONTRASEÑA USER)
    private boolean checkCampos() {
        boolean mandado = false;
        
        String user_name = nickname.getText().trim();
        String emailText = email.getText();
        
        boolean validUser = !user_name.isEmpty();
        boolean validEmail = !emailText.isEmpty() && !emailText.contains(" ");
        boolean nickNoUsado = !nav.exitsNickName(nickname.getText());
        boolean pwd1Valida = pswrd.getText().length() >= 6;
        boolean pwd2Valida = pswrd_check.getText().equals(pswrd.getText());
        
        //user_errorImg.setVisible(!validUser);
        user_error.setVisible(true);
        user_error.setStyle(validUser ? "-fx-text-fill: #7c7c7c;" : "-fx-text-fill: #fc0000;");
        if(!mandado && !validUser){nickname.requestFocus(); mandado = true;}
        
        //pswrdErrorImg.setVisible(pswrd.getText().isEmpty());
        pswrd_error.setVisible(true);
        pswrd_error.setStyle(pswrd.getText().isEmpty() ?  "-fx-text-fill: #fc0000;" : "-fx-text-fill: #7c7c7c;");
        if(!mandado && !pwd1Valida){pswrd.requestFocus(); mandado = true;}
        
        //pswrd_check_ErrorImg.setVisible(!pswrd_check.getText().equals(pswrd.getText()));
        pswrd_check_error.setVisible(true);
        pswrd_check_error.setStyle(pswrd_check.getText().equals(pswrd.getText()) ?  "-fx-text-fill: #fc0000;" : "-fx-text-fill: #7c7c7c;");
        if(!mandado && !pwd2Valida){pswrd_check.requestFocus(); mandado = true;}
        
        //email_errorImg.setVisible(email.getText().isEmpty());
        email_error.setVisible(true);
        email_error.setStyle(email.getText().isEmpty() ?  "-fx-text-fill: #fc0000;" : "-fx-text-fill: #7c7c7c;");
        if(!mandado && !validEmail){email.requestFocus(); mandado = true;}
        
        return validUser && validEmail && nickNoUsado && pwd1Valida && pwd2Valida;
    }
    
    // Metodo que limpia todos los datos 
    private void limpiarCampos() {
        nickname.clear();
        bdate.setValue(null);
        email.clear();
        pswrd.clear();
        pswrd_check.clear();

        avatar_img.setImage(imgArray[0]);

        //user_errorImg.setVisible(false);
        user_error.setVisible(false);
        user_error.setStyle("-fx-text-fill: #7c7c7c;");
        //emailErrImg.setVisible(false);
        email_error.setVisible(false);
        email_error.setStyle("-fx-text-fill: #7c7c7c;");
        //passwrdErrImg.setVisible(false);
        pswrd_error.setVisible(false);
        pswrd_error.setStyle("-fx-text-fill: #7c7c7c;");
        pswrd_check_error.setVisible(false);
        pswrd_check_error.setStyle("-fx-text-fill: #7c7c7c;");
    }
    
    //Método para crear una alerta
    private void mostrarAlert(){ //REVISAR
        //Alert alert = new Alert(Alert.AlertType.INFORMATION);
        //alert.setTitle("Registro completado");
        //alert.setHeaderText(null);
        //alert.setContentText("Inicia sesión para usar la aplicación");
        //alert.getDialogPane().setStyle("-fx-background-color: #a4dc8c");
        //alert.getDialogPane().getStylesheets().add("styles/EstilosFondo.css");
        //alert.showAndWait();
    }
    
    public void setDst(String s) {
        dest = s;
    }
    
    public void setFocus() {
        nickname.requestFocus();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        //limpiarCampos();
        InicioSesionController controller = (InicioSesionController) JavaFXMLApplication.getController("IniciarSesion");
        JavaFXMLApplication.setRoot("IniciarSesion");
        controller.setFocus();
    }

    @FXML
    private void aceptar(ActionEvent event) {
        //TODO -- Verificar todos los campos + ampliaciones necesarias
        boolean camposValidos = checkCampos();
        //boolean contrValida = checkPassWrd() && eqPassWrd();
        
        if (camposValidos /*&& contrValida*/) {
            try{
                nav.registerUser(nickname.getText(), email.getText(), pswrd.getText(), avatar_img.getImage(), bdate.getValue());
                mostrarAlert();
                limpiarCampos();
                JavaFXMLApplication.setRoot(dest);
            }catch(Exception e){
                System.err.println(e.toString());
            }
        }
    }
}
