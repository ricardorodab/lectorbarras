/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lectorbarras;

/**
 *
 * @author ricardo
 */
import java.io.IOException;
import java.util.logging.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Manages control flow for logins
 */
public class WindowsManager {

    private Scene scene;

    public WindowsManager(Scene scene) {
        this.scene = scene;
    }

    /**
     * Callback method invoked to notify that a user has been authenticated.
     * Will show the main application screen.
     */
    public void authenticated(String apellido, String nombre, String carrera) {
        showMainView(apellido, nombre, carrera);
    }

    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("Login.fxml")
            );
            this.scene.setRoot((Parent) loader.load());
            LoginController controller
                    = loader.<LoginController>getController();
            controller.initManager(this);
  
        } catch (IOException ex) {
            System.out.println("ERROR");
            //Crear ventana error.
        }
    }

    private void showMainView(String apellido, String nombre, String carrera) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("Interfaz.fxml")
            );
            this.scene.setRoot((Parent) loader.load());         
            InterfazController controller
                    = loader.<InterfazController>getController();
            controller.setNombres(apellido, nombre, carrera);
            controller.initManager(this);
        } catch (IOException ex) {
            //Crear ventana error
        }
    }

    public void goBack() {
        showLoginScreen();
    }
}
