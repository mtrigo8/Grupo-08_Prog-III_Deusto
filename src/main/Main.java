package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import domain.Equipo;
import domain.Liga;
import domain.Partido;
import gui.JFrameInicio;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Liga> ligas = new ArrayList<Liga>();
		ligas.add(new Liga("LaLiga", "España", 20, null));
		ligas.add(new Liga("Premier", "Inglaterra", 20, null));
		ligas.add(new Liga("Bundesliga", "Alemania", 20, null));
		JFrameInicio jfi = new JFrameInicio(ligas);
		jfi.setVisible(true);
		Equipo equipo1 = new Equipo("Equipo 1", "Ciudad 1", null, 0, 0, null);
		Equipo equipo2 = new Equipo("Equipo 2", "Ciudad 2", null, 0, 0, null);
			
	for (Liga liga : ligas) {
		ArrayList<ArrayList<Partido>> calendario = new ArrayList<ArrayList<Partido>>();
		for (int i = 1; i <= 38; i++) {
			ArrayList<Partido> jornada = new ArrayList<Partido>();
			for (int j = 1; j <= 10; j++) {
				Partido partido = new Partido(equipo1, equipo2, 0, 0, LocalDate.now());
				jornada.add(partido);
			}
			calendario.add(jornada);
	}
		liga.setCalendario(calendario);
	}
	}
}
