# TP2 - Diseño de Interfaz Centrado en el Usuario

## A1. Prototipo en Figma

Para esta entrega completamos el prototipo del sistema en Figma cubriendo el flujo principal: desde que el usuario inicia sesion hasta que gestiona los turnos del dia. Las tres pantallas elegidas son las mas representativas del caso de uso central del sistema.

**Pantallas incluidas:**

1. **Login** - pantalla de inicio de sesion con campos de usuario y contrasena, diferenciada por rol (admin, medico, paciente)
2. **Cartel de Bienvenida** - se da la bienvenida muestra nombre de quien ingresa, su rol y horario de ingreso, este mismo desaparece automaticamente tras 3 segundos.
3. **Panel principal** - vista central con la tabla de turnos, acciones rapidas segun rol y panel de notificaciones en tiempo real
4. **Gestion de turnos** - pantalla de consulta con filtros por fecha, medico y estado del turno
5. **Estadisticas** - pantalla de estadisticas donde se muestra turnos por estado y medico, de igual manera quien seria el que mas turnos tiene
6. **Cartel de sesion expirada** - tras cierto tiempo de inactividad el sistema va a cerra automaticamente la sesion del usuario

**Link al prototipo Figma:**  
https://www.figma.com/design/mRutYYwYpRe4THQYHtVc3X/MediTurnos---Pantallas?node-id=0-1&t=hKwMku6UAWEPle7o-1

**Capturas del prototipo:**

> Pantalla 1 - Login
<img width="767" height="494" alt="image" src="https://github.com/user-attachments/assets/404a428e-5ee2-4167-8d9b-01342f242658" />

> Pantalla 2 - Cartel de bienvenida
<img width="469" height="291" alt="image" src="https://github.com/user-attachments/assets/995d6e6e-5cb2-4fdf-8db1-b0c4af50fc7c" />


> Pantalla 3 - Panel principal
<img width="1366" height="729" alt="image" src="https://github.com/user-attachments/assets/9dc59c32-16c1-488b-8a25-5a2645d55fb0" />


> Pantalla 4 - Gestion de turnos
<img width="1364" height="723" alt="image" src="https://github.com/user-attachments/assets/5285cd9c-a996-415b-805c-6965ef06e962" />

> Pantalla 5 - Estadisticas
<img width="569" height="474" alt="image" src="https://github.com/user-attachments/assets/8f798c32-a614-42ea-bde9-dd067cff034b" />

> Pantalla 6 - Cartel de sesion expirada
<img width="346" height="128" alt="image" src="https://github.com/user-attachments/assets/86d59c3a-d631-4e7c-b42c-a2cec3a25a5f" />


---

## A2. Analisis de usuario, tarea y contexto

El sistema MediTurnos esta orientado a tres tipos de usuario con perfiles y necesidades muy distintas entre si. El primero es el personal administrativo, que en una clinica o consultorio es quien mas tiempo pasa frente al sistema. Su tarea principal es registrar nuevos turnos, confirmarlos, cancelarlos y hacer seguimiento del estado de la agenda diaria. Este usuario trabaja desde una computadora de escritorio en la recepcion, generalmente con interrupciones frecuentes por atencion al publico presencial, lo que exige que la interfaz sea directa y permita retomar una tarea sin perder el hilo de lo que estaba haciendo.

El segundo perfil es el medico, que interactua con el sistema de forma mas puntual y en momentos acotados. Su necesidad principal es ver que turnos tiene asignados, marcarlos como atendidos y en algunos casos revisar el historial de un paciente. A diferencia del administrativo, el medico no tiene tiempo de explorar menus ni aprender funciones nuevas entre consulta y consulta, por eso decidimos que las opciones disponibles para su rol fueran las minimas necesarias. Esto se implemento en el TP1 a traves del sistema de roles que filtra las acciones visibles segun quien inicio sesion.

El tercer perfil es el paciente, que es el usuario con menor experiencia tecnica esperada. Su unica accion dentro del sistema es solicitar un turno: ingresar su nombre, elegir una especialidad y confirmar. Este usuario puede estar accediendo desde distintos dispositivos y en condiciones de estres vinculadas a su salud, lo que hace que la interfaz deba ser lo mas clara y guiada posible, sin opciones confusas ni pasos innecesarios.

En los tres casos el sistema opera en contextos con cierta presion de tiempo o atencion dividida. Esto refuerza la necesidad de que la interfaz permita completar el flujo principal rapido, que los errores se comuniquen con mensajes claros y que no haya imprecision sobre si una accion se realizo o no. 

---

## A3. Auditoria de usabilidad segun ISO 9241-11

La norma ISO 9241-11 define usabilidad como el grado en que un sistema permite a usuarios especificos alcanzar objetivos especificos con eficacia, eficiencia y satisfaccion en un contexto de uso determinado. Para esta auditoria seleccionamos los criterios de **eficiencia** y **satisfaccion**, que consideramos los mas criticos dado el perfil de usuarios que describimos en la seccion anterior.


