/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapa;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


public class LabelCeldaGraficaEditor extends JLabel implements CeldaGrafica{
    private Punto id;
    
    public LabelCeldaGraficaEditor(Punto pt) {
        super();
        id = pt;
    }
    
    @Override
    public void setImagen(Image imagen) {
        setIcon(new ImageIcon(imagen));
    }

    @Override
    public JComponent getComponente() {
        return this;
    }

    @Override
    public void setId(Punto pt) {
        id = pt;
    }

    @Override
    public Punto getId() {
        return id;
    }
    
}
