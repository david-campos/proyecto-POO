/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapa;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;


public class PanelCeldaGraficaEditor extends JPanel implements CeldaGrafica{
    private Punto id;
    private final JLabel fondo;
    private final JLabel delante;
    private ImagenCelda ultima;
    public PanelCeldaGraficaEditor(Punto pt) {
        super();
        id = pt;
        setLayout(new OverlayLayout(this));
        fondo = new JLabel();
        delante = new JLabel();
        ultima = null;
        add(delante);
        add(fondo);
    }
    
    @Override
    public void setImagen(ImagenCelda imagen) {
        if(ultima == null || !ultima.equals(imagen)){
            ultima = imagen;
            if(imagen.getFondo() != null)
                fondo.setIcon(new ImageIcon(imagen.getFondo()));
            else
                fondo.setIcon(null);
            if(imagen.getDelante() != null)
                delante.setIcon(new ImageIcon(imagen.getDelante()));
            else
                delante.setIcon(null);
        }
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

    @Override
    public void setDelante(Image imagen) {
        ultima = new ImagenCelda(ultima.getFondo());
        ultima.setDelante(imagen);
        delante.setIcon(new ImageIcon(imagen));
    }

    @Override
    public ImagenCelda ultimaImagen() {
        return ultima;
    }
    
}
