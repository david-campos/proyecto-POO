/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Juego;

import Excepciones.ComandoExcepcion;
import Excepciones.PersonajeException;
import Personajes.Jugador;
import Mapa.Mapa;
import Personajes.Enemigo;
import Comandos.*;
import Mapa.Punto;

/**
 * Clase que se encargará de gestionar el juego en general
 * @author crist
 */
public final class Juego {
    private Mapa mapa;
    private Jugador jug;
    private Consola consola;
    private String[] anteriorComando=null;
    
    public Juego (Mapa mapa, Jugador jugador, Consola c) {
        this.mapa = mapa;
        jug = jugador;
        consola = c;
    }
    public Juego (Mapa mapa) {
        this(mapa, null, new ConsolaMapa2(new Punto(mapa.getAncho(), mapa.getAlto())));
    }
    
    public Mapa getMapa() {
        return mapa;
    }
    public void setMapa(Mapa map) {
        if(map != null) 
            mapa = map;
    }
    public Jugador getJugador() {
        return jug;
    }
    public void setJugador(Jugador j) {
        if(j!=null)
            jug = j;            
    }
    public void setConsola(Consola csl) {
        if(csl!=null)
            consola = csl;
    }
    public Consola getConsola() {
        return consola;
    }
    
    /**
     * Inicia el juego.
     */
    public void iniciar() throws Exception {
        if(mapa == null || jug == null || consola == null){
            throw new Exception("No se puede iniciar el juego :c"); //No se inicia tt...
        }
        
        log(mapa.getNombre());
        log(mapa.getDescripcion());
        log("Pulsa enter para continuar...");
        consola.leer();
        while(true)
        {
            int seguir = 2;
            //Imprimir mapa
            impMapa();
            log("\tNuevo turno", true);
            //Setear energia
            jug.setEnergia(jug.getEnergiaPorTurno());
            //Sólo dura un turno
            if(jug.getToreado() >= 0){
                jug.setEnergia(jug.getEnergia() - jug.getToreado());
                jug.setToreado(0);
            }
            //Bucle de turno
            while(jug.getEnergia() > 0 && jug.getVida() > 0 && seguir>1)
            {
                //Imprimir estado
                imprimirPrompt();
                //Leemos comando
                try{
                    seguir = procesaComando(consola.leer().split(" "));
                    consola.imprimirMapa(mapa);
                }catch(ComandoExcepcion e){
                    log(e.getMessage(), true);
                }
            }
            if(jug.getEnergia() == 0)
                log("SIN ENERGÍA, cambio de turno.", true);
            if(seguir==0)
                break;
            
            //Enemigos
            for(Enemigo enemigo: mapa.getEnemigos())
            {
                enemigo.setEnergia(enemigo.getEnergiaPorTurno());
                if(enemigo.getToreado() >= 0){
                    enemigo.setEnergia(enemigo.getEnergia() - enemigo.getToreado());
                    enemigo.setToreado(0);
                }

                enemigo.iaTurno();
                
                if(jug.getVida() <= 0)
                    break;
            }
            
            //Explosiones :D
            for(int i=0; i < mapa.getAlto(); i++)
                for(int j=0; j < mapa.getAncho(); j++)
                    if(mapa.getCelda(j,i).isBomba())
                        mapa.getCelda(j, i).detonar();
            
            if(jug.getVida() <= 0){
                log("HAS MUERTO! :c");
                break;
            }
        }
        log("Fin del juego.");
    }
    
    private void imprimirPrompt(){
        consola.imprimirEstado(jug.getNombre() + "[ "
                +(jug.tieneBinoculares()?"+"+jug.getBinoculares().getPlusRango()+"\u229A , ":"")
                +(jug.tieneArmas()?"+"+jug.getEfectoArmas()+"A , ":"")
                +(jug.getArmadura()!=null?"+"+jug.getArmadura().getDefensa()+"\u2666 , ":"")
                +jug.getVida()+"\u2665 , "
                +jug.getEnergia()+"E ] ");
    }
    public void log(String mensaje, boolean limpiarPrimero) {
        if(limpiarPrimero)
            consola.limpiar();
        consola.imprimir(mensaje);
    }
    public void log(String mensaje){
        log(mensaje, false);
    }
    public void impMapa() {
        if(mapa != null)
            consola.imprimirMapa(mapa);
    }
    
