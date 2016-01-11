package Juego;

import Excepciones.ComandoExcepcion;
import Personajes.Jugador;
import Mapa.Mapa;
import Personajes.Enemigo;
import Comandos.*;
import Excepciones.CeldaObjetivoNoValida;
import Excepciones.JuegoException;
import Excepciones.MaximoObjetosException;
import Excepciones.MaximoPesoException;
import Excepciones.ObjetoNoEncontradoException;
import Excepciones.ObjetoNoEquipableException;
import Utilidades.Punto;
import Objetos.Arma;
import Personajes.HeavyFloater;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Clase que se encargará de gestionar el juego en general
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 * @author Cristofer Canosa Domínguez <a href="mailto:cristofer.canosa@rai.usc.es">cristofer.canosa@rai.usc.es</a>
 */
public final class Juego {
    private Mapa mapa;
    private Jugador jug;
    private Consola consola;
    private String[] anteriorComando=null;
    private double modDificultad;
    
    /**
     * Crea un nuevo juego, pero no lo inicia.
     * @param mapa mapa en que se desarrollará el juego
     * @param jugador jugador que jugará el juego
     * @param c consola que el juego empleará
     */
    public Juego (Mapa mapa, Jugador jugador, Consola c) {
        this.mapa = mapa;
        jug = jugador;
        consola = c;
        modDificultad = 1.0;
    }
    
    /**
     * Obtiene el mapa en que se desarrolla el juego
     * @return El mapa sobre el que se desarrolla el juego
     */
    public Mapa getMapa() {
        return mapa;
    }

    /**
     * Cambia el mapa sobre el que se desarrollará el juego, no se admiten
     * valores nulos. No realiza ningún cambio en el jugador, que seguirá
     * enlazado al mapa anterior.
     * @param map mapa sobre el que se desarrollará el juego ahora.
     */
    public void setMapa(Mapa map) {
        if(map != null) 
            mapa = map;
    }

    /**
     * Obtiene el jugador del juego
     * @return El jugador del juego
     */
    public Jugador getJugador() {
        return jug;
    }

    /**
     * Cambia el jugador del juego. No modifica el enlace del mismo con
     * el mapa, ni el del mapa anterior con el jugador.
     * @param j nuevo jugador del juego
     */
    public void setJugador(Jugador j) {
        if(j!=null)
            jug = j;            
    }

    /**
     * Cambia la consola en la que está corriendo el juego, no recomendable
     * después de iniciarlo.
     * @param csl nueva consola en la que correr el juego
     */
    public void setConsola(Consola csl) {
        if(csl!=null)
            consola = csl;
    }

    /**
     * Obtiene la consola en la que correrá / está corriendo el juego.
     * @return La consola sobre la que corre el juego.
     */
    public Consola getConsola() {
        return consola;
    }

    /**
     * Obtiene la dificultad que el juego tiene fijada
     * @return La dificultad del juego.
     */
    public double getModDificultad() {
        return modDificultad;
    }

