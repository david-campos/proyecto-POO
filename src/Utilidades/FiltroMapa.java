/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilidades;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class FiltroMapa extends FileFilter {
    @Override
    public boolean accept(File f) {
        return f.isDirectory();
    }

    @Override
    public String getDescription() {
        return "Carpetas de mapa y demás carpetas.";
    }
}