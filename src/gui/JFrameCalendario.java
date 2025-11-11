package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import domain.Equipo;
import domain.Liga;
import domain.Partido;

public class JFrameCalendario extends JFramePadre {
	private int jornadaSeleccionada = -1;
	private Liga liga;
	private static final long serialVersionUID = 1L;
	private DefaultTableModel  mDatTab;
	private JTable tablaCalendario;
	private JScrollPane scrollPane;
	private int filaCalendario = -1;
	private JComboBox<Integer> seleccionarJornada;
	private HashMap<String, ImageIcon> mapaEscudos;
	
	public JFrameCalendario(Liga liga, JFramePadre ventanaAnterior) {
		super();
		super.framePrevio = ventanaAnterior;
		this.liga = liga;
		JPanel panel = super.panel;
		panel.setLayout(new BorderLayout());
		iniciarizarCalendario();
		cargarCalendario();
		//Cargar los escudos tan solo una vez
		cargarEscudos();
		//Filtrar por jornada
		//Crear el comboBox con todas las jornadas
		seleccionarJornada = new JComboBox<>();
		for (int i=0; i <=liga.getCalendario().size(); i++) {
			seleccionarJornada.addItem(i);
		}
		//Añadir el listener al combo Box para que se filtren las jornadas
		seleccionarJornada.addActionListener(e -> {
			Integer jornada = (int) seleccionarJornada.getSelectedItem();
			cargarCalendarioFiltro(jornada);
		});
		//Crear un panel para añadir el boton atras y el combo box
		JPanel panelNorte = new JPanel(new BorderLayout()); 
		//Añadir boton atras a la izquierda
		panelNorte.add(botonAtras, BorderLayout.WEST);
		//Crear un panel auxiliar para que el combo box no ocupe toda la parte superior de la pantalla
		JPanel panelNorteCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panelNorteCentro.add(new JLabel("Seleccionar Jornada"));
		panelNorteCentro.add(seleccionarJornada);
		panelNorte.add(panelNorteCentro);
		//Establecer altura de las filas
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

		
		
		panel.add(panelNorte, BorderLayout.NORTH);
		panel.add(scrollPane, BorderLayout.CENTER);
		
		this.add(panel);
		usoBotonAtras(super.framePrevio);
		}
	public void iniciarizarCalendario () {
		Vector<String> columnNames = new Vector<String>(Arrays.asList("Jornada", "Fecha", "Equipo Local", "Equipo Visitante", "Result"));
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
				
				ImageIcon escudo = mapaEscudos.get(result.getText());
				try {
					result.setIcon(escudo);
				} catch (Exception e) {
					System.err.println("No se encontro el escudo del equipo: "+result.getText());
				}
				if (row % 2 == 0) {
					result.setBackground(new Color(196, 207, 196));
				}
				//Cambiar color de fondo segun la posicion del raton
				if(filaCalendario != -1 && row == filaCalendario){
					result.setBackground(new Color(144,213,255));
					result.setForeground(Color.BLACK);
				}
				result.setOpaque(true);
				return result;
			}
		};
		TableCellRenderer headerRenderer = new TableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				// TODO Auto-generated method stub
				JLabel result = new JLabel(value.toString());
				result.setHorizontalAlignment(JLabel.CENTER);
				result.setFont(new Font("Arial", Font.BOLD, 15));
				result.setOpaque(true);
				if (result.getText().equals("Jornada")) {
					result.setText("J");
				}
				//Poner espacios a los lados
				result.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
				Color colorFondo = table.getBackground();
				Color colorLetra = Color.WHITE;
				switch (liga.getNombre()) {
				case "LaLiga":
					colorFondo = new Color(235, 235, 235);
					colorLetra = Color.BLACK;
					break;
				case "Bundesliga":
					colorFondo = new Color(208, 1, 27);
					break;
				case "Premier":
					colorFondo = new Color(55, 0, 60); 
				}
				result.setBackground(colorFondo);
				result.setForeground(colorLetra);
				if (column == 2) {
					ImageIcon icono = new ImageIcon("resources/images/logos/local.png");
					ImageIcon iconoAjustado = new ImageIcon(icono.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
					result.setIcon(iconoAjustado);
				}else if (column == 3){
					ImageIcon icono = new ImageIcon("resources/images/logos/avion.png");
					ImageIcon iconoAjustado = new ImageIcon(icono.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
					result.setIcon(iconoAjustado);
				}
				return result;
			}
		};
		this.tablaCalendario.getTableHeader().setDefaultRenderer(headerRenderer);
		
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
		
		//Crear listener para acceder a los equipos a traves de la tabla
		tablaCalendario.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked (MouseEvent e) {
				tablaCalendario.repaint();
				if(e.getClickCount() == 2) {
					int row = tablaCalendario.rowAtPoint(e.getPoint());
					int column = tablaCalendario.columnAtPoint(e.getPoint());
					
					String nombreEquipo =(String) tablaCalendario.getValueAt(row, column);				
					Equipo equipoEncontrado = null;
					
					for (Equipo eq : liga.getEquipos()) {
						if (eq.getNombre().equals(nombreEquipo)) {
							equipoEncontrado = eq;
							break;
						}
					}
					try {
						JFrameEquipo jfe = new JFrameEquipo(equipoEncontrado, JFrameCalendario.this);
						jfe.setVisible(true);
						setVisible(false);
					} catch (Exception e2) {
						System.err.println("El valor del equipo es null");
					}
				}
			}
			
		
		});
		
		
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
	public void cargarEscudos () {
		mapaEscudos = new HashMap<String, ImageIcon>();
		for (Equipo e : liga.getEquipos()) {
			ImageIcon escudo = new ImageIcon("resources/images/equipos/"+liga.getNombre()+"/"+e.getNombrePNGEquipo()+".png");
			ImageIcon escudoAjustado = new ImageIcon(escudo.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
			mapaEscudos.put(e.getNombre(), escudoAjustado);
		}
	}
}
