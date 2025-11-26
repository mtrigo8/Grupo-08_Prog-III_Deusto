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


public class GestorBD {

	private final String PROPERTIES_FILE = "resources/config/app.properties";
	private final String CSV_LALIGA = "resources/data/laliga_calendario.csv";
	private final String CSV_BUNDESLIGA = "resources/data/bundesliga_calendario.csv";
	private final String CSV_PREMIER = "resources/data/premier_calendario.csv";
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
			List<Personaje> personajes = this.loadCSVPersonajes();
			//Se insertan los personajes en la BBDD
			this.insertarPesonaje(personajes.toArray(new Personaje[personajes.size()]));
			
			//Se leen los comics del CSV
			List<Comic> comics = this.loadCVSComics();				
			//lambda expression: enlaza los personajes con los comics porque al leer los
			//comics sólo se recuperan los nombres de los personajes y faltan el resto de
			//datos.
			comics.forEach(c -> updatePersonajes(c, personajes));
			
			//Se insertan los comics en la BBDD
			this.insertarComic(comics.toArray(new Comic[comics.size()]));				
		}
	}

	public void crearBBDD() {
		//Sólo se crea la BBDD si la propiedad initBBDD es true.
		if (properties.get("createBBDD").equals("true")) {
			//La base de datos tiene 3 tablas: Personaje, Comic y Personajes_Comic
			String sql1 = "CREATE TABLE IF NOT EXISTS Personaje (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " editorial TEXT NOT NULL,\n"
	                + " nombre TEXT NOT NULL,\n"
	                + " email TEXT NOT NULL,\n"
	                + " UNIQUE(nombre, email));";
	
			String sql2 = "CREATE TABLE IF NOT EXISTS Comic (\n"
	                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
	                + " editorial TEXT NOT NULL,\n"
	                + " titulo TEXT UNIQUE NOT NULL\n"
	                + ");";
	
			String sql3 = "CREATE TABLE IF NOT EXISTS Personajes_Comic (\n"
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
			String sql1 = "DROP TABLE IF EXISTS Personaje;";
			String sql2 = "DROP TABLE IF EXISTS Comic";
			String sql3 = "DROP TABLE IF EXISTS Personajes_Comic;";
			
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
			String sql1 = "DELETE FROM Personaje;";
			String sql2 = "DELETE FROM Comic;";
			String sql3 = "DELETE FROM Personajes_Comic;";
			
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
	 * Inserta Personajes en la BBDD
	 */
	public void insertarPesonaje(Personaje... personajes) {
		//Se define la plantilla de la sentencia SQL
		String sql = "INSERT INTO Personaje (editorial, nombre, email) VALUES (?, ?, ?);";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement pStmt = con.prepareStatement(sql)) {
									
			//Se recorren los clientes y se insertan uno a uno
			for (Personaje p : personajes) {
				//Se añaden los parámetros al PreparedStatement
				pStmt.setString(1, p.getEditorial().toString());
				pStmt.setString(2, p.getNombre());
				pStmt.setString(3, p.getEmail());
				
				if (pStmt.executeUpdate() != 1) {					
					logger.warning(String.format("No se ha insertado el Personaje: %s", p));
				} else {
					//IMPORTANTE: El valor del ID del personaje se establece automáticamente al
					//insertarlo en la BBDD. Por lo tanto, después de insertar un personaje, 
					//se recupera de la BBDD para establecer el campo ID en el objeto que está
					//en memoria.
					p.setId(this.getPersonajeByNombre(p.getNombre()).getId());					
					logger.info(String.format("Se ha insertado el Personaje: %s", p));
				}
			}
			
			logger.info(String.format("%d Personajes insertados en la BBDD", personajes.length));
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar personajes: %s", ex.getMessage()));
		}			
	}
	
	/**
	 * Inserta Comics en la BBDD
	 */
	public void insertarComic(Comic... comics) {
		//Se define la plantilla de la sentencia SQL			
		String sql = "INSERT INTO Comic (editorial, titulo) VALUES (?, ?);";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement pStmt = con.prepareStatement(sql)) {
			
			//Se recorren los clientes y se insertan uno a uno
			for (Comic c : comics) {
				//Se definen los parámetros de la sentencia SQL
				pStmt.setString(1, c.getEditorial().toString());
				pStmt.setString(2, c.getTitulo());
				
				if (pStmt.executeUpdate() != 1) {					
					logger.warning(String.format("No se ha insertado el Comic: %s", c));
				} else {
					//IMPORTANTE: El valor del ID del comic se establece automáticamente al
					//insertarlo en la BBDD. Por lo tanto, después de insertar un comic, 
					//se recupera de la BBDD para establecer el campo ID en el objeto que está
					//en memoria.
					c.setId(this.getComicByTitulo(c.getTitulo()).getId());					
					
					//Se guarda la relación entre personajes y comics en la BBDD.
					for (Personaje p : c.getPersonajes()) {
						this.insertarPersonajeComic(c.getId(), p.getId());
					}
					
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
	 * Almacena la relación entre un pesonaje y un comic en la BBDD. 
	 */
	public void insertarPersonajeComic(int idComic, int idPersonaje) {
		String sql = "INSERT INTO Personajes_Comic (id_comic, id_personaje) VALUES (?, ?);";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
			 PreparedStatement pStmt = con.prepareStatement(sql)) {
				
			//Se añaden los parámetros al PreparedStatement
			pStmt.setInt(1, idComic);
			pStmt.setInt(2, idPersonaje);
				
			if (pStmt.executeUpdate() != 1) {					
				logger.warning(String.format("No se ha insertado el personaje %d del comic %d.", idComic, idPersonaje));
			} else {
				logger.info(String.format("Se ha insertado el personaje %d del comic %d.", idComic, idPersonaje));
			}
		} catch (Exception ex) {
			logger.warning(String.format("Error al insertar personaje del comic: %s", ex.getMessage()));
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
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
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
	 * Recupera de la BBDD un Personaje a partir de su ID 
	 */
	public Personaje getPersonajeById(int id) {
		Personaje personaje = null;
		String sql = "SELECT * FROM Personaje WHERE id = ? LIMIT 1";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			
			//Se definen los parámetros de la sentencia SQL
			pStmt.setInt(1, id);
			
			//Se ejecuta la sentencia y se obtiene el ResultSet
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
			logger.warning(String.format("Error recuperar los personajes con id %d: %s", id, ex.getMessage()));						
		}		
		
		return personaje;
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
	
	public List<Integer> getIdsPersonajesComic(Comic comic) {
		List<Integer> idsPersonaje = new ArrayList<>();		
		String sql = "SELECT id_personaje FROM Personajes_Comic WHERE id_comic = ?";
		
		//Se abre la conexión y se crea el PreparedStatement con la sentencia SQL
		try (Connection con = DriverManager.getConnection(connectionString);
		     PreparedStatement pStmt = con.prepareStatement(sql)) {			
			
			//Se definen los parámetros de la sentencia SQL
			pStmt.setInt(1, comic.getId());
			
			//Se ejecuta la sentencia y se obtiene el ResultSet con los resutlados
			ResultSet rs = pStmt.executeQuery();			

			//Se procesa el único resultado
			while (rs.next()) {
				idsPersonaje.add(rs.getInt("id_personaje"));
			}
			
			//Se cierra el ResultSet
			rs.close();
			
			logger.info(String.format("Se han recuperado %d ids de los personajes del comic %s", 
					idsPersonaje.size(), comic.getTitulo()));			
		} catch (Exception ex) {
			logger.warning(String.format("Error recuperar los ids de los personajes del comic %d: %s",
					comic.getTitulo(), ex.getMessage()));						
		}		
		
		return idsPersonaje;
	}
	
	/**
	 * IMPORTANTE: La información del CSV de los comics sólo trae el nombre de los personajes.
	 * Este método procesa cada comic y reemplaza cada personaje leído desde el CSV (que sólo tiene el nombre)
	 * por el objeto personaje con todos los datos.
	 * @param comic Comic cuyos personajes va a procesarse.
	 * @param personajes List<Personaje> con los personajes que tienen todos los datos.
	 */
	private void updatePersonajes(Comic comic, List<Personaje> personajes) {		
		for (int i=0; i<comic.getPersonajes().size();i++) {
			
			for (Personaje p : personajes) {
				if (comic.getPersonajes().get(i).getNombre().equals(p.getNombre())) {
					comic.getPersonajes().set(i, p);
				}
			}
		}		
	}
	
	private List<Personaje> loadCSVPersonajes() {
		List<Personaje> personajes = new ArrayList<>();
		
		try (BufferedReader in = new BufferedReader(new FileReader(CSV_PERSONAJES))) {
			String linea = null;
			Personaje p = null;
			//Omitir la cabecera
			in.readLine();		
			
			while ((linea = in.readLine()) != null) {
				p = Personaje.parseCSV(linea);
				
				if (p != null) {
					personajes.add(p);
				}
			}			
			
		} catch (Exception ex) {
			logger.warning(String.format("Error leyendo personajes del CSV: %s", ex.getMessage()));
		}
		
		return personajes;
	}
	
	private List<Comic> loadCVSComics() {
		List<Comic> comics = new ArrayList<>();
		
		try (BufferedReader in = new BufferedReader(new FileReader(CSV_COMICS))) {
			String linea = null;
			
			//Omitir la cabecera
			in.readLine();			
			
			while ((linea = in.readLine()) != null) {
				comics.add(Comic.parseCSV(linea));
			}			
			
		} catch (Exception ex) {
			logger.warning(String.format("Error leyendo comics del CSV: %s", ex.getMessage()));
		}
		
		return comics;
	}
	
	//MODIFICACIÓN 4: Guarda una lista de comics en un CSV
	public void storeCSVComics(List<Comic> comics) {
		if (comics != null) {
			try (PrintWriter out = new PrintWriter(new File(CSV_COMICS))) {
				out.println("EDITORIAL;TITULO;PERSONAJES");
				comics.forEach(c -> out.println(Comic.toCSV(c)));			
				logger.info("Se han guardado los comics en un CSV.");
			} catch (Exception ex) {
				logger.warning(String.format("Error guardando comics en el CSV: %s", ex.getMessage()));
			}
		}
	}
	
	//MODIFICACIÓN 4: Guarda una lista de personajes en un CSV
	public void storeCSVPersonajes(List<Personaje> personajes) {
		if (personajes != null) {
			try (PrintWriter out = new PrintWriter(new File(CSV_PERSONAJES))) {
				out.println("EDITORIAL;NOMBRE;EMAIL");
				personajes.forEach(p -> out.println(Personaje.toCSV(p)));
				logger.info("Se han guardado los personajes en un CSV.");
			} catch (Exception ex) {
				logger.warning(String.format("Error guardando personajes en el CSV: %s", ex.getMessage()));
			}			
		}
	}
}