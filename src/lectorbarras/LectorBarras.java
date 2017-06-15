/* -------------------------------------------------------------------
* LectorBarras.java
* versión 1.0
* Copyright (C) 2017  José Ricardo Rodríguez Abreu.
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import jfx.messagebox.MessageBox;

/**
 * @author Jose Ricardo Rodriguez Abreu
 * @version 1.0
 * @since Jun 15 2017.
 * <p>
 * Clase principal.</p>
 *
 * <p>
 *  Esta clase manda a llamar al controlador de la vista..</p>
 */
public class LectorBarras extends Application {
    
    /**
     * Metodo main de un programa hecho en JavaFX.
     * @param stage - Es la interfaz que se crea.
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Interfaz.fxml"));
        Scene scene = new Scene(root);
        InterfazController.stage = stage;
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest((WindowEvent we) -> {
            MessageBox.show(new Stage(), "Recuerde que "
                    + "para cerrar correctamente el programa debe presionar el  "
                    + "menú de Archivo seguido de la opción Salir. \n",
                    "¿Está seguro?", MessageBox.ICON_WARNING | MessageBox.OK);
            try {
                if(InterfazController.writter != null)
                    InterfazController.writter.close();
                stage.close();
            } catch (IOException ex) {
                MessageBox.show(new Stage(), "¡ERROR FATAL! \n\n Ocurrió un error al"
                        + " querer terminar el programa. Vuelva a intentarlo \n",
                        "Error crítico", MessageBox.ICON_ERROR | MessageBox.OK);
            }
        });
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }    
} //Fin de LectorBarras.java
