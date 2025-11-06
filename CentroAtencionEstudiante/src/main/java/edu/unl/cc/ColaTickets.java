package edu.unl.cc;
// Cola de tickets implementada con lista enlazada para insertar, sacar, verificar y listar tickets
public class ColaTickets {
    public NodoCola frente;
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

            System.out.print("Ticket #" + String.format("%02d", actual.ticket.numeroEnCola));
            System.out.println(" CI: " + actual.ticket.cedula + " | " + actual.ticket.estudiante + " | " + actual.ticket.tipoTramite + " | Estado: " + actual.ticket.estado);
            actual = actual.siguiente;
        }
    }

    public Ticket eliminarTicket (int id) {
        if (esVacia()) return null;

        NodoCola actual = frente;
        NodoCola anterior = null;

        while (actual != null) {
            if (actual.ticket.getId() == id) {  // Encontramos el ticket a eliminar
                // Si es el primero
                if (anterior == null) {
                    frente = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                // Si era el último
                if (actual == fin) {
                    fin = anterior;
                }
                return actual.ticket;
            }
            anterior = actual;
            actual = actual.siguiente;
        }

        return null; // Si no se encontró ningun ticket
    }
}


