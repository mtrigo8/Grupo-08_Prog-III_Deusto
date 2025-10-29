package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JPanel;
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

	public JFrameCalendario(ArrayList<Liga> ligas, Liga liga) {
		super();
		this.liga = liga;
		this.ligas = ligas;
		JPanel panel = super.panel;
		Vector<String> columnNames = new Vector<String>(Arrays.asList("Fecha", "Equipo Local", "Equipo Visitante", "Resultado"));
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
			String golLocal = Integer.toString(partido.getGolesLocal());
			String golVisitante = Integer.toString(partido.getGolesVisitante());
			String resultado = golLocal + " - " + golVisitante;
			mDatTab.addRow(new Object[] {partido.getFecha().toString(), partido.getEquipoLocal().getNombre(), partido.getEquipoVisitante().getNombre(), resultado});
			}
		table.setBounds(0, 100, 1000, 400);
		panel.add(scrollPane);
		this.add(panel);
		usoBotonAtras();
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
