package edu.unl.cc;
// Cola de tickets implementada con lista enlazada para insertar, sacar, verificar y listar tickets
public class ColaTickets {
    private NodoCola frente;
    private NodoCola fin;
    public boolean esVacia() {
        return frente == null;
    }
    public void insertar(Ticket ticket) {
        NodoCola nuevo = new NodoCola(ticket);
        if (esVacia()) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
    }

    public Ticket sacar() {
        if (esVacia()) return null;
        Ticket ticket = frente.ticket;
        frente = frente.siguiente;
        if  (frente == null) fin = null;
        return ticket;
    }

    public void listar (){
        if (esVacia()) {
            System.out.println("----------------------------------");
            System.out.println("No existen tickets en espera");
            System.out.println("----------------------------------");
            return;
        }

        NodoCola actual = frente;
        System.out.println("----------------------------------");
        System.out.println("Tickets en espera:");
        System.out.println("----------------------------------");
        while (actual != null){
            actual.ticket.mostrarInfo();
            actual = actual.siguiente;
        }
    }
}


