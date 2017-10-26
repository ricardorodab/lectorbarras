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

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfx.messagebox.MessageBox;
import org.apache.commons.io.FileUtils;

/**
 * @author @version 1.0
 * @since Jun 15 2017.
 * <p>
 * Clase principal de la vista.</p>
 *
 * <p>
 * Desde esta clase se mantiene la vista y el controlador.</p>
 */
public class InterfazController implements Initializable {

    /**
     * Necesitamos mantener el texto en algun metodo de entrada.
     */
    @FXML
    private TextField entrada;

    @FXML
    private Text nombreLabel;

    @FXML
    private Text apellidoLabel;

    @FXML
    private Button cerrarSesion;

    @FXML
    private ProgressIndicator procesoBar;
    @FXML
    private AnchorPane root;

    /**
     * Es el archivo que estamos constantemente modificando.
     */
    private File file;

    /**
     * Es el objeto que va a escribir sobre el archivo.
     */
    protected static BufferedWriter writter;

    /**
     * Es el objeto que va a leer sobre el archivo.
     */
    protected static BufferedReader reader;

    /**
     * Para propositos de uso diario, se almacena los datos con fecha.
     */
    private Calendar fecha;

    /**
     * Constante para poder obtener el stage.
     */
    protected static Stage stage;

    /**
     * Apellido de la persona que inició sesión.
     */
    private String apellido;

    /**
     * Apellido de la persona que inició sesión.
     */
    private String nombre;

    /**
     * Es la carrera de los alumnos a registrar.
     */
    private String carrera;

