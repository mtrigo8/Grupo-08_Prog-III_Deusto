package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import domain.Liga;

public class JFrameListaEquipos extends JFramePadre {
	private static final long serialVersionUID = 1L;
	private Liga liga;
	private JTextField filtradoNombre;
	private JTable tablaEquipos;
	
	public JFrameListaEquipos (Liga liga) {
		JPanel panel = super.panel;
		this.liga = liga;
		//Crear Barra buscadora
		filtradoNombre = new JTextField(20);
		JPanel panelFiltro = new JPanel();
		//Panel donde aparece la barra buscadora
		panelFiltro.add(new JLabel("Filtrado por nombre: "));
		panelFiltro.add(filtradoNombre);
		panelFiltro.setBounds(0, 100, 1000, 400);
		//Añadir panel y boton 
		panel.add(panelFiltro);
		panel.add(botonAtras);
		//Dar funcionalidad a botonAtras
		usoBotonAtras();
		this.add(panel);
		//Inicializar las tablas con los equipos
		inicializarTabla();
		
		//Insertar la tabla de equipos en un panel con scroll
	//	JScrollPane scrollPaneEquipos = new JScrollPane(this.tablaEquipos);
	//	scrollPaneEquipos.setBorder(new TitledBorder("Equipos"));
	//	this.tablaEquipos.setFillsViewportHeight(true);
		
		
	}
	private void inicializarTabla() {
		
	}
	
	@Override
	public void usoBotonAtras() {
		// TODO Auto-generated method stub
		super.botonAtras.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//Regreso a la ventana de liga
				JFrameLiga jfl = new JFrameLiga(ligas, liga);
				//Desaparece la ventana ListaEquipos
				setVisible(false);
				//Aparece ventana Liga
				jfl.setVisible(true);
				
			}
		});
	}

}
