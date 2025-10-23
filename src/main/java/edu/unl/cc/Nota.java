package edu.unl.cc;
// Nodo de lista enlazada que almacena un texto de nota y un enlace al siguiente nodo
public class Nota {
    String texto;
    Nota siguiente;

    public Nota(String texto) {
        this.texto = texto;
        this.siguiente = null;
    }
}

