package Utilidades;

import java.io.File;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;

/**
 * FileView que usan los diálogos de apertura de mapa, cambia el icono de los
 * archivos con extensión de mapa por un mapita.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 * @see ConstantesGenerales#EXTENSION_MAPA
 */
public class FileViewMapa extends FileView {
    @Override
    public Icon getIcon(File f) {
        if(!f.isDirectory())
        {
            String nombre = f.getName();
            if(nombre.lastIndexOf(".") != -1 && nombre.substring(nombre.lastIndexOf(".")+1).equals(ConstantesGenerales.EXTENSION_MAPA))
                return new ImageIcon("img/ico_map.png");
        }
        return super.getIcon(f);
    }
}