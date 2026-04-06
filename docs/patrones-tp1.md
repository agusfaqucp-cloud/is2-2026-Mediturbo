# Patrones de Diseño - TP1

## Patrón 1: Strategy

### Intención
Permitir definir diferentes algoritmos de asignación de turnos y poder intercambiarlos sin modificar el sistema principal.

### Problema en el sistema
El sistema necesita asignar turnos a pacientes. Esta lógica puede variar según distintos criterios (disponibilidad, prioridad, especialidad, etc.).  
Si se implementara directamente en una clase con condicionales, el código sería rígido y difícil de mantener.

### Justificación de la elección
Se eligió el patrón Strategy para desacoplar la lógica de asignación del resto del sistema.  
Esto permite cambiar o agregar nuevas estrategias sin modificar el código existente.

### Implementación en el sistema
- Interfaz: `EstrategiaAsignacion`
- Implementación concreta: `AsignacionPorDisponibilidad`
- Contexto: `GestorTurnos`

El `GestorTurnos` utiliza una estrategia para crear turnos, delegando la lógica de asignación.

## Patrón 2: Observer

### Intención
Permitir que múltiples objetos sean notificados automáticamente cuando ocurre un cambio en otro objeto.

### Problema en el sistema
Cuando un turno cambia de estado (confirmado, cancelado, atendido), el sistema debe notificar a otros componentes (por ejemplo, servicios de notificación).  
Sin este patrón, la lógica quedaría acoplada dentro de la clase Turno.

### Justificación de la elección
Se utilizó Observer para desacoplar la gestión de eventos del objeto principal.  
Esto permite agregar nuevos observadores sin modificar la clase Turno.

### Implementación en el sistema
- Subject: `Turno`
- Observer: `Observer` (interfaz)
- Implementación: `ServicioNotificacion`

El `Turno` notifica automáticamente a sus observadores cuando cambia su estado.

## Conclusión

Se aplicaron los patrones Strategy y Observer para mejorar:

- Desacoplamiento del sistema
- Flexibilidad ante cambios
- Mantenibilidad del código

El sistema permite evolucionar fácilmente sin necesidad de modificar estructuras existentes.
