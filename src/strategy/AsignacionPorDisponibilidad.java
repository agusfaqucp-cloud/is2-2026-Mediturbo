package strategy;

import model.*;
import java.util.ArrayList;
import java.util.List;

public class AsignacionPorDisponibilidad implements EstrategiaAsignacion {

    private final List<Medico> medicos = new ArrayList<>();

    public AsignacionPorDisponibilidad() {
        medicos.add(new Medico("Dr. Garcia",   "manana", "Cardiologia",   true));
        medicos.add(new Medico("Dra. Lopez",   "tarde",  "Pediatria",     true));
        medicos.add(new Medico("Dr. Martinez", "manana", "Dermatologia",  true));
        medicos.add(new Medico("Dra. Romero",  "tarde",  "Cardiologia",   true));
        medicos.add(new Medico("Dr. Silva",    "manana", "Pediatria",     true));
    }

    @Override
    public Turno asignarTurno(Paciente paciente, Especialidad especialidad) {
        for (Medico m : medicos) {
            if (m.getEspecialidad().equalsIgnoreCase(especialidad.getNombre()) && m.isDisponible()) {
                return new Turno(paciente, especialidad, m, SistemaSesion.getUsuarioActual());
            }
        }
        for (Medico m : medicos) {
            if (m.isDisponible()) {
                return new Turno(paciente, especialidad, m, SistemaSesion.getUsuarioActual());
            }
        }
        Medico sinMedico = new Medico("Sin asignar", "", "General", false);
        return new Turno(paciente, especialidad, sinMedico, SistemaSesion.getUsuarioActual());
    }
}
