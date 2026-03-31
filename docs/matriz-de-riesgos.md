#  Matriz de Riesgos del Proyecto
## Sistema de Gestión de Turnos Médicos — Ingeniería en Software II

**Metodología:** Kanban  
**Equipo:** Olivera · Gomez Borjas · Fritz · Ibarra  
**Fecha:** Marzo 2026

# MATRIZ DE RIESGO

| # | Riesgo | Categoría | Prob. | Impacto | P x I | Nivel | Estrategia | Plan de Acción | Plan de CONTINGENCIA (si ocurre) |
|---|--------|-----------|-------|---------|-------|-------|------------|----------------|----------------------------------|
| 1 | Falta de pruebas (deuda técnica), si el equipo escribe código sin probarlo, los errores se acumulan silenciosamente. Eso se llama "deuda técnica", como una deuda de dinero pero de calidad. Cuanto más tardamos en probarlo, más problemas acumulamos. | Técnico | 4 | 4 | 16 | 🔴 MUY ALTO | Mitigar | Definir tests antes de implementar y tener un Aseguramiento de Calidad | Hacer rollback de la funcionalidad afectada y dedicar un sprint exclusivo a corrección de bugs. |
| 2 | Problemas de integración front-back. Donde el front es lo que el usuario ve (botones, pantallas, colores). El back es el motor que funciona por detrás (base de datos, lógica, cálculos). Son dos equipos distintos que trabajan por separado y en algún momento tienen que "conectarse". Si no acordaron desde el principio cómo van a hablar entre sí, cuando llega ese momento nada encaja. | Técnico | 3 | 5 | 15 | 🔴 MUY ALTO | Mitigar | Definir API desde el inicio y usar mocks | Desacoplar módulos temporalmente y entregar el front con datos simulados (mocks) hasta resolver la integración. |
| 3 | Conflictos de merge en el código. Cuando varios programadores trabajan al mismo tiempo sobre el mismo archivo de código, ocurre lo mismo que cuando dos personas editan el mismo documento de Word a la vez: los cambios chocan. A eso se le llama "conflicto de merge". Si no se gestiona bien, un programador puede pisar el trabajo de otro sin darse cuenta. | Técnico | 4 | 3 | 12 | 🟠 ALTO | Mitigar | Usar ramas cortas y Pull Requests | Asignar al Dev Lead para resolver conflictos de forma inmediata y pausar nuevos commits hasta que el repositorio esté estable. |
| 4 | Falta de tiempo del equipo | Equipo | 3 | 4 | 12 | 🟠 ALTO | Mitigar | Redistribuir tareas | Renegociar el alcance con el cliente, reducir funcionalidades no críticas y priorizar el MVP. |
| 5 | Requisitos poco claros | Requisitos | 3 | 4 | 12 | 🟠 ALTO | Mitigar | Definir criterios y validar | Congelar el desarrollo de la funcionalidad afectada y convocar reunión de urgencia con el cliente para clarificar antes de continuar. |
| 6 | Tablero de tareas desactualizado | Proceso | 3 | 2 | 6 | 🟡 MEDIO | Mitigar | Revisar tablero en cada reunión | Reconstruir el tablero en la próxima reunión con todo el equipo y reasignar tareas faltantes. |
| 7 | Falta de comunicación en el equipo | Equipo | 2 | 3 | 6 | 🟡 MEDIO | Mitigar | Reuniones breves y seguimiento constante | Implementar canal de comunicación de emergencia (ej. grupo de WhatsApp o Slack urgente) y reunión inmediata para alinear al equipo. |
| 8 | Notificaciones mal implementadas | Técnico | 2 | 2 | 4 | 🟢 BAJO | Aceptar | Mantener solución simple | Deshabilitar el módulo de notificaciones temporalmente y entregar sin esa funcionalidad hasta corregirla en el siguiente sprint. |

---

### Referencia de Niveles

| Nivel | Rango P x I |
|-------|-------------|
| 🔴 MUY ALTO | 15 – 25 |
| 🟠 ALTO | 10 – 14 |
| 🟡 MEDIO | 5 – 9 |
| 🟢 BAJO | 1 – 4 |

