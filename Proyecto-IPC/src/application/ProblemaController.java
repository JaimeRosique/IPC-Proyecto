/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import application.Poi;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import model.*;


public class ProblemaController implements Initializable {

    @FXML
    private ToggleButton problemaTool;
    
    private static class ProblemState {
        boolean expanded = false;
        boolean answered = false;
        String selectedAnswerText = null; // El texto de la respuesta seleccionada
        List<Answer> shuffledAnswers = new ArrayList<>(); // Las respuestas mezcladas para este problema
    }
    private Map<Problem, ProblemState> problemStates = new HashMap<>();
    private boolean answerSelected = false;
    private Line lineaTemporalCompas = null;
    private boolean modoCompasAvanzado = false;
    private List<Point2D> puntosCompas = new ArrayList<>();
    private double distanciaCompas = -1;
    private Arc arcoTemporal = null;
    private boolean modoRatonActivo = false;
    private int currentIndex = -1;
    private List<Problem> preguntasAleatorias;
    private List<Problem> problemas;
    private Navigation nav;
    private User user;
    private boolean compasActivo = false;
    private Group compasVisual;
    private Point2D centroCompas;
    private Line brazoCompas;
    private Circle pivoteCompas;
    private double radioInicialCompas = 100;
    private boolean midiendoDistancia = false;
    private List<Point2D> puntosMedicion = new ArrayList<>();
    private Line lineaMedida;
    private VBox escalaVertical;
    private final double TAMANIO_SUBDIVISION = 50.0;
    @FXML
    private Button regla;
    private boolean reglaActiva = false;
    @FXML
    private Button transportadorButton;
    @FXML
    private Button transportador;
    private boolean transportadorActivo = false;
    private double anchoBoton;
    private double altoBoton;
    private Group ghostPunto;
    private Line lineaTemporal;
    private List<Point2D> puntosArcoLibre = new ArrayList<>();
    private final List<Node> marcasSeleccionadas = new ArrayList<>();
    private boolean creandoTexto = false;
    private boolean creandoArco = false;
    private Point2D centroArco = null;
    private double radioArco = 0;
    private boolean creandoLinea = false;
    private List<Point2D> puntosLinea = new ArrayList<>();

    // Configuración del estilo de línea
    private final Color colorLinea = Color.BLUE;
    private final double grosorLinea = 3.0;
    
    private boolean creandoPunto = false;

    //=======================================
    // hashmap para guardar los puntos de interes POI
    private final HashMap<String, Poi> hm = new HashMap<>();
    private ObservableList<String> data;
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;

    @FXML
    private ListView<Problem> map_listview;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    @FXML
    private MenuButton map_pin;
    @FXML
    private MenuItem pin_info;
    @FXML
    private SplitPane splitPane;
    @FXML
    private MenuItem punto;
    @FXML
    private Pane cartaPane;
    private double offsetX;
    private double offsetY;
    @FXML
    private ImageView image_map;
    private ToggleButton toggleThemeButton;
    @FXML
    private Button limpiarCartaButton;
    @FXML
    private Button arcoButton;
    private boolean modoArcoActivo = false;
    private Point2D centroArcos = null;
    @FXML
    private Button lineaButton;
    private boolean modoLineaActivo = false;
    private Point2D primerPunto = null;
    @FXML
    private Button puntoButton;
    private boolean modoPuntosActivo = false;
    @FXML
    private Button reglaButton;
    @FXML
    private Button gomaButton;
    private boolean modoGomaActivo = false;
    @FXML
    private Button textoButton;
    private boolean modoTextoActivo = false;
    @FXML
    private MenuItem compas;
    @FXML
    private ColorPicker coloresButton;
    private boolean modoPintarActivo = false;
    private Color colorSeleccionado = Color.BLACK;
    @FXML
    private Button seleccionarButton;
    @FXML
    private Button compasButton;
    @FXML
    private StackPane toolBar;
    @FXML
    private ToggleButton toolBarButton;
    private User usuarioLogueado;
    @FXML
    private VBox rootPane;
    @FXML
    private ComboBox<Integer> grosorButton;
    @FXML
    private Circle rotadorButton;

    @FXML
    public void mostrarAyudaTransportador() {
        mostrarAyuda("Transportador y regla", "Los botones de transportador o regla hacen que estos sean visibles(inicialmente en la esquina superior izquierda). Para moverlos, haz clic y arrastra con el mouse.");
    }
    @FXML
    public void mostrarAyudaColores() {
        mostrarAyuda("Colores y grosor", "Las marcas previamente seleccionadas cambian de color o grosor desde los botones de seleccionar color o grosor. ");
    }
    @FXML
    public void mostrarAyudaDibujar() {
        mostrarAyuda("Funciones de dibujo", "Se dibuja un punto, línea o arco al pulsar en la carta náutica. Para cada punto un click, línea dos(inicio primero y final segundo) y para arco dos(primero centro y segundo punto central de la semicircunferencia).");
    }
    @FXML
    public void mostrarAyudaCompas() {
        mostrarAyuda("Compás", "El compás mide la distancia entre dos puntos y dado un ángulo crea una línea con esa distancia.");
    }
    @FXML
    public void mostrarAyudaHerramientas() {
        mostrarAyuda("Herramientas", "Cada botón activa una funcionalidad, todas ellas explicadas en el menú ''Ayuda''. La barra de herramientas puede mostrarse u ocultarse en el botón superior también están las mismas funcionalidades en el menú ''Herramientas''.");
    }
    public void mostrarAyudaSeleccionar() {
        mostrarAyuda("Seleccionar", "Al activar esta funcionalidad se desactivan las demás para poder seleccionar marcas o moverte sin dibujar. Usando shift puedes seleccionar más de una marca.");
    }
    @FXML
    public void mostrarAyudaBorrar() {
        mostrarAyuda("Borrar y limpiar", "Con la goma borras las marcas previamente seleccionadas. Del botón ''X'' borras todas las marcas de la carta náutica.");
    }
    @FXML
    public void mostrarAyudaProblemas() {
        mostrarAyuda("Problemas", "Los problemas salen a la izquierda, existe la opción de elegir un problema(clicando en ese problema en la lista) o de elegir uno aleatoriamente. Al pulsar se carga el problema y te da las 4 opciones, al volver a pulsar vuelve a la lista de problemas; elige la correcta.");
    }
    private void mostrarAyuda(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ayuda - " + titulo);
        alert.setHeaderText(titulo);
        
        Label label = new Label(contenido);
        label.setWrapText(true);
        label.setMaxWidth(400); // Ajusta esto según el tamaño que prefieras

        VBox vbox = new VBox(label);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        alert.getDialogPane().setContent(vbox);
        alert.getDialogPane().setPrefWidth(450); // Ancho del diálogo
        alert.getDialogPane().setPrefHeight(Region.USE_COMPUTED_SIZE); // Altura automática

        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/resources/compas.png")));
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource(ThemeManager.getEstiloActual()).toExternalForm());
        dialogPane.getStyleClass().add(" ");
        
