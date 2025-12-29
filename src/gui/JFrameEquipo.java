package gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
	private int filaJugador = -1;
	
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
		//Añadir boton atras nuevo
		usoBotonAtras(ventanaAnterior);
		
		add(mainPanel);
		String rutaalineacion = "resources/images/logos/alineacion.png";
		BotonCircular btnAlineacion = new BotonCircular(new ImageIcon(rutaalineacion));
		btnAlineacion.setPreferredSize(new Dimension(40, 40));
		btnAlineacion.addActionListener(e -> {
		    JFrameAlineacion jfa = new JFrameAlineacion(JFrameEquipo.this, equipo);
		    setVisible(false);
		    jfa.setVisible(true);
		});

		JPanel panelIzquierda = new JPanel(new BorderLayout());
		panelIzquierda.setOpaque(false);

		panelIzquierda.add(panelInformacion, BorderLayout.NORTH); 
		
		JPanel panelBoton = new JPanel(); 
		panelBoton.setOpaque(false);
		panelBoton.add(btnAlineacion);
		panelIzquierda.add(panelBoton, BorderLayout.CENTER); 

		panelPrincipal.add(panelIzquierda, BorderLayout.WEST);
        
		this.setContentPane(mainPanel);
	}
	@Override
	public void usoBotonAtras (JFramePadre frameAnterior) {
		botonAtras.addActionListener(e ->{
			setVisible(false);
			if (frameAnterior.getClass().equals(JFrameCalendario.class)) {
				frameAnterior.setVisible(true);
			}else if (frameAnterior.getClass().equals(JFrameClasificacion.class)) {
				frameAnterior.setVisible(true);
			}else {
				JFrameListaEquipos jfle = new JFrameListaEquipos(liga, frameAnterior.framePrevio);
				jfle.setVisible(true);
			}
		});
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
				//Modifica el color de la fila si esta el cursor por encima
				if (filaJugador != -1 && row == filaJugador) {
					result.setBackground(colorSeleccion);
					result.setForeground(colorTextoSeleccion);
					result.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
						JFrameJugador jfj = new JFrameJugador(jugador, JFrameEquipo.this);
						jfj.setVisible(true);
						setVisible(false);
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			}
		});
		
		MouseMotionListener miMouseMotionListener = new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				Point puntoRaton = new Point(e.getX(), e.getY());
				filaJugador = tablaJugadores.rowAtPoint(puntoRaton);
				// Cambiar el cursor de la tabla
		        if (filaJugador != -1) {
		            tablaJugadores.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		        } else {
		            tablaJugadores.setCursor(Cursor.getDefaultCursor());
		        }
				tablaJugadores.repaint();
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
		MouseAdapter miMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseExited (MouseEvent e) {
				filaJugador = -1;
				tablaJugadores.repaint();
			}
		};
		tablaJugadores.addMouseMotionListener(miMouseMotionListener);
		tablaJugadores.addMouseListener(miMouseAdapter);
		
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

	//Devolver jugador seleccionado teniendo en cuenta todos las posibles excepciones
	 public Jugador conseguirJugador (int row) {
		 Jugador resultado = null;
		 
		 
		 //validar seleccion
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
	 private class BotonCircular extends JButton {
	    	
			private static final long serialVersionUID = 1L;
	    	private Icon icono;
	    	public BotonCircular (Icon ruta) {
	    		super ("");
	    		this.icono=ruta;
	    		setContentAreaFilled(false);
	    		setFocusPainted(false);
	    		setBorderPainted(false);
	    		setOpaque(false);
	    		setCursor (new Cursor(Cursor.HAND_CURSOR));
	    		
	    	}
	    	
	    	@Override
	    	  protected void paintComponent(Graphics g) {
	            Graphics2D g2 = (Graphics2D) g.create();
	            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	            Color color1 = new Color(0, 242, 254); 
	            Color color2 = new Color(79, 172, 254);
	            
	            GradientPaint gp = new GradientPaint(0, 0, color2, getWidth(), getHeight(), color1);
	            g2.setPaint(gp);
	            g2.fillOval(0, 0, getWidth(), getHeight());

	            if (icono instanceof ImageIcon) {
	                Image img = ((ImageIcon) icono).getImage();
	                int padding = 8;
	                g2.drawImage(img, padding, padding, getWidth() - padding * 2, getHeight() - padding * 2, this);
	            }
	            g2.setColor(new Color(255, 255, 255, 100));
	            g2.setStroke(new BasicStroke(1.5f));
	            g2.drawOval(1, 1, getWidth() - 3, getHeight() - 3);

	            g2.dispose();
	 }
	    }
}
		