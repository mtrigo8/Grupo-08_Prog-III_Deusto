package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import db.GestorBD;
import domain.Liga;
import domain.Opcion;
import domain.Pregunta;
import domain.Pregunta.Dificultad;
import domain.Usuario;


public class JFrameQuiz extends JFramePadre{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList <Liga> ligas;
	private JPanel panelQuiz;
	private HiloQuiz contadorQuiz = new HiloQuiz(); //Creacion del hilo
	//Tiempo
	private int TIEMPO = 60;
	private JPanel tiempo;
	private JLabel lblTiempo;
	private JPanel panelTiempoYPuntos;
	private boolean tiempoCumplido;
	private Timer delayTimer;
	//Puntuacion
	private JPanel panelPuntuacion;
	private JLabel lblPuntuacion;
	private int puntuacion;
	//Preguntas
	private JPanel panelPregunta;
	private Set<Pregunta> preguntasUsadas; //Asegura que las preguntas del quiz seran todas diferentes
	private Pregunta pregunta;
	private JPanel panelPreguntaYRespuesta;
	//Respuestas
	private JPanel panelRespuestas;
	private List<Opcion> opciones;
	private List<JLabel> labelOpciones;
	private Opcion opcionCorrecta;
	//Gestor de base de datos
	private GestorBD GBD;
	private Clip clip;
	private boolean respondido=false;
	//Thead tiempo por pregunta
	private int TIEMPO_MAX_RESPUESTA = 10; //10 segundos
	private JProgressBar barraTiempo;
	
	
	public JFrameQuiz (ArrayList<Liga> ligas, JFramePadre frameP) {
		super();
		super.framePrevio = frameP;
		this.ligas = ligas;
		usoBotonAtras(super.framePrevio);
		botonAtras.addActionListener(e->{
			if (clip !=null) {
				clip.stop();
			}
		});
	
		//Inicializar el Gestor BD
		GBD = new GestorBD();
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
     //Crear la barra de progreso
        barraTiempo = new JProgressBar(0, 100);
        barraTiempo.setValue(0);
        barraTiempo.setStringPainted(true);
        componentesPanelInicio();
        musica("resources/audios/The_Shire.wav");
        
	}
	
