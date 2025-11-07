package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
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
	private JPanel panelInformacion;
	private Color colorFondo;
	private Color colorPanelInfo;
	private Color colorTextoInfo;
	private Color colorTextoValores;
	private Color colorCabeceraTabla;
	private Color colorTextoCabecera;
	private Color colorFondoTabla;
	private Color colorFondoTablaAlterno;
	private Color colorTextoTabla;
	private Color colorSeleccion;
	private Color colorTextoSeleccion;
	public JFrameEquipo(Equipo equipo, JFramePadre ventanaAnterior) {
		this.listaJugadores = equipo.getJugadores();
		this.equipo = equipo;
		super.framePrevio = ventanaAnterior;
		this.liga = equipo.getLiga();
		JPanel mainPanel = super.panel;
		mainPanel.setLayout(new BorderLayout());
		
	    setImagenDeFondo(null);
        String nombreLiga = liga.getNombre();
        if (nombreLiga.equals("Premier")) {
        colorFondo = new Color(55, 0, 60); 
        colorPanelInfo = new Color(45, 0, 50); 
        colorTextoInfo = Color.WHITE;
        colorTextoValores = Color.WHITE;
        colorCabeceraTabla = new Color(30, 0, 35); 
        colorTextoCabecera = Color.WHITE;
        colorFondoTabla = colorFondo;
        colorFondoTablaAlterno = new Color(65, 10, 70); 
        colorTextoTabla = Color.WHITE;
        colorSeleccion = new Color(230, 230, 230); 
        colorTextoSeleccion = Color.BLACK;

    } else if (nombreLiga.equals("Bundesliga")) {
    	colorFondo = new Color(208, 1, 27); 
        colorPanelInfo = new Color(180, 1, 20); 
        colorTextoInfo = Color.WHITE;
        colorTextoValores = Color.WHITE;
        colorCabeceraTabla = new Color(100, 0, 10); 
        colorTextoCabecera = Color.WHITE;
        colorFondoTabla = colorFondo;
        colorFondoTablaAlterno = new Color(218, 10, 37); 
        colorTextoTabla = Color.WHITE;
        colorSeleccion = new Color(255, 255, 255); 
        colorTextoSeleccion = Color.BLACK;
        
    } else if (nombreLiga.equals("LaLiga")) {
    	
    	colorFondo = new Color(235, 235, 235); 
    	colorPanelInfo = new Color(245, 245, 245); 
    	colorTextoInfo = new Color(23, 58, 100); 
    	colorTextoValores = Color.BLACK;
        colorCabeceraTabla = new Color(23, 58, 100); 
        colorTextoCabecera = Color.WHITE;
        colorFondoTabla = Color.WHITE;
        colorFondoTablaAlterno = new Color(245, 245, 245);
        colorTextoTabla = Color.BLACK;
        colorSeleccion = colorCabeceraTabla; 
        colorTextoSeleccion = Color.WHITE;
        
    } else {
    	
    	colorFondo = new Color(30, 30, 30);
        colorPanelInfo = new Color(40, 40, 40);
        colorTextoInfo = Color.WHITE;
        colorTextoValores = Color.WHITE;
        colorCabeceraTabla = new Color(20, 20, 20);
        colorTextoCabecera = Color.WHITE;
        colorFondoTabla = colorFondo;
        colorFondoTablaAlterno = new Color(50, 50, 50);
        colorTextoTabla = Color.WHITE;
        colorSeleccion = Color.LIGHT_GRAY;
        colorTextoSeleccion = Color.BLACK;
    }
        
    mainPanel.setBackground(colorFondo);
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
		panelEscudo.setOpaque(false);
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
		panelPrincipal.setOpaque(false);
		mainPanel.add(panelPrincipal, BorderLayout.CENTER);
		//Crear panel de informacion
		this.panelInformacion = new JPanel();
		panelInformacion.setOpaque(false);
		panelInformacion.setBackground(colorPanelInfo);
		panelInformacion.setBorder(new EmptyBorder(15, 15, 15, 15));
		//Redimensionar la ventana
		panelInformacion.setPreferredSize(new Dimension(350,250));
		//Cargar datos del equipo
		inicializarPanelInformacion(equipo, panelInformacion);
				
		//Añadir ventana
		panelPrincipal.add(panelInformacion, BorderLayout.WEST);
		inicializarTablas();
		cargarJugadores();
		//Crear Scroll panel de la plantilla
		
		this.scrollJugadores = new JScrollPane(this.tablaJugadores);
		scrollJugadores.getViewport().setBackground(colorFondoTabla);
		// no me gusta sin borde scrollJugadores.setBorder(BorderFactory.createEmptyBorder());
		
		panelPrincipal.add(scrollJugadores,BorderLayout.CENTER);
		
		usoBotonAtras(super.framePrevio);
		add(mainPanel);		
		this.setContentPane(mainPanel);
	}
	private void inicializarTablas()  {
		Vector <String> cabeceraJugador = new Vector<String>(Arrays.asList("POS", "NOMBRE", "NACIONALIDAD","EDAD","NUMERO CAMISETA" ));
		this.modeloDatosJugador = new DefaultTableModel(new  Vector<Vector<Object>>(), cabeceraJugador);
		
		this.tablaJugadores = new JTable(this.modeloDatosJugador) {
			public boolean isCellEditable (int row, int column) {
				return false;
			}		
		};
		this.tablaJugadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.tablaJugadores.setRowHeight(25);
		tablaJugadores.setBackground(colorFondoTabla);
		tablaJugadores.setForeground(colorTextoTabla);
		//personalizar el header
		tablaJugadores.getTableHeader().setBackground(colorCabeceraTabla);
		tablaJugadores.getTableHeader().setForeground(colorTextoCabecera);
		tablaJugadores.getTableHeader().setFont(new Font("Arial", Font.BOLD,14));
		//para que ocupe toda la ventan la tabla
		tablaJugadores.setFillsViewportHeight(true);
		//Añadir listener a la tabla para que abra el JFrame de jugador
		TableCellRenderer miCellRenderer = new TableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel result = new JLabel(value.toString());
				result.setOpaque(true);
				result.setHorizontalAlignment(SwingConstants.CENTER);
				//Alternar color por columnas
				Color colorFondo;
				if (row % 2 == 0) {
					result.setBackground(colorFondoTabla);
					result.setForeground(colorTextoTabla);
				}else {
					result.setBackground(colorFondoTablaAlterno);
					result.setForeground(colorTextoTabla);
				}
				
				if (isSelected) {
					result.setBackground(colorSeleccion);
	                result.setForeground(colorTextoSeleccion);
				}
				return result;
				
			}
		};
		
		this.tablaJugadores.setDefaultRenderer(Object.class, miCellRenderer);
		
		tablaJugadores.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked (MouseEvent e) {
				tablaJugadores.repaint();
				if (e.getClickCount() == 2) {
					int row = tablaJugadores.rowAtPoint(e.getPoint());
					Jugador jugador = conseguirJugador(row);
					try {
						JFrameJugador jfj = new JFrameJugador(jugador, ventanaAnterior);
						jfj.setVisible(true);
						setVisible(false);
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			}
		});
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
		panel.setLayout(new GridLayout(6, 2, 5, 10));
		//Carga la informacion del equipo
		String[] informacion= {
				"Nombre:",
				"Ciudad:",
				"Liga:",
				"Estadio:",
				"Año de fundación:",
				"Titulos:",
	};
		String[] valores = extraerInformacion(e);
		//Por cada pieza de informacion crea un label y lo añade al panel
		for (int i=0; i<informacion.length;i++) {
			JLabel labelinfo=new JLabel(informacion[i]);
			labelinfo.setForeground(colorTextoInfo);
			labelinfo.setFont(new Font("Arial",Font.BOLD, 14));
			labelinfo.setHorizontalAlignment(JLabel.CENTER);
			panel.add(labelinfo);
			JLabel labelvalor= new JLabel (valores[i]);
			labelvalor.setForeground(colorTextoValores);
			labelvalor.setFont(new Font("Arial",Font.PLAIN,14));
			labelvalor.setHorizontalAlignment(JLabel.LEFT);
			panel.add(labelvalor);
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
	//Formato de los labels de informacion
	public void modificarLabel (JLabel label) {
		
	}

	//Devolver jugador seleccionado teniendo en cuenta todos las posibles excepciones
	 public Jugador conseguirJugador (int row) {
		 Jugador resultado = null;
		 
		 
		 //validar seleccion
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
		