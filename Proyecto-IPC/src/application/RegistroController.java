/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package application;

import java.io.File;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import model.*;
import java.time.LocalDate;
import java.time.Period;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author jrosi
 */
public class RegistroController implements Initializable {

    private String dest;
    private Image[] imgArray = new Image[10];
    private Navigation nav;
    private int i;
    private boolean modoModificar = false;
    @FXML
    private VBox rootPane;
    @FXML
    private PasswordField pswrdField;
    @FXML
    private TextField pswrdTextField;
    @FXML
    private ImageView eyeIcon2;
    @FXML
    private PasswordField pswrdCheckField;
    @FXML
    private TextField pswrdCheckTextField;
    @FXML
    private ImageView eyeIcon3;

        public void setModoModificar(boolean modificar) {
        this.modoModificar = modificar;
        if (modificar) {
        nickname.setDisable(true);  // bloquea campo usuario
    }
        }
        private boolean showingPasswordMain = false;
        private boolean showingPasswordConfirm = false;
        private final String ojocerrado = "/resources/abrirojo.png";
        private final String ojoabierto = "/resources/cerrarojo.png";
    @FXML
    private TextField nickname;
    @FXML
    private DatePicker bdate;
    @FXML
    private TextField email;
    @FXML
    private Image lF;
    @FXML
    private Image dF;
    @FXML
    private ImageView avatar_izq;
    @FXML
    private ImageView avatar_img;
    @FXML
    private ImageView avatar_der;
    @FXML
    private Label user_error;
    @FXML
    private Label pswrd_error;
    @FXML
    private Label pswrd_check_error;
    @FXML
    private Label email_error;
    @FXML
    private Label edad_error;
    private User user;
    
    @FXML
    private void cambiarTema() {
        Scene scene = rootPane.getScene();

    if (scene != null) {
        // Cambiar el tema
        ThemeManager.toggleTheme(scene);

        // Forzar recarga visual total
        Parent originalRoot = scene.getRoot();

        // 1. Desvincular temporalmente el root
        scene.setRoot(new Group());

            // 2. Volver a asignar el root original
            scene.setRoot(originalRoot);

            // 3. Reaplicar estilos y layout
            originalRoot.applyCss();
            originalRoot.layout();
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
         // INICIAL contraseña principal
    pswrdTextField.setVisible(false);
    pswrdField.setVisible(true);
    eyeIcon2.setImage(new Image(getClass().getResourceAsStream(ojocerrado)));

    pswrdTextField.managedProperty().bind(pswrdTextField.visibleProperty());
    pswrdField.managedProperty().bind(pswrdField.visibleProperty());

    pswrdTextField.textProperty().bindBidirectional(pswrdField.textProperty());

    eyeIcon2.setOnMouseClicked(event -> togglePasswordVisibilityMain());

    // INICIAL confirmar contraseña
    pswrdCheckTextField.setVisible(false);
    pswrdCheckField.setVisible(true);
    eyeIcon3.setImage(new Image(getClass().getResourceAsStream(ojocerrado)));

    pswrdCheckTextField.managedProperty().bind(pswrdCheckTextField.visibleProperty());
    pswrdCheckField.managedProperty().bind(pswrdCheckField.visibleProperty());

    pswrdCheckTextField.textProperty().bindBidirectional(pswrdCheckField.textProperty());

    eyeIcon3.setOnMouseClicked(event -> togglePasswordVisibilityConfirm());

        
        
        
        
        
        
        
        
        
        // Espera a que el nodo esté en escena para obtener el Stage
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Image icono = new Image(getClass().getResourceAsStream("compas"));
                stage.getIcons().add(icono);
            }
        });
        
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
        if (newScene != null) {
            // Asegurar clase root
            if (!rootPane.getStyleClass().contains("root")) {
                rootPane.getStyleClass().add("root");
            }

            // Aplicar el CSS activo
            newScene.getStylesheets().clear();
            newScene.getStylesheets().add(ThemeManager.getEstiloActual());
        }
    });
        
        if (modoModificar) {
        nickname.setDisable(true);  // bloquea el campo para no poder cambiarlo
        }
        try {
            nav = Navigation.getInstance();
        }catch(Exception e) {
            System.err.println(e.toString());
        }
        imgSetUp();
        
        // Cambia el tamaño de las flechas cuando pones el cursor encima
        avatar_der.setOnMouseEntered(event -> {
            avatar_der.setFitWidth(40); avatar_der.setFitHeight(40);
        });
        
        // Cambia el tamaño de las flechas cuando pones el cursor encima
        avatar_izq.setOnMouseEntered(event -> {
            avatar_izq.setFitWidth(40); avatar_izq.setFitHeight(40);
        });
        
        // Cambia el tamaño de las flechas cuando pones el cursor encima
        avatar_der.setOnMouseExited(event -> {
            avatar_der.setFitWidth(35); avatar_der.setFitHeight(35);
        });
        
        // Cambia el tamaño de las flechas cuando pones el cursor encima
        avatar_izq.setOnMouseExited(event -> {
            avatar_izq.setFitWidth(35); avatar_izq.setFitHeight(35);
        });
        
        // Permitir solo letras y espaciado en el nickname
        nickname.textProperty().addListener((observable, oldValue, newValue) -> {
            if ( newValue.length() > 15) {
            nickname.setText(oldValue);
            }
        });
        
        
        // Errores en nickname usado
        //nickname.setOnKeyTyped(event -> validarNickname());
        
        // Errores en nickname usado
        /*nickname.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                validarNickname();
            }
        });
        */
        // Permitir solo letras y numeros  sin espaciado en el correo
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9\\s'\\-áéíúóàèìòùÁÉÍÓÚÀÈÌÒÙäëïöüÄËÏÖÜñÑ@.]*")) {
                email.setText(oldValue);
            }
        });    
        
        // Errores en nombre
        email.setOnKeyTyped(event -> errEmail());
        
        // Errores en nombre cuando cambias de campo y está vacío
        email.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                errEmail();
            }
        });
        
        // Permitir todo tipo de letras numeros o simbolos en el campo de contraseña
        pswrdField.textProperty().addListener((observable, oldValue, newValue) -> {
            
            checkPassWrd();
        });
        
        // Errores en pwd cuando se cambie de textField
        pswrdField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && checkPassWrd()) {
                errPasswrd1();
            }
        });
        
        // Errores en pwd
        pswrdField.setOnKeyTyped(event -> errPasswrd1());
        
        // Permitir todo tipo de simbolos en el campo de confirmar contraseña
        pswrdCheckField.textProperty().addListener((observable, oldValue, newValue) -> {
            
            eqPassWrd();
        });
        
        // Errores en pwd1 cuando se cambie de textField
        pswrdCheckField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && checkPassWrd()) {
                errPasswrd1();
            }
        });
        
        // Errores en pwd1
        pswrdCheckField.setOnKeyTyped(event -> errPasswrd1());
    }
        
    
    
    private void togglePasswordVisibilityMain() {
        new Image(getClass().getResourceAsStream("/resources/cerrarojo.png"));
    new Image(getClass().getResourceAsStream("/resources/abrirojo.png"));
    showingPasswordMain = !showingPasswordMain;
    if (showingPasswordMain) {
        
        pswrdTextField.setVisible(true);
        pswrdField.setVisible(false);
        eyeIcon2.setImage(new Image(getClass().getResourceAsStream(ojoabierto)));
    } else {
        pswrdTextField.setVisible(false);
        pswrdField.setVisible(true);
        eyeIcon2.setImage(new Image(getClass().getResourceAsStream(ojocerrado)));
    }
}

