package edu.unl.cc;
// Nodo que representa un elemento en la cola de tickets, con referencia al siguiente nodo
public class NodoCola {
    Ticket ticket;
    NodoCola siguiente;

    public NodoCola(Ticket ticket) {
        this.ticket = ticket;
    }
}


