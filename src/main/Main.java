package main;

import java.util.ArrayList;

import domain.Liga;
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
			}
	//ga ga
	}


