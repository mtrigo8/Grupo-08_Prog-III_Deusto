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
import java.awt.geom.RoundRectangle2D;
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
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import domain.Equipo;
import domain.Jugador;
import domain.Liga;
import domain.Jugador.TipoPosicion;
import javax.swing.table.TableRowSorter;

public class JFrameEquipo extends JFramePadre{
	private ArrayList<Liga> ligas;
	private Liga liga; 	
	private Equipo equipo;
	private DefaultTableModel modeloDatosJugador;
	private JTable tablaJugadores;
	private HashMap<Jugador.TipoPosicion,ArrayList<Jugador>> listaJugadores;
	private JScrollPane scrollJugadores;
	private PanelInformacionAnimado panelInformacion;
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
	private Color colorAcento;
	private int filaJugador = -1;
	private float animacionEscudo = 0f;
	private Timer timerEscudo;
	
	public JFrameEquipo(Equipo equipo, JFramePadre ventanaAnterior) {
		this.listaJugadores = equipo.getJugadores();
		this.equipo = equipo;
		super.framePrevio = ventanaAnterior;
		this.liga = equipo.getLiga();
		JPanel mainPanel = super.panel;
		mainPanel.setLayout(new BorderLayout());
		
	    setImagenDeFondo(null);
        String nombreLiga = liga.getNombre();
        colorFondo = new Color(185, 255, 183); 
        colorPanelInfo = new Color(185, 230, 183); 
        colorTextoInfo = new Color(33, 33, 33);
        colorTextoValores = Color.DARK_GRAY;
        colorCabeceraTabla = new Color(100, 220, 150); 
        colorTextoCabecera = Color.BLACK;
        colorFondoTabla = Color.WHITE;
        colorFondoTablaAlterno = new Color(240, 255, 240); 
        colorTextoTabla = Color.BLACK;
        colorSeleccion = new Color(135, 205, 133); 
        colorTextoSeleccion = Color.BLACK;
        colorAcento = new Color(100, 220, 150);
        
    mainPanel.setBackground(colorFondo);
		//Cargar el icono
		String ruta = "resources/images/equipos/"+liga.getNombre().toLowerCase()+"/"+equipo.getNombrePNGEquipo().toLowerCase()+".png";
		ImageIcon escudoOriginal = null;
		try {
			escudoOriginal = new ImageIcon(ruta);
		} catch (Exception e) {
			System.err.println("No se ha encontrado la imagen en la direccion: "+ruta);
		}
		//Crear panel animado para el escudo
		PanelEscudoAnimado panelEscudo = new PanelEscudoAnimado(escudoOriginal, colorAcento);
		panelEscudo.setPreferredSize(new Dimension(getWidth(), 150));
		mainPanel.add(panelEscudo, BorderLayout.NORTH);
		
		//Crear panel contenedor de la informacion y alineaciones
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		panelPrincipal.setOpaque(false);
		mainPanel.add(panelPrincipal, BorderLayout.CENTER);
		
		//Crear panel de informacion animado
		this.panelInformacion = new PanelInformacionAnimado(colorPanelInfo, colorAcento);
		panelInformacion.setBorder(new EmptyBorder(20, 20, 20, 20));
		panelInformacion.setPreferredSize(new Dimension(380,280));
		
		//Cargar datos del equipo
		inicializarPanelInformacion(equipo, panelInformacion);
				
		inicializarTablas();
		cargarJugadores();
		
		//Crear Scroll panel de la plantilla con bordes redondeados
		this.scrollJugadores = new JScrollPane(this.tablaJugadores);
		scrollJugadores.getViewport().setBackground(colorFondoTabla);
		scrollJugadores.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		scrollJugadores.setOpaque(false);
		scrollJugadores.getViewport().setOpaque(false);
		
		//Añadir boton atras nuevo
		usoBotonAtras(ventanaAnterior);
		
		add(mainPanel);
		
		String rutaalineacion = "resources/images/logos/alineacion.png";
		BotonCircular btnAlineacion = new BotonCircular(new ImageIcon(rutaalineacion));
		btnAlineacion.setPreferredSize(new Dimension(60, 60));
		btnAlineacion.setToolTipText("Alineacion");
		btnAlineacion.addActionListener(e -> {
		    JFrameAlineacion jfa = new JFrameAlineacion(JFrameEquipo.this, equipo);
		    setVisible(false);
		    jfa.setVisible(true);
		});
		//Crear el boton de calendario
		String rutaCalendario = "resources/images/logos/calendario.png";
		BotonCircular btnCalendario = new BotonCircular(new ImageIcon(rutaCalendario));
		btnCalendario.setPreferredSize(new Dimension(60, 60));
		btnCalendario.setToolTipText("Calendario");
		btnCalendario.addActionListener(e -> {
			JFrameCalendarioEquipo jfce = new JFrameCalendarioEquipo(this.liga, this.equipo, JFrameEquipo.this);
			setVisible(false);
			jfce.setVisible(true);
		});
		
		JPanel panelIzquierda = new JPanel(new BorderLayout(0, 15));
		panelIzquierda.setOpaque(false);
		panelIzquierda.add(panelInformacion, BorderLayout.NORTH); 
		
		JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10)); 
		panelBoton.setOpaque(false);
		panelBoton.add(btnAlineacion);
		panelBoton.add(btnCalendario);
		panelIzquierda.add(panelBoton, BorderLayout.CENTER); 

		panelPrincipal.add(panelIzquierda, BorderLayout.WEST);
		panelPrincipal.add(scrollJugadores,BorderLayout.CENTER);
        
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
		Vector <String> cabeceraJugador = new Vector<String>(Arrays.asList("POS", "NOMBRE", "NACIONALIDAD","EDAD","Nº" ));
		this.modeloDatosJugador = new DefaultTableModel(new  Vector<Vector<Object>>(), cabeceraJugador);
		
		this.tablaJugadores = new JTable(this.modeloDatosJugador) {
			public boolean isCellEditable (int row, int column) {
				return false;
			}		
		};
		this.tablaJugadores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.tablaJugadores.setRowHeight(32);
		tablaJugadores.setBackground(colorFondoTabla);
		tablaJugadores.setForeground(colorTextoTabla);
		tablaJugadores.setFont(new Font("Arial", Font.PLAIN, 13));
		
		//personalizar el header
		tablaJugadores.getTableHeader().setBackground(colorCabeceraTabla);
		tablaJugadores.getTableHeader().setForeground(colorTextoCabecera);
		tablaJugadores.getTableHeader().setFont(new Font("Arial", Font.BOLD,13));
		tablaJugadores.getTableHeader().setPreferredSize(new Dimension(0, 40));
		tablaJugadores.setShowGrid(false);
		tablaJugadores.setIntercellSpacing(new Dimension(0, 0));
		
		//para que ocupe toda la ventana la tabla
		tablaJugadores.setFillsViewportHeight(true);

		
		TableCellRenderer miCellRenderer = new TableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel result = new JLabel(value.toString()) {
					@Override
					protected void paintComponent(Graphics g) {
						Graphics2D g2 = (Graphics2D) g.create();
						g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						
						if (filaJugador != -1 && row == filaJugador) {
							g2.setColor(colorSeleccion);
							g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);
						} else if (row % 2 == 0) {
							g2.setColor(colorFondoTabla);
							g2.fillRect(0, 0, getWidth(), getHeight());
						} else {
							g2.setColor(colorFondoTablaAlterno);
							g2.fillRect(0, 0, getWidth(), getHeight());
						}
						
						if (filaJugador != -1 && row == filaJugador) {
							g2.setColor(new Color(colorAcento.getRed(), colorAcento.getGreen(), 
									colorAcento.getBlue(), 100));
							g2.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 8, 8);
						}
						
						g2.dispose();
						super.paintComponent(g);
					}
				};
				
				result.setOpaque(false);
				result.setHorizontalAlignment(SwingConstants.CENTER);
				
				if (filaJugador != -1 && row == filaJugador) {
					result.setForeground(colorTextoSeleccion);
				} else {
					result.setForeground(colorTextoTabla);
				}
				
				if (isSelected) {
					result.setForeground(colorTextoSeleccion);
				}
				
				return result;
			}
		};
		
		this.tablaJugadores.setDefaultRenderer(Object.class, miCellRenderer);
		
		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(this.modeloDatosJugador);
		this.tablaJugadores.setRowSorter(sorter);
		
		//Tratar los numeros de los jugadores como int y no como String
		sorter.setComparator(4, (o1, o2) ->{
			try {
		        Integer num1 = Integer.parseInt(o1.toString());
		        Integer num2 = Integer.parseInt(o2.toString());
		        return num1.compareTo(num2);
		    } catch (NumberFormatException e) {
		        return o1.toString().compareTo(o2.toString());
		    }
		});
		//Ordenar la edad como un int no String
		sorter.setComparator(3, (o1, o2) ->{
			try {
		        Integer edad1 = Integer.parseInt(o1.toString());
		        Integer edad2 = Integer.parseInt(o2.toString());
		        return edad1.compareTo(edad2);
		    } catch (NumberFormatException e) {
		        return o1.toString().compareTo(o2.toString());
		    }
		});
		//Ordenar posicion por el Enum
		sorter.setComparator(0, (o1, o2) ->{
			try {
		        TipoPosicion pos1 = TipoPosicion.valueOf(o1.toString());
		        TipoPosicion pos2 = TipoPosicion.valueOf(o2.toString());
		        return Integer.compare(pos1.ordinal(), pos2.ordinal());
		    } catch (IllegalArgumentException e) {
		        return o1.toString().compareTo(o2.toString());
		    }
		});;
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
					}
				}
			}
		});
		
		MouseMotionListener miMouseMotionListener = new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				Point puntoRaton = new Point(e.getX(), e.getY());
				int nuevaFila = tablaJugadores.rowAtPoint(puntoRaton);
				
				if (nuevaFila != filaJugador) {
					filaJugador = nuevaFila;
					tablaJugadores.repaint();
				}
				
		        if (filaJugador != -1) {
		            tablaJugadores.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		        } else {
		            tablaJugadores.setCursor(Cursor.getDefaultCursor());
		        }
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
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
		panel.setLayout(new GridLayout(6, 2, 10, 15));
		
		String[] informacion= {
				"Nombre:",
				"Ciudad:",
				"Liga:",
				"Estadio:",
				"Año fundación:",
				"Títulos:",
		};
		String[] valores = extraerInformacion(e);
		
		for (int i=0; i<informacion.length;i++) {
			JLabel labelinfo=new JLabel(informacion[i]);
			labelinfo.setForeground(colorTextoInfo);
			labelinfo.setFont(new Font("Arial",Font.BOLD, 13));
			labelinfo.setHorizontalAlignment(JLabel.RIGHT);
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

	//Devolver jugador seleccionado
	public Jugador conseguirJugador (int row) {
	    Jugador resultado = null;
	    
	    if (row < 0) {
	        System.err.println("No hay fila seleccionada");
	        return resultado;
	    }
	    
	    // Convertir el índice de la vista al índice del modelo
	    int modelRow = tablaJugadores.convertRowIndexToModel(row);
	    
	    String posicionString = (String) modeloDatosJugador.getValueAt(modelRow, 0);
	    TipoPosicion posicion = TipoPosicion.valueOf(posicionString);
	    String nomJugadorBuscar = (String) modeloDatosJugador.getValueAt(modelRow, 1);
	    
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
	 
	 // Panel animado para el escudo
	 //IAG la animacion del escudo
	 private class PanelEscudoAnimado extends JPanel {
		 private ImageIcon escudo;
		 private float animacion = 0f;
		 private Timer timer;
		 private Color colorBrillo;
		 
		 public PanelEscudoAnimado(ImageIcon escudoOriginal, Color acento) {
			 this.colorBrillo = acento;
			 setOpaque(false);
			 
			 // Escalar escudo
			 int ANCHO_MAXIMO = 120;
			 if (escudoOriginal != null && escudoOriginal.getIconWidth() > ANCHO_MAXIMO) {
				 int altoNuevo = (int) ((double) ANCHO_MAXIMO / escudoOriginal.getIconWidth() * escudoOriginal.getIconHeight());
				 Image imagen = escudoOriginal.getImage();
				 Image imagenEscalada = imagen.getScaledInstance(ANCHO_MAXIMO, altoNuevo, Image.SCALE_SMOOTH);
				 this.escudo = new ImageIcon(imagenEscalada);
			 } else {
				 this.escudo = escudoOriginal;
			 }
			 
			 // Animación de entrada
			 timer = new Timer(20, e -> {
				 animacion += 0.05f;
				 if (animacion >= 1f) {
					 animacion = 1f;
					 timer.stop();
				 }
				 repaint();
			 });
			 timer.start();
		 }
		 
		 @Override
		 protected void paintComponent(Graphics g) {
			 super.paintComponent(g);
			 if (escudo == null) return;
			 
			 Graphics2D g2 = (Graphics2D) g.create();
			 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			 g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			 
			 int w = getWidth();
			 int h = getHeight();
			 int escudoW = escudo.getIconWidth();
			 int escudoH = escudo.getIconHeight();
			 int x = (w - escudoW) / 2;
			 int y = (h - escudoH) / 2;
			 
			 // Efecto de brillo circular de fondo
			 int radio = (int)(Math.max(escudoW, escudoH) * 0.7);
			 for (int i = 3; i > 0; i--) {
				 int alpha = (int)(30 * animacion / i);
				 g2.setColor(new Color(colorBrillo.getRed(), colorBrillo.getGreen(), 
						 colorBrillo.getBlue(), alpha));
				 int offset = i * 15;
				 g2.fillOval(x + escudoW/2 - radio - offset, y + escudoH/2 - radio - offset, 
						 (radio + offset) * 2, (radio + offset) * 2);
			 }
			 
			 // Dibujar escudo con animación de escala
			 float escala = 0.5f + (animacion * 0.5f);
			 int escudoAnimW = (int)(escudoW * escala);
			 int escudoAnimH = (int)(escudoH * escala);
			 int xAnim = x + (escudoW - escudoAnimW) / 2;
			 int yAnim = y + (escudoH - escudoAnimH) / 2;
			 
			 g2.drawImage(escudo.getImage(), xAnim, yAnim, escudoAnimW, escudoAnimH, this);
			 
			 g2.dispose();
		 }
	 }
	 
	 // Panel de información con efecto de tarjeta
	 private class PanelInformacionAnimado extends JPanel {
		 private Color colorFondo;
		 private Color colorBorde;
		 
		 public PanelInformacionAnimado(Color fondo, Color borde) {
			 this.colorFondo = fondo;
			 this.colorBorde = borde;
			 setOpaque(false);
		 }
		 
		 @Override
		 protected void paintComponent(Graphics g) {
			 Graphics2D g2 = (Graphics2D) g.create();
			 g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			 
			 int w = getWidth();
			 int h = getHeight();
			 
			 
			 
			 // Fondo con gradiente sutil
			 GradientPaint gp = new GradientPaint(0, 0, colorFondo, 
					 0, h, new Color(
							 Math.max(0, colorFondo.getRed() - 10),
							 Math.max(0, colorFondo.getGreen() - 10),
							 Math.max(0, colorFondo.getBlue() - 10)
					 ));
			 g2.setPaint(gp);
			 g2.fillRoundRect(0, 0, w, h, 20, 20);
			 
			 // Borde con color de acento
			 g2.setColor(new Color(colorBorde.getRed(), colorBorde.getGreen(), 
					 colorBorde.getBlue(), 100));
			 g2.drawRoundRect(1, 1, w - 2, h - 2, 20, 20);
			 
			 // Línea de brillo superior
			 g2.setColor(new Color(255, 255, 255, 30));
			 g2.drawRoundRect(3, 3, w - 6, h / 3, 18, 18);
			 
			 g2.dispose();
			 super.paintComponent(g);
		 }
		 
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
		