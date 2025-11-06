package edu.unl.cc;

public class Ticket {
    int id;
    String estudiante;
    String cedula;
    String tipoTramite;
    boolean fueReencolado;
    int numeroEnCola;
    EstadoTicket estado;
    ListaNotas notas;
    public boolean esUrgente;
//Creamos un constructor y un metodo para que muestre la informacion
    public Ticket(int id, String estudiante, String cedula, String tipoTramite, boolean esUrgente) {
        this.id = id;
        this.estudiante = estudiante;
        this.cedula = cedula;
        this.tipoTramite = tipoTramite;
        this.estado = EstadoTicket.En_Cola;
        this.notas = new ListaNotas();
        this.esUrgente = esUrgente;
        this.fueReencolado = false;
        this.numeroEnCola = 0;

    }

    public int getId() {
        return id;
    }
    public void mostrarInfo() {
        String tipo = esUrgente ? "Urgente" : "Normal";
        System.out.println("Ticket #" + id + "| CI: " + cedula + "| Nombre: " + estudiante + "|Tramite: " + tipoTramite + "| Tipo: " + tipo + "| Estado: " + estado);
    }

}


