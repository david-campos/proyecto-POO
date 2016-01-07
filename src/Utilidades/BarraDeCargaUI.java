/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilidades;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class BarraDeCargaUI extends BasicProgressBarUI {

    private Color colorBarra;
    private Color colorFondo;

    public BarraDeCargaUI(Color colorBarra, Color colorFondo) {
        super();
        this.colorBarra = colorBarra;
        this.colorFondo = colorFondo;
    }

    public BarraDeCargaUI() {
        this(Color.blue, Color.black);
    }

    
    
    public Color getColorBarra() {
        return colorBarra;
    }

    public void setColorBarra(Color colorBarra) {
        this.colorBarra = colorBarra;
    }

    public Color getColorFondo() {
        return colorFondo;
    }

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
