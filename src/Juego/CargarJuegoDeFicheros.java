/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import Mapa.*;
import Objetos.*;
import Personajes.*;
import Excepciones.CargadorException;
import Excepciones.PersonajeException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

/**
 *
 * @author crist
 */
public class CargarJuegoDeFicheros implements CargadorJuego{
    private String ficheroMapa;
    private String ficheroObjetos;
    private String ficheroNPCs;
    private String tipoJugador;
    private static Random r = new Random();

    public String getFicheroMapa() {
        return ficheroMapa;
    }

    public void setFicheroMapa(String ficheroMapa) {
        this.ficheroMapa = ficheroMapa;
    }

    public String getFicheroObjetos() {
        return ficheroObjetos;
    }

    public void setFicheroObjetos(String ficheroObjetos) {
        this.ficheroObjetos = ficheroObjetos;
    }

    public String getFicheroNPCs() {
        return ficheroNPCs;
    }

    public void setFicheroNPCs(String ficheroNPCs) {
        this.ficheroNPCs = ficheroNPCs;
    }

    public String getTipoJugador() {
        return tipoJugador;
    }

    public void setTipoJugador(String tipoJugador) {
        this.tipoJugador = tipoJugador;
    }
    
    /**
     * Sin argumentos da la ruta por defecto de los archivos
     */
    public CargarJuegoDeFicheros(String tipoJugador) {
       ficheroMapa = "ficheros/mapa.csv";
       ficheroObjetos = "ficheros/objetos.csv";
       ficheroNPCs = "ficheros/npcs.csv";
       this.tipoJugador = tipoJugador;
    }
    public CargarJuegoDeFicheros(String fMapa, String fObj, String fNPCs, String tipoJugador) {
        if(fMapa != null && fObj != null && fNPCs != null) {
            ficheroMapa = fMapa;
            ficheroObjetos = fObj;
            ficheroNPCs = fNPCs;
            this.tipoJugador = tipoJugador;
        }
    }
    
    @Override
    public Juego cargarJuego() throws CargadorException {
        Mapa mapa = cargarMapa();
        Juego juego = new Juego(mapa);
        mapa.setJuego(juego);
        Jugador jugador = cargarNPCs(ficheroNPCs, mapa, juego);
        mapa.setJugador(jugador);
        juego.setJugador(jugador);
        cargarObjetos(ficheroObjetos, mapa);
        return juego;
    }
    
