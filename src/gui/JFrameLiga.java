package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.Liga;

public class JFrameLiga extends JFramePadre {
	private static final long serialVersionUID = 1L;

	public JFrameLiga(Liga liga) {
		super();
		
		JPanel panel = super.panel;
		GridBagConstraints gbc = new GridBagConstraints();
		
		JLabel ligaT = new JLabel(liga.getNombre(), JLabel.CENTER);
		ligaT.setFont(new Font("Arial", Font.BOLD, 30));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(50, 0, 5, 0); // Espacio debajo del título
		panel.add(ligaT, gbc);
		
		JLabel titulo = new JLabel("Seleccione una opcion");
		titulo.setFont(new Font("Arial", Font.BOLD, 30));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 60, 0); // Espacio debajo del título
		panel.add(titulo, gbc);
		
		JPanel botones = new JPanel(new GridLayout(2, 3));
		botones.setOpaque(false);
		
		String[] contenidos = {"calendario", "equipo", "clasificacion"};
		for (String contenido : contenidos) {
			ImageIcon iconoLiga = new ImageIcon("images/logos/" + contenido + ".png");
			ImageIcon iconoAjustado = new ImageIcon(iconoLiga.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH));
			JButton boton = new JButton();
			boton.setPreferredSize(new Dimension(200, 200));
			boton.setIcon(iconoAjustado);
			boton.addActionListener(e -> {
				switch (contenido) {
					case "calendario":
						setVisible(false);
						JFrameCalendario jfca = new JFrameCalendario(liga);
						jfca.setVisible(true);
						break;
					case "equipo":
						setVisible(false);
						//JFrameCalendario jfe = new JFrameEquipos(liga);
						//jfe.setVisible(true);
						break;
					case "clasificacion":
						setVisible(false);
						//JFrameCalendario jfcl = new JFrameClasificacion(liga);
						//jfcl.setVisible(true);
						break;
				}
			});
			botones.add(boton);
		}
		for (String contenido : contenidos) {
			JLabel label = new JLabel(contenido.toUpperCase(), JLabel.CENTER);
			label.setFont(new Font("Arial", Font.BOLD, 18));
			botones.add(label);
		}
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.insets = new Insets(0, 0, 0, 0);
		panel.add(botones, gbc);
		
		this.add(panel);
	}

}
