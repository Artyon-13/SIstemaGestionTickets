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

    //Metodo para convertir el nuestro ticket a CSV haciendo uso de las , ya que asi se manejan los archivos CSV
    public String toCSV() {
        return id + "," + estudiante + "," + cedula + "," + tipoTramite + "," + estado;
    }

}


