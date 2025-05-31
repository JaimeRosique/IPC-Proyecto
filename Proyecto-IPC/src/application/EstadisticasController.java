package application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;
import javafx.scene.Scene; // Asegúrate de que esta importación sea correcta

import model.Session; // Asegúrate de que la clase Session esté en tu paquete model
import model.User;    // Asegúrate de que la clase User esté en tu paquete model
import model.Navigation; // Asegúrate de que la clase Navigation esté en tu paquete model

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class EstadisticasController implements Initializable {

    @FXML
    private VBox rootPane; // Asegúrate de que tu VBox principal en el FXML tiene fx:id="rootPane"

    @FXML
    private Label problemasRealizados;
    @FXML
    private Label problemasAcertados;
    @FXML
    private Label problemasFallados;
    @FXML
    private Label media;
    @FXML
    private Button moreInfoButton; // Si tienes este botón
    @FXML
    private DatePicker filtroFechaCombo; // Asegúrate de que tu DatePicker tiene fx:id="filtroFechaCombo"
    @FXML
    private PieChart dataPieChart; // Asegúrate de que tu PieChart tiene fx:id="dataPieChart"

    private List<Session> allSessions; // Almacenará todas las sesiones del usuario
    private Map<LocalDate, List<Session>> sessionsByDate; // Mapa para acceso rápido por fecha

    private Navigation nav; // Si necesitas esto para navegar
    private User user;     // Usuario actual

    // Método llamado automáticamente cuando se carga el FXML
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar el DatePicker para que, al cambiar de fecha, actualice los datos
        filtroFechaCombo.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                updateStatisticsForDate(newDate);
            } else {
                // Si se deselecciona una fecha, podrías mostrar estadísticas totales o limpiar
                displayTotalStatistics();
            }
        });
        
        // Puedes establecer una fecha predeterminada si lo deseas, por ejemplo, la fecha actual
        // filtroFechaCombo.setValue(LocalDate.now());
    }

    // Este método se llama desde tu clase principal (Main o similar) para pasar los datos
    public void setDatos(Navigation n, User u) {
        nav = n;
        user = u;
        allSessions = user.getSessions(); // Obtener todas las sesiones del usuario

        // Mapear todas las sesiones por fecha una sola vez
        sessionsByDate = allSessions.stream()
                .collect(Collectors.groupingBy(s -> s.getTimeStamp().toLocalDate()));

        // Establecer las fechas disponibles en el DatePicker si solo quieres fechas con sesiones
        // Esto es opcional, pero podría ser útil para guiar al usuario
        // filtroFechaCombo.setDayCellFactory(picker -> new DateCell() {
        //     @Override
        //     public void updateItem(LocalDate date, boolean empty) {
        //         super.updateItem(date, empty);
        //         setDisable(empty || !sessionsByDate.containsKey(date));
        //     }
        // });

        // Establecer la fecha actual o la última fecha con sesiones por defecto en el DatePicker
        // Y actualizar las estadísticas para esa fecha
        if (!allSessions.isEmpty()) {
            LocalDate initialDate = sessionsByDate.keySet().stream().max(LocalDate::compareTo).orElse(LocalDate.now());
            filtroFechaCombo.setValue(initialDate); // Esto disparará el Listener y llamará a updateStatisticsForDate
        } else {
            // Si no hay sesiones, mostrar 0 en todo
            displayTotalStatistics(); // Mostrar 0 o un mensaje de "sin datos"
        }
    }

    /**
     * Actualiza las etiquetas de estadísticas y el PieChart para una fecha específica.
     * @param date La fecha para la que se deben mostrar las estadísticas.
     */
    private void updateStatisticsForDate(LocalDate date) {
        List<Session> filteredSessions = sessionsByDate.getOrDefault(date, List.of()); // Obtener sesiones para la fecha, o una lista vacía

        int hits = filteredSessions.stream().mapToInt(Session::getHits).sum();
        int faults = filteredSessions.stream().mapToInt(Session::getFaults).sum();
        int total = hits + faults;

        problemasRealizados.setText(String.valueOf(total));
        problemasAcertados.setText(String.valueOf(hits));
        problemasFallados.setText(String.valueOf(faults));
        media.setText(total > 0 ? String.format("%.1f", hits * 100.0 / total) + "%" : "0%");

        dataPieChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Aciertos", hits),
                new PieChart.Data("Fallos", faults)
        ));
    }
    
    /**
     * Muestra las estadísticas totales de todas las sesiones.
     */
    private void displayTotalStatistics() {
        if (allSessions == null || allSessions.isEmpty()) {
            problemasRealizados.setText("0");
            problemasAcertados.setText("0");
            problemasFallados.setText("0");
            media.setText("0%");
            dataPieChart.setData(FXCollections.observableArrayList(
                    new PieChart.Data("Aciertos", 0),
                    new PieChart.Data("Fallos", 0)
            ));
            return;
        }

        int totalAciertos = allSessions.stream().mapToInt(Session::getHits).sum();
        int totalFallos = allSessions.stream().mapToInt(Session::getFaults).sum();
        int total = totalAciertos + totalFallos;

        problemasRealizados.setText(String.valueOf(total));
        problemasAcertados.setText(String.valueOf(totalAciertos));
        problemasFallados.setText(String.valueOf(totalFallos));
        media.setText(total > 0 ? String.format("%.1f", totalAciertos * 100.0 / total) + "%" : "0%");

        dataPieChart.setData(FXCollections.observableArrayList(
                new PieChart.Data("Aciertos", totalAciertos),
                new PieChart.Data("Fallos", totalFallos)
        ));
    }


    @FXML
    private void cambiarTema() {
        Scene scene = rootPane.getScene();

        if (scene != null) {
            ThemeManager.toggleTheme(scene);

            // Estas líneas para recrear la escena son a menudo innecesarias y pueden causar parpadeo
            // Parent currentRoot = scene.getRoot();
            // scene.setRoot(new Group());
            // scene.setRoot(currentRoot);

            // Simplemente aplicar CSS debería ser suficiente si las propiedades están bien manejadas
            // currentRoot.applyCss();
            // currentRoot.layout();
        }
    }

    @FXML
    private void cerrar(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}