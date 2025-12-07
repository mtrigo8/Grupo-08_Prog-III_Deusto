package db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import domain.Equipo;
import domain.Jugador;
import domain.Jugador.TipoPosicion;
import domain.Liga;
import domain.Opcion;
import domain.Pregunta;
import domain.Usuario;
import domain.Pregunta.Dificultad;


public class GestorBD {
	
	private final String LOG_FOLDER = "resources/log";
	private final String PROPERTIES_FILE = "resources/config/app.properties";
	
	//CSV
	private final String CSV_LALIGA = "resources/data/laliga_calendario.csv";
	private final String CSV_BUNDESLIGA = "resources/data/bundesliga_calendario.csv";
	private final String CSV_PREMIER = "resources/data/premier_calendario.csv";
	private final String CSV_EQUIPOS = "resources/data/equipos.csv";
	private final String CSV_LIGAS = "resources/data/ligas.csv";
	private String PREGUNTAS_CSV = "resources/data/preguntas.csv";
	private String OPCIONES_CSV = "resources/data/opciones.csv"; 
	
	
	private Properties properties;
	private String driverName;
	private String databaseFile;
	private String connectionString;
	
	private static Logger logger = Logger.getLogger(GestorBD.class.getName());
	
	public GestorBD() {
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
	
	/**
	 * Inicializa la BBDD leyendo los datos de los ficheros CSV 
	 */
	public void initilizeFromCSV() {
		
		//Sólo se inicializa la BBDD si la propiedad initBBDD es true.
		if (properties.get("loadCSV") != null && properties.get("loadCSV").equals("true")) {
			//Se borran los datos, si existía alguno
			this.borrarDatos();
			
			//Se leen los equipos del CSV
			List<Equipo> equipos = this.loadCSVEquipos();
			//Se leen las ligas del CSV
			List<Liga> ligas = this.loadCVSLigas();	
			//lambda expression: enlaza los personajes con los comics porque al leer los
			//comics sólo se recuperan los nombres de los personajes y faltan el resto de
			//datos.
			updateEquipos(equipos, ligas);
			//Se insertan los equipos en la BBDD
			this.insertarEquipos(equipos.toArray(new Equipo[equipos.size()]));
			
						
			
			
			//Se insertan los comics en la BBDD
			this.insertarLigas(ligas.toArray(new Liga[ligas.size()]));	
			
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
		//Sólo se crea la BBDD si la propiedad initBBDD es true.
		if (properties.get("createBBDD").equals("true")) {
			
			String sql1 = "CREATE TABLE IF NOT EXISTS Equipo (\n"
	                + " nombre TEXT PRIMARY KEY,\n"
	                + " liga TEXT NOT NULL,\n"
	                + " estadio TEXT NOT NULL,\n"
	                + " ciudad TEXT NOT NULL,\n"
	                + " npng TEXT NOT NULL,\n"
	                + " anyofundacion INTEGER NOT NULL,\n"
	                + " titulos INTEGER NOT NULL,\n"
	                + " UNIQUE(nombre),\n"
	                + " FOREIGN KEY (liga) REFERENCES Liga(nombre) ON DELETE CASCADE)";
	
			String sql2 = "CREATE TABLE IF NOT EXISTS Liga (\n"
	                + " nombre TEXT PRIMARY KEY,\n"
	                + " pais TEXT NOT NULL,\n"
	                + " numeroEquipos TEXT NOT NULL\n"
	                + ");";
			String sql3 = "CREATE TABLE IF NOT EXISTS Jugador (\n"
	                + " id_comic INTEGER,\n"
	                + " id_personaje INTEGER,\n"
	                + " PRIMARY KEY(id_comic, id_personaje)\n"
	                + " FOREIGN KEY(id_comic) REFERENCES Comic(id) ON DELETE CASCADE\n"
	                + " FOREIGN KEY(id_personaje) REFERENCES Personaje(id) ON DELETE CASCADE\n"
	                + ");";
			
			String sql4 = "CREATE TABLE IF NOT EXISTS pregunta (\n"
	                + " cod_pregunta INTEGER PRIMARY KEY,\n"
	                + " pregunta TEXT NOT NULL,\n"
	                + " dificultad TEXT NOT NULL CHECK(dificultad IN ('FACIL', 'MEDIA', 'DIFICIL')),\n"
	                + " categoria TEXT NOT NULL\n"
	                + " );";
	
			String sql5 = "CREATE TABLE IF NOT EXISTS opcion (\n"
	                + " cod_opcion INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT,\n"
	                + " cod_pregunta INTEGER NOT NULL,\n"
	                + " opcion TEXT NOT NULL,\n"
	                + " es_correcta INTEGER NOT NULL DEFAULT 0,\n"
	                + " FOREIGN KEY(cod_pregunta) REFERENCES pregunta(cod_pregunta) ON DELETE CASCADE\n"
	                +" ON UPDATE CASCADE\n"
	                + ");"; 
	
			String sql6 = "CREATE TABLE IF NOT EXISTS usuario (" +
                    "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT NOT NULL, " +
                    "fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, " +
                    "puntuacion INTEGER DEFAULT 0" +
                    ");";
			String sql7 = "CREATE TABLE IF NOT EXISTS Jugador (\n"
					+ "cod_jugador INTEGER UNIQUE PRIMARY KEY AUTOINCREMENT,\n"
					+ "nom_jugador TEXT NOT NULL,\n"
					+ "num_camiseta INTEGER NOT NULL,\n"
					+ "posicion TEXT NOT NULL, \n"
					+ "nacionalidad TEXT NOT NULL,\n"
					+ "edad TEXT NOT NULL,\n"
					+ "FOREIGN KEY (equipo) REFERENCES Equipo(nombre) ON DELETE CASCADE);";
	        //Se abre la conexión y se crea un PreparedStatement para crer cada tabla
			//Al abrir la conexión, si no existía el fichero por defecto, se crea.
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt1 = con.prepareStatement(sql1);
				 PreparedStatement pStmt2 = con.prepareStatement(sql2);
				 PreparedStatement pStmt3 = con.prepareStatement(sql3);
				 PreparedStatement pStmt4 = con.prepareStatement(sql4);
				 PreparedStatement pStmt5 = con.prepareStatement(sql5);
			   	 PreparedStatement pStmt6 = con.prepareStatement(sql6);
				 PreparedStatement pStmt7 = con.prepareStatement(sql7)){
				
				//Se ejecutan las sentencias de creación de las tablas
		        if (!pStmt1.execute() && !pStmt2.execute() && !pStmt3.execute()&&!pStmt4.execute() && !pStmt5.execute() && !pStmt6.execute() && !pStmt7.execute()) {
		        	logger.info("Se han creado las tablas");
		        	System.out.println("Se han creado las tablas");
		        }
			} catch (Exception ex) {
				logger.warning(String.format("Error al crear las tablas: %s", ex.getMessage()));
			}
		}
	}
	
	/**
	 * Borra las tablas y el fichero de la BBDD.
	 */
	public void borrarBBDD() {
		//Sólo se borra la BBDD si la propiedad deleteBBDD es true
		if (properties.get("deleteBBDD").equals("true")) {	
			String sql1 = "DROP TABLE IF EXISTS Equipo;";
			String sql2 = "DROP TABLE IF EXISTS Liga";
			String sql3 = "DROP TABLE IF EXISTS Jugador;";
			String sql4 = "DROP TABLE IF EXISTS pregunta;";
			String sql5 = "DROP TABLE IF EXISTS opcion";
			String sql6 = "DROP TABLE IF EXISTS usuario;";
			String sql7 = "DROP TABLE IF EXISTS Jugador;";
			
	        //Se abre la conexión y se crea un PreparedStatement para borrar cada tabla
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt1 = con.prepareStatement(sql1);
				 PreparedStatement pStmt2 = con.prepareStatement(sql2);
				 PreparedStatement pStmt3 = con.prepareStatement(sql3);
				 PreparedStatement pStmt4 = con.prepareStatement(sql4);
				 PreparedStatement pStmt5 = con.prepareStatement(sql5);
				 PreparedStatement pStmt6 = con.prepareStatement(sql6);
				 PreparedStatement pStmt7 = con.prepareStatement(sql7)) {
				
				//Se ejecutan las sentencias de borrado de las tablas
		        if (!pStmt1.execute() && !pStmt2.execute() && !pStmt3.execute()
		        	&& !pStmt4.execute() && !pStmt5.execute() && !pStmt6.execute()
		        	&& !pStmt7.execute()) {
		        	logger.info("Se han borrado las tablas");
		        	System.out.println("Se han borrado las tablas");
		        }
			} catch (Exception ex) {
				logger.warning(String.format("Error al borrar las tablas: %s", ex.getMessage()));
			}
			
			try {
				//Se borra físicamente el fichero de la BBDD
				Files.delete(Paths.get(databaseFile));
				logger.info("Se ha borrado el fichero de la BBDD");
				System.out.println("Se ha borrado correctamente la BBDD");
			} catch (Exception ex) {
				logger.warning(String.format("Error al borrar el fichero de la BBDD: %s", ex.getMessage()));
			}
		}
	}
	
