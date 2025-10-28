package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.Liga;

public class JFrameInicio extends JFramePadre{


	private static final long serialVersionUID = 1L;

	public JFrameInicio(ArrayList<Liga> ligas) {
		super();
		
		//Parte mejorada mediante el uso de Claude 4.5 debido a un error en mi codigo original
		// Crear panel con GridBagLayout para centrar elementos
		JPanel panel = super.panel;
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Configurar el título
		JLabel titulo = new JLabel("Bienvenido a FutGoat");
		titulo.setOpaque(true);
		titulo.setBackground(Color.white);
		titulo.setFont(new Font("Arial", Font.BOLD, 30));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 20, 0); // Espacio debajo del título
		panel.add(titulo, gbc);
		
		// Crear y configurar el botón
		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.setFont(new Font("Arial", Font.PLAIN, 18));
		btnEntrar.setPreferredSize(new java.awt.Dimension(100, 60));
		btnEntrar.setBackground(Color.red);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		panel.add(btnEntrar, gbc);
		
		this.add(panel);
		
		btnEntrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrameSeleccionarLigas jfs = new JFrameSeleccionarLigas(ligas);
				setVisible(false);
				jfs.setVisible(true);
			}		
		});		
	}	
 }
	

	

	



