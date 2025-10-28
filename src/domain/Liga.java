package domain;

import java.util.ArrayList;
import java.util.Objects;

public class Liga {
	private String nombre;
	private String pais;
	private int numeroEquipos;
	private ArrayList<Equipo> equipos;
	private int jornada;
	private ArrayList<ArrayList<Partido>> calendario;
	
	public Liga(String nombre, String pais, int numeroEquipos, ArrayList<Equipo> equipos) {
		super();
		this.nombre = nombre;
		this.pais = pais;
		this.numeroEquipos = numeroEquipos;
		this.equipos = equipos;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public int getNumeroEquipos() {
		return numeroEquipos;
	}

	public void setNumeroEquipos(int numeroEquipos) {
		this.numeroEquipos = numeroEquipos;
	}

	public ArrayList<Equipo> getEquipos() {
		return equipos;
	}

	public void setEquipos(ArrayList<Equipo> equipos) {
		this.equipos = equipos;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nombre, pais);
	}

	public int getJornada() {
		return jornada;
	}

	public void setJornada(int jornada) {
		this.jornada = jornada;
	}

	public ArrayList<ArrayList<Partido>> getCalendario() {
		return calendario;
	}

	public void setCalendario(ArrayList<ArrayList<Partido>> calendario) {
		this.calendario = calendario;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Liga other = (Liga) obj;
		return Objects.equals(nombre, other.nombre) && Objects.equals(pais, other.pais);
	}
	
	
	
	

}
