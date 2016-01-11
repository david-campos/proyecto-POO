package Utilidades;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Clase para el control de la reproducción de los sonidos del juego.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public abstract class Sonido {
    
    /**
     * Reproduce el sonido indicado
     * @param sonido ruta realtiva desde 'snd/' del archivo de sonido, sin la extensión,
     *          que debe ser '.wav'.
     * @return El clip de reproducción del sonido.
     */
    public static Clip play(String sonido){
        try {
            AudioInputStream stream;
            AudioFormat format;
            DataLine.Info info;
            Clip clip;
            
            File archivo = new File("snd/"+sonido+".wav");
            
            stream = AudioSystem.getAudioInputStream(archivo);
            format = stream.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);
            clip.start();
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(Sonido.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
