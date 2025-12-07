package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.Timer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import db.GestorBD;
import domain.Liga;
import domain.Opcion;
import domain.Pregunta;
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
        
        componentesPanelInicio();
        musica("resources/audios/The_Shire.wav");
        
	}
	
	// Pantalla de inicio del quiz
	private void componentesPanelInicio() {
		List<Usuario> clasificacion = GBD.cargarClasificacion();
		panelQuiz.removeAll();
		contadorQuiz = new HiloQuiz();
		JButton botonInicio = new JButton("Iniciar quiz");
		botonInicio.setFont(new Font("Arial", Font.BOLD, 24));
        botonInicio.setPreferredSize(new Dimension(200, 60));
        botonInicio.setFocusPainted(false);
		botonInicio.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        botonInicio.addActionListener(e -> {
        	this.quizIniciado();
        	contadorQuiz.start();
        	
        });
        JPanel panelClasificacion = new JPanel(new GridLayout(10,2));
        for(Usuario usuario : clasificacion) {
        	panelClasificacion.add(new JLabel(usuario.getNombre()));
        	panelClasificacion.add(new JLabel("" + usuario.getPuntuacion()));
        	
        }
        panelClasificacion.setOpaque(true);
        panelQuiz.add(panelClasificacion, BorderLayout.NORTH);
        panelQuiz.add(botonInicio, BorderLayout.SOUTH);
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
		panelPregunta = new JPanel();
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
			panelPregunta.add(new JLabel(pregunta.getPregunta()));
			//Llama a la funcion para cargar las opciones
			this.añadirRespuestas();
		} catch (Exception e) {
			// TODO: handle exception
			System.err.println("No se ha podido cargar la pregunta de la base de datos: " + e.getMessage());
		}
		
	}
	
	private void añadirRespuestas() {
	    opciones = GBD.cargarOpcionesDePregunta(pregunta);
	    opcionCorrecta = null;
	    labelOpciones = new ArrayList<JLabel>();
	    //Crea el lbl para cada opcion y le da la funcionalidad para que sea clickable
	    for (int i = 0; i < opciones.size(); i++) {
	    	
	        Opcion opcion = opciones.get(i);
	        //Crea un lbl con el contenido de cada opcion
	        JLabel lblOpcion = new JLabel(opcion.getTexto_opcion());
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
	    
	    
	    if (opcionSeleccionada.getEs_correcta() == 1) {
	        pintarOpcionCorrecta(opcionSeleccionada);
	        puntuacion += 1;
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
                	componentesPanelInicio();
                	JFrameQuiz.this.revalidate();
                    JFrameQuiz.this.repaint();
	                JLabel texto = new JLabel("Su puntuacion ha sido de " + puntuacion + " " + "Introduzca su usuario", JLabel.CENTER);
	                String usuario = JOptionPane.showInputDialog(
	                        JFrameQuiz.this,
	                        texto,
	                        "Tiempo finalizado",
	                        JOptionPane.PLAIN_MESSAGE
	                    );
	                añadirPuntuacion(usuario, puntuacion);
	                
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
	
}
