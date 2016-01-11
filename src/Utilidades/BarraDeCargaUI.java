package Utilidades;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 * Interfaz de usuario para las barras de carga de la consola de juego. <br>
 * Es necesario pues la apariencia de las JProgressBar la controla, por defecto,
 * el UIManager.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class BarraDeCargaUI extends BasicProgressBarUI {
    private Color colorBarra;
    private Color colorFondo;

    /**
     * Crea una nueva interfaz de usuario para las progress bar
     * @param colorBarra color de la barra de carga
     * @param colorFondo color de fondo del elemento
     */
    public BarraDeCargaUI(Color colorBarra, Color colorFondo) {
        super();
        this.colorBarra = colorBarra;
        this.colorFondo = colorFondo;
    }

    /**
     * Crea una nueva interfaz de usuario para las progress bar con color de barra
     * azul y color de fondo negro.
     */
    public BarraDeCargaUI() {
        this(Color.blue, Color.black);
    }

    /**
     * Obtiene el color de la barra
     * @return El color de la barra
     */
    public Color getColorBarra() {
        return colorBarra;
    }

    /**
     * Cambia el color de la barra. No se verá hasta llamar a {@code repaint},
     * obviamente.
     * @param colorBarra nuevo color de la barra
     */
    public void setColorBarra(Color colorBarra) {
        this.colorBarra = colorBarra;
    }

    /**
     * Obtiene el color de fondo
     * @return El color de fondo
     */
    public Color getColorFondo() {
        return colorFondo;
    }

    /**
     * Cambia el color de fondo
     * @param colorFondo nuevo color de fondo
     */
    public void setColorFondo(Color colorFondo) {
        this.colorFondo = colorFondo;
    }

    @Override
    public void paintDeterminate(Graphics g, JComponent c) {
        if (!(g instanceof Graphics2D)) {
            return;
        }
        Insets b = progressBar.getInsets(); //Área para el borde
        int barRectWidth = progressBar.getWidth() - (b.right + b.left);
        int barRectHeight = progressBar.getHeight() - (b.top + b.bottom);
        if (barRectWidth <= 0 || barRectHeight <= 0) {
            return;
        }
        
        //Si se quieren dibujar celdas más adelante
        int cellLength = getCellLength();
        int cellSpacing = getCellSpacing();
        // Progreso a dibujar
        int amountFull = this.getAmountFull(b, barRectWidth, barRectHeight);

        if (progressBar.getOrientation() == JProgressBar.HORIZONTAL) {
            // Dibujamos las celdas
            float x = amountFull / (float) barRectWidth;
            g.setColor(colorBarra);
            g.fillRect(b.left, b.top, amountFull, barRectHeight);
            g.setColor(colorFondo);
            g.fillRect(amountFull, b.top, barRectWidth, barRectHeight);
        } else
            // VERTICAL
            // Por ahora no las quiero vericales
            return;
        
        // Si hay que pintar texto bueno...
        if (progressBar.isStringPainted()) {
            paintString(g, b.left, b.top, barRectWidth, barRectHeight, amountFull, b);
        }
    }
}
