package domain;

import domain.Pregunta.Dificultad;

public class Opcion {
	private static int contador = 1;
	private int cod_opcion;
	private int cod_pregunta;
	private String texto_opcion; 
	private int es_correcta; // 0 incorrecta 1 correcta
	
	public Opcion(int cod_pregunta, String texto_opcion, int es_correcta) {
		super();
		this.cod_opcion = contador;
		contador ++;
		this.cod_pregunta = cod_pregunta;
		this.texto_opcion = texto_opcion;
		this.es_correcta = es_correcta;
	}

	public int getCod_pregunta() {
		return cod_pregunta;
	}

	public void setCod_pregunta(int cod_pregunta) {
		this.cod_pregunta = cod_pregunta;
	}

	public String getTexto_opcion() {
		return texto_opcion;
	}

	public void setTexto_opcion(String texto_opcion) {
		this.texto_opcion = texto_opcion;
	}

	public int getEs_correcta() {
		return es_correcta;
	}

	public void setEs_correcta(int es_correcta) {
		this.es_correcta = es_correcta;
	}

	public int getCod_opcion() {
		return cod_opcion;
	}
	// CSV CON FORMATO : cod_opcion, cod_pregunta, opcion, es_correcta
	public static Opcion parseCSV (String linea) {
		
		if (linea != null) {
			String[] campos = linea.strip().split(",");
			int cod_pregunta = Integer.parseInt(campos[1]);
			String contenido_opcion = campos[2];
			int es_correcta = Integer.parseInt(campos[3]);
			
			return new Opcion(cod_pregunta, contenido_opcion, es_correcta);
			
		}
		return null;
	}
	
}