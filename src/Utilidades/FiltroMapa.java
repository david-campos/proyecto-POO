package Utilidades;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Filtro de archivos de los diálogos de apertura de mapa.<br>
 * Sólo se muestran los archivos con la extensión de archivo de mapa.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class FiltroMapa extends FileFilter {
    @Override
    public boolean accept(File f) {
        if(f.isFile()){
            String nombre = f.getName();
            return (nombre.lastIndexOf(".") != -1 && nombre.substring(nombre.lastIndexOf(".")+1).equals(ConstantesGenerales.EXTENSION_MAPA));
        }
        return true;
    }

    @Override
    public String getDescription() {
        return "Mapas";
    }
}