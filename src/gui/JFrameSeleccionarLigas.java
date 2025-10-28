package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.SwingConstants;

import domain.Liga;

public class JFrameSeleccionarLigas extends JFramePadre{
	
	private static final long serialVersionUID = 1L;
	
	public JFrameSeleccionarLigas(ArrayList<Liga> ligas) {
		super();
		
		usoBotonAtras(); //Llama al metodo para usar el boton atras
		
		this.ligas = ligas;
		JButton botonAtras=super.botonAtras;
		JPanel panel= new JPanel();
		panel.setBackground(new Color(255, 195, 0));
		panel.setOpaque(true);
		panel.setLayout(null); // Desactivar el layout manager
		panel.add(botonAtras);
		botonAtras.setBounds(10, 10, 60, 50);
		this.setContentPane(panel);
		// Configurar el título
		JLabel titulo = new JLabel("Seleccione una liga ");
		titulo.setOpaque(false);
		titulo.setForeground(Color.BLACK);
		titulo.setFont(new Font("Arial", Font.BOLD, 30));
		titulo.setBounds(350, 50, 300, 40); // x, y, ancho, alto
		panel.add(titulo);
		
		// Panel de botones
		JPanel botones = new JPanel(new GridLayout(3,1,10,10));
		botones.setOpaque(false);
		
		for (Liga liga : ligas) {
			ImageIcon iconoLiga = new ImageIcon("images/ligas/" + liga.getNombre() + ".png");
			ImageIcon iconoAjustado = new ImageIcon(iconoLiga.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
			JButton botonLiga = new JButton();
			botonLiga.setFont(new Font("Arial", Font.PLAIN, 18));
			botonLiga.setBackground(Color.WHITE);
			botonLiga.setForeground(Color.BLACK);
			botonLiga.setIcon(iconoAjustado);
			botonLiga.setBorderPainted(false);
			botonLiga.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFrameLiga jfl = new JFrameLiga(ligas, liga);
					setVisible(false);
					jfl.setVisible(true);
				}
			});
			botones.add(botonLiga);
		}
		
		// Calcular tamaño del panel de botones según número de ligas
		int numLigas = ligas.size();
		int anchoBotones = 100; // 110px por botón (100 + margen) + padding
		int altoBotones = (numLigas*100)-10;
		
		botones.setBounds((1000 - anchoBotones) / 2, 150, anchoBotones, altoBotones); // Centrado
		panel.add(botones);
	}
	//Codigo cambiado parcialmente apartir de lo creado por nosotros para cambiar del uso de gridBagCOntraits el 

	@Override
	public void usoBotonAtras() {
		// TODO Auto-generated method stub
		super.botonAtras.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrameInicio jfi = new JFrameInicio(ligas);
				setVisible(false);
				jfi.setVisible(true);
			}});
		
	} 
	
}
