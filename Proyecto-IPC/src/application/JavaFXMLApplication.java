/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.util.HashMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class JavaFXMLApplication extends Application {
    
    private static Scene scene;
    private static HashMap<String, Parent> roots = new HashMap<>();
    private static HashMap<String, Object> controllers = new HashMap<>();
    
    @Override
    public void start(Stage stage) throws Exception {
        //======================================================================
        // 1- creación del grafo de escena a partir del fichero FXML
        FXMLLoader loader= new  FXMLLoader(getClass().getResource("InicioSesion.fxml"));
        Parent root = loader.load();
        //======================================================================
        // 2- creación de la escena con el nodo raiz del grafo de escena
        Scene scene = new Scene(root);
        //======================================================================
        // 3- asiganación de la escena al Stage que recibe el metodo 
        //     - configuracion del stage
        //     - se muestra el stage de manera no modal mediante el metodo show()
        
        //Crea la escena a partir del fichero FXML para 
        //Iniciar Sesion
        loader = new FXMLLoader(getClass().getResource("InicioSesion.fxml"));
        root = loader.load();
        roots.put("IniciarSesion", root);
        controllers.put("IniciarSesion", loader.getController());
        //Crea la escena a partir del fichero FXML para
        //Registro
        loader = new FXMLLoader(getClass().getResource("Registro.fxml"));
        root = loader.load();
        roots.put("Registro", root);
        controllers.put("Registro", loader.getController());
        //Crea la escena a partir del fichero FXML para 
        //Menu
        /*
        loader = new FXMLLoader(getClass().getResource("Menu.fxml"));
        root = loader.load();
        roots.put("Menu", root);
        controllers.put("Menu", loader.getController());
        //Crea la escena a partir del fichero FXML para 
        //ActualizarDatos
        loader = new FXMLLoader(getClass().getResource("ActualizarDatos.fxml"));
        root = loader.load();
        roots.put("ActualizarDatos", root);
        controllers.put("ActualizarDatos", loader.getController());
        //Crea la escena a partir del fichero FXML para 
        //Añadir gastos
        loader = new FXMLLoader(getClass().getResource("AddGastos.fxml"));
        root = loader.load();
        roots.put("AddGastos", root);
        controllers.put("AddGastos", loader.getController());
        //Crea la escena a partir del fichero FXML para 
        //Añadir categoria
        loader = new FXMLLoader(getClass().getResource("AddCat.fxml"));
        root = loader.load();
        roots.put("AddCat", root);
        controllers.put("AddCat", loader.getController());
        //Crea la escena a partir del fichero FXML para 
        //Ver cuenta
        loader = new FXMLLoader(getClass().getResource("VerCuenta.fxml"));
        root = loader.load();
        roots.put("VerCuenta", root);
        controllers.put("VerCuenta", loader.getController());
        //Crea la escena a partir del fichero FXML para 
        //Ver cuenta Año
        loader = new FXMLLoader(getClass().getResource("VerCuentaAño.fxml"));
        root = loader.load();
        roots.put("VerCuentaAño", root);
        controllers.put("VerCuentaAño", loader.getController());
        //Crea la escena a partir del fichero FXML para 
        //Ver cuenta Cat
        loader = new FXMLLoader(getClass().getResource("VerCuentaCat.fxml"));
        root = loader.load();
        roots.put("VerCuentaCat", root);
        controllers.put("VerCuentaCat", loader.getController());
        //Crea la escena a partir del fichero FXML para 
        //Lista de cargos
        loader = new FXMLLoader(getClass().getResource("ListaCargos.fxml"));
        root = loader.load();
        roots.put("ListaCargos", root);
        controllers.put("ListaCargos", loader.getController());
        */
        //Creación de la escena con el nodo raíz del grafo de escena
        scene = new Scene(roots.get("IniciarSesion"));
        //String css = this.getClass().getResource("/styles/EstilosLayout.css").toExternalForm();
        //scene.getStylesheets().add(css);
        //Asignación de la escena al Stage que recibe el metodo, incluyendo titulos y dimensiones
        stage.setMinWidth(500);
        stage.setMinHeight(400);
        
        stage.setScene(scene);
        stage.setTitle("Inicio de sesión");
        stage.show();
    }
    
    public static void setRoot(Parent root) {
        scene.setRoot(root);
        Stage st = (Stage)scene.getWindow();
    }
    
    public static void setRoot(String clave) {
        Parent root = roots.get(clave);
        if (root != null) {
            setRoot(root);
            
            Stage stage = (Stage) scene.getWindow();
            switch(clave) {
                
                case "IniciarSesion":
                    stage.setMinWidth(500);
                    stage.setMinHeight(400);
                    stage.setTitle("Inicio Sesion");
                    break;
                case "Registro":
                    stage.setMinWidth(922);
                    stage.setMinHeight(830);
                    stage.setTitle("Registro");
                    break;
                case "Menu":
                    stage.setMinWidth(922);
                    stage.setMinHeight(830);
                    stage.setTitle("Menu");
                    break;
                case "ActualizarDatos":
                    stage.setMinWidth(922);
                    stage.setMinHeight(830);
                    stage.setTitle("Actualizar Datos");
                    break;
                case "AddGastos":
                    stage.setMinWidth(922);
                    stage.setMinHeight(830);
                    stage.setTitle("Añadir Gastos");
                    break;
                case "AddCat":
                    stage.setMinWidth(400);
                    stage.setMinHeight(300);
                    stage.setTitle("Añadir Categoria");
                    break;
                case "VerCuenta":
                    stage.setMinWidth(922);
                    stage.setMinHeight(830);
                    stage.setTitle("Ver Cuenta");
                    break;
                case "VerCuentaAño":
                    stage.setMinWidth(922);
                    stage.setMinHeight(830);
                    stage.setTitle("Ver Cuenta");
                    break;
                case "VerCuentaCat":
                    stage.setMinWidth(922);
                    stage.setMinHeight(830);
                    stage.setTitle("Ver Cuenta");
                    break;
                case "ListaCargos":
                    stage.setMinWidth(922);
                    stage.setMinHeight(830);
                    stage.setTitle("Lista Cargos");
                    break;
            }
        }
        else {
            System.err.printf("No se encuentra la escena: %s", clave);
        }
    }
    
    public static Object getController(String clave) {
        Object controller = controllers.get(clave);
        if(controller != null){
            return controller;
        }
        else {
            System.err.printf("No se encuentra el controller: %s", clave);
            return null;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
    }


    
}