### Criterio 1 - Eficiencia

**Definicion aplicada al sistema:**  
La eficiencia mide el costo con el que un usuario logra completar su tarea, en terminos de tiempo, cantidad de pasos y esfuerzo cognitivo. En MediTurnos la tarea principal del administrativo es registrar un turno nuevo, y un sistema eficiente deberia permitirle hacerlo rapido y sin tener que pensar demasiado en como funciona la interfaz.

**Metrica definida:**  
Cantidad de interacciones necesarias para registrar un turno desde que se abre el panel principal hasta que el turno aparece en la tabla. El valor de referencia que consideramos aceptable es un maximo de 4 interacciones en menos de 30 segundos para un usuario con experiencia basica en el sistema.

**Simulacion sobre el prototipo actual:**  
El flujo principal es: ingresar el nombre del paciente → seleccionar la especialidad en el combo → hacer clic en "Nuevo Turno". Son 3 interacciones, lo que esta dentro del rango aceptable. Sin embargo identificamos una inconsistencia: si el usuario accede por el boton del sidebar en lugar del de la toolbar, se abre un dialogo emergente adicional que agrega un paso mas. La misma tarea tiene dos caminos con distinta cantidad de pasos dependiendo desde donde se inicie, lo que puede generar confusion especialmente en usuarios nuevos.

**Mejora propuesta:**  
Unificar el acceso para crear un turno en un unico punto visible, preferentemente desde la toolbar superior que es lo primero que el usuario ve. Ademas seria util que el campo de nombre del paciente tome el foco automaticamente al abrir la ventana, para evitar el clic previo de posicionamiento que actualmente se necesita.

---

### Criterio 2 - Satisfaccion

**Definicion aplicada al sistema:**  
La satisfaccion es la percepcion del usuario sobre el sistema: si lo encuentra comodo, confiable y adecuado para su trabajo cotidiano. En sistemas de uso diario como este, la satisfaccion baja se traduce directamente en mas errores del operador y en resistencia a usar el sistema, lo que en un entorno medico puede tener consecuencias reales.

**Metrica definida:**  
Se usaria una escala SUS (System Usability Scale) simplificada de 5 preguntas con puntaje del 1 al 5, aplicada a usuarios representativos de cada rol despues de completar el flujo principal. Un puntaje por encima de 70 sobre 100 se considera aceptable segun la literatura de HCI. Como medicion indirecta tambien se puede contar la cantidad de errores que comete el usuario durante el flujo antes de completarlo correctamente.

**Simulacion sobre el prototipo actual:**  
Identificamos dos problemas concretos que afectan la satisfaccion. El primero es que cuando se crea un turno exitosamente no aparece ninguna confirmacion visual: el turno aparece en la tabla pero no hay mensaje ni indicacion de que la operacion se completo. Para un usuario nuevo esto genera duda sobre si hizo bien las cosas. El segundo problema es que los botones de accion del sidebar (Confirmar, Cancelar, Atendido) no hacen nada ni muestran aviso si no hay un turno seleccionado en la tabla, lo que es confuso porque el usuario no entiende por que el boton no responde.

**Mejora propuesta:**  
Agregar un mensaje de confirmacion breve que aparezca unos segundos despues de registrar un turno, indicando el nombre del paciente y el medico asignado. Para los botones de accion sin seleccion, mostrar un aviso del tipo "Primero selecciona un turno de la lista". Ambos cambios son pequenos a nivel de codigo pero tienen impacto directo en la percepcion de confiabilidad del sistema por parte del usuario.

---

### Alineacion con ISO 13407 - Diseño centrado en el humano

La ISO 13407 propone un ciclo iterativo de cuatro fases para desarrollar sistemas interactivos: comprender el contexto de uso, especificar los requisitos del usuario, producir soluciones de disenio, y evaluar esas soluciones contra los requisitos. Lo interesante es que si miramos el proceso que seguimos en este proyecto, podemos identificar cada una de esas fases aunque no las hayamos seguido de forma explicita desde el principio.

En la primera fase, antes de diseñar cualquier pantalla, identificamos que el sistema tenia tres tipos de usuario con necesidades muy distintas y contextos de uso diferentes. En la segunda fase definimos que cada rol debia acceder solo a las funciones que le corresponden, requisito que se implemento directamente en el codigo del TP1 con el sistema de roles. En la tercera fase disenamos y construimos el prototipo con la interfaz diferenciada por rol, la paleta de colores consistente con un entorno medico y las pantallas que documentamos en A1. La cuarta fase, la evaluacion formal con usuarios reales, es la que no pudimos completar en profundidad por las limitaciones del contexto academico, pero esta auditoria ISO 9241-11 es nuestra aproximacion a esa evaluacion: tomamos el prototipo, definimos metricas concretas, identificamos dos problemas reales y propusimos mejoras especificas. Si el proyecto continuara, el paso siguiente seria implementar esas mejoras y volver a evaluar, cerrando el ciclo iterativo que propone la norma.


