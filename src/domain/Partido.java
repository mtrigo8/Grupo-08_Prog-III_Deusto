package domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Partido {
	private Equipo equipoLocal;
	private Equipo equipoVisitante;
	private int golesLocal;
	private int golesVisitante;
	private LocalDate fecha;
	private Equipo equipoGanador;
	private Equipo equipoPerdedor;
	private int jornada;
	
	public Partido(Equipo equipoLocal, Equipo equipoVisitante, int golesLocal, int golesVisitante, LocalDate fecha, int jornada) {
		super();
		this.equipoLocal = equipoLocal;
		this.equipoVisitante = equipoVisitante;
		this.golesLocal = golesLocal;
		this.golesVisitante = golesVisitante;
		this.fecha = fecha;
		this.jornada = jornada;
	}
	
	public void actualizacionPuntos() {
		if (this.golesLocal > this.golesVisitante ) {
			this.equipoLocal.setPuntos(this.equipoLocal.getPuntos()+3);
		}else if (this.golesLocal < this.golesVisitante) {
			this.equipoVisitante.setPuntos(this.equipoVisitante.getPuntos()+3);
		}else {
			this.equipoLocal.setPuntos(this.equipoLocal.getPuntos()+1);
			this.equipoLocal.setPuntos(this.equipoLocal.getPuntos()+1);
		}
	}
	public void actualizarGoles() {
		equipoLocal.setGoles(equipoLocal.getGoles()+golesLocal-golesVisitante);
		equipoVisitante.setGoles(equipoVisitante.getGoles()-golesLocal+golesVisitante);
		equipoLocal.actualizarPartidos();
		equipoVisitante.actualizarPartidos();
	}

	public Equipo getEquipoLocal() {
		return equipoLocal;
	}

	public void setEquipoLocal(Equipo equipoLocal) {
		this.equipoLocal = equipoLocal;
	}

	public Equipo getEquipoVisitante() {
		return equipoVisitante;
	}

	public void setEquipoVisitante(Equipo equipoVisitante) {
		this.equipoVisitante = equipoVisitante;
	}

	public int getGolesLocal() {
		return golesLocal;
	}

	public void setGolesLocal(int golesLocal) {
		this.golesLocal = golesLocal;
	}

	public int getGolesVisitante() {
		return golesVisitante;
	}

	public void setJornada(int jornada) {
		this.jornada = jornada;
	}
	public int getJornada() {
		return jornada;
	}

	public void setGolesVisitante(int golesVisitante) {
		this.golesVisitante = golesVisitante;
	}
	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		return jornada + "-" + equipoLocal.getNombre() + " " + golesLocal + " - " + golesVisitante + " " + equipoVisitante.getNombre() + " (" + fecha + ")";
	}

	@Override
	public int hashCode() {
		return Objects.hash(equipoLocal, equipoVisitante, fecha);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Partido other = (Partido) obj;
		return Objects.equals(equipoLocal, other.equipoLocal) && Objects.equals(equipoVisitante, other.equipoVisitante)
				&& Objects.equals(fecha, other.fecha);
	}
	
	
}
