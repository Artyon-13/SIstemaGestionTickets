package edu.unl.cc;

public class Ticket {
    int id;
    String estudiante;
    String cedula;
    String tipoTramite;
    EstadoTicket estado;
    ListaNotas notas;
//Creamos un constructor y un metodo para que muestre la informacion
    public Ticket(int id, String estudiante, String cedula, String tipoTramite) {
        this.id = id;
        this.estudiante = estudiante;
        this.cedula = cedula;
        this.tipoTramite = tipoTramite;
        this.estado = EstadoTicket.En_Cola;
        this.notas = new ListaNotas();


    }

    public void mostrarInfo() {
        System.out.println("Ticket #" + id + "CI: " + cedula + "|" + estudiante + "|" + tipoTramite + "| Estado: " + estado);
    }

}


