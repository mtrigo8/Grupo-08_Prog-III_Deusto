package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

import domain.Liga;

public class JFrameInicio extends JFramePadre {
	private ArrayList<Liga> ligas;

    private static final long serialVersionUID = 1L;
    private static final long MAX_VALUE = 3000000;

    private JLabel titulo;
    private JButton btnEntrar;
    private JButton btnQuiz;
    private JProgressBar progressBar = new JProgressBar(0, 100);
    
    private Contador contador;
    
    public JFrameInicio(ArrayList<Liga> ligas) {
        super();
        this.ligas = ligas;
        usoBotonAtras(super.framePrevio);

        JPanel panel = super.panel;
        panel.setLayout(null);

        // --- Crear Título ---
        titulo = new JLabel("Bienvenido a FutGoat", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(new Color(33, 33, 33));
        titulo.setOpaque(false);
        panel.add(titulo, BorderLayout.NORTH);
        
        
        
        // --- Crear Botón Entrar ---
        btnEntrar = new JButton("Entrar aplicacion");
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 20));
        btnEntrar.setBackground(new Color(185, 255, 183));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setForeground(Color.BLACK);
        btnEntrar.setBorderPainted(false);
        
        panel.add(btnEntrar);

       

        // Acción del botón
        btnEntrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	contador = new Contador();
            	panel.add(progressBar);
            	progressBar.setBounds(0, 543, getWidth(), 20);
            	progressBar.setBackground(new Color(239, 71, 111));
            	progressBar.setForeground(progressBar.getBackground().darker());
            	progressBar.setStringPainted(true); 
            	contador.start();
            }
        });
        
        //Crear boton del quiz
        btnQuiz = new JButton("Entrar Quiz");
        btnQuiz.setFont(new Font("Arial", Font.BOLD, 20));
        btnQuiz.setBackground(new Color(185, 255, 183));
        btnQuiz.setFocusPainted(false);
        btnQuiz.setForeground(Color.BLACK);
        btnQuiz.setBorderPainted(false);
        
        btnQuiz.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrameQuiz jfq = new JFrameQuiz(ligas, JFrameInicio.this);
				setVisible(false);
				jfq.setVisible(true);
			}
		});
        
        panel.add(btnQuiz);
        super.botonAtras.setVisible(false);
        posicionarComponentes();
        
        
        //Crear listener para entrar en el siguente frame introduciendo enter
    
        KeyListener kLEntrarAplicacion = new KeyListener() {
			
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
				if (e.getKeyCode() == KeyEvent.VK_ENTER ) {
					
					JFrameSeleccionarLigas jfs = new JFrameSeleccionarLigas(ligas , JFrameInicio.this);
	                setVisible(false);
	                jfs.setVisible(true);
				}
			}
		};
	
		panel.addKeyListener(kLEntrarAplicacion);
		
		this.add(panel);
    }
    
    
    //Funcion creada con ayuda de ChatGPT para arreglar un error en el posicionamiento de los componentes al ejecutar
    //IAG
    private void posicionarComponentes() {
        int ancho = getWidth();
        int alto = getHeight();

        // Tamaños preferidos
        int anchoTitulo = (int) (ancho * 0.8);
        int altoTitulo = 60;

        int anchoBoton = (int) (ancho * 0.2);
        int altoBoton = 60;

        // Posiciones relativas
        int xTitulo = (ancho - anchoTitulo) / 2;
        int yTitulo = (int) (alto * 0.08);

        int xBotonEntrar = ((ancho - anchoBoton) / 2)/2;
        int xBotonQuiz = ancho - xBotonEntrar*2;
        int yBoton = (int) (alto * 0.78);

        // Asignar posiciones
        titulo.setBounds(xTitulo, yTitulo, anchoTitulo, altoTitulo);
        btnEntrar.setBounds(xBotonEntrar, yBoton, anchoBoton, altoBoton);
        btnQuiz.setBounds(xBotonQuiz, yBoton, anchoBoton, altoBoton);
    }
    @Override
	public void usoBotonAtras(JFramePadre framePrevio) {
    }
    
    private class Contador extends Thread {
    	@Override
    	public void run() {
    		int progreso;
    		
    		for (int i=0; i <= MAX_VALUE; i++) {
    			
    			
    			// Valor de progreso
    			progreso = (int) ((i * 100) / MAX_VALUE);
    			
    			updateProgressBar(progreso);
    		}
    		JFrameInicio.this.panel.remove(progressBar);
            JFrameSeleccionarLigas jfs = new JFrameSeleccionarLigas(ligas , JFrameInicio.this);
            jfs.setVisible(true);
            setVisible(false);
    	}
    }
    
    private void updateProgressBar(final int value) {
        SwingUtilities.invokeLater(() -> progressBar.setValue(value));
    }
    

}


	



