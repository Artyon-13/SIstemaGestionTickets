# SIstemaGestionTickets

Desarrollar un módulo de consola robusto y reproducible que persista datos entre ejecuciones, aplique un catálogo de estados con transiciones
válidas, genere consultas/reportes útiles y soporte atención prioritaria (cola de urgentes), integrando SLL, Stack y Queue.

                                                       MANUAL DE INSTRUCCIONES

Menú de Opciones

-----Modulo de Atencion CAE----
1. Nuevo Ticket || Se agrega un nuevo ticket de un estudiante, el ticket puede ser normal o urgente
2. Atender siguiente || Se atienden los tickets conforme al orden de llegada pero si es urgente tendra prioridad
3. Agregar Nota || Ingreso de anotaciones al ticket en atención
4. Eliminar Nota || Permite eliminar notas que fueron agregadas por error
5. Deshacer ultimo cambio || Permite eliminar un cambio reciente
6. Rehacer ultimo cambio || Permite rehacer un cambio que fue deshecho
7. Finalizar Atencion || Al finalizar la atencion se permiten lo siguiente:

    Seleccione el estado final del ticket:
    1. Completado - Ticket finalizado exitosamente
    
    2. Pendiente - El ticket quedó incompleto o falta documentación -> Si el ticket es marcado como pendiente será dirigido a la cola de tickets pendientes y se podran visualizar
       las notas registradas
       
9. Ver tickets en espera || Permite visualizar los tickets urgentes y normales que estan esperando atención

10. Ver historial de ticket actual || Permite visualizar las notas que tiene el ticket en atención

11. Buscar ticket || Muestra una lista de los tickets donde se puede ver informacion detallada del ticket seleccionado , ademas de tener dos opciones:

        1. Ver notas - Visualización de todas las notas del tickets

        2. Eliminar ticket 

12. Consultar tickets por estado
         Seleccione el estado:
    
          1. En Espera

          2. En Atencion

          3. En_Proceso

          4. Pendiente_DOCS

          5. Completado
    
13. Ver tickets pendientes || Muestra una lista de los tickets pendientes

0. Salir


