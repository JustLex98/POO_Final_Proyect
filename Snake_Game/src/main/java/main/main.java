//Integrantes 

//MARIO ALEXANDER MOLINA FUENTES - 00372624
//GLADYS ALEJADRA RIVERA IRAHETA - 00379224
//JONATHAN RIGOBERTO MARTINEZ MENJIVAR - 00095423
//VICTOR DANIEL ORELLANA CHACON - 00074624
//MARIO ALFREDO TOBAR AYALA - 00040224


// INDICACIONES GENERALES: 
//1.- Navega hasta el archivo DatabaseManager.java que se encuentra en el paquete persistencia.
//2.- Cambiar el puerto del localhost al correspondiente a tu computadora.
//3.- Cambiar el nombre de usuario y contraseña correspondientes.

package main;

import controlador.ControladorJuego;
import vista.GameFrame;
import javax.swing.SwingUtilities;
import Musica.EfectosSonidos;

public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControladorJuego controlador = new ControladorJuego();
            EfectosSonidos sonido = new EfectosSonidos();
            new GameFrame(controlador, sonido); 
        });
    }
}