/* -------------------------------------------------------------------
* InterfazController.java
* versión 1.0
* Copyright (C) 2017 
* Facultad de Ciencias,
* Universidad Nacional Autónoma de México, Mexico.
*
* Este programa es software libre; se puede redistribuir
* y/o modificar en los términos establecidos por la
* Licencia Pública General de GNU tal como fue publicada
* por la Free Software Foundation en la versión 2 o
* superior.
*
* Este programa es distribuido con la esperanza de que
* resulte de utilidad, pero SIN GARANTÍA ALGUNA; de hecho
* sin la garantía implícita de COMERCIALIZACIÓN o
* ADECUACIÓN PARA PROPÓSITOS PARTICULARES. Véase la
* Licencia Pública General de GNU para mayores detalles.
*
* Con este programa se debe haber recibido una copia de la
* Licencia Pública General de GNU, de no ser así, visite el
* siguiente URL:
* http://www.gnu.org/licenses/gpl.html
* o escriba a la Free Software Foundation Inc.,
* 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
* -------------------------------------------------------------------
 */
package lectorbarras;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Dialog;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import jfx.messagebox.MessageBox;

/**
 * @author ricardo abreu
 * @version 1.0
 * @since Oct 23 2017.
 * <p>
 * Clase principal.</p>
 *
 * <p>
 * Esta clase manda a llamar al controlador de la vista login.</p>
 */
public class LoginController {

    @FXML
    private TextField nombre;
    @FXML
    private TextField apellido;
    @FXML
    private Button loginButton;
    @FXML
    private MenuButton carreras;
    @FXML
    private Hyperlink linkBorrar;

    private Scene scene;

