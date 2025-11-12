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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
		botonAtras.addActionListener(e -> {
	setVisible(false);
	frameAnterior.setVisible(true);
		});
	}
		
	
 public JFramePadre() {
	 this.setSize(1000, 600);
		this.setTitle("FutGoat");
		this.getContentPane().setBackground(new Color(152, 217, 194));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		ImageIcon logo = new ImageIcon("resources/images/logos/logoApp.png");
		this.setIconImage(logo.getImage());
		//inicializo la imagen de fondo por defecto con el logo
		this.imagenFondo = new ImageIcon("resources/images/logos/logoApp.png").getImage();
		 KeyListener esclistener = new KeyListener() {
     		
     		@Override
     		public void keyTyped(KeyEvent e) {
     			// TODO Auto-generated method stub
     			
     		}
     		
     		@Override
     		public void keyReleased(KeyEvent e) {
     			// TODO Auto-generated method stub
     			
     		}
     		
     		@Override
     		public void keyPressed(KeyEvent e) {
     			// TODO Auto-generated method stub
     			
     			if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
     				if (framePrevio != null) {
	     				setVisible(false);
	     				framePrevio.setVisible(true);
     				}
     			}
     		}
     	};
		
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
		panel.setBackground(new Color(152, 217, 194));
		panel.addKeyListener(esclistener);
		panel.setFocusable(true); //IAG
		panel.requestFocusInWindow(); //IAG
		JButton botonAtras = new JButton(); //Creo que si eliminas el JButton que ya esta declarado en la clase y eliminas la linea 65 funciona
		ImageIcon iconoAtras = new ImageIcon("resources/images/logos/atras.png");
		ImageIcon iconoAjustadoAtras = new ImageIcon(iconoAtras.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		botonAtras.setIcon(iconoAjustadoAtras);
		botonAtras.setBackground(new Color(239, 71, 111));
		botonAtras.setBounds(10, 10, 60, 50);
		botonAtras.setFocusable(false);
		this.botonAtras = botonAtras;
        panel.add(botonAtras);
        
}
 //IAG lo he usado para modificar el set visible para que el panel reciba el foco cada vez que se haga el set visible
 @Override
 public void setVisible(boolean b) {
     super.setVisible(b);
     if (b) {
         panel.requestFocusInWindow(); // Siempre que se muestre, el panel recibe foco
     }
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
