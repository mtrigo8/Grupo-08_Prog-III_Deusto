package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

import domain.Liga;

public class JFrameInicio extends JFramePadre {
	private ArrayList<Liga> ligas;

    private static final long serialVersionUID = 1L;

    private JLabel titulo;
    private JButton btnEntrar;

    public JFrameInicio(ArrayList<Liga> ligas) {
        super();
        super.framePrevio = null;
        this.ligas = ligas;
        usoBotonAtras(super.framePrevio);

        JPanel panel = super.panel;
        panel.setLayout(null);

        // --- Crear Título ---
        titulo = new JLabel("Bienvenido a FutGoat", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(new Color(33, 33, 33));
        titulo.setOpaque(false);
        panel.add(titulo);

        // --- Crear Botón Entrar ---
        btnEntrar = new JButton("Entrar");
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 20));
        btnEntrar.setBackground(new Color(0, 74, 153));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setBorderPainted(false);
        
        panel.add(btnEntrar);

        this.add(panel);

        // Acción del botón
        btnEntrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrameSeleccionarLigas jfs = new JFrameSeleccionarLigas(ligas , JFrameInicio.this);
                setVisible(false);
                jfs.setVisible(true);
            }
        });
        super.botonAtras.setVisible(false); //ya que es la ventana principal el boton de atras no es util poeque no cierra la app
        // --- Posicionar componentes inicialmente ---
        posicionarComponentes();
    }

    //Funcion creada con ayuda de ChatGPT para arreglar un error en el posicionamiento de los componentes al ejecutar
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

        int xBoton = (ancho - anchoBoton) / 2;
        int yBoton = (int) (alto * 0.78);

        // Asignar posiciones
        titulo.setBounds(xTitulo, yTitulo, anchoTitulo, altoTitulo);
        btnEntrar.setBounds(xBoton, yBoton, anchoBoton, altoBoton);
    }

}


	