    /**
     * Procesa los comandos introducidos por el jugador.
     * @param comando el comando a procesar, separado en un array de Strings
     * @return 0 si no sigue el juego, 1 si no sigue el turno, 2 si sigue
     */
    private int procesaComando(String[] comando) throws ComandoExcepcion {
        if(comando == null || comando.length == 0 || comando[0].isEmpty())
            return procesaComando(anteriorComando==null?new String[]{"ayuda"}:anteriorComando);
        anteriorComando = comando; //Repeticion del comando pulsando enter
        ComandoCompuesto cc = new ComandoCompuesto();
        for(int i=0; i < comando.length; i++){
            switch(comando[i].toLowerCase())
            {
                case "pasar":
                    cc.ejecutar();
                    return 1;
                case "salir":
                    consola.cerrar();
                    return 0;
                case "mover":
                    if(i+1<comando.length){
                        cc.add(new ComandoMover(jug, comando[i+1]));
                        i++;
                    }else
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para mover, requerido 1 (palabra "+(i+1)+")");
                    break;
                case "atacar":
                    if(i+3 >= comando.length)
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para atacar, requeridos 3(palabra "+(i+1)+")");
                    else{
                        cc.add(new ComandoAtacar(jug,
                                comando[i+3].toLowerCase().equals("-")?null:comando[i+3],
                                jug.getPos().x + Integer.parseInt(comando[i+1]), jug.getPos().y + Integer.parseInt(comando[i+2]))
                        );
                        i+=3;
                    }
                    break;
                case "mirar_mochila":
                    cc.add(new ComandoMirarMochila(jug));
                    break;
                case "mirar":
                    if(i+1 < comando.length){
                        if(comando[i+1].matches("-?\\d")){
                            if(i+3 < comando.length){
                                cc.add(new ComandoMirar(
                                        jug,
                                        mapa.getCelda(jug.getPos().x + Integer.parseInt(comando[i+1]), jug.getPos().y + Integer.parseInt(comando[i+2])),
                                        comando[i+3].toLowerCase().equals("-")?null:comando[i+3])
                                );
                                i+=3;
                            }else
                                throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para mirar, requeridos 3 (palabra "+(i+1)+")");
                        }else{
                            if(i+1 < comando.length){
                               cc.add(new ComandoMirarPorObjetos(comando[i+1].toLowerCase().equals("-")?null:comando[i+1], jug));
                               i++;
                            }else
                                throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para mirar, retquerido 1 (palabra "+(i+1)+")");
                        }
                    }else
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para mirar requeridos 1 o 3 (palabra "+(i+1)+")");
                    break;
                case "coger":
                    if(i+1 < comando.length){
                        cc.add(new ComandoCoger(jug, comando[i+1]));
                        i++;
                    }else
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para coger, requerido 1 (palabra "+(i+1)+")");
                    break;
                case "tirar":
                    if(i+1 < comando.length){
                        cc.add(new ComandoTirar(jug, comando[i+1]));
                        i++;
                    }else
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para tirar, requerido 1 (palabra "+(i+1)+")");
                    break;
                case "equipar":
                    if(i+1 < comando.length){
                        cc.add(new ComandoEquipar(jug, comando[i+1]));
                        i++;
                    }else
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para equipar, requerido 1 (palabra "+(i+1)+")");
                    break;
                case "desequipar":
                    if(i+1 < comando.length){
                        cc.add(new ComandoDesequipar(jug, comando[i+1]));
                        i++;
                    }else
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para desequipar, requerido 1 (palabra "+(i+1)+")");
                    break;
                case "usar":
                    if(i+1 < comando.length){
                        cc.add(new ComandoUsar(jug, comando[i+1]));
                        i++;
                    }else
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para usar, requerido 1 (palabra "+(i+1)+")");
                    break;
                case "ayuda":
                    if(i+1 < comando.length){
                        cc.add(new ComandoAyuda(comando[i+1].toLowerCase().equals("-")?null:comando[i+1], consola));
                        i++;
                    }else
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para ayuda, requerido 1 (palabra "+(i+1)+")");
                    break;
                default:
                    throw new ComandoExcepcion("Hay un error de sintaxis, palabra " + (i+1) + ", se esperaba un nombre de comando, se encontró '"+comando[i]+"'");
            }
            if(i+1 < comando.length)
                if(comando[i+1].matches("\\d")){
                    ComandoRepetido cr = new ComandoRepetido(cc.getComandos().get(cc.size()-1), Integer.parseInt(comando[i+1]));
                    cc.remove(cc.getComandos().get(cc.size()-1)); //Convertir en comando repetido
                    cc.add(cr);
                    i++;
                }
        }
        cc.ejecutar();
        return 2;
    }
}
