/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.YEARS;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.NavDAOException;
import model.Navigation;
import model.User;

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
    @FXML private TextField tfMail;
    @FXML private PasswordField tfPassword;
    @FXML private DatePicker datepicker;
    @FXML private ImageView avatar;
    @FXML private Label lbPasswordError;
    @FXML private Label lbMailError;
    @FXML private Label lbDateError;
    @FXML private User user;  // O el tipo real correcto que tengas para el usuario

    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            nav = Navigation.getInstance();
        }catch(Exception e) {
            System.err.println(e.toString());
        }
    }
private void cambiarFoto(MouseEvent event) throws FileNotFoundException {
        FileChooser chooser = new FileChooser();
    chooser.setTitle("Open File");
    chooser.setInitialDirectory(new File(System.getProperty("user.home")));
    chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files",".bmp", ".png", ".jpg", ".gif")); 
    File file = chooser.showOpenDialog(new Stage());
    

    if(file != null) {
            //String imagepath = file.toURI().toURL().toString();
            InputStream isImage = (InputStream) new FileInputStream(file);
            avatar.setImage(new Image(isImage));
    }
    else
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText("Please Select a File");
        alert.setContentText("You didn't select a file!");
        alert.showAndWait();
    }
    }
    
    private boolean checkMail(String mail){
        String email = mail;
        boolean isValid = email.matches("^[\\w!#$%&'*+/=?{|}~^-]+(?:\\.[\\w!#$%&'*+/=?{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        return isValid;
    }
    
    
    
    private boolean checkPassword(String pssw) {
        String password = pssw;
        boolean isValid = password.matches("^(?=.[0-9])(?=.[a-zA-Z]).{8,15}$");
        return isValid;
    }
    
    private boolean checkDate(LocalDate date){
        LocalDate value = date;
        boolean isValid = value.isBefore(LocalDate.now().minus(18, YEARS));
        return isValid;
    }

    @FXML
    private void registerAction(ActionEvent event) throws NavDAOException {
        
        boolean ok = true;
        String nick = user.getNickName();
        String mail = tfMail.getText();
        String pssw = tfPassword.getText();
        LocalDate date = datepicker.getValue();
        Image img = avatar.getImage();
        
        lbPasswordError.setVisible(!checkPassword(pssw));
        lbMailError.setVisible(!checkMail(mail));
        lbDateError.setVisible(!checkDate(date));
        
        ok=ok&&checkPassword(pssw)&&checkMail(mail)&&checkDate(date);
        if(ok==false){
            System.out.println("No registrado");
            return;
        }
        
        user.setPassword(pssw);
        user.setEmail(mail);
        user.setAvatar(img);
        user.setBirthdate(date);
        System.out.println("Si registrado");
        Stage stageActual = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stageActual.close();
    }    
    
}
