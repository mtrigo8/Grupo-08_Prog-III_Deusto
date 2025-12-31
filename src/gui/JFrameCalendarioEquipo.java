package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.swing.table.DefaultTableModel;

import domain.Equipo;
import domain.Liga;
import domain.Partido;

public class JFrameCalendarioEquipo extends JFrameCalendario{
	private static final long serialVersionUID = 1L;
	private Equipo equipoSeleccionado;
	private DefaultTableModel mDatTab;
	private Liga liga;
	
	
	public JFrameCalendarioEquipo(Liga liga, Equipo equipo, JFramePadre ventanaAnterior) {
		super(liga, ventanaAnterior);
		// TODO Auto-generated constructor stub
		this.liga = liga;
		this.equipoSeleccionado = equipo;
		System.out.println(equipo);
	}
	/*
	@Override
	public void cargarCalendario() {
		cargarCalendarioEquipo();
	}
	
	private void cargarCalendarioEquipo() {
		mDatTab = (DefaultTableModel) tablaCalendario.getModel();
		mDatTab.setRowCount(0);
		
		List<Partido> partidosEquipo = obtenerPartidosDelEquipo(equipoSeleccionado);
		for (Partido p : partidosEquipo) {
			mDatTab.addRow(new Object[] {
					String.valueOf(p.getJornada()), p.getFecha().toString(), p.getEquipoLocal().getNombre(), p.getEquipoVisitante().getNombre(), 
					String.valueOf(p.getGolesLocal())+" - "+ String.valueOf(p.getGolesVisitante())
			});
		}
	}

	private List<Partido> obtenerPartidosDelEquipo (Equipo equipoSeleccionado) {
		List<Partido> partidosEquipo = new ArrayList<Partido>();
		
		TreeMap<Integer, ArrayList<Partido>> calendario = equipoSeleccionado.getLiga().getCalendario();
		
		for (int jornada : calendario.keySet()) {
			for (Partido partido : calendario.get(jornada)) {
				if (partido.getEquipoLocal().equals(equipoSeleccionado) || partido.getNombreEquipoVisitante().equals(equipoSeleccionado)) {
					partidosEquipo.add(partido);
				}
			}
		}
		
		return partidosEquipo;
	}
*/
}