private void togglePasswordVisibilityConfirm() {
    new Image(getClass().getResourceAsStream("/resources/cerrarojo.png"));
    new Image(getClass().getResourceAsStream("/resources/abrirojo.png"));
    
    
    showingPasswordConfirm = !showingPasswordConfirm;
    if (showingPasswordConfirm) {
        pswrdCheckTextField.setVisible(true);
        pswrdCheckField.setVisible(false);
        eyeIcon3.setImage(new Image(getClass().getResourceAsStream(ojoabierto)));
    } else {
        pswrdCheckTextField.setVisible(false);
        pswrdCheckField.setVisible(true);
        eyeIcon3.setImage(new Image(getClass().getResourceAsStream(ojocerrado)));
    }
}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public void cargarDatosUsuario(User user) {
        this.user = user;
    nickname.setText(user.getNickName());
    email.setText(user.getEmail());
    pswrdField.setText(user.getPassword());
    bdate.setValue(user.getBirthdate());
    avatar_img.setImage(user.getAvatar());
}
    
    private boolean validarNickname() {
        String nick = nickname.getText();
        boolean nickValido = !nav.exitsNickName(nick); 
        boolean nickNoVacio = !nick.isEmpty();
        boolean formatoValido = nick.matches("[a-zA-Z0-9_-]{6,15}");
        
        if (!nickNoVacio) {
        user_error.setVisible(true);
        user_error.setText("No debería estar vacío");
        user_error.setStyle("-fx-text-fill: #cc3333;");
        return false;

    } else if (nick.contains(" ")) {
        user_error.setVisible(true);
        user_error.setText("El nombre de usuario no puede contener espacios.");
        user_error.setStyle("-fx-text-fill: #cc3333;");
        return false;

    } else if (!nickValido) {
        user_error.setVisible(true);
        user_error.setText("Usuario repetido");
        user_error.setStyle("-fx-text-fill: #cc3333;");
        return false;

    } else if (!formatoValido) {
        user_error.setVisible(true);
        user_error.setText("Debe tener entre 6 y 15 caracteres.");
        user_error.setStyle("-fx-text-fill: #cc3333;");
        return false;

    } else {
        user_error.setVisible(false);
        user_error.setText("");
        return true;
    }
    }
    

     private boolean validarEmail() {
    String emailInput = email.getText().trim();

    if (emailInput.toLowerCase().endsWith("@gmail.com")) {
        email_error.setVisible(false);
        email_error.setText("");  // sin mensaje si es válido
        return true;
    } else {
        email_error.setVisible(true);
        email_error.setText("Email no  valido");
        email_error.setStyle("-fx-text-fill: #cc3333;");  // rojo
        return false;
    }

        
    }
     private boolean validarEdad() {
    LocalDate fechaNacimiento = bdate.getValue();
    LocalDate hoy = LocalDate.now();

    if (fechaNacimiento == null) {
        edad_error.setText("Debes seleccionar tu fecha de nacimiento.");
        edad_error.setStyle("-fx-text-fill: #cc3333;");
        return false;
    }

    int edad = Period.between(fechaNacimiento, hoy).getYears();

    if (edad > 16) {
        edad_error.setText("");  // sin error si pasa
        return true;
    } else {
        edad_error.setVisible(true);
        edad_error.setText("Debes tener más de 16 años para registrarte.");
        edad_error.setStyle("-fx-text-fill: #cc3333;");
        return false;
    }
}

    
    
    // Mostrar errores en el correo 
    private void errEmail() { 
        String emailText = email.getText();
        boolean emailValido = !emailText.isEmpty() && emailText.contains(" ");

        if (!emailValido) {
            //emailErrImg.setVisible(true);
            email_error.setStyle("-fx-text-fill: #cc3333;");
        } else {
            //emailErrImg.setVisible(false);
            email_error.setStyle("-fx-text-fill: #FFFFFF;");
        }
    }
    
    // Mostrar errores en la contraseña
    private void errPasswrd1() {
        String pwdText = pswrdField.getText();
        boolean pwdValida = pwdText.length() >= 8 && pwdText.length() <= 20;

        if (!pwdValida) {
            //passwrdErrImg1.setVisible(true);
            pswrd_error.setStyle("-fx-text-fill: #cc3333;");
        } else {
            //passwrdErrImg1.setVisible(false);
            pswrd_error.setStyle("-fx-text-fill: #FFFFFF;");
        }
    }
    
    
    
    // Método para verificar las condiciones de la contraseña
    private boolean checkPassWrd() {
        
    boolean hasLowercase = false;
    boolean hasUppercase = false;
    boolean hasNumber = false;
    boolean hasSymbol = false;

    String password = pswrdField.getText();

    if (password.length() < 8 || password.length() > 20) {
        pswrd_error.setVisible(true);
        pswrd_error.setText("La contraseña debe tener entre 8 y 20 caracteres.");
        pswrd_error.setStyle("-fx-text-fill: #fc0000;");
        return false;
    }

    for (char c : password.toCharArray()) {
        if (Character.isLowerCase(c)) {
            hasLowercase = true;
        } else if (Character.isUpperCase(c)) {
            hasUppercase = true;
        } else if (Character.isDigit(c)) {
            hasNumber = true;
        } else {
            hasSymbol = true;
        }
    }

    if (hasLowercase && hasUppercase && hasNumber && hasSymbol) {
        pswrd_error.setVisible(false);
        pswrd_error.setText("");
        return true;
    } else {
        pswrd_error.setVisible(true);
        pswrd_error.setText("Debe incluir  alguna minúscula, mayúscula, número y símbolo.");
        pswrd_error.setStyle("-fx-text-fill: #fc0000;");
        return false;
    }
}

    
    
    // Método para comprobar que tanto la primera como la segunda contraseñas son iguales
    private boolean eqPassWrd() {
        if (pswrdField.getText().equals(pswrdCheckField.getText())) {
            pswrd_check_error.setVisible(false);
            pswrd_check_error.setText("");
            return true;
        }
        else {
            pswrd_check_error.setVisible(true);
            pswrd_check_error.setText("Las contraseñas no coinciden.");
            pswrd_check_error.setStyle("-fx-text-fill: #7c7c7c;");
            return false;
        }
    }
    
    // Metodo que inicializa las 12 imagenes de imgArray 
    private void imgSetUp() {
        
        lF = new Image("resources/flecha_izq.png", 140, 150, false, true);
        dF = new Image("resources/flecha_der.png", 140, 150, false, true);          
        imgArray[0] = new Image("resources/men.png", 140, 150, false, true);
        imgArray[1] = new Image("resources/men2.png", 140, 150, false, true);
        imgArray[2] = new Image("resources/men3.png", 140, 150, false, true);
        imgArray[3] = new Image("resources/men4.png", 140, 150, false, true);
        imgArray[4] = new Image("resources/men5.png", 140, 150, false, true);
        imgArray[5] = new Image("resources/woman.png", 140, 150, false, true);
        imgArray[6] = new Image("resources/woman2.png", 140, 150, false, true);
        imgArray[7] = new Image("resources/woman3.png", 140, 150, false, true);
        imgArray[8] = new Image("resources/woman4.png", 140, 150, false, true);
        imgArray[9] = new Image("resources/woman5.png", 140, 150, false, true);
        
        avatar_izq.setImage(lF);
        avatar_der.setImage(dF);
        i = 0;
        avatar_img.setImage(imgArray[i]);
        
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
        boolean pwd1Valida = pswrdField.getText().length() >= 6;
        boolean pwd2Valida = pswrdCheckField.getText().equals(pswrdField.getText());
        boolean edadValida = validarEdad();
            if (!mandado && !edadValida) {
            bdate.requestFocus();
            mandado = true;
            }
        
        //user_errorImg.setVisible(!validUser);
        boolean nickOk = validarNickname();
if (!mandado && !nickOk) { nickname.requestFocus(); mandado = true; }
        
        //pswrdErrorImg.setVisible(pswrd.getText().isEmpty());
        pswrd_error.setVisible(true);
        pswrd_error.setStyle(pswrdField.getText().isEmpty() ?  "-fx-text-fill: #fc0000;" : "-fx-text-fill: #7c7c7c;");
        if(!mandado && !pwd1Valida){pswrdField.requestFocus(); mandado = true;}
        
        //pswrd_check_ErrorImg.setVisible(!pswrd_check.getText().equals(pswrd.getText()));
        pswrd_check_error.setVisible(true);
        pswrd_check_error.setStyle(pswrdCheckField.getText().equals(pswrdField.getText()) ?  "-fx-text-fill: #fc0000;" : "-fx-text-fill: #7c7c7c;");
        if(!mandado && !pwd2Valida){pswrdCheckField.requestFocus(); mandado = true;}
        
        //email_errorImg.setVisible(email.getText().isEmpty());
        email_error.setVisible(true);
        email_error.setStyle(email.getText().isEmpty() ?  "-fx-text-fill: #fc0000;" : "-fx-text-fill: #7c7c7c;");
        if(!mandado && !validEmail){email.requestFocus(); mandado = true;}
        
        return nickOk && validEmail  && pwd1Valida && pwd2Valida;
    }
    
    // Metodo que limpia todos los datos 
    private void limpiarCampos() {
        nickname.clear();
        bdate.setValue(null);
        email.clear();
        pswrdField.clear();
        pswrdCheckField.clear();

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
        boolean camposValidos = checkCampos();

    if (camposValidos) {
        if (modoModificar) {
            // Estamos modificando un usuario ya existente
            user.setEmail(email.getText());
            user.setPassword(pswrdField.getText());
            user.setAvatar(avatar_img.getImage());
            user.setBirthdate(bdate.getValue());

            System.out.println("✅ Usuario modificado correctamente");
        } else {
            // Registro de un nuevo usuario
            try {
                nav.registerUser(
                    nickname.getText(),
                    email.getText(),
                    pswrdField.getText(),
                    avatar_img.getImage(),
                    bdate.getValue()
                );
                System.out.println("✅ Usuario registrado correctamente");
            } catch (Exception e) {
                System.err.println("❌ Error al registrar usuario: " + e.getMessage());
            }
        }

        limpiarCampos();
        JavaFXMLApplication.setRoot(dest);  // volver a la pantalla destino
    }
    }

    @FXML
    private void img_izq(MouseEvent event) {
        i = (i - 1 + imgArray.length) % imgArray.length;
        System.out.println("Izquierda, i = " + i);
        avatar_img.setImage(imgArray[i]);
    }

    @FXML
    private void img_der(MouseEvent event) {
        i = (i + 1) % imgArray.length;
        System.out.println("Derecha, i = " + i);
        avatar_img.setImage(imgArray[i]);
    }

    @FXML
    private void getImg(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Buscar Imagen");
        
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Images", "*.*"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
            new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if(selectedFile != null){
            Image image = null;
            try{
                image = new Image("file:" + selectedFile.getAbsolutePath(), 140, 150, false, true);
            }catch(Exception e){
                System.err.println(e.toString());
            }
            
            avatar_img.setImage(image);
        }
    }
}


