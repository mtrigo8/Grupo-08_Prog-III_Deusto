package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import domain.Liga;

public class JFrameSeleccionarLigas extends JFramePadre{
	private ArrayList<Liga> ligas;
	private JPanel botones;
	
	private static final long serialVersionUID = 1L;
	
	public JFrameSeleccionarLigas(ArrayList<Liga> ligas, JFramePadre frameP) {
		super();
		super.framePrevio = frameP;
		
		usoBotonAtras(super.framePrevio); //Llama al metodo para usar el boton atras
		
		this.ligas = ligas;
		JPanel panel = super.panel;
		
		panel.setBackground(new Color(255, 195, 0));
		setImagenDeFondo(null);
		panel.setOpaque(true);
		panel.setLayout(null); // Desactivar el layout manager
		panel.add(botonAtras);
		botonAtras.setBounds(10, 10, 60, 50);
		
		this.setContentPane(panel);
		// Configurar el título
		JLabel titulo = new JLabel("Seleccione una liga ");
		titulo.setOpaque(false);
		titulo.setForeground(Color.BLACK);
		titulo.setFont(new Font("Arial", Font.BOLD, 30));
		titulo.setBounds(350, 50, 300, 40); // x, y, ancho, alto
		panel.add(titulo);
		
		// Panel de botones
		botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		for (Liga liga : this.ligas) {
			
			
			ImageIcon iconoLiga = new ImageIcon("resources/images/ligas/" + liga.getNombre() + ".png");
			ImageIcon iconoAjustado = new ImageIcon(iconoLiga.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
			//Cambiar tamaño del boton al pasar el raton por encima
			double aumento = 1.2;
			int nuevoAlto = (int) (iconoAjustado.getIconHeight() * aumento);
			int nuevoAncho = (int) (iconoAjustado.getIconWidth() * aumento);
			ImageIcon iconoAumentado = escalarIcono(iconoAjustado, nuevoAncho, nuevoAlto);
			JButton botonLiga = new JButton();
			//Aumentar tamaño de los botones
			botonLiga.setPreferredSize(new Dimension(iconoAumentado.getIconWidth(), iconoAumentado.getIconHeight()));
			
			botonLiga.setIcon(iconoAjustado);
			botonLiga.setContentAreaFilled(false);
			botonLiga.setBorderPainted(false);
			
			//Añadir el icono aumentado cuando el raton lo pasa por encima
			botonLiga.setRolloverIcon(iconoAumentado);
			
			
			botonLiga.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JFrameLiga jfl = new JFrameLiga(liga, JFrameSeleccionarLigas.this);
					setVisible(false);
					jfl.setVisible(true);
				}
			});
			
			botones.setBounds(300, 200, 400, 200); // x, y, width, height
			botones.add(botonLiga);
			
		}
		//poner el fondo del panel botones transparente
		botones.setOpaque(false);
		// Calcular tamaño del panel de botones según número de ligas
		panel.add(botones);
		
	}
	//Codigo cambiado parcialmente apartir de lo creado por nosotros para cambiar del uso de gridBagCOntraits el 
	//IAG Chat gpt
	public static ImageIcon escalarIcono(ImageIcon icono, int ancho, int alto) {
        Image img = icono.getImage();
        
        // Creamos un BufferedImage con las nuevas dimensiones y transparencia
        BufferedImage imgEscalada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = imgEscalada.createGraphics();

        // Configura las opciones de renderizado para alta calidad (interpolación)
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        // Dibuja la imagen original en el BufferedImage escalándola
        g2d.drawImage(img, 0, 0, ancho, alto, null);
        g2d.dispose(); // Libera recursos gráficos

        return new ImageIcon(imgEscalada);
    }
		
	} 
	

