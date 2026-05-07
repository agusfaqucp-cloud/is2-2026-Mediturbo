# TP2 - Pruebas de Software

### Clases de equivalencia

Una clase de equivalencia es un grupo de valores de entrada que se espera que el sistema procese de la misma forma. La idea detras de esta tecnica es simple: si probas un valor de un grupo y funciona, es razonable asumir que todos los valores de ese grupo van a funcionar igual. Y al reves, si uno falla, todos deberian fallar. Esto nos permite cubrir el comportamiento del sistema sin tener que probar todos los valores posibles, que en la practica seria imposible.

**Ejemplo concreto en nuestro proyecto:**

El metodo que crea un turno en `GestorTurnos` recibe el nombre del paciente como String. Identificamos tres clases:

- **Clase valida:** nombre no vacio, por ejemplo "Juan Perez" → el sistema crea el turno correctamente
- **Clase invalida 1:** nombre vacio `""` → deberia rechazarse o al menos ser detectable como invalido
- **Clase invalida 2:** nombre con solo espacios `"   "` → deberia tratarse igual que vacio y rechazarse

En lugar de probar cien nombres distintos, con un caso por clase ya cubrimos los tres comportamientos posibles del sistema.


### Valores limite

Un valor limite es un valor que esta exactamente en el borde entre una clase valida y una invalida. La experiencia en testing muestra que los bugs se concentran justo en esos bordes: el sistema puede funcionar perfectamente para valores claramente dentro del rango pero fallar cuando el valor es exactamente el minimo o el maximo aceptable.

Para aplicarla identificamos los extremos de cada rango valido y probamos esos valores especificos: el valor justo en el limite, el inmediatamente por debajo y el inmediatamente por encima.

**Ejemplo concreto en nuestro proyecto:**

En `PersistenciaTurnos`, el metodo `cargarJSON` lee una lista de turnos desde un archivo. Casos de valor limite que identificamos:

- **Lista con 0 turnos** (archivo JSON vacio o con array `[]`) → el sistema deberia devolver una lista vacia sin romper
- **Lista con 1 turno** (el minimo con datos reales) → deberia cargarlo correctamente
- **Paciente con nombre de longitud 1** → un solo caracter, el minimo posible para un nombre no vacio, deberia aceptarse


## B1. Pruebas unitarias

### Framework elegido: JUnit 5

Elegimos JUnit 5 porque es el estandar para proyectos Java, tiene integracion directa con Maven y con VS Code a traves de la extension "Test Runner for Java". No necesita configuracion compleja y los resultados se ven directamente en la terminal o en el IDE con colores que indican si el test paso o fallo. Con estevamos a poder utilizar todas las herramientas necesarias para hacer las pruebas 



### Tabla de casos de prueba

| # | Metodo bajo prueba | Tecnica | Datos de entrada | Resultado esperado |
|---|---|---|---|---|
| 1 | `GestorTurnos.crearTurno()` | Equivalencia valida | Paciente "Juan Perez", especialidad "Cardiologia" | Turno creado con estado PENDIENTE |
| 2 | `GestorTurnos.crearTurno()` | Equivalencia invalida | Paciente con nombre vacio `""` | El nombre del paciente queda vacio, detectable como invalido |
| 3 | `Turno.cambiarEstado()` | Equivalencia valida | Estado CONFIRMADO sobre turno PENDIENTE | Estado del turno cambia a CONFIRMADO |
| 4 | `Turno.cambiarEstado()` | Equivalencia valida | Estado CANCELADO sobre turno CONFIRMADO | Estado del turno cambia a CANCELADO |
| 5 | `PersistenciaTurnos.cargarJSON()` | Valor limite | Archivo JSON con array vacio `[]` | Lista vacia devuelta sin excepcion |
| 6 | `PersistenciaTurnos.cargarJSON()` | Valor limite | Paciente con nombre de solo espacios `"   "` | Nombre detectable como invalido al hacer trim() |



### Codigo de los tests

Los archivos estan en `pruebas/unit/` dentro del repositorio.


## B2. GitHub Actions - CI/CD

Elegimos JUnit 5 con Maven porque Maven tiene soporte en GitHub Actions y el comando `mvn test` ejecuta todos los tests y muestra el resultado en consola sin configuracion extra. El archivo de workflow esta en `.github/workflows/test.yml` en el repositorio y se dispara automaticamente en cada push a main.

**Evidencia de ejecucion exitosa:**

El workflow corrio correctamente con status **Success** en GitHub Actions. La captura se incluye a continuacion:

<img width="1366" height="611" alt="image" src="https://github.com/user-attachments/assets/358d93d6-5df8-4388-af19-dbd09daa791f" />


**Video de ejecucion de los tests:** https://youtu.be/E-YOE0nhhTE




## B3. Diseño de pruebas de integracion


### Dependencia 1 - Sistema de archivos (PersistenciaTurnos)

El modulo `PersistenciaTurnos` depende directamente del sistema de archivos para guardar y cargar turnos en JSON y TXT. Esto es una dependencia externa porque el comportamiento puede variar segun el sistema operativo, los permisos del directorio o el estado del disco en el momento de la prueba. En una prueba de integracion futura esto se mockeria usando una implementacion en memoria que simule las operaciones de lectura y escritura sin tocar el disco real, lo que hace los tests mas rapidos y predecibles.

### Dependencia 2 - Sistema de sesion (SistemaSesion)

`SistemaSesion` guarda el usuario actual en un campo estatico compartido por toda la aplicacion. En pruebas de integracion esto es problematico porque el estado que deja una prueba puede contaminar la siguiente si no se limpia bien. En un sistema real esta dependencia seria un servicio de autenticacion externo, ya sea una base de datos de usuarios, un directorio o un proveedor , que en las pruebas se reemplazaria por un stub que devuelve siempre un usuario fijo y controlado.



### Flujo de prueba de integracion (pseudocodigo)

```
DADO que el sistema tiene un usuario admin autenticado (stub de SistemaSesion)
Y que el archivo turnos.json existe con 2 turnos previos (archivo temporal de prueba)

CUANDO el sistema carga los turnos al iniciar
Y el usuario crea un nuevo turno para "Ana Garcia" en "Pediatria"
Y el sistema guarda los turnos

ENTONCES el archivo turnos.json deberia contener 3 turnos
Y el tercer turno deberia tener paciente "Ana Garcia" y estado "PENDIENTE"
Y el ServicioNotificacion deberia haber recibido exactamente 1 notificacion
```
