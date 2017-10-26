/* -------------------------------------------------------------------
 * EsNumero.java
 * versión 2.0
 * Copyright (C) 2014  José Ricardo Rodríguez Abreu.
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

/**
 * @author Jose Ricardo Rodriguez Abreu
 * @version 2.0
 * @since Aug 27 2014.
 * <p>
 * Clase para ver si una cadena representa o no un número.</p>
 *
 * <p>
 * Con un sólo método, el propósito de esta clase es conocer si una cadena
 * representa o no un número.</p>
 */
public class EsNumero {

    /**
     * Método único de la clase.
     *
     * @param posibleNumero - es una cadena que puede o no ser número.
     * @return true sólo si es un número o false caso contrario.
     */
    public static boolean esNumero(String posibleNumero) {
        try {
            double num = Double.parseDouble(posibleNumero);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
} //Fin de EsNumero.java