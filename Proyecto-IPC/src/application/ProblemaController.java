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
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Shape;


public class ProblemaController implements Initializable {
    
    
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
    private ObservableList<Poi> data;
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;

    @FXML
    private ListView<Poi> map_listview;
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
    @FXML
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
            if (e.getButton() == MouseButton.SECONDARY) {
                e.consume();
                if (!e.isShiftDown()) {
                    limpiarSeleccion();
                }
                seleccionarMarca(arc);
            }
        });

        cartaPane.getChildren().add(arc);
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
    
    @FXML
    private void activarModoRaton() {
        modoTextoActivo = false;
        modoPuntosActivo = false;
        modoLineaActivo = false;
        modoArcoActivo = false;
        modoGomaActivo = false;
        modoPintarActivo = false;
        centroArcos = null;
        primerPunto = null;
    }
    
    @FXML
    private void activarModoPunto() {
        modoTextoActivo = false;
        modoPuntosActivo = true;
        modoLineaActivo = false;
        modoArcoActivo = false;
        modoGomaActivo = false;
        modoPintarActivo = false;
        centroArcos = null;
        primerPunto = null;
    }

    @FXML
    private void activarModoLinea() {
        modoTextoActivo = false;
        modoPuntosActivo = false;
        modoLineaActivo = true;
        modoArcoActivo = false;
        modoGomaActivo = false;
        modoPintarActivo = false;
        centroArcos = null;
        primerPunto = null;
    }
    
    @FXML
    private void activarModoArco() {
        modoTextoActivo = false;
        modoPuntosActivo = false;
        modoLineaActivo = false;
        modoArcoActivo = true;
        modoGomaActivo = false;
        modoPintarActivo = false;
        centroArcos = null;
        primerPunto = null;
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
        transportadorActivo = !transportadorActivo;
        transportador.setVisible(transportadorActivo);
    }
    @FXML
    private void activarRegla() {
        reglaActiva = !reglaActiva;
        regla.setVisible(reglaActiva);
    }
    
    @FXML
    private void activarModoGoma() {
        modoGomaActivo = true;
        modoPuntosActivo = false;
        modoLineaActivo = false;
        modoArcoActivo = false;
        modoTextoActivo = false;
        modoPintarActivo = false;
        centroArcos = null;
        primerPunto = null;
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
            colorSeleccionado = coloresButton.getValue();
            if (modoGomaActivo) {
                cartaPane.getChildren().remove(nodo);
                event.consume();
            } else if (modoPintarActivo) {
                if (nodo instanceof Shape) {
                    ((Shape) nodo).setStroke(colorSeleccionado);
                    if (!(nodo instanceof Line)) {
                        ((Shape) nodo).setFill(colorSeleccionado);
                    }
                    if (nodo instanceof Arc) {
                        ((Shape) nodo).setFill(null);
                    }
                } else if (nodo instanceof Label) {
                    ((Label) nodo).setTextFill(colorSeleccionado);
                }
                event.consume();
            }
        });
    }
    
    @FXML
    private void limpiarCarta() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText("¿Estás seguro de que quieres limpiar la carta entera?");
        alerta.setContentText("Se eliminarán todo lo que hayas escrito hasta ahora.");
        
        //Esperar la respuesta del usuario
        Optional<ButtonType> resultado = alerta.showAndWait();
        if(resultado.isPresent() && resultado.get() == ButtonType.OK) {
            cartaPane.getChildren().removeIf(nodo -> nodo != image_map);
        }
    }
    
    private void initData() {
        data = map_listview.getItems();
        data.add(new Poi("1F", "Edificion del DSIC", 275, 250));
        data.add( new Poi("Agora", "Agora", 575, 350));
        data.add( new Poi("Pista", "Pista de atletismo y campo de futbol", 950, 350));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initData();
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
            offsetX = event.getSceneX() - transportador.getLayoutX();
            offsetY = event.getSceneY() - transportador.getLayoutY();
        });
        transportador.setOnMouseDragged(event -> {
            double newX = event.getSceneX() - offsetX;
            double newY = event.getSceneY() - offsetY;

            double anchoPane = cartaPane.getWidth();
            double altoPane = cartaPane.getHeight();

            double anchoBoton = transportador.getBoundsInParent().getWidth();
            double altoBoton = transportador.getBoundsInParent().getHeight();

            if (newX >= 0 && newX <= anchoPane - anchoBoton) {
                transportador.setLayoutX(newX);
            }
            if (newY >= 0 && newY <= altoPane - altoBoton) {
                transportador.setLayoutY(newY);
            }
        });
    
        regla.setVisible(false); // Oculta al principio
        regla.setOnMousePressed(e -> {

                offsetX = e.getSceneX() - regla.getLayoutX();
                offsetY = e.getSceneY() - regla.getLayoutY();
        });
        regla.setOnMouseDragged(e -> {
                regla.setLayoutX(e.getSceneX() - offsetX);
                regla.setLayoutY(e.getSceneY() - offsetY);
        });

        
        // Escucha cuando el botón tenga Scene (garantiza que la vista ya está cargada)
        toggleThemeButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                // Aplica estilo claro al inicio
                newScene.getStylesheets().add(getClass().getResource("/application/problemas.css").toExternalForm());
                toggleThemeButton.setSelected(false); // No seleccionado = modo claro
                toggleThemeButton.setText("Modo oscuro");

                // Ahora sí, configurar acción del botón
                toggleThemeButton.setOnAction(event -> {
                    newScene.getStylesheets().clear();
                    if (toggleThemeButton.isSelected()) {
                        // Modo oscuro
                        newScene.getStylesheets().add(getClass().getResource("/application/modoNocturno.css").toExternalForm());
                        toggleThemeButton.setText("Modo claro");
                    } else {
                        // Modo claro
                        newScene.getStylesheets().add(getClass().getResource("/application/problemas.css").toExternalForm());
                        toggleThemeButton.setText("Modo oscuro");
                    }
                });
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
        
        // Manejar clicks en el pane
        cartaPane.setOnMouseClicked(event -> {
            if (modoPuntosActivo) {
                Circle punto = new Circle(event.getX(), event.getY(), 5);
                cartaPane.getChildren().add(punto);
                hacerInteractivo(punto);
            } else if (modoLineaActivo) {
                if (primerPunto == null) {
                    primerPunto = new Point2D(event.getX(), event.getY());
                } else {
                    Line linea = new Line(
                    primerPunto.getX(), primerPunto.getY(),
                    event.getX(), event.getY()
                    );
                    linea.setStroke(Color.BLUE);
                    linea.setStrokeWidth(2);
                    cartaPane.getChildren().add(linea);
                    hacerInteractivo(linea);
                    primerPunto = null;
                }
            } else if (modoArcoActivo) {
                if (centroArcos == null) {
                // Primer clic: centro del arco
                    centroArcos = new Point2D(event.getX(), event.getY());
                } else {
                    // Segundo clic: punto sobre la circunferencia
                    Point2D puntoCircunferencia = new Point2D(event.getX(), event.getY());

                    double radio = centroArcos.distance(puntoCircunferencia);

                    // Calcular el ángulo desde el centro hacia el punto de la circunferencia
                    double deltaX = puntoCircunferencia.getX() - centroArcos.getX();
                    double deltaY = puntoCircunferencia.getY() - centroArcos.getY();
                    double angle = Math.toDegrees(Math.atan2(-deltaY, deltaX)); // Y invertido

                    // Ajustar para que el arco esté orientado hacia el segundo punto
                    double startAngle = angle - 90;

                    Arc arco = new Arc();
                    arco.setCenterX(centroArcos.getX());
                    arco.setCenterY(centroArcos.getY());
                    arco.setRadiusX(radio);
                    arco.setRadiusY(radio);
                    arco.setStartAngle(startAngle);
                    arco.setLength(180);  // Semicírculo
                    arco.setType(ArcType.OPEN);
                    arco.setStroke(Color.ORANGE);
                    arco.setStrokeWidth(2);
                    arco.setFill(null);

                    cartaPane.getChildren().add(arco);
                    hacerInteractivo(arco);
                    
                    // Reset
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

                // Crear el punto final (idéntico al ghost)
                Circle externo = new Circle(x, y, 12);
                externo.setStroke(Color.BLACK);
                externo.setStrokeWidth(3);
                externo.setFill(Color.TRANSPARENT);

                Circle interno = new Circle(x, y, 5);
                interno.setFill(Color.BLACK);

                Group punto = new Group(externo, interno);
                punto.setCursor(Cursor.HAND);
                punto.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        e.consume();
                        if (!e.isShiftDown()) {
                            limpiarSeleccion();
                        }
                        seleccionarMarca(punto);
                    }
                });

                cartaPane.getChildren().add(punto);

                // Limpiar estado
                creandoPunto = false;
                cartaPane.setCursor(Cursor.DEFAULT);
                if (ghostPunto != null) {
                    cartaPane.getChildren().remove(ghostPunto);
                    ghostPunto = null;
                }
            } else if (creandoLinea) {
                double clickX = event.getX();
                double clickY = event.getY();
                Point2D puntoClic = new Point2D(clickX, clickY);

                // Ajuste al punto más cercano si hay
                Point2D puntoAjustado = buscarPuntoCercano(puntoClic);
                Point2D puntoFinal = (puntoAjustado != null) ? puntoAjustado : puntoClic;
                puntosLinea.add(puntoFinal);

                if (puntosLinea.size() == 1) {
                    // Primer punto: crear línea provisional
                    lineaTemporal = new Line();
                    lineaTemporal.setStartX(puntoFinal.getX());
                    lineaTemporal.setStartY(puntoFinal.getY());
                    lineaTemporal.setEndX(puntoFinal.getX());
                    lineaTemporal.setEndY(puntoFinal.getY());
                    lineaTemporal.setStroke(colorLinea);
                    lineaTemporal.setStrokeWidth(grosorLinea);
                    cartaPane.getChildren().add(lineaTemporal);
                } else if (puntosLinea.size() == 2) {
                    // Segundo punto: fijar la línea
                    cartaPane.setCursor(Cursor.DEFAULT);
                    creandoLinea = false;

                    // Usar la línea temporal y fijar su posición final
                    lineaTemporal.setEndX(puntoFinal.getX());
                    lineaTemporal.setEndY(puntoFinal.getY());

                    // Agregar comportamiento de selección
                    lineaTemporal.setOnMouseClicked(e -> {
                        if (e.getButton() == MouseButton.SECONDARY) {
                            e.consume();
                            if (!e.isShiftDown()) {
                                limpiarSeleccion();
                            }
                            seleccionarMarca(lineaTemporal);
                        }
                    });

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
                        if (e.getButton() == MouseButton.SECONDARY) {
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
                        if (e.getButton() == MouseButton.SECONDARY) {
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
                    alerta.setTitle("Medición de distancia");
                    alerta.setHeaderText("Resultado");
                    alerta.setContentText("Distancia medida: " + String.format("%.2f", subdivisiones) + " subdivisiones.");
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
            if (event.getButton() == MouseButton.SECONDARY && !event.isConsumed()) {
                limpiarSeleccion();
            }
        });
        
        cartaPane.setOnMouseMoved(event -> {
            if (creandoLinea && puntosLinea.size() == 1 && lineaTemporal != null) {
                lineaTemporal.setEndX(event.getX());
                lineaTemporal.setEndY(event.getY());
            } else if (creandoPunto && ghostPunto != null) {
                ghostPunto.setLayoutX(event.getX());
                ghostPunto.setLayoutY(event.getY());
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
        
        /*transportador.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Point2D clicLocal = new Point2D(event.getX(), event.getY());
                dragDelta.x = event.getX();
                dragDelta.y = event.getY();
                event.consume();
            }
        });

        transportador.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // Convertir la posición del puntero a coordenadas del padre del transportador
                Point2D parentCoords = transportador.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());

                // Restar el delta para mantener el desplazamiento natural
                transportador.setLayoutX(parentCoords.getX() - dragDelta.x);
                transportador.setLayoutY(parentCoords.getY() - dragDelta.y);

                event.consume();
            }
        });*/
        
        /*// Crear la imagen de la regla
        regla = new ImageView();
        Image imgRegla = new Image(getClass().getResourceAsStream("/resources/regla.png")); // asegúrate de que exista
        regla.setImage(imgRegla);
        regla.setFitWidth(1000);
        regla.setFitHeight(200);
        egla.setVisible(reglaActiva);
        zoomGroup.getChildren().add(regla);

        // Hacer que la regla se pueda arrastrar
        regla.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                dragDelta.x = event.getX();
                dragDelta.y = event.getY();
                event.consume();
            }
        });

        regla.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Point2D parentCoords = regla.getParent().sceneToLocal(event.getSceneX(), event.getSceneY());
                regla.setLayoutX(parentCoords.getX() - dragDelta.x);
                regla.setLayoutY(parentCoords.getY() - dragDelta.y);
                event.consume();
            }
        });*/
        
    }

    @FXML
    private void showPosition(MouseEvent event) {
        // AQUI ESTABA LO DEL SHOW POSITION
    }

    private void closeApp(ActionEvent event) {
        ((Stage) zoom_slider.getScene().getWindow()).close();
    }

    @FXML
    private void about(ActionEvent event) {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
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
                map_listview.getItems().add(poi);
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
    }

    @FXML
    private void eliminarMarca(ActionEvent event) {
        if (marcasSeleccionadas.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "No hay ninguna marca seleccionada para eliminar.");
            alerta.showAndWait();
            return;
        }

        // Confirmar la eliminación con el usuario (opcional)
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, 
                "¿Deseas eliminar las marcas seleccionadas?", ButtonType.YES, ButtonType.NO);
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
            // Inicializar posición si quieres (por ejemplo, centro)
            transportador.setLayoutX(100);
            transportador.setLayoutY(100);
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

    @FXML
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
    private void modificarPerfilAction(ActionEvent event) {
    }



    @FXML
    private void activarModoCompás(ActionEvent event) {
    }




    
    // Clase auxiliar para almacenar la diferencia al arrastrar
    private static class Delta {
        double x, y;
    }
    
}

    