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
import javax.swing.table.DefaultTableCellRenderer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
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
    
    public JFrameComparar(JFramePadre ventanaAnterior, Jugador jugadorinicial) {
        super();
        super.framePrevio = ventanaAnterior;
        cargarTodosLosJugadores(JFramePadre.ligas);
        this.panel.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setOpaque(false);
        

        JPanel panelTitulo = new JPanel();
        panelTitulo.setOpaque(false);
        JLabel lblTitulo = new JLabel("COMPARADOR DE JUGADORES", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        
        mainPanel.add(panelTitulo, BorderLayout.NORTH);
        

        JPanel panelJugadores = new JPanel(new GridLayout(1, 4, 10, 0));
        panelJugadores.setOpaque(false);
        
        for (int i = 0; i < 4; i++) {
            paneles[i] = new PanelJugador(i);
            panelJugadores.add(paneles[i]);
        }

        mainPanel.add(panelJugadores, BorderLayout.CENTER);
        this.panel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20)); 
        this.panel.add(mainPanel, BorderLayout.CENTER);
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
        
        private static final long serialVersionUID = 1L;
        private int idSlot;
        private Jugador jugadorActual;
        private JLabel lblEscudoEquipo;
        private JLabel lblNombre, lblEquipo, lblPosicion, lblPrecio;
        private JTable tablaStats;
        private ModeloComparador modeloTabla;
        private JButton btnAccion;
        private JLabel lblFoto;
        
        public PanelJugador(int id) {
            this.idSlot = id;
            setLayout(new BorderLayout(0, 0));
            setOpaque(false);
            
     
            JPanel contenedor = new JPanel(new BorderLayout(0, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Fondo degradado
                    GradientPaint gp = new GradientPaint(0, 0, new Color(255, 255, 255), 
                                                        0, getHeight(), new Color(240, 242, 245));
                    g2d.setPaint(gp);
                    g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                    
                   
                }
            };
            contenedor.setOpaque(false);
            add(contenedor, BorderLayout.CENTER);
            
            // Panel superior con foto y datos
            JPanel panelSuperior = new JPanel(new BorderLayout(0, 8));
            panelSuperior.setOpaque(false);
            panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

            
            lblFoto = new JLabel();
            lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
            
            panelSuperior.add(lblFoto, BorderLayout.NORTH);
            
            // Panel de datos
            JPanel panelDatos = new JPanel(new GridLayout(4, 1, 0, 3));
            panelDatos.setOpaque(false);
            
            lblNombre = new JLabel("Vacío", SwingConstants.CENTER);
            lblNombre.setFont(new Font("SansSerif", Font.BOLD, 15));
            lblNombre.setForeground(new Color(33, 37, 41));
            
            lblPosicion = new JLabel("-", SwingConstants.CENTER);
            lblPosicion.setFont(new Font("SansSerif", Font.ITALIC, 11));
            lblPosicion.setForeground(new Color(108, 117, 125));
            
            JPanel panelEquipo = new JPanel(new BorderLayout(5, 0));
            panelEquipo.setOpaque(false);
            
            lblEscudoEquipo = new JLabel();
            lblEscudoEquipo.setHorizontalAlignment(SwingConstants.RIGHT);
            lblEscudoEquipo.setPreferredSize(new Dimension(25, 25));
            
            lblEquipo = new JLabel("-", SwingConstants.LEFT);
            lblEquipo.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lblEquipo.setForeground(new Color(73, 80, 87));
            
            panelEquipo.add(lblEscudoEquipo, BorderLayout.WEST);
            panelEquipo.add(lblEquipo, BorderLayout.CENTER);
            
            lblPrecio = new JLabel("-", SwingConstants.CENTER);
            lblPrecio.setForeground(new Color(40, 167, 69));
            lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 14));

            panelDatos.add(lblNombre);
            panelDatos.add(lblPosicion);
            panelDatos.add(panelEquipo);
            panelDatos.add(lblPrecio);
            
            panelSuperior.add(panelDatos, BorderLayout.CENTER);
            contenedor.add(panelSuperior, BorderLayout.NORTH);

            modeloTabla = new ModeloComparador();
            tablaStats = new JTable(modeloTabla);
            tablaStats.setRowHeight(28);
            tablaStats.setEnabled(false);
            tablaStats.setFillsViewportHeight(true);
            tablaStats.setFont(new Font("SansSerif", Font.PLAIN, 12));
            tablaStats.setGridColor(new Color(230, 230, 230));
            tablaStats.setShowVerticalLines(false);
            
            tablaStats.getTableHeader().setBackground(new Color(52, 58, 64));
            tablaStats.getTableHeader().setForeground(Color.WHITE);
            tablaStats.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
            tablaStats.getTableHeader().setPreferredSize(new Dimension(0, 30));
            
         
            DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                @Override
                public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (row % 2 == 0) {
                        c.setBackground(new Color(248, 249, 250));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                    if (column == 0) {
                        setFont(new Font("SansSerif", Font.BOLD, 11));
                        setForeground(new Color(73, 80, 87));
                    } else {
                        setFont(new Font("SansSerif", Font.PLAIN, 12));
                        setForeground(new Color(33, 37, 41));
                    }
                    return c;
                }
            };
            
            for (int i = 0; i < tablaStats.getColumnCount(); i++) {
                tablaStats.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }
            
            JScrollPane scrollPane = new JScrollPane(tablaStats);
            scrollPane.setBorder(null);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            contenedor.add(scrollPane, BorderLayout.CENTER);
            
            btnAccion = new JButton("+ Seleccionar Jugador") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    if (getModel().isPressed()) {
                        g2d.setColor(getBackground().darker());
                    } else if (getModel().isRollover()) {
                        g2d.setColor(getBackground().brighter());
                    } else {
                        g2d.setColor(getBackground());
                    }
                    
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    super.paintComponent(g);
                }
            };
            btnAccion.setFont(new Font("SansSerif", Font.BOLD, 13));
            btnAccion.setForeground(Color.WHITE);
            btnAccion.setBackground(new Color(0, 123, 255));
            btnAccion.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnAccion.setPreferredSize(new Dimension(0, 40));
            btnAccion.setBorderPainted(false);
            btnAccion.setFocusPainted(false);
            btnAccion.setContentAreaFilled(false);
            btnAccion.setOpaque(false);
            
            btnAccion.addActionListener(e -> {
                if (jugadorActual == null) {
                    abrirBuscador();
                } else {
                    quitarJugador();
                }
            });
            
            JPanel panelBoton = new JPanel(new BorderLayout());
            panelBoton.setOpaque(false);
            panelBoton.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
            panelBoton.add(btnAccion, BorderLayout.CENTER);
            
            contenedor.add(panelBoton, BorderLayout.SOUTH);
        }
        
        private void abrirBuscador() {
            TipoPosicion restriccionEncontrada = null;
            List<String> ocupados = new ArrayList<>();

            for (PanelJugador p : paneles) {
                if (p.jugadorActual != null && p.jugadorActual.getPosicion() != null) {
                    ocupados.add(p.jugadorActual.getNombre());
                    if (restriccionEncontrada == null) {
                        restriccionEncontrada = p.jugadorActual.getPosicion();
                    }
                }
            }

            final TipoPosicion filtroPosicion = restriccionEncontrada;

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

            Object[] opciones = candidatos.stream()
                .map(j -> j.getNombre() + " (" + j.getEquipo().getNombre() + " - " + j.getPosicion() + ")")
                .toArray();

            String seleccion = (String) JOptionPane.showInputDialog(
                this, "Selecciona jugador:", "Slot " + (idSlot + 1),
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones.length > 0 ? opciones[0] : null
            );

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
            
            String rutaFoto = "resources/images/logos/jugador.png";
            ImageIcon iconoOriginal = new ImageIcon(rutaFoto);
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(imagenEscalada));
            
            String nombreLiga = j.getEquipo().getLiga().getNombre().toLowerCase();
            String nombreEquipo = j.getEquipo().getNombrePNGEquipo().toLowerCase();
            String rutaEscudo = "resources/images/equipos/" + nombreLiga + "/" + nombreEquipo + ".png";
            ImageIcon iconoEscudo = new ImageIcon(rutaEscudo);
            Image escudoEscalado = iconoEscudo.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            lblEscudoEquipo.setIcon(new ImageIcon(escudoEscalado));
            
            lblNombre.setText(j.getNombre());
            lblPosicion.setText(j.getPosicion().toString());
            lblEquipo.setText(j.getEquipo().getNombre());
            lblPrecio.setText(String.format("%,.0f mill. €", j.getValorMercado()));
            
            btnAccion.setText("Quitar Jugador");
            btnAccion.setBackground(new Color(220, 53, 69));
            
            modeloTabla.cargarEstadisticas(j);
        }

        private void quitarJugador() {
            this.jugadorActual = null;
            
            lblEscudoEquipo.setIcon(null);
            lblFoto.setIcon(null);
            lblNombre.setText("Vacío");
            lblPosicion.setText("-");
            lblEquipo.setText("-");
            lblPrecio.setText("-");
            btnAccion.setText("+ Seleccionar Jugador");
            btnAccion.setBackground(new Color(0, 123, 255));
            modeloTabla.setRowCount(0);
        }
    }
    
    private class ModeloComparador extends DefaultTableModel {
        
        public ModeloComparador() {
            super(new String[]{"Estadística", "Valor"}, 0);
        }

        public void cargarEstadisticas(Jugador j) {
            this.setRowCount(0); 
            
            addRow(new Object[]{"Edad", j.getEdad() + " años"});
            addRow(new Object[]{"Altura", j.getAltura() + " cm"});
            addRow(new Object[]{"Partidos", j.getPartidosJugados()});
            
            if (j.getPosicion() == TipoPosicion.PORTERO) {
                addRow(new Object[]{"Porterías a 0", j.getPorteriasaCero()});
                addRow(new Object[]{"Paradas", j.getParadas()});
                addRow(new Object[]{"⚠Goles Encajados", j.getGolesEncajados()});
            } else if (j.getPosicion() == TipoPosicion.CENTROCAMPISTA || j.getPosicion() == TipoPosicion.DELANTERO) {
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