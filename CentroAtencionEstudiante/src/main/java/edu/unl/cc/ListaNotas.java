package edu.unl.cc;

public class ListaNotas {
    private Nota cabeza;

    // Inserta una nueva nota al inicio de la lista
    public void insertarInicio(String texto) {
        Nota nueva = new Nota(texto);
        nueva.siguiente = cabeza;
        cabeza = nueva;
    }

    //Si la cabeza de la nota esta vacia retorna falso
    public boolean eliminar(String texto) {
        if (cabeza == null) return false;
        if (cabeza.texto.equals(texto)) {
            cabeza = cabeza.siguiente;
            return true;
        }

        Nota actual = cabeza;
        while (actual.siguiente != null && !actual.siguiente.texto.equals(texto)) {
            actual = actual.siguiente;
        }

        if (actual.siguiente != null) {
            actual.siguiente = actual.siguiente.siguiente;
            return true;
        }
        return false;
    }

    // Muestra todas las notas con un número secuencial
    public void listarConNumeros() {
        Nota actual = cabeza;
        if (actual == null) {
            System.out.println("No hay notas registradas");
            return;
        }
        int numero = 1;
        while (actual != null) {
            System.out.println(numero + ". " + actual.texto);
            actual = actual.siguiente;
            numero++;
        }
    }
    
    // Elimina una nota según su posición en la lista
    public boolean eliminarPorNumero(int numero) {
        if (cabeza == null || numero <= 0) return false;

        if (numero == 1) { // eliminar la cabeza
            cabeza = cabeza.siguiente;
            return true;
        }

        Nota actual = cabeza;
        int contador = 1;
        while (actual.siguiente != null && contador < numero - 1) {
            actual = actual.siguiente;
            contador++;
        }

        if (actual.siguiente != null) {
            actual.siguiente = actual.siguiente.siguiente;
            return true;
        }
        return false;
    }

    // Metodo para deevolver todas las notas como una lista de Strings (para guardar en CSV)
    public java.util.List<String> obtenerNotasComoLista() {
        java.util.List<String> lista = new java.util.ArrayList<>();
        Nota actual = cabeza;
        while (actual != null) {
            lista.add(actual.texto);
            actual = actual.siguiente;
        }
        return lista;
    }

    // Agrega una nota (usado al cargar desde CSV)
    public void agregarNota(String texto) {
        insertarInicio(texto);
    }

}
