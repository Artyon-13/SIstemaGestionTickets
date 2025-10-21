package edu.unl.cc;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestorTickets {
    private ColaTickets cola = new ColaTickets();
    private PilaAcciones undo = new PilaAcciones();
    private PilaAcciones redo = new PilaAcciones();
    private int id = 1;
    private Ticket enAtencion = null;
    private Scanner scanner = new Scanner(System.in);
    private List<Ticket> historial = new ArrayList<>();

    public void menu(){
        int opcion;
           
        do {
            //Interfaz del Usuario principal 
            System.out.println("-----Modulo de Atencion CAE----");
            System.out.println("1. Nuevo Ticket");
            System.out.println("2. Atender siguiente");
            System.out.println("3. Agregar Nota");
            System.out.println("4. Eliminar Nota");
            System.out.println("5. Deshacer ultimo cambio");
            System.out.println("6. Rehacer ultimo cambio");
            System.out.println("7. Finalizar Atencion");
            System.out.println("8. Ver tickets en espera");
            System.out.println("9. Ver historial de ticket actual");
            System.out.println("10. Buscar ticket");
            System.out.println("0. Salir");
            System.out.print("Opcion: ");

            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> insertarTicket();
                case 2 -> atenderTicket();
                case 3 -> agregarNota();
                case 4 -> eliminarNota();
                case 5 -> deshacer();
                case 6 -> rehacer();
                case 7 -> finalizarAtencion();
                case 8 -> cola.listar();
                case 9 -> verHistorial();
                case 10 -> buscarTicket();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opcion invalida");
            }
        } while (opcion != 0);
    }

    private void insertarTicket() {
        System.out.print("Nombre del estudiante: ");
        String nombre = scanner.nextLine();
        System.out.print("Tipo de Tramite: ");
        String tipoTramite = scanner.nextLine();
        Ticket ticket = new Ticket(id++, nombre, tipoTramite);
        cola.insertar(ticket);
        System.out.println("Ticket #" + ticket.id + " agregado correctamente");
        historial.add(ticket);
    }

    private void atenderTicket() {
        if (enAtencion != null) {
            System.out.println("Ya hay un ticket en atencion");
            return;
        }
        //Saca el ticket siguinte de la cola FIFO
        enAtencion = cola.sacar();
        if (enAtencion == null) {
            System.out.println("No hay tickets en espera...");
            return;
        }
        enAtencion.estado = EstadoTicket.En_Atencion;
        undo.limpiar();
        redo.limpiar();
        System.out.println("Atendiendo ticket:");
        enAtencion.mostrarInfo();
    }
    private void agregarNota() {
        if (enAtencion == null) {
            System.out.println("No hay ticket en atencion");
            return;
        }
        System.out.print("Ingrese una nota: ");
        String texto = scanner.nextLine();
        enAtencion.notas.insertarInicio(texto);
        undo.push("Agrego nota: " + texto); //Guarda accion  para deshacer 
        redo.limpiar(); //Limpia redo al hacer una nueva accion 
        System.out.println("Nota agregada correctamente");
    }
    private void eliminarNota() {
        if (enAtencion == null) {
            System.out.println("No hay ticket en atencion");
            return;
        }
        System.out.print("Ingrese una nota a eliminar: ");
        String texto = scanner.nextLine();
        if (enAtencion.notas.eliminar(texto)) {
            undo.push("Elimino nota: " + texto);
            redo.limpiar();
            System.out.println("Nota eliminada correctamente");
        } else {
            System.out.println("Nota no encontrada");
        }
    }
    private void deshacer() {
        if (enAtencion == null) {
            System.out.println("No hay ticket en atencion");
            return;
        }
        
        String accion = undo.pop(); // Obtiene la ulma accion 
        if (accion == null) {
            System.out.println("No hay acciones para deshacer");
            return;
        }
        // Logica para revertir la  accion 
        if (accion.startsWith("Agrego nota: ")) {
            String texto = accion.substring(13);
            enAtencion.notas.eliminar(texto);
            redo.push("Agrego nota: " + texto); // Prepara para rehacer 
            System.out.println("Se deshizo correctamente la adicion de la nota");
        } else if (accion.startsWith("Eliminar: ")) {
            String texto = accion.substring(13);
            enAtencion.notas.insertarInicio(texto);
            redo.push("Elimino nota: " + texto);
            System.out.println("Se deshizo correctamente la eliminacion de la nota");
        }
    }

    public void rehacer (){
        if (enAtencion == null) {
            System.out.println("No hay ticket en atencion");
            return;
        }
        String accion = redo.pop(); //Obtiene la accion para rehacer
        if (accion == null){
            System.out.println("No hay acciones para rehacer");
            return;
        }
        // Re-ejecuta la acción previamente deshecha
        if (accion.startsWith("Agrego nota: ")) {
            String texto = accion.substring(13);
            enAtencion.notas.insertarInicio(texto);
            undo.push("Agrego nota: " + texto);
            System.out.println("Se rehicieron correctamente los cambios de la nota");
        } else if (accion.startsWith("Eliminar: ")) {
            String texto = accion.substring(13);
            enAtencion.notas.eliminar(texto);
            undo.push("Elimino nota: " + texto);
            System.out.println("Se rehizo correctamente la eliminacion de la nota");
        }
    }

    private void finalizarAtencion() {
        if (enAtencion == null) {
            System.out.println("No hay ticket en atencion");
            return;
        }
       enAtencion.estado = EstadoTicket.Completado;
       System.out.println("Ticket finalizado correctamente");
       enAtencion.mostrarInfo();
       System.out.println("Notas registradas:");
       enAtencion.notas.listar();
       enAtencion = null; //Libera el tikect actual 
       undo.limpiar(); //Limpia pilas de acciones 
       redo.limpiar();
    }

    private void verHistorial(){
        if (enAtencion == null) {
            System.out.println("No hay ticket en atencion");
            return;
        }
        System.out.println("Notas del ticket actual:");
        enAtencion.notas.listar();
    }

    private void buscarTicket(){
        System.out.println("-------Lista de tickets registrados-------");
        for (Ticket ticket : historial) {
            ticket.mostrarInfo();
        }
        System.out.print("Numero de ticket para mas detalles: ");
        int numeroTicket = scanner.nextInt();
        scanner.nextLine();
        // Busca de forma lineal en el historial 
        for (Ticket ticket : historial) {
            if (ticket.id == numeroTicket) {
                ticket.mostrarInfo();
                System.out.println("Notas registradas:");
                ticket.notas.listar();
                return;
            }
        }
        System.out.println("Ticket no encontrado");
    }

}



