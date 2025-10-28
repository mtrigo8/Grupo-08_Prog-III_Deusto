package gui;

import java.awt.Color;
import java.awt.Dimension;
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
		
		usoBotonAtras();
		
		//Parte mejorada mediante el uso de Claude 4.5 debido a un error en mi codigo original
		// Crear panel con GridBagLayout para centrar elementos
		JPanel panel = super.panel;
		
		// Configurar el título
		JLabel titulo = new JLabel("Bienvenido a FutGoat");
		titulo.setFont(new Font("Arial", Font.BOLD, 30));
		titulo.setOpaque(true);
		titulo.setBackground(Color.white);
		Dimension size = titulo.getPreferredSize();
		int anchoT = size.width;
		int altoT = size.height;
		titulo.setBounds(300 - (anchoT/2), 200 - (altoT/2), anchoT, altoT);
		panel.add(titulo);
		
		// Crear y configurar el botón
		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.setFont(new Font("Arial", Font.BOLD, 18));
		btnEntrar.setBackground(new Color(80, 187, 212));
		int anchoB = 100;
		int altoB = 50;
		btnEntrar.setBounds(500 - (anchoB/2), 400 - (altoB/2), anchoB, altoB);
		panel.add(btnEntrar);
		
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

	@Override
	public void usoBotonAtras() {
		// TODO Auto-generated method stub
		super.botonAtras.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
			}});
	}	
 }
	

	

	



