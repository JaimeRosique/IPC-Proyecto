package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.VBox;
import model.*;

public class EstadisticasController implements Initializable {

    @FXML
    private VBox rootPane;
    @FXML
    private BarChart<?, ?> dataHistogram;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;
    
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

    private List<Session> sesion;
    private Navigation nav;
    private User user;

    @FXML
    private Label problemasRealizados;
    @FXML
    private Label problemasAcertados;
    @FXML
    private Label problemasFallados;
    @FXML
    private Label media;
    @FXML
    private Button moreInfoButton;
    private PieChart dataPieChart;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No se hace nada hasta que setDatos() es llamado
    }

    public void setDatos(Navigation n, User u) {
        nav = n;
        user = u;
        sesion = user.getSessions();
        //iniData();
    }

    private void iniData() {
        int totalAciertos = 0;
        int totalFallos = 0;

        for (Session s : sesion) {
            totalAciertos += s.getHits();
            totalFallos += s.getFaults();
        }

        int total = totalAciertos + totalFallos;

        problemasRealizados.setText(String.valueOf(total));
        problemasAcertados.setText(String.valueOf(totalAciertos));
        problemasFallados.setText(String.valueOf(totalFallos));
        media.setText(total > 0 ? String.format("%.1f", totalAciertos * 100.0 / total) + "%" : "0%");

        dataPieChart.getData().clear();
        dataPieChart.getData().add(new PieChart.Data("Aciertos", totalAciertos));
        dataPieChart.getData().add(new PieChart.Data("Fallos", totalFallos));
    }
}