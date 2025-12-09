package gui;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import domain.Equipo;
import domain.Jugador;
import domain.Jugador.TipoPosicion;
import domain.Liga;

public class JFrameComparar extends JFramePadre{

	private static final long serialVersionUID = 1L;
	private List<Jugador> baseDatosJugadores;
	private PanelJugador[] paneles = new PanelJugador[4];
	//private TipoPosicion posicionBloqueada = null;
	public JFrameComparar(JFramePadre ventanaAnterior,Jugador jugadorinicial) {
		super();
        super.framePrevio = ventanaAnterior;
        cargarTodosLosJugadores(JFramePadre.ligas);
        this.panel.setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBounds(70, 10, 920, 540);
        mainPanel.setOpaque(false);
        JLabel lblTitulo = new JLabel("COMPARADOR DE JUGADORES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        mainPanel.add(lblTitulo, BorderLayout.NORTH);
        JPanel panelJugadores = new JPanel(new GridLayout(1, 4));
        panelJugadores.setOpaque(false);
        for (int i = 0; i < 4; i++) {
        	paneles[i] = new PanelJugador(i);
        	panelJugadores.add(paneles[i]);
        }

        mainPanel.add(panelJugadores, BorderLayout.CENTER);
        this.panel.add(mainPanel);
        this.add(panel);
        paneles[0].asignarJugador(jugadorinicial);
        usoBotonAtras(ventanaAnterior);
        this.panel.revalidate(); 
        this.panel.repaint();

	}
	private void cargarTodosLosJugadores(ArrayList<Liga> ligas) {
        this.baseDatosJugadores = new ArrayList<>();
           for (Liga liga : ligas) {
               if (liga.getEquipos() != null) {
                   for (Equipo equipo : liga.getEquipos()) {
                       HashMap<TipoPosicion, ArrayList<Jugador>> mapa = equipo.getJugadores();
                          for (ArrayList<Jugador> listaPosicion : mapa.values()) {
                              this.baseDatosJugadores.addAll(listaPosicion);
                            
                        }
                    }
                }
            }
        }
	
	 private class PanelJugador extends JPanel {
	        
	        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
			private int idSlot;
	        private Jugador jugadorActual;
	        private JLabel lblEscudoEquipo;
	        private JLabel lblNombre,lblEquipo ,lblPrecio;
	        private JTable tablaStats;
	        private ModeloComparador modeloTabla;
	        private JButton btnAccion;
	        private JLabel lblFoto;
	        public PanelJugador(int id) {
	            this.idSlot = id;
	            setLayout(new BorderLayout());
	            setBackground(new Color(245, 245, 245));
	            setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
	            JPanel panelSuperior = new JPanel(new BorderLayout(0, 5));
	            panelSuperior.setOpaque(false);
	            panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

	            lblFoto = new JLabel();
	            lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
	            lblFoto.setPreferredSize(new Dimension(180, 180));
	            panelSuperior.add(lblFoto, BorderLayout.NORTH);
	            
	            JPanel panelDatos = new JPanel(new GridLayout(3, 1, 0, 5));
	            panelDatos.setOpaque(false);
	            
	            lblNombre = new JLabel("Vacío", SwingConstants.CENTER);
	            lblNombre.setFont(new Font("SansSerif", Font.BOLD, 14));
	            
	           
	            JPanel panelEquipo = new JPanel(new BorderLayout(5, 0));
	            panelEquipo.setOpaque(false);
	            
	            lblEscudoEquipo = new JLabel();
	            lblEscudoEquipo.setHorizontalAlignment(SwingConstants.RIGHT);
	            lblEscudoEquipo.setPreferredSize(new Dimension(25, 25));
	            
	            lblEquipo = new JLabel("-", SwingConstants.LEFT);
	            lblEquipo.setFont(new Font("SansSerif", Font.PLAIN, 12));
	            
	            panelEquipo.add(lblEscudoEquipo, BorderLayout.WEST);
	            panelEquipo.add(lblEquipo, BorderLayout.CENTER);
	            
	            lblPrecio = new JLabel("-", SwingConstants.CENTER);
	            lblPrecio.setForeground(new Color(0, 120, 0)); 
	            lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 13));

	            panelDatos.add(lblNombre);
	            panelDatos.add(panelEquipo);
	            panelDatos.add(lblPrecio);
	            
	            panelSuperior.add(panelDatos, BorderLayout.CENTER);
	            add(panelSuperior, BorderLayout.NORTH);

	            modeloTabla = new ModeloComparador();
	            tablaStats = new JTable(modeloTabla);
	            tablaStats.setRowHeight(22);
	            tablaStats.setEnabled(false);
	            tablaStats.setFillsViewportHeight(true);
	            tablaStats.setFont(new Font("SansSerif", Font.PLAIN, 11));
	            
	            tablaStats.getTableHeader().setBackground(new Color(40, 40, 40));
	            tablaStats.getTableHeader().setForeground(Color.WHITE);
	            tablaStats.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
	            
	            JScrollPane scrollPane = new JScrollPane(tablaStats);
	            scrollPane.setBorder(null);
	            add(scrollPane, BorderLayout.CENTER);
	            
	            btnAccion = new JButton("Seleccionar");
	            btnAccion.setFont(new Font("SansSerif", Font.BOLD, 12));
	            btnAccion.setBackground(new Color(220, 220, 220));
	            btnAccion.setCursor(new Cursor(Cursor.HAND_CURSOR));
	            btnAccion.setPreferredSize(new Dimension(0, 35));
	            
	            btnAccion.addActionListener(e -> {
	                if (jugadorActual == null) {
	                    abrirBuscador();
	                } else {
	                    quitarJugador();
	                }
	            });
	            add(btnAccion, BorderLayout.SOUTH);
	        }
	        //IAG
	        private void abrirBuscador() {
	            // 1. Detectar si hay restricciones mirando los OTROS paneles
	            TipoPosicion restriccionEncontrada = null;
	            List<String> ocupados = new ArrayList<>();

	            for (PanelJugador p : paneles) {
	                if (p.jugadorActual != null && p.jugadorActual.getPosicion() != null) {
	                    ocupados.add(p.jugadorActual.getNombre());
	                    // Si encontramos un jugador, COPIAMOS su posición como restricción
	                    if (restriccionEncontrada == null) {
	                        restriccionEncontrada = p.jugadorActual.getPosicion();
	                    }
	                }
	            }

	            // Variable final para poder usarla dentro del filtro (Lambda)
	            final TipoPosicion filtroPosicion = restriccionEncontrada;

	            // 2. Filtrar candidatos
	            List<Jugador> candidatos = baseDatosJugadores.stream()
	                .filter(j -> !ocupados.contains(j.getNombre())) 
	                .filter(j -> filtroPosicion == null || j.getPosicion().equals(filtroPosicion)) 
	                .collect(Collectors.toList());

	            if (candidatos.isEmpty()) {
	                JOptionPane.showMessageDialog(
	                    this,
	                    "No hay más jugadores disponibles " + 
	                    (filtroPosicion != null ? "para la posición " + filtroPosicion : "") + ".",
	                    "Sin candidatos",
	                    JOptionPane.WARNING_MESSAGE
	                );
	                return;
	            }

	            // 3. Crear opciones para el popup
	            Object[] opciones = candidatos.stream()
	                .map(j -> j.getNombre() + " (" + j.getEquipo().getNombre() + " - " + j.getPosicion() + ")")
	                .toArray();

	            // 4. Mostrar Popup
	            String seleccion = (String) JOptionPane.showInputDialog(
	                this, "Selecciona jugador:", "Slot " + (idSlot + 1),
	                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones.length > 0 ? opciones[0] : null
	            );

	            // 5. Asignar si eligió algo
	            if (seleccion != null) {
	                int indice = -1;
	                for(int i=0; i<opciones.length; i++) {
	                    if(opciones[i].equals(seleccion)) indice = i;
	                }
	                if(indice != -1) asignarJugador(candidatos.get(indice));
	            }
	        }
	        private void asignarJugador(Jugador j) {
	            this.jugadorActual = j;
	            
	        
 
	            String rutaFoto = "resources/images/logos/villalibre.png";
	            ImageIcon iconoOriginal = new ImageIcon(rutaFoto);
	            iconoOriginal = new ImageIcon(rutaFoto);
	            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
	            lblFoto.setIcon(new ImageIcon(imagenEscalada));
	            String nombreLiga = j.getEquipo().getLiga().getNombre().toLowerCase();
	            String nombreEquipo = j.getEquipo().getNombrePNGEquipo().toLowerCase();
	            String rutaEscudo = "resources/images/equipos/" + nombreLiga + "/" + nombreEquipo + ".png";
	            ImageIcon iconoEscudo = new ImageIcon(rutaEscudo);
	            Image escudoEscalado = iconoEscudo.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
	            lblEscudoEquipo.setIcon(new ImageIcon(escudoEscalado));
	            lblNombre.setText(j.getNombre());
	            lblEquipo.setText(j.getEquipo().getNombre());
	            lblPrecio.setText(String.format("%,.0f mill. €", j.getValorMercado()));
	            btnAccion.setText("X");
	            btnAccion.setBackground(new Color(255, 100, 100));
	            modeloTabla.cargarEstadisticas(j);
	        }

