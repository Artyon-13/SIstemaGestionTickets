package edu.unl.cc;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestorTickets {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    private ColaTickets cola = new ColaTickets();
    private ColaTickets colaUrgente = new ColaTickets();
    private ColaTickets colaPendientes = new ColaTickets();
    private PilaAcciones undo = new PilaAcciones();
    private PilaAcciones redo = new PilaAcciones();
    private int id = 1;
    private int siguienteNumeroEnCola = 1;
    private Ticket enAtencion = null;
    private Scanner scanner = new Scanner(System.in);
    private List<Ticket> historial = new ArrayList<>();
    private static final String ARCHIVO_CSV = "tickets.csv";


    public GestorTickets() {
        // Cargar los tickets existentes (si esque hay)
        historial = cargarTicketsCSV(ARCHIVO_CSV);
        cargarNotasCSV(historial, "notas.csv");

        // Restaura los tickets que siguen con su estad en cola
        for (Ticket t : historial) {
            if (t.estado == EstadoTicket.En_Cola) {
                cola.insertar(t);
            }
        }

        // este apartado hace que desde el ultimo ticket creado se almacene su id y al momento de crear otro este id vaya aumentado por lo que al volver a cargar el programa se vea esto
        if (!historial.isEmpty()) { // aqui se verifica si existen tickets anteriores
            id = historial.get(historial.size() - 1).id + 1; // en este apartado tenemos para obtener el ultimo ticket de la lista
                                                         // ticket.size nos devuelve los elementos que contiene la lista en este cado de los tickets
                                                         // y como se sabe en java los elementos de las listas empiezan en 0 entonces por eso ponemos - 1
                                                        // y por ultimo el id + 1 para que al momento de tener un nuevo ticket este id vaya creciendo y sea consecutivo
        }
    }

    public void menu(){
        int opcion;

        do {
            //Interfaz del Usuario principal
            System.out.println("*********************************");
            System.out.println(PURPLE + "-----Modulo de Atencion CAE----" + RESET);
            System.out.println("1. Nuevo Ticket");
            System.out.println("2. Atender siguiente ticket");
            System.out.println("3. Agregar Nota");
            System.out.println("4. Eliminar Nota");
            System.out.println("5. Deshacer ultimo cambio");
            System.out.println("6. Rehacer ultimo cambio");
            System.out.println("7. Finalizar Atencion");
            System.out.println("8. Ver tickets en la cola");
            System.out.println("9. Ver historial de notas del ticket actual");
            System.out.println("10. Historial de tickets");
            System.out.println("11. Consultar tickets por estado");
            System.out.println("12. Ver tickets pendientes");
            System.out.println("0. Salir");
            opcion = pedirOpcionMenu();

            //Ejecuta una accion dependiendo de la opcion que seleccionen
            switch (opcion) {
                case 1 -> insertarTicket();
                case 2 -> atenderTicket();
                case 3 -> agregarNota();
                case 4 -> eliminarNota();
                case 5 -> deshacer();
                case 6 -> rehacer();
                case 7 -> finalizarAtencion();
                case 8 -> listarTicketsAgregados();
                case 9 -> verHistorial();
                case 10 -> buscarTicket();
                case 11 -> consultarPorEstado();
                case 12 -> verPendientes();
                case 0 -> { System.out.println("Gracias por usar el modulo de atencion CAE"); System.out.println("Adios..."); System.exit(0);
                }

                default -> System.out.println("Opcion invalida");
            }
        } while (opcion != 0);
    }

    // Solicita y valida una opción de menu (0-10)
    private int pedirOpcionMenu() {
        while (true) {
            System.out.print("Opcion (0-12): ");
            String linea = scanner.nextLine();
            if (linea == null) {
                System.out.println(RED + "Entrada inválida: ingrese un número entre 0 y 12." + RESET);
                continue;
            }
            linea = linea.trim();
            // Solo dígitos (rechaza letras, símbolos, decimales con punto/coma, signos)
            if (!linea.matches("\\d+")) {
                System.out.println(RED + "Entrada inválida: solo se permiten dígitos (0-12)." + RESET);
                continue;
            }

            try {
                int opt = Integer.parseInt(linea);
                if (opt >= 0 && opt <= 12) {
                    return opt;
                } else {
                    System.out.println(RED + "Opción inválida: debe estar entre 0 y 12." + RESET);
                }
            } catch (NumberFormatException e) {

                System.out.println(RED + "Entrada inválida: error al interpretar el número." + RESET);
            }
        }
    }

    // Crea un nuevo ticket y lo agrega a la cola y al historial
    private void insertarTicket() {
        System.out.println("\n===================================================");
        System.out.print(GREEN +"Nombre del estudiante: "+ RESET);
        String nombre = scanner.nextLine();
        String cedula = pedirCedulaValida();

        // Verifica si ya existe un ticket con la misma cédula
        Ticket ticketExistente = existeTicketCedula(cedula);
        if (ticketExistente != null) {
            if (!ticketExistente.estudiante.equalsIgnoreCase(nombre)){
                System.out.println(RED + "La cedula ingresada se encuentra registrada con otro nombre (" + ticketExistente.estudiante + ")" + RESET);
            }
            return;
        }
        System.out.print(GREEN +"Tipo de Tramite: " + RESET);
        String tipoTramite = scanner.nextLine();
        System.out.print("\n");
        System.out.println("¿El ticket es urgente? (s/n):   ");
        boolean esUrgente = scanner.nextLine().trim().equalsIgnoreCase("s");
        System.out.println("\n===================================================");
        Ticket ticket = new Ticket(id++, nombre, cedula, tipoTramite, esUrgente);
        historial.add(ticket); // Guardamos también en la lista persistente
        guardarTicketsCSV(historial, ARCHIVO_CSV); // Se guarda automáticamente en la lista y en el archivo csv
        if (esUrgente) {
            colaUrgente.insertar(ticket);
            System.out.println(CYAN + "Ticket urgente #" + ticket.id + " agregado correctamente" + RESET);
        }else{
            cola.insertar(ticket);
            System.out.println(CYAN +"Ticket normal #" + ticket.id + " agregado correctamente" + RESET);  // para verificar si el archivo de guardo de manera correcta

        }
    }

    // Atiende el siguiente ticket de la cola
    private void atenderTicket() {
        System.out.println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        if (enAtencion != null) {
            System.out.println(CYAN + "Ya hay un ticket en atención. Finalice la atención actual primero." + RESET);
            return;
        }

        // Prioridad 1: Atender ticket urgente
        if (!colaUrgente.esVacia()) {
            enAtencion = colaUrgente.sacar();
            System.out.println(RED + "------ Atendiendo ticket urgente ------" + RESET);
        }
        // Prioridad 2: Si no hay urgentes, atender ticket normal
        else if (!cola.esVacia()) {
            enAtencion = cola.sacar();
            System.out.println(CYAN + "------ Atendiendo ticket normal ------" + RESET);
        }
        // Prioridad 3: Si no hay normales, atender ticket pendiente
        else if (!colaPendientes.esVacia()) {
            enAtencion = colaPendientes.sacar();
            System.out.println(YELLOW + "------ Atendiendo ticket pendiente -----" + RESET);
        }

        // Si no hay tickets en ninguna cola
        if (enAtencion == null) {
            System.out.println(CYAN + "No hay tickets en espera..." + RESET);
            return;
        }

        // Cambiar estado y preparar entorno
        enAtencion.estado = EstadoTicket.En_Atencion;
        undo.limpiar();
        redo.limpiar();

        // Mostrar ticket en atención
        enAtencion.mostrarInfo();
    }

    // Permite agregar notas en el ticket
    private void agregarNota() {
        System.out.println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::");
        if (enAtencion == null) {
            System.out.println(CYAN +"No hay ticket en atencion"+ RESET);
            return;
        }
        System.out.print(BLUE +"Ingrese una nota: "+ RESET);
        String texto = scanner.nextLine();
        enAtencion.notas.insertarInicio(texto);
        undo.push("Agrego nota: " + texto); //Guarda accion  para deshacer
        redo.limpiar(); //Limpia redo al hacer una nueva accion
        guardarNotasCSV(historial, "notas.csv"); // guarda la nota en el archivo csv despues aver sido creada
        System.out.println(BLUE +"Nota agregada correctamente"+ RESET);
    }

    // Elimina una nota específica del ticket en atención
    private void eliminarNota() {
        System.out.println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::");
        if (enAtencion == null) {
            System.out.println(CYAN + "No hay ticket en atencion" + RESET);
            return;
        }

        System.out.println("Notas registradas:");
        enAtencion.notas.listarConNumeros(); // Mostrar con números

        while (true) {
            System.out.print("Ingrese el número de la nota a eliminar (solo dígitos): ");
            String linea = scanner.nextLine();
            if (linea == null || linea.trim().isEmpty()) {
                System.out.println(RED + "Entrada vacía: ingrese el número de la nota." + RESET);
                continue;
            }

            linea = linea.trim();
            // Solo dígitos: rechaza letras, símbolos, decimales, signos, etc.
            if (!linea.matches("\\d+")) {
                System.out.println(RED + "Entrada inválida: solo se permiten números (sin letras ni símbolos)." + RESET);
                continue;
            }

            int numeroNota;
            try {
                numeroNota = Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println(RED + "Número inválido (demasiado grande)." + RESET);
                continue;
            }

            if (enAtencion.notas.eliminarPorNumero(numeroNota)) {
                undo.push("Eliminar nota #" + numeroNota);
                redo.limpiar();
                guardarNotasCSV(historial, "notas.csv"); // guarda la nota despues de elimar osea que actualiza en caso de ser eliminada
                System.out.println(BLUE + "Nota eliminada correctamente" + RESET);
                break;
            } else {
                System.out.println(RED + "Número de nota inválido. Intente de nuevo." + RESET);
                // sigue el bucle para permitir reintentos
            }
        }
    }

    private String pedirCedulaValida() {
        String cedula;
        while (true) {
            System.out.print(YELLOW + "Cedula del estudiante: " + RESET);
            cedula = scanner.nextLine();

            // Verifica que sean 10 dígitos
            if (!cedula.matches("\\d{10}")) {
                System.out.println(RED + "Error: La cédula debe tener exactamente 10 dígitos." + RESET);
                continue;
            }
/*
            if (existeTikectActivo(cedula)) {
                System.out.println(RED + "Error: Usted cuenta con un ticket en espera." + RESET);
                continue;
            }
            */

            return cedula;
        }
    }


    private void deshacer() {
        System.out.println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::");
        if (enAtencion == null) {
            System.out.println(CYAN +"No hay ticket en atencion"+ RESET);
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
            System.out.println(BLUE +"Se deshizo correctamente la adicion de la nota"+ RESET);
        } else if (accion.startsWith("Eliminar: ")) {
            String texto = accion.substring(13);
            enAtencion.notas.insertarInicio(texto);
            redo.push("Elimino nota: " + texto);
            System.out.println(BLUE +"Se deshizo correctamente la eliminacion de la nota"+ RESET);
        }
        guardarNotasCSV(historial, "notas.csv");
    }

    public void rehacer (){
        System.out.println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::");
        if (enAtencion == null) {
            System.out.println(CYAN +"No hay ticket en atencion"+ RESET);
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
            System.out.println(BLUE +"Se rehicieron correctamente los cambios de la nota"+ RESET);
        } else if (accion.startsWith("Eliminar: ")) {
            String texto = accion.substring(13);
            enAtencion.notas.eliminar(texto);
            undo.push("Elimino nota: " + texto);
            System.out.println(BLUE +"Se rehizo correctamente la eliminacion de la nota"+ RESET);
        }
        guardarNotasCSV(historial, "notas.csv");
    }

    // Finaliza la atención del ticket actual y limpia las pilas de acciones
    private void finalizarAtencion() {
        System.out.println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::");
        if (enAtencion == null) {
            System.out.println("No hay ticket en atencion");
            return;
        }

        // Mostrar opciones de estado final
        System.out.println("Seleccione el estado final del ticket:");
        System.out.println("1. Completado - Ticket finalizado exitosamente");
        System.out.println("2. Pendiente - El ticket quedó incompleto o falta documentación");

        int opcionEstado;
        while (true) {
            System.out.print("Opción (1-2): ");
            String linea = scanner.nextLine().trim();
            if (linea.matches("[1-2]")) {
                opcionEstado = Integer.parseInt(linea);
                break;
            } else {
                System.out.println(RED + "Opción inválida. Debe ser 1 o 2." + RESET);
            }
        }

        // Asignar estado según la opción seleccionada
        switch (opcionEstado) {
            case 1 -> {
                enAtencion.estado = EstadoTicket.Completado;
                System.out.println(GREEN + "¡Ticket marcado como COMPLETADO!" + RESET);
            }
            case 2 -> {
                enAtencion.estado = EstadoTicket.Pendiente_DOCS;
                System.out.println(YELLOW + "Ticket marcado como PENDIENTE. Se lo enviará a la cola de pendientes." + RESET);
                // Reinserta el ticket en la cola de pendientes
                colaPendientes.insertar(enAtencion);
            }
        }

        // Mostrar información y notas del ticket finalizado
        enAtencion.mostrarInfo();
        System.out.println(BLUE + "Notas registradas:" + RESET);
        enAtencion.notas.listarConNumeros();

        // Libera el ticket actual y limpia las pilas
        enAtencion = null;
        undo.limpiar();
        redo.limpiar();

        // Guardar cambios en el archivo CSV
        guardarTicketsCSV(historial, ARCHIVO_CSV);
    }
    private void verPendientes() {
        System.out.println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println(YELLOW + "-------Tickets Pendientes-------" + RESET);

        if (colaPendientes.esVacia()) {
            System.out.println("----------------------------------");
            System.out.println("No hay tickets pendientes");
            System.out.println("----------------------------------");
            return;
        }

        // Mostrar tickets pendientes con numeros
        System.out.println("----------------------------------");
        System.out.println("Tickets Pendientes:");
        System.out.println("----------------------------------");

        NodoCola actual = colaPendientes.frente;
        int numero = 1;
        while (actual != null) {
            System.out.print(numero + ". ");
            actual.ticket.mostrarInfo();
            actual = actual.siguiente;
            numero++;
        }

        // Opciones para el usuario
        System.out.println("\n Selecione una Opcion:");
        System.out.println("1. Reencolar un ticket ");
        System.out.println("2. Finalizar un ticket ");
        System.out.println("3. Volver al menú principal");

        int opcion;
        while (true) {
            System.out.print("Opción (1-3): ");
            String linea = scanner.nextLine();
            if (linea.matches("[1-3]")) {
                opcion = Integer.parseInt(linea);
                break;
            } else {
                System.out.println(RED + "Opción inválida. Debe ser 1, 2 o 3." + RESET);
            }
        }

        switch (opcion) {
            case 1:
                reencolarTicketEspecifico();
                break;
            case 2:
                finalizarTicketPendiente();
                break;
            case 3:
                System.out.println("Volviendo al menú principal...");
                break;
        }
    }

    private void reencolarTicketEspecifico() {
        System.out.print("Número del ticket a reencolar: ");
        String linea = scanner.nextLine();

        if (!linea.matches("\\d+")) {
            System.out.println(RED + "Número inválido" + RESET);
            return;
        }

        int numeroTicket = Integer.parseInt(linea);

        // Buscar el ticket por número en la lista
        NodoCola actual = colaPendientes.frente;
        NodoCola anterior = null;
        int contador = 1;

        while (actual != null) {
            if (contador == numeroTicket) {
                // Encontramos el ticket, lo removemos de pendientes
                Ticket ticket = actual.ticket;

                if (anterior == null) {
                    colaPendientes.frente = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }

                // Cambiar el estado a En_Cola y asignamos un  nuevo numero
                ticket.estado = EstadoTicket.En_Cola;
                ticket.numeroEnCola = siguienteNumeroEnCola++;

                // Reencolar en la cola normal
                cola.insertar(ticket);
                System.out.println(GREEN + " Ticket reencolado correctamente" + RESET);
                return;
            }
            anterior = actual;
            actual = actual.siguiente;
            contador++;
        }

        System.out.println(RED + "No se encontró el ticket número " + numeroTicket + RESET);
    }

    private void finalizarTicketPendiente() {
        System.out.print("Número del ticket a finalizar: ");
        String linea = scanner.nextLine();

        if (!linea.matches("\\d+")) {
            System.out.println(RED + "Número inválido" + RESET);
            return;
        }

        int numeroTicket = Integer.parseInt(linea);

        // Buscar el ticket por número en la lista
        NodoCola actual = colaPendientes.frente;
        NodoCola anterior = null;
        int contador = 1;

        while (actual != null) {
            if (contador == numeroTicket) {
                // Encontramos el ticket, lo removemos de pendientes y lo marcamos como completado
                Ticket ticket = actual.ticket;

                if (anterior == null) {
                    // Es el primer nodo
                    colaPendientes.frente = actual.siguiente;
                } else {
                    // Es un nodo intermedio o final
                    anterior.siguiente = actual.siguiente;
                }

                // Marcar como completado
                ticket.estado = EstadoTicket.Completado;
                System.out.println(GREEN + " Ticket #" + String.format("%02d", ticket.numeroEnCola) + " finalizado como COMPLETADO" + RESET);
                ticket.mostrarInfo();
                return;
            }
            anterior = actual;
            actual = actual.siguiente;
            contador++;
        }

        System.out.println(RED + "No se encontró el ticket número " + numeroTicket +RESET);
    }

    // Muestra las notas del ticket en atención
    private void verHistorial(){
        System.out.println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::");
        if (enAtencion == null) {
            System.out.println(CYAN +"No hay ticket en atencion"+ RESET);
            return;
        }
        System.out.println("Notas del ticket actual:");
        enAtencion.notas.listarConNumeros();
    }

    //Busca un ticket por su id 
    private void buscarTicket(){
        int opcion;
        System.out.println("\n:::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println(CYAN +"-------Lista de tickets registrados-------"+ RESET);
        for (Ticket ticket : historial) {
            ticket.mostrarInfo();
        }
        System.out.print("Numero de ticket para visualizar: ");
        int numeroTicket = scanner.nextInt();
        scanner.nextLine();
        // Busca de forma lineal en el historial
        for (Ticket ticket : historial) {
            if (ticket.id == numeroTicket) {
                ticket.mostrarInfo();
                System.out.println("------ Elija una opcion ------ ");
                System.out.println("1. Ver notas");
                System.out.println("2. Eliminar ticket");
                System.out.print("Opcion (1-2): ");
                opcion = scanner.nextInt();
                scanner.nextLine();
                if (opcion == 1) {
                    System.out.println(BLUE +"Notas registradas:"+ RESET);
                    ticket.notas.listarConNumeros();
                    return;
                } else if (opcion == 2) {
                    historial.remove(ticket);
                    undo.limpiar();
                    redo.limpiar();
                    cola.eliminarTicket(numeroTicket);
                    guardarTicketsCSV(historial, ARCHIVO_CSV);
                    guardarNotasCSV(historial, "notas.csv");
                    System.out.println(CYAN +"Ticket eliminado correctamente"+ RESET);
                    return;
                }
                return;
            }
        }
        System.out.println(CYAN +"Ticket no encontrado"+ RESET);

    }

    // Verifica si existe un ticket con la cedula que ingreso
    private Ticket existeTicketCedula(String cedula){
        for (Ticket ticket : historial) {
            if (ticket.cedula.equals(cedula)) {
                return ticket;
            }
        }
        return null;
    }
// metodo para guardar los tickets en el archivo csv
    public void guardarTicketsCSV(List<Ticket> tickets, String nombreArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            // Cabecera del archivo
            writer.println("id,estudiante,cedula,tipoTramite,estado,esUrgente");

            // Escribir cada ticket
            for (Ticket t : tickets) {
                writer.println(t.id + "," + t.estudiante + "," + t.cedula + "," + t.tipoTramite + "," + t.estado + "," + t.esUrgente);
            }

            System.out.println("Tickets guardados correctamente en " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al guardar los tickets: " + e.getMessage());
        }
    }



// Metodo para cargar el archivo csv y vaya leyendo en la manera de que este esta compuesto que es por ","
    public List<Ticket> cargarTicketsCSV(String nombreArchivo) {
        List<Ticket> tickets = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            br.readLine(); // Saltar la cabecera

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 6) {
                    int id = Integer.parseInt(partes[0]);
                    String estudiante = partes[1];
                    String cedula = partes[2];
                    String tipoTramite = partes[3];
                    EstadoTicket estado = EstadoTicket.valueOf(partes[4]);
                    boolean esUrgente = Boolean.parseBoolean(partes[5]);

                    Ticket t = new Ticket(id, estudiante, cedula, tipoTramite, esUrgente);
                    t.estado = estado;
                    tickets.add(t);
                }
            }

            System.out.println("Tickets cargados correctamente desde " + nombreArchivo); // impresion para saber que los tickets se cargaron de manera correcta
        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo, se creará uno nuevo al guardar."); // en caso de que no tengamos el archivo se presentara para saber que el archivo no se encuentra, si ocurre esto entonces al momento de guardar un nuevo ticket nosotros este archivo se creara de manera automatica
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        return tickets;
    }

    // Metodo para guardar las notas de todos los tickets en un archivo CSV
    public void guardarNotasCSV(List<Ticket> tickets, String nombreArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {
            writer.println("ticket_id,nota"); // Cabecera del archivo CSV

            for (Ticket t : tickets) {
                List<String> notas = t.notas.obtenerNotasComoLista(); // obtiene las notas del ticket
                for (String nota : notas) {
                    writer.println(t.id + "," + nota.replace(",", " ")); // reemplaza comas para no romper el formato CSV
                }
            }

            System.out.println("Notas guardadas correctamente en " + nombreArchivo);
        } catch (IOException e) {
            System.out.println("Error al guardar las notas: " + e.getMessage());
        }
    }

    // Metodo para cargar las notas desde un archivo CSV y asignarlas a sus tickets
    public void cargarNotasCSV(List<Ticket> tickets, String nombreArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea;
            br.readLine(); // Saltar la cabecera

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",", 2); // solo dividir en 2 partes: id y nota
                if (partes.length == 2) {
                    int idTicket = Integer.parseInt(partes[0]);
                    String nota = partes[1];

                    // Buscar el ticket con el mismo ID y agregarle la nota
                    for (Ticket t : tickets) {
                        if (t.id == idTicket) {
                            t.notas.agregarNota(nota);
                            break;
                        }
                    }
                }
            }

            System.out.println("Notas cargadas correctamente desde " + nombreArchivo);
        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo de notas, se creará uno nuevo al guardar.");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo de notas: " + e.getMessage());
        }
    }

    private void consultarPorEstado(){
        System.out.println("\n-------------------------------");
        if (historial.isEmpty()){
            System.out.println(CYAN + "No existen tickects registrados" + RESET);
            return;
        }
        System.out.println(CYAN + "Seleccione el estado:" + RESET);
        System.out.println("1. En Espera");
        System.out.println("2. En Atencion");
        System.out.println("3. En_Proceso");
        System.out.println("4. Pendiente_DOCS");
        System.out.println("5. Completado");
        System.out.print("Opcion: ");

        String opcion = scanner.nextLine().trim();
        EstadoTicket estadoBuscado = null;

        switch (opcion){
            case "1" -> estadoBuscado = EstadoTicket.En_Cola;
            case "2" -> estadoBuscado = EstadoTicket.En_Atencion;
            case "3" -> estadoBuscado = EstadoTicket.En_Proceso;
            case "4" -> estadoBuscado = EstadoTicket.Pendiente_DOCS;
            case "5" -> estadoBuscado = EstadoTicket.Completado;
            default -> {
                System.out.println(RED + "Opcion no valida" + RESET);
                return;
            }
        }

        System.out.println(CYAN + "\nTickets con estado: " + estadoBuscado + RESET);
        boolean encontrado = false;

        for (Ticket ticket : historial){
            if (ticket.estado == estadoBuscado){
                ticket.mostrarInfo();
                encontrado = true;
            }
        }

        if (!encontrado){
            System.out.println(YELLOW + "No hay tickets con ese estado" + RESET);
        }
    }
    public void listarTicketsAgregados() {
        System.out.println("=============== Tickets Urgentes ==================");
        colaUrgente.listar();
        System.out.println("=============== Tickets Normales ==================");
        cola.listar();
    }

}
