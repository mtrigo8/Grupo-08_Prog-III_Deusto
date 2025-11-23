package gui;

import java.util.ArrayList;

import javax.swing.JPanel;

import domain.Liga;

public class JFrameQuiz extends JFramePadre{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList <Liga> ligas;

	
	public JFrameQuiz (ArrayList<Liga> ligas, JFramePadre frameP) {
		super();
		super.framePrevio = frameP;
		this.ligas = ligas;
		usoBotonAtras(super.framePrevio);
		
		JPanel panel = super.panel;
		setImagenDeFondo(null);
		panel.setOpaque(true);
		panel.setLayout(null); // Desactivar el layout manager
		panel.add(botonAtras);
		botonAtras.setBounds(10, 10, 60, 50);
		
		this.setContentPane(panel);
	}

}
