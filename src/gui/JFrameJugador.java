package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import domain.Equipo;
import domain.Jugador;
import domain.Jugador.TipoPosicion;
import domain.Liga;

public class JFrameJugador extends JFramePadre {

	
	private static final long serialVersionUID = 1L;
	private Jugador jugador;
	private Liga liga;


	private JPanel panelNombre;//irá arriba como lo mas importante
	private JFramePadre ventanaAnterior;
	
	public JFrameJugador(Jugador jugador, JFramePadre ventanaAnterior) {
		super();
		this.jugador = jugador;
		super.framePrevio = ventanaAnterior;
		this.liga=jugador.getEquipo().getLiga();
		JPanel panel = super.panel;
		setImagenDeFondo(null);
		JPanel panelFicha = new JPanel();
		panelFicha.setLayout(null);
		panelFicha.setBackground(new Color(252, 252, 252));
		panelFicha.setBounds(50,30,900,550);
		panelFicha.setBorder(BorderFactory.createLineBorder(Color.GRAY,1));
		panel.add(panelFicha);
		
	
		panelNombre = new JPanel();
		panelNombre.setOpaque(false);
		JLabel labelDorsal= new JLabel ("#"+jugador.getNumeroCamiseta());
		labelDorsal.setFont(new Font("Arial",Font.BOLD,50));
		labelDorsal.setForeground(new Color(200,200,200));
		labelDorsal.setBounds(30,20,150,60);
		panelFicha.add(labelDorsal);
		JLabel labelNombre = new JLabel(jugador.getNombre());
		labelNombre.setFont(new Font("Arial", Font.BOLD, 40));
		labelNombre.setForeground(new Color (23,58,100));
		labelNombre.setBounds(140,25,500,50);
		panelFicha.add(labelNombre);

		
		String nombreLiga=liga.getNombre().toLowerCase();
		String nombreEquipo=jugador.getEquipo().getNombrePNGEquipo().toLowerCase();
		String ruta = "resources/images/equipos/"+nombreLiga+"/"+nombreEquipo+".png";
		JPanel panelEscudo=new JPanel();
		ImageIcon icono = new ImageIcon(ruta);
		Image imagenEscalada = icono.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
		JLabel labelIcono = new JLabel(new ImageIcon(imagenEscalada));
		labelIcono.setBounds(15, 15, 100, 100);
		panelEscudo.add(labelIcono);
		panelEscudo.setLayout(null);
		panelEscudo.setBackground(Color.WHITE);
		panelEscudo.setBorder(BorderFactory.createLineBorder(new Color(220,220,220)));
		panelEscudo.setBounds(570, 25, 300, 130);
		JLabel lblNombreEquipo = new JLabel(jugador.getEquipo().getNombre());
		lblNombreEquipo.setFont(new Font("Arial", Font.BOLD, 16));
		lblNombreEquipo.setForeground(new Color (74,144,226));
		lblNombreEquipo.setBounds(125, 35, 170, 25);
		panelEscudo.add(lblNombreEquipo);
		
		JLabel lblNombreLiga = new JLabel(liga.getNombre());
		lblNombreLiga.setFont(new Font("Arial", Font.PLAIN, 14));
		lblNombreLiga.setForeground(Color.GRAY);
		lblNombreLiga.setBounds(125, 65, 170, 25);
		panelEscudo.add(lblNombreLiga);
		
		panelFicha.add(panelEscudo);
		JPanel panelValor = new JPanel();
		panelValor.setLayout(new GridLayout(2, 1));
		panelValor.setBackground(new Color (74,144,226)); 
		panelValor.setBounds(600, 420, 270, 100);
		Random r = new Random();
		int valor = 5 + r.nextInt(95); 
		JLabel lblPrecio = new JLabel(valor + ",00 mill. €", SwingConstants.CENTER);
		lblPrecio.setFont(new Font("Arial", Font.BOLD, 32));
		lblPrecio.setForeground(Color.WHITE);
		
		JLabel lblTextoValor = new JLabel("Valor de mercado", SwingConstants.CENTER);
		lblTextoValor.setFont(new Font("Arial", Font.PLAIN, 14));
		lblTextoValor.setForeground(new Color(220, 220, 255));
		panelValor.add(lblPrecio);
		panelValor.add(lblTextoValor);
		panelFicha.add(panelValor);
		
		JPanel panelDatos = new JPanel(new GridLayout(5, 2, 10, 15));
		panelDatos.setOpaque(false);
		panelDatos.setBounds(30, 120, 400, 250);
		
		añadirFilaDato(panelDatos, "Edad;", String.valueOf(jugador.getEdad()));
		añadirFilaDato(panelDatos, "Lugar de nacimiento:", "Desconocido");
		añadirFilaDato(panelDatos, "Nacionalidad:", jugador.getNacionalidad());
		añadirFilaDato(panelDatos, "Altura:", "1,80 m"); 
		añadirFilaDato(panelDatos, "Posición:", jugador.getPosicion().toString());
		
		panelFicha.add(panelDatos);
		JLabel lblStatsTitulo = new JLabel("Estadísticas de la temporada: ");
		lblStatsTitulo.setFont(new Font("Arial", Font.BOLD, 14));
		lblStatsTitulo.setBounds(30, 400, 250, 20);
		panelFicha.add(lblStatsTitulo);
		
		JPanel panelStats = new JPanel(new GridLayout(1, 3, 20, 0));
		panelStats.setOpaque(false);
		panelStats.setBounds(30, 430, 300, 80);
		panelStats.add(crearCirculoStat("PJ", String.valueOf(10 + r.nextInt(20))));
		if (!jugador.getPosicion().toString().equals("PORTERO")) {
			panelStats.add(crearCirculoStat("Goles", String.valueOf(r.nextInt(15))));
			panelStats.add(crearCirculoStat("Asist", String.valueOf(r.nextInt(10))));
		} else {
			panelStats.add(crearCirculoStat("Paradas", String.valueOf(r.nextInt(50))));
			panelStats.add(crearCirculoStat("Porterias a 0", String.valueOf(r.nextInt(10))));
		}
		
		panelFicha.add(panelStats);

		this.setContentPane(panel);
		usoBotonAtras(super.framePrevio);
	}
	private void añadirFilaDato(JPanel panel, String titulo, String valor) {
		JLabel lblTitulo = new JLabel(titulo);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
		lblTitulo.setForeground(Color.GRAY);
		
		JLabel lblValor = new JLabel(valor);
		lblValor.setFont(new Font("Arial", Font.PLAIN, 16));
		lblValor.setForeground(new Color (30,30,30));
		
		panel.add(lblTitulo);
		panel.add(lblValor);
	}
	private JPanel crearCirculoStat(String titulo, String valor) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		
		JLabel lblValor = new JLabel(valor, SwingConstants.CENTER);
		lblValor.setFont(new Font("Arial", Font.BOLD, 20));
		lblValor.setForeground(new Color (74,144,226));
		lblValor.setBorder(BorderFactory.createLineBorder(new Color (74,144,226), 2, true)); //IAG para poner el borde en las stats
		
		JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Arial", Font.PLAIN, 12));
		
		panel.add(lblValor, BorderLayout.CENTER);
		panel.add(lblTitulo, BorderLayout.SOUTH);
		
		return panel;
	}
}
