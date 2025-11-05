package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import domain.Equipo;
import domain.Jugador;
import domain.Liga;
import domain.Jugador.TipoPosicion;

public class JFrameEquipo extends JFramePadre{
	private ArrayList<Liga> ligas;
	private Liga liga;
	private Equipo equipo;
	private JFramePadre ventanaAnterior;
	private DefaultTableModel modeloDatosJugador;
	private JTable tablaJugadores;
	private HashMap<Jugador.TipoPosicion,ArrayList<Jugador>> listaJugadores;
	private JScrollPane scrollJugadores;
	
	public JFrameEquipo(Equipo equipo, JFramePadre ventanaAnterior) {
		this.listaJugadores = equipo.getJugadores();
		this.equipo = equipo;
		super.framePrevio = ventanaAnterior;
		this.liga = equipo.getLiga();
		JPanel mainPanel = super.panel;
		mainPanel.setLayout(new BorderLayout());
		this.inicializarTablas();
		this.cargarJugadores();
		//Cargar el icono
		String ruta = "resources/images/equipos/"+liga.getNombre().toLowerCase()+"/"+equipo.getNombrePNGEquipo().toLowerCase()+".png";
		ImageIcon escudoOriginal = null;
		try {
			escudoOriginal = new ImageIcon(ruta);
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("No se ha encontrado la imagen en la direccion: "+ruta);
		}
		//Crear un label donde aparecera el escudo y un panel que lo contenga
		JPanel panelEscudo = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		//Modificar tamaño de imagen
		int ANCHO_MAXIMO = 100;
		ImageIcon escudoEscalado;
		if (escudoOriginal.getIconWidth()>ANCHO_MAXIMO) {
			int altoNuevo = (int) ((double) ANCHO_MAXIMO / escudoOriginal.getIconWidth() * escudoOriginal.getIconHeight());
			Image imagen = escudoOriginal.getImage();
			Image imagenEsalada = imagen.getScaledInstance(ANCHO_MAXIMO, altoNuevo, Image.SCALE_SMOOTH);
			escudoEscalado = new ImageIcon(imagenEsalada);
		}else {
			escudoEscalado = escudoOriginal;
		}
		//Añadir imagen
		JLabel labelEscudo = new JLabel(escudoEscalado);
		panelEscudo.add(labelEscudo);	
		mainPanel.add(panelEscudo, BorderLayout.NORTH);
		//Crear panel contenedor de la informacion y alineaciones
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		mainPanel.add(panelPrincipal, BorderLayout.CENTER);
		//Crear panel de informacion
		JPanel panelInformacion = new JPanel();
		//Crear Scroll panel de la plantilla
		
		this.scrollJugadores = new JScrollPane(this.tablaJugadores);
		
		//Cargar datos del equipo
		inicializarPanelInformacion(equipo, panelInformacion);
		//Redimensionar la ventana
		panelInformacion.setPreferredSize(new Dimension(250,200));
		panelInformacion.setBackground(Color.BLACK);
		panelInformacion.setForeground(Color.WHITE);	
		//Añadir ventana
		panelPrincipal.add(panelInformacion, BorderLayout.WEST);
		inicializarTablas();
		cargarJugadores();
		
		panelPrincipal.add(scrollJugadores);
		
		usoBotonAtras(super.framePrevio);
		add(mainPanel);		

	}
	private void inicializarTablas() {
		Vector <String> cabeceraJugador = new Vector<String>(Arrays.asList("POS", "NOMBRE", "NACIONALIDAD","EDAD","NUMERO CAMISETA" ));
		this.modeloDatosJugador = new DefaultTableModel(new  Vector<Vector<Object>>(), cabeceraJugador);
		
		this.tablaJugadores = new JTable(this.modeloDatosJugador) {
			public boolean isCellEditable (int row, int column) {
				return false;
			}		
		};
		this.tablaJugadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//Añadir listener a la tabla para que abra el JFrame de jugador
		this.tablaJugadores.getSelectionModel().addListSelectionListener(e ->{
			
			//Crear el nuevo JFrame consiguendo la posicion del jugador en el hashmap mediante el nume
		
			Jugador jugador = conseguirJugador();
			try {
				JFrameJugador jfj = new JFrameJugador(jugador, ventanaAnterior);
				jfj.setVisible(true);
			} catch (Exception e2) {
				// TODO: handle exception
			}
			this.setVisible(false);
			//System.out.println("Aparece nueva ventana");
		});
		
		TableCellRenderer miCellRenderer = new TableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel result = new JLabel(value.toString());
				//Alternar color por columnas
				Color colorFondo;
				if (row % 2 == 0) {
					colorFondo = new Color(240,248,255);
				}else {
					colorFondo = new Color(245, 245, 245);
				}
				result.setBackground(colorFondo);
				result.setOpaque(true);
				result.setHorizontalAlignment(SwingConstants.CENTER);

				return result;
			}
		};
		this.tablaJugadores.setDefaultRenderer(Object.class, miCellRenderer);
	}
	

	private void cargarJugadores() {
		this.modeloDatosJugador.setRowCount(0);
		ArrayList <Jugador> jugadoresPorPosicion = null;
		for(Jugador.TipoPosicion pos : listaJugadores.keySet()) {
			jugadoresPorPosicion = listaJugadores.get(pos);
			jugadoresPorPosicion.forEach(j -> this.modeloDatosJugador
					.addRow(new Object[] {String.valueOf(j.getPosicion()), j.getNombre(), j.getNacionalidad(),
							String.valueOf(j.getEdad()), String.valueOf(j.getNumeroCamiseta())})
					);
			}
		}	


	
	//Funcion que inicializa los datos del equipo al panel de Informacion
	public void inicializarPanelInformacion (Equipo e, JPanel panel) {
		//Carga la informacion del equipo
		String[] informacion = extraerInformacion(e);
		JLabel label;
		//Por cada pieza de informacion crea un label y lo añade al panel
		for (String inf : informacion) {
			label = new JLabel(inf);
			panel.add(label);
		}
	}
	//Crea un array con la informacion del equipo
	public String[] extraerInformacion (Equipo e) {
		return new String[] {
				e.getNombre(),
				e.getCiudad(),
				e.getLiga().getNombre(),
				e.getEstadio(),
				String.valueOf(e.getAnyoFundacion()),
				String.valueOf(e.getTitulos())
		};
	}

	//Devolver jugador seleccionado teniendo en cuenta todos las posibles excepciones
	 public Jugador conseguirJugador () {
		 Jugador resultado = null;
		 int row = tablaJugadores.getSelectedRow();
		 
		 //validar seleccion
		 System.out.println("tablaJugadores = " + tablaJugadores);
		 System.out.println("row = " + row);
		 if (row < 0) {	//Hay un error la fila seleccionada aparece como -1
			 System.err.println("No hay fila seleccionada");
			 return resultado;
		 }
		 //Obtener posicion
		 String posicionString = (String) tablaJugadores.getValueAt(row, 0);
		 TipoPosicion posicion = TipoPosicion.valueOf(posicionString);
		 //Obtener nombre del jugador
		 String nomJugadorBuscar = (String) tablaJugadores.getValueAt(row, 1);
		 
		 ArrayList<Jugador> posiblesJugadores = this.listaJugadores.get(posicion);
		 if (posiblesJugadores == null) {
			 System.err.println("No hay jugadores en esta posicion");
			 return resultado;
		 }
		 
		 for (Jugador j : posiblesJugadores) {
			 if(j.getNombre().equals(nomJugadorBuscar)) {
				 resultado = j;
				 break;
			 }
		 }		 
		 return resultado;
	 }
}
		/*
		//Apartir de aqui es todo lo hecho en plantilla que se ha borrado
		for(TipoPosicion clave : equipo.getJugadores().keySet()) {
			Vector<String> columnNames = new Vector<String>(Arrays.asList(clave.toString(), "Nombre"));
		}
		panel.setLayout(null);
		this.add(panel);
		usoBotonAtras(super.framePrevio);
		for(TipoPosicion clave : equipo.getJugadores().keySet()) {
			Vector<String> columnNames = new Vector<String>(Arrays.asList(clave.toString(), "Nombre"));
			DefaultTableModel mDatTab = new DefaultTableModel(new Vector<Vector<Object>>(), columnNames);
			JTable table = new JTable(mDatTab);
			table.setRowHeight(30);
			table.getTableHeader().setReorderingAllowed(false);

			//Se modifica el modelo de selección de la tabla para que se pueda selecciona únicamente una fila
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			//Se define el comportamiento el evento de selección de una fila de la tabla
			table.getSelectionModel().addListSelectionListener(e1 -> {
				
			});
			for (Jugador jug : equipo.getJugadores().get(clave)) {
				mDatTab.addRow(new Object[] {clave, jug.getNombre()});
				}
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setBounds(0, 100, 1000, 400);
			panel.add(scrollPane);
			this.setSize(1000,600);
			this.setVisible(true);
			table.setVisible(true);
		
	}}
	*/
