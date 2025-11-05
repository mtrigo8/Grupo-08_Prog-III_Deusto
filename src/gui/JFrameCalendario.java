package gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import domain.Liga;
import domain.Partido;

public class JFrameCalendario extends JFramePadre {
	private int jornadaSeleccionada = 1;
	private Liga liga;
	private static final long serialVersionUID = 1L;

	public JFrameCalendario(Liga liga, JFramePadre ventanaAnterior) {
		super();
		super.framePrevio = ventanaAnterior;
		this.liga = liga;
		JPanel panel = super.panel;
		Vector<String> columnNames = new Vector<String>(Arrays.asList("Fecha", "Equipo Local", "Equipo Visitante", "Resultado"));
		DefaultTableModel mDatTab = new DefaultTableModel(new Vector<Vector<Object>>(), columnNames);
		JTable table = new JTable(mDatTab) {
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		table.setRowHeight(30);
		table.getTableHeader().setReorderingAllowed(false);
		//Se modifica el modelo de selección de la tabla para que se pueda selecciona únicamente una fila
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//Se define el comportamiento el evento de selección de una fila de la tabla
		table.getSelectionModel().addListSelectionListener(e -> {
			
		});
		for (Partido partido : this.liga.getCalendario().get(jornadaSeleccionada - 1)) {
			String golLocal = Integer.toString(partido.getGolesLocal());
			String golVisitante = Integer.toString(partido.getGolesVisitante());
			String resultado = golLocal + " - " + golVisitante;
			mDatTab.addRow(new Object[] {partido.getFecha().toString(), partido.getEquipoLocal().getNombre(), partido.getEquipoVisitante().getNombre(), resultado});
			}
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBar(new JScrollBar());
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(25, 30));
		scrollPane.setBounds(0, 100, 1000, 400);
		panel.add(scrollPane);
		this.add(panel);
		usoBotonAtras(super.framePrevio);
		}
			
		
		
				

	

}
