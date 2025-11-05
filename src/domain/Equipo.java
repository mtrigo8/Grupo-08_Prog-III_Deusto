package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import domain.Jugador.TipoPosicion;

public class Equipo implements Comparable<Equipo> {
	private HashMap<TipoPosicion, ArrayList<Jugador>> jugadores;
	private String nombre;
	private String ciudad;
	private Liga liga;
	private int anyoFundacion;
	private int titulos;
	private String estadio;
	private String	nombrePNGEquipo;
	private int partidosJugados;
	
	private int puntos;
	private int goles;

	public Equipo(String nombre, String ciudad, Liga liga, int anyoFundacion, int titulos,
			String estadio, String nombrePNGEquipo) {
		super();
		this.jugadores = new HashMap<TipoPosicion, ArrayList<Jugador>>();
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.liga = liga;
		this.anyoFundacion = anyoFundacion;
		this.titulos = titulos;
		this.estadio = estadio;
		this.nombrePNGEquipo = nombrePNGEquipo;
	}
	public int getPartidosJugados() {
		return this.partidosJugados;
	}
	
	
	public void actualizarPartidos() {
		this.partidosJugados+=1;
	}

	public int getGoles() {
		return this.goles;
	}
	
	
	public void setGoles(int goles) {
		this.goles = goles;
	}
	public int getPuntos() {
		return this.puntos;
	}
	
	
	public void setPuntos(int puntos) {
		this.puntos = puntos;
	}


	public HashMap<TipoPosicion, ArrayList<Jugador>> getJugadores() {
		return jugadores;
	}

	public void anyadirJugador(Jugador jugador) {
		if (!jugadores.containsKey(jugador.getPosicion())) {
			jugadores.put(jugador.getPosicion(), new ArrayList<Jugador>());
		}
		jugadores.get(jugador.getPosicion()).add(jugador);
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

	public int getTitulos() {
		return titulos;
	}

	public void setTitulos(int titulos) {
		this.titulos = titulos;
	}

	public String getEstadio() {
		return estadio;
	}

	public void setEstadio(String estadio) {
		this.estadio = estadio;
	}
	public String getNombrePNGEquipo () {
		return nombrePNGEquipo;
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
	@Override
	public int compareTo(Equipo o) {
		// TODO Auto-generated method stub
		 return Integer.compare(o.puntos, this.puntos);
	}
	
	

}
