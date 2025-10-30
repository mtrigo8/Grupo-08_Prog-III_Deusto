package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.Equipo;
import domain.Jugador;
import domain.Jugador.TipoPosicion;
import domain.Liga;

public class JFrameJugador extends JFramePadre {

	
	private static final long serialVersionUID = 1L;
	private Jugador jugador;
	private Liga liga;
	private JLabel labelNumero;
	private JLabel labelNombre;

	private JPanel panelAtributos;//las estadisticas, edad o lo que sea
	private JPanel panelNombre;//irá arriba como lo mas importante
	
	public JFrameJugador(ArrayList<Liga> ligas, Liga liga, Jugador jugador) {
		super();
		this.jugador = jugador;
		this.ligas = ligas; 
		this.liga = liga;
		JPanel panel = super.panel;
		setImagenDeFondo(null);
		
		Color coloFondo = new Color (33,33,33);
		Color colorLetra = Color.WHITE;
		panel.setBackground(coloFondo);
		panelNombre = new JPanel();
		panelNombre.setOpaque(false);
		
		labelNumero = new JLabel(" #" + jugador.getNumeroCamiseta());
		
		labelNumero.setFont(new Font("Arial",Font.BOLD,36));
		labelNumero.setForeground(colorLetra);
		
		labelNombre = new JLabel(jugador.getNombre());
		labelNombre.setFont(new Font("Arial", Font.BOLD, 40));
		labelNombre.setForeground(colorLetra);
		panelNombre.add(labelNombre);
		panelNombre.add(labelNumero);
		
		panel.add(panelNombre);
		panelAtributos = new JPanel(new GridLayout(4,2,10,10));
		panelAtributos.setOpaque(false);
		
		//el label para poner posicion:
		JLabel labelPosicion = new JLabel("Posición: ");
		labelPosicion.setFont(new Font("Arial", Font.BOLD, 22));
		labelPosicion.setForeground(colorLetra);
		labelPosicion.setHorizontalAlignment(JLabel.RIGHT);
		panelAtributos.add(labelPosicion);
		
		//label para poner al lado de "posicion:"
		JLabel numeroPosicion = new JLabel(jugador.getPosicion().toString());
		numeroPosicion.setFont(new Font("Arial", Font.PLAIN, 22));
		numeroPosicion.setForeground(colorLetra);
		numeroPosicion.setHorizontalAlignment(JLabel.LEFT);
		panelAtributos.add(numeroPosicion);
		
		//label "equipo:"
		JLabel labelEquipo = new JLabel("Equipo: ");
		labelEquipo.setFont(new Font("Arial", Font.BOLD, 22));
		labelEquipo.setForeground(colorLetra);
		labelEquipo.setHorizontalAlignment(JLabel.RIGHT);
		panelAtributos.add(labelEquipo);

		JLabel nombreEquipo=new JLabel(jugador.getEquipo().getNombre());
		nombreEquipo.setFont(new Font("Arial", Font.PLAIN, 22));
		nombreEquipo.setForeground(colorLetra);
		nombreEquipo.setHorizontalAlignment(JLabel.LEFT);
		panelAtributos.add(nombreEquipo);
		
		JLabel labelNacionalidad= new JLabel("Nacionalidad: ");
		labelNacionalidad.setFont(new Font("Arial", Font.BOLD, 22));
		labelNacionalidad.setForeground(colorLetra);
		labelNacionalidad.setHorizontalAlignment(JLabel.RIGHT);
		panelAtributos.add(labelNacionalidad);
		
		JLabel nombreNacionalidad=new JLabel(jugador.getNacionalidad());
		nombreNacionalidad.setFont(new Font("Arial", Font.PLAIN, 22));
		nombreNacionalidad.setForeground(colorLetra);
		nombreNacionalidad.setHorizontalAlignment(JLabel.LEFT);
		panelAtributos.add(nombreNacionalidad);
		
		JLabel labelEdad= new JLabel("Edad:");
		labelEdad.setFont(new Font("Arial", Font.BOLD, 22));
		labelEdad.setForeground(colorLetra);
		labelEdad.setHorizontalAlignment(JLabel.RIGHT);
		panelAtributos.add(labelEdad);
		
		JLabel numeroEdad=new JLabel(String.valueOf(jugador.getEdad()));
		numeroEdad.setFont(new Font("Arial", Font.PLAIN, 22));
		numeroEdad.setForeground(colorLetra);
		numeroEdad.setHorizontalAlignment(JLabel.LEFT);
		panelAtributos.add(numeroEdad);
		panel.add(panelAtributos);
		this.setContentPane(panel);
		posicionarComponentes();
		
	}
	private void posicionarComponentes() {
		int anchoVentana = getWidth();
		int altoNombre=60;
		int anchoNombre=800;
		int xNombre=(anchoVentana-anchoNombre)/2;
		int yNombre = 100;
		panelNombre.setBounds(xNombre, yNombre, anchoNombre, altoNombre);
		
		int anchoAtributos = 600;
		int altoAtributos = 200;
		int xAtributos = (anchoVentana - anchoAtributos) / 2; // Centrado
		int yAtributos = 250;
		
		panelAtributos.setBounds(xAtributos, yAtributos, anchoAtributos, altoAtributos);

	}
	@Override
	public void usoBotonAtras(ArrayList<Liga> ligas, Liga liga) {
		// TODO Auto-generated method stub
		
	}
	//credo por ia para ver si funciona o no la ventana independientemente
public static void main(String[] args) {
		
		// 1. --- Simular los datos del Main.java ---
		TipoPosicion posPrueba = TipoPosicion.DELANTERO; 
		
		// Usar el constructor completo de Equipo
		Equipo equipoPrueba = new Equipo("Manchester United", "Manchester", null, 1878, 20, "Old Trafford","manchesterunited");
		
		// Usar el constructor completo de Jugador
		// (¡Asegúrate de que este constructor existe en tu clase Jugador!)
		Jugador jugadorPrueba = new Jugador(
			"Marcus Rashford", 10, posPrueba, equipoPrueba, "Inglaterra", 27
		);
		
		// Usar el constructor completo de Liga
		ArrayList<Equipo> equiposPrueba = new ArrayList<>();
		equiposPrueba.add(equipoPrueba);
		Liga ligaPrueba = new Liga("Premier", "Inglaterra", 20, equiposPrueba);
		
		ArrayList<Liga> todasLasLigas = new ArrayList<>();
		todasLasLigas.add(ligaPrueba);

		// 2. --- Ejecutar la ventana ---
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrameJugador ventanaPrueba = new JFrameJugador(
					todasLasLigas, ligaPrueba, jugadorPrueba
				);
				ventanaPrueba.setVisible(true);
			}
		});
	}

}
