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
		
		JLabel ligaT = new JLabel(liga.getNombre(), JLabel.CENTER);
		ligaT.setFont(new Font("Arial", Font.BOLD, 30));
		Dimension sizeL = ligaT.getPreferredSize();
		int anchoL = sizeL.width;
		int altoL = sizeL.height;
		ligaT.setBounds(300 - (anchoL/2), 200 - (altoL/2), anchoL, altoL);
		panel.add(ligaT);
		
		JLabel titulo = new JLabel("Seleccione una opcion");
		titulo.setFont(new Font("Arial", Font.BOLD, 30));
		Dimension sizeT = titulo.getPreferredSize();
		int anchoT = sizeT.width;
		int altoT = sizeT.height;
		ligaT.setBounds(300 - (anchoT/2), 200 - (altoT/2), anchoT, altoT);
		panel.add(titulo);
		
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
		

		Dimension sizeB = botones.getPreferredSize();
		int anchoB = sizeB.width;
		int altoB = sizeB.height;
		ligaT.setBounds(300 - (anchoB/2), 200 - (altoB/2), anchoB, altoB);
		panel.add(botones);
		
		this.add(panel);
	}

	@Override
	public void usoBotonAtras() {
		// TODO Auto-generated method stub
		
	}

}
