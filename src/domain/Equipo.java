package domain;

import java.util.ArrayList;
import java.util.Objects;

public class Equipo {
	private ArrayList<Jugador> jugadores;
	private String nombre;
	private String ciudad;
	private Liga liga;
	private int anyoFundacion;
	private int titulos;
	private String estadio;
	
	public Equipo(String nombre, String ciudad, Liga liga, int anyoFundacion, int titulos,
			String estadio) {
		super();
		this.jugadores = new ArrayList<Jugador>();
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.liga = liga;
		this.anyoFundacion = anyoFundacion;
		this.titulos = titulos;
		this.estadio = estadio;
	}

	public ArrayList<Jugador> getJugadores() {
		return jugadores;
	}

	public void setJugadores(ArrayList<Jugador> jugadores) {
		this.jugadores = jugadores;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public Liga getLiga() {
		return liga;
	}

	public void setLiga(Liga liga) {
		this.liga = liga;
	}

	public int getAnyoFundacion() {
		return anyoFundacion;
	}

	public void setAnyoFundacion(int anyoFundacion) {
		this.anyoFundacion = anyoFundacion;
	}

	public int gettitulos() {
		return titulos;
	}

	public void settitulos(int titulos) {
		this.titulos = titulos;
	}

	public String getEstadio() {
		return estadio;
	}

	public void setEstadio(String estadio) {
		this.estadio = estadio;
	}

	@Override
	public int hashCode() {
		return Objects.hash(liga, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Equipo other = (Equipo) obj;
		return Objects.equals(liga, other.liga) && Objects.equals(nombre, other.nombre);
	}
	
	

}