    private Mapa cargarMapa () throws CargadorException {
        try {
            RandomAccessFile buf = new RandomAccessFile(ficheroMapa, "r");
            String cadena;
            int ancho = 0, alto = 0;
            while((cadena = buf.readLine()) != null) {
                if(!(cadena.startsWith("#") || (cadena.split(",").length < 2))) {   //Salta comentarios y lineas vacias
                    String[] trozos = cadena.split(",");
                    if(ancho < Integer.parseInt(trozos[1])) {   //Máximo ancho
                        ancho = Integer.parseInt(trozos[1]);
                    }
                    if(alto < Integer.parseInt(trozos[0])) {   //Máximo alto
                        alto = Integer.parseInt(trozos[0]);
                    }
                }
            }
            buf.seek(0);    //Se vuelve al inicio
            ancho++; alto++;
            boolean[] tipos = new boolean[ancho*alto];
            for (int i = 0; i< ancho*alto; i++) {     //Inicializa el vector como no transitable
                tipos[i] = false;
            }
            while((cadena = buf.readLine()) != null) {
                if(!(cadena.startsWith("#") || (cadena.split(",").length < 2))) {   //Salta comentarios y lineas vacias
                    String[] trozos = cadena.split(",");
                    tipos[(ancho)*Integer.parseInt(trozos[0]) + Integer.parseInt(trozos[1])] = true;  //Pone transitable en la celda 
                }
            }
            return new Mapa("Mapa", "Un plano soleado", ancho, alto, tipos, null);
        }catch(Exception e){
            throw new CargadorException("No se pudo cargar el mapa: " + e.getMessage());
        }
    }
    private Jugador cargarNPCs(String archivo, Mapa mapa, Juego juego) throws CargadorException {
        Jugador jug = null;
        try {
            FileReader f = new FileReader(archivo);
            BufferedReader buf = new BufferedReader(f);
            String cadena;
            while((cadena = buf.readLine())!=null) {
                //Si comentario, ignorar
                if(!cadena.startsWith("#") && cadena.split(";").length > 4) {
                    String[] trozos = cadena.split(";");
                    String[] str_celda = trozos[0].split(",");
                    Punto celda = new Punto();
                    celda.y = Integer.parseInt(str_celda[0]);
                    celda.x = Integer.parseInt(str_celda[1]);
                    String tipo = trozos[1];
                    String strnombre = trozos[2];
                    int saludInicial = Integer.parseInt(trozos[3]);
                    int energiaPorTurno = Integer.parseInt(trozos[4]);
                    switch (tipo) {
                        case "jugador":
                            mapa.setPosicionInicial(celda);
                            switch (tipoJugador.toLowerCase()) {
                                default:
                                    jug = new Marine(strnombre, saludInicial, energiaPorTurno, new Mochila(), null, null, null, mapa, 5, juego);
                                    break;
                                case "zapador":
                                    jug = new Zapador(strnombre, saludInicial, energiaPorTurno, new Mochila(), null, null, null, mapa, 5, juego);
                                    break;
                                case "francotirador":
                                    jug = new Francotirador(strnombre, saludInicial, energiaPorTurno, new Mochila(), null, null, null, mapa, 5, juego);
                                    break;
                            }   break;
                        case "enemigo":
                            switch (r.nextInt(3)) {
                                case 0:
                                    mapa.addEnemigo(new Sectoid(strnombre, saludInicial, energiaPorTurno, celda.toArray(), juego));
                                    break;
                                case 1:
                                    mapa.addEnemigo(new LightFloater(strnombre, saludInicial, energiaPorTurno, celda.toArray(), juego));
                                    break;
                                case 2:
                                    mapa.addEnemigo(new HeavyFloater(strnombre, saludInicial, energiaPorTurno, celda.toArray(), juego));
                                    break;
                            }   break;
                    }
                }
            }
            buf.close();
        }catch(Exception e){
            throw new CargadorException("No se pudieron cargar los NPC's: " + e.getMessage());
        }
        return jug;
    }
    private void cargarObjetos(String archivo, Mapa mapa) throws CargadorException {
        try {
            BufferedReader buf = new BufferedReader(new FileReader(archivo));
            String cadena;
            while((cadena = buf.readLine())!=null) {
                //Si comentario, ignorar
                if(!cadena.startsWith("#") && cadena.split(";").length > 6) {
                    String[] trozos = cadena.split(";");
                    String[] str_celda = trozos[0].split(",");
                    Punto celda = new Punto();
                    celda.y = Integer.parseInt(str_celda[0]);
                    celda.x = Integer.parseInt(str_celda[1]);
                    String portador = trozos[1];
                    String tipo = trozos[2];
                    String strnombre = trozos[3];
                    String strdescripcion = trozos[4];
                    int valor1 = Integer.parseInt(trozos[5]);
                    double valor2 = Double.parseDouble(trozos[6]);
                    int valor3 = 0;
                    double valor4 = 0;
                    if(trozos.length > 7) {
                        valor3 = Integer.parseInt(trozos[7]);
                        valor4 = Double.parseDouble(trozos[8]);
                    }
                    if(portador.equals(".") || portador.equals("jugador")) {
                        switch (tipo) {
                            case "mochila":
                                mapa.getJugador().setMochila(new Mochila(valor1, (int)Math.round(valor2)));
                                break;
                            case "binoculares":
                                Binoculares binoculares = new Binoculares (strnombre, valor2, valor1);
                                ((Transitable) mapa.getCelda(celda)).addObjeto(binoculares);
                                break;
                            case "arma":
                                Arma arma = null;
                                if(valor3 == 1)
                                    arma = new Arma(valor4, strnombre, strdescripcion, (int) valor2, valor1, Arma.ARMA_UNA_MANO);
                                else if(valor3 == 2)
                                    arma = new Arma(valor4, strnombre, strdescripcion, (int) valor2, valor1, Arma.ARMA_DOS_MANOS);
                                ((Transitable)mapa.getCelda(celda)).addObjeto(arma);
                                break;
                            case "armadura":
                                Armadura armadura = new Armadura(strnombre, strdescripcion, valor4, valor1, (int) valor2, valor3);
                                ((Transitable)mapa.getCelda(celda)).addObjeto(armadura);
                                break;
                            case "botiquin":
                                Botiquin botiquin = new Botiquin(strnombre, valor2, valor1);
                                ((Transitable)mapa.getCelda(celda)).addObjeto(botiquin);
                                break;
                            default:
                                throw new CargadorException("El tipo "+tipo+" no está definido.");
                        }
                    } else {  //Objetos de los enemigos
                        switch (tipo) {
                            case "arma":
                                Arma arma = null;
                                if(valor3 == 1)
                                    arma = new Arma(valor4, strnombre, strdescripcion, (int) valor2, valor1, Arma.ARMA_UNA_MANO);
                                else if(valor3 == 2)
                                    arma = new Arma(valor4, strnombre, strdescripcion, (int) valor2, valor1, Arma.ARMA_UNA_MANO);
                                ((Transitable)mapa.getCelda(celda)).getEnemigo(portador).getMochila().addObjeto(arma);
                                ((Transitable)mapa.getCelda(celda)).getEnemigo(portador).equipar(arma);
                                break;
                            case "armadura":
                                Armadura armadura = new Armadura(strnombre, strdescripcion, valor4, valor1, (int) valor2, valor3);
                                ((Transitable)mapa.getCelda(celda)).getEnemigo(portador).getMochila().addObjeto(armadura);
                                ((Transitable)mapa.getCelda(celda)).getEnemigo(portador).equipar(armadura);
                                break;
                        }
                    }
                }
            }
            buf.close();
        }catch(IOException | NumberFormatException | CargadorException | PersonajeException e) {
            throw new CargadorException("No se pudieron cargar los objetos: (" + e.getClass().getSimpleName()+ ") " + e.getMessage() );
        }
    }
}
