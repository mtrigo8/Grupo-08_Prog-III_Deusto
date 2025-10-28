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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class JFramePadre extends JFrame {
	protected JPanel panel;
	protected JButton botonAtras;
	
	private static final long serialVersionUID = 1L;

	public abstract void usoBotonAtras(); //Implemnta en cada clase hija su uso del boton atras
	
 public JFramePadre() {
	 this.setSize(1000, 600);
		this.setTitle("FutGoat");
		this.getContentPane().setBackground(Color.orange);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		ImageIcon logo = new ImageIcon("images/logos/logoApp.png");
		this.setIconImage(logo.getImage());
		//Parte sugerida por Claude 4.5 para el fondo de pantalla debido a un error de programacion mio al inetntar que ser reescribiera la funcion de pintado
		panel = new JPanel() {
            private static final long serialVersionUID = 1L;
            private Image imagenFondo = new ImageIcon("images/logos/logoApp.png").getImage();
           
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagenFondo != null) {
                	int x = (getWidth() - 600) / 2;
                    int y = (getHeight() - 600) / 2;
                    g.drawImage(imagenFondo, x, y, 600, 600, this);
                }
            }
        };
        panel.setLayout(null);
		panel.paintComponents(getGraphics());
		panel.setBackground(Color.orange);
		JButton botonAtras = new JButton();
		ImageIcon iconoAtras = new ImageIcon("images/logos/atras.png");
		ImageIcon iconoAjustadoAtras = new ImageIcon(iconoAtras.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		botonAtras.setIcon(iconoAjustadoAtras);
		botonAtras.setBackground(new Color(80, 187, 212));
		botonAtras.setBounds(10, 10, 60, 50);
		this.botonAtras = botonAtras;
        panel.add(botonAtras);
}
}
