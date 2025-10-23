package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JFramePadre extends JFrame {
	protected JPanel panel;
	
	private static final long serialVersionUID = 1L;

	
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
		panel = new JPanel(new GridBagLayout()) {
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
		panel.paintComponents(getGraphics());
		panel.setBackground(Color.orange);
}
}
