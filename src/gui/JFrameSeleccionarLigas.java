package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.Liga;

public class JFrameSeleccionarLigas extends JFramePadre{


	private static final long serialVersionUID = 1L;

	public JFrameSeleccionarLigas(ArrayList<Liga> ligas) {
		super();
		
		JPanel panel = super.panel;
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Configurar el título
		JLabel titulo = new JLabel("Seleccione una liga");
		titulo.setOpaque(true);
		titulo.setBackground(Color.white);
		titulo.setFont(new Font("Arial", Font.BOLD, 30));
		gbc.gridx = 0;
		gbc.gridy = -1;
		gbc.insets = new Insets(0, 0, 50, 0); // Espacio debajo del título
		panel.add(titulo, gbc);
		
		JPanel botones = new JPanel(new FlowLayout());
		botones.setOpaque(false);
		for (Liga liga : ligas) {
			ImageIcon iconoLiga = new ImageIcon("images/ligas/" + liga.getNombre() + ".png");
			ImageIcon iconoAjustado = new ImageIcon(iconoLiga.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
			JButton botonLiga = new JButton();
			botonLiga.setFont(new Font("Arial", Font.PLAIN, 18));
			botonLiga.setPreferredSize(new Dimension(100, 100));
			botonLiga.setIcon(iconoAjustado);
			botonLiga.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JFrameLiga jfl = new JFrameLiga(liga);
					setVisible(false);
					jfl.setVisible(true);
				}});
			botones.add(botonLiga);
		}
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		panel.add(botones, gbc);
		
		
		this.add(panel);
		
		
} 
	
}