    /**
     * Modifica la dificultad que el juego tiene fijada.
     * @param md nueva dificultad del juego.
     */
    public void setModDificultad(double md) {
        modDificultad = md;
    }
    /**
     * <p>Inicia el juego y lo maneja. Esta función debería ser llamada
     * desde un hilo aparte porque la lectura en algunas consolas puede
     * bloquear el hilo en que se inicie. </p>
     * @throws JuegoException si no es posible iniciarlo
     */
    public void iniciar() throws JuegoException{
        if(mapa == null || jug == null || consola == null){
            throw new JuegoException("No se puede iniciar el juego :c"); //No se inicia tt...
        }
        
        //Imprimimos nombre y descripción del mapa, esperamos.
        log(mapa.getNombre());
        log(mapa.getDescripcion());
        log("Pulsa enter para continuar...");
        consola.leer();
        
        //Iniciamos bucle de juego
        Utilidades.Sonido.play("cuerda_viento");
        boolean jefeFinal = false;
        while(true)
        {
            int seguir = 2;
            //Imprimir mapa
            impMapa();
            log("\tNuevo turno", false);
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
                imprimirEstado();
                //Leemos comando
                try{
                    seguir = procesaComando(consola.leer().split(" "));
                    consola.imprimirMapa();
                }catch(ComandoExcepcion e){
                    log(e.getMessage(), true);
                }
            }
            if(jug.getEnergia() == 0)
                log("SIN ENERGÍA, cambio de turno.", false);
            else
                log("", true); //Has pasado, esto se borra.
            if(seguir==0)
                break;
            
            //Si has muerto (suicidio)
            if(jug.getVida() <= 0){ hasMuerto(); break;}
            
            //Enemigos
            for(Enemigo enemigo: mapa.getEnemigos())
            {
                enemigo.setEnergia(enemigo.getEnergiaPorTurno());
                if(enemigo.getToreado() >= 0){
                    enemigo.setEnergia(enemigo.getEnergia() - enemigo.getToreado());
                    enemigo.setToreado(0);
                }

                enemigo.iaTurno();
                //Si te han matado, no seguimos
                if(jug.getVida() <= 0)
                    break;
            }
            
            //Si has muerto (te han matado)
            if(jug.getVida() <= 0){ hasMuerto(); break;}
            
            //Explosiones :D
            for(int i=0; i < mapa.getAlto(); i++)
                for(int j=0; j < mapa.getAncho(); j++)
                    if(mapa.getCelda(j,i).isBomba())
                        mapa.getCelda(j, i).detonar();
            
            //Si has muerto (por la explosión)
            if(jug.getVida() <= 0){ hasMuerto(); break;}
            
            if(mapa.getEnemigos().isEmpty()) {
                generarJefeFinal();
                if(jefeFinal){
                    JOptionPane.showMessageDialog(null, "HAS GANADO!", "Enhorabuena", JOptionPane.INFORMATION_MESSAGE);
                    if(consola instanceof ConsolaGrafica)
                        ((ConsolaGrafica)consola).hasGanado();
                    break;
                }
                jefeFinal = true;                
            }
            
            //Recuperas el turno con una agradable campanilla
            //que te recuerda que están listos tus muffins.
            Utilidades.Sonido.play("timbre_horno");
        }
        //Un mensajito de despedida que en la consola gráfica no tiene
        //demasiado sentido.
        if(! (consola instanceof ConsolaGrafica))
            log("Fin del juego.");
    }
    
    /**
     * Imprime el estado del jugador
     */
    public void imprimirEstado(){
        consola.imprimirEstado(jug.getNombre() + "[ "
                +(jug.tieneBinoculares()?"+"+jug.getBinoculares().getPlusRango()+"\u229A , ":"")
                +(jug.tieneArmas()?"+"+jug.getEfectoArmas()+"A , ":"")
                +(jug.getArmadura()!=null?"+"+jug.getArmadura().getDefensa()+"\u2666 , ":"")
                +jug.getVida()+"\u2665 , "
                +jug.getEnergia()+"E ] ");
    }

    /**
     * Escribe en consola el mensaje
     * @param mensaje mensaje a imprimir
     * @param limpiarPrimero indica si se limpia la consola primero
     */
    public void log(String mensaje, boolean limpiarPrimero) {
        if(limpiarPrimero)
            consola.limpiar();
        consola.imprimir(mensaje);
    }

    /**
     * Imprime en consola el mensaje (sin borrar primero)
     * @param mensaje mensaje a escribir
     */
    public void log(String mensaje){
        log(mensaje, false);
    }

    /**
     * Imprime el mapa a la consola (ella sabrá como hacerlo)
     */
    public void impMapa() {
        if(mapa != null)
            consola.imprimirMapa();
    }
    
    /**
     * Procesa los comandos introducidos por el jugador y devuelve la continuación de juego
     * @param comando el comando a procesar, separado en un array de Strings por cada espacio
     * @return 0 si no sigue el juego, 1 si no sigue el turno, 2 si sigue
     * @throws ComandoExcepcion si hay un error de usuario en el comando.
     */
    private int procesaComando(String[] comando) throws ComandoExcepcion {
        //Servicio de repetición de comando (al enviar uno vacío)
        if(comando == null || comando.length == 0 || comando[0].isEmpty())
            return procesaComando(anteriorComando==null?new String[]{"ayuda"}:anteriorComando);
        
        anteriorComando = comando; //Guardar para la repetición
        ComandoCompuesto cc = new ComandoCompuesto(); //Comando compuesto que contendrá todos los comandos introducidos
        for(int i=0; i < comando.length; i++){
            switch(comando[i].toLowerCase()){
                case "pasar":               //PASAR ejecuta el comando hasta este punto y devuelve 1
                    cc.ejecutar();
                    return 1;
                case "salir":               //SALIR sale del juego, cerrando la consola y devolviendo 0
                    consola.cerrar();
                    return 0;
                case "mover":               //MOVER mueve el jugador (mover [n|s|e|o])
                    if(i+1<comando.length){
                        cc.add(new ComandoMover(jug, comando[i+1]));
                        i++;
                    }else
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para mover, requerido 1 (palabra "+(i+1)+")");
                    break;
                case "atacar":              //ATACAR ataca una celda o un enemigo (atacar x y enemigo)
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
                case "mirar_mochila":       //MIRAR_MOCHILA lista los objetos en la mochila
                    cc.add(new ComandoMirarMochila(jug));
                    break;
                case "mirar":               //MIRAR mira una celda en busca de enemigos, un enemigo concreto,
                                            //la celda del jugador en busca de objetos o la celda del jugador
                                            //por un objeto concreto.
                                            //mirar x y enemigo | mirar objeto
                    if(i+1 < comando.length){
                        if(comando[i+1].matches("-\\d+|\\d*")){
                            if(i+3 < comando.length){
                                cc.add(new ComandoMirar(
                                        jug,
                                        Integer.parseInt(comando[i+1]),
                                        Integer.parseInt(comando[i+2]),
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
                case "coger":               //COGER coge un objeto de la celda (coger objeto)
                    if(i+1 < comando.length){
                        cc.add(new ComandoCoger(jug, comando[i+1]));
                        i++;
                    }else
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para coger, requerido 1 (palabra "+(i+1)+")");
                    break;
                case "tirar":               //TIRAR tira un objeto en la celda (tirar objeto)
                    if(i+1 < comando.length){
                        cc.add(new ComandoTirar(jug, comando[i+1]));
                        i++;
                    }else
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para tirar, requerido 1 (palabra "+(i+1)+")");
                    break;
                case "equipar":             //EQUIPAR equipa un objeto de la mochila (equipar objeto)
                    if(i+1 < comando.length){
                        cc.add(new ComandoEquipar(jug, comando[i+1]));
                        i++;
                    }else
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para equipar, requerido 1 (palabra "+(i+1)+")");
                    break;
                case "desequipar":          //DESEQUIPAR desequipa un objeto de la mochila (desequipar objeto)
                    if(i+1 < comando.length){
                        cc.add(new ComandoDesequipar(jug, comando[i+1]));
                        i++;
                    }else
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para desequipar, requerido 1 (palabra "+(i+1)+")");
                    break;
                case "usar":                //USAR usa un objeto de la mochila (usar objeto)
                    if(i+1 < comando.length){
                        cc.add(new ComandoUsar(jug, comando[i+1]));
                        i++;
                    }else
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para usar, requerido 1 (palabra "+(i+1)+")");
                    break;
                case "ayuda":               //AYUDA muestra la ayuda (ayuda temaDeAyuda)
                    if(i+1 < comando.length){
                        cc.add(new ComandoAyuda(comando[i+1].toLowerCase().equals("-")?null:comando[i+1], consola));
                        i++;
                    }else
                        throw new ComandoExcepcion("Sintaxis incorrecta. Faltan argumentos para ayuda, requerido 1 (palabra "+(i+1)+")");
                    break;
                case "suicidarse":          //SUICIDARSE causa la muerte del jugador
                    cc.add(new ComandoMatar(jug));
                    cc.ejecutar();
                    return 1;
                default:
                    new ComandoAyuda("comandos", consola).ejecutar();
                    throw new ComandoExcepcion("Hay un error de sintaxis, palabra " + (i+1) + ", se esperaba un nombre de comando, se encontró '"+comando[i]+"'");
            }
            if(i+1 < comando.length)
                //Comprobación de repetición de comando n veces
                if(comando[i+1].matches("\\d+")){
                    ComandoRepetido cr = new ComandoRepetido(cc.getComandos().get(cc.size()-1), Integer.parseInt(comando[i+1]));
                    cc.remove(cc.getComandos().get(cc.size()-1)); //Convertir en comando repetido
                    cc.add(cr);
                    i++;
                }
        }
        cc.ejecutar(); //Ejecutar finalmente el comando compuesto
        return 2;
    }
    
    //Cuando llegas al final del juego (¡un jefe final!)
    private void generarJefeFinal() {
        Punto pos = new Punto (mapa.getAlto()/2, mapa.getAncho()/2);
        mapa.hacerTransitable(pos, false);
        try {
            Enemigo jefeFinal = new HeavyFloater("jefe final", 300, 60, new int[]{pos.y, pos.x}, this);
            Arma arma = new Arma(1.5, "Ragnarok", "Espada buena, mejor cogela", 2, 120, Arma.ARMA_UNA_MANO);
            try {
                jefeFinal.getMochila().addObjeto(arma);
                jefeFinal.equipar(arma);
            } catch (MaximoObjetosException | MaximoPesoException | ObjetoNoEquipableException | ObjetoNoEncontradoException ex) {
                //No se debería dar porque sólo tiene un objeto
                Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
            }
            mapa.addEnemigo(jefeFinal);
        } catch (CeldaObjetivoNoValida ex) {
            Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Cuando has muerto, te lo ponemos
    private void hasMuerto() {
        if(consola instanceof ConsolaGrafica){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
            }
            ((ConsolaGrafica)consola).hasMuerto();
        }else
            log("HAS MUERTO! :c");
    }
}
