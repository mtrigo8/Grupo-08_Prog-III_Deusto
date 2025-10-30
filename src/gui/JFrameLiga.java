package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.Liga;

public class JFrameLiga extends JFramePadre {
	private static final long serialVersionUID = 1L;
	private Liga liga;
	private JLabel titulo;
	private JPanel botones;
	private JLabel ligaT;
	private ArrayList<Liga> ligas;

	public JFrameLiga(ArrayList<Liga> ligas, Liga liga) {
		super();
        usoBotonAtras(ligas, null);

        this.ligas = ligas;
        this.liga = liga;
		JPanel panel = super.panel;
        panel.setLayout(null); 

        // --- Título de la liga ---
        ligaT = new JLabel(liga.getNombre(), JLabel.CENTER);
        ligaT.setFont(new Font("Arial", Font.BOLD, 30));
        panel.add(ligaT);

        // --- Subtítulo ---
        titulo = new JLabel("Seleccione una opción", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 25));
        panel.add(titulo);

        // --- Panel de botones ---
        botones = new JPanel(new GridLayout(2, 3, 10, 10)); 
        botones.setOpaque(false);

        String[] contenidos = {"calendario", "equipo", "clasificacion"};
        for (String contenido : contenidos) {
            ImageIcon iconoLiga = new ImageIcon("images/logos/" + contenido + ".png");
            ImageIcon iconoAjustado = new ImageIcon(iconoLiga.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            JButton boton = new JButton();
            boton.setPreferredSize(new Dimension(150, 150));
            boton.setIcon(iconoAjustado);
            boton.addActionListener(e -> {
                switch (contenido) {
                    case "calendario":
                        setVisible(false);
                        JFrameCalendario jfca = new JFrameCalendario(this.ligas, this.liga);
                        jfca.setVisible(true);
                        break;
                    case "equipo":
                        setVisible(false);
                        JFrameListaEquipos jfe = new JFrameListaEquipos(ligas, liga);
                        jfe.setVisible(true);
                        break;
                    case "clasificacion":
                        setVisible(false);
                        JFrameClasificacion jfcl = new JFrameClasificacion(this.ligas,this.liga);
                        jfcl.setVisible(true);
                        break;
                }
            });
            botones.add(boton);
        }

        // Etiquetas debajo de los botones
        for (String contenido : contenidos) {
            JLabel label = new JLabel(contenido.toUpperCase(), JLabel.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 18));
            botones.add(label);
        }
		

		
		panel.add(botones);
		
		this.add(panel);
		
		posicionarComponentes();
		
		

	}
	//Funcion cambiada por chatgpt para que quede mas bonito y mejor cuadrado
	private void posicionarComponentes() {
		int ancho = getWidth();
        int alto = getHeight();

        // --- Liga (nombre principal) ---
        int anchoLiga = (int) (ancho * 0.8);
        int altoLiga = 50;
        int xLiga = (ancho - anchoLiga) / 2;
        int yLiga = (int) (alto * 0.10);
        ligaT.setBounds(xLiga, yLiga, anchoLiga, altoLiga);

        // --- Subtítulo ---
        int anchoTitulo = (int) (ancho * 0.8);
        int altoTitulo = 40;
        int xTitulo = (ancho - anchoTitulo) / 2;
        int yTitulo = (int) (alto * 0.25);
        titulo.setBounds(xTitulo, yTitulo, anchoTitulo, altoTitulo);

        // --- Panel de botones ---
        int anchoBotones = (int) (ancho * 0.8);
        int altoBotones = (int) (alto * 0.45);
        int xBotones = (ancho - anchoBotones) / 2;
        int yBotones = (int) (alto * 0.40);
        botones.setBounds(xBotones, yBotones, anchoBotones, altoBotones);
    }

	@Override
	public void usoBotonAtras(ArrayList<Liga> ligas, Liga liga) {
		// TODO Auto-generated method stub
		botonAtras.addActionListener(e -> {
			JFrameSeleccionarLigas jfsl = new JFrameSeleccionarLigas(ligas);
			jfsl.setVisible(true);
			setVisible(false);
		});
	}

}
