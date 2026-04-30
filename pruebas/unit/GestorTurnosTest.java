import model.*;
import strategy.*;
import observer.ServicioNotificacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GestorTurnosTest {

    private GestorTurnos gestor;

    @BeforeEach
    public void setUp() {
        SistemaSesion.setUsuarioActual(new Usuario("admin", Rol.ADMIN));
        gestor = new GestorTurnos(new AsignacionPorDisponibilidad(), new ServicioNotificacion());
    }

    // Caso 1 - Equivalencia valida: turno con datos correctos queda en PENDIENTE
    @Test
    public void testCrearTurnoValidoEstadoPendiente() {
        Paciente paciente = new Paciente("Juan Perez");
        Especialidad especialidad = new Especialidad("Cardiologia");

        Turno turno = gestor.crearTurno(paciente, especialidad);

        assertEquals(Estado.PENDIENTE, turno.getEstado(),
            "Un turno recien creado deberia tener estado PENDIENTE");
    }

    // Caso 2 - Equivalencia invalida: nombre de paciente vacio
    @Test
    public void testCrearTurnoConNombreVacioDevuelveNombreVacio() {
        Paciente paciente = new Paciente("");
        Especialidad especialidad = new Especialidad("Pediatria");

        Turno turno = gestor.crearTurno(paciente, especialidad);

        assertTrue(turno.getPaciente().getNombre().isEmpty(),
            "El turno se creo con nombre vacio, el sistema no valida este caso");
    }

    // Caso 3 - Equivalencia valida: cambio de estado de PENDIENTE a CONFIRMADO
    @Test
    public void testCambiarEstadoAConfirmado() {
        Paciente paciente = new Paciente("Maria Lopez");
        Especialidad especialidad = new Especialidad("Dermatologia");

        Turno turno = gestor.crearTurno(paciente, especialidad);
        turno.cambiarEstado(Estado.CONFIRMADO);

        assertEquals(Estado.CONFIRMADO, turno.getEstado(),
            "El estado deberia haber cambiado a CONFIRMADO");
    }

    // Caso 4 - Equivalencia valida: cambio de estado de CONFIRMADO a CANCELADO
    @Test
    public void testCambiarEstadoDeConfirmadoACancelado() {
        Paciente paciente = new Paciente("Carlos Ruiz");
        Especialidad especialidad = new Especialidad("Cardiologia");

        Turno turno = gestor.crearTurno(paciente, especialidad);
        turno.cambiarEstado(Estado.CONFIRMADO);
        turno.cambiarEstado(Estado.CANCELADO);

        assertEquals(Estado.CANCELADO, turno.getEstado(),
            "El estado deberia haber cambiado a CANCELADO");
    }
}
