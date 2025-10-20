package edu.unl.cc;

public class Ticket {
    int id;
    String estudiante;
    String tipoTramite;
    EstadoTicket estado;
    ListaNotas notas;

    public Ticket(int id, String estudiante, String tipoTramite) {
        this.id = id;
        this.estudiante = estudiante;
        this.tipoTramite = tipoTramite;
        this.estado = EstadoTicket.En_Cola;
        this.notas = new ListaNotas();

    }
//Creamos un constructor y un metodo para que muestre la informacion
    public void mostrarInfo() {
        System.out.println("Ticket #" + id + "|" + estudiante + "|" + tipoTramite + "| Estado: " + estado);
    }

}

