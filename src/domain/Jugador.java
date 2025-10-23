package domain;

import java.util.Objects;

public class Jugador {
	private String nombre;
	private int numeroCamiseta;
	private TipoPosicion posicion;
	private Equipo equipo;
	private String nacionalidad;
		
public enum TipoPosicion {
	DELANTERO,
	MEDIOCAMPISTA,
	DEFENSOR,
	PORTERO
}

public Jugador(String nombre, int numeroCamiseta, TipoPosicion posicion, Equipo equipo, String nacionalidad) {
	super();
	this.nombre = nombre;
	this.numeroCamiseta = numeroCamiseta;
	this.posicion = posicion;
	this.equipo = equipo;
	this.nacionalidad = nacionalidad;
}

public String getNombre() {
	return nombre;
}

public void setNombre(String nombre) {
	this.nombre = nombre;
}

public int getNumeroCamiseta() {
	return numeroCamiseta;
}

public void setNumeroCamiseta(int numeroCamiseta) {
	this.numeroCamiseta = numeroCamiseta;
}

public TipoPosicion getPosicion() {
	return posicion;
}

public void setPosicion(TipoPosicion posicion) {
	this.posicion = posicion;
}

public Equipo getEquipo() {
	return equipo;
}

public void setEquipo(Equipo equipo) {
	this.equipo = equipo;
}

public String getNacionalidad() {
	return nacionalidad;
}

public void setNacionalidad(String nacionalidad) {
	this.nacionalidad = nacionalidad;
}

@Override
public int hashCode() {
	return Objects.hash(equipo, nombre);
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Jugador other = (Jugador) obj;
	return Objects.equals(equipo, other.equipo) && Objects.equals(nombre, other.nombre);
}

@Override
public String toString() {
	return "Jugador " + nombre + " con el numero de camiseta " + numeroCamiseta + " es " + posicion + " en el equipo " + equipo ;
}




}
