package com.byma.back_acdits.application.util;

public class Validador {

    public static final String CAMPO_NO_NULO = "Los campos no pueden ser nulos";

    public static boolean tieneCamposNulos(Object... objects){
        for (Object object : objects){
            if (object == null){
                return true;
            }
        }
        return false;
    }

    public static void validarNoNulo(Object... objects){
        if (tieneCamposNulos(objects)) {
            throw new IllegalArgumentException(CAMPO_NO_NULO);
        }
    }
}
