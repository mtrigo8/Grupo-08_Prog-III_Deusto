package gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

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
	
	public JFramePlantilla(ArrayList<Liga> ligas, Liga liga, Equipo equipo) {
		
		super();
		this.ligas = ligas;
		this.liga = liga;
		
		HashMap<String,ArrayList<Jugador>> jugadorPorPos = new HashMap<>(); 
		for (Jugador jugador : equipo.getJugadores()) {
			String posicion = jugador.getPosicion().toString();
			if (!(jugadorPorPos.keySet().contains(posicion))){
				jugadorPorPos.put(posicion, new ArrayList<Jugador>());
			}
			jugadorPorPos.get(posicion).add(jugador);
		}
		for(String clave : jugadorPorPos.keySet()) {
			Vector<String> columnNames = new Vector<String>(Arrays.asList(clave, "Nombre"));
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
			for (Jugador jug : jugadorPorPos.get(clave)) {
				mDatTab.addRow(new Object[] {clave, jug.getNombre()});
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
		
	}
	
	
	
	
	
	
	
	
	
	@Override
	public void usoBotonAtras(ArrayList<Liga> ligas, Liga liga) {
		// TODO Auto-generated method stub
		botonAtras.addActionListener(e -> {
			setVisible(false);
			JFrameClasificacion flc = new JFrameClasificacion(ligas, liga);
			JFrameListaEquipos fle = new JFrameListaEquipos(ligas, liga);
			fle.setVisible(true);
		});
		
	}

}
