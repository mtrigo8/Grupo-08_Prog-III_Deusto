package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import domain.Equipo;
import domain.Liga;
import domain.Partido;
import gui.JFrameClasificacion;
import gui.JFrameInicio;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Ejemplos generados con IA
		// ====== LaLiga ======
		Equipo fcBarcelona = new Equipo("FC Barcelona", "Barcelona", null, 1899, 27, "Spotify Camp Nou");
		Equipo realMadrid = new Equipo("Real Madrid", "Madrid", null, 1902, 36, "Santiago Bernabéu");
		Equipo atleticoMadrid = new Equipo("Atlético de Madrid", "Madrid", null, 1903, 11, "Cívitas Metropolitano");
		Equipo sevillaFC = new Equipo("Sevilla FC", "Sevilla", null, 1890, 1, "Ramón Sánchez-Pizjuán");
		Equipo realSociedad = new Equipo("Real Sociedad", "San Sebastián", null, 1909, 2, "Reale Arena");

		ArrayList<Equipo> laLigaEquipos = new ArrayList<>();
		laLigaEquipos.add(fcBarcelona);
		laLigaEquipos.add(realMadrid);
		laLigaEquipos.add(atleticoMadrid);
		laLigaEquipos.add(sevillaFC);
		laLigaEquipos.add(realSociedad);


		// ====== Premier League ======
		Equipo manchesterUnited = new Equipo("Manchester United", "Manchester", null, 1878, 20, "Old Trafford");
		Equipo manchesterCity = new Equipo("Manchester City", "Manchester", null, 1880, 10, "Etihad Stadium");
		Equipo liverpoolFC = new Equipo("Liverpool FC", "Liverpool", null, 1892, 19, "Anfield");
		Equipo chelseaFC = new Equipo("Chelsea FC", "Londres", null, 1905, 6, "Stamford Bridge");
		Equipo arsenalFC = new Equipo("Arsenal FC", "Londres", null, 1886, 13, "Emirates Stadium");

		ArrayList<Equipo> premierLeagueEquipos = new ArrayList<>();
		premierLeagueEquipos.add(manchesterUnited);
		premierLeagueEquipos.add(manchesterCity);
		premierLeagueEquipos.add(liverpoolFC);
		premierLeagueEquipos.add(chelseaFC);
		premierLeagueEquipos.add(arsenalFC);


		// ====== Bundesliga ======
		Equipo bayernMunich = new Equipo("Bayern Munich", "Múnich", null, 1900, 34, "Allianz Arena");
		Equipo borussiaDortmund = new Equipo("Borussia Dortmund", "Dortmund", null, 1909, 5, "Signal Iduna Park");
		Equipo rbLeipzig = new Equipo("RB Leipzig", "Leipzig", null, 2009, 0, "Red Bull Arena");
		Equipo bayerLeverkusen = new Equipo("Bayer Leverkusen", "Leverkusen", null, 1904, 1, "BayArena");
		Equipo borussiaMonchengladbach = new Equipo("Borussia Mönchengladbach", "Mönchengladbach", null, 1900, 5, "Borussia-Park");

		ArrayList<Equipo> bundesligaEquipos = new ArrayList<>();
		bundesligaEquipos.add(bayernMunich);
		bundesligaEquipos.add(borussiaDortmund);
		bundesligaEquipos.add(rbLeipzig);
		bundesligaEquipos.add(bayerLeverkusen);
		bundesligaEquipos.add(borussiaMonchengladbach);
		//
		
		ArrayList<Liga> ligas = new ArrayList<Liga>();
		Liga laLiga = new Liga("LaLiga", "España", 20, laLigaEquipos);
		Liga premier = (new Liga("Premier", "Inglaterra", 20, premierLeagueEquipos));
		Liga bundesliga = (new Liga("Bundesliga", "Alemania", 20, bundesligaEquipos));
		ligas.add(laLiga);
		ligas.add(premier);
		ligas.add(bundesliga);
		JFrameInicio jfi = new JFrameInicio(ligas);
		jfi.setVisible(true);	
	for (Liga liga : ligas) {
		ArrayList<ArrayList<Partido>> calendario = new ArrayList<ArrayList<Partido>>();
		for (int i = 1; i <= 38; i++) {
			ArrayList<Partido> jornada = new ArrayList<Partido>();
			for (int j = 1; j <= 10; j++) {
				Partido partido = new Partido(fcBarcelona, realMadrid, 0, 0, LocalDate.now());
				jornada.add(partido);
			}
			calendario.add(jornada);
	}
		liga.setCalendario(calendario);
	}
	}
}
