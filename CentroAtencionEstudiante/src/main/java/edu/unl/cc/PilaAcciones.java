package edu.unl.cc;
/**
 Implementa una pila de acciones mediante una lista enlazada, 
 con operaciones para agregar (push), retirar (pop), verificar si está vacía 
 y limpiar la pila.
 */
public class PilaAcciones {
    private NodoAccion tope;

    public void push (String accion){
        NodoAccion nuevo = new NodoAccion(accion);
        nuevo.siguiente = tope;
        tope = nuevo;
    }
    public String pop (){
        if (tope == null) return null;
        String accion = tope.accion;
        tope = tope.siguiente;
        return accion;
    }

    public boolean esVacia() {
        return tope == null;
    }

    public void limpiar (){
        tope = null;
    }
}