        alert.showAndWait();
    }
    
    private void actualizarPosicionBoton() {
	Bounds bounds = regla.localToParent(regla.getBoundsInLocal());

	double reglaX = bounds.getMinX();
	double reglaY = bounds.getMinY();
	double reglaWidth = bounds.getWidth();
	double reglaHeight = bounds.getHeight();

	rotadorButton.setLayoutX(reglaX + reglaWidth / 2);
	rotadorButton.setLayoutY(reglaY + reglaHeight / 2);
}
    
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
        
    @FXML
    void zoomIn(ActionEvent event) {
        //================================================
        // el incremento del zoom dependerá de los parametros del 
        // slider y del resultado esperado
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }
    
    private void drawArc(Point2D center, double radius) {
        Arc arc = new Arc();
        arc.setCenterX(center.getX());
        arc.setCenterY(center.getY());
        arc.setRadiusX(radius);
        arc.setRadiusY(radius);
        arc.setStartAngle(0); // ángulo inicial en grados
        arc.setLength(180);   // ángulo de arco (puedes hacerlo configurable)

        arc.setType(ArcType.OPEN); // arco abierto
        arc.setStroke(javafx.scene.paint.Color.BLUE); // usa tu color predefinido
        arc.setStrokeWidth(grosorLinea); // usa tu grosor predefinido
        arc.setFill(null); // sin relleno
        
        arc.setOnMouseClicked(e -> {
            if (modoRatonActivo && e.getButton() == MouseButton.PRIMARY) {
                e.consume();
                if (!e.isShiftDown()) {
                    limpiarSeleccion();
                }
                seleccionarMarca(arc);
            }
        });

        cartaPane.getChildren().add(arc);
    }
    public void setUsuarioLogueado(User usuario) {
        this.usuarioLogueado = usuario;
    }
    @FXML
    public void modificarPerfilAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ModificarRegistro.fxml"));
        Parent root = loader.load();
        ModificarRegistroController controller = loader.getController();

        controller.cargarDatosUsuario(user);
        controller.setModoModificar(true);
        controller.setDst("Problema");

        JavaFXMLApplication.setRoot(root);                
    }
    @FXML
    public void cerrarSesion(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("InicioSesion.fxml"));
    Parent root = loader.load();

    JavaFXMLApplication.setRoot(root);
}

    
    private void seleccionarMarca(Node nodo) {
        if (!marcasSeleccionadas.contains(nodo)) {
            javafx.scene.effect.DropShadow efecto = new javafx.scene.effect.DropShadow();
            efecto.setColor(Color.GOLD);
            efecto.setRadius(20);
            efecto.setSpread(0.6);
            nodo.setEffect(efecto);
            marcasSeleccionadas.add(nodo);
        }
    }
    
    private void limpiarSeleccion() {
        for (Node n : marcasSeleccionadas) {
            n.setEffect(null);
        }
        marcasSeleccionadas.clear();
    }
    
    private Point2D buscarPuntoCercano(Point2D puntoClic) {
        double umbral = 15.0;

        for (Node nodo : cartaPane.getChildren()) {
            if (nodo instanceof Group) {
                Group grupo = (Group) nodo;

                if (grupo.getChildren().size() == 2 &&
                    grupo.getChildren().get(0) instanceof Circle &&
                    grupo.getChildren().get(1) instanceof Circle) {

                    Circle c1 = (Circle) grupo.getChildren().get(0);
                    Point2D centro = new Point2D(c1.getCenterX(), c1.getCenterY());

                    if (centro.distance(puntoClic) < umbral) {
                        return centro;
                    }
                }
            }
        }

        return null;
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

    @FXML
    void listClicked(MouseEvent event) {
        /*
        Poi itemSelected = map_listview.getSelectionModel().getSelectedItem();

        // Animación del scroll hasta la mousePosistion del item seleccionado
        double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
        double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
        double scrollH = itemSelected.getPosition().getX() / mapWidth;
        double scrollV = itemSelected.getPosition().getY() / mapHeight;
        final Timeline timeline = new Timeline();
        final KeyValue kv1 = new KeyValue(map_scrollpane.hvalueProperty(), scrollH);
        final KeyValue kv2 = new KeyValue(map_scrollpane.vvalueProperty(), scrollV);
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv1, kv2);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        // movemos el objto map_pin hasta la mousePosistion del POI
//        double pinW = map_pin.getBoundsInLocal().getWidth();
//        double pinH = map_pin.getBoundsInLocal().getHeight();
        map_pin.setLayoutX(itemSelected.getPosition().getX());
        map_pin.setLayoutY(itemSelected.getPosition().getY());
        pin_info.setText(itemSelected.getDescription());
        map_pin.setVisible(true);
        */
    }
    
    private void activarCompas(Point2D centro) {
        if (compasVisual != null) {
            cartaPane.getChildren().remove(compasVisual);
        }

        centroCompas = centro;

        // Círculo fijo en el centro
        pivoteCompas = new Circle(centro.getX(), centro.getY(), 5, Color.DARKBLUE);

        // Brazo que gira
        brazoCompas = new Line();
        brazoCompas.setStartX(centro.getX());
        brazoCompas.setStartY(centro.getY());
        brazoCompas.setEndX(centro.getX() + radioInicialCompas);
        brazoCompas.setEndY(centro.getY());

        brazoCompas.setStroke(Color.DARKRED);
        brazoCompas.setStrokeWidth(2);

        // Agrupar
        compasVisual = new Group(brazoCompas, pivoteCompas);
        cartaPane.getChildren().add(compasVisual);

        // Permitir rotar el brazo arrastrando
        brazoCompas.setOnMouseDragged(e -> {
            double dx = e.getX() - centroCompas.getX();
            double dy = e.getY() - centroCompas.getY();
            double distancia = Math.sqrt(dx * dx + dy * dy);

            // Mantener longitud (radio fijo)
            double angle = Math.atan2(dy, dx);
            double endX = centroCompas.getX() + radioInicialCompas * Math.cos(angle);
            double endY = centroCompas.getY() + radioInicialCompas * Math.sin(angle);
            brazoCompas.setEndX(endX);
            brazoCompas.setEndY(endY);
        });

        // Al hacer doble clic: dibuja un arco
        brazoCompas.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                drawArc(centroCompas, radioInicialCompas); // Usa tu función
            }
        });
    }
    
    private void limpiar() {
        modoRatonActivo = false;
        modoTextoActivo = false;
        modoPuntosActivo = false;
        modoLineaActivo = false;
        modoArcoActivo = false;
        modoGomaActivo = false;
        modoPintarActivo = false;
        modoCompasAvanzado = false;

        creandoPunto = false;
        creandoLinea = false;
        creandoArco = false; // ← IMPORTANTE
        centroArcos = null;
        centroArco = null;
        primerPunto = null;
        radioArco = 0;
        puntosLinea.clear();
        distanciaCompas = -1; 
        puntosCompas.clear();

        if (ghostPunto != null) {
            cartaPane.getChildren().remove(ghostPunto);
            ghostPunto = null;
        }
        if (lineaTemporal != null) {
            cartaPane.getChildren().remove(lineaTemporal);
            lineaTemporal = null;
        }
        if (arcoTemporal != null) {
            cartaPane.getChildren().remove(arcoTemporal);
            arcoTemporal = null;
        }
        if (lineaTemporalCompas != null) {
            cartaPane.getChildren().remove(lineaTemporalCompas);
            lineaTemporalCompas = null;
        }

        cartaPane.setCursor(Cursor.DEFAULT);
        //cartaPane.setOnMouseMoved(null);
    }
        
    @FXML
    private void activarModoRaton() {
        limpiar();
        modoRatonActivo = true;
    }
    
    @FXML
    private void activarModoPunto() {
        /*
        limpiar();
        modoPuntosActivo = true;
        
        creandoPunto = true;

        if (ghostPunto == null) {
            Circle externo = new Circle(0, 0, 12);
            externo.setStroke(Color.GRAY);
            externo.setStrokeWidth(3);
            externo.setFill(Color.TRANSPARENT);

            Circle interno = new Circle(0, 0, 5);
            interno.setFill(Color.GRAY);

            ghostPunto = new Group(externo, interno);
            ghostPunto.setMouseTransparent(true);
            cartaPane.getChildren().add(ghostPunto);
        }
        Platform.runLater(() -> {
            cartaPane.requestFocus();
            cartaPane.fireEvent(new MouseEvent(MouseEvent.MOUSE_MOVED,
                cartaPane.getWidth() / 2, cartaPane.getHeight() / 2,
                cartaPane.getWidth() / 2, cartaPane.getHeight() / 2,
                MouseButton.NONE, 0, false, false, false, false,
                false, false, false, false, false, false, null));
        });
        //cartaPane.setCursor(Cursor.CROSSHAIR);
        */
        limpiar(); // Call limpiar first to clear previous states and potential ghostPunto cleanup
        modoPuntosActivo = true;
        creandoPunto = true;

        // Add ghostPunto to the pane ONLY when activating point mode, and ensure it's on top
        if (!cartaPane.getChildren().contains(ghostPunto)) {
            cartaPane.getChildren().add(ghostPunto);
        }
        ghostPunto.toFront(); // <--- Ensure it's always on top when added/activated
        ghostPunto.setVisible(true); // Ensure it's visible when point mode is active

        Platform.runLater(() -> {
            cartaPane.requestFocus();
            // Simulate a mouse move to position the ghostPunto immediately
            // This is good for initial positioning but 'toFront()' is key for layering
            cartaPane.fireEvent(new MouseEvent(MouseEvent.MOUSE_MOVED,
                cartaPane.getWidth() / 2, cartaPane.getHeight() / 2,
                cartaPane.getWidth() / 2, cartaPane.getHeight() / 2,
                MouseButton.NONE, 0, false, false, false, false,
                false, false, false, false, false, false, null));
        });
        cartaPane.setCursor(Cursor.CROSSHAIR);
    }

    @FXML
    private void activarModoLinea() {
        limpiar();
        modoLineaActivo = true;
   
        creandoLinea = true;
        puntosLinea.clear();
        if (lineaTemporal != null) {
            cartaPane.getChildren().remove(lineaTemporal);
            lineaTemporal = null;
        }
        cartaPane.setCursor(Cursor.CROSSHAIR);
    }
    
    @FXML
    private void activarModoArco() {
        limpiar();
        modoArcoActivo = true;
        //centroArco = null;
        //radioArco = 0;

        cartaPane.setCursor(Cursor.CROSSHAIR);
        
    }
    
    @FXML
    private void activarModoTexto() {
        modoTextoActivo = true;
        modoPuntosActivo = false;
        modoLineaActivo = false;
        modoArcoActivo = false;
        modoGomaActivo = false;
        modoPintarActivo = false;
        centroArcos = null;
        primerPunto = null;
    }
    
    @FXML
    private void activarTransportador() {
        limpiar();
        transportadorActivo = !transportadorActivo;
        transportador.setVisible(transportadorActivo);
    }
    @FXML
    private void activarRegla() {
        limpiar();
        reglaActiva = !reglaActiva;
        regla.setVisible(reglaActiva);
    }
    
    @FXML
    private void activarModoGoma() {
        /*
        limpiar();
        modoGomaActivo = true;
        if (!marcasSeleccionadas.isEmpty()) {
            for (Node n : new ArrayList<>(marcasSeleccionadas)) {
                cartaPane.getChildren().remove(n);
            }
            marcasSeleccionadas.clear();
        }
        */
        limpiar(); // This already handles many cleanup tasks, but we'll add more specific ghostPunto handling
        modoGomaActivo = true;

        // Immediately hide and remove ghostPunto if it exists and is visible
        if (ghostPunto != null) {
            cartaPane.getChildren().remove(ghostPunto);
            ghostPunto = null; // Set to null to ensure it's re-created if point mode is activated again
        }

        if (!marcasSeleccionadas.isEmpty()) {
            for (Node n : new ArrayList<>(marcasSeleccionadas)) {
                cartaPane.getChildren().remove(n);
            }
            marcasSeleccionadas.clear();
        }
    }
    @FXML
    private void activarModoPintar() {
        modoPintarActivo = true;
        modoGomaActivo = false;
        modoTextoActivo = false;
        modoPuntosActivo = false;
        modoLineaActivo = false;
        modoArcoActivo = false;
        centroArcos = null;
        primerPunto = null;
    }
    private void hacerInteractivo(Node nodo) {
        nodo.setOnMouseClicked(event -> {
            if (modoGomaActivo) {
                Node objetivo = event.getPickResult().getIntersectedNode();
                if (objetivo instanceof Line || objetivo instanceof Circle || objetivo instanceof Arc || objetivo instanceof TextField || objetivo instanceof Group) {
                    cartaPane.getChildren().remove(objetivo);
                    marcasSeleccionadas.remove(objetivo);
                }
            } else if (modoPintarActivo && marcasSeleccionadas.contains(nodo)) {
                Color nuevoColor = coloresButton.getValue();

                if (nodo instanceof Shape) {
                    Shape shape = (Shape) nodo;
                    shape.setStroke(nuevoColor);
                    if (!(shape instanceof Line)) {
                        shape.setFill(nuevoColor);
                    }
                    if (shape instanceof Arc) {
                        shape.setFill(null);
                    }

                } else if (nodo instanceof Group) {
                    Group grupo = (Group) nodo;
                    for (Node child : grupo.getChildrenUnmodifiable()) {
                        if (child instanceof Shape) {
                            Shape shape = (Shape) child;
                            shape.setStroke(nuevoColor);
                            if (!(shape instanceof Line)) {
                                shape.setFill(nuevoColor);
                            }
                        }
                    }

                } else if (nodo instanceof Label) {
                    Label label = (Label) nodo;
                    label.setTextFill(nuevoColor);
                }

                event.consume();
            }
        });
    }
    
    @FXML
    private void limpiarCarta() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setGraphic(null);
        Stage alertStage = (Stage) alerta.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/resources/compas.png")));
        alerta.setTitle("Confirmación");
        alerta.setHeaderText("¿Estás seguro de que quieres limpiar la carta entera?");
        alerta.setContentText("Se eliminarán todo lo que hayas escrito hasta ahora.");
        DialogPane dialogPane = alerta.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource(ThemeManager.getEstiloActual()).toExternalForm());
        dialogPane.getStyleClass().add(" ");
        //Esperar la respuesta del usuario
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.isPresent() && resultado.get() == ButtonType.OK) {
            cartaPane.getChildren().removeIf(nodo -> nodo != image_map && nodo != transportador && nodo != regla);
        }
    }
    
    public void setDatos(Navigation n, User u) {
        nav = n;
        problemas = nav.getProblems();
        user=u;
        initData();
        //randomData();
    }
    
    @FXML
    private void cambiarGrosor() {
         if (marcasSeleccionadas != null && grosorButton.getValue() != null) {
            int nuevoGrosor = grosorButton.getValue();
            // This needs to iterate through marcasSeleccionadas and apply to each shape
            for (Node nodo : marcasSeleccionadas) {
                if (nodo instanceof Shape) {
                    ((Shape) nodo).setStrokeWidth(nuevoGrosor);
                } else if (nodo instanceof Group) {
                    Group grupo = (Group) nodo;
                    for (Node child : grupo.getChildrenUnmodifiable()) {
                        if (child instanceof Shape) {
                            ((Shape) child).setStrokeWidth(nuevoGrosor);
                        }
                    }
                }
            }
        }
    }
    
    private void manejarRespuesta(Problem problem, ProblemState st, Answer a, Button btn, List<Button> botones) {
        if (st.answered) return;

        st.answered = true;
        st.selectedAnswerText = btn.getText();
        botones.forEach(b -> b.setDisable(true));

        boolean esCorrecta = a.getValidity();

        if (esCorrecta) {
            btn.setStyle("-fx-background-color: lightgreen;");
            if (user != null) {
                user.addSession(1, 0);
            }
        } else {
            btn.setStyle("-fx-background-color: red;");
            if (user != null) {
                user.addSession(0, 1);
            }
            // Si la respuesta es incorrecta, busca y resalta la correcta
            for (int i = 0; i < st.shuffledAnswers.size(); i++) {
                if (st.shuffledAnswers.get(i).getValidity()) {
                    botones.get(i).setStyle("-fx-background-color: lightgreen;");
                }
            }
        }

        // Imprime el error solo una vez si el usuario es nulo
        if (user == null) {
            System.err.println("Error: El objeto 'user' es nulo. No se pudo guardar la sesión.");
        }

        map_listview.refresh();
    }

    public void initData() {
        if (problemas == null || problemas.isEmpty()) {
            System.err.println("Advertencia: La lista de problemas está vacía.");
            return;
        }

        problemStates.clear();
        for (Problem p : problemas) {
            ProblemState state = new ProblemState();
            state.shuffledAnswers = new ArrayList<>(p.getAnswers());
            Collections.shuffle(state.shuffledAnswers);
            problemStates.put(p, state);
        }

        map_listview.setItems(FXCollections.observableArrayList(problemas));
        // map_listview.setFixedCellSize(-1); // Consider removing or setting dynamically if content size varies greatly

        map_listview.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Problem problem, boolean empty) {
                super.updateItem(problem, empty);

                if (empty || problem == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                ProblemState st = problemStates.get(problem);
                if (st == null) {
                    // This case should ideally not happen if problemStates is pre-populated correctly
                    st = new ProblemState();
                    st.shuffledAnswers = new ArrayList<>(problem.getAnswers());
                    Collections.shuffle(st.shuffledAnswers);
                    problemStates.put(problem, st);
                }

                final ProblemState finalSt = st; // Reference for lambdas

                VBox vbox = new VBox(10);
                vbox.setPadding(new Insets(10));
                vbox.setFillWidth(true);
                vbox.prefWidthProperty().bind(map_listview.widthProperty().subtract(20));
                vbox.setMaxWidth(Double.MAX_VALUE);

                Label pregunta = new Label("Pregunta: " + problem.getText());
                pregunta.setWrapText(true);
                pregunta.setStyle("-fx-font-weight: bold;");
                pregunta.prefWidthProperty().bind(vbox.prefWidthProperty());
                vbox.getChildren().add(pregunta);

                List<Button> answerButtons = new ArrayList<>(); // Use a different name to avoid confusion with parameter 'botones' in manejarRespuesta

                // Always render answers if expanded or answered
                if (finalSt.expanded || finalSt.answered) {
                    for (Answer a : finalSt.shuffledAnswers) {
                        Button btn = new Button(a.getText());
                        btn.setWrapText(true);
                        btn.setMaxWidth(Double.MAX_VALUE);
                        btn.prefWidthProperty().bind(vbox.prefWidthProperty());
                        btn.setId("listButton"); // Add ID for CSS if needed

                        answerButtons.add(btn);
                        vbox.getChildren().add(btn);

                        // Apply styles and disable based on state
                        if (finalSt.answered) {
                            btn.setDisable(true); // Disable all answer buttons after answering

                            // Re-apply styles based on stored answer text and validity
                            if (Objects.equals(finalSt.selectedAnswerText, a.getText())) {
                                 // This was the user's selected button
                                btn.setStyle(a.getValidity() ? "-fx-background-color: lightgreen;" : "-fx-background-color: red;");
                            } else if (a.getValidity()) {
                                // This is the correct answer, but not the one selected by the user
                                btn.setStyle("-fx-background-color: lightgreen;");
                            } else {
                                // Reset style for unselected incorrect answers
                                btn.setStyle(null); // Or your default button style
                            }

                        } else {
                            // Attach action only if not yet answered
                            final Answer answerRef = a;
                            final Button buttonRef = btn;
                            btn.setOnAction(ev -> manejarRespuesta(problem, finalSt, answerRef, buttonRef, answerButtons)); // Pass 'answerButtons'
                        }
                    }

                    // Add the "Next" button only if the question has been answered
                    if (finalSt.answered) {
                        Button nextButton = new Button("Siguiente");
                        nextButton.setMaxWidth(Double.MAX_VALUE);
                        nextButton.prefWidthProperty().bind(vbox.prefWidthProperty());
                        nextButton.setOnAction(ev -> {
                            // Find the index of the current problem
                            int currentProblemIndex = problemas.indexOf(problem);
                            if (currentProblemIndex < problemas.size() - 1) {
                                // Scroll to the next problem
                                map_listview.scrollTo(currentProblemIndex + 1);
                            } else {
                                // All questions answered
                                Alert fin = new Alert(Alert.AlertType.INFORMATION, "¡Has respondido todas las preguntas!");
                                Stage alertStage = (Stage) fin.getDialogPane().getScene().getWindow();
                                alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/compas.png")));
                                fin.setGraphic(null);
                                DialogPane dialogPane = fin.getDialogPane();
                                dialogPane.getStylesheets().add(getClass().getResource(ThemeManager.getEstiloActual()).toExternalForm());
                                dialogPane.getStyleClass().add(" ");
                                fin.showAndWait();
                            }
                        });
                        vbox.getChildren().add(nextButton);
                    }
                }

                pregunta.setOnMouseClicked(e -> {
                    if (!finalSt.answered) { // Only allow expanding/collapsing if not answered
                        finalSt.expanded = !finalSt.expanded;
                        map_listview.refresh();
                    }
                });

                setMinHeight(Region.USE_COMPUTED_SIZE);
                setPrefHeight(Region.USE_COMPUTED_SIZE);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                setGraphic(vbox);
            }
        });
    }

    private void randomData() {
        if (problemas == null || problemas.isEmpty()) {
            System.err.println("Advertencia: La lista de problemas está vacía. Carga tus problemas antes de usar randomData().");
            return;
        }

        if (preguntasAleatorias == null || preguntasAleatorias.isEmpty()) {
            preguntasAleatorias = new ArrayList<>(problemas);
            Collections.shuffle(preguntasAleatorias);
            currentIndex = -1; // Para que la primera llamada lo ponga en 0
        }

        currentIndex++;
        if (currentIndex >= preguntasAleatorias.size()) {
            Alert fin = new Alert(Alert.AlertType.INFORMATION, "¡Has respondido todas las preguntas!");
            Stage alertStage = (Stage) fin.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/compas.png")));
            fin.setGraphic(null);
            DialogPane dialogPane = fin.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource(ThemeManager.getEstiloActual()).toExternalForm());
            dialogPane.getStyleClass().add(" ");
            fin.showAndWait();
            return;
        }

        Problem problemaActual = preguntasAleatorias.get(currentIndex);
        List<Answer> respuestas = new ArrayList<>(problemaActual.getAnswers());
        Collections.shuffle(respuestas);

        map_listview.setItems(FXCollections.observableArrayList(List.of(problemaActual)));
        
        // --- Importante: La magia ocurre dentro de la ListCell ---
        map_listview.setCellFactory(lv -> new ListCell<Problem>() {
            // Variables de estado dentro de la celda para esta pregunta específica
            private boolean answered = false;
            private Button selectedButton = null; // Para recordar qué botón se seleccionó
            private Button nextButton = null; // Referencia al botón Siguiente

            @Override
            protected void updateItem(Problem p, boolean empty) {
                super.updateItem(p, empty);

                if (empty || p == null) {
                    setGraphic(null);
                } else {
                    VBox vbox = new VBox(10);
                    vbox.setFillWidth(true);
                    vbox.setMaxWidth(Double.MAX_VALUE);
                    vbox.prefWidthProperty().bind(map_listview.widthProperty().subtract(20));

                    Label pregunta = new Label(p.getText());
                    pregunta.setWrapText(true);
                    pregunta.setMaxWidth(Double.MAX_VALUE);
                    pregunta.setStyle("-fx-font-weight: bold;");
                    pregunta.prefWidthProperty().bind(vbox.prefWidthProperty());

                    vbox.getChildren().add(pregunta); // Añadir la pregunta primero

                    List<Button> botones = new ArrayList<>();

                    for (Answer a : respuestas) {
                        Button btn = new Button(a.getText());
                        btn.setId("listButton");
                        btn.setWrapText(true);
                        btn.setMaxWidth(Double.MAX_VALUE);
                        btn.prefWidthProperty().bind(vbox.prefWidthProperty());
                        botones.add(btn); // Añadir a la lista de botones antes de configurar el setOnAction
                        vbox.getChildren().add(btn); // Añadir a la VBox

                        // --- Configurar el evento de clic ---
                        btn.setOnAction(e -> {
                            if (answered) {
                                return; // Si ya se respondió, no hagas nada (evita clics duplicados)
                            }
                            answered = true; // Marca que esta pregunta ha sido respondida
                            selectedButton = btn; // Guarda el botón que el usuario seleccionó

                            // Deshabilitar todos los botones de respuesta
                            botones.forEach(b -> b.setDisable(true));

                            // Lógica para registrar la sesión y aplicar estilos
                            if (user != null) {
                                if (a.getValidity()) {
                                    selectedButton.setStyle("-fx-background-color: lightgreen;");
                                    user.addSession(1, 0); // 1 acierto, 0 fallos
                                } else {
                                    selectedButton.setStyle("-fx-background-color: red;");
                                    user.addSession(0, 1); // 0 aciertos, 1 fallo

                                    // Mostrar la respuesta correcta en verde
                                    for (int i = 0; i < respuestas.size(); i++) {
                                        if (respuestas.get(i).getValidity()) {
                                            botones.get(i).setStyle("-fx-background-color: lightgreen;");
                                        }
                                    }
                                }
                            } else {
                                System.err.println("Error: El objeto 'user' es nulo. No se pudo guardar la sesión.");
                                if (a.getValidity()) {
                                    selectedButton.setStyle("-fx-background-color: lightgreen;");
                                } else {
                                    selectedButton.setStyle("-fx-background-color: red;");
                                    for (int i = 0; i < respuestas.size(); i++) {
                                        if (respuestas.get(i).getValidity()) {
                                            botones.get(i).setStyle("-fx-background-color: lightgreen;");
                                        }
                                    }
                                }
                            }

                            // Añadir el botón "Siguiente"
                            nextButton = new Button("Siguiente");
                            nextButton.setMaxWidth(Double.MAX_VALUE);
                            nextButton.prefWidthProperty().bind(vbox.prefWidthProperty());
                            nextButton.setOnAction(ev -> randomData()); // Pasa a la siguiente pregunta
                            vbox.getChildren().add(nextButton);
                        });
                    }

                    // --- Renderizar el estado guardado al refrescar la celda ---
                    if (answered) {
                        // Si ya se respondió, aplica los estilos y el botón "Siguiente"
                        // basándose en el estado guardado o recalculado.
                        // Volvemos a aplicar los estilos si la celda se reutiliza o refresca
                        for (int i = 0; i < respuestas.size(); i++) {
                            Answer currentAnswer = respuestas.get(i);
                            Button currentButton = botones.get(i);

                            if (selectedButton != null && currentButton == selectedButton) {
                                // Este es el botón que el usuario seleccionó originalmente
                                if (currentAnswer.getValidity()) {
                                    currentButton.setStyle("-fx-background-color: lightgreen;");
                                } else {
                                    currentButton.setStyle("-fx-background-color: red;");
                                }
                            } else if (currentAnswer.getValidity()) {
                                // Este es el botón correcto (si no fue el seleccionado por el usuario)
                                currentButton.setStyle("-fx-background-color: lightgreen;");
                            }
                            currentButton.setDisable(true); // Deshabilita todos los botones de respuesta
                        }

                        // Asegúrate de que el botón Siguiente esté presente
                        if (nextButton == null) { // Si la celda se refresca, puede que el nextButton sea nulo
                            nextButton = new Button("Siguiente");
                            nextButton.setMaxWidth(Double.MAX_VALUE);
                            nextButton.prefWidthProperty().bind(vbox.prefWidthProperty());
                            nextButton.setOnAction(ev -> randomData());
                        }
                        // Solo añadir si no está ya en los hijos (para evitar duplicados al refrescar)
                        if (!vbox.getChildren().contains(nextButton)) {
                             vbox.getChildren().add(nextButton);
                        }
                    }

                    setGraphic(vbox);
                }
            }
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (int i = 1; i <= 300; i++) {
            grosorButton.getItems().add(i);
        }
         // Espera a que el nodo esté en escena para obtener el Stage
        rootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                Image icono = new Image(getClass().getResourceAsStream("compas"));
                stage.getIcons().add(icono);
            }
        });
        
        // Add action listener to problemaTool toggle button
        problemaTool.setOnAction(event -> {
            if (problemaTool.isSelected()) {
                // If selected, switch to random data mode
                //isRandomMode = true;
                randomData();
            } else {
                // If unselected, switch back to initial data mode
                //isRandomMode = false;
                initData();
            }
        });

        // Initialize with default mode (initData)
        problemaTool.setSelected(false);
        
        // TODO
        //==========================================================
        // inicializamos el slider y enlazamos con el zoom
        zoom_slider.setMin(0.125);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(0.6);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));
             
        toolBarButton.setOnAction(e -> {
            toolBar.setVisible(!toolBar.isVisible());
        });
        
        transportador.setVisible(false);
        transportador.setOnMousePressed(event -> {
            offsetX = event.getX();
            offsetY = event.getY();
        });
        transportador.setOnMouseDragged(event -> {
            // Convertir la posición del mouse en escena a coordenadas dentro de cartaPane
            Point2D localCoords = cartaPane.sceneToLocal(event.getSceneX(), event.getSceneY());

            double newX = localCoords.getX() - transportador.getWidth() / 2;
            double newY = localCoords.getY() - transportador.getHeight() / 2;

            // Límites para que no se salga del pane
            double anchoPane = cartaPane.getWidth();
            double altoPane = cartaPane.getHeight();

            if (newX >= 0 && newX <= anchoPane - transportador.getWidth()) {
                transportador.setLayoutX(newX);
        }
            if (newY >= 0 && newY <= altoPane - transportador.getHeight()) {
                transportador.setLayoutY(newY);
            }
        });        
        
        regla.setVisible(false); // Oculta al principio
        rotadorButton.setVisible(false); //Oculta al principio
        regla.setOnMousePressed(event -> {
                offsetX = event.getX();
                offsetY = event.getY();
        });
        regla.setOnMouseDragged(event -> {
                // Convertir la posición del mouse en escena a coordenadas dentro de cartaPane
            Point2D localCoords = cartaPane.sceneToLocal(event.getSceneX(), event.getSceneY());

            double newX = localCoords.getX() - regla.getWidth() / 2;
            double newY = localCoords.getY() - regla.getHeight() / 2;

            // Límites para que no se salga del pane
            double anchoPane = cartaPane.getWidth();
            double altoPane = cartaPane.getHeight();

            if (newX >= 0 && newX <= anchoPane - regla.getWidth()) {
                regla.setLayoutX(newX);
            }
            if (newY >= 0 && newY <= altoPane - regla.getHeight()) {
                regla.setLayoutY(newY);
            }
            actualizarPosicionBoton();
    	});
   	 
    	rotadorButton.setOnMouseDragged(event -> {
        	// Calcular ángulo de rotación
        	double mouseX = event.getSceneX();
        	double mouseY = event.getSceneY();

        	Bounds boundsInScene = regla.localToScene(regla.getBoundsInLocal());
        	double centerX = (boundsInScene.getMinX() + boundsInScene.getMaxX()) / 2;
        	double centerY = (boundsInScene.getMinY() + boundsInScene.getMaxY()) / 2;

        	double deltaX = mouseX - centerX;
        	double deltaY = mouseY - centerY;
        	double angle = Math.toDegrees(Math.atan2(deltaY, deltaX));

        	// Aplicar rotación
        	regla.getTransforms().clear();
        	Rotate rotate = new Rotate(angle, regla.getBoundsInLocal().getWidth() / 2, regla.getBoundsInLocal().getHeight() / 2);
        	regla.getTransforms().add(rotate);

        	// ❗Evitar que el mapa de fondo se mueva
        	event.consume();
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
        
        Platform.runLater(() -> {
            splitPane.setDividerPositions(0.23);
        });
        
        splitPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isControlDown()) {
                event.consume();  // Prevenir cualquier otro scroll

                double delta = event.getDeltaY();
                double currentZoom = zoom_slider.getValue();
                double zoomFactor = 0.1; // Ajusta la sensibilidad si quieres

                if (delta > 0) {
                    zoom_slider.setValue(Math.min(zoom_slider.getMax(), currentZoom + zoomFactor));
                } else {
                    zoom_slider.setValue(Math.max(zoom_slider.getMin(), currentZoom - zoomFactor));
                }
            }
        });
        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
        
        //transportador = new ImageView();
        //Image imgTransportador = new Image(getClass().getResourceAsStream("/resources/transportador.png"));
        //transportador.setImage(imgTransportador);
        //transportador.setVisible(transportadorActivo);
        //zoomGroup.getChildren().add(transportador);

        // Configurar arrastre del transportador
        final Delta dragDelta = new Delta();
        
        cartaPane.setOnMouseMoved(event -> {
            if (creandoLinea && puntosLinea.size() == 1 && lineaTemporal != null) {
                lineaTemporal.setEndX(event.getX());
                lineaTemporal.setEndY(event.getY());
            } else if (creandoPunto && ghostPunto != null) {
                double x = event.getX();
                double y = event.getY();

                // Limitar a los bordes del mapa
                double mapWidth = image_map.getBoundsInParent().getWidth();
                double mapHeight = image_map.getBoundsInParent().getHeight();

                if (x >= 0 && x <= mapWidth && y >= 0 && y <= mapHeight) {
                    ghostPunto.setVisible(true);
                    ghostPunto.setLayoutX(x);
                    ghostPunto.setLayoutY(y);
                } else {
                    ghostPunto.setVisible(false); // ocultar si está fuera
                }
            } if (modoArcoActivo && arcoTemporal != null && centroArcos != null) {
                Point2D puntoCircunferencia = new Point2D(event.getX(), event.getY());

                // Calcular el radio propuesto
                double radioPropuesto = centroArcos.distance(puntoCircunferencia);

                // Limitar el radio máximo según los bordes del pane
                double maxIzquierda = centroArcos.getX(); // Distancia al borde izquierdo
                double maxDerecha = cartaPane.getWidth() - centroArcos.getX(); // borde derecho
                double maxArriba = centroArcos.getY(); // borde superior
                double maxAbajo = cartaPane.getHeight() - centroArcos.getY(); // borde inferior

                double radioMaximo = Math.min(Math.min(maxIzquierda, maxDerecha), Math.min(maxArriba, maxAbajo));
                double radioFinal = Math.min(radioPropuesto, radioMaximo);

                // Calcular ángulo y aplicar
                double deltaX = puntoCircunferencia.getX() - centroArcos.getX();
                double deltaY = puntoCircunferencia.getY() - centroArcos.getY();
                double angle = Math.toDegrees(Math.atan2(-deltaY, deltaX));
                double startAngle = angle - 90;

                arcoTemporal.setRadiusX(radioFinal);
                arcoTemporal.setRadiusY(radioFinal);
                arcoTemporal.setStartAngle(startAngle);
            } if (modoCompasAvanzado && puntosCompas.size() == 1) {
                Point2D p1 = puntosCompas.get(0);
                Point2D p2 = new Point2D(event.getX(), event.getY());
                if (lineaTemporalCompas != null) cartaPane.getChildren().remove(lineaTemporalCompas);
                lineaTemporalCompas = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                lineaTemporalCompas.getStrokeDashArray().addAll(5.0, 5.0);
                lineaTemporalCompas.setStroke(colorLinea);
                lineaTemporalCompas.setStrokeWidth(grosorLinea);
                cartaPane.getChildren().add(lineaTemporalCompas);
            }
        }); 
        
        coloresButton.setOnAction(e -> {
            Color nuevoColor = coloresButton.getValue();
            for (Node nodo : marcasSeleccionadas) {
                if (nodo instanceof Shape) {
                    Shape shape = (Shape) nodo;
                    shape.setStroke(nuevoColor);
                    if (!(shape instanceof Line)) {
                        shape.setFill(nuevoColor);
                    }
                    if (shape instanceof Arc) {
                        shape.setFill(null); // Para los arcos, mantener transparencia
                    }
                } else if (nodo instanceof Group) {
                    Group grupo = (Group) nodo;
                    for (Node child : grupo.getChildrenUnmodifiable()) {
                        if (child instanceof Circle) {
                            Circle c = (Circle) child;
                            if (c.getRadius() > 10) {
                                c.setStroke(nuevoColor);      // borde del externo
                                c.setFill(Color.TRANSPARENT); // externo sin relleno
                            } else {
                                c.setFill(nuevoColor);        // interno con relleno
                                c.setStroke(nuevoColor);      // opcional
                            }
                        }
                    }
                } else if (nodo instanceof Label) {
                    Label label = (Label) nodo;
                    label.setTextFill(nuevoColor);
                }
            }
        });
        
        // Manejar clicks en el pane
        cartaPane.setOnMouseClicked(event -> {
            if (modoPuntosActivo) {
                creandoPunto = true;

                // Crear la "previsualización" del punto
                Circle externo = new Circle(0, 0, 12);
                externo.setStroke(Color.GRAY);
                externo.setStrokeWidth(3);
                externo.setFill(Color.TRANSPARENT);

                Circle interno = new Circle(0, 0, 5);
                interno.setFill(Color.GRAY);

                ghostPunto = new Group(externo, interno);
                ghostPunto.setMouseTransparent(true); // para que no interfiera con clics
                cartaPane.getChildren().add(ghostPunto);
                hacerInteractivo(ghostPunto);
                Platform.runLater(() -> {
                    cartaPane.requestFocus();
                    cartaPane.fireEvent(new MouseEvent(MouseEvent.MOUSE_MOVED,
                        cartaPane.getWidth() / 2, cartaPane.getHeight() / 2,
                        cartaPane.getWidth() / 2, cartaPane.getHeight() / 2,
                        MouseButton.NONE, 0, false, false, false, false,
                        false, false, false, false, false, false, null));
                });
            } else if (modoCompasAvanzado) {
                Point2D punto = new Point2D(event.getX(), event.getY());
                if (distanciaCompas < 0) {
                    puntosCompas.add(punto);
                    if (puntosCompas.size() == 2) {
                        distanciaCompas = puntosCompas.get(0).distance(puntosCompas.get(1));
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        Stage alertStage = (Stage) a.getDialogPane().getScene().getWindow();
                        alertStage.getIcons().add(
                            new Image(getClass().getResourceAsStream("/resources/compas.png")));
                        DialogPane dialogPane = a.getDialogPane();
                        dialogPane.getStylesheets().add(getClass().getResource(ThemeManager.getEstiloActual()).toExternalForm());
                        dialogPane.getStyleClass().add(" ");
                        a.setHeaderText("Distancia guardada: " + String.format("%.2f", distanciaCompas));
                        a.showAndWait();
                        if (lineaTemporalCompas != null) {
                            cartaPane.getChildren().remove(lineaTemporalCompas);
                            lineaTemporalCompas = null;
                        }
                    }
                } else {
                    TextInputDialog dialog = new TextInputDialog("0");
                    dialog.setTitle("Elegir ángulo");
                    dialog.setHeaderText("Introduce el ángulo en grados para la nueva línea:");
                    Optional<String> res = dialog.showAndWait();

                    res.ifPresent(text -> {
                        try {
                            double angulo = Math.toRadians(Double.parseDouble(text));
                            double dx = distanciaCompas * Math.cos(angulo);
                            double dy = -distanciaCompas * Math.sin(angulo); // Invertido por coordenadas de pantalla

                            Point2D destino = new Point2D(punto.getX() + dx, punto.getY() + dy);

                            if (destino.getX() < 0 || destino.getY() < 0 || destino.getX() > cartaPane.getWidth() || destino.getY() > cartaPane.getHeight()) {
                                Alert fuera = new Alert(Alert.AlertType.ERROR);
                                Stage alertStage = (Stage) fuera.getDialogPane().getScene().getWindow();
                                alertStage.getIcons().add(
                                        new Image(getClass().getResourceAsStream("/resources/compas.png")));
                                DialogPane dialogPane = fuera.getDialogPane();
                                dialogPane.getStylesheets().add(getClass().getResource(ThemeManager.getEstiloActual()).toExternalForm());
                                dialogPane.getStyleClass().add(" ");
                                fuera.setHeaderText("La línea se sale del límite del panel");
                                fuera.setContentText("Elige otro punto o reduce el ángulo.");
                                fuera.showAndWait();
                                return;
                            }

                            Line linea = new Line(punto.getX(), punto.getY(), destino.getX(), destino.getY());
                            linea.setStroke(colorLinea);
                            linea.setStrokeWidth(grosorLinea);

                            linea.setOnMouseClicked(e -> {
                                if (modoRatonActivo && e.getButton() == MouseButton.PRIMARY) {
                                    e.consume();
                                    if (!e.isShiftDown()) limpiarSeleccion();
                                    seleccionarMarca(linea);
                                }
                            });

                            cartaPane.getChildren().add(linea);

                        } catch (NumberFormatException ex) {
                            Alert error = new Alert(Alert.AlertType.ERROR);
                            Stage alertStage = (Stage) error.getDialogPane().getScene().getWindow();
                            alertStage.getIcons().add(
                                new Image(getClass().getResourceAsStream("/resources/compas.png")));
                            DialogPane dialogPane = error.getDialogPane();
                            dialogPane.getStylesheets().add(getClass().getResource(ThemeManager.getEstiloActual()).toExternalForm());
                            dialogPane.getStyleClass().add(" ");
                            error.setHeaderText("Ángulo inválido");
                            error.setContentText("Debes introducir un número válido.");
                            error.showAndWait();
                        }
                    });
                }
            } else if (modoArcoActivo) {
                if (centroArcos == null) {
                    centroArcos = new Point2D(event.getX(), event.getY());

                    // Crear un arco temporal
                    arcoTemporal = new Arc();
                    arcoTemporal.setCenterX(centroArcos.getX());
                    arcoTemporal.setCenterY(centroArcos.getY());
                    arcoTemporal.setRadiusX(0);
                    arcoTemporal.setRadiusY(0);
                    arcoTemporal.setStartAngle(0);
                    arcoTemporal.setLength(180);
                    arcoTemporal.setType(ArcType.OPEN);
                    arcoTemporal.setStroke(colorLinea);
                    arcoTemporal.setStrokeWidth(grosorLinea);
                    arcoTemporal.setFill(null);

                    cartaPane.getChildren().add(arcoTemporal);

                } else {
                
                    // Crear arco definitivo con las propiedades del temporal
                    Arc arcoFinal = new Arc();
                    arcoFinal.setCenterX(arcoTemporal.getCenterX());
                    arcoFinal.setCenterY(arcoTemporal.getCenterY());
                    arcoFinal.setRadiusX(arcoTemporal.getRadiusX());
                    arcoFinal.setRadiusY(arcoTemporal.getRadiusY());
                    arcoFinal.setStartAngle(arcoTemporal.getStartAngle());
                    arcoFinal.setLength(arcoTemporal.getLength());
                    arcoFinal.setType(ArcType.OPEN);
                    arcoFinal.setStroke(colorLinea);
                    arcoFinal.setStrokeWidth(grosorLinea);
                    arcoFinal.setFill(null);

                    // Hacer seleccionable
                    arcoFinal.setOnMouseClicked(e -> {
                        if (modoRatonActivo && e.getButton() == MouseButton.PRIMARY) {
                            e.consume();
                            if (!e.isShiftDown()) {
                                limpiarSeleccion();
                            }
                            seleccionarMarca(arcoFinal);
                        }
                    });

                    cartaPane.getChildren().remove(arcoTemporal); // eliminar temporal
                    cartaPane.getChildren().add(arcoFinal);       // agregar definitivo
                    arcoTemporal = null;
                    centroArcos = null;
                }
            } else if (modoTextoActivo) {
                TextField textoNew = new TextField();
                textoNew.setLayoutX(event.getX());
                textoNew.setLayoutY(event.getY());

                // Opcional: ancho fijo o automático
                textoNew.setPrefColumnCount(10);

                // Al presionar Enter, el TextField se vuelve Label
                textoNew.setOnAction(e -> {
                    Label etiqueta = new Label(textoNew.getText());
                    etiqueta.setLayoutX(textoNew.getLayoutX());
                    etiqueta.setLayoutY(textoNew.getLayoutY());
                    cartaPane.getChildren().remove(textoNew);
                    cartaPane.getChildren().add(etiqueta);
                });

                cartaPane.getChildren().add(textoNew);
                hacerInteractivo(textoNew);
                textoNew.requestFocus(); // Poner foco directamente
            }
            if (creandoPunto) {
                double x = event.getX();
                double y = event.getY();

                double mapWidth = image_map.getBoundsInParent().getWidth();
                double mapHeight = image_map.getBoundsInParent().getHeight();

                // Solo crear punto si está dentro del mapa
                if (x >= 0 && x <= mapWidth && y >= 0 && y <= mapHeight) {
                    Circle externo = new Circle(x, y, 12);
                    externo.setStroke(Color.BLACK);
                    externo.setStrokeWidth(3);
                    externo.setFill(Color.TRANSPARENT);

                    Circle interno = new Circle(x, y, 5);
                    interno.setFill(Color.BLACK);

                    Group punto = new Group(externo, interno);
                    punto.setCursor(Cursor.HAND);
                    punto.setOnMouseClicked(e -> {
                        if (modoRatonActivo && e.getButton() == MouseButton.PRIMARY) {
                            e.consume();
                            if (!e.isShiftDown()) {
                                limpiarSeleccion();
                            }
                            seleccionarMarca(punto);
                            seleccionarMarca(ghostPunto);
                        }
                    });

                    cartaPane.getChildren().add(punto);
                }
            } else if (creandoLinea) {
                double clickX = event.getX();
                double clickY = event.getY();
                Point2D puntoClic = new Point2D(clickX, clickY);

                Point2D puntoAjustado = buscarPuntoCercano(puntoClic);
                Point2D puntoFinal = (puntoAjustado != null) ? puntoAjustado : puntoClic;
                puntosLinea.add(puntoFinal);

                if (puntosLinea.size() == 1) {
                    // Crear línea provisional
                    lineaTemporal = new Line();
                    lineaTemporal.setStartX(puntoFinal.getX());
                    lineaTemporal.setStartY(puntoFinal.getY());
                    lineaTemporal.setEndX(puntoFinal.getX());
                    lineaTemporal.setEndY(puntoFinal.getY());
                    lineaTemporal.setStroke(colorLinea);
                    lineaTemporal.setStrokeWidth(grosorLinea);
                    cartaPane.getChildren().add(lineaTemporal);
                } else if (puntosLinea.size() == 2) {
                    cartaPane.setCursor(Cursor.CROSSHAIR);

                    lineaTemporal.setEndX(puntoFinal.getX());
                    lineaTemporal.setEndY(puntoFinal.getY());

                    // Crear una línea definitiva con la misma geometría
                    Line lineaFinal = new Line(
                        lineaTemporal.getStartX(), lineaTemporal.getStartY(),
                        lineaTemporal.getEndX(), lineaTemporal.getEndY()
                    );
                    lineaFinal.setStroke(lineaTemporal.getStroke());
                    lineaFinal.setStrokeWidth(lineaTemporal.getStrokeWidth());

                    // Añadir comportamiento de selección
                    lineaFinal.setOnMouseClicked(e -> {
                        if (modoRatonActivo && e.getButton() == MouseButton.PRIMARY) {
                            e.consume();
                            if (!e.isShiftDown()) {
                                limpiarSeleccion();
                            }
                            seleccionarMarca(lineaFinal);
                        }
                    });

                    // Añadir al canvas
                    cartaPane.getChildren().add(lineaFinal);

                    // Limpiar temporal
                    cartaPane.getChildren().remove(lineaTemporal);
                    lineaTemporal = null;
                    puntosLinea.clear();
                }
            } else if (creandoArco) {
                if (centroArco == null) {
                    // Primer clic: seleccionar centro del arco
                    centroArco = new Point2D(event.getX(), event.getY());

                    // Marca visual opcional del centro
                    Circle marcaCentro = new Circle(centroArco.getX(), centroArco.getY(), 4, Color.RED);
                    cartaPane.getChildren().add(marcaCentro);
                    
                    // PUEDE CAMBIARSE
                    marcaCentro.setOnMouseClicked(e -> {
                        if (modoRatonActivo && e.getButton() == MouseButton.PRIMARY) {
                            e.consume();
                            if (!e.isShiftDown()) {
                                limpiarSeleccion();
                            }
                            seleccionarMarca(marcaCentro);
                        }
                    });

                } else {
                    // Solicita al usuario un radio manualmente
                    TextInputDialog dialog = new TextInputDialog("50");
                    dialog.setTitle("Definir radio");
                    dialog.setHeaderText("Introduce el radio del arco:");
                    dialog.setContentText("Radio:");

                    Optional<String> resultado = dialog.showAndWait();
                    if (resultado.isPresent()) {
                        try {
                            radioArco = Double.parseDouble(resultado.get());
                            drawArc(centroArco, radioArco);
                        } catch (NumberFormatException ex) {
                            Alert alerta = new Alert(Alert.AlertType.ERROR, "Radio inválido. Introduce un número válido.");
                            alerta.setGraphic(null);
                            Stage alertStage = (Stage) alerta.getDialogPane().getScene().getWindow();
                            alertStage.getIcons().add(
                                new Image(getClass().getResourceAsStream("/resources/compas.png")));
                            DialogPane dialogPane = alerta.getDialogPane();
                            dialogPane.getStylesheets().add(getClass().getResource(ThemeManager.getEstiloActual()).toExternalForm());
                            dialogPane.getStyleClass().add(" ");
                            alerta.showAndWait();
                        }
                    }

                    // Resetear estado
                    creandoArco = false;
                    centroArco = null;
                    radioArco = 0;
                    cartaPane.setCursor(Cursor.DEFAULT);
                }
            } else if (creandoTexto) {
                // Pedir texto con diálogo
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Anotar Texto");
                dialog.setHeaderText("Introduce el texto a mostrar");
                dialog.setContentText("Texto:");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent() && !result.get().trim().isEmpty()) {
                    String texto = result.get().trim();

                    Label label = new Label(texto);
                    label.setLayoutX(event.getX());
                    label.setLayoutY(event.getY());
                    label.setStyle("-fx-font-size: 14px; -fx-text-fill: black;"); // estilo básico, personalízalo
                    
                    label.setOnMouseClicked(e -> {
                        if (modoRatonActivo && e.getButton() == MouseButton.PRIMARY) {
                            e.consume();
                            if (!e.isShiftDown()) {
                                limpiarSeleccion();
                            }
                            seleccionarMarca(label);
                        }
                    });
                    cartaPane.getChildren().add(label);
                }

                // Salir del modo texto
                creandoTexto = false;
                cartaPane.setCursor(Cursor.DEFAULT);
            } else if (midiendoDistancia) {
                Point2D punto = new Point2D(event.getX(), event.getY());
                puntosMedicion.add(punto);
                if (puntosMedicion.size() == 2) {
                    Point2D p1 = puntosMedicion.get(0);
                    Point2D p2 = puntosMedicion.get(1);

                    // Dibujar línea
                    if (lineaMedida != null) cartaPane.getChildren().remove(lineaMedida);
                    lineaMedida = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                    lineaMedida.setStroke(Color.RED);
                    cartaPane.getChildren().add(lineaMedida);

                    // Calcular distancia
                    double distancia = p1.distance(p2);
                    double subdivisiones = distancia / TAMANIO_SUBDIVISION;

                    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                    alerta.setGraphic(null);
                    alerta.setTitle("Medición de distancia");
                    alerta.setHeaderText("Resultado");
                    alerta.setContentText("Distancia medida: " + String.format("%.2f", subdivisiones) + " subdivisiones.");
                    Stage alertStage = (Stage) alerta.getDialogPane().getScene().getWindow();
                    alertStage.getIcons().add(
                        new Image(getClass().getResourceAsStream("/resources/compas.png")));
                    alerta.showAndWait();

                    puntosMedicion.clear();
                    midiendoDistancia = false;
                    cartaPane.setCursor(Cursor.DEFAULT);
                    //medirButton.setSelected(false);
                }
                return;
            } else if (compasActivo) {
                activarCompas(new Point2D(event.getX(), event.getY()));
                compasActivo = false;
                cartaPane.setCursor(Cursor.DEFAULT);
            }
            if (event.getButton() == MouseButton.PRIMARY && !event.isConsumed()) {
                limpiarSeleccion();
            }
        });
        
        cartaPane.setOnMouseEntered(e -> {
            if (modoPuntosActivo && ghostPunto != null) {
                ghostPunto.setVisible(true);
            }
        });

        cartaPane.setOnMouseExited(e -> {
            if (ghostPunto != null) {
                ghostPunto.setVisible(false);
            }
        });
        
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> {
            zoom(newVal.doubleValue());

            if (newVal.doubleValue() == zoom_slider.getMin()) {
                // Centrar el mapa cuando se aleja al máximo
                Platform.runLater(() -> {
                    map_scrollpane.setHvalue(0.5);
                    map_scrollpane.setVvalue(0.5);
                });
            }
        });   
    }

    @FXML
    private void showPosition(MouseEvent event) {
        // AQUI ESTABA LO DEL SHOW POSITION
    }

    private void closeApp(ActionEvent event) {
        ((Stage) zoom_slider.getScene().getWindow()).close();
    }

    private void about(ActionEvent event) {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        mensaje.setGraphic(null);
        Stage alertStage = (Stage) mensaje.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/resources/compas.png")));
        DialogPane dialogPane = mensaje.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource(ThemeManager.getEstiloActual()).toExternalForm());
        dialogPane.getStyleClass().add(" ");
        // Acceder al Stage del Dialog y cambiar el icono
        Stage dialogStage = (Stage) mensaje.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("IPC - 2025");
        mensaje.showAndWait();
    }

    @FXML
    private void addPoi(MouseEvent event) {

        if (event.isControlDown()) {
            Dialog<Poi> poiDialog = new Dialog<>();
            poiDialog.setTitle("Nuevo POI");
            poiDialog.setHeaderText("Introduce un nuevo POI");
            // Acceder al Stage del Dialog y cambiar el icono
            Stage dialogStage = (Stage) poiDialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));

            ButtonType okButton = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
            poiDialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

            TextField nameField = new TextField();
            nameField.setPromptText("Nombre del POI");

            TextArea descArea = new TextArea();
            descArea.setPromptText("Descripción...");
            descArea.setWrapText(true);
            descArea.setPrefRowCount(5);

            VBox vbox = new VBox(10, new Label("Nombre:"), nameField, new Label("Descripción:"), descArea);
            poiDialog.getDialogPane().setContent(vbox);

            poiDialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButton) {
                    return new Poi(nameField.getText().trim(), descArea.getText().trim(), 0, 0);
                }
                return null;
            });
            Optional<Poi> result = poiDialog.showAndWait();

            if(result.isPresent()) {
                Point2D localPoint = zoomGroup.sceneToLocal(event.getSceneX(), event.getSceneY());
                Poi poi=result.get();
                poi.setPosition(localPoint);
                //map_listview.getItems().add(poi);
            }
        }
    }

    @FXML
    private void crearPunto(ActionEvent event) {
        creandoPunto = true;
        
        if (ghostPunto != null) {
            cartaPane.getChildren().remove(ghostPunto);
        }

        // Crear la "previsualización" del punto
        Circle externo = new Circle(0, 0, 12);
        externo.setStroke(Color.GRAY);
        externo.setStrokeWidth(3);
        externo.setFill(Color.TRANSPARENT);

        Circle interno = new Circle(0, 0, 5);
        interno.setFill(Color.GRAY);

        ghostPunto = new Group(externo, interno);
        ghostPunto.setMouseTransparent(true); // para que no interfiera con clics
        cartaPane.getChildren().add(ghostPunto);
    }

    @FXML
    private void crearLinea(ActionEvent event) {
        creandoLinea = true;
        puntosLinea.clear();
        if (lineaTemporal != null) {
            cartaPane.getChildren().remove(lineaTemporal);
            lineaTemporal = null;
        }
        cartaPane.setCursor(Cursor.CROSSHAIR);
    }

    @FXML
    private void crearArco(ActionEvent event) {
        creandoArco = true;
        centroArco = null;
        radioArco = 0;
        cartaPane.setCursor(Cursor.CROSSHAIR);
    }

    @FXML
    private void crearTexto(ActionEvent event) {
        creandoTexto = true;
        cartaPane.setCursor(Cursor.TEXT);
    }

    @FXML
    private void cambiarColor(ActionEvent event) {
        /*
        if (marcasSeleccionadas.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "No hay ninguna marca seleccionada");
            alerta.showAndWait();
            return;
        }
                
        // Preguntar si el usuario quiere cambiar el color (opcionalidad explícita)
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas cambiar el color de las marcas seleccionadas?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isEmpty() || respuesta.get() != ButtonType.YES) {
            return; // El usuario no desea cambiar el color
        }

        // Mostrar el selector de color
        ColorPicker colorPicker = new ColorPicker();
        Dialog<Color> dialog = new Dialog<>();
        dialog.setTitle("Seleccionar color");
        dialog.getDialogPane().setContent(colorPicker);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return colorPicker.getValue();
            }
            return null;
        });

        Optional<Color> resultado = dialog.showAndWait();

        resultado.ifPresent(color -> {
            for (Node marca : marcasSeleccionadas) {
                if (marca instanceof ImageView) {
                    Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Cambiar color de puntos requiere usar círculos en vez de imágenes.");
                    alerta.showAndWait();
                } else if (marca instanceof Line) {
                    ((Line) marca).setStroke(color);
                } else if (marca instanceof Arc) {
                    ((Arc) marca).setStroke(color);
                } else if (marca instanceof Label) {
                    ((Label) marca).setTextFill(color);
                } else if (marca instanceof Group) {
                    // Si la marca es un Group (por ejemplo un punto compuesto),
                    // puedes decidir cómo cambiar el color, por ejemplo cambiar el color del círculo interno:
                    Group grupo = (Group) marca;
                    for (Node child : grupo.getChildren()) {
                        if (child instanceof Circle) {
                            ((Circle) child).setFill(color);
                            ((Circle) child).setStroke(color);
                        }
                    }
                }
            }
        });
        */
        Color nuevoColor = coloresButton.getValue();
        for (Node nodo : marcasSeleccionadas) {
            if (nodo instanceof Shape) {
                Shape shape = (Shape) nodo;
                shape.setStroke(nuevoColor);
                if (!(shape instanceof Line)) {
                    shape.setFill(nuevoColor);
                }
                if (shape instanceof Arc) {
                    shape.setFill(null); // Para los arcos, mantener transparencia
                }
            } else if (nodo instanceof Group) {
                Group grupo = (Group) nodo;
                for (Node child : grupo.getChildrenUnmodifiable()) {
                    if (child instanceof Circle) {
                        Circle c = (Circle) child;
                        if (c.getRadius() > 10) {
                            c.setStroke(nuevoColor);      // borde del externo
                            c.setFill(Color.TRANSPARENT); // externo sin relleno
                        } else {
                            c.setFill(nuevoColor);        // interno con relleno
                            c.setStroke(nuevoColor);      // opcional
                        }
                    }
                }
            } else if (nodo instanceof Label) {
                Label label = (Label) nodo;
                label.setTextFill(nuevoColor);
            }
        }
    }

    @FXML
    private void eliminarMarca(ActionEvent event) {
        if (marcasSeleccionadas.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "No hay ninguna marca seleccionada para eliminar.");
            alerta.setGraphic(null);
            Stage alertStage = (Stage) alerta.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/resources/compas.png")));
            DialogPane dialogPane = alerta.getDialogPane();
            dialogPane.getStylesheets().add(getClass().getResource(ThemeManager.getEstiloActual()).toExternalForm());
            dialogPane.getStyleClass().add(" ");
            alerta.showAndWait();
            return;
        }

        // Confirmar la eliminación con el usuario (opcional)
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, 
                "¿Deseas eliminar las marcas seleccionadas?", ButtonType.YES, ButtonType.NO);
        confirmacion.setGraphic(null);
        Stage alertStage = (Stage) confirmacion.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(
                new Image(getClass().getResourceAsStream("/resources/compas.png")));
        DialogPane dialogPane = confirmacion.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource(ThemeManager.getEstiloActual()).toExternalForm());
        dialogPane.getStyleClass().add(" ");
        Optional<ButtonType> respuesta = confirmacion.showAndWait();

        if (respuesta.isPresent() && respuesta.get() == ButtonType.YES) {
            for (Node marca : marcasSeleccionadas) {
                cartaPane.getChildren().remove(marca);
            }
            marcasSeleccionadas.clear();
        }
    }

    private void limpiarCarta(ActionEvent event) {
        // Limpiamos la selección primero
        limpiarSeleccion();
        Node mapaBase = null;
        for (Node nodo : cartaPane.getChildren()) {
            if ("mapa".equals(nodo.getId())) {
                mapaBase = nodo;
                break;
            }
        }

        // Creamos una lista temporal para eliminar nodos no deseados sin modificar la lista durante la iteración
        List<Node> nodosAEliminar = new ArrayList<>();

        for (Node nodo : cartaPane.getChildren()) {
            if (nodo != mapaBase) {
                // Solo eliminar si es Group (punto), Arc, Line o Label
                if (nodo instanceof Group || nodo instanceof Arc || nodo instanceof Line || nodo instanceof Label || nodo instanceof Circle) {
                    nodosAEliminar.add(nodo);
                }
            }
        }

        cartaPane.getChildren().removeAll(nodosAEliminar);
    }

    @FXML
    private void crearTransportador(ActionEvent event) {
        transportadorActivo = !transportadorActivo;  // Alternar estado
        transportador.setVisible(transportadorActivo);

        if (transportadorActivo) {
            cartaPane.setCursor(Cursor.OPEN_HAND);
        } else {
            cartaPane.setCursor(Cursor.DEFAULT);
        }
    }
    
    @FXML
    private void crearRegla(ActionEvent event) {
        reglaActiva = !reglaActiva;
        regla.setVisible(reglaActiva);
    }

    private void medirDistancia(ActionEvent event) {
        midiendoDistancia = true;
        puntosMedicion.clear();
        if (lineaMedida != null) {
            cartaPane.getChildren().remove(lineaMedida);
            lineaMedida = null;
        }

        if (midiendoDistancia) {
            cartaPane.setCursor(Cursor.CROSSHAIR);
        } else {
            cartaPane.setCursor(Cursor.DEFAULT);
        }
    }

    @FXML
    private void crearCompas(ActionEvent event) {
        compasActivo = true;
        cartaPane.setCursor(compasActivo ? Cursor.CROSSHAIR : Cursor.DEFAULT);
    }

    @FXML
    private void activarModoCompás(ActionEvent event) {
        limpiar();
        modoCompasAvanzado = true;
        puntosCompas.clear();
        distanciaCompas = -1;
        cartaPane.setCursor(Cursor.CROSSHAIR);
    }

    @FXML
    private void irEstadisticas(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("estadisticas.fxml"));
            Parent root = loader.load();

            EstadisticasController controller = loader.getController();
            controller.setDatos(nav, user);

            Stage stage = new Stage();
            stage.setTitle("Estadísticas");
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.getIcons().add(new Image(ProblemaController.class.getResourceAsStream("/resources/compas.png")));
            stage.initModality(Modality.APPLICATION_MODAL); // <- bloquea la ventana actual
            stage.initOwner(cartaPane.getScene().getWindow()); // <- ventana principal como owner
            stage.showAndWait(); // <- espera hasta que se cierre

        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    // Clase auxiliar para almacenar la diferencia al arrastrar
    private static class Delta {
        double x, y;
    }
    
}

    