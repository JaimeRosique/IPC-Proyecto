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
        /*
        cargarEscena("Menu.fxml", "Menu");
        cargarEscena("ActualizarDatos.fxml", "ActualizarDatos");
        cargarEscena("AddGastos.fxml", "AddGastos");
        cargarEscena("AddCat.fxml", "AddCat");
        cargarEscena("VerCuenta.fxml", "VerCuenta");
        cargarEscena("VerCuentaAño.fxml", "VerCuentaAño");
        cargarEscena("VerCuentaCat.fxml", "VerCuentaCat");
        cargarEscena("ListaCargos.fxml", "ListaCargos");
        */

        // Crear la escena inicial
        scene = new Scene(roots.get("IniciarSesion"));

        // Cargar CSS si lo tienes
        //String css = this.getClass().getResource("/styles/EstilosLayout.css").toExternalForm();
        //scene.getStylesheets().add(css);

        // Configuración de la ventana principal
        stage.setScene(scene);
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
                    stage.setTitle("Inicio Sesion");
                    stage.centerOnScreen();
                    stage.setMaxWidth(600);
                    stage.setMaxHeight(500);
                    stage.setMinHeight(500);
                    stage.setMinWidth(400);
                    break;
                case "Registro":
                    stage.setTitle("Registro");
                    stage.centerOnScreen();
                    stage.setMaxWidth(900);
                    stage.setMaxHeight(700);
                    stage.setMinHeight(550);
                    stage.setMinWidth(400);
                    break;
                case "Problema":
                    //stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    stage.setX(screenBounds.getMinX());
                    stage.setY(screenBounds.getMinY());
                    stage.setWidth(screenBounds.getWidth());
                    stage.setHeight(screenBounds.getHeight());
                    stage.setMinWidth(922);
                    stage.setMinHeight(830);
                    stage.setTitle("Menu");
                    stage.centerOnScreen();
                    stage.setMinHeight(900);
                    stage.setMinWidth(700);
                    stage.setMaximized(true); // PANTALLA GRANDE
                    break;
                case "ActualizarDatos":
                    stage.setMinWidth(922);
                    stage.setMinHeight(830);
                    stage.setTitle("Actualizar Datos");
                    stage.centerOnScreen();
                    break;
                case "AddGastos":
                    stage.setMinWidth(922);
                    stage.setMinHeight(830);
                    stage.setTitle("Añadir Gastos");
                    stage.centerOnScreen();
                    break;
                case "AddCat":
                    stage.setMinWidth(400);
                    stage.setMinHeight(300);
                    stage.setTitle("Añadir Categoria");
                    stage.centerOnScreen();
                    break;
                case "VerCuenta":
                case "VerCuentaAño":
                case "VerCuentaCat":
                    stage.setMinWidth(922);
                    stage.setMinHeight(830);
                    stage.setTitle("Ver Cuenta");
                    stage.centerOnScreen();
                    break;
                case "ListaCargos":
                    stage.setMinWidth(922);
                    stage.setMinHeight(830);
                    stage.setTitle("Lista Cargos");
                    stage.centerOnScreen();
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