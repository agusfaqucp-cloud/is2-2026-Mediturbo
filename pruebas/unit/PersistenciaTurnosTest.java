import datos.PersistenciaTurnos;
import model.Turno;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class PersistenciaTurnosTest {

    // Caso 5 - Valor limite: archivo JSON con lista vacia
    @Test
    public void testCargarJSONVacioDevuelveListaVacia() throws Exception {
        File archivo = new File("turnos.json");
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            pw.println("[]");
        }

        List<Turno> resultado = PersistenciaTurnos.cargarJSON();

        assertNotNull(resultado, "La lista no deberia ser null");
        assertTrue(resultado.isEmpty(),
            "Con un JSON vacio la lista de turnos deberia estar vacia");
    }

    // Caso 6 - Valor limite: paciente con nombre de solo espacios
    @Test
    public void testCargarJSONConPacienteNombreSoloEspacios() throws Exception {
        File archivo = new File("turnos.json");
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            pw.println("[");
            pw.println("  {");
            pw.println("    \"paciente\": \"   \",");
            pw.println("    \"especialidad\": \"Cardiologia\",");
            pw.println("    \"medico\": \"Dr. Garcia\",");
            pw.println("    \"estado\": \"PENDIENTE\",");
            pw.println("    \"fecha\": \"27/04/2026 10:00\",");
            pw.println("    \"creadoPor\": \"admin\"");
            pw.println("  }");
            pw.println("]");
        }

        List<Turno> resultado = PersistenciaTurnos.cargarJSON();

        assertFalse(resultado.isEmpty(), "Se cargo al menos un turno");
        String nombrePaciente = resultado.get(0).getPaciente().getNombre().trim();
        assertTrue(nombrePaciente.isEmpty(),
            "Un nombre con solo espacios deberia considerarse invalido");
    }

    @AfterEach
    public void limpiar() {
        new File("turnos.json").delete();
    }
}
