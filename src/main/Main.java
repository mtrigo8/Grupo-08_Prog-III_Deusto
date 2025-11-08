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
		// TODO Auto-generated method stub
		ArrayList<Liga> ligas = new ArrayList<Liga>();
		Liga laLiga = new Liga("LaLiga", "España", 20, null);
		Liga premier = (new Liga("Premier", "Inglaterra", 20, null));
		Liga bundesliga = (new Liga("Bundesliga", "Alemania", 20, null));
		ligas.add(laLiga);
		ligas.add(premier);
		ligas.add(bundesliga);
		//GUI
		// ====== LaLiga ======
		Equipo fcBarcelona = new Equipo("FC Barcelona", "Barcelona", laLiga, 1899, 27, "Spotify Camp Nou","barcelona");
		fcBarcelona.anyadirJugador(new Jugador("Negreira", 0, TipoPosicion.DELANTERO, fcBarcelona, "España", 0));
		Equipo realMadrid = new Equipo("Real Madrid", "Madrid", laLiga, 1902, 36, "Santiago Bernabéu","realmadrid");
		Equipo atleticoMadrid = new Equipo("Atlético de Madrid", "Madrid", laLiga, 1903, 11, "Cívitas Metropolitano","atlmadrid");
		Equipo sevillaFC = new Equipo("Sevilla FC", "Sevilla", laLiga, 1890, 1, "Ramón Sánchez-Pizjuán","sevilla");
		Equipo realSociedad = new Equipo("Real Sociedad", "San Sebastián", laLiga, 1909, 2, "Reale Arena","realsociedad");
		Equipo egeala = new Equipo("Egeala", "San Sebastián", laLiga, 1909, 2, "Reale Arena","realsociedad");
		

		ArrayList<Equipo> laLigaEquipos = new ArrayList<>();
		laLigaEquipos.add(fcBarcelona);
		laLigaEquipos.add(realMadrid);
		laLigaEquipos.add(atleticoMadrid);
		laLigaEquipos.add(sevillaFC);
		laLigaEquipos.add(realSociedad);
		laLigaEquipos.add(egeala);

		laLiga.setEquipos(laLigaEquipos);
		// ====== Premier League ======
		Equipo manchesterUnited = new Equipo("Manchester United", "Manchester", premier, 1878, 20, "Old Trafford","manchesterunited");
		Equipo manchesterCity = new Equipo("Manchester City", "Manchester", premier, 1880, 10, "Etihad Stadium","manchestercity");
		Equipo liverpoolFC = new Equipo("Liverpool FC", "Liverpool", premier, 1892, 19, "Anfield","liverpool");
		Equipo chelseaFC = new Equipo("Chelsea FC", "Londres", premier, 1905, 6, "Stamford Bridge","chelsea");
		Equipo arsenalFC = new Equipo("Arsenal FC", "Londres", premier, 1886, 13, "Emirates Stadium","arsenal");

		ArrayList<Equipo> premierLeagueEquipos = new ArrayList<>();
		premierLeagueEquipos.add(manchesterUnited);
		premierLeagueEquipos.add(manchesterCity);
		premierLeagueEquipos.add(liverpoolFC);
		premierLeagueEquipos.add(chelseaFC);
		premierLeagueEquipos.add(arsenalFC);
		
		premier.setEquipos(premierLeagueEquipos);

		// ====== Bundesliga ======
		Equipo bayernMunich = new Equipo("Bayern Munich", "Múnich", bundesliga, 1900, 34, "Allianz Arena","bayernmunchen");
		Equipo borussiaDortmund = new Equipo("Borussia Dortmund", "Dortmund", bundesliga, 1909, 5, "Signal Iduna Park","borussiadortmund");
		Equipo rbLeipzig = new Equipo("RB Leipzig", "Leipzig", bundesliga, 2009, 0, "Red Bull Arena","rbleipzig");
		Equipo bayerLeverkusen = new Equipo("Bayer Leverkusen", "Leverkusen", bundesliga, 1904, 1, "BayArena","bayerleverkusen");
		Equipo borussiaMonchengladbach = new Equipo("Borussia Mönchengladbach", "Mönchengladbach", bundesliga, 1900, 5, "Borussia-Park","bmonchengladbach");

		ArrayList<Equipo> bundesligaEquipos = new ArrayList<>();
		bundesligaEquipos.add(bayernMunich);
		bundesligaEquipos.add(borussiaDortmund);
		bundesligaEquipos.add(rbLeipzig);
		bundesligaEquipos.add(bayerLeverkusen);
		bundesligaEquipos.add(borussiaMonchengladbach);
		
		bundesliga.setEquipos(bundesligaEquipos);
		//
		
		
		JFrameInicio jfi = new JFrameInicio(ligas);
		jfi.setVisible(true);	
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
	}
}
