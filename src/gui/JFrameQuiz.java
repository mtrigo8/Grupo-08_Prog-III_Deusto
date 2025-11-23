package gui;

import java.awt.BorderLayout;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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

	
	public JFrameQuiz (ArrayList<Liga> ligas, JFramePadre frameP) {
		super();
		super.framePrevio = frameP;
		this.ligas = ligas;
		usoBotonAtras(super.framePrevio);
		
		JPanel panel = super.panel;
		setImagenDeFondo(null);
		panel.setOpaque(true);
		panel.setLayout(null); // Desactivar el layout manager
		panel.add(botonAtras);
		botonAtras.setBounds(10, 10, 60, 50);
		lblTiempo = new JLabel();
		panel.add(lblTiempo);
		lblTiempo.setBounds(400, 0, 200, 70);
		lblTiempo.setHorizontalAlignment(SwingConstants.CENTER);
		HiloQuiz hiloquiz = new HiloQuiz();
		hiloquiz.start();
		this.setContentPane(panel);
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