    @FXML
    private void removeCarrera(ActionEvent event) {
        linkBorrar.setVisited(false);
        LectorCarrerasXML actuales = new LectorCarrerasXML();
        List<String> choices = actuales.getNombres();

        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle("Escoja carrera a eliminar");
        dialog.setHeaderText("¿Qué carrera desea eliminar?");
        dialog.setContentText("Carrera a eliminar:");

// Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String resultado = result.get();
            int seguro = MessageBox.show(new Stage(), "¿Está seguro que "
                    + "desea borrar la carrera " + resultado + "? \n",
                    "¿Está seguro?", MessageBox.ICON_QUESTION | MessageBox.YES | MessageBox.NO);
            if (seguro == MessageBox.YES) {
                try {
                    Carrera aBorrar = actuales.getCarreras(resultado);
                    if (aBorrar != null) {
                        actuales.deleteCarrera(aBorrar);
                        setCarreras();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

// The Java 8 way to get the response value (with lambda expression).
        result.ifPresent(letter -> System.out.println("Your choice: " + letter));
    }

    @FXML
    private void addCarrera(ActionEvent event) {
        Dialog<Pair<Integer, String>> dialog = new Dialog<>();
        dialog.setTitle("Crear nueva carrera");
        dialog.setHeaderText("Ingresa los datos de la nueva carrera");
        dialog.setGraphic(new ImageView(this.getClass()
                .getResource("icono_ciencias.png").toString()));
        ButtonType loginButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        Spinner<Integer> id = new Spinner<Integer>();
        id.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 150));
        id.setEditable(true);

        TextField nombre = new TextField();
        nombre.setPromptText("Nombre de la nueva carrera...");

        grid.add(new Label("Clave de la carrera:"), 0, 0);
        grid.add(id, 1, 0);
        grid.add(new Label("Nombre de la carrera:"), 0, 1);
        grid.add(nombre, 1, 1);

        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);
        dialog.getDialogPane().setContent(grid);
        nombre.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });
        Platform.runLater(() -> nombre.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>((Integer) id.getValue(), nombre.getText());
            }
            return null;
        });

        Optional<Pair<Integer, String>> result = dialog.showAndWait();

        result.ifPresent(idNombre -> {
            Carrera nueva = new Carrera(idNombre.getKey(), idNombre.getValue().toUpperCase());
            LectorCarrerasXML lector = new LectorCarrerasXML();
            try {
                lector.addCarrera(nueva);
                setCarreras();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
    }

    public void initManager(final WindowsManager loginManager) {
        setCarreras();
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (nombre.getText().trim().isEmpty()) {
                    MessageBox.show(new Stage(), "Debes colocar su nombre \n\n "
                            + "Debe poner su nombre en el cuadro correspondiente."
                            + " Vuelva a intentarlo \n",
                            "Falta nombre", MessageBox.ICON_ERROR | MessageBox.OK);

                } else if (apellido.getText().trim().isEmpty()) {
                    MessageBox.show(new Stage(), "Debes colocar sus apellidos \n\n "
                            + "Debe poner sus apellidos en el cuadro correspondiente."
                            + " Vuelva a intentarlo \n",
                            "Faltan apellidos", MessageBox.ICON_ERROR | MessageBox.OK);
                } else {
                    String carreraSelect = carreras.getText().equals("Carreras")
                            ? "<Carrera no asignada>"
                            : carreras.getText();
                    loginManager.authenticated(apellido.getText(), nombre.getText(),
                            carreraSelect);
                }
            }
        };

        loginButton.setOnAction(event);
        apellido.setOnAction(event);
        nombre.setOnAction(event);
    }

    /**
     * Método que despliega la licencia del programa y una pequeña ayuda.
     *
     * @param event – es el evento.
     */
    @FXML
    private void acercaDe(ActionEvent event) {
        String msj = " Programa que almacena codigos de barra:\n"
                + " Versión 1.0\n"
                + " Copyright (C) 2015 "
                + " Facultad de Ciencias,\n"
                + " Universidad Nacional Autónoma de México, Mexico.\n"
                + " \n"
                + " Este programa es software libre; se puede redistribuir\n"
                + " y/o modificar en los términos establecidos por la\n"
                + " Licencia Pública General de GNU tal como fue publicada\n"
                + " por la Free Software Foundation en la versión 2 o\n"
                + " superior.\n"
                + " \n"
                + " Este programa es distribuido con la esperanza de que\n"
                + " resulte de utilidad, pero SIN GARANTÍA ALGUNA; de hecho\n"
                + " sin la garantía implícita de COMERCIALIZACIÓN o\n"
                + " ADECUACIÓN PARA PROPÓSITOS PARTICULARES. Véase la\n"
                + " Licencia Pública General de GNU para mayores detalles.\n"
                + " \n"
                + " Con este programa se debe haber recibido una copia de la\n"
                + " Licencia Pública General de GNU, de no ser así, visite el\n"
                + " siguiente URL:\n"
                + " http://www.gnu.org/licenses/gpl.html\n"
                + " o escriba a la Free Software Foundation Inc.,\n"
                + " 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.\n\n"
                + "Contacto:";

        MessageBox.show(new Stage(), msj, "Un poco sobre el programa...",
                MessageBox.OK);

    }

    /**
     * Metodo para salir correctamente el programa y detener ejecucion.
     *
     * @param event - Registra el evento del click.
     */
    @FXML
    private void salir(ActionEvent event) {
        int seguro = MessageBox.show(new Stage(), "¿Está seguro que "
                + "desea abandonar el programa? \n",
                "¿Está seguro?", MessageBox.ICON_QUESTION | MessageBox.YES | MessageBox.NO);
        if (seguro == MessageBox.YES) {
            System.exit(0);
        }
    }
   

    private void setCarreras() {
        LectorCarrerasXML lector = new LectorCarrerasXML();
        LinkedList<Carrera> carreraList = lector.getCarreras();
        carreras.getItems().clear();
        for (int i = 0; i < carreraList.size(); i++) {
            MenuItem menu = new MenuItem(carreraList.get(i).getNombre());
            menu.addEventHandler(EventType.ROOT, new EventHandler<Event>() {
                @Override
                public void handle(Event event) {
                    carreras.setText(menu.getText());
                }
            });
            carreras.getItems().add(menu);
        }
    }

}
