package gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
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


	public JFrameLiga(Liga liga, JFramePadre ventanaAnterior) {
		super();
		// Implementar el logo de la liga como fondo de pantalla
		super.setImagenDeFondo("resources/images/ligas/"+liga.getNombre().toLowerCase()+".png");
		
        super.framePrevio = ventanaAnterior;
        usoBotonAtras(super.framePrevio);
        this.liga = liga;
		JPanel panel = super.panel;
        panel.setLayout(null);
        Color colorLetra= Color.BLACK;
        
        panel.setOpaque(true);
        setImagenDeFondo(null);
        
        // --- Título de la liga ---
        ligaT = new JLabel(liga.getNombre(), JLabel.CENTER);
        ligaT.setFont(new Font("Arial", Font.BOLD, 30));
        	ligaT.setForeground(colorLetra);
        panel.add(ligaT);

        // --- Subtítulo ---
        titulo = new JLabel("Seleccione una opción", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 25));
        	titulo.setForeground(colorLetra);
        panel.add(titulo);

        // --- Panel de botones ---
        botones = new JPanel(new GridLayout(2, 3, 10, 10)); 
        botones.setOpaque(false);

        String[] contenidos = {"calendario", "equipo", "clasificacion"};
        for (String contenido : contenidos) {
            ImageIcon iconoLiga = new ImageIcon("resources/images/logos/" + contenido + ".png");
            ImageIcon iconoAjustado = new ImageIcon(iconoLiga.getImage().getScaledInstance(90, 90, Image.SCALE_SMOOTH));
            //Cambiar tamaño de la imagen al poner el raton encima
            double aumento = 1.2;
            int nuevoAlto = (int) (iconoAjustado.getIconHeight() * aumento);
			int nuevoAncho = (int) (iconoAjustado.getIconWidth() * aumento);
			ImageIcon iconoAumentado = escalarIcono(iconoAjustado, nuevoAncho, nuevoAlto);
            //Crear boton
			JButton boton = new JButton();
            boton.setPreferredSize(new Dimension(150, 150));
            boton.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            boton.setIcon(iconoAjustado);
            //Borrar el fondo del boton y el borde
            boton.setContentAreaFilled(false);
            boton.setBorderPainted(false);
            
            //Añadir nuevo icono al pasar con el raton por encima
            boton.setRolloverIcon(iconoAumentado);
            boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            boton.addActionListener(e -> {
                switch (contenido) {
                    case "calendario":
                        setVisible(false);
                        JFrameCalendario jfca = new JFrameCalendario( this.liga, this);
                        jfca.setVisible(true);
                        break;
                    case "equipo":
                        setVisible(false);
                        JFrameListaEquipos jfe = new JFrameListaEquipos(liga, this);
                        jfe.setVisible(true);
                        break;
                    case "clasificacion":
                        setVisible(false);
                        JFrameClasificacion jfcl = new JFrameClasificacion(this.liga, this);
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
            label.setForeground(colorLetra);
            botones.add(label);
        }
		

		
		panel.add(botones);
		
		this.add(panel);
		
		posicionarComponentes();
		
		

	}
	//IAG
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
	//IAG: Chat GPT funcion para cambiar tamaño de la imagen
	public static ImageIcon escalarIcono(ImageIcon icono, int ancho, int alto) {
        Image img = icono.getImage();
        
        // Creamos un BufferedImage con las nuevas dimensiones y transparencia
        BufferedImage imgEscalada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imgEscalada.createGraphics();

        // Configura las opciones de renderizado para alta calidad (interpolación)
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        // Dibuja la imagen original en el BufferedImage escalándola
        g2d.drawImage(img, 0, 0, ancho, alto, null);
        g2d.dispose(); // Libera recursos gráficos

        return new ImageIcon(imgEscalada);
    }

}
