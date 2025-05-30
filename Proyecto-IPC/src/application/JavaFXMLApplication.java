package application;

import java.io.IOException;
import java.util.HashMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class JavaFXMLApplication extends Application {

    private static Scene scene;
    private static HashMap<String, Parent> roots = new HashMap<>();
    private static HashMap<String, Object> controllers = new HashMap<>();

    @Override
    public void start(Stage stage) throws Exception {
        // Cargar todas las escenas
        cargarEscena("InicioSesion.fxml", "IniciarSesion");
        cargarEscena("Registro.fxml", "Registro");
        cargarEscena("Problema.fxml", "Problema");
        // Crear la escena inicial
        scene = new Scene(roots.get("IniciarSesion"));

        // Configuración de la ventana principal
        stage.setScene(scene);
        stage.getIcons().add(new Image(InicioSesionController.class.getResourceAsStream("/resources/compas.png")));
        stage.centerOnScreen();
        stage.setMaxWidth(600);
        stage.setMaxHeight(500);
        stage.setMinHeight(500);
        stage.setMinWidth(400);
        stage.show();
    }

    private void cargarEscena(String nombreFXML, String clave) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(nombreFXML));
        Parent root = loader.load();
        root.getStylesheets().add(ThemeManager.getEstiloActual());
        roots.put(clave, root);
        controllers.put(clave, loader.getController());
    }

    public static void setRoot(Parent root) {
        scene.setRoot(root);        
    }

    public static void setRoot(String clave) {
        Parent root = roots.get(clave);
        if (root != null) {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(ThemeManager.getEstiloActual());
            
            setRoot(root);

      
            Stage stage = (Stage) scene.getWindow();
            switch (clave) {
                case "IniciarSesion":
                    stage.getIcons().add(new Image(InicioSesionController.class.getResourceAsStream("/resources/compas.png")));
                    stage.setTitle("Inicio Sesion");
                    stage.centerOnScreen();
                    stage.setMaxWidth(600);
                    stage.setMaxHeight(500);
                    stage.setMinHeight(500);
                    stage.setMinWidth(400);
                    break;
                case "Registro":
                    stage.getIcons().add(new Image(RegistroController.class.getResourceAsStream("/resources/compas.png")));
                    stage.setTitle("Registro");
                    stage.centerOnScreen();
                    stage.setMaxWidth(900);
                    stage.setMaxHeight(700);
                    stage.setMinHeight(550);
                    stage.setMinWidth(400);
                    break;
                case "Problema":
                    stage.setMaximized(true);
                    stage.getIcons().add(new Image(ProblemaController.class.getResourceAsStream("/resources/compas.png")));
                    stage.setMinHeight(900);
                    stage.setMinWidth(700);
                    stage.setMaxHeight(8000);
                    stage.setMaxWidth(8000);
                    break;
            }
        } else {
            System.err.printf("No se encuentra la escena: %s%n", clave);
        }
    }

    public static Object getController(String clave) {
        if (controllers.containsKey(clave)) {
            return controllers.get(clave);
        } else {
            System.err.printf("No se encuentra el controller: %s%n", clave);
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}