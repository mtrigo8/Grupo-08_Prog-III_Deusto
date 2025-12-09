//IAG
//Main modificado por IA para en vez de añadir 5 equipos por liga como habiamos hecho a mano haga todos
package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;
import java.util.List;

import db.GestorBD;
import domain.Equipo;
import domain.Jugador;
import domain.Jugador.TipoPosicion;
import domain.Liga;
import domain.Partido;
import gui.JFrameClasificacion;
import gui.JFrameInicio;

public class Main {

	public static void main(String[] args) {
		GestorBD gbd = new GestorBD();
		
		gbd.borrarBBDD();
		gbd.crearBBDD();
		gbd.initilizeFromCSV();
		
		//Cargar las ligas de la BBDD
		List<Liga> ligas = new ArrayList<Liga>();
		ligas = gbd.getLigas();

		//Cargar los equipos de la BBDD
		List<Equipo> equipos = new ArrayList<Equipo>();
		equipos = gbd.getEquipos();

		
		gbd.updateEquipos(equipos, ligas);


		// GUI
		JFrameInicio jfi = new JFrameInicio((ArrayList<Liga>) ligas);
		jfi.setVisible(true);
		
		// Generar calendario
		for (Liga liga : ligas) {
			int partidosMax = liga.getNumeroEquipos()*2-2;
			int partidosMaxVuelta = partidosMax/2;
			int partidosPorJ = liga.getNumeroEquipos()/2;
			TreeMap<Integer, ArrayList<Partido>> calendario = new TreeMap<Integer, ArrayList<Partido>>();
				ArrayList<ArrayList<Equipo>>emparejamientos = generarEmparejamientos(liga);
				ArrayList<Partido> jornada = new ArrayList<Partido>();
				Partido primerPartido = new Partido(emparejamientos.get(0).get(0), emparejamientos.get(0).get(1), 0, 0, LocalDate.now(), 1 );
				jornada.add(primerPartido);
				for(ArrayList<Equipo>emparejamiento : emparejamientos){
					introducirPartido(calendario, emparejamiento,partidosPorJ,partidosMaxVuelta);
					
				}
				int numJornadasIda = calendario.size();

			    for (int jornadaIda = 1; jornadaIda <= numJornadasIda; jornadaIda++) {
			        int jornadaVuelta = jornadaIda + numJornadasIda;
			             ArrayList<Partido> partidosIda = calendario.get(jornadaIda);
			             ArrayList<Partido> partidosVuelta = new ArrayList<>();
			             for (Partido pIda : partidosIda) {
			                 Partido pVuelta = new Partido(pIda.getEquipoVisitante(), pIda.getEquipoLocal(), 0, 0, LocalDate.now(), jornadaVuelta);
			                 partidosVuelta.add(pVuelta);
			             }
			             calendario.put(jornadaVuelta, partidosVuelta);
			        
			    }
			
			liga.setCalendario(calendario);
		}
		for (Liga liga : ligas) {
			for (Equipo equipo : liga.getEquipos()) {
				int dorsal = 1;
				for (int i = 0; i < 4; i++) {
					for (TipoPosicion posicion : TipoPosicion.values()) {
						Jugador j = new Jugador("Nombre Apellido", dorsal, posicion, equipo, "Nacionalidad", 20);
						dorsal++;
						equipo.anyadirJugador(j);
					}
				}
			}
			
		}
		
	}
	
