# SIstemaGestionTickets

Este proyecto implementa el módulo de gestión de tickets para el Centro de Atención al Estudiante (CAE), permitiendo la recepción, atención y finalización de trámites (certificados, constancias, homologaciones, etc.).
El módulo garantiza un flujo de trabajo organizado con funcionalidades de deshacer/rehacer y un historial de observaciones por ticket y se basa en una estricta separación entre la lógica de dominio y la interacción con los datos de entrada y salida. 


### Acciones Registrables para Undo/Redo

Se registran en la pila los **estados significativos** del caso, así como las acciones mínimas necesarias para un deshacer/rehacer:

  * Añadir Nota
  * Eliminar Nota
  * Cambio de Estado

## II.Estados del Caso del Ticket

El estado de cada ticket es fundamental para el seguimiento:

| Estado | Descripción |
| :--- | :--- |
| **EN COLA** | El ticket está en la estructura de espera. |
| **EN ATENCIÓN** | Un agente está trabajando activamente en el caso.|
| **EN PROCESO** | La resolución requiere acciones internas fuera del alcance inmediato del sistema. |
| **PENDIENTE DOCS** | La resolución está en pausa, esperando la entrega de documentación por parte del estudiante. |
| **COMPLETADO** | El trámite ha sido finalizado. |

-----

## III. Casos Borde y Manejo de Errores

Se ha priorizado el manejo de las siguientes situaciones para garantizar la solidez del sistema.

  * **Estructuras Vacias:** Manejo seguro al intentar atender un caso cuando la cola está vacía.
  * **Undo/Redo en Vacio:** Prevenir errores al intentar deshacer/rehacer sin que existan acciones previas registradas en las pilas.
  * **Eliminacion de Notas Inexistentes:** Gestion de referencias seguras al intentar eliminar una nota que no se encuentre.
  * **Integridad Post-Finalización:** Asegurar que los casos en estado **COMPLETADO** no puedan ser alterados por nuevas acciones o por la funcionalidad de "Undo/Redo".
  * **Cambio de Estado Sin Atención:** Evitar cambios de estado si no hay un caso activo siendo atendido por el agente.

## IV. Escenarios de prueba


