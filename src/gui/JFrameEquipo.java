package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.Equipo;
import domain.Liga;

public class JFrameEquipo extends JFramePadre{
	private ArrayList<Liga> ligas;
	private Liga liga;
	private Equipo equipo;
	private JFramePadre ventanaAnterior;

	
	public JFrameEquipo(ArrayList<Liga>ligas, Liga liga, Equipo equipo, JFramePadre ventanaAnterior) {
		this.ligas = ligas;
		this.liga = liga;
		this.equipo = equipo;
		this.ventanaAnterior = ventanaAnterior;
		
		JPanel mainPanel = super.panel;

		String ruta = "resources/images/equipos/"+liga.getNombre().toLowerCase()+"/"+equipo.getNombrePNGEquipo().toLowerCase()+".png";
		ImageIcon escudo;
		try {
			escudo = new ImageIcon(ruta);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("No se ha encontrado la imagen en la direccion: "+ruta);
		}
		JPanel panelEscudo = new JPanel();
		

		usoBotonAtras(ligas, liga);
		
		
		add(mainPanel);		

	}
	@Override
	public void usoBotonAtras(ArrayList<Liga> ligas, Liga liga) {
		// TODO Auto-generated method stub
		super.botonAtras.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
				ventanaAnterior.setVisible(true);
			}
		});
	}

}
