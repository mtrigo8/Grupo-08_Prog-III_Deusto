package gui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class JFramecargapantalla extends JFrame{
	
	private JLabel imagenfondo;
	private Thread thread;
	private ArrayList<String> imagenes;
	private JButton btnSaltar;
	public JFramecargapantalla (){
		setSize(600, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        
        btnSaltar = new JButton("Saltar");
        add(btnSaltar, BorderLayout.NORTH);
        
        imagenfondo = new JLabel();
        JPanel panel = new JPanel (new BorderLayout());
        panel.add(imagenfondo, BorderLayout.CENTER);
        this.add(panel, BorderLayout.CENTER); 

        imagenes = new ArrayList<>();
        imagenes.add("resources/images/logos/calendario.png");
        //meter mas fotos
        
        startBanner();
        
        
        btnSaltar.addActionListener(e -> {
        	btnSaltar.setEnabled(false);
        	if (thread != null) {
        		thread.interrupt(); 
        	}
        	
            setVisible(false);
       });
        
	}
	 protected void startBanner() {
	    	thread= new Thread (()->{
	        	while (!Thread.currentThread().isInterrupted()) {
	        		
	        		for (String i: imagenes ) {
	        			actualizarLblBanner(i);
	        			try {
							Thread.sleep(5000);
						} catch (InterruptedException ex) {
							Thread.currentThread().interrupt();
						}
	        		}
	        	}
	        });
	    	thread.start();
	    	
	    }
	 protected void actualizarLblBanner(String imagen) {
	        SwingUtilities.invokeLater(() -> {
	            ImageIcon icon = new ImageIcon(imagen);
	            // Escalar la imagen para que quepa en la ventana
	            Image img = icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
	            imagenfondo.setIcon(new ImageIcon(img));
	        });
	    }
	 public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> new JFramecargapantalla());
	        new JFramecargapantalla().setVisible(true);
	    }
	}

	

