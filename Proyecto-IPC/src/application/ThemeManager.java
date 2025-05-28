/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package application;

import javafx.scene.Scene;



public class ThemeManager {
    private static boolean modoOscuro = false;

    public static boolean isModoOscuro() {
        return modoOscuro;
    }

    public static void setModoOscuro(boolean oscuro) {
        modoOscuro = oscuro;
    }

    public static String getEstiloActual() {
        return modoOscuro 
                ? "/application/problemas.css"
                : "/application/modoNocturno.css" ;
    }

    public static void toggleTheme(Scene scene) {
        modoOscuro = !modoOscuro;
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getEstiloActual());
    }
}
