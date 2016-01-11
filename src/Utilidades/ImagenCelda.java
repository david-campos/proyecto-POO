package Utilidades;

import java.awt.Image;
import java.util.Objects;

/**
 * Imagen compuesta que manejan las celdas, posee una parte delantera y un
 * fondo.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class ImagenCelda {
    private Image fondo;
    private Image delante;
    
    /**
     * Crea una nueva imagen de celda con el fondo indicado y la parte delantera
     * vacía.
     * @param imagen imagen de fondo con la que se desea crear
     */
    public ImagenCelda(Image imagen) {
        fondo = imagen;
        delante = null;
    }

    /**
     * Obtiene el fondo de la imagen
     * @return Imagen que forma el fondo de la imagen compuesta
     */
    public Image getFondo() {
        return fondo;
    }

    /**
     * Cambia el fondo
     * @param fondo nueva imagen de fondo de la imagen compuesta
     */
    public void setFondo(Image fondo) {
        this.fondo = fondo;
    }

    /**
     * Obtiene la imagen delantera
     * @return La imagen de delante.
     */
    public Image getDelante() {
        return delante;
    }

    /**
     * Cambia la imagen delantera
     * @param delante nueva imagen de la capa delantera de la imagen compuesta
     */
    public void setDelante(Image delante) {
        this.delante = delante;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.fondo);
        hash = 13 * hash + Objects.hashCode(this.delante);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImagenCelda other = (ImagenCelda) obj;
        if (!Objects.equals(this.fondo, other.fondo)) {
            return false;
        }
        return Objects.equals(this.delante, other.delante);
    }

}
