package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import javax.swing.*;

import domain.Liga;

public class JFrameInicio extends JFramePadre {
	private ArrayList<Liga> ligas;

    private static final long serialVersionUID = 1L;
    private static final long MAX_VALUE = 3000000;
    private FondoAnimado panelPrincipal;
    private JLabel titulo;
    private JButton btnEntrar;
    private JButton btnQuiz;
    private JProgressBar progressBar = new JProgressBar(0, 100);
    private Contador contador;
    private JLabel logo;
    public JFrameInicio(ArrayList<Liga> ligas) {
        super();
        this.ligas = ligas;
        usoBotonAtras(super.framePrevio);
        panelPrincipal= new FondoAnimado();
        panelPrincipal.setLayout(null);
        this.setContentPane(panelPrincipal);
        // --- Crear Título ---
        titulo = new JLabel("Bienvenido a FutGoat", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 36));
        titulo.setForeground(new Color(33, 33, 33));
        titulo.setOpaque(false);
        panelPrincipal.add(titulo, BorderLayout.NORTH);
        
        ImageIcon imagen= new ImageIcon( "resources/images/logos/logoApp.png");
        Image imagenescalada=imagen.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        logo =new JLabel(new ImageIcon(imagenescalada));
        panelPrincipal.add(logo);
        
        // --- Crear Botón Entrar ---
        btnEntrar = new BotonCircular("Entrar Aplicacion", new Color(185, 255, 183), new Color(135, 205, 133));
        btnEntrar.setPreferredSize(new Dimension(230, 70));
        
        panelPrincipal.add(btnEntrar);

       

