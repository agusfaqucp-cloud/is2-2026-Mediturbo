# 📋 Matriz de Riesgos del Proyecto
## Sistema de Gestión de Turnos Médicos — Ingeniería en Software II

**Metodología:** Kanban  
**Equipo:** Olivera · Gomez Borjas · Fritz · Ibarra  
**Fecha:** Marzo 2026

---

## Escala de Valoración

**Nivel de Riesgo = Probabilidad × Impacto (escala 1–5)**

| Nivel | Rango P×I | Descripción |
|-------|-----------|-------------|
| 🟢 Bajo | 1 – 3 | Sin acción inmediata requerida |
| 🟡 Medio | 4 – 8 | Requiere monitoreo |
| 🟠 Alto | 9 – 14 | Requiere plan de acción concreto |
| 🔴 Crítico | 15 – 25 | Acción inmediata requerida |

---

## Riesgos Identificados

| # | Descripción del Riesgo | Categoría | Prob. | Impacto | P×I | Prioridad | Estrategia | Plan de Acción / Mitigación | Responsable | Estado |
|---|------------------------|-----------|:-----:|:-------:|:---:|-----------|------------|----------------------------|-------------|--------|
| 1 | Falta de disponibilidad de integrantes por carga académica simultánea | Equipo / Recursos | 3 | 4 | 12 | 🟠 ALTO | Mitigar | Definir WIP limit=1 por persona; redistribuir tareas si alguien no puede avanzar en 48 hs. Revisión en sincronización semanal. | Olivera (SM) | Activo |
| 2 | Conflictos de merge en ramas de desarrollo concurrente | Técnico / Código | 4 | 3 | 12 | 🟠 ALTO | Mitigar | Ramas cortas de vida <48 hs. Pull Requests obligatorios con revisión del Dev Lead antes de merge a main. Commits pequeños y frecuentes. | Gomez Borjas (Dev Lead) | Activo |
| 3 | Ambigüedad en los requisitos de los estados del turno (pendiente, confirmado, cancelado, atendido) | Requisitos | 3 | 4 | 12 | 🟠 ALTO | Mitigar | Documentar criterios de aceptación por estado antes de codificar. Validar con el docente en semana 2. | Olivera (SM) + Fritz (QA) | Activo |
| 4 | Diseño de UX inconsistente entre la vista del recepcionista y la del paciente | Diseño / UX | 3 | 3 | 9 | 🟠 ALTO | Mitigar | Ibarra define guía de estilos y componentes reutilizables en semana 1. Revisión cruzada antes de codificar pantallas. | Ibarra (UX Lead) | Activo |
| 5 | Deuda técnica acumulada por falta de pruebas automatizadas | Técnico / QA | 4 | 4 | 16 | 🔴 CRÍTICO | Mitigar | Fritz define casos de prueba por historia antes de implementar. Criterio de "listo" incluye prueba QA aprobada. Al menos smoke tests por endpoint. | Fritz (QA Lead) | Activo |
| 6 | Integración tardía entre front-end y back-end genera bloqueos al final del desarrollo | Técnico / Integración | 3 | 5 | 15 | 🔴 CRÍTICO | Mitigar | Acordar contrato de API (endpoints y payloads) en semana 2. Usar mocks en front-end mientras el back-end no está listo. Integración continua desde semana 3. | Gomez Borjas  + Ibarra | Activo |
| 7 | Alcance mal dimensionado: funcionalidades que exceden el tiempo disponible | Alcance / Planificación | 3 | 4 | 12 | 🟠 ALTO | Mitigar | Priorizar backlog con MoSCoW. Las funcionalidades "Could" solo entran si el Must y Should están terminados. Revisar alcance en cada sincronización. | Olivera (SM) | Activo |
| 8 | Pérdida de datos o código por falta de backup / push al repositorio | Técnico / Infraestructura | 2 | 5 | 10 | 🟠 ALTO | Mitigar | Todo el código vive en GitHub. Política: push obligatorio al finalizar cada sesión de trabajo. No trabajo local sin rama remota. | Todo el equipo | Activo |
| 9 | Notificaciones simuladas mal implementadas: lógica compleja que distrae del núcleo del sistema | Técnico / Funcionalidad | 3 | 2 | 6 | 🟡 MEDIO | Aceptar | Implementar notificaciones como alerta visual simple en pantalla (sin email real). Alcance mínimo acordado desde el inicio. |   Ibarra | Activo |
| 10 | Ausencia prolongada (enfermedad u otra) de un integrante clave | Equipo / Personas | 2 | 4 | 8 | 🟡 MEDIO | Contingencia | Documentación mínima de cada tarea en Trello (descripción + criterios de aceptación). Cualquier otro integrante puede tomar la tarea con contexto suficiente. | Olivera (SM) | Latente |
| 11 | Herramienta de gestión abandonada: tablero desactualizado | Proceso / Kanban | 3 | 3 | 9 | 🟠 ALTO | Mitigar | SM revisa el tablero antes de cada sincronización. Criterio de equipo: no se considera trabajo hecho si la tarjeta no está en "Listo". | Olivera (SM) | Activo |
| 12 | Falta de validación con usuarios reales (recepcionista / paciente) | Validación / Producto | 4 | 3 | 12 | 🟠 ALTO | Mitigar | Usar personas/escenarios definidos por el docente como proxy de usuario. Fritz valida flujos con casos de prueba basados en esos escenarios. | Fritz (QA) + Ibarra (UX) | Activo |

---

## Resumen Ejecutivo

| Prioridad | Cantidad | IDs |
|-----------|:--------:|-----|
| 🔴 Crítico | 2 | R05, R06 |
| 🟠 Alto | 8 | R01, R02, R03, R04, R07, R08, R11, R12 |
| 🟡 Medio | 2 | R09, R10 |
| 🟢 Bajo | 0 | — |
| **Total** | **12** | |

---

## Mapa de Calor

```

PROB/IMPACTO     1       2       3       4       5
             ┌───────┬───────┬───────┬───────┬───────┐
5 Casi seg.  │       │       │       │       │       │
             ├───────┼───────┼───────┼───────┼───────┤
4 Probable   │       │       │  (2)  │  (1)  │       │
             ├───────┼───────┼───────┼───────┼───────┤
3 Posible    │       │  (1)  │  (2)  │  (3)  │  (1)  │
             ├───────┼───────┼───────┼───────┼───────┤
2 Improb.    │       │       │       │  (1)  │  (1)  │
             ├───────┼───────┼───────┼───────┼───────┤
1 Rara       │       │       │       │       │       │
             └───────┴───────┴───────┴───────┴───────┘

(n) = cantidad de riesgos en ese nivel de probabilidad × impacto
```

---


---

*Documento — Ingeniería en Software II · Marzo 2026*


