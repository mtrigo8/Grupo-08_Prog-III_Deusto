package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
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


	private JPanel panelNombre;//irá arriba como lo mas importante
	private JFramePadre ventanaAnterior;
	
	public JFrameJugador(Jugador jugador, JFramePadre ventanaAnterior) {
		super();
		this.jugador = jugador;
		super.framePrevio = ventanaAnterior;
		this.liga=jugador.getEquipo().getLiga();
		this.todasLasLigas = ligas;
		JPanel panel = super.panel;
		setImagenDeFondo(null);

		JLabel labelDorsal= new JLabel ("#"+jugador.getNumeroCamiseta());
		labelDorsal.setFont(new Font("SansSerif",Font.BOLD,54));
		labelDorsal.setForeground(Color.BLACK);
		labelDorsal.setBounds(60, 50, 140, 70);
		panel.add(labelDorsal);
		JLabel labelNombre = new JLabel(jugador.getNombre());
        labelNombre.setFont(new Font("SansSerif",Font.BOLD,42));
        labelNombre.setForeground(new Color(33, 43, 54)); 
        labelNombre.setBounds(190, 55, 400, 55); 
        panel.add(labelNombre);
        JPanel panelDatos = new JPanel(new GridLayout(5, 2, 10, 15));
        panelDatos.setOpaque(false);
        panelDatos.setBounds(60, 150, 400, 220); 
        añadirFilaDato(panelDatos, "Edad:", String.valueOf(jugador.getEdad()), new Font("SansSerif", Font.BOLD, 15),new Font("SansSerif", Font.PLAIN, 15));
        añadirFilaDato(panelDatos, "Nacionalidad:", jugador.getNacionalidad(), new Font("SansSerif", Font.BOLD, 15), new Font("SansSerif", Font.PLAIN, 15));
        añadirFilaDato(panelDatos, "Altura:", "1,80 m", new Font("SansSerif", Font.BOLD, 15), new Font("SansSerif", Font.PLAIN, 15));
        añadirFilaDato(panelDatos, "Posición:", jugador.getPosicion().toString(), new Font("SansSerif", Font.BOLD, 15), new Font("SansSerif", Font.PLAIN, 15));
        añadirFilaDato(panelDatos, "Pie:", "Derecho", new Font("SansSerif", Font.BOLD, 15), new Font("SansSerif", Font.PLAIN, 15));
        panel.add(panelDatos);
		String nombreLiga=liga.getNombre().toLowerCase();
		String nombreEquipo=jugador.getEquipo().getNombrePNGEquipo().toLowerCase();
		String ruta = "resources/images/equipos/"+nombreLiga+"/"+nombreEquipo+".png";
		PanelRedondeado panelEscudo = new PanelRedondeado(Color.WHITE,Color.WHITE,30);
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
		lblNombreEquipo.setForeground(new Color (74,144,226));
		lblNombreEquipo.setBounds(100, 30, 180, 20);
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
		lblTextoValor.setForeground(new Color(220, 220, 255));
		panelValor.add(lblPrecio);
		panelValor.add(lblTextoValor);
		panel.add(panelValor);
		
		
		JLabel lblStatsTitulo = new JLabel("Estadísticas de la temporada: ");
		lblStatsTitulo.setFont(new Font("SansSerif", Font.BOLD, 14));
		lblStatsTitulo.setForeground(Color.GRAY);
		lblStatsTitulo.setBounds(60, 430, 250, 20);
		panel.add(lblStatsTitulo);
		ImageIcon imagenjug= new ImageIcon( "resources/images/logos/villalibre.png");
        Image imagenescaladajug=imagenjug.getImage().getScaledInstance(200, 250, Image.SCALE_SMOOTH);
        JLabel lblfotojug =new JLabel(new ImageIcon(imagenescaladajug));
        lblfotojug.setBounds(655, 160, 230, 230);
        panel.add(lblfotojug);
        String ruta2="resources/images/logos/equilibrio.png";
        ImageIcon ftequilibrio= new ImageIcon (ruta2);
        BotonCircular btnComparar= new BotonCircular(ftequilibrio);
        btnComparar.setSize(new Dimension (60,60));
        btnComparar.setBounds(430, 475, 80, 40);
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
		JPanel panelStats = new JPanel(new GridLayout(1, 3, 20, 0));
		panelStats.setOpaque(false);
		panelStats.setBounds(60, 460, 350, 80);
		panelStats.add(crearCirculoStat("PJ", String.valueOf(jugador.getPartidosJugados())));
		if (jugador.getPosicion().toString().equals("DELANTERO") || jugador.getPosicion().toString().equals("CENTROCAMPISTA") ) {
			panelStats.add(crearCirculoStat("Goles", String.valueOf(jugador.getGoles())));
			panelStats.add(crearCirculoStat("Asist", String.valueOf(jugador.getAsistencias())));
			panelStats.add(crearCirculoStat("Regates", String.valueOf(jugador.getRegates())));
		} else if (jugador.getPosicion().toString().equals("PORTERO")){
			panelStats.add(crearCirculoStat("Paradas", String.valueOf(jugador.getParadas())));
			panelStats.add(crearCirculoStat("Goles encajados", String.valueOf(jugador.getGolesEncajados())));
			panelStats.add(crearCirculoStat("Porterias a 0", String.valueOf(jugador.getPorteriasaCero())));
		} else {
			panelStats.add(crearCirculoStat("Goles", String.valueOf(jugador.getGoles())));
			panelStats.add(crearCirculoStat("Asist", String.valueOf(jugador.getAsistencias())));
			panelStats.add(crearCirculoStat("Regates", String.valueOf(jugador.getRegates())));
			panelStats.add(crearCirculoStat("Porterias a 0", String.valueOf(jugador.getPorteriasaCero())));
		}
		
		panel.add(panelStats);

		this.setContentPane(panel);
		usoBotonAtras(super.framePrevio);
	}
	private void añadirFilaDato(JPanel panel, String titulo, String valor,Font fTitle, Font fValue) {
		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 16));
		lblTitulo.setForeground(Color.GRAY);
		
		JLabel lblValor = new JLabel(valor);
		lblValor.setFont(new Font("SansSerif", Font.PLAIN, 16));
		lblValor.setForeground(new Color (30,30,30));
		
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
                
                // Fondo blanco
                g2.setColor(Color.WHITE);
                g2.fillOval(2, 2, getWidth()-5, getHeight()-5);
                
                // Borde Azul bonito
                g2.setStroke(new java.awt.BasicStroke(3f)); // Borde más grueso
                g2.setColor(new Color(74, 144, 226));
                g2.drawOval(2, 2, getWidth()-5, getHeight()-5);
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout());
        
        JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
        lblValor.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblValor.setForeground(new Color(74, 144, 226));
        
        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblTitulo.setForeground(Color.GRAY);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0)); // Espacio abajo
        
        panel.add(lblValor, BorderLayout.CENTER);
        panel.add(lblTitulo, BorderLayout.SOUTH);
        
        return panel;
    
	}
	class PanelRedondeado extends JPanel {
	    private Color colorInicio;
	    private Color colorFin;
	    private int radio;

	    // Constructor para Degradado (Gradient)
	    public PanelRedondeado(Color inicio, Color fin, int radio) {
	        this.colorInicio = inicio;
	        this.colorFin = fin;
	        this.radio = radio;
	        setOpaque(false);
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        Graphics2D g2 = (Graphics2D) g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        java.awt.GradientPaint gp = new java.awt.GradientPaint(0, 0, colorInicio, getWidth(), getHeight(), colorFin);
	        g2.setPaint(gp);
	        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
	    }
	}
	 private class BotonCircular extends JButton {
	    	
			private static final long serialVersionUID = 1L;
	    	private Icon icono;
	    	public BotonCircular (Icon ruta) {
	    		super ("");
	    		this.icono=ruta;
	    		setPreferredSize(new Dimension(icono.getIconWidth(), icono.getIconHeight()));
	    		//Quito el diseño por defecto para los botones de Java
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
	            g2.setColor(Color.WHITE); 
	            g2.fillOval(0, 0, getWidth(), getHeight());

	          //IAG porque sino no escalaba bien
	               if (icono instanceof ImageIcon) {
	                   Image img = ((ImageIcon) icono).getImage();

	                    g2.drawImage(img, 0, 0, getWidth(), getHeight(), this);
	                } else {
	                   
	                    int x = (getWidth() - icono.getIconWidth()) / 2;
	                    int y = (getHeight() - icono.getIconHeight()) / 2;
	                    icono.paintIcon(this, g2, x, y);
	                }
	            
	            
	           
	             g2.setColor(new Color(200, 200, 200));
	             g2.drawOval(0, 0, getWidth()-1, getHeight()-1);
	            g2.dispose();
	 }
	    }
}