	        private void quitarJugador() {
	            this.jugadorActual = null;
	            
	            // Resetea el panel
	            lblEscudoEquipo.setIcon(null);
	            lblFoto.setIcon(null);
	            lblNombre.setText("Vacío");
	            lblEquipo.setText("-");
	            lblPrecio.setText("-");
	            btnAccion.setText("Seleccionar");
	            btnAccion.setBackground(new Color(220, 220, 220));
	            modeloTabla.setRowCount(0);

	        }
	    }
	private class ModeloComparador extends DefaultTableModel {
		
        public ModeloComparador() {
            super(new String[]{"Dato", "Valor"}, 0);
        }

        public void cargarEstadisticas(Jugador j) {
            this.setRowCount(0); 
            
            addRow(new Object[]{"Edad", j.getEdad()});
            addRow(new Object[]{"Altura", j.getAltura()});
            addRow(new Object[]{"Partidos", j.getPartidosJugados()});
            
            
            if (j.getPosicion()==TipoPosicion.PORTERO) {
            	
                addRow(new Object[]{"Porterías a 0", j.getPorteriasaCero()});
                addRow(new Object[]{"Paradas", j.getParadas()});
                addRow(new Object[]{"Goles Encajados", j.getGolesEncajados()});
            } else if (j.getPosicion()==TipoPosicion.CENTROCAMPISTA||j.getPosicion()==TipoPosicion.DELANTERO){
            	addRow(new Object[]{"Regates", j.getRegates()});
                addRow(new Object[]{"Goles", j.getGoles()});
                addRow(new Object[]{"Asistencias", j.getAsistencias()});
                int ga = j.getGoles() + j.getAsistencias();
                addRow(new Object[]{"G/A", ga});
            } else {
            	addRow(new Object[]{"Porterías a 0", j.getPorteriasaCero()});
            	addRow(new Object[]{"Regates", j.getRegates()});
                addRow(new Object[]{"Goles", j.getGoles()});
                addRow(new Object[]{"Asistencias", j.getAsistencias()});
                int ga = j.getGoles() + j.getAsistencias();
                addRow(new Object[]{"G/A", ga});
            }
        }

        @Override 
        public boolean isCellEditable(int row, int column) { 
        	return false; 
        	}
    }

	    }
