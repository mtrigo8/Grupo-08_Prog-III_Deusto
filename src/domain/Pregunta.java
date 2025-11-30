package domain;


public class Pregunta {
	
	public enum Dificultad {
        FACIL,
        MEDIA,
        DIFICIL
    }
	private int codigo;
	private String pregunta;
	private Dificultad dificultad;
	private String categoria;
	
	public Pregunta(int codigo, String pregunta, Dificultad dificultad, String categoria) {
		this.codigo = codigo;
		this.pregunta = pregunta;
		this.dificultad = dificultad;
		this.categoria = categoria;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getPregunta() {
		return pregunta;
	}

	public Dificultad getDificultad() {
		return dificultad;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public void setDificultad(Dificultad dificultad) {
		this.dificultad = dificultad;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
	public static Pregunta parseCSV(String linea) {
		
		if (linea != null) {
			String[] campos = linea.strip().split(",");
			int codigo = (Integer.parseInt(campos[0]));
			String contenido_pregunta = campos[1];
			String categoria = campos[2];
			Dificultad dificultad = (Dificultad.valueOf(campos[3].toUpperCase()));
			
			Pregunta p = new Pregunta(codigo, contenido_pregunta, dificultad, categoria);
			return p;
		}
		return null;
	}
	
}

