/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Estrategia de guardado para el guardado con Gson del mapa. <br>
 * Hace que no se guarden los atributos "mapa" y "juego" para evitar bucles
 * infinitos.
 * @author David Campos Rodríguez <a href="mailto:david.campos@rai.usc.es">david.campos@rai.usc.es</a>
 */
public class EstrategiaGuardado implements ExclusionStrategy{
    @Override
    public boolean shouldSkipField(FieldAttributes fa) {
        return fa.getName().equals("mapa") //Para celdas y enemigos
                || fa.getName().equals("juego"); //Para todos
    }
    @Override
    public boolean shouldSkipClass(Class<?> type) {
        return false; //No excluye ninguna clase
    }
}
