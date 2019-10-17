package com.mjgl.utla.utla;

public class prueba {


    public static void main(String[] args) {
        //Segunda forma.
        /*String a = "sql";
        String texto = "lenguaje sql";
        int intIndex = texto.indexOf(a);
        System.out.println(intIndex);*/



        //Forma #2.
        String palabra = "aje";
        String cadena = "lenguaje chovi manuel";
        boolean resultado = cadena.contains(palabra);
        int intIndex = cadena.indexOf(palabra);

        if(resultado){
            System.out.println("palabra encontrada");
            System.out.println("Posición : " + intIndex);
        }else{
            System.out.println("palabra no encontrada");
        }
    }


}
