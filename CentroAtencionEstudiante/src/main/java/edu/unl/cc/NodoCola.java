package edu.unl.cc;
//Cola FIFO el primer tikect en entar es el primero en salir 
public class NodoCola {
    Ticket ticket;
    NodoCola siguiente;

    public NodoCola(Ticket ticket) {
        this.ticket = ticket;
    }
}

