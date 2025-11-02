package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import domain.Equipo;
import domain.Jugador;
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
		mainPanel.setLayout(new BorderLayout());
		//Cargar el icono
		String ruta = "resources/images/equipos/"+liga.getNombre().toLowerCase()+"/"+equipo.getNombrePNGEquipo().toLowerCase()+".png";
		ImageIcon escudoOriginal = null;
		try {
			escudoOriginal = new ImageIcon(ruta);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("No se ha encontrado la imagen en la direccion: "+ruta);
		}
		//Crear un label donde aparecera el escudo y un panel que lo contenga
		JPanel panelEscudo = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		//Modificar tamaño de imagen
		int ANCHO_MAXIMO = 100;
		ImageIcon escudoEscalado;
		if (escudoOriginal.getIconWidth()>ANCHO_MAXIMO) {
			int altoNuevo = (int) ((double) ANCHO_MAXIMO / escudoOriginal.getIconWidth() * escudoOriginal.getIconHeight());
			Image imagen = escudoOriginal.getImage();
			Image imagenEsalada = imagen.getScaledInstance(ANCHO_MAXIMO, altoNuevo, Image.SCALE_SMOOTH);
			escudoEscalado = new ImageIcon(imagenEsalada);
		}else {
			escudoEscalado = escudoOriginal;
		}
		//Añadir imagen
		JLabel labelEscudo = new JLabel(escudoEscalado);
		panelEscudo.add(labelEscudo);	
		mainPanel.add(panelEscudo, BorderLayout.NORTH);
		//Crear panel contenedor de la informacion y alineaciones
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		mainPanel.add(panelPrincipal, BorderLayout.CENTER);
		//Crear panel de informacion
		JPanel panelInformacion = new JPanel();
		//Crear panel de la plantilla
		JPanel panelPlantilla = new JPanel();
		//Cargar datos del equipo
		inicializarPanelInformacion(equipo, panelInformacion);
		//Redimensionar la ventana
		panelInformacion.setPreferredSize(new Dimension(250,200));
		panelInformacion.setBackground(Color.BLACK);
		panelInformacion.setForeground(Color.WHITE);
		
		//Añadir ventana
		panelPrincipal.add(panelInformacion, BorderLayout.WEST);
		
		
		usoBotonAtras(ligas, liga);
		add(mainPanel);		

	}
	//Funcion que inicializa los datos del equipo al panel de Informacion
	public void inicializarPanelInformacion (Equipo e, JPanel panel) {
		
		JLabel nombreEquipo = null;
		JLabel nombreCiudad = null;
		JLabel liga = null;
		JLabel nombreEstadio = null;
		try {
			nombreEquipo = new JLabel(e.getNombre());
			nombreCiudad = new JLabel(e.getCiudad());
			liga = new JLabel(e.getLiga().getNombre());
		//	JLabel anyoFundacion = new JLabel(e.getAnyoFundacion());
			nombreEstadio = new JLabel(e.getEstadio());
		} catch (Exception e2) {
			// TODO: handle exception
		}
	
		panel.setLayout(new FlowLayout());
		panel.add(nombreEquipo);
		panel.add(nombreCiudad);
		panel.add(liga);
		panel.add(nombreEstadio);
		
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