    public void setNombres(String apellido, String nombre, String carrera) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.carrera = carrera;
        System.out.println("Carrera:" + carrera);
        nombreLabel.setText(nombre);
        apellidoLabel.setText(apellido);
    }

    public void initManager(final WindowsManager loginManager) {
        cerrarSesion.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.setHeight(stage.getHeight() / 1.2);
                stage.setWidth(stage.getWidth() / 1.5);
                loginManager.goBack();
            }
        });
    }
    
    @FXML
    private void guardarComo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter0 = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().addAll(extFilter0);
        File fileTmp = fileChooser.showSaveDialog(stage);
        if (fileTmp != null) {
            try {
                FileUtils.copyFile(file, fileTmp);
            } catch (IOException ex) {
               //Error
                Logger.getLogger(InterfazController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Metodo para guardar un nuevo registro con un nuevo nombre.
     *
     * @param event - Registra el evento del click.
     */
    @FXML
    private void crearNuevoRegistro(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter0 = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().addAll(extFilter0);
        file = fileChooser.showSaveDialog(stage);
        String hoy = fecha.get(Calendar.YEAR) + "-" + (1 + fecha.get(Calendar.MONTH)) + "-"
                + fecha.get(Calendar.DAY_OF_MONTH);
        if (file != null) {
            try {
                writter = new BufferedWriter(new FileWriter(file, false));
                writter.write("[Fecha:"+hoy + "]\n");
                writter.flush();
            } catch (IOException ex) {
                MessageBox.show(new Stage(), "¡ERROR FATAL! \n\n Ocurrió un error al"
                        + " querer guardar su registro. Disculpe las inconveniencias. \n"
                        + "Algunos consejos para evitar errores son: \n"
                        + "Iniciar como administrador el programa o tener los permisos necesarios.\n"
                        + "Tener una versión de java actualizada (1.8.0_51 o superior).",
                        "Error crítico", MessageBox.ICON_ERROR | MessageBox.OK);

            }
        }
    }

    @FXML
    private void abrirRegistroAnterior(ActionEvent event) {
        FileChooser ventana = new FileChooser();
        ventana.setTitle("Abrir");
        FileChooser.ExtensionFilter txt = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        ventana.getExtensionFilters().add(txt);
        File archivo = ventana.showOpenDialog(stage);

        if (archivo != null) {
            try {
                file = archivo;
                writter = new BufferedWriter(new FileWriter(file, true));
            } catch (IOException e) {
                MessageBox.show(new Stage(), "¡ERROR FATAL! \n\n Ocurrió un error al"
                        + " querer abrir su registro. Disculpe las inconveniencias. \n"
                        + "Algunos consejos para evitar errores son: \n"
                        + "Iniciar como administrador el programa o tener los permisos necesarios.\n"
                        + "Tener una versión de java actualizada (1.8.0_51 o superior)."
                        + "ricardo_rodab@ciencias.unam.mx",
                        "Error crítico", MessageBox.ICON_ERROR | MessageBox.OK);
            }
        }

    }

    /**
     * Metodo para abrir un registro en edicion.
     *
     * @param event - Registra el evento del click.
     */
    @FXML
    private void abrirRegistro(ActionEvent event) {
        if (Desktop.isDesktopSupported()) {
            new Thread(() -> {
                Desktop dt = Desktop.getDesktop();
                try {
                    dt.open(file);
                } catch (IOException ex) {
                    MessageBox.show(new Stage(), "¡ERROR FATAL! \n\n No se pudo "
                            + "abrir el el registro. Vuelva a intentar.\n",
                            "Error crítico", MessageBox.ICON_ERROR | MessageBox.OK);
                }
            }).start();
        }
    }

    /**
     * Metodo para reiniciar un registro desde cero.
     *
     * @param event - Registra el evento del click.
     */
    @FXML
    private void reiniciarRegistro(ActionEvent event) {
        String nombre = file.getName();
        if (!file.delete()) {
            //error;
        } else {
            String hoy = fecha.get(Calendar.YEAR) + "-" + (1 + fecha.get(Calendar.MONTH)) + "-"
                    + fecha.get(Calendar.DAY_OF_MONTH);
            try {
                file = new File("./results/" + nombre);
                writter = new BufferedWriter(new FileWriter(file));
                writter.write("[Fecha:" + hoy + "]\n");
                writter.flush();
            } catch (IOException ex) {
                MessageBox.show(new Stage(), "¡ERROR FATAL! \n\n No se pudo "
                        + "realizar el reinicio del registro. Vuelva a intentar.\n",
                        "Error crítico", MessageBox.ICON_ERROR | MessageBox.OK);
            }
        }
    }

    /**
     * Metodo para ecanear un codigo de barras y almacenarlo.
     *
     * @param event - Registra el evento del click.
     */
    @FXML
    private void escanear(ActionEvent event) {
        String entradaText = entrada.getText();
        entradaText = entradaText.trim();
        entradaText = entradaText.replaceAll("tid:", "");
        entradaText = entradaText.replace("tid", "");
        entradaText = entradaText.replace("Tid", "");
        if (entradaText.contains(":")) {
            entradaText = entradaText.split(":")[1];
        }
        try {
            if (event.getSource() instanceof TextField) {
                Thread.sleep(500);
            }
        } catch (InterruptedException ex) {
        } finally {
            if (EsNumero.esNumero(entradaText)) {
                guardarEntrada(entradaText);
            } else {
                entrada.setText("");
                MessageBox.show(new Stage(), "¡Error del codigo! \n\n Ocurrió un error al"
                        + " ingresar el codigo de barras. Sólo debe recibir números. \n",
                        "Error de codigo", MessageBox.ICON_ERROR | MessageBox.OK);
            }
        }
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
            try {
                writter.close();
            } catch (IOException ex) {
                MessageBox.show(new Stage(), "¡ERROR FATAL! \n\n Ocurrió un error al"
                        + " salir del programa. Vuelva a intentarlo. \n",
                        "Error crítico", MessageBox.ICON_ERROR | MessageBox.OK);
            }
            System.exit(0);
        }
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

    @FXML
    private void ordenarYQuitarRepetidos(ActionEvent event) {
        myEliminarRepetidos();
        myOrdernar();
    }

    @FXML
    private void ordenar(ActionEvent event) {
        myOrdernar();
    }

    @FXML
    private void eliminarRepetidos(ActionEvent event) {
        myEliminarRepetidos();
    }

    /**
     * Este es el main de nuestro programa.
     *
     * @param url -
     * @param rb -
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stage.setHeight(stage.getHeight() * 1.2);
        stage.setWidth(stage.getWidth() * 1.5);
        fecha = Calendar.getInstance();
        String hoy = fecha.get(Calendar.YEAR) + "-" + (1 + fecha.get(Calendar.MONTH)) + "-"
                + fecha.get(Calendar.DAY_OF_MONTH);
        try {
            file = new File("./results/" + hoy + ".txt");
            if (!file.exists()) {
                writter = new BufferedWriter(new FileWriter(file));
                writter.write("[Fecha:" + hoy + "]\n");
                writter.flush();
            } else {
                writter = new BufferedWriter(new FileWriter(file, true));
            }
        } catch (IOException ex) {
            MessageBox.show(new Stage(), "¡ERROR FATAL! \n\n Ocurrió un error al"
                    + " querer abrir su registro. Disculpe las inconveniencias. \n"
                    + "Algunos consejos para evitar errores son: \n"
                    + "Iniciar como administrador el programa o tener los permisos necesarios.\n"
                    + "Tener una versión de java actualizada (1.8.0_51 o superior).",
                    "Error crítico", MessageBox.ICON_ERROR | MessageBox.OK);
        }
    }

    /**
     * Guarda en el archivo lo que reciba literal.
     *
     * @param entradaText - Es la entrada que guardaremos.
     */
    private void guardarLinea(String entradaText) {
        try {
            writter.write(entradaText);
            writter.flush();
        } catch (IOException ex) {
            MessageBox.show(new Stage(), "¡ERROR FATAL! \n\n Ocurrió un error al"
                    + " leer y guardar el codigo de barras. Vuelva a intentarlo \n",
                    "Error crítico", MessageBox.ICON_ERROR | MessageBox.OK);
        }
    }

    /**
     * Guarda con formato de entrada de lectura en el archivo.
     *
     * @param entradaText El nuevo registro a guardar.
     */
    private void guardarEntrada(String entradaText) {
        int hora = fecha.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha.get(Calendar.MINUTE);
        String horaComplete = minuto < 10
                ? Integer.toString(hora) + ":0" + Integer.toString(minuto)
                : Integer.toString(hora) + ":" + Integer.toString(minuto);
        try {
            writter.write("[Codigo:" + entradaText + "],-,"
                    + "[Carrera:" + this.carrera + "],-,"
                    + "[Registrado por:" + this.nombre + " "
                    + this.apellido + "],-,[Hora:" + horaComplete + "]\n");
            writter.flush();
            entrada.setText("");
        } catch (IOException ex) {
            MessageBox.show(new Stage(), "¡ERROR FATAL! \n\n Ocurrió un error al"
                    + " leer y guardar el codigo de barras. Vuelva a intentarlo \n",
                    "Error crítico", MessageBox.ICON_ERROR | MessageBox.OK);
        }
    }

    private void myEliminarRepetidos() {
        procesoBar.setVisible(true);
        root.setDisable(true);
        HashMap<Integer, String> conjunto = new HashMap<Integer, String>();
        String fecha = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.contains("Fecha") || linea.equals("") || linea.isEmpty()) {
                    if (linea.contains("Fecha")) {
                        fecha = linea;
                    }
                    continue;
                }
                String[] parse = linea.split(",-,");
                String dato = parse[0];
                dato = dato.replace("[", "").replace("]", "");
                dato = dato.split(":")[1];
                int id = Integer.parseInt(dato);
                if (!conjunto.containsKey(id)) {
                    conjunto.put(id, linea);
                }
            }
            String nombre = file.getName();
            if (!file.delete()) {
                //error;
            } else {
                file = new File("./results/" + nombre);
                writter = new BufferedWriter(new FileWriter(file));
                guardarLinea(fecha + "\n");
                for (String entry : conjunto.values()) {
                    guardarLinea(entry + "\n");
                }
            }
        } catch (IOException ex) {
            MessageBox.show(new Stage(), "¡ERROR FATAL! \n\n Ocurrió un error al"
                    + " querer abrir su registro. Disculpe las inconveniencias. \n"
                    + "Algunos consejos para evitar errores son: \n"
                    + "Iniciar como administrador el programa o tener los permisos necesarios.\n"
                    + "Tener una versión de java actualizada (1.8.0_51 o superior).",
                    "Error crítico", MessageBox.ICON_ERROR | MessageBox.OK);
        }

        root.setDisable(false);
        procesoBar.setVisible(false);
    }

    private void myOrdernar() {
        procesoBar.setVisible(true);
        root.setDisable(true);
        ArrayList<String> elementos = new ArrayList<String>();
        String fecha = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.contains("Fecha") || linea.equals("") || linea.isEmpty()) {
                    if (linea.contains("Fecha")) {
                        fecha = linea;
                    }
                    continue;
                }
                elementos.add(linea);
            }
            elementos.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    if (o1.equals(02)) {
                        return 0;
                    }
                    String[] parse = o1.split(",-,");
                    String dato = parse[0];
                    dato = dato.replace("[", "").replace("]", "");
                    dato = dato.split(":")[1];
                    int id = Integer.parseInt(dato);

                    String[] parse2 = o2.split(",-,");
                    String dato2 = parse2[0];
                    dato2 = dato2.replace("[", "").replace("]", "");
                    dato2 = dato2.split(":")[1];
                    int id2 = Integer.parseInt(dato2);
                    if (id < id2) {
                        return -1;
                    } else if (id == id2) {
                        return 0;
                    } else {
                        return 1;
                    }
                }
            });
            String nombre = file.getName();
            if (!file.delete()) {
                //error;
            } else {
                file = new File("./results/" + nombre);
                writter = new BufferedWriter(new FileWriter(file));
                guardarLinea(fecha + "\n");
                for (int i = 0; i < elementos.size(); i++) {
                    guardarLinea(elementos.get(i) + "\n");
                }
            }
        } catch (IOException ex) {
            MessageBox.show(new Stage(), "¡ERROR FATAL! \n\n Ocurrió un error al"
                    + " querer abrir su registro. Disculpe las inconveniencias. \n"
                    + "Algunos consejos para evitar errores son: \n"
                    + "Iniciar como administrador el programa o tener los permisos necesarios.\n"
                    + "Tener una versión de java actualizada (1.8.0_51 o superior).",
                    "Error crítico", MessageBox.ICON_ERROR | MessageBox.OK);
        }

        root.setDisable(false);
        procesoBar.setVisible(false);
    }

} //Fin de InterfazController.java
