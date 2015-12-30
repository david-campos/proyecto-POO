/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Mapa;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 *
 * @author David Campos Rodríguez <david.campos@rai.usc.es>
 */
public class EstrategiaGuardado implements ExclusionStrategy{
    @Override
    public boolean shouldSkipField(FieldAttributes fa) {
        return fa.getName().equals("mapa") //Para celdas y enemigos
                || fa.getName().equals("juego"); //Para todos
    }
    @Override
    public boolean shouldSkipClass(Class<?> type) {
        return false;
    }
}