	public static ArrayList<ArrayList<Equipo>> generarEmparejamientos(Liga liga) {
		ArrayList<Equipo>equipos = liga.getEquipos();
		ArrayList<ArrayList<Equipo>>result = new ArrayList<>();
		generarEmparejamientosRec(equipos, result, new ArrayList<Equipo>(), 2);
		return result;
		
	}
	
	
	public static void generarEmparejamientosRec(ArrayList<Equipo>equipos, ArrayList<ArrayList<Equipo>> result, ArrayList<Equipo>emparejamiento, int n) {
		if (n == 0) {
    		Collections.sort(emparejamiento);
    		if(!result.contains(emparejamiento)){
         	ArrayList<Equipo> combinaciones = new ArrayList<>(emparejamiento);
             result.add(combinaciones);
    		}
         } else {
             // Caso recursivo. Por cada caracter
             for (Equipo c : equipos) {
             	if (!emparejamiento.contains(c)) {
             		emparejamiento.add(c);
             		generarEmparejamientosRec(equipos, result, emparejamiento, n - 1);
             		emparejamiento.removeLast();
             	}
             }
         }
	}
	public static void introducirPartido(TreeMap<Integer, ArrayList<Partido>> calendario, ArrayList<Equipo> emparejamiento, int partidosPorJornada, int maxJornadasIda) {
	    Equipo equipo1 = emparejamiento.get(0);
	    Equipo equipo2 = emparejamiento.get(1);
	    Equipo local;
	    Equipo visitante;
	    for (Integer numJornada : calendario.keySet()) {
	        
	        ArrayList<Partido> jornadaPartidos = calendario.get(numJornada);

	        if (jornadaPartidos.size() < partidosPorJornada) {

	            boolean equipo1Ocupado = false;
	            boolean equipo2Ocupado = false;
	            
	            for (Partido p : jornadaPartidos) {
	                if (p.getEquipoLocal().equals(equipo1) || p.getEquipoVisitante().equals(equipo1)) {
	                    equipo1Ocupado = true;
	                }
	                if (p.getEquipoLocal().equals(equipo2) || p.getEquipoVisitante().equals(equipo2)) {
	                    equipo2Ocupado = true;
	                }
	            }


	            if (!equipo1Ocupado && !equipo2Ocupado) {
	            	if (Math.random() < 0.5) {
	                    local = equipo1;
	                } else {
	                    local = equipo2;
	                }
	            	if (local == equipo1) {
	                    visitante = equipo2;
	                } else {
	                    visitante = equipo1;
	                }
	            
	                
	                Partido partidoNuevo = new Partido(local, visitante, 0, 0, LocalDate.now(), numJornada);
	                jornadaPartidos.add(partidoNuevo);
	                return; 
	            }
	        }
	    }
	    int nuevaJornada = 0;
	    if (calendario.isEmpty()) {
	        nuevaJornada = 1;
	    } else {
	        nuevaJornada = calendario.lastKey() + 1;
	    }
	    
	    if (nuevaJornada <= maxJornadasIda) {
	    	if (Math.random() < 0.5) {
                local = equipo1;
            } else {
                local = equipo2;
            }
        	if (local == equipo1) {
                visitante = equipo2;
            } else {
                visitante = equipo1;
            }
        
	        ArrayList<Partido> nuevaJornadaPartidos = new ArrayList<>();
	        Partido partidoNuevo = new Partido(local, visitante, 0, 0, LocalDate.now(), nuevaJornada);
	        nuevaJornadaPartidos.add(partidoNuevo);
	        
	        calendario.put(nuevaJornada, nuevaJornadaPartidos);
	    }
	    
	}
/*	public static void asignarEquiposLigas (List<Equipo> equipos, List<Liga> ligas) {
		for (Liga l:ligas) {
			ArrayList<Equipo> equiposLiga = new ArrayList<>();
			for (Equipo e:equipos ) {
				if (e.getLiga().getNombre().equals(l.getNombre())){
					equiposLiga.add(e);
					e.setLiga(l);
				}
			}
			l.setEquipos(equiposLiga);
		}
	}
	public static void asignarPartidosLigas (List<Partido> partidos, List<Liga> ligas) {
		for (Partido p:partidos) {
			for (Liga l:ligas) {
				if (l.getEquipos().contains(p.getEquipoLocal())) {
					TreeMap<Integer, ArrayList<Partido>> calendario = l.getCalendario();
					int numJornada = p.getJornada();
					if (!calendario.containsKey(numJornada)) {
						calendario.put(numJornada, new ArrayList<Partido>());
						
						}
					calendario.get(numJornada).add(p);
					}
				}
			}
		}*/
}
