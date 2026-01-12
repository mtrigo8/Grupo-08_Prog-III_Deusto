package gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import domain.Equipo;
import domain.Jugador;
import domain.Jugador.TipoPosicion;
import domain.Liga;

public class JFrameJugador extends JFramePadre {

	
	private static final long serialVersionUID = 1L;
	private ArrayList<Liga> todasLasLigas; 

	private Jugador jugador;
	private Liga liga;

	private final Color COLOR_TITULO = new Color(180, 180, 180); 
	private final Color COLOR_VALOR = Color.WHITE; 
	private final Color COLOR_ACENTO = new Color(0, 242, 254); 

	public JFrameJugador(Jugador jugador, JFramePadre ventanaAnterior) {
		super();
		this.jugador = jugador;
		super.framePrevio = ventanaAnterior;
		this.liga=jugador.getEquipo().getLiga();
		this.todasLasLigas = ligas;
		
		//Implementar el boton atras
		usoBotonAtras(framePrevio);
		JPanel panel = new JPanel () {
			@Override
			protected void paintComponent (Graphics g) {
				super.paintComponent(g);
				Graphics2D g2= (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
				Color color1 = new Color(10, 15, 30);
				Color color2 = new Color(45, 75, 110);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		panel.setLayout(null);
		setImagenDeFondo(null);
		//Modificar y añadir boton atras
		panel.add(botonAtras);
		
		JLabel labelDorsal= new JLabel ("#"+jugador.getNumeroCamiseta());
		labelDorsal.setFont(new Font("SansSerif", Font.BOLD, 80));
		labelDorsal.setForeground(new Color(245, 240, 225));
        labelDorsal.setBounds(100, 30, 180, 90);
        panel.add(labelDorsal);
		JLabel labelNombre = new JLabel(jugador.getNombre());
        labelNombre.setFont(new Font("SansSerif",Font.BOLD,42));
        labelNombre.setForeground(Color.white); 
        labelNombre.setBounds(250, 55, 350, 55); 
        panel.add(labelNombre);
        JPanel panelDatos = new JPanel(new GridLayout(5, 2, 10, 15));
        panelDatos.setOpaque(false);
        panelDatos.setBounds(60, 150, 400, 220); 
        añadirFilaDato(panelDatos, "Edad:", String.valueOf(jugador.getEdad()), new Font("SansSerif", Font.BOLD, 15),new Font("SansSerif", Font.PLAIN, 15));
        añadirFilaDato(panelDatos, "Nacionalidad:", jugador.getNacionalidad(), new Font("SansSerif", Font.BOLD, 15), new Font("SansSerif", Font.PLAIN, 15));
        añadirFilaDato(panelDatos, "Altura:", String.format("%d'%02d", jugador.getAltura() / 100, jugador.getAltura() % 100)+ " m", new Font("SansSerif", Font.BOLD, 15), new Font("SansSerif", Font.PLAIN, 15));
        añadirFilaDato(panelDatos, "Posición:", jugador.getPosicion().toString(), new Font("SansSerif", Font.BOLD, 15), new Font("SansSerif", Font.PLAIN, 15));
        añadirFilaDato(panelDatos, "Pie:", "Derecho", new Font("SansSerif", Font.BOLD, 15), new Font("SansSerif", Font.PLAIN, 15));
        panel.add(panelDatos);
		String nombreLiga=liga.getNombre().toLowerCase();
		String nombreEquipo=jugador.getEquipo().getNombrePNGEquipo().toLowerCase();
		String ruta = "resources/images/equipos/"+nombreLiga+"/"+nombreEquipo+".png";
		PanelRedondeado panelEscudo = new PanelRedondeado(Color.WHITE, new Color(240, 240, 255), 30, 200);
        panelEscudo.setLayout(null);
        panelEscudo.setBounds(620, 50, 300, 100);
		ImageIcon icono = new ImageIcon(ruta);
		Image imagenEscalada = icono.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
		panelEscudo.setBounds(620, 50, 300, 100);
		JLabel labelIcono = new JLabel(new ImageIcon(imagenEscalada));
		labelIcono.setBounds(20, 20, 60, 60);
		panelEscudo.add(labelIcono);
		JLabel lblNombreEquipo = new JLabel(jugador.getEquipo().getNombre());
		lblNombreEquipo.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblNombreEquipo.setForeground(new Color(20, 30, 48));
		lblNombreEquipo.setBounds(110, 30, 180, 20);
		panelEscudo.add(lblNombreEquipo);
		
		JLabel lblNombreLiga = new JLabel(liga.getNombre());
		lblNombreLiga.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblNombreLiga.setForeground(Color.GRAY);
		lblNombreLiga.setBounds(100, 50, 180, 20);
		panelEscudo.add(lblNombreLiga);
		
		panel.add(panelEscudo);
		PanelRedondeado panelValor = new PanelRedondeado(new Color(79, 172, 254), new Color(0, 242, 254), 40);
        panelValor.setLayout(null);
		panelValor.setBounds(520, 450, 420, 110);
		double valor= jugador.getValorMercado(); 
		JLabel lblPrecio = new JLabel(valor + " mill. €", SwingConstants.CENTER);
		lblPrecio.setFont(new Font("SansSerif", Font.BOLD, 32));
		lblPrecio.setForeground(Color.WHITE);
		lblPrecio.setBounds(0, 15, 420, 50);
		JLabel lblTextoValor = new JLabel("Valor de mercado", SwingConstants.CENTER);
		lblTextoValor.setFont(new Font("SansSerif", Font.PLAIN, 14));
		lblTextoValor.setForeground(new Color(255, 255, 255, 200));
		lblTextoValor.setBounds(0, 60, 420, 20);
		panelValor.add(lblPrecio);
		panelValor.add(lblTextoValor);
		panel.add(panelValor);
		
		
		JLabel lblStatsTitulo = new JLabel("Estadísticas de la temporada: ");
		lblStatsTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblStatsTitulo.setForeground(COLOR_TITULO);
		lblStatsTitulo.setBounds(60, 430, 250, 20);
		panel.add(lblStatsTitulo);
		ImageIcon imagenjug= new ImageIcon( "resources/images/logos/jugador.png");
        Image imagenescaladajug=imagenjug.getImage().getScaledInstance(200, 250, Image.SCALE_SMOOTH);
        JLabel lblfotojug =new JLabel(new ImageIcon(imagenescaladajug));
        lblfotojug.setBounds(655, 160, 230, 230);
        panel.add(lblfotojug);
        String ruta2="resources/images/logos/equilibrio.png";
        ImageIcon ftequilibrio= new ImageIcon (ruta2);
        BotonCircular btnComparar= new BotonCircular(ftequilibrio);
        btnComparar.setSize(new Dimension (60,60));
        btnComparar.setBounds(430, 475, 60, 60);
        btnComparar.setToolTipText("Comparar jugador");
        btnComparar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				 JFrameComparar jfc = new JFrameComparar(JFrameJugador.this,JFrameJugador.this.jugador);
			        setVisible(false);
			        jfc.setVisible(true);
			}
		});
        panel.add(btnComparar);
		JPanel panelStats = new JPanel(new GridLayout(1, 4, 15, 0));
		panelStats.setOpaque(false);
		panelStats.setBounds(60, 460, 350, 80);
		panelStats.add(crearCirculoStat("PJ", String.valueOf(jugador.getPartidosJugados())));

		String pos = String.valueOf(jugador.getPosicion());

		if (pos.equals("DELANTERO") || pos.equals("CENTROCAMPISTA")) {
		    panelStats.add(crearCirculoStat("Goles", String.valueOf(jugador.getGoles())));
		    panelStats.add(crearCirculoStat("Asist.", String.valueOf(jugador.getAsistencias())));
		    panelStats.add(crearCirculoStat("Reg.", String.valueOf(jugador.getRegates())));
		} else if (pos.equals("PORTERO")) {
		    panelStats.add(crearCirculoStat("Paradas", String.valueOf(jugador.getParadas())));
		    panelStats.add(crearCirculoStat("G. Enc.", String.valueOf(jugador.getGolesEncajados()))); 
		    panelStats.add(crearCirculoStat("Por. 0", String.valueOf(jugador.getPorteriasaCero()))); 
		} else {
		    panelStats.add(crearCirculoStat("Goles", String.valueOf(jugador.getGoles())));
		    panelStats.add(crearCirculoStat("Asist.", String.valueOf(jugador.getAsistencias())));
		    panelStats.add(crearCirculoStat("Por. 0", String.valueOf(jugador.getPorteriasaCero())));
		}
		panel.add(panelStats);
		String accionNombre = "accionVolverAtras";
		KeyStroke ctrl = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE,KeyEvent.CTRL_DOWN_MASK);
		
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrl, accionNombre);
        panel.getActionMap().put(accionNombre, new AbstractAction() {
        	@Override
            public void actionPerformed(ActionEvent e) { 
            	botonAtras.doClick(); 
        }
        });
	    this.setContentPane(panel);
	    usoBotonAtras(super.framePrevio);
	}
	private void añadirFilaDato(JPanel panel, String titulo, String valor,Font fTitle, Font fValue) {
		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblTitulo.setForeground(COLOR_TITULO);
		
		JLabel lblValor = new JLabel(valor);
		lblValor.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblValor.setForeground(COLOR_VALOR);
		
		panel.add(lblTitulo);
		panel.add(lblValor);
	}

	
	//IAG
	private JPanel crearCirculoStat(String titulo, String valor) {
		JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int grosor = 3;
                int size = Math.min(getWidth(), getHeight()) - 10;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;

                g2.setColor(new Color(255, 255, 255, 20));
                g2.setStroke(new BasicStroke(grosor));
                g2.drawOval(x, y, size, size);

                g2.setColor(COLOR_ACENTO);
                g2.setStroke(new BasicStroke(grosor, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawArc(x, y, size, size, 90, 360);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        
        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblValor.setForeground(COLOR_ACENTO);
        lblValor.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblTitulo.setForeground(new Color(200, 200, 200));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        
        panel.add(lblValor, BorderLayout.CENTER);
        panel.add(lblTitulo, BorderLayout.SOUTH);
        
        return panel;
    
	}
	class PanelRedondeado extends JPanel {
	    private Color colorInicio;
	    private Color colorFin;
	    private int radio;
	    //alpha es transparencia
	    private int alpha = 255;
	    public PanelRedondeado(Color inicio, Color fin, int radio,int alpha) {
	        this.colorInicio = inicio;
	        this.colorFin = fin;
	        this.radio = radio;
	        this.alpha = alpha;
	        setOpaque(false);
	    }
	    public PanelRedondeado(Color inicio, Color fin, int radio) {
            this(inicio, fin, radio, 255);
        }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2 = (Graphics2D) g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        Color c1 = new Color(colorInicio.getRed(), colorInicio.getGreen(), colorInicio.getBlue(), alpha);
            Color c2 = new Color(colorFin.getRed(), colorFin.getGreen(), colorFin.getBlue(), alpha);

            GradientPaint gp = new GradientPaint(0, 0, c1, getWidth(), getHeight(), c2);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
            
            if (alpha < 255) {
                g2.setColor(new Color(255, 255, 255, 80));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radio, radio);
            }
        }
	}
	 private class BotonCircular extends JButton {
	    	
			private static final long serialVersionUID = 1L;
	    	private Icon icono;
	    	public BotonCircular (Icon ruta) {
	    		super ("");
	    		this.icono=ruta;
	    		setPreferredSize(new Dimension(icono.getIconWidth(), icono.getIconHeight()));
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
	            GradientPaint gp = new GradientPaint(0, 0, new Color(255, 130, 50), getWidth(), getHeight(), new Color(255, 50, 50));
	            g2.setPaint(gp);
	            g2.fillOval(0, 0, getWidth(), getHeight());

	            if (icono instanceof ImageIcon) {
	                Image img = ((ImageIcon) icono).getImage();
	                g2.drawImage(img, 15, 15, getWidth()-15*2, getHeight()-15*2, this);
	            }

	            g2.setColor(new Color(255, 255, 255, 150));
	            // el borde
	            g2.setStroke(new BasicStroke(2));
	            g2.drawOval(1, 1, getWidth() - 3, getHeight() - 3);
	            
	            g2.dispose();
	 }
	    }
}
