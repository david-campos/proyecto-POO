package Utilidades;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * No usado actualmente...
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 * @deprecated 
 */
public class LabelCeldaGrafica extends JLabel implements CeldaGrafica{
    private Punto id;
    private ImagenCelda ultima;
    
    public LabelCeldaGrafica(Punto pt) {
        super();
        ultima = null;
        id = pt;
    }
    
    @Override
    public void setImagen(ImagenCelda imagen) {
        ultima = imagen;
        if(imagen.getDelante() != null)
            setDelante(imagen.getDelante());
        else
            setDelante(imagen.getFondo());
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
        setIcon(new ImageIcon(imagen));
    }

    @Override
    public ImagenCelda ultimaImagen() {
        return ultima;
    }
    
}
