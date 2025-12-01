package db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import domain.Opcion;
import domain.Pregunta;

public class GestorBDQuiz {
	private String PREGUNTAS_CSV = "resources/data/preguntas.csv";
	private String OPCIONES_CSV = "resources/data/opciones.csv"; 
	private final String LOG_FOLDER = "resources/log";
	private final String PROPERTIES_FILE = "resources/config/app.properties";

	private Properties properties;
	private String driverName;
	private String databaseFile;
	private String connectionString;
	
	private static Logger logger = Logger.getLogger(GestorBD.class.getName());
	public GestorBDQuiz() {
		try (FileInputStream fis = new FileInputStream("resources/config/logger.properties")) {
			//Inicialización del Logger
			LogManager.getLogManager().readConfiguration(fis);
			
			//Lectura del fichero properties
			properties = new Properties();
			properties.load(new FileReader(PROPERTIES_FILE));
			
			driverName = properties.getProperty("driver");
			databaseFile = properties.getProperty("file");
			connectionString = properties.getProperty("connection");
			
			//Crear carpetas de log si no existe
			File dir = new File(LOG_FOLDER);
			
			if (!dir.exists()) {
				dir.mkdirs();
			}

			//Crear carpeta para la BBDD si no existe
			dir = new File(databaseFile.substring(0, databaseFile.lastIndexOf("/")));
			
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			//Cargar el diver SQLite
			Class.forName(driverName);
		} catch (Exception ex) {
			logger.warning(String.format("Error al cargar el driver de BBDD: %s", ex.getMessage()));
		}
	}
	// Inicializa los datos de la BBDD desde varios CSV
	public void initilizeFromCSV() {
		//Sólo se inicializa la BBDD si la propiedad initBBDD es true.
		if (properties.get("loadCSV").equals("true")) {
			//Se borran los datos, si existía alguno
			this.borrarDatos();
			
			//Se leen las preguntas del CSV
			List<Pregunta> preguntas = this.cargarCSVPreguntas();
			//Se insertan los personajes en la BBDD
			this.insertarPreguntas((ArrayList<Pregunta>) preguntas);
			
			//Se leen las opciones del CSV
			List<Opcion> opciones = this.cargarCSVOpciones();
			
			//Insertar las opciones en la BBDD
			this.insertarOpciones(opciones.toArray(new Opcion[opciones.size()]));
		}
		
	}
	public void crearBBDD() {
		 // Verificar si el archivo de BBDD ya existe
	    File dbFile = new File(databaseFile);
	    
	    if (dbFile.exists()) {
	        logger.info("La base de datos ya existe, no se creará de nuevo");
	        return;
	    }
	    
		//Sólo se crea la BBDD si la propiedad initBBDD es true.
		if (properties.get("createBBDD").equals("true")) {
			//La base de datos tiene 3 tablas: Personaje, Comic y Personajes_Comic
			String sql1 = "CREATE TABLE IF NOT EXISTS pregunta (\n"
	                + " cod_pregunta INTEGER PRIMARY KEY,\n"
	                + " pregunta TEXT NOT NULL,\n"
	                + " dificultad TEXT NOT NULL CHECK(dificultad IN ('Facil', 'Media', 'Dificil')),\n"
	                + " categoria TEXT NOT NULL\n"
	                + " );";
	
			String sql2 = "CREATE TABLE IF NOT EXISTS opcion (\n"
	                + " cod_opcion INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " cod_pregunta INTEGER NOT NULL,\n"
	                + " opcion TEXT UNIQUE NOT NULL\n"
	                + " es_correcta INTEGER NOT NULL DEFAULT 0\n"
	                + " FOREIGN KEY(cod_pregunta) REFERENCES pregunta(cod_pregunta) ON DELETE CASCADE\n"
	                +" ON UPDATE CASCADE\n"
	                + ");"; 
	
			String sql3 = "CREATE TABLE IF NOT EXISTS usuario (" +
                    "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT NOT NULL, " +
                    "fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                    "puntuacion INTEGER DEFAULT 0" +
                    ");";
			
	        //Se abre la conexión y se crea un PreparedStatement para crer cada tabla
			//Al abrir la conexión, si no existía el fichero por defecto, se crea.
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt1 = con.prepareStatement(sql1);
				 PreparedStatement pStmt2 = con.prepareStatement(sql2);
				 PreparedStatement pStmt3 = con.prepareStatement(sql3)) {
				
				//Se ejecutan las sentencias de creación de las tablas
		        if (!pStmt1.execute() && !pStmt2.execute() && !pStmt3.execute()) {
		        	logger.info("Se han creado las tablas");
		        }
			} catch (Exception ex) {
				logger.warning(String.format("Error al crear las tablas: %s", ex.getMessage()));
			}
		}
	}
	public void borrarBBDD() {
		//Sólo se borra la BBDD si la propiedad deleteBBDD es true
		if (properties.get("deleteBBDD").equals("true")) {	
			String sql1 = "DROP TABLE IF EXISTS pregunta;";
			String sql2 = "DROP TABLE IF EXISTS opcion";
			String sql3 = "DROP TABLE IF EXISTS usuario;";
			
	        //Se abre la conexión y se crea un PreparedStatement para borrar cada tabla
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt1 = con.prepareStatement(sql1);
				 PreparedStatement pStmt2 = con.prepareStatement(sql2);
				 PreparedStatement pStmt3 = con.prepareStatement(sql3)) {
				
				//Se ejecutan las sentencias de borrado de las tablas
		        if (!pStmt1.execute() && !pStmt2.execute() && !pStmt3.execute()) {
		        	logger.info("Se han borrado las tablas");
		        }
			} catch (Exception ex) {
				logger.warning(String.format("Error al borrar las tablas: %s", ex.getMessage()));
			}
			
			try {
				//Se borra físicamente el fichero de la BBDD
				Files.delete(Paths.get(databaseFile));
				logger.info("Se ha borrado el fichero de la BBDD");
			} catch (Exception ex) {
				logger.warning(String.format("Error al borrar el fichero de la BBDD: %s", ex.getMessage()));
			}
		}
	}
	public void borrarDatos() {
		//Sólo se borran los datos si la propiedad cleanBBDD es true
		if (properties.get("cleanBBDD").equals("true")) {	
			String sql1 = "DELETE FROM pregunta;";
			String sql2 = "DELETE FROM opcion;";
			String sql3 = "DELETE FROM usuario;";
			
	        //Se abre la conexión y se crea un PreparedStatement para borrar los datos de cada tabla
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt1 = con.prepareStatement(sql1);
				 PreparedStatement pStmt2 = con.prepareStatement(sql2);
				 PreparedStatement pStmt3 = con.prepareStatement(sql3)) {
				
				//Se ejecutan las sentencias de borrado de las tablas
		        if (!pStmt1.execute() && !pStmt2.execute() && !pStmt3.execute()) {
		        	logger.info("Se han borrado los datos");
		        }
			} catch (Exception ex) {
				logger.warning(String.format("Error al borrar los datos: %s", ex.getMessage()));
			}
		}
	}
	// Lee el fichero csv y añade todas las preguntas a una lista
	private List<Pregunta> cargarCSVPreguntas (){
		ArrayList<Pregunta> preguntas = new ArrayList<Pregunta>();
		try (BufferedReader in = new BufferedReader(new FileReader(PREGUNTAS_CSV))){
			String linea = null;
			Pregunta p = null;
			//Omitir la cabecera
			in.readLine();
			
			while((linea = in.readLine()) != null) {
				p = Pregunta.parseCSV(linea);
				if (p != null) {
					preguntas.add(p);
				}
			}
				
		} catch (Exception ex) {
			// TODO: handle exception
			logger.warning(String.format("Error leyendo preguntas del CSV: %s", ex.getMessage()));		}
		
		return preguntas;
	}
	// Añade las preguntas a la BBDD
	private void insertarPreguntas (ArrayList<Pregunta> preguntas) {
		//Definir la sentencia de SQl
		String sql = "INSERT INTO preguntas (cod_pregunta, pregunta, dificultad, categoria) VALUES (?, ?, ?, ?) ";
		try (Connection con = DriverManager.getConnection(connectionString);
				 PreparedStatement pStmt = con.prepareStatement(sql)) {
										
				//Se recorren los clientes y se insertan uno a uno
				for (Pregunta p : preguntas) {
					//Se añaden los parámetros al PreparedStatement
					pStmt.setInt(1, p.getCodigo());
					pStmt.setString(2, p.getPregunta());
					pStmt.setString(3, p.getCategoria());
					pStmt.setString(4, p.getDificultad().toString());
					
					if (pStmt.executeUpdate() != 1) {					
						logger.warning(String.format("No se ha insertado el Personaje: %s", p));
					} else {				
						logger.info(String.format("Se ha insertado el Personaje: %s", p));
					}
				}
				
				logger.info(String.format("%d Preguntas insertados en la BBDD", preguntas.size()));
			} catch (Exception ex) {
				logger.warning(String.format("Error al insertar preguntas: %s", ex.getMessage()));
			}
	}
	private void insertarOpciones (Opcion...opciones ) {
		String sql = "INSERT INTO opcion (cod_pregunta, opcion, es_correcta) VALUES (?, ?, ?) ";
		try (Connection con = DriverManager.getConnection(connectionString);
				 PreparedStatement pStmt = con.prepareStatement(sql)) {
										
				//Se recorren los clientes y se insertan uno a uno
				for (Opcion o : opciones) {
					//Se añaden los parámetros al PreparedStatement
					pStmt.setInt(1, o.getCod_pregunta());
					pStmt.setString(2, o.getTexto_opcion());
					pStmt.setInt(3, o.getEs_correcta());
					
					if (pStmt.executeUpdate() != 1) {					
						logger.warning(String.format("No se ha insertado el Personaje: %s", o));
					} else {				
						logger.info(String.format("Se ha insertado el Personaje: %s", o));
					}
				}
				
				logger.info(String.format("%d Preguntas insertados en la BBDD", opciones.length));
			} catch (Exception ex) {
				logger.warning(String.format("Error al insertar preguntas: %s", ex.getMessage()));
			}
	}

	// CSV CON FORMATO : cod_opcion, cod_pregunta, opcion, es_correcta

	private List<Opcion> cargarCSVOpciones (){
		List<Opcion> opciones = new ArrayList<Opcion>();
		
		try (BufferedReader in = new BufferedReader(new FileReader(OPCIONES_CSV))) {
			String linea = null;
			
			//Omitir la cabecera
			in.readLine();			
			
			while ((linea = in.readLine()) != null) {
				opciones.add(Opcion.parseCSV(linea));
			}			
			
		} catch (Exception ex) {
			logger.warning(String.format("Error leyendo opciones del CSV: %s", ex.getMessage()));
		}
		return opciones;
	}
}