	/**
	 * Borra los datos de la BBDD.
	 */
	public void borrarDatos() {
		//Sólo se borran los datos si la propiedad cleanBBDD es true
		if (properties.get("cleanBBDD").equals("true")) {	
			String sql1 = "DELETE FROM Equipo;";
			String sql2 = "DELETE FROM Liga;";
			String sql3 = "DELETE FROM Jugador;";
			String sql4 = "DELETE FROM pregunta;";
			String sql5 = "DELETE FROM opcion;";
			String sql6 = "DELETE FROM usuario;";
			String sql7 = "DELETE FROM Jugador;";
	        //Se abre la conexión y se crea un PreparedStatement para borrar los datos de cada tabla
			try (Connection con = DriverManager.getConnection(connectionString);
			     PreparedStatement pStmt1 = con.prepareStatement(sql1);
				 PreparedStatement pStmt2 = con.prepareStatement(sql2);
				 PreparedStatement pStmt3 = con.prepareStatement(sql3);
				 PreparedStatement pStmt4 = con.prepareStatement(sql4);
				 PreparedStatement pStmt5 = con.prepareStatement(sql5);
				 PreparedStatement pStmt6 = con.prepareStatement(sql6);
				 PreparedStatement pStmt7 = con.prepareStatement(sql7)) {
				
				//Se ejecutan las sentencias de borrado de las tablas
		        if (!pStmt1.execute() && !pStmt2.execute() && !pStmt3.execute()
		        	&& !pStmt4.execute() && !pStmt5.execute() && !pStmt6.execute()
		        	&& !pStmt7.execute()) {
		        	logger.info("Se han borrado los datos");
		        }
			} catch (Exception ex) {
				logger.warning(String.format("Error al borrar los datos: %s", ex.getMessage()));
			}
		}
	}
	
