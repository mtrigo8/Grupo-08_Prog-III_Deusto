package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import domain.Liga;

public class JFrameCalendario extends JFramePadre {
	private Liga liga;
	private static final long serialVersionUID = 1L;

	public JFrameCalendario(ArrayList<Liga> ligas, Liga liga) {
		super();
		this.liga = liga;
		this.ligas = ligas;
		Vector<String> columnNames = new Vector<String>(Arrays.asList("Fecha", "Equipo Local", "Equipo Visitante", "Resultado"));
		JTable table = new JTable(new DefaultTableModel(new Vector<Vector<Object>>(), columnNames));
		//Se modifica el modelo de selección de la tabla para que se pueda selecciona únicamente una fila
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//Se define el comportamiento el evento de selección de una fila de la tabla
		table.getSelectionModel().addListSelectionListener(e -> {
			// Aquí se puede agregar el código para manejar la selección de una fila
		});
		
		
		table.setBounds(50, 50, table.getWidth(), table.getHeight());
		super.panel.add(table);
	}

	@Override
	public void usoBotonAtras() {
		// TODO Auto-generated method stub
		botonAtras.addActionListener(e -> {
			setVisible(false);
			JFrameLiga jfl = new JFrameLiga(ligas, liga);
			jfl.setVisible(true);
		});
	}
	
	

}
