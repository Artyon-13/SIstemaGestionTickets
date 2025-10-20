# SIstemaGestionTickets

Este proyecto implementa el módulo de gestión de tickets para el Centro de Atención al Estudiante (CAE), permitiendo la recepción, atención y finalización de trámites (certificados, constancias, homologaciones, etc.).
El módulo garantiza un flujo de trabajo organizado con funcionalidades de deshacer/rehacer y un historial de observaciones por ticket y se basa en una estricta separación entre la lógica de dominio y la interacción con los datos de entrada y salida. 

### [cite\_start]Estructuras de Datos Propias Requeridas [cite: 18]

| Estructura | Nombre de la Clase (Ejemplo) | Propósito y Regla |
| :--- | :--- | :--- |
| **Cola (Queue)** | `TicketQueue` | [cite\_start]Almacena y gestiona los **casos en espera**[cite: 19]. [cite\_start]**FIFO (First-In, First-Out)**[cite: 19]. |
| **Pila (Stack)** | `ActionStack` (para Undo) / `RedoStack` (para Redo) | [cite\_start]Registra acciones para la funcionalidad **Deshacer/Rehacer**[cite: 20]. **LIFO (Last-In, First-Out)**. |
| **Lista Enlazada Simple (SLL)** | `NoteSinglyLinkedList` | [cite\_start]Almacena el **historial de notas y observaciones** asociadas a un ticket[cite: 21]. [cite\_start]Permite inserción al inicio y eliminación por primera coincidencia[cite: 21]. |

### Acciones Registrables para Undo/Redo

[cite\_start]Se registran en la pila los **estados significativos** del caso [cite: 28][cite\_start], así como las acciones mínimas necesarias para un deshacer/rehacer significativo[cite: 46]:

  * [cite\_start]Añadir Nota[cite: 47].
  * [cite\_start]Eliminar Nota[cite: 47].
  * [cite\_start]Cambio de Estado[cite: 47].

-----

## II. Catálogo de Estados del Caso (Ticket)

[cite\_start]El estado de cada ticket es fundamental para el seguimiento[cite: 28]:

| Estado | Descripción |
| :--- | :--- |
| **EN COLA** | Recepción inicial. [cite\_start]El ticket está en la estructura de espera[cite: 13]. |
| **EN ATENCIÓN** | [cite\_start]Un agente está trabajando activamente en el caso[cite: 14]. [cite\_start]Permite registro de observaciones y cambios[cite: 14, 15]. |
| **EN PROCESO** | La resolución requiere acciones internas fuera del alcance inmediato del agente (p. ej., derivación). |
| **PENDIENTE DOCS** | La resolución está en pausa, esperando la entrega de documentación por parte del estudiante. |
| **COMPLETADO** | El trámite ha sido finalizado. [cite\_start]El historial del caso se conserva para consulta posterior[cite: 16, 29]. |

-----

## III. Casos Borde y Manejo de Errores

[cite\_start]Se ha priorizado el manejo de las siguientes situaciones para garantizar la robustez del sistema[cite: 27]:

  * **Estructuras Vacías:** Manejo seguro al intentar atender un caso cuando la cola (`TicketQueue`) está vacía.
  * [cite\_start]**Undo/Redo en Vacío:** Prevenir errores al intentar deshacer/rehacer sin que existan acciones previas registradas en las pilas[cite: 27].
  * [cite\_start]**Eliminación de Notas Inexistentes:** Gestión de referencias segura al intentar eliminar una nota que no se encuentra en la `NoteSinglyLinkedList`[cite: 27, 43].
  * [cite\_start]**Integridad Post-Finalización:** Asegurar que los casos en estado **COMPLETADO** no puedan ser alterados por nuevas acciones o por la funcionalidad de `Undo/Redo`[cite: 29].
  * [cite\_start]**Cambio de Estado Sin Atención:** Evitar cambios de estado si no hay un caso activo siendo atendido por el agente[cite: 27].

-----

## IV. Guía de Compilación y Ejecución

[cite\_start]El proyecto fue desarrollado utilizando la biblioteca estándar de Java[cite: 48].

### 1\. Requisitos

  * Java Development Kit (JDK) 17 o superior.

### 2\. Ejecución

1.  **Compilar** las clases fuente (`.java`).
2.  **Ejecutar** la clase principal (por ejemplo, `MainConsole.java`).
    ```bash
    java MainConsole
    ```

### [cite\_start]3. Escenarios de Prueba (Evidencias) [cite: 36]

Para verificar el cumplimiento de los requerimientos, ejecute los siguientes pasos y tome capturas de la salida de consola:

1.  **Prueba de la Cola (FIFO):**
      * Registrar 3-5 nuevos casos.
      * Consultar la lista de casos en espera para mostrar el orden de llegada (Evidencia **a**).
2.  **Prueba de Historial y Estado Final:**
      * Atender 2 casos de forma completa, asegurando que pasen al estado **COMPLETADO**.
      * Registrar múltiples notas para cada uno.
      * Consultar el historial (notas y estado final) de ambos casos (Evidencia **b**).
3.  **Prueba de Undo/Redo:**
      * Atender un caso.
      * Realizar varias acciones consecutivas (ej. Añadir Nota $\to$ Cambiar Estado $\to$ Eliminar Nota).
      * Usar `Undo` (deshacer) para revertir las acciones.
      * Usar `Redo` (rehacer) para restaurar las acciones deshechas (Evidencia **c**).
