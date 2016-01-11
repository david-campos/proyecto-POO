package proyecto2;

import Menus.Menu;
import Menus.MenuConsola;
import Menus.MenuGrafico;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Proyecto de Programación Orientada a Objetos
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 * @author Cristofer Canosa <a href="mailto:cristofer.canosa@rai.usc.es">cristofer.canosa@rai.usc.es</a>
 * @version 4.0
 */
public class Proyecto2 {

    /**
     * Función de inicio del programa
     * @param args the command line arguments
     */
    public static void main(String[] args){
        Menu menuInicio;
        if(args.length > 0 && args[0].equalsIgnoreCase("-consola"))
            menuInicio =  new MenuConsola();
        else{
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(MenuGrafico.class.getName()).log(Level.SEVERE, null, ex);
            }
            menuInicio = new MenuGrafico();
        }
        menuInicio.lanzar();
    }
}