	/**
	 * Inserta Equipos en la BBDD
	 */
	public void insertarEquipos(Equipo... equipos) {
		//Se define la plantilla de la sentencia SQL
		String sql = "INSERT INTO Equipo (liga, nombre, estadio, ciudad, anyofundacion, titulos, npng) VALUES (?, ?, ?, ?, ?, ?, ?);";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement pStmt = con.prepareStatement(sql)) {
									
			//Se recorren los clientes y se insertan uno a uno
			for (Equipo e : equipos) {
				//Se añaden los parámetros al PreparedStatement
				pStmt.setString(1, e.getLiga().getNombre());
				pStmt.setString(2, e.getNombre());
				pStmt.setString(3, e.getEstadio());
				pStmt.setString(4, e.getCiudad());
				pStmt.setInt(5, e.getAnyoFundacion());
				pStmt.setInt(6, e.getTitulos());
				pStmt.setString(7, e.getNombrePNGEquipo());
				
				if (pStmt.executeUpdate() != 1) {					
					logger.warning(String.format("No se ha insertado el equipo: %s", e));
				} else {
					//IMPORTANTE: El valor del ID del personaje se establece automáticamente al
					//insertarlo en la BBDD. Por lo tanto, después de insertar un personaje, 
					//se recupera de la BBDD para establecer el campo ID en el objeto que está
					//en memoria.				
					logger.info(String.format("Se ha insertado el equipo: %s", e));
				}
			}
			
			logger.info(String.format("%d equipos insertados en la BBDD", equipos.length));
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar ligas: %s", ex.getMessage()));
		}			
	}
	
	
	/**
	 * Inserta Ligas en la BBDD
	 */
	public void insertarLigas(Liga... ligas) {
		//Se define la plantilla de la sentencia SQL			
		String sql = "INSERT INTO Liga (nombre, pais, numeroEquipos) VALUES (?, ?, ?);";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement pStmt = con.prepareStatement(sql)) {
			
			//Se recorren los clientes y se insertan uno a uno
			for (Liga l : ligas) {
				//Se definen los parámetros de la sentencia SQL
				pStmt.setString(1, l.getNombre());
				pStmt.setString(2, l.getPais());
				pStmt.setInt(3, l.getNumeroEquipos());
				
				if (pStmt.executeUpdate() != 1) {					
					logger.warning(String.format("No se ha insertado la Liga: %s", l));
				} else {
					//IMPORTANTE: El valor del ID del comic se establece automáticamente al
					//insertarlo en la BBDD. Por lo tanto, después de insertar un comic, 
					//se recupera de la BBDD para establecer el campo ID en el objeto que está
					//en memoria.				
					
					
					logger.info(String.format("Se ha insertado la Liga: %s", l));
				}
			}
			
			logger.info(String.format("%d Comics insertados en la BBDD", ligas.length));
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar ligas: %s", ex.getMessage()));
		}				
	}
	
	/**
	 * Inserta Jugadores en la BBDD
	 */
	public void insertarJugadores(Jugador... jugadores) {
		//Se define la plantilla de la sentencia SQL
		String sql = "INSERT INTO Jugador (cod_jugador, nom_jugador, num_camiseta, posicion, nacionalidad, edad, equipo) VALUES (?, ?, ?, ?, ?, ?, ?);";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement pStmt = con.prepareStatement(sql)) {
									
			//Se recorren los clientes y se insertan uno a uno
			for (Jugador j : jugadores) {
				//Se añaden los parámetros al PreparedStatement
				pStmt.setInt(1, j.getCod_jugador());
				pStmt.setString(2, j.getNombre());
				pStmt.setInt(3, j.getNumeroCamiseta());
				pStmt.setString(4, j.getPosicion().toString());
				pStmt.setString(5, j.getNacionalidad());
				pStmt.setInt(6, j.getEdad());
				pStmt.setString(7, j.getEquipo().toString());
				
				if (pStmt.executeUpdate() != 1) {					
					logger.warning(String.format("No se ha insertado el jugador: %s", j));
				} else {
					//IMPORTANTE: El valor del ID del personaje se establece automáticamente al
					//insertarlo en la BBDD. Por lo tanto, después de insertar un personaje, 
					//se recupera de la BBDD para establecer el campo ID en el objeto que está
					//en memoria.				
					logger.info(String.format("Se ha insertado el jugador: %s", j));
				}
			}
			
			logger.info(String.format("%d equipos insertados en la BBDD", jugadores.length));
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar ligas: %s", ex.getMessage()));
		}			
	}
	
		
	/**
	 * IMPORTANTE: La información del CSV de los comics sólo trae el nombre de los personajes.
	 * Este método procesa cada comic y reemplaza cada personaje leído desde el CSV (que sólo tiene el nombre)
	 * por el objeto personaje con todos los datos.
	 * @param comic Comic cuyos personajes va a procesarse.
	 * @param personajes List<Personaje> con los personajes que tienen todos los datos.
	 */
	private void updateEquipos(List<Equipo> equipos, List<Liga> ligas) {
		for (Liga liga : ligas) {
			for (Equipo equipo : equipos) {
				if (equipo.getNomLiga().equals(liga.getNombre())) {
					equipo.setLiga(liga);
					liga.getEquipos().add(equipo);
				}
			}
		}
	}
	private void updateJugadores(List<Jugador> jugadores, List<Equipo> equipos) {
		for (Equipo equipo : equipos) {
			for (Jugador jugador : jugadores ) {
				if (jugador.getEquipo().equals(equipo.getNombre())) {
					jugador.setEquipo(equipo);
					HashMap<TipoPosicion, ArrayList<Jugador>>mapaJugadores = equipo.getJugadores();
					if(!mapaJugadores.containsKey(jugador.getPosicion())) {
						mapaJugadores.put(jugador.getPosicion(), new ArrayList<Jugador>());
					}
					mapaJugadores.get(jugador.getPosicion()).add(jugador);
				}
			}
		}
	}
	
	private List<Equipo> loadCSVEquipos() {
		List<Equipo> equipos = new ArrayList<>();
		
		try (BufferedReader in = new BufferedReader(new FileReader(CSV_EQUIPOS))) {
			String linea = null;
			//Omitir la cabecera
			in.readLine();		
			
			while ((linea = in.readLine()) != null) {
				String[] campos = linea.split(";");
				Equipo e = new Equipo(campos[0], campos[1], null, Integer.parseInt(campos[3]), Integer.parseInt(campos[4]), campos[5], campos[6], campos[2]);
				equipos.add(e);
			}			
			
		} catch (Exception ex) {
			logger.warning(String.format("Error leyendo equipos del CSV: %s", ex.getMessage()));
		}
		
		return equipos;
	}
	
	private List<Liga> loadCVSLigas() {
		List<Liga> ligas = new ArrayList<>();
		
		try (BufferedReader in = new BufferedReader(new FileReader(CSV_LIGAS))) {
			String linea = null;
			//Omitir la cabecera
			in.readLine();		
			
			while ((linea = in.readLine()) != null) {
				String[] campos = linea.split(";");
				Liga l = new Liga(campos[0], campos[1], Integer.parseInt(campos[2]), new ArrayList<Equipo>());
				ligas.add(l);
			}			
			
		} catch (Exception ex) {
			logger.warning(String.format("Error leyendo ligas del CSV: %s", ex.getMessage()));
		}
		
		return ligas;
	}
	
	//MODIFICACIÓN 4: Guarda una lista de comics en un CSV
	public void storeCSVLigas(List<Liga> ligas) {
		if (ligas != null) {
			try (PrintWriter out = new PrintWriter(new File(CSV_LIGAS))) {
				out.println("NOMBRE;PAIS;NTITULOS");
				ligas.forEach(l -> out.println(l.getNombre() + ";" + l.getPais() + ";" + l.getNumeroEquipos() + ";"));			
				logger.info("Se han guardado los ligas en un CSV.");
			} catch (Exception ex) {
				logger.warning(String.format("Error guardando ligas en el CSV: %s", ex.getMessage()));
			}
		}
	}
	
	//MODIFICACIÓN 4: Guarda una lista de personajes en un CSV
	public void storeCSVEquipos(List<Equipo> equipos) {
		if (equipos != null) {
			try (PrintWriter out = new PrintWriter(new File(CSV_EQUIPOS))) {
				out.println("NOMBRE;CIUDAD;LIGA;ANYOFUNDACION;TITULOS;ESTADIO;NOMBREFICHERO");
				equipos.forEach(e -> out.println(e.getNombre() + ";" + e.getCiudad() + ";" + e.getLiga().getNombre() + ";" + e.getAnyoFundacion() + ";" + e.getTitulos() + ";" + e.getEstadio() + ";" + e.getNombrePNGEquipo()));
				logger.info("Se han guardado los equipos en un CSV.");
			} catch (Exception ex) {
				logger.warning(String.format("Error guardando equipos en el CSV: %s", ex.getMessage()));
			}			
		}
	}
	//========================================
	//FUNCIONES DE LA BBDD QUE AFECTAN AL QUIZ
	//========================================
	
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
			String sql = "INSERT INTO pregunta (cod_pregunta, pregunta, dificultad, categoria) VALUES (?, ?, ?, ?) ";
			try (Connection con = DriverManager.getConnection(connectionString);
					 PreparedStatement pStmt = con.prepareStatement(sql)) {
											
					//Se recorren los clientes y se insertan uno a uno
					for (Pregunta p : preguntas) {
						//Se añaden los parámetros al PreparedStatement
						pStmt.setInt(1, p.getCodigo());
						pStmt.setString(2, p.getPregunta());
						pStmt.setString(3, p.getDificultad().toString()); 
		                pStmt.setString(4, p.getCategoria());
						
						if (pStmt.executeUpdate() != 1) {					
							logger.warning(String.format("No se ha insertado la pregunta: %s", p));
						} else {				
							logger.info(String.format("Se ha insertado la pregunta: %s", p));
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
							logger.warning(String.format("No se ha insertado la opcion: %d", o.getCod_opcion()));
						} else {				
							logger.info(String.format("Se ha insertado la opcion: %d", o.getCod_opcion()));
						}
					}
					
					logger.info(String.format("%d Preguntas insertados en la BBDD", opciones.length));
				} catch (Exception ex) {
					logger.warning(String.format("Error al insertar opciones: %s", ex.getMessage()));
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
		
		// Seleccionar una pregunta aleatoria diferente a las seleccionadas previamente
		
		public Pregunta cargarPreguntaAleatoria (Set<Pregunta> preguntasMostradas) {
			String sql1 = "SELECT * FROM pregunta "+
						"WHERE cod_pregunta NOT IN ("+ getIdsExcluidos(preguntasMostradas) +")" + 
						"ORDER BY RANDOM() LIMIT 1";
			Pregunta pregAleatoria = null;
			try (Connection con = DriverManager.getConnection(connectionString);
					PreparedStatement pst = con.prepareStatement(sql1)){
				ResultSet rs = pst.executeQuery();
				
				while (rs.next()) {
					int cod_pregunta = rs.getInt("cod_pregunta");
					String pregunta = rs.getString("pregunta");
					Dificultad dif = Dificultad.valueOf(rs.getString("dificultad").toUpperCase());
					String categoria = rs.getString("categoria");
					
					pregAleatoria = new Pregunta(cod_pregunta, pregunta, dif, categoria);
					preguntasMostradas.add(pregAleatoria);
					}
			} catch (Exception e) {
				System.err.println("Error al seleccionar la pregunta de la base de datos: " +e.getMessage());
			}
			
			return pregAleatoria;
		}
		public List<Opcion> cargarOpcionesDePregunta (Pregunta pregunta){
			List<Opcion> opciones = new ArrayList<Opcion>();
			
			String sql = "SELECT * FROM opcion WHERE cod_pregunta = ?";
			
			try (Connection con = DriverManager.getConnection(connectionString);
					PreparedStatement pst = con.prepareStatement(sql)){
				pst.setInt(1, pregunta.getCodigo());
				ResultSet rs = pst.executeQuery();
				
				while(rs.next()) {
		            String textoOpcion = rs.getString("opcion");
		            int esCorrecta = rs.getInt("es_correcta");
		            int codPregunta = rs.getInt("cod_pregunta");
		            
		            opciones.add(new Opcion(codPregunta, textoOpcion, esCorrecta));
				}
				
			} catch (Exception e) {
				System.err.println("Error al cargar opciones de la pregunta: " + e.getMessage());
			}
			return opciones;
		}
		public void insertarUsuario (Usuario usuario) {
			String sql = "INSERT INTO usuario (nombre, puntuacion) VALUES (?, ?) ";
			try (Connection con = DriverManager.getConnection(connectionString);
					 PreparedStatement pStmt = con.prepareStatement(sql)) {
					
					pStmt.setString(1, usuario.getNombre());
					pStmt.setInt(2, usuario.getPuntuacion());
					pStmt.executeUpdate();
					logger.info(String.format("Usuario: " + usuario.getNombre() + "añadido a la BBDD"));
				} catch (Exception ex) {
					logger.warning(String.format("Error al insertar usuario: %s", ex.getMessage()));
				}
			
		}
		public List<Usuario> cargarClasificacion() {
		    List<Usuario> clasificacion = new ArrayList<>();
		    String sql = "SELECT nombre, puntuacion FROM usuario ORDER BY puntuacion DESC"; 

		    try (Connection con = DriverManager.getConnection(connectionString);
		         PreparedStatement pst = con.prepareStatement(sql);
		         ResultSet rs = pst.executeQuery()) {

		        while (rs.next()) {
		            String nombre = rs.getString("nombre");
		            int puntuacion = rs.getInt("puntuacion");
		            clasificacion.add(new Usuario(nombre, puntuacion)); 
		        }

		    } catch (Exception e) {
		        System.err.println("Error al cargar la clasificación: " + e.getMessage());
		        logger.warning(String.format("Error al cargar la clasificación: %s", e.getMessage()));
		    }
		    return clasificacion;
		}
		
		private String getIdsExcluidos (Set<Pregunta> preguntasMostradas) {
			//Si las preguntas mostradas son null o esta vacio el set no hay preguntas 
			//entonces se pone -1 (No hay ningun codigo de preguntacon -1)
			if (preguntasMostradas == null || preguntasMostradas.isEmpty()){
				return "-1";
			}
			
			StringBuilder sb = new StringBuilder();
		    Iterator<Pregunta> iterator = preguntasMostradas.iterator();
		    
		    while (iterator.hasNext()) {
		        sb.append(iterator.next().getCodigo());
		        if (iterator.hasNext()) {
		            sb.append(",");
		        }
		    }
		    
		    return sb.toString();
			
		}
}