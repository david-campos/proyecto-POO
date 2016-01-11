package Utilidades;

import java.awt.Image;
import javax.swing.JComponent;

/**
 * <p>Interfaz para las celdas gráficas que muestran el mapa en el editor y en la 
 * consola gráfica de juego.</p>
 * <p>Poseen un id, que identifica la celda del mapa a la que van asociadas.</p>
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public interface CeldaGrafica{

    /**
     * Cambia la imagen de la celda
     * @param imagen imagen compuesta que pasará a tomar la celda
     */
    public void setImagen(ImagenCelda imagen);
    /**
     * Cambia la imagen de la capa delantera de la celda
     * @param imagen nueva imagen que ocupará la capa delantera
     */
    public void setDelante(Image imagen);
    /**
     * Recupera la última imagen que ha sido aplicada a la celda,
     * es decir, la que se está mostrando actualmente.
     * @return La última imagen aplicada a la celda
     */
    public ImagenCelda ultimaImagen();
    /**
     * Obtiene el JComponent que representa realmente esta celda en la interfaz
     * @return El JComponent que representa la celda gráfica en la UI
     */
    public JComponent getComponente();
    /**
     * Cambia el Id que asocia esta celda gráfica a una celda concreta del mapa.
     * @param pt id que se quiere asignar a la celda gráfica
     */
    public void setId(Punto pt);
    /**
     * Obtiene el id actual que enlaza esta celda gráfica a una celda en el mapa.
     * @return El id de la celda gráfica
     */
    public Punto getId();
}
