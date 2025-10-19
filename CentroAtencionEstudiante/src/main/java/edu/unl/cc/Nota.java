package edu.unl.cc;

public class Nota {
    String texto;
    Nota siguiente;

    public Nota(String texto) {
        this.texto = texto;
        this.siguiente = null;
    }
}
