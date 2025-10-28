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
	ArrayList<Liga> ligas;
	private static final long serialVersionUID = 1L;
	
	public JFrameSeleccionarLigas(ArrayList<Liga> ligas) {
		super();
		
		usoBotonAtras(); //Llama al metodo para usar el boton atras
		
		this.ligas = ligas;
		
		JPanel panel = super.panel;
		panel.setLayout(null); // Desactivar el layout manager
		
		// Configurar el título
		JLabel titulo = new JLabel("Seleccione una liga");
		titulo.setOpaque(true);
		titulo.setBackground(Color.white);
		titulo.setFont(new Font("Arial", Font.BOLD, 30));
		titulo.setBounds(350, 50, 300, 40); // x, y, ancho, alto
		panel.add(titulo);
		
		// Panel de botones
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
					JFrameLiga jfl = new JFrameLiga(liga);
					setVisible(false);
					jfl.setVisible(true);
				}
			});
			botones.add(botonLiga);
		}
		
		// Calcular tamaño del panel de botones según número de ligas
		int numLigas = ligas.size();
		int anchoBotones = (numLigas * 110) + 20; // 110px por botón (100 + margen) + padding
		int altoBotones = 120;
		
		botones.setBounds((1000 - anchoBotones) / 2, 150, anchoBotones, altoBotones); // Centrado
		panel.add(botones);
		
		this.add(panel);
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
