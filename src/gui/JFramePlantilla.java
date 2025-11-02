package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import domain.Equipo;
import domain.Jugador;
import domain.Jugador.TipoPosicion;
import domain.Liga;

public class JFramePlantilla extends JFramePadre {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Liga> ligas;
	private Liga liga;
	private JFramePadre ventanaAnterior;
	
	
	public JFramePlantilla(ArrayList<Liga> ligas, Liga liga, Equipo equipo, JFramePadre ventanaAnterior) {
		
		super();
		this.ligas = ligas;
		this.liga = liga;
		
		
		
		for(TipoPosicion clave : equipo.getJugadores().keySet()) {
			Vector<String> columnNames = new Vector<String>(Arrays.asList(clave.toString(), "Nombre"));
		this.ventanaAnterior = ventanaAnterior;
		}
		panel.setLayout(null);
		this.add(panel);
		usoBotonAtras(ligas, liga);
		for(TipoPosicion clave : equipo.getJugadores().keySet()) {
			Vector<String> columnNames = new Vector<String>(Arrays.asList(clave.toString(), "Nombre"));
			DefaultTableModel mDatTab = new DefaultTableModel(new Vector<Vector<Object>>(), columnNames);
			JTable table = new JTable(mDatTab);
			table.setRowHeight(30);
			table.getTableHeader().setReorderingAllowed(false);

			//Se modifica el modelo de selección de la tabla para que se pueda selecciona únicamente una fila
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			//Se define el comportamiento el evento de selección de una fila de la tabla
			table.getSelectionModel().addListSelectionListener(e -> {
				
			});
			for (Jugador jug : equipo.getJugadores().get(clave)) {
				mDatTab.addRow(new Object[] {clave, jug.getNombre()});
				}
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setBounds(0, 100, 1000, 400);
			panel.add(scrollPane);
			this.setSize(1000,600);
			this.setVisible(true);
			table.setVisible(true);
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	@Override
	public void usoBotonAtras(ArrayList<Liga> ligas, Liga liga) {
		// TODO Auto-generated method stub
		botonAtras.addActionListener(e -> {
			setVisible(false);
			ventanaAnterior.setVisible(true);
		});
	}

}
