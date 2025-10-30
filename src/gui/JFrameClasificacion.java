package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import domain.Equipo;
import domain.Liga;
import domain.Partido;

public class JFrameClasificacion extends JFramePadre {

	private int jornadaSeleccionada = 1;
	private Liga liga;
	private static final long serialVersionUID = 1L;
	private int pos = 1;
	
	public JFrameClasificacion(ArrayList<Liga> ligas, Liga liga) {
		super();
		this.liga = liga;
		this.ligas = ligas;
		ArrayList<Equipo> clasificacion = this.liga.getEquipos();
		Collections.sort(clasificacion);
		Vector<String> columnNames = new Vector<String>(Arrays.asList("Pos", "Equipo", "Pts", "PJ", "DG"));
		DefaultTableModel mDatTab = new DefaultTableModel(new Vector<Vector<Object>>(), columnNames);
		JTable table = new JTable(mDatTab);
		table.setRowHeight(30);
		
		table.getTableHeader().setReorderingAllowed(false);
		panel.setLayout(null);
		//Se modifica el modelo de selección de la tabla para que se pueda selecciona únicamente una fila
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//Se define el comportamiento el evento de selección de una fila de la tabla
		table.getSelectionModel().addListSelectionListener(e -> {
			
		});
		for (Equipo equipo : clasificacion) {
			String posicion =  Integer.toString(pos);
			String puntos = Integer.toString(equipo.getPuntos());
			String partidos = Integer.toString(equipo.getPartidosJugados());
			String goles = Integer.toString(equipo.getGoles());
			mDatTab.addRow(new Object[] {posicion, equipo.getNombre(), puntos, partidos, goles});
			pos++;
			}
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(0, 100, 1000, 400);
		panel.add(scrollPane);
		this.add(panel);
		this.setSize(1000,600);
		this.setVisible(true);
		table.setVisible(true);
		usoBotonAtras(ligas, liga);
		}
		
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void usoBotonAtras(ArrayList<Liga> ligas, Liga liga) {
		botonAtras.addActionListener(e -> {
			setVisible(false);
			JFrameLiga jfl1 = new JFrameLiga(ligas, liga);
			jfl1.setVisible(true);
		});
		
	}

}
