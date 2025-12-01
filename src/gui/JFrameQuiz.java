package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import domain.Liga;


public class JFrameQuiz extends JFramePadre{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList <Liga> ligas;
	private JLabel lblTiempo;
	private int TIEMPO = 10;
	private JPanel panelQuiz;
	private HiloQuiz contadorQuiz;
	private JPanel panelTiempoYPuntos;
	private JPanel panelPreguntaYRespuesta;
	private JPanel tiempo;
	private JPanel panelPuntuacion;
	private JLabel lblPuntuacion;
	
	public JFrameQuiz (ArrayList<Liga> ligas, JFramePadre frameP) {
		super();
		super.framePrevio = frameP;
		this.ligas = ligas;
		usoBotonAtras(super.framePrevio);
		//Creacion del thread
		contadorQuiz = new HiloQuiz();
		JPanel panel = super.panel;
		setImagenDeFondo(null);
		panel.setOpaque(true);
		panel.setLayout(null); // Desactivar el layout manager
		panel.add(botonAtras);
		botonAtras.setBounds(10, 10, 60, 50);
	
		this.setContentPane(panel);
		
		
		//Crear un panel para el quiz
		panelQuiz = new JPanel(new BorderLayout());
		panelQuiz.setBackground(Color.WHITE);
        panelQuiz.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        panelQuiz.setOpaque(true);
		
        panel.add(panelQuiz);
     // Calcular posición y tamaño del panelQuiz
        int panelWidth = 600;
        int panelHeight = 500;
        int x = (1000 - panelWidth) / 2;
        int y = (600 - panelHeight) / 2;
        panelQuiz.setBounds(x, y, panelWidth, panelHeight);
        
        componentesPanelInicio();
	}
	
	// Pantalla de inicio del quiz
	private void componentesPanelInicio() {
		panelQuiz.removeAll();
		
		JButton botonInicio = new JButton("Iniciar quiz");
		botonInicio.setFont(new Font("Arial", Font.BOLD, 24));
        botonInicio.setPreferredSize(new Dimension(200, 60));
        botonInicio.setFocusPainted(false);
		
        botonInicio.addActionListener(e -> {
        	this.quizIniciado();
        	contadorQuiz.start();
        });
        
        panelQuiz.add(botonInicio, BorderLayout.SOUTH);
	}
	// Pantalla del juego
	private void quizIniciado() {
		panelQuiz.removeAll();
		
		//Crear la estructura base del Quiz (Paneles contenedores)
		panelTiempoYPuntos = new JPanel(new GridLayout(1, 2, 15, 15));
		panelPreguntaYRespuesta = new JPanel(new GridLayout(2, 1, 10, 30));
		
		//Añadir el panel del tiempo
		tiempo = new JPanel();
		lblTiempo = new JLabel();
		tiempo.add(lblTiempo);
		panelTiempoYPuntos.add(tiempo);
		//Añadir panel de puntos
		panelPuntuacion = new JPanel();
		lblPuntuacion = new JLabel("0");
		panelPuntuacion.add(lblPuntuacion);
		
		panelTiempoYPuntos.add(panelPuntuacion);
		
		panelQuiz.add(panelTiempoYPuntos, BorderLayout.NORTH);
		
		//Crear el panel de la pregunta
		JPanel pregunta = new JPanel();
		pregunta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panelPreguntaYRespuesta.add(pregunta);
		//Crear panel de las respuestas
		JPanel respuestas = new JPanel(new GridLayout(2, 2, 15, 15));
		respuestas.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panelPreguntaYRespuesta.add(respuestas);
		
		panelQuiz.add(panelPreguntaYRespuesta);
	}
	public void actualizarTiempo(int tiempoRestante) {
		int minutos = (tiempoRestante % 3600) / 60;
		int segundos = tiempoRestante % 60;
		
		LocalTime tiempo = LocalTime.of(0, minutos, segundos);
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("mm:ss");
		lblTiempo.setText("Tiempo restante: " + formato.format(tiempo));
	}
	private class HiloQuiz extends Thread {
        @Override
        public void run() {
        	actualizarTiempo(TIEMPO);
            while (!this.isInterrupted()) {
                for (int i = TIEMPO; i >= 0; i--) {
                    final int tiempoRestante = i;
                    SwingUtilities.invokeLater(() -> {
                       actualizarTiempo(tiempoRestante);
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    	this.interrupt();
                    	break;
                    }
                }
                this.interrupt();
                JLabel texto = new JLabel("¡Te has quedado sin tiempo!", JLabel.CENTER);
                JOptionPane.showMessageDialog(JFrameQuiz.this, 
                		texto, 
                		"Fin del juego", 
                		JOptionPane.OK_OPTION);
                
               }
            }
        }
    
	
}