	// Pantalla de inicio del quiz
	private void componentesPanelInicio() {
		
		panelQuiz.removeAll();
		JLabel lblTitulo= new JLabel ("TABLA DE CLASIFICACIÓN");
		lblTitulo.setFont ( new Font("SansSerif", Font.BOLD, 24));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		String [] col= {"POS","USUARIO","PUNTOS"};
		JPanel panelgeneral= new JPanel(); 
	    panelgeneral.setBackground(Color.WHITE);
	    panelgeneral.setLayout(new BorderLayout(10, 10));
	    panelQuiz.setLayout(new BorderLayout());
		TableCellRenderer cellrenderer = new TableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
			JLabel result = new JLabel(value.toString());
			result.setOpaque(true);
			result.setHorizontalAlignment(SwingConstants.CENTER); 
			result.setFont(new Font("SansSerif", Font.PLAIN, 14));			
			if (row % 2 == 0) {
					result.setBackground(Color.WHITE);
				} else {
					result.setBackground(new Color(245, 255, 245));
				}		
			if (column == 0 && row!=1) {

			result.setText((row + 1) + "º");}
			result.setOpaque(true);
			result.setHorizontalAlignment(SwingConstants.CENTER);
			result.setFont(new Font("SansSerif", Font.PLAIN, 14));
			

			if (row % 2 == 0) {
				result.setBackground(Color.WHITE);
			} else {
				result.setBackground(new Color(245, 255, 245));
			}

			if (column == 0) { 
				result.setFont(new Font("SansSerif", Font.BOLD, 14));
				
				if (row == 0) {
						result.setText("");
						ImageIcon imagen = new ImageIcon("resources/images/logos/trofeo.png");
						Image imgEscalada = imagen.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
						result.setIcon(new ImageIcon(imgEscalada));
						result.setHorizontalTextPosition(SwingConstants.CENTER);
						
				}
				
				else if (row == 1) {
					result.setText("");
					ImageIcon imagen = new ImageIcon("resources/images/logos/plata.png");
					Image imgEscalada = imagen.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
					result.setIcon(new ImageIcon(imgEscalada));
					result.setHorizontalTextPosition(SwingConstants.CENTER);
				}
				else if (row == 2) {
					result.setText("");
					ImageIcon imagen = new ImageIcon("resources/images/logos/broncemedalla.png");
					Image imgEscalada = imagen.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
					result.setIcon(new ImageIcon(imgEscalada));
					result.setHorizontalTextPosition(SwingConstants.CENTER);
				}
				
			} else {
				result.setForeground(Color.BLACK);
			}
			
			return result;
			}
		};
		
		DefaultTableModel Tablamodelo = new DefaultTableModel(col, 0) {
	        @Override
	        public boolean isCellEditable(int row, int column) {
	            return false;
	        }
	    };
		List<Usuario> clasifi= GBD.cargarClasificacion();
		for (Usuario u : clasifi) {
			Tablamodelo.addRow(new Object[] { 
				"",          
				u.getNombre(),
				u.getPuntuacion()
			});
		}
		JTable tabla = new JTable(Tablamodelo);
		for (int i = 0; i < tabla.getColumnCount(); i++) {
			tabla.getColumnModel().getColumn(i).setCellRenderer(cellrenderer);
		}
		tabla.setRowHeight(40);
		tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
		tabla.getTableHeader().setBackground(new Color(185, 255, 183));
		JButton btnInicio=new BotonCircular("Iniciar Quiz",Color.gray,Color.DARK_GRAY);
		btnInicio.setForeground(Color.WHITE);
		btnInicio.setPreferredSize(new Dimension(230, 70));
		btnInicio.addActionListener(e -> {
			this.quizIniciado();
			contadorQuiz = new HiloQuiz();
			contadorQuiz.start();
		});
		
		
		JScrollPane scrollPane = new JScrollPane(tabla);
		panelgeneral.add(lblTitulo, BorderLayout.NORTH);
	    panelgeneral.add(scrollPane, BorderLayout.CENTER);
	    
	    JPanel panelContenedor = new JPanel(new FlowLayout());
	    panelContenedor.setBackground(Color.WHITE);
	    panelContenedor.setOpaque(false); 
	    panelContenedor.add(btnInicio);
	    panelgeneral.add(panelContenedor, BorderLayout.SOUTH);
	  
	    panelQuiz.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    
	    panelQuiz.add(panelgeneral, BorderLayout.CENTER);
		panelQuiz.revalidate();
		panelQuiz.repaint();
	}
	// Pantalla del juego
	private void quizIniciado() {
		panelQuiz.removeAll();
		puntuacion = 0;
		//Reiniciar el Set de preguntas usadas
		preguntasUsadas = new HashSet<Pregunta>();
		//Crear la estructura base del Quiz (Paneles contenedores)
		panelTiempoYPuntos = new JPanel(new GridLayout(1, 2, 15, 15));
		panelPreguntaYRespuesta = new JPanel(new GridLayout(2, 1, 10, 30));
		
		//Añadir el panel del tiempo
		tiempo = new JPanel();
		lblTiempo = new JLabel();
		tiempo.add(lblTiempo);
		panelTiempoYPuntos.add(tiempo);
		//Añadir panel de puntos
		panelPuntuacion = new JPanel(new FlowLayout());
		panelPuntuacion.add(new JLabel  ("PUNTUACION: "));
		
		lblPuntuacion = new JLabel("0");
		panelPuntuacion.add(lblPuntuacion);
		
		panelTiempoYPuntos.add(panelPuntuacion);
		
		panelQuiz.add(panelTiempoYPuntos, BorderLayout.NORTH);
		
		//Crear el panel de la pregunta
		panelPregunta = new JPanel(new GridBagLayout());
		panelPregunta.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panelPreguntaYRespuesta.add(panelPregunta);
		panelPregunta.setBackground(Color.WHITE);
		//Crear panel de las respuestas
		panelRespuestas = new JPanel(new GridLayout(2, 2, 15, 15));
		panelRespuestas.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		panelRespuestas.setBackground(Color.WHITE);
		
		
		
		panelPreguntaYRespuesta.add(panelRespuestas, pregunta);
		
		panelQuiz.add(panelPreguntaYRespuesta);
		
		this.añadirPregunta();
	}
	
	private void añadirPregunta () {
		// Cargar la pregunta de la BBDD
		this.pregunta = GBD.cargarPreguntaAleatoria(preguntasUsadas);
		try {
			//Añadir la pregunta a las preguntas usadas
			this.preguntasUsadas.add(pregunta);
			//Se añade el la pregunta al panel
			JLabel lblPregunta = new JLabel(pregunta.getPregunta());
			
			panelPregunta.add(lblPregunta);
			
			
			//Llama a la funcion para cargar las opciones
			this.añadirRespuestas();
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("No se ha podido cargar la pregunta de la base de datos: " + e.getMessage());
		}
		
	}
	
	private void añadirRespuestas() {
		this.respondido=false;
	    opciones = GBD.cargarOpcionesDePregunta(pregunta);
	    Collections.shuffle(opciones);//para mover las opciones
	    opcionCorrecta = null;
	    labelOpciones = new ArrayList<JLabel>();
	    //Crea el lbl para cada opcion y le da la funcionalidad para que sea clickable
	    for (int i = 0; i < opciones.size(); i++) {
	    	
	        Opcion opcion = opciones.get(i);
	        //Crea un lbl con el contenido de cada opcion
	        JLabel lblOpcion = new JLabel(opcion.getTexto_opcion(), SwingConstants.CENTER);
	        labelOpciones.add(lblOpcion);
	        //Conseguir la opcion correcta de la pregunta
	        if (opcion.getEs_correcta() == 1) {
	        	opcionCorrecta = opcion;
	        }
	        
	        
	        // Hacer que parezca clicable
	        lblOpcion.setCursor(new Cursor(Cursor.HAND_CURSOR));
	        lblOpcion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	        
	        lblOpcion.addMouseListener(new MouseAdapter() {
	        	//Si se clicka verifica la opcion si es correcta
	        	@Override
	            public void mouseClicked(MouseEvent e) {
	                verificarRespuesta(opcion);
	            }
	            //Modifica color del label si el raton esta encima
	            @Override
	            public void mouseEntered(MouseEvent e) {
	                lblOpcion.setBackground(Color.LIGHT_GRAY);
	                lblOpcion.setOpaque(true);
	            }
	            
	            //Modifica el color del label 
	            @Override
	            public void mouseExited(MouseEvent e) {
	                lblOpcion.setBackground(Color.WHITE);
	                
	            }
	            
	        });
	        
	        panelRespuestas.add(lblOpcion);
	    }
	}
	
	//Verifica si la pregunta seleccionada es correcta
	private void verificarRespuesta(Opcion opcionSeleccionada) {
	    if (respondido==true) {
	    	return;
	    }
	    respondido =true;
	    if (opcionSeleccionada.getEs_correcta() == 1) {
	        pintarOpcionCorrecta(opcionSeleccionada);
	        puntuacion += calcularPuntuacion(pregunta.getDificultad());
	        System.out.println(pregunta.getDificultad());
	        lblPuntuacion.setText("" + puntuacion);
	        
	    } else {
	    	pintarOpcionIncorrecta(opcionSeleccionada, opcionCorrecta);	
	    }
	    
	   
	 // Detener el timer anterior si existe
	    if (delayTimer != null && delayTimer.isRunning()) {
	        delayTimer.stop();
	    }
	    
	    // Esperar medio segundo (500ms) antes de cargar la siguiente pregunta
	    delayTimer = new javax.swing.Timer(500, e -> {
	        panelPregunta.removeAll();
	        panelRespuestas.removeAll();
	        // Cargar nueva pregunta
	        añadirPregunta();
	        panelQuiz.revalidate();
	        panelQuiz.repaint();
	    });
	    delayTimer.setRepeats(false);
	    delayTimer.start();
	}
	//Colorea el label de color verde claro si la respuesta ha sido correcta
	private void pintarOpcionCorrecta(Opcion opcionSeleccionada) {
		for (JLabel label : this.labelOpciones) {
			if (label.getText().equals(opcionSeleccionada.getTexto_opcion())) {
				label.setBackground(new Color(144, 238, 144));
				label.setBorder(BorderFactory.createCompoundBorder(
	                    BorderFactory.createLineBorder(new Color(34, 139, 34), 3),
	                    BorderFactory.createEmptyBorder(15, 15, 15, 15)));
				break;
			}
		}
	}
	//Colorea la opcion seleccionada de rojo y la opcion correcta de verde
	private void pintarOpcionIncorrecta (Opcion opcionSeleccionada, Opcion opcionCorrecta) {
		for (JLabel label : this.labelOpciones) {
			if (label.getText().equals(opcionSeleccionada.getTexto_opcion())) {
				label.setBackground(new Color(255, 182, 193));
				label.setBorder(BorderFactory.createCompoundBorder(
	                    BorderFactory.createLineBorder(new Color(220, 20, 60), 3),
	                    BorderFactory.createEmptyBorder(15, 15, 15, 15)));
			}else if(label.getText().equals(opcionCorrecta.getTexto_opcion())){
				label.setBackground((new Color(144, 238, 144)));
				label.setBorder(BorderFactory.createCompoundBorder(
	                    BorderFactory.createLineBorder(new Color(34, 139, 34), 3),
	                    BorderFactory.createEmptyBorder(15, 15, 15, 15)));
				
			}
		}
	}
	public void añadirPuntuacion(String nom_usuario, int puntuacion) {
		try {
           Usuario usuario = new Usuario(nom_usuario, puntuacion);
            
            // 2. Llamar al método del GestorBD
            JFrameQuiz.this.GBD.insertarUsuario(usuario);
            JFrameQuiz.this.GBD.storeCSVUsuario(usuario);
            JOptionPane.showMessageDialog(JFrameQuiz.this, "Puntuación guardada con éxito.");
            
        } catch (Exception e) {
        	System.err.println("No se ha podido guardar la puntuacion de la base de datos: " + e.getMessage());
        }
    
    }
	
	public void actualizarTiempo(int tiempoRestante) {
		if(tiempoRestante == 5) {
			lblTiempo.setForeground(Color.RED);
			
		}
		int minutos = (tiempoRestante % 3600) / 60;
		int segundos = tiempoRestante % 60;
		
		LocalTime tiempo = LocalTime.of(0, minutos, segundos);
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("mm:ss");
		lblTiempo.setText("Tiempo restante: " + formato.format(tiempo));
	}
	private class HiloQuiz extends Thread {
        @Override
        public void run() {
        	tiempoCumplido = false;
        	actualizarTiempo(TIEMPO);
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
                
                tiempoCumplido = true;
                if(tiempoCumplido && !this.isInterrupted()) {
                	SwingUtilities.invokeLater(() -> {
	                JLabel texto = new JLabel("Su puntuacion ha sido de " + puntuacion + " " + "Introduzca su usuario", JLabel.CENTER);
	                String usuario = JOptionPane.showInputDialog(
	                        JFrameQuiz.this,
	                        texto,
	                        "Tiempo finalizado",
	                        JOptionPane.PLAIN_MESSAGE
	                    );
	             añadirPuntuacion(usuario, puntuacion);
	                
                componentesPanelInicio();
                JFrameQuiz.this.revalidate();
                JFrameQuiz.this.repaint();
                });
                
            }
        this.interrupt();
        }
	}
	private void musica (String ruta) {
		try {
            File archivo = new File(ruta);
            
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(archivo);
                clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.loop(Clip.LOOP_CONTINUOUSLY); 
                clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	@Override
	public void usoBotonAtras(JFramePadre frameAnterior) { //Implemnta en cada clase hija su uso del boton atras
		botonAtras.addActionListener(e -> {
			contadorQuiz.interrupt();
			setVisible(false);
			frameAnterior.setVisible(true);
	
		});
		botonAtras.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
	private int calcularPuntuacion(Dificultad d) {
		puntuacion = d.getPuntuacionMaxima();
		return puntuacion;
	}
	Thread contador;
	
}
	
