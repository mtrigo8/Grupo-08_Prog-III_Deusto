package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class JFrameInicio extends JFrame{

	private static final long serialVersionUID = 1L;

	public JFrameInicio() {
		this.setSize(1000, 600);
		this.setTitle("Nombre_App");
		this.getContentPane().setBackground(Color.orange);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		JLabel titulo = new JLabel("Bienvenido a Nombre_App");
		titulo.setFont(new Font("Arial", Font.BOLD, 30));
		titulo.setHorizontalAlignment(JLabel.CENTER);
		titulo.setVerticalAlignment(JLabel.CENTER);
		this.add(titulo);
		ImageIcon logo = new ImageIcon("images/logos/logoApp.png");
		this.setIconImage(logo.getImage());
		
	}
	
	public static void main(String[] args) {
		JFrameInicio jfi = new JFrameInicio();
		jfi.setVisible(true);
	}
	
	

}
