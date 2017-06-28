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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import jfx.messagebox.MessageBox;


/**
 * @author 
 * @version 1.0
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
    
    /**
     * Es el archivo que estamos constantemente modificando.
     */
    private File file;
    
    /**
     * Es el objeto que va a escribir sobre el archivo.
     */
    protected static BufferedWriter writter;
    
    /**
     * Para propositos de uso diario, se almacena los datos con fecha.
     */
    private Calendar fecha;
    
    /** 
     * Constante para poder obtener el stage. 
     */
    protected static Stage stage;
    
    
    /**
     * Metodo para guardar un nuevo registro con un nuevo nombre.
     * @param event - Registra el evento del click.
     */
    @FXML
    private void guardarComo(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter0 = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().addAll(extFilter0);
        file = fileChooser.showSaveDialog(stage);
        String hoy = fecha.get(Calendar.YEAR) + "-" + (1+fecha.get(Calendar.MONTH)) + "-" +
                fecha.get(Calendar.DAY_OF_MONTH);
        if(file != null){
            try {
                writter = new BufferedWriter(new FileWriter(file, false));
                writter.write(hoy + "\n");
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
    
    /**
     * Metodo para abrir un registro en edicion.
     * @param event - Registra el evento del click.
     */
    @FXML
    private void abrirRegistro(ActionEvent event) {
	if( Desktop.isDesktopSupported() )
	    {
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
     * @param event - Registra el evento del click.
     */
    @FXML
    private void reiniciarRegistro(ActionEvent event) {
        String nombre = file.getName();
        if(!file.delete()) {
            //error;
        } else {
            String hoy = fecha.get(Calendar.YEAR) + "-" + (1+fecha.get(Calendar.MONTH)) + "-" +
                    fecha.get(Calendar.DAY_OF_MONTH);
            try {
                file = new File("./results/" + nombre);
                writter = new BufferedWriter(new FileWriter(file));
                writter.write(hoy + "\n");
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
     * @param event - Registra el evento del click.
     */
    @FXML
    private void escanear(ActionEvent event) {
        String entradaText = entrada.getText();
        entradaText = entradaText.replaceAll("tid:", "");
        if(event.getSource() instanceof TextField) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                
            } finally {
                try {
                    writter.write(entradaText + "\n");
                    writter.flush();
                    entrada.setText("");
                } catch (IOException ex) {
                    MessageBox.show(new Stage(), "¡ERROR FATAL! \n\n Ocurrió un error al"
                            + " leer y guardar el codigo de barras. Vuelva a intentarlo \n",
                            "Error crítico", MessageBox.ICON_ERROR | MessageBox.OK);
                }
            }
        }
    }
    
    /**
     * Metodo para salir correctamente el programa y detener ejecucion.
     * @param event - Registra el evento del click.
     */
    @FXML
    private void salir(ActionEvent event) {
        int seguro = MessageBox.show(new Stage(), "¿Está segudo que "
                + "desea abandonar el programa? \n",
                "¿Está seguro?", MessageBox.ICON_QUESTION | MessageBox.YES | MessageBox.NO);
        if(seguro == MessageBox.YES) {
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
     * @param event – es el evento.
     */
    @FXML
    private void acercaDe(ActionEvent event){
        String msj = " Programa que almacena codigos de barra:\n" +
                " Versión 1.0\n" +
                " Copyright (C) 2015 " +
                " Facultad de Ciencias,\n" +
                " Universidad Nacional Autónoma de México, Mexico.\n" +
                " \n" +
                " Este programa es software libre; se puede redistribuir\n" +
                " y/o modificar en los términos establecidos por la\n" +
                " Licencia Pública General de GNU tal como fue publicada\n" +
                " por la Free Software Foundation en la versión 2 o\n" +
                " superior.\n" +
                " \n" +
                " Este programa es distribuido con la esperanza de que\n" +
                " resulte de utilidad, pero SIN GARANTÍA ALGUNA; de hecho\n" +
                " sin la garantía implícita de COMERCIALIZACIÓN o\n" +
                " ADECUACIÓN PARA PROPÓSITOS PARTICULARES. Véase la\n" +
                " Licencia Pública General de GNU para mayores detalles.\n" +
                " \n" +
                " Con este programa se debe haber recibido una copia de la\n" +
                " Licencia Pública General de GNU, de no ser así, visite el\n" +
                " siguiente URL:\n" +
                " http://www.gnu.org/licenses/gpl.html\n" +
                " o escriba a la Free Software Foundation Inc.,\n" +
                " 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.\n\n"
                + "Contacto:";
        
        MessageBox.show(new Stage(), msj, "Un poco sobre el programa...",
                MessageBox.OK);
        
    }
    
    /**
     * Este es el main de nuestro programa.
     * @param url -
     * @param rb  -
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fecha = Calendar.getInstance();
        String hoy = fecha.get(Calendar.YEAR) + "-" + (1+fecha.get(Calendar.MONTH)) + "-" +
                fecha.get(Calendar.DAY_OF_MONTH);
        try {
            file = new File("./results/" + hoy + ".txt");
            if(!file.exists()) {
                writter = new BufferedWriter(new FileWriter(file));
                writter.write(hoy + "\n");
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
    
} //Fin de InterfazController.java
