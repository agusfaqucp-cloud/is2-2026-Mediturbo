package strategy;

import model.Paciente;
import model.Especialidad;
import model.Turno;
import observer.Observer;

public class GestorTurnos {

    private EstrategiaAsignacion estrategia;
    private Observer observerNotificacion;

    public GestorTurnos(EstrategiaAsignacion estrategia) {
        this.estrategia = estrategia;
    }

    public GestorTurnos(EstrategiaAsignacion estrategia, Observer observerNotificacion) {
        this.estrategia = estrategia;
        this.observerNotificacion = observerNotificacion;
    }

    public void setEstrategia(EstrategiaAsignacion estrategia) {
        this.estrategia = estrategia;
    }

    public EstrategiaAsignacion getEstrategia() {
        return estrategia;
    }

    public Turno crearTurno(Paciente paciente, Especialidad especialidad) {
        Turno turno = estrategia.asignarTurno(paciente, especialidad);
        if (observerNotificacion != null) {
            turno.agregarObserver(observerNotificacion);
        }
        return turno;
    }
}
