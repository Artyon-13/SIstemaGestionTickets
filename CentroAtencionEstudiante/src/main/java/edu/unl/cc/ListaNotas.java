package edu.unl.cc;

public class ListaNotas {
    private  Nota cabeza;

    public void insertarInicio(String texto){
        Nota nueva = new Nota(texto);
        nueva.siguiente = cabeza;
        cabeza = nueva;
    }
    
//Si la cabeza de la nota esta vacia retorna falso
    public boolean eliminar(String texto){
        if(cabeza == null) return false;
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

    public void listar() {
        Nota actual = cabeza;
        if (actual == null){
            System.out.println("No hay notas registradas");
            return;
        }
        while (actual != null){
            System.out.println("- " + actual.texto);
            actual = actual.siguiente;
        }
    }
}

