# Estrategia de Pruebas - MediTurnos

**Proyecto:** MediTurnos - Sistema de gestion de turnos medicos  
**Materia:** Ingenieria de Software II  



## Introduccion

La idea es tener en un solo lugar todo lo que tiene que ver con como probamos el sistema: que herramientas usamos, que casos de prueba implementamos, como pensamos escalar las pruebas en el futuro y que decisiones tomamos en el camino. Se va actualizando a medida que el proyecto avanza.



## Herramientas utilizadas

| Herramienta | Tipo | Uso en el proyecto | Estado |
|---|---|---|---|
| JUnit 5 | Pruebas unitarias | Verificar comportamiento de clases de dominio y persistencia | Implementado |
| Maven (mvn test) | Build y ejecucion de tests | Compilar y correr los tests desde terminal y CI/CD | Implementado |
| GitHub Actions | CI/CD | Ejecutar tests automaticamente en cada push a main | Implementado |
| Mockito | Mocking | Simular dependencias externas en pruebas futuras | Planificado |
| Pruebas manuales | Interfaz grafica | Las ventanas Swing se prueban manualmente, no son automatizables en servidor sin pantalla | Manual |



## Casos de prueba implementados

Todos los casos estan en `pruebas/unit/` y cubren dos clases del sistema: `GestorTurnos` y `PersistenciaTurnos`.

| # | Clase bajo prueba | Metodo | Tecnica | Entrada | Resultado esperado |
|---|---|---|---|---|---|
| 1 | `GestorTurnos` | `crearTurno()` | Equivalencia valida | Paciente "Juan Perez", especialidad "Cardiologia" | Turno con estado PENDIENTE |
| 2 | `GestorTurnos` | `crearTurno()` | Equivalencia invalida | Paciente con nombre vacio `""` | Nombre queda vacio, detectable como invalido |
| 3 | `Turno` | `cambiarEstado()` | Equivalencia valida | CONFIRMADO sobre turno PENDIENTE | Estado cambia a CONFIRMADO |
| 4 | `Turno` | `cambiarEstado()` | Equivalencia valida | CANCELADO sobre turno CONFIRMADO | Estado cambia a CANCELADO |
| 5 | `PersistenciaTurnos` | `cargarJSON()` | Valor limite | Archivo JSON con array vacio `[]` | Lista vacia sin excepcion |
| 6 | `PersistenciaTurnos` | `cargarJSON()` | Valor limite | Paciente con nombre de solo espacios `"   "` | Nombre invalido detectable con trim() |



## Plan de mocks para pruebas de integracion futuras

Las pruebas de integracion no estan implementadas todavia, estan planificadas para el trabajo integrador final cuando el sistema tenga mas modulos. Lo que si definimos es como se haria cuando llegue el momento.

### Dependencia 1 - Sistema de archivos

`PersistenciaTurnos` depende del sistema de archivos para leer y escribir `turnos.json`. En una prueba de integracion esto se mockeria con una implementacion en memoria que simule las operaciones sin tocar el disco real. Esto hace los tests mas rapidos y evita que queden archivos residuales despues de cada ejecucion.

### Dependencia 2 - Sistema de sesion

`SistemaSesion` guarda el usuario actual en un campo estatico compartido. En integracion esto es problematico porque el estado de una prueba puede contaminar la siguiente si no se limpia bien. Se resolveria con un stub que devuelva siempre un usuario fijo y controlado, limpiando el estado entre cada test con `@AfterEach`.

### Herramienta elegida: Mockito

Mockito es el framework de mocking estandar en Java. Se integra directo con JUnit 5 y la sintaxis es bastante clara incluso para alguien que lo usa por primera vez. Para nuestro proyecto lo usariamos principalmente para mockear `ServicioNotificacion` en los tests de `GestorTurnos`, verificando que el patron Observer funciona correctamente sin depender de la implementacion real del servicio.



## Flujo de prueba de extremo a extremo (conceptual)

El flujo E2E cubre el caso de uso principal: un administrativo inicia sesion y registra un turno.

```
1. Usuario ingresa credenciales de admin en LoginVentana
2. Sistema valida el rol y abre VentanaPrincipal con las opciones de admin
3. Usuario ingresa nombre del paciente y selecciona especialidad
4. Sistema ejecuta AsignacionPorDisponibilidad (patron Strategy)
5. Se crea el Turno con estado PENDIENTE
6. ServicioNotificacion recibe la notificacion (patron Observer)
7. El turno aparece en la tabla de VentanaPrincipal
8. Usuario guarda los turnos
9. PersistenciaTurnos escribe turnos.json en disco
10. Al reiniciar, el sistema carga los turnos desde el archivo automaticamente
```

Este flujo no esta automatizado todavia. Las pruebas unitarias actuales cubren los pasos 4, 5, 6 y 9 de forma aislada.



## Estrategia de regresion

Cada vez que se modifica una clase del dominio o de la capa de persistencia, los tests existentes se corren automaticamente a traves del pipeline de GitHub Actions. Si alguno falla, el commit queda marcado con una X roja en el repositorio indicando que hay una regresion que hay que resolver antes de seguir.

Para el futuro, la estrategia seria:

- Mantener los tests existentes sin modificarlos salvo que cambie la logica que prueban
- Agregar tests nuevos por cada funcionalidad nueva que se agregue al sistema
- Nunca reducir la cantidad de tests que pasan, solo sumarle
- Revisar la cobertura de codigo periodicamente usando el plugin de Surefire de Maven



## Plan de pruebas de estres (futuro)

Las pruebas de estres estan reservadas para el trabajo integrador final. Lo que definimos conceptualmente por ahora es lo siguiente:

**Escenario 1 - Carga masiva de turnos**  
Cargar un archivo `turnos.json` con 10.000 turnos y medir el tiempo que tarda el sistema en procesarlos y mostrarlos en la tabla. El tiempo aceptable deberia ser menor a 3 segundos.

**Escenario 2 - Concurrencia en la asignacion de medicos**  
Simular dos usuarios creando turnos para el mismo medico al mismo tiempo y verificar que el sistema no asigna al mismo medico dos veces. Este escenario expone directamente el bug de concurrencia que identificamos en el analisis de casos criticos del TP2.

**Escenario 3 - Archivo JSON corrupto**  
Cargar un archivo JSON con datos invalidos mezclados con datos validos y medir cuantos turnos corruptos llegan a la tabla sin que el sistema lo detecte. Este escenario esta relacionado con el bug de persistencia que ya documentamos.



## Relacion entre patrones de diseno y testabilidad

Una de las decisiones del TP1 que mas impacta en como podemos probar el sistema es la implementacion de los patrones Strategy y Observer.

**Patron Strategy** hace que `GestorTurnos` no dependa de una implementacion especifica de asignacion. Esto permite en los tests reemplazar `AsignacionPorDisponibilidad` por una estrategia falsa que devuelva siempre el mismo medico, haciendo los tests deterministas y predecibles sin depender de la logica real de asignacion.

**Patron Observer** hace que `ServicioNotificacion` este completamente desacoplado del dominio. Esto permite en tests futuros mockear el observer con Mockito y verificar que fue notificado exactamente una vez cuando se crea un turno, sin ejecutar la logica real del servicio de notificaciones.

En resumen, haber aplicado estos patrones en el TP1 no solo mejoro la arquitectura del sistema sino que tambien facilito directamente la escritura de pruebas en el TP2, lo cual es una de las ventajas concretas del diseno orientado a objetos bien aplicado.




