package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import domain.Liga;
import domain.Partido;

public class JFrameCalendario extends JFramePadre {
	private int jornadaSeleccionada = 1;
	private Liga liga;
	private static final long serialVersionUID = 1L;
	private DefaultTableModel  mDatTab;
	private JTable tablaCalendario;
	private JScrollPane scrollPane;
	private int filaCalendario = -1;
	private JComboBox<Integer> seleccionarJornada;
	
	public JFrameCalendario(Liga liga, JFramePadre ventanaAnterior) {
		super();
		super.framePrevio = ventanaAnterior;
		this.liga = liga;
		JPanel panel = super.panel;
		panel.setLayout(new BorderLayout());
		iniciarizarCalendario();
		cargarCalendario();
		//Filtrar por jornada
		
		seleccionarJornada = new JComboBox<>();
		for (int i=0; i <=liga.getCalendario().size(); i++) {
			seleccionarJornada.addItem(i);
		}

		seleccionarJornada.addActionListener(e -> {
			Integer jornada = (int) seleccionarJornada.getSelectedItem();
			cargarCalendarioFiltro(jornada);
		});
		JPanel panelNorte = new JPanel(new BorderLayout()); 
	
		panelNorte.add(botonAtras, BorderLayout.WEST);
		JPanel panelNorteCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelNorteCentro.add(new JLabel("Seleccionar Jornada"));
		panelNorteCentro.add(seleccionarJornada);
		panelNorte.add(panelNorteCentro);
		tablaCalendario.setRowHeight(30);
		tablaCalendario.getTableHeader().setReorderingAllowed(false);
		//Se modifica el modelo de selección de la tabla para que se pueda selecciona únicamente una fila
		tablaCalendario.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//Se define el comportamiento el evento de selección de una fila de la tabla
		tablaCalendario.getSelectionModel().addListSelectionListener(e -> {
			
		});
		this.scrollPane = new JScrollPane(tablaCalendario);
		this.scrollPane.setVerticalScrollBar(new JScrollBar());
		this.scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(25, 30));
		this.scrollPane.setBounds(0, 100, 1000, 400);
		
		
		panel.add(panelNorte, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		this.add(panel);
		usoBotonAtras(super.framePrevio);
		}
	public void iniciarizarCalendario () {
		Vector<String> columnNames = new Vector<String>(Arrays.asList("Jornada", "Fecha", "Equipo Local", "Equipo Visitante", "Resultado"));
		mDatTab = new DefaultTableModel(new Vector<Vector<Object>>(), columnNames);
		tablaCalendario = new JTable(mDatTab) {
			public boolean isCellEditable(int row, int column) {
				return false;
			};
		};
		TableCellRenderer calendarioRenderer = new TableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				// TODO Auto-generated method stub
				JLabel result = new JLabel(value.toString());
				result.setHorizontalAlignment(JLabel.CENTER);
				
				//Cambiar color de fondo
				if(filaCalendario != -1 && row == filaCalendario){
					result.setBackground(new Color(144,213,255));
					result.setForeground(Color.BLACK);
				}
				result.setOpaque(true);
				return result;
			}
		};
		
		
		tablaCalendario.setDefaultRenderer(Object.class, calendarioRenderer);
		//Definir anchura de la columna Jornada
		tablaCalendario.getColumnModel().getColumn(0).setPreferredWidth(50);
		tablaCalendario.getColumnModel().getColumn(0).setMaxWidth(50);
		//Definir anchura fecha y resultado
		tablaCalendario.getColumnModel().getColumn(1).setMaxWidth(100);
		tablaCalendario.getColumnModel().getColumn(4).setMaxWidth(100);
		
		//Cambiar el color de las filas al pasar con el raton por encima
		MouseMotionListener miMouseMotionListener = new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				Point puntoRaton = new Point(e.getX(), e.getY());
				filaCalendario = tablaCalendario.rowAtPoint(puntoRaton);

				tablaCalendario.repaint();
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		//Detectar que el raton abandona la tabla
		MouseAdapter miMouseAdapter = new MouseAdapter() {
			public void mouseExited(MouseEvent e) {
				filaCalendario = -1;
				tablaCalendario.repaint();
			}
		};
		tablaCalendario.addMouseMotionListener(miMouseMotionListener);
		tablaCalendario.addMouseListener(miMouseAdapter);
		
	}
			
	public void cargarCalendario () {
		mDatTab.setRowCount(0);
		for (int j : this.liga.getCalendario().keySet()) {
			ArrayList<Partido> jornada = liga.getCalendario().get(j);
			jornada.forEach(p -> this.mDatTab
					.addRow(new Object[] {String.valueOf(p.getJornada()), p.getFecha().toString(), p.getEquipoLocal().getNombre(), p.getEquipoVisitante().getNombre(), 
							String.valueOf(p.getGolesLocal())+" - "+ String.valueOf(p.getGolesVisitante())}));
		}
		
	}
	public void cargarCalendarioFiltro(int jornadaSeleccionada) {
		mDatTab.setRowCount(0);
		if (jornadaSeleccionada == 0) {
			cargarCalendario();
		}else {
		ArrayList<Partido> jornada = liga.getCalendario().get(jornadaSeleccionada);
		jornada.forEach(p -> this.mDatTab
				.addRow(new Object[] {String.valueOf(p.getJornada()), p.getFecha().toString(), p.getEquipoLocal().getNombre(), p.getEquipoVisitante().getNombre(), 
						String.valueOf(p.getGolesLocal())+" - "+ String.valueOf(p.getGolesVisitante())}));
		}
		}
	
				

	

}
