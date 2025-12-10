package domain;

import java.util.Objects;
import java.util.Random;

public class Jugador {
	private static int contador = 1;
	private String nombre;
	private int numeroCamiseta;
	private TipoPosicion posicion;
	private Equipo equipo;
	private String stringNombreEquipo;
	private String nacionalidad;
	private int edad;
	private int cod_jugador;
	private String nombreEquipo;
	private PiernaHabil piernaHabil;
	private int altura;
	private int goles;
	private int partidosJugados;
	private int asistencias;
    private int regates;
    private int porteriasaCero;
	private int paradas;
    private int golesEncajados;
    private double valorMercado;

public enum PiernaHabil{
	IZQUIERDA,
	DERECHA,
}
public enum TipoPosicion {
	PORTERO, 
	DEFENSA,
	CENTROCAMPISTA, 
	DELANTERO
}

public Jugador(String nombre, int numeroCamiseta, TipoPosicion posicion, Equipo equipo, String nacionalidad,int edad) {
	super();
	this.nombre = nombre;
	this.numeroCamiseta = numeroCamiseta;
	this.posicion = posicion;
	this.equipo = equipo;
	this.nacionalidad = nacionalidad;
	this. edad= edad;
	this.cod_jugador = contador;
	contador ++;
	
	}
public Jugador(String nombre, int numeroCamiseta, TipoPosicion posicion, String nacionalidad,int edad, String nombreEquipo) {
	super();
	this.nombre = nombre;
	this.numeroCamiseta = numeroCamiseta;
	this.posicion = posicion;
	this.nombreEquipo = nombreEquipo;
	this.nacionalidad = nacionalidad;
	this. edad= edad;
	this.cod_jugador = contador;
	contador ++;
	
	}

public Jugador(String nom_jugador, int num_camiseta, String posicion,
        String nacionalidad, int edad, String equipo, String pierna_habil,
        int altura, int goles, int partidos_jugados, int asistencias,
        int regates, int porterias_acero, int paradas, int goles_encajados,
        double valor_mercado) {

	
	this.nombre = nom_jugador;
	this.numeroCamiseta = num_camiseta;
	this.posicion =TipoPosicion.valueOf(posicion);
	this.nacionalidad = nacionalidad;
	this.edad = edad;
	this.stringNombreEquipo = equipo;
	this.piernaHabil = PiernaHabil.valueOf(pierna_habil);
	this.altura = altura;
	this.goles = goles;
	this.partidosJugados = partidos_jugados;
	this.asistencias = asistencias;
	this.regates = regates;
	this.porteriasaCero = porterias_acero;
	this.paradas = paradas;
	this.golesEncajados = goles_encajados;
	this.valorMercado = valor_mercado;
}

public int getCod_jugador() {
	return cod_jugador;
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
public int getEdad() {
	return edad;
}
public void setEdad(int edad) {
	this.edad = edad;
}
public PiernaHabil getPiernaHabil() {
	return piernaHabil;
}
public void setPiernaHabil(PiernaHabil piernaHabil) {
	this.piernaHabil = piernaHabil;
}
public int getAltura() {
	return altura;
}
public void setAltura(int altura) {
	this.altura = altura;
}

public String getNombreEquipo() {
	return nombreEquipo;
}
public void setNombreEquipo(String nombreEquipo) {
	this.nombreEquipo = nombreEquipo;
}


public int getGoles() {
	return goles;
}
public void setGoles(int goles) {
	this.goles = goles;
}
public int getPartidosJugados() {
	return partidosJugados;
}
public void setPartidosJugados(int partidosJugados) {
	this.partidosJugados = partidosJugados;
}
public int getAsistencias() {
	return asistencias;
}
public void setAsistencias(int asistencias) {
	this.asistencias = asistencias;
}
public int getRegates() {
	return regates;
}
public void setRegates(int regates) {
	this.regates = regates;
}
public int getPorteriasaCero() {
	return porteriasaCero;
}
public void setPorteriasaCero(int porteriasaCero) {
	this.porteriasaCero = porteriasaCero;
}
public int getParadas() {
	return paradas;
}
public double getValorMercado() {
	return valorMercado;
}
public void setValorMercado(double valorMercado) {
	this.valorMercado = valorMercado;
}
public void setParadas(int paradas) {
	this.paradas = paradas;
}
public int getGolesEncajados() {
	return golesEncajados;
}
public void setGolesEncajados(int golesEncajados) {
	this.golesEncajados = golesEncajados;
}
private void generarEstadisticasAleatorias() {
    Random r = new Random();
    
    this.partidosJugados = r.nextInt(24) + 15; 

    if (this.posicion == TipoPosicion.PORTERO) {
        this.goles = 0; 
        this.asistencias = 0; 
        this.porteriasaCero = r.nextInt(this.partidosJugados);
        this.paradas = r.nextInt(60) + 20;
        this.golesEncajados = r.nextInt(50) + 8;
        this.regates = 0;
        this.valorMercado=(this.porteriasaCero*1500000 + this.paradas*100000+this.partidosJugados*50000)/(1000000);
    } 
    else if (this.posicion == TipoPosicion.DEFENSA) {

        this.goles = r.nextInt(6);
        this.asistencias = r.nextInt(8);
        this.porteriasaCero = r.nextInt(this.partidosJugados);
        this.regates = r.nextInt(10);
        this.paradas = 0; 
        this.golesEncajados = 0;
        this.valorMercado=(this.porteriasaCero*1000000 +this.asistencias*1000000+this.regates*20000+ this.goles*2000000+this.partidosJugados*50000)/(1000000);
    } 
    else if (this.posicion == TipoPosicion.CENTROCAMPISTA) {
 
        this.goles = r.nextInt(3*this.partidosJugados/2);
        this.asistencias = r.nextInt(this.partidosJugados) + 2;
        this.regates = r.nextInt(30) + 5;
        this.paradas = 0;
        this.porteriasaCero = 0;
        this.valorMercado=(this.asistencias*1500000+this.regates*50000+ this.goles*1500000+this.partidosJugados*50000)/(1000000);
    } 
    else if (this.posicion == TipoPosicion.DELANTERO) {
        this.goles = r.nextInt(2*this.partidosJugados) + 3;
        this.asistencias = r.nextInt(12);
        this.regates = r.nextInt(60) + 10; 
        this.paradas = 0;
        this.porteriasaCero = 0;
        this.valorMercado=(this.asistencias*1000000+this.regates*50000+ this.goles*2000000+this.partidosJugados*50000)/(1000000);
    }
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
