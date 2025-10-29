package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import domain.Equipo;
import domain.Liga;

public class JFrameListaEquipos extends JFramePadre {
	private static final long serialVersionUID = 1L;
	private Liga liga;
	private JTextField filtradoNombre;
	private JTable tablaEquipos;
	private DefaultTableModel modeloDatosEquipos;
	
	public JFrameListaEquipos (Liga liga) {
		JPanel panel = super.panel;
		panel.setLayout(new BorderLayout());
		this.liga = liga;
		//Crear Barra buscadora
		filtradoNombre = new JTextField(20);
		JPanel panelFiltro = new JPanel();
		//Panel donde aparece la barra buscadora
		panelFiltro.add(new JLabel("Filtrado por nombre: "));
		panelFiltro.add(filtradoNombre);
		
		//Añadir panel y boton 
		panel.add(BorderLayout.NORTH, panelFiltro);
		panel.add(botonAtras);
		//Dar funcionalidad a botonAtras
		usoBotonAtras();
		
		//Inicializar las tablas con los equipos
		inicializarTabla();
		cargarEquiposTabla();
		//Insertar la tabla de equipos en un panel con scroll
		JScrollPane scrollPaneEquipos = new JScrollPane(this.tablaEquipos);
		scrollPaneEquipos.setBorder(new TitledBorder("Equipos"));
		this.tablaEquipos.setFillsViewportHeight(true);
		
		panel.add(BorderLayout.CENTER, scrollPaneEquipos);
		this.add(panel);
	}
	private void inicializarTabla() {
		Vector<String> cabezeraEquipos = new Vector<String>(Arrays.asList("ESCUDO", "NOMBRE"));
		modeloDatosEquipos = new DefaultTableModel(new Vector<Vector<Object>>(), cabezeraEquipos);
		this.tablaEquipos = new JTable(this.modeloDatosEquipos);
		this.tablaEquipos.setRowHeight(40);
		//Crear tableCellRenderer
		
		
		
	}
	private void cargarEquiposTabla() {
		//Borrar datos
		this.modeloDatosEquipos.setRowCount(0);
		//Añadir uno a uno al modelo de datos
		for (Equipo e: liga.getEquipos()) {
			this.modeloDatosEquipos.addRow(new Object[] {null, e.getNombre()});
		}
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
