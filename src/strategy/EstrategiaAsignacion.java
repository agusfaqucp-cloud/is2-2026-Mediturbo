package strategy;

import model.Paciente;
import model.Especialidad;
import model.Turno;

public interface EstrategiaAsignacion {
    Turno asignarTurno(Paciente paciente, Especialidad especialidad);
}
