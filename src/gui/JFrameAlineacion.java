
package gui;

	import java.awt.BasicStroke;
	import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
	import java.awt.Font;
	import java.awt.Graphics;
	import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
	import java.awt.event.MouseAdapter;
	import java.awt.event.MouseEvent;
	import java.util.ArrayList;
	import java.util.Collections;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
	import javax.swing.JOptionPane;
	import javax.swing.JPanel;
	import javax.swing.SwingConstants;

	import domain.Equipo;
	import domain.Jugador;
	
	import domain.Jugador.TipoPosicion; 

	public class JFrameAlineacion extends JFramePadre {

	    private static final long serialVersionUID = 1L;
	    private Equipo equipo;
	    private JPanel panelCampo;
	    
	    private Map<Integer, PanelPosicion> mapaPosiciones;
	    private HashMap<TipoPosicion, ArrayList<Jugador>> plantilla;

	    public JFrameAlineacion(JFramePadre frameAnterior, Equipo equipo) {
	        super();
	        this.framePrevio = frameAnterior;
	        this.equipo = equipo;
	        this.plantilla =  equipo.getJugadores(); 
	        this.mapaPosiciones = new HashMap<>();

	        usoBotonAtras(frameAnterior);


	        JPanel panelPrincipal = super.panel;
	        panelPrincipal.setLayout(null);
	        setImagenDeFondo(null); 
	        panelPrincipal.setBackground(new Color(152, 217, 194));

	        JLabel lblTitulo = new JLabel("ALINEACIÓN 4-3-3: " + equipo.getNombre().toUpperCase());
	        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
	        lblTitulo.setForeground(Color.WHITE);
	        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
	        lblTitulo.setBounds(0, 10, 1000, 40);
	        panelPrincipal.add(lblTitulo);

	        botonAtras.setBounds(20, 20, 60, 50);
	        panelPrincipal.add(botonAtras);

	        panelCampo = new PanelCampo();
	        panelCampo.setBounds(100, 70, 800, 500);
	        panelCampo.setLayout(null);
	        panelPrincipal.add(panelCampo);

	        inicializarPosiciones();

	        rellenarAlineacionAuto();

	        this.setContentPane(panelPrincipal);
	        
	        //Boton de Reroll
	        ImageIcon rr = new ImageIcon("resources/images/logos/reroll.png");
	        Image rr2 = rr.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
	        ImageIcon rr3 = new ImageIcon(rr2);
	        JLabel botonReRoll = new JLabel(rr3);
	        botonReRoll.addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseClicked(MouseEvent e) {
	                rellenarAlineacionAuto();
	            }
	        });
	        botonReRoll.setBounds(20, 100, 50, 50);
	        botonReRoll.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	        panelPrincipal.add(botonReRoll);
	    }

	    private void inicializarPosiciones() {
	        crearPosicion(0, TipoPosicion.PORTERO, 350, 400);

	        
	        int yDef = 300;
	        crearPosicion(1, TipoPosicion.DEFENSA, 50, yDef);  // LI
	        crearPosicion(2, TipoPosicion.DEFENSA, 250, yDef); // DFC
	        crearPosicion(3, TipoPosicion.DEFENSA, 450, yDef); // DFC
	        crearPosicion(4, TipoPosicion.DEFENSA, 650, yDef); // LD

	     
	        int yMed = 180;
	        crearPosicion(5, TipoPosicion.CENTROCAMPISTA, 150, yMed);//MC
	        crearPosicion(6, TipoPosicion.CENTROCAMPISTA, 350, yMed);//MCD
	        crearPosicion(7, TipoPosicion.CENTROCAMPISTA, 550, yMed);//MC

	        int yDel = 40;
	        crearPosicion(8, TipoPosicion.DELANTERO, 100, yDel);//EI
	        crearPosicion(9, TipoPosicion.DELANTERO, 350, 20); // Punta 
	        crearPosicion(10, TipoPosicion.DELANTERO, 600, yDel);// ED
	    }

	    private void crearPosicion(int id, TipoPosicion tipo, int x, int y) {
	        PanelPosicion ficha = new PanelPosicion(tipo);
	        ficha.setBounds(x, y, 100, 90); 
	        mapaPosiciones.put(id, ficha);
	        panelCampo.add(ficha);
	    }

	    private void rellenarAlineacionAuto() {
	      
	        List<Jugador> porteros = CopiaJugadores(TipoPosicion.PORTERO);
	        List<Jugador> defensas = CopiaJugadores(TipoPosicion.DEFENSA);
	        List<Jugador> medios = CopiaJugadores(TipoPosicion.CENTROCAMPISTA);
	        List<Jugador> delanteros = CopiaJugadores(TipoPosicion.DELANTERO);

	        Collections.shuffle(porteros);
	        Collections.shuffle(defensas);
	        Collections.shuffle(medios);
	        Collections.shuffle(delanteros);

	        if (!porteros.isEmpty()) mapaPosiciones.get(0).setJugador(porteros.get(0));

	        for (int i = 0; i < 4; i++) {
	            if (i < defensas.size()) mapaPosiciones.get(i + 1).setJugador(defensas.get(i));
	        }

	        for (int i = 0; i < 3; i++) {
	            if (i < medios.size()) mapaPosiciones.get(i + 5).setJugador(medios.get(i));
	        }

	        for (int i = 0; i < 3; i++) {
	            if (i < delanteros.size()) mapaPosiciones.get(i + 8).setJugador(delanteros.get(i));
	        }
	        
	        repaint();
	    }
	    private ArrayList<Jugador> CopiaJugadores(TipoPosicion tipo) {
	        ArrayList<Jugador> originales = plantilla.get(tipo);
	        if (originales == null) {
	            return new ArrayList<>(); 
	        }
	        return new ArrayList<>(originales);
	    }

	    private class PanelCampo extends JPanel {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            Graphics2D g2 = (Graphics2D) g;
	            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	            // Césped
	            g2.setColor(new Color(34, 139, 34)); 
	            g2.fillRect(0, 0, getWidth(), getHeight());

	            // Líneas Blancas
	            g2.setColor(new Color(255, 255, 255, 180)); 
	            g2.setStroke(new BasicStroke(3));

	            // Bordes y centro
	            g2.drawRect(10, 10, getWidth() - 20, getHeight() - 20);
	            g2.drawLine(10, getHeight() / 2, getWidth() - 10, getHeight() / 2); 
	            g2.drawOval((getWidth() - 100) / 2, (getHeight() - 100) / 2, 100, 100); 
	            
	            // Áreas
	            g2.drawRect((getWidth()-200)/2, 10, 200, 60);
	            g2.drawRect((getWidth()-200)/2, getHeight()-70, 200, 60);
	        }
	    }

	    private class PanelPosicion extends JPanel {
	        private Jugador jugador;
	        private TipoPosicion tipoRequerido;
	        private JLabel lblDorsal, lblNombre;

	        public PanelPosicion(TipoPosicion tipo) {
	            this.tipoRequerido = tipo;
	            this.setOpaque(false);
	            this.setLayout(null);
	            this.setCursor(new Cursor(Cursor.HAND_CURSOR));

	            lblDorsal = new JLabel("?");
	            lblDorsal.setFont(new Font("SansSerif", Font.BOLD, 24));
	            lblDorsal.setForeground(Color.WHITE);
	            lblDorsal.setHorizontalAlignment(SwingConstants.CENTER);
	            lblDorsal.setBounds(0, 15, 100, 30);
	            add(lblDorsal);

	            lblNombre = new JLabel("VACÍO");
	            lblNombre.setFont(new Font("SansSerif", Font.BOLD, 11));
	            lblNombre.setForeground(Color.WHITE);
	            lblNombre.setHorizontalAlignment(SwingConstants.CENTER);
	            lblNombre.setBounds(0, 50, 100, 20);
	            add(lblNombre);

	            addMouseListener(new MouseAdapter() {
	                @Override
	                public void mouseClicked(MouseEvent e) {
	                    gestionarClick();
	                }
	            });
	        }

	        public void setJugador(Jugador j) {
	            this.jugador = j;
	            if (j != null) {
	                lblDorsal.setText(String.valueOf(j.getNumeroCamiseta()));
	                lblNombre.setText(j.getNombre());
	                lblDorsal.setForeground(new Color(50, 255, 255));
	            } else {
	                lblDorsal.setText("+");
	                lblNombre.setText(tipoRequerido.toString());
	                lblDorsal.setForeground(Color.LIGHT_GRAY);
	            }
	            repaint();
	        }
	        
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            Graphics2D g2 = (Graphics2D) g;
	            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	            if (jugador != null) {
	                g2.setColor(new Color(0, 0, 0, 150));
	                g2.fillOval(10, 10, 80, 80);
	                g2.setColor(Color.WHITE);
	                g2.setStroke(new BasicStroke(2));
	                g2.drawOval(10, 10, 80, 80);
	            } else {
	                g2.setColor(new Color(255, 255, 255, 50));
	                g2.fillOval(10, 10, 80, 80);
	            }
	        }

	        private void gestionarClick() {
	            if (jugador != null) {

	                int opt = JOptionPane.showConfirmDialog(JFrameAlineacion.this, 
	                    "¿Quitar a " + jugador.getNombre() + "?", "Alineación", JOptionPane.YES_NO_OPTION);
	                if (opt == JOptionPane.YES_OPTION) {
	                    setJugador(null);
	                }
	            } else {
	                abrirSelector();
	            }
	        }

	        private void abrirSelector() {
	            List<Jugador> candidatos = new ArrayList<>();
	            ArrayList<Jugador> disponibles = plantilla.get(tipoRequerido);
	            if (disponibles != null) {
	            	for (Jugador j : disponibles) {
		                if (j.getPosicion() == tipoRequerido) {
		                	boolean yaJuega = false;
		                    for (PanelPosicion p : mapaPosiciones.values()) {
		                        if (p.jugador != null && p.jugador.equals(j)) {
		                            yaJuega = true;
		                            break;
		                        }
		                    }
		                    if (!yaJuega) candidatos.add(j);
		                }
		            }
	            
	            

	            if (candidatos.isEmpty()) {
	                JOptionPane.showMessageDialog(JFrameAlineacion.this, "No quedan más jugadores disponibles para esta posición.");
	                return;
	            }

	            String[] nombres = new String[candidatos.size()];
	            
	            for (int i = 0; i < candidatos.size(); i++) {
	                nombres[i] = candidatos.get(i).getNombre();
	            }

	            String seleccion = (String) JOptionPane.showInputDialog(JFrameAlineacion.this,
	                    "Elige un jugador:", "Alinear", JOptionPane.QUESTION_MESSAGE, null, nombres, nombres[0]);

	            if (seleccion != null) {
	                for (Jugador j : candidatos) {
	                    if (j.getNombre().equals(seleccion)) {
	                        setJugador(j);
	                        break;
	                    }
	                }
	            }
	        }
	    }
	}
	}
