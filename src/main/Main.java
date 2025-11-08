//IAG
//Main modificado por IA para en vez de añadir 5 equipos por liga como habiamos hecho a mano haga todos
package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

import domain.Equipo;
import domain.Jugador;
import domain.Jugador.TipoPosicion;
import domain.Liga;
import domain.Partido;
import gui.JFrameClasificacion;
import gui.JFrameInicio;

public class Main {

	public static void main(String[] args) {
		ArrayList<Liga> ligas = new ArrayList<Liga>();
		Liga laLiga = new Liga("LaLiga", "España", 20, null);
		Liga premier = new Liga("Premier", "Inglaterra", 20, null);
		Liga bundesliga = new Liga("Bundesliga", "Alemania", 20, null);
		ligas.add(laLiga);
		ligas.add(premier);
		ligas.add(bundesliga);

		// ====== LaLiga (20 equipos) ======
		Equipo fcBarcelona = new Equipo("FC Barcelona", "Barcelona", laLiga, 1899, 27, "Spotify Camp Nou", "barcelona");
		fcBarcelona.anyadirJugador(new Jugador("Negreira", 0, TipoPosicion.DELANTERO, fcBarcelona, "España", 0));
		Equipo realMadrid = new Equipo("Real Madrid", "Madrid", laLiga, 1902, 36, "Santiago Bernabéu", "realmadrid");
		Equipo atleticoMadrid = new Equipo("Atlético de Madrid", "Madrid", laLiga, 1903, 11, "Cívitas Metropolitano", "atlmadrid");
		Equipo sevillaFC = new Equipo("Sevilla FC", "Sevilla", laLiga, 1890, 1, "Ramón Sánchez-Pizjuán", "sevilla");
		Equipo realSociedad = new Equipo("Real Sociedad", "San Sebastián", laLiga, 1909, 2, "Reale Arena", "realsociedad");
		Equipo realBetis = new Equipo("Real Betis", "Sevilla", laLiga, 1907, 0, "Benito Villamarín", "betis"); //Error en logo
		Equipo villarreal = new Equipo("Villarreal CF", "Villarreal", laLiga, 1923, 0, "Estadio de la Cerámica", "villarreal");
		Equipo athleticBilbao = new Equipo("Athletic Club", "Bilbao", laLiga, 1898, 8, "San Mamés", "athletic");
		Equipo valencia = new Equipo("Valencia CF", "Valencia", laLiga, 1919, 6, "Mestalla", "valencia");
		Equipo getafe = new Equipo("Getafe CF", "Getafe", laLiga, 1983, 0, "Coliseum Alfonso Pérez", "getafe");
		Equipo espanyol = new Equipo("RCD Espanyol", "Barcelona", laLiga, 1900, 4, "RCDE Stadium", "espanyol");
		Equipo rayo = new Equipo("Rayo Vallecano", "Madrid", laLiga, 1924, 0, "Campo de Vallecas", "rayovallecano"); //Error en logo
		Equipo mallorca = new Equipo("RCD Mallorca", "Palma", laLiga, 1916, 1, "Son Moix", "mallorca");
		Equipo osasuna = new Equipo("CA Osasuna", "Pamplona", laLiga, 1920, 0, "El Sadar", "osasuna");
		Equipo celta = new Equipo("Celta de Vigo", "Vigo", laLiga, 1923, 0, "Balaídos", "celta");
		Equipo girona = new Equipo("Girona FC", "Girona", laLiga, 1930, 0, "Montilivi", "girona");
		Equipo elche = new Equipo("Elche CF", "Elche", laLiga, 1922, 0, "Martinez Valero", "elche"); //Error en logo
		Equipo alaves = new Equipo("Deportivo Alavés", "Vitoria", laLiga, 1921, 0, "Mendizorroza", "alaves");
		Equipo oviedo = new Equipo("Real Oviedo", "Oviedo", laLiga, 1926, 0, "Carlos Tartiere", "realoviedo"); //Error en logo
		Equipo levante = new Equipo("Levante UD", "Valencia", laLiga, 1909, 0, "Ciutat de Valencia", "levante"); //Error en logo

		ArrayList<Equipo> laLigaEquipos = new ArrayList<>();
		laLigaEquipos.add(fcBarcelona);
		laLigaEquipos.add(realMadrid);
		laLigaEquipos.add(atleticoMadrid);
		laLigaEquipos.add(sevillaFC);
		laLigaEquipos.add(realSociedad);
		laLigaEquipos.add(realBetis);
		laLigaEquipos.add(villarreal);
		laLigaEquipos.add(athleticBilbao);
		laLigaEquipos.add(valencia);
		laLigaEquipos.add(getafe);
		laLigaEquipos.add(espanyol);
		laLigaEquipos.add(rayo);
		laLigaEquipos.add(mallorca);
		laLigaEquipos.add(osasuna);
		laLigaEquipos.add(celta);
		laLigaEquipos.add(girona);
		laLigaEquipos.add(elche);
		laLigaEquipos.add(alaves);
		laLigaEquipos.add(oviedo);
		laLigaEquipos.add(levante);

		laLiga.setEquipos(laLigaEquipos);
		
		// ====== Premier League (20 equipos) ======
		Equipo manchesterUnited = new Equipo("Manchester United", "Manchester", premier, 1878, 20, "Old Trafford", "manchesterunited");
		Equipo manchesterCity = new Equipo("Manchester City", "Manchester", premier, 1880, 10, "Etihad Stadium", "manchestercity");
		Equipo liverpoolFC = new Equipo("Liverpool FC", "Liverpool", premier, 1892, 19, "Anfield", "liverpool");
		Equipo chelseaFC = new Equipo("Chelsea FC", "Londres", premier, 1905, 6, "Stamford Bridge", "chelsea");
		Equipo arsenalFC = new Equipo("Arsenal FC", "Londres", premier, 1886, 13, "Emirates Stadium", "arsenal");
		Equipo tottenham = new Equipo("Tottenham Hotspur", "Londres", premier, 1882, 2, "Tottenham Hotspur Stadium", "tottenham");
		Equipo newcastle = new Equipo("Newcastle United", "Newcastle", premier, 1892, 4, "St James' Park", "newcastle");
		Equipo astonvilla = new Equipo("Aston Villa", "Birmingham", premier, 1874, 7, "Villa Park", "astonvilla");
		Equipo brighton = new Equipo("Brighton & Hove Albion", "Brighton", premier, 1901, 0, "Amex Stadium", "brighton");
		Equipo westham = new Equipo("West Ham United", "Londres", premier, 1895, 3, "London Stadium", "westham");
		Equipo sunderland = new Equipo("Sunderland AFC", "Sunderland", premier, 1879, 6, "Stadium of Light", "sunderland"); //Error en logo
		Equipo everton = new Equipo("Everton FC", "Liverpool", premier, 1878, 9, "Goodison Park", "everton");
		Equipo wolves = new Equipo("Wolverhampton Wanderers", "Wolverhampton", premier, 1877, 3, "Molineux Stadium", "wolves");
		Equipo fulham = new Equipo("Fulham FC", "Londres", premier, 1879, 0, "Craven Cottage", "fulham");
		Equipo brentford = new Equipo("Brentford FC", "Brentford", premier, 1889, 0, "Gtech Community Stadium", "brentford");
		Equipo crystalpalace = new Equipo("Crystal Palace", "Londres", premier, 1905, 0, "Selhurst Park", "crystalpalace");
		Equipo bournemouth = new Equipo("AFC Bournemouth", "Bournemouth", premier, 1899, 0, "Vitality Stadium", "bournemouth");
		Equipo nottingham = new Equipo("Nottingham Forest", "Nottingham", premier, 1865, 2, "City Ground", "nottingham_forest"); //Error en logo
		Equipo burnley = new Equipo("Burnley FC", "Burnley", premier, 1882, 2, "Turf Moor", "burnley"); //Error en logo
		Equipo leeds = new Equipo("Leeds United FC", "Leeds", premier, 1919, 3, "Elland Road", "leeds"); //Error en logo

		ArrayList<Equipo> premierLeagueEquipos = new ArrayList<>();
		premierLeagueEquipos.add(manchesterUnited);
		premierLeagueEquipos.add(manchesterCity);
		premierLeagueEquipos.add(liverpoolFC);
		premierLeagueEquipos.add(chelseaFC);
		premierLeagueEquipos.add(arsenalFC);
		premierLeagueEquipos.add(tottenham);
		premierLeagueEquipos.add(newcastle);
		premierLeagueEquipos.add(astonvilla);
		premierLeagueEquipos.add(brighton);
		premierLeagueEquipos.add(westham);
		premierLeagueEquipos.add(sunderland);
		premierLeagueEquipos.add(everton);
		premierLeagueEquipos.add(wolves);
		premierLeagueEquipos.add(fulham);
		premierLeagueEquipos.add(brentford);
		premierLeagueEquipos.add(crystalpalace);
		premierLeagueEquipos.add(bournemouth);
		premierLeagueEquipos.add(nottingham);
		premierLeagueEquipos.add(burnley);
		premierLeagueEquipos.add(leeds);

		premier.setEquipos(premierLeagueEquipos);

		// ====== Bundesliga (18 equipos) ======
		Equipo bayernMunich = new Equipo("Bayern Munich", "Múnich", bundesliga, 1900, 34, "Allianz Arena", "bayernmunchen");
		Equipo borussiaDortmund = new Equipo("Borussia Dortmund", "Dortmund", bundesliga, 1909, 5, "Signal Iduna Park", "borussiadortmund");
		Equipo rbLeipzig = new Equipo("RB Leipzig", "Leipzig", bundesliga, 2009, 0, "Red Bull Arena", "rbleipzig");
		Equipo bayerLeverkusen = new Equipo("Bayer Leverkusen", "Leverkusen", bundesliga, 1904, 1, "BayArena", "bayerleverkusen");
		Equipo borussiaMonchengladbach = new Equipo("Borussia Mönchengladbach", "Mönchengladbach", bundesliga, 1900, 5, "Borussia-Park", "bmonchengladbach");
		Equipo eintracht = new Equipo("Eintracht Frankfurt", "Frankfurt", bundesliga, 1899, 1, "Deutsche Bank Park", "eintrachtfrankfurt"); //Error en logo
		Equipo wolfsburg = new Equipo("VfL Wolfsburg", "Wolfsburg", bundesliga, 1945, 1, "Volkswagen Arena", "wolfsburg");
		Equipo unionberlin = new Equipo("Union Berlin", "Berlín", bundesliga, 1966, 0, "Stadion An der Alten Försterei", "unionberlin");
		Equipo freiburg = new Equipo("SC Freiburg", "Friburgo", bundesliga, 1904, 0, "Europa-Park Stadion", "freiburg");
		Equipo stuttgart = new Equipo("VfB Stuttgart", "Stuttgart", bundesliga, 1893, 5, "Mercedes-Benz Arena", "stuttgart");
		Equipo hoffenheim = new Equipo("TSG Hoffenheim", "Sinsheim", bundesliga, 1899, 0, "PreZero Arena", "hoffenheim");
		Equipo mainz = new Equipo("FSV Mainz 05", "Maguncia", bundesliga, 1905, 0, "Mewa Arena", "mainz05"); //Error en logo
		Equipo augsburg = new Equipo("FC Augsburg", "Augsburgo", bundesliga, 1907, 0, "WWK Arena", "augsburgo"); //Error en logo
		Equipo werderbremen = new Equipo("Werder Bremen", "Bremen", bundesliga, 1899, 4, "Weserstadion", "werderbremen");
		Equipo koln = new Equipo("FC Koln", "Colonia", bundesliga, 1948, 3, "RheinEnergieStadion", "koln"); //Error en logo
		Equipo stpauli = new Equipo("FC St. Pauli", "Hamburgo", bundesliga, 1910, 0, "Millerntor-Stadion", "st_pauli"); //Error en logo
		Equipo heidenheim = new Equipo("1. FC Heidenheim", "Heidenheim", bundesliga, 1846, 0, "Voith-Arena", "heidenheim");
		Equipo hamburgo = new Equipo("Hamburgo SV", "Hamburgo", bundesliga, 1887, 6, "Volksparkstadion", "hamburgo"); //Error en logo
		
		
		ArrayList<Equipo> bundesligaEquipos = new ArrayList<>();
		bundesligaEquipos.add(bayernMunich);
		bundesligaEquipos.add(borussiaDortmund);
		bundesligaEquipos.add(rbLeipzig);
		bundesligaEquipos.add(bayerLeverkusen);
		bundesligaEquipos.add(borussiaMonchengladbach);
		bundesligaEquipos.add(eintracht);
		bundesligaEquipos.add(wolfsburg);
		bundesligaEquipos.add(unionberlin);
		bundesligaEquipos.add(freiburg);
		bundesligaEquipos.add(stuttgart);
		bundesligaEquipos.add(hoffenheim);
		bundesligaEquipos.add(mainz);
		bundesligaEquipos.add(augsburg);
		bundesligaEquipos.add(werderbremen);
		bundesligaEquipos.add(hamburgo);
		bundesligaEquipos.add(stpauli);
		bundesligaEquipos.add(heidenheim);
		bundesligaEquipos.add(koln);

		bundesliga.setEquipos(bundesligaEquipos);

		// GUI
		JFrameInicio jfi = new JFrameInicio(ligas);
		jfi.setVisible(true);
		
		// Generar calendario
		for (Liga liga : ligas) {
			TreeMap<Integer, ArrayList<Partido>> calendario = new TreeMap<Integer, ArrayList<Partido>>();
			for (int i = 1; i <= 38; i++) {
				ArrayList<Partido> jornada = new ArrayList<Partido>();
				int numJornada = i;
				for (int j = 1; j <= 20; j++) {
					Partido partido = new Partido(fcBarcelona, realMadrid, 0, 0, LocalDate.now(), numJornada);
					jornada.add(partido);
				}
				if (!calendario.containsKey(i)) {
					calendario.put(i, new ArrayList<Partido>());
				}
				calendario.get(i).addAll(jornada);
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
}