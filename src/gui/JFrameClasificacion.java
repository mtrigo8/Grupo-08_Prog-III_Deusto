package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import domain.Liga;
import domain.Partido;

public class JFrameClasificacion extends JFramePadre {

	private int jornadaSeleccionada = 1;
	private Liga liga;
	private static final long serialVersionUID = 1L;
	
	public JFrameClasificacion(ArrayList<Liga> ligas, Liga liga) {
		super();
		Collections.sort(liga.getEquipos());
		this.liga = liga;
		this.ligas = ligas;
		Vector<String> columnNames = new Vector<String>(Arrays.asList("Pos", "Equipo", "Pts", "PJ", "DG"));
		DefaultTableModel mDatTab = new DefaultTableModel(new Vector<Vector<Object>>(), columnNames);
		JTable table = new JTable(mDatTab);
		table.setRowHeight(30);
		JScrollPane scrollPane = new JScrollPane(table);
		table.getTableHeader().setReorderingAllowed(false);
		//Se modifica el modelo de selección de la tabla para que se pueda selecciona únicamente una fila
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//Se define el comportamiento el evento de selección de una fila de la tabla
		table.getSelectionModel().addListSelectionListener(e -> {
			
		});
		for (Partido partido : this.liga.getCalendario().get(jornadaSeleccionada - 1)) {
			mDatTab.addRow(new Object[] {});
			}
		table.setBounds(0, 100, 1000, 400);
		panel.add(scrollPane);
		this.add(panel);
		}
		
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void usoBotonAtras() {
		botonAtras.addActionListener(e -> {
			setVisible(false);
			JFrameLiga jfl1 = new JFrameLiga(ligas, liga);
			jfl1.setVisible(true);
		});
		
	}

}
