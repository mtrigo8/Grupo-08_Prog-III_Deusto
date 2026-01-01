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
	    this.liga = liga;
	    this.equipoSeleccionado = equipo;
	    
	    // Limpiar la tabla de la carga incorrecta del padre
	    DefaultTableModel model = (DefaultTableModel) tablaCalendario.getModel();
	    model.setRowCount(0);
	    
	    // Ahora SÍ cargar correctamente
	    cargarCalendario();
	}
	
	@Override
	public void cargarCalendario() {
		cargarCalendarioEquipo();
	}
	
	private void cargarCalendarioEquipo() {
		mDatTab = (DefaultTableModel) tablaCalendario.getModel();
		mDatTab.setRowCount(0);
		
		List<Partido> partidosEquipo = obtenerPartidosDelEquipo();
		for (Partido p : partidosEquipo) {
			mDatTab.addRow(new Object[] {
					String.valueOf(p.getJornada()), p.getFecha().toString(), p.getEquipoLocal().getNombre(), p.getEquipoVisitante().getNombre(), 
					String.valueOf(p.getGolesLocal())+" - "+ String.valueOf(p.getGolesVisitante())
			});
		}
	}

	private List<Partido> obtenerPartidosDelEquipo() {
    List<Partido> partidosEquipo = new ArrayList<Partido>();
    
    
    if (this.equipoSeleccionado == null) {
        return partidosEquipo; // Retorna lista vacía silenciosamente
    }
    
        
       // Verificación 2: La liga tiene calendario
       TreeMap<Integer, ArrayList<Partido>> calendario = this.equipoSeleccionado.getLiga().getCalendario();
        if (calendario == null) {
            System.err.println("La liga no tiene calendario inicializado");
            return partidosEquipo;
        }
        
        for (int jornada : calendario.keySet()) {
            ArrayList<Partido> partidos = calendario.get(jornada);
            
            // Verificación 3: La jornada tiene partidos
            if (partidos == null) {
                continue; // Saltamos esta jornada
            }
            
            for (Partido partido : partidos) {
                // Verificación 4: El partido y sus equipos no son null
                if (partido != null && partido.getEquipoLocal() != null) {
                    if (partido.getEquipoLocal().equals(this.equipoSeleccionado) || 
                        partido.getEquipoVisitante().equals(this.equipoSeleccionado)) {
                        partidosEquipo.add(partido);
                    }
                }
            }
        }
    
    return partidosEquipo;
}
}