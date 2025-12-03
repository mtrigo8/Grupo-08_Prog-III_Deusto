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
import java.util.List;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import domain.Equipo;
import domain.Liga;


public class GestorBD {
	
	private final String PROPERTIES_FILE = "resources/config/app.properties";
	private final String CSV_LALIGA = "resources/data/laliga_calendario.csv";
	private final String CSV_BUNDESLIGA = "resources/data/bundesliga_calendario.csv";
	private final String CSV_PREMIER = "resources/data/premier_calendario.csv";
	private final String CSV_EQUIPOS = "resources/data/equipos.csv";
	private final String CSV_LIGAS = "resources/data/ligas.csv";
	private final String LOG_FOLDER = "resources/log";
	
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
		if (properties.get("loadCSV").equals("true")) {
			//Se borran los datos, si existía alguno
			this.borrarDatos();
			
			//Se leen los personajes del CSV
			List<Equipo> equipos = this.loadCSVEquipos();
			//Se insertan los personajes en la BBDD
			this.insertarEquipos(equipos.toArray(new Equipo[equipos.size()]));
			
			//Se leen los comics del CSV
			List<Liga> ligas = this.loadCVSLigas();				
			//lambda expression: enlaza los personajes con los comics porque al leer los
			//comics sólo se recuperan los nombres de los personajes y faltan el resto de
			//datos.
			updateEquipos(equipos, ligas);
			
			//Se insertan los comics en la BBDD
			this.insertarComic(comics.toArray(new Comic[comics.size()]));				
		}
	}

	public void crearBBDD() {
		//Sólo se crea la BBDD si la propiedad initBBDD es true.
		if (properties.get("createBBDD").equals("true")) {
			//La base de datos tiene 3 tablas: Personaje, Comic y Personajes_Comic
			String sql1 = "CREATE TABLE IF NOT EXISTS Equipo (\n"
	                + " nombre TEXT PRIMARY KEY,\n"
	                + " liga TEXT NOT NULL,\n"
	                + " estadio TEXT NOT NULL,\n"
	                + " ciudad TEXT NOT NULL,\n"
	                + " npng TEXT NOT NULL,\n"
	                + " anyofundacion INTEGER NOT NULL,\n"
	                + " titulos INTEGER NOT NULL,\n"
	                + " UNIQUE(nombre),\n"
	                + " UNIQUE KEY `nombre_UNIQUE` (`nombre`),\n"
	                + "  KEY `nombreLiga_idx` (`liga`),\n"
	                + "  CONSTRAINT `nombreLiga` FOREIGN KEY (`liga`) REFERENCES `liga` (`nombre`) ON DELETE CASCADE);";
	
			String sql2 = "CREATE TABLE IF NOT EXISTS Liga (\n"
	                + " nombre TEXT PRIMARY KEY,\n"
	                + " pais TEXT NOT NULL,\n"
	                + " numeroEquipos TEXT NOT NULL\n"
	                + ");";
			//Queda por añadir la tabla de usuarios
			String sql3 = "CREATE TABLE IF NOT EXISTS Jugador (\n"
	                + " id_comic INTEGER,\n"
	                + " id_personaje INTEGER,\n"
	                + " PRIMARY KEY(id_comic, id_personaje)\n"
	                + " FOREIGN KEY(id_comic) REFERENCES Comic(id) ON DELETE CASCADE\n"
	                + " FOREIGN KEY(id_personaje) REFERENCES Personaje(id) ON DELETE CASCADE\n"
	                + ");";
			
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
	
	/**
	 * Borra las tablas y el fichero de la BBDD.
	 */
	public void borrarBBDD() {
		//Sólo se borra la BBDD si la propiedad deleteBBDD es true
		if (properties.get("deleteBBDD").equals("true")) {	
			String sql1 = "DROP TABLE IF EXISTS Equipo;";
			String sql2 = "DROP TABLE IF EXISTS Liga";
			String sql3 = "DROP TABLE IF EXISTS Jugador;";
			
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
	
	/**
	 * Borra los datos de la BBDD.
	 */
	public void borrarDatos() {
		//Sólo se borran los datos si la propiedad cleanBBDD es true
		if (properties.get("cleanBBDD").equals("true")) {	
			String sql1 = "DELETE FROM Equipo;";
			String sql2 = "DELETE FROM Liga;";
			String sql3 = "DELETE FROM Jugador;";
			
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
	
	/**
	 * Inserta Equipos en la BBDD
	 */
	public void insertarEquipos(Equipo... equipos) {
		//Se define la plantilla de la sentencia SQL
		String sql = "INSERT INTO Equipo (liga, nombre, estadio, ciudad, anyofun, titulos, npng) VALUES (?, ?, ?, ?, ?, ?, ?);";
		
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
					logger.warning(String.format("No se ha insertado el Personaje: %s", e));
				} else {
					//IMPORTANTE: El valor del ID del personaje se establece automáticamente al
					//insertarlo en la BBDD. Por lo tanto, después de insertar un personaje, 
					//se recupera de la BBDD para establecer el campo ID en el objeto que está
					//en memoria.				
					logger.info(String.format("Se ha insertado el Personaje: %s", e));
				}
			}
			
			logger.info(String.format("%d Personajes insertados en la BBDD", equipos.length));
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar personajes: %s", ex.getMessage()));
		}			
	}
	
	/**
	 * Inserta Comics en la BBDD
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
					logger.warning(String.format("No se ha insertado el Comic: %s", c));
				} else {
					//IMPORTANTE: El valor del ID del comic se establece automáticamente al
					//insertarlo en la BBDD. Por lo tanto, después de insertar un comic, 
					//se recupera de la BBDD para establecer el campo ID en el objeto que está
					//en memoria.				
					
					
					logger.info(String.format("Se ha insertado el Comic: %s", c));
				}
			}
			
			logger.info(String.format("%d Comics insertados en la BBDD", comics.length));
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar comics: %s", ex.getMessage()));
		}				
	}
		
	//MODIFICACIÓN 1: Método para actualizar la información de un Comic en la BBDD
	/**
	 * Actualiza un Comic
	 */
	public void actualizarComic(Comic comic) {
		//Se define la plantilla de la sentencia SQL			
		String sql = "UPDATE Comic SET editorial = ?, titulo = ? WHERE id = ?;";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement pStmt = con.prepareStatement(sql)) {
			
			//Se definen los parámetros de la sentencia SQL
			pStmt.setString(1, comic.getEditorial().toString());
			pStmt.setString(2, comic.getTitulo());
			pStmt.setInt(3, comic.getId());
				
			if (pStmt.executeUpdate() != 1) {					
				logger.warning(String.format("No se ha actualizado el Comic: %s", comic.getTitulo()));
			} else {					
				logger.info(String.format("Se ha actualizado el Comic: %s", comic.getTitulo()));
			}			
		} catch (Exception ex) {
			logger.warning(String.format("Error al actualizar comic: %s", ex.getMessage()));
		}				
	}
	

	
	/**
	 * Borra la relación entre un pesonaje y un comic en la BBDD. 
	 */
	public void borrarPersonajeComic(int idComic, int idPersonaje) {
		String sql = "DELETE FROM Personajes_Comic WHERE id_comic = ? AND id_personaje = ?;";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement pStmt = con.prepareStatement(sql)) {
				
			//Se añaden los parámetros al PreparedStatement
			pStmt.setInt(1, idComic);
			pStmt.setInt(2, idPersonaje);
				
			if (pStmt.executeUpdate() != 1) {					
				logger.warning(String.format("No se ha borrado el personaje %d del comic %d.", idComic, idPersonaje));
			} else {
				logger.info(String.format("Se ha borrado el personaje %d del comic %d.", idComic, idPersonaje));
			}
		} catch (Exception ex) {
			logger.warning(String.format("Error al borrar personaje de un comic: %s", ex.getMessage()));
		}				
	}
	
	/**
	 * Recupera los Personajes de la BBDD.
	 */
	public List<Personaje> getPersonajes() {
		List<Personaje> personajes = new ArrayList<>();
		String sql = "SELECT * FROM Personaje";
		
		//Se abre la conexión sy se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			
			//Se ejecuta la sentencia y se obtiene el ResultSet
			ResultSet rs = pStmt.executeQuery();			
			Personaje personaje;
			
			//Se recorre el ResultSet y se crean objetos
			while (rs.next()) {
				personaje = new Personaje(rs.getInt("id"), 
						rs.getString("nombre"), 
						rs.getString("email"), 
						Editorial.valueOf(rs.getString("editorial")));
				
				//Se inserta cada nuevo cliente en la lista de clientes
				personajes.add(personaje);
			}
			
			//Se cierra el ResultSet
			rs.close();
			
			logger.info(String.format("Se han recuperado %d personajes.", personajes.size()));			
		} catch (Exception ex) {
			logger.warning(String.format("Error recuperar los personajes: %s", ex.getMessage()));						
		}		
		
		return personajes;
	}
	
	

	
	/**
	 * Recupera de la BBDD un Personaje a partir de su nombre. 
	 */
	public Personaje getPersonajeByNombre(String nombre) {
		Personaje personaje = null;
		String sql = "SELECT * FROM Personaje WHERE nombre = ? LIMIT 1";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			
			//Se definen los parámetros de la sentencia SQL
			pStmt.setString(1, nombre);
			
			//Se ejecuta la sentencia y se obtiene el ResultSet con los resutlados
			ResultSet rs = pStmt.executeQuery();			

			//Se procesa el único resultado
			if (rs.next()) {
				personaje = new Personaje(rs.getInt("id"), 
						rs.getString("nombre"), 
						rs.getString("email"), 
						Editorial.valueOf(rs.getString("editorial")));
			}
			
			//Se cierra el ResultSet
			rs.close();
			
			logger.info(String.format("Se ha recuperado el personaje %s", personaje));			
		} catch (Exception ex) {
			logger.warning(String.format("Error recuperar el personaje con nombre %s: %s", nombre, ex.getMessage()));						
		}		
		
		return personaje;
	}
	
	/**
	 * Recupera los Comics de la BBDD. 
	 */
	public List<Comic> getComics() {
		List<Comic> comics = new ArrayList<>();
		String sql = "SELECT * FROM Comic";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			
			//Se ejecuta la sentencia y se obtiene el ResultSet con los resutlados
			ResultSet rs = pStmt.executeQuery();			
			Comic comic;
			
			//Se recorre el ResultSet y se crean los Comics
			while (rs.next()) {
				comic = new Comic(rs.getInt("id"), 
							Editorial.valueOf(rs.getString("editorial")),
							rs.getString("titulo"));
				
				//Se recuperan los IDs de los personajes del Comic
				List<Integer> idsPersonaje = this.getIdsPersonajesComic(comic);
				
				//A partir de los IDs, se van recuperando los personajes de la BBDD
				//y se añaden al comic.
				for(int id : idsPersonaje) {
					comic.addPersonaje(this.getPersonajeById(id));
				}
				
				//Se inserta cada nuevo cliente en la lista de clientes
				comics.add(comic);
			}
			
			//Se cierra el ResultSet
			rs.close();
			
			logger.info(String.format("Se han recuperado %d comics", comics.size()));			
		} catch (Exception ex) {
			logger.warning(String.format("Error recuperar los comics: %s", ex.getMessage()));						
		}		
		
		return comics;
	}
	
	public Comic getComicByTitulo(String titulo) {
		Comic comic = null;
		String sql = "SELECT * FROM Comic WHERE titulo = ? LIMIT 1";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			
			//Se definen los parámetros de la sentencia SQL
			pStmt.setString(1, titulo);
			
			//Se ejecuta la sentencia y se obtiene el ResultSet con los resutlados
			ResultSet rs = pStmt.executeQuery();			

			//Se procesa el único resultado
			if (rs.next()) {
				comic = new Comic(rs.getInt("id"), 
						Editorial.valueOf(rs.getString("editorial")),
						rs.getString("titulo"));

				//Se recuperan los personajes del comic
				List<Integer> idsPersonaje = this.getIdsPersonajesComic(comic);
				
				//Se recuperan los personajes de la BBDD
				for(int id : idsPersonaje) {
					comic.addPersonaje(this.getPersonajeById(id));
				}
			}
			
			//Se cierra el ResultSet
			rs.close();
			
			logger.info(String.format("Se ha recuperado el comic %s", comic));			
		} catch (Exception ex) {
			logger.warning(String.format("Error recuperar el comic con nombre %s: %s", titulo, ex.getMessage()));						
		}		
		
		return comic;
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
			logger.warning(String.format("Error leyendo personajes del CSV: %s", ex.getMessage()));
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
			logger.warning(String.format("Error leyendo personajes del CSV: %s", ex.getMessage()));
		}
		
		return ligas;
	}
	
	//MODIFICACIÓN 4: Guarda una lista de comics en un CSV
	public void storeCSVLigas(List<Liga> ligas) {
		if (ligas != null) {
			try (PrintWriter out = new PrintWriter(new File(CSV_LIGAS))) {
				out.println("NOMBRE;PAIS;NTITULOS");
				ligas.forEach(l -> out.println(l.getNombre() + ";" + l.getPais() + ";" + l.getNumeroEquipos() + ";"));			
				logger.info("Se han guardado los comics en un CSV.");
			} catch (Exception ex) {
				logger.warning(String.format("Error guardando comics en el CSV: %s", ex.getMessage()));
			}
		}
	}
	
	//MODIFICACIÓN 4: Guarda una lista de personajes en un CSV
	public void storeCSVEquipos(List<Equipo> equipos) {
		if (equipos != null) {
			try (PrintWriter out = new PrintWriter(new File(CSV_EQUIPOS))) {
				out.println("NOMBRE;CIUDAD;LIGA;ANYOFUNDACION;TITULOS;ESTADIO;NOMBREFICHERO");
				equipos.forEach(p -> out.println());
				logger.info("Se han guardado los personajes en un CSV.");
			} catch (Exception ex) {
				logger.warning(String.format("Error guardando personajes en el CSV: %s", ex.getMessage()));
			}			
		}
	}
}