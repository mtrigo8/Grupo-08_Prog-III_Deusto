package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JFrameInicio extends JFrame{


	private static final long serialVersionUID = 1L;

	public JFrameInicio() {
		this.setSize(1000, 600);
		this.setTitle("Nombre_App");
		this.getContentPane().setBackground(Color.orange);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		//Parte mejorada mediante el uso de Claude 4.5 debido a un error en mi codigo original
		// Crear panel con GridBagLayout para centrar elementos
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.orange);
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Configurar el título
		JLabel titulo = new JLabel("Bienvenido a Nombre_App");
		titulo.setFont(new Font("Arial", Font.BOLD, 30));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 20, 0); // Espacio debajo del título
		panel.add(titulo, gbc);
		
		// Crear y configurar el botón
		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.setFont(new Font("Arial", Font.PLAIN, 18));
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		panel.add(btnEntrar, gbc);
		
		this.add(panel);
		
		btnEntrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
			}
			
			
		});
		
	}
	

	public static void main(String[] args) {
		JFrameInicio jfi = new JFrameInicio();
		jfi.setVisible(true);
			}
	
	

}
