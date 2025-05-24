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
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 *
 * @author jsoler
 */
public class ProblemaController implements Initializable {
    
    private Node marcaSeleccionada = null;
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
    private ImageCursor cursorPunto;    

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

        cartaPane.getChildren().add(arc);
    }
    
    private void seleccionarMarca(Node nodo) {
        // Opcional: quita efecto de selección anterior
        if (marcaSeleccionada != null) {
            marcaSeleccionada.setEffect(null);
        }
        marcaSeleccionada = nodo;

        // Efecto visual simple para la selección (por ejemplo un resplandor)
        javafx.scene.effect.DropShadow efecto = new javafx.scene.effect.DropShadow();
        efecto.setColor(javafx.scene.paint.Color.YELLOW);
        efecto.setRadius(10);
        marcaSeleccionada.setEffect(efecto);
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

    private void initData() {
        data=map_listview.getItems();
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
        zoom_slider.setMin(0.5);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(1.0);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));

        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
        
        // Cargar el cursor personalizado solo una vez
        Image puntoImg = new Image(getClass().getResource("/resources/punto.jpeg").toExternalForm());
        cursorPunto = new ImageCursor(puntoImg, puntoImg.getWidth() / 2, puntoImg.getHeight() / 2);
        
        // Manejar clicks en el pane
        cartaPane.setOnMouseClicked(event -> {
            if (creandoPunto) {
                Circle punto = new Circle(8, javafx.scene.paint.Color.BLUE); // Radio 8, color azul por defecto
                punto.setLayoutX(event.getX());
                punto.setLayoutY(event.getY());
                punto.setCursor(Cursor.HAND);

                punto.setOnMouseClicked(e -> {
                    e.consume(); // para que no se propague el evento
                    seleccionarMarca(punto); // método que debes tener para manejar la selección
                });

                cartaPane.getChildren().add(punto);

                // Restaurar estado normal
                cartaPane.setCursor(Cursor.DEFAULT);
                creandoPunto = false;
            } else if (creandoLinea) {
                // Guardar punto actual
                Point2D punto = new Point2D(event.getX(), event.getY());
                puntosLinea.add(punto);

                // Cuando hay 2 puntos, trazar línea
                if (puntosLinea.size() == 2) {
                    Point2D p1 = puntosLinea.get(0);
                    Point2D p2 = puntosLinea.get(1);

                    Line linea = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                    linea.setStroke(javafx.scene.paint.Color.BLUE);  // corregido el color para JavaFX
                    linea.setStrokeWidth(grosorLinea);

                    cartaPane.getChildren().add(linea);

                    // Finalizar modo línea
                    creandoLinea = false;
                    puntosLinea.clear();
                    cartaPane.setCursor(Cursor.DEFAULT);
                }
            } else if (creandoArco) {
                if (centroArco == null) {
                    // Guardar el centro del arco
                    centroArco = new Point2D(event.getX(), event.getY());

                    // Aquí puedes pedir el radio al usuario con un diálogo o usar uno predefinido
                    TextInputDialog dialog = new TextInputDialog("50"); // 50 es valor por defecto
                    dialog.setTitle("Radio del Arco");
                    dialog.setHeaderText("Introduce el radio del arco");
                    dialog.setContentText("Radio:");

                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        try {
                            radioArco = Double.parseDouble(result.get());
                        } catch (NumberFormatException e) {
                            radioArco = 50; // fallback si no es válido
                        }
                    } else {
                        // Si cancela el diálogo, salir del modo arco
                        creandoArco = false;
                        cartaPane.setCursor(Cursor.DEFAULT);
                        centroArco = null;
                        return;
                    }

                    // Una vez definido el radio, dibujamos el arco
                    drawArc(centroArco, radioArco);

                    // Salir del modo arco
                    creandoArco = false;
                    cartaPane.setCursor(Cursor.DEFAULT);
                    centroArco = null;
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

                    cartaPane.getChildren().add(label);
                }

                // Salir del modo texto
                creandoTexto = false;
                cartaPane.setCursor(Cursor.DEFAULT);
            }
        });
        
        punto.setOnAction(e -> {
            crearPunto(null); // o la acción que defina el modo de crear punto
        });
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
        cartaPane.setCursor(cursorPunto);
    }

    @FXML
    private void crearLinea(ActionEvent event) {
        creandoLinea = true;
        puntosLinea.clear();
        cartaPane.setCursor(Cursor.CROSSHAIR); // cursor de precisión
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
        if (marcaSeleccionada == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "No hay ninguna marca seleccionada");
            alerta.showAndWait();
            return;
        }

        // Mostrar un selector de color
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
            // Cambiar color según tipo nodo
            if (marcaSeleccionada instanceof ImageView) {
                // Para puntos: puedes aplicar un efecto de color o cambiar la imagen si tienes versiones tintadas
                // Aquí un filtro simple (no es ideal para cambiar imagen, pero es una idea)
                marcaSeleccionada.setEffect(new javafx.scene.effect.ColorAdjust(0, 0, 0, 0));
                // Para cambiar el color real necesitarías una imagen diferente o crear un círculo en vez de imagen
                // Mejor usar Circle para puntos si quieres cambiar color
                Alert alerta = new Alert(Alert.AlertType.INFORMATION, "Cambiar color de puntos requiere usar círculos en vez de imagen.");
                alerta.showAndWait();
            } else if (marcaSeleccionada instanceof Line) {
                ((Line) marcaSeleccionada).setStroke(color);
            } else if (marcaSeleccionada instanceof Arc) {
                ((Arc) marcaSeleccionada).setStroke(color);
            } else if (marcaSeleccionada instanceof Label) {
                ((Label) marcaSeleccionada).setTextFill(color);
            }
        });
    }


}