        // Acción del botón
        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	contador = new Contador();
            	panelPrincipal.add(progressBar);
            	progressBar.setBounds(0, 543, getWidth(), 20);
            	progressBar.setBackground(new Color(239, 71, 111));
            	progressBar.setForeground(progressBar.getBackground().darker());
            	progressBar.setStringPainted(true); 
            	contador.start();
            }
        });
        
        //Crear boton del quiz
        btnQuiz = new BotonCircular("Entrar Quiz",new Color(245, 245, 220), new Color(210, 205, 170));
        btnQuiz.setPreferredSize(new Dimension(230, 70));

        panelPrincipal.add(btnQuiz);
        btnQuiz.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFrameQuiz jfq = new JFrameQuiz(ligas, JFrameInicio.this);
				setVisible(false);
				jfq.setVisible(true);
			}
		});
        
        
        super.botonAtras.setVisible(false);
        
        
        
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
	
		panelPrincipal.addKeyListener(kLEntrarAplicacion);
		posicionarComponentes();
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
        int xLogo = (ancho - 250) / 2; 
        int yLogo = (int) (alto * 0.28);
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
        logo.setBounds(xLogo, yLogo, 250, 250);
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
    		SwingUtilities.invokeLater(()->{
    			panelPrincipal.remove(progressBar);
    			panelPrincipal.repaint();    			
    			});
            JFrameSeleccionarLigas jfs = new JFrameSeleccionarLigas(ligas , JFrameInicio.this);
            jfs.setVisible(true);
            setVisible(false);
    	}
    	
    }
    
    private void updateProgressBar(final int value) {
        SwingUtilities.invokeLater(() -> progressBar.setValue(value));
    }
    private class BotonCircular extends JButton {
    	
		private static final long serialVersionUID = 1L;
		private Color colorNormal,colorSeleccionado, colorPresionado;
    	private boolean Seleccionado=false;
    	public BotonCircular (String text, Color normal, Color seleccionado) {
    		super (text);
    		this.colorNormal=normal;
    		this.colorSeleccionado= seleccionado;
    		this.colorPresionado=seleccionado.darker();
    		//Quito el diseño por defecto para los botones de Java
    		setContentAreaFilled(false);
    		setFocusPainted(false);
    		setBorderPainted(false);
    		setOpaque(false);
    		setForeground(Color.BLACK);
    		setFont (new Font ("SansSerif",Font.BOLD,18));
    		setCursor (new Cursor(Cursor.HAND_CURSOR));
    		addMouseListener (new MouseAdapter(){
    			@Override
    			public void mouseEntered(MouseEvent e) {
    				// TODO Auto-generated method stub
    				Seleccionado=true;
    			}
    			@Override
    			public void mouseExited(MouseEvent e) {
    				// TODO Auto-generated method stub
    				Seleccionado=false;
    			}
    		});
    	}
    	//IAG
    	@Override
    	  protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Decidimos el color según el estado (Presionado, Hover, o Normal)
            if (getModel().isPressed()) g2.setColor(colorPresionado);
            else if (Seleccionado) g2.setColor(colorSeleccionado); // Aquí usamos la variable booleana
            else g2.setColor(colorNormal);

            // Dibujamos el rectángulo redondeado (Radio 30)
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            
            // Dejamos que Java pinte el texto encima
            super.paintComponent(g2);
            g2.dispose();
        }
    }
    private class FondoAnimado extends JPanel{

		private static final long serialVersionUID = 1L;
	//una clase de hilos para hacer que se muevan las "particulas"
    private class Movimiento extends Thread{
    	@Override
    	public void run() {
    		while (!currentThread().isInterrupted()) {
    		for (Particula p:particulas) {
    			p.mover();
    		}
    		repaint();
    		try {
				Thread.sleep(16); //para hacer que se mueva a 60 fps
			} catch (InterruptedException e) {
				// TODO: handle exception
			}
    	}
    }
    }
    //IAG la clase particula
    	 private class Particula {
             float x, y, velocidad, tamaño;
             int alpha; // Transparencia (0 a 255)
             
             Particula() { reiniciar(); }
             
             // Coloca la partícula en una posición aleatoria abajo
             void reiniciar() {
                 x = (float) (Math.random() * getWidth());
                 y = getHeight() + (float) (Math.random() * 200); // Empieza fuera de pantalla por abajo
                 velocidad = 0.5f + (float) (Math.random() * 1.5f);
                 tamaño = 20 + (float) (Math.random() * 40);
                 alpha = 30 + (int) (Math.random() * 50); // Transparencia suave
             }
             
             // Mueve la partícula hacia arriba
             void mover() {
                 y -= velocidad;
                 // Si sale por arriba, vuelve a empezar abajo
                 if (y + tamaño < 0) reiniciar();
             }
         }
    	 //creamos lista de particulas (las manchas que se mueven en la pantalla)
    	 private ArrayList<Particula> particulas = new ArrayList<>();
    	 private Movimiento movimiento;
    	 private Image imagenParticula;
    	 public FondoAnimado () {
    		 ImageIcon icono= new ImageIcon("resources/images/logos/logoApp.png");
    		 imagenParticula=icono.getImage();
    		 for (int i =0; i<30; i++) {
    		 particulas.add(new Particula ());
    		 }
    		 movimiento = new Movimiento();
    		 movimiento.start();
    	 }
    	 //IAG
    	 @Override
         protected void paintComponent(Graphics g) {
             super.paintComponent(g);
             Graphics2D g2 = (Graphics2D) g;
             
             // 1. Calidad visual (Antialiasing)
             g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
             //para la imagen
             g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
             // --- AQUÍ ESTÁ EL CAMBIO DEL COLOR ---
             
             // Definimos el degradado vibrante original
             GradientPaint gradiente = new GradientPaint(
                 0, 0, new Color(185, 255, 183),
                 0, getHeight(), new Color(100, 220, 150) 
             );
             
             // Aplicamos la "pintura" degradada y rellenamos el fondo
             g2.setPaint(gradiente);
             g2.fillRect(0, 0, getWidth(), getHeight());

             // -------------------------------------

             // 2. Preparar pincel para las partículas
             // Guardamos la configuración "sólida" para restaurarla luego
             Composite composicionOriginal = g2.getComposite();

             g2.setColor(Color.WHITE);
             try {
                 for (Particula p : particulas) {
                	 g2.drawImage(imagenParticula, (int)p.x, (int)p.y, (int)p.tamaño, (int)p.tamaño, null);
                     // redistribuimos las partículas para que no salgan todas en la esquina.
                     if (p.x == 0 && getWidth() > 0 && Math.random() > 0.90) {
                         p.x = (float)(Math.random() * getWidth());
                     }
                     
                     // Hacemos el pincel transparente según la partícula
                     g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, p.alpha / 255f));
                     
                 }
             } catch (Exception e) {} // Ignorar error visual de hilos
             
             // 3. ¡IMPORTANTE! Restaurar el pincel a modo sólido
             // Si no haces esto, los botones se verán transparentes
             g2.setComposite(composicionOriginal);
         }
    }
}

