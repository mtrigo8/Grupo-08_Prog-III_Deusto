package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import domain.Liga;

public abstract class JFramePadre extends JFrame {
	protected JPanel panel;
	protected JButton botonAtras;
	protected ArrayList<Liga> ligas;
	protected Image imagenFondo;
	private static final long serialVersionUID = 1L;
	protected JFramePadre framePrevio;

	public void usoBotonAtras(JFramePadre frameAnterior) { //Implemnta en cada clase hija su uso del boton atras
	this.setVisible(false);
	frameAnterior.setVisible(true);
	}
		
 public JFramePadre() {
	 this.setSize(1000, 600);
		this.setTitle("FutGoat");
		this.getContentPane().setBackground(Color.orange);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		ImageIcon logo = new ImageIcon("resources/images/logos/logoApp.png");
		this.setIconImage(logo.getImage());
		//inicializo la imagen de fondo por defecto con el logo
		this.imagenFondo = new ImageIcon("resources/images/logos/logoApp.png").getImage();
		//Parte sugerida por Claude 4.5 para el fondo de pantalla debido a un error de programacion mio al inetntar que ser reescribiera la funcion de pintado
		panel = new JPanel() {
            private static final long serialVersionUID = 1L;
         
           
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (JFramePadre.this.imagenFondo != null) {
                	int x = (getWidth() - 600) / 2;
                    int y = (getHeight() - 600) / 2;
                    g.drawImage(JFramePadre.this.imagenFondo, x, y, 600, 600, this);
                }
            }
        };
        panel.setLayout(null);
		panel.paintComponents(getGraphics());
		panel.setBackground(Color.orange);
		JButton botonAtras = new JButton(); //Creo que si eliminas el JButton que ya esta declarado en la clase y eliminas la linea 65 funciona
		ImageIcon iconoAtras = new ImageIcon("resources/images/logos/atras.png");
		ImageIcon iconoAjustadoAtras = new ImageIcon(iconoAtras.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		botonAtras.setIcon(iconoAjustadoAtras);
		botonAtras.setBackground(new Color(80, 187, 212));
		botonAtras.setBounds(10, 10, 60, 50);
		this.botonAtras = botonAtras;
        panel.add(botonAtras);
        
}
 //permite a las clases hijas cambiar la foto de fondo y en caso de no poner ninguna ruta de foto pone el logo de la app
 protected void setImagenDeFondo(String ruta) {
     if (ruta == null) {
         this.imagenFondo = null;
     } else {
         this.imagenFondo = new ImageIcon(ruta).getImage();
     }
     panel.repaint(); 
}
}
