package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.GridBagLayout; 
import java.awt.GridBagConstraints;
import domain.Equipo;
import domain.Liga;

// Se eliminan las importaciones de AbstractCellEditor, TableCellEditor y JButton que ya no se usan.
// Se ha eliminado la clase ComponentCellEditor al final del archivo.

public class JFrameListaEquipos extends JFramePadre {
	private static final long serialVersionUID = 1L;
	private Liga liga;
	private JComboBox <String> comboOrdenar;
	private JTextField filtradoNombre;
	private JTable tablaEquipos;
	private DefaultTableModel modeloDatosEquipos;
	private HashMap<String, ImageIcon> mapaEscudos;
	// private JButton botonEquipo; // Eliminado

	private static final Color COLOR_BORDE_TABLA = new Color(222, 226, 230); // Gris claro para bordes
	private static final Color COLOR_HOVER_FILA = new Color(220, 235, 255); // Azul claro al pasar el ratón
	private static final Color COLOR_TEXTO_ENLACE = new Color(0, 86, 179); // Azul para el texto interactivo
	private static final Font FONT_TITULO = new Font("Arial", Font.BOLD, 28);
	private static final Font FONT_NOMBRE_EQUIPO = new Font("Arial", Font.BOLD, 15);
    
    
    private int columnaSelccionada = -1;

	public JFrameListaEquipos (Liga liga, JFramePadre ventanaAnterior) {
		this.liga = liga;
		super.framePrevio = ventanaAnterior;
		JPanel panel = super.panel;
		panel.setLayout(new BorderLayout());
		panel.setBackground(new Color(248, 249, 250)); // Fondo principal
		//Cargar los escudos una unica vez
		cargarEscudos();
		// 1. TÍTULO SUPERIOR (NORTH - Header Principal)
		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBackground(new Color(152, 217, 194)); // Fondo oscuro para contraste
		
		JLabel titleLabel = new JLabel("Lista de Equipos - " + liga.getNombre());
		titleLabel.setFont(FONT_TITULO);
		titleLabel.setForeground(Color.BLACK);
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titlePanel.add(titleLabel, BorderLayout.CENTER);
		
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(Color.WHITE); 
        topPanel.setBorder(new LineBorder(COLOR_BORDE_TABLA, 1));
		
		JPanel panelFiltro = new JPanel(new GridBagLayout());
		panelFiltro.setBackground(new Color(152, 217, 194));
		//IAG
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets.right = 20; 
		gbc.insets.left = 0;
		gbc.anchor = GridBagConstraints.WEST;
        //Fin IAG
		

		topPanel.add(botonAtras, BorderLayout.WEST);


		filtradoNombre = new JTextField(15);
		filtradoNombre.setToolTipText("Buscar equipo por nombre (Ctrl+F)");
		filtradoNombre.setPreferredSize(new Dimension(200, 35));
		JLabel labelFiltro = new JLabel("    Filtrar: ");
		labelFiltro.setFont(new Font("Arial", Font.PLAIN, 14));
		
		//IAG
		gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
		panelFiltro.add(labelFiltro, gbc);
		gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panelFiltro.add(filtradoNombre, gbc);
		//Fin IAG
		
		// --- Combobox para ordenar ---
		JLabel labelOrdenar = new JLabel("Ordenar por:");
		labelOrdenar.setFont(new Font("Arial", Font.PLAIN, 14));
		String [] opciones= { 
			"Alfabético (asc)","Alfabético (des)","Titulos (asc)","Titulos (des)"
		};
		comboOrdenar=new JComboBox<>(opciones);
		comboOrdenar.setPreferredSize(new Dimension(180, 35));
		
		//IAG
		gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
		panelFiltro.add(labelOrdenar, gbc);
		gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		//Fin IAG
		panelFiltro.add(comboOrdenar, gbc);

		topPanel.add(panelFiltro, BorderLayout.CENTER);
		
		JPanel PanelContenedor = new JPanel(new BorderLayout());
		PanelContenedor.add(titlePanel, BorderLayout.NORTH);
		PanelContenedor.add(topPanel, BorderLayout.CENTER);
		
		panel.add(BorderLayout.NORTH, PanelContenedor);
		
		// Se define el funcionamiento del filtro de texto
		DocumentListener listenerFiltrar = new DocumentListener() {
			@Override 
			public void removeUpdate(DocumentEvent e) { 
				// TODO Auto-generated method stub
				cargarEquiposTablaFiltro(filtradoNombre.getText()); 
				}
			@Override 
			public void insertUpdate(DocumentEvent e) { 
				// TODO Auto-generated method stub
				cargarEquiposTablaFiltro(filtradoNombre.getText());
				}
			@Override 
			public void changedUpdate(DocumentEvent e) { 
				// TODO Auto-generated method stub
				cargarEquiposTablaFiltro(filtradoNombre.getText()); 
				}
		};
		this.filtradoNombre.getDocument().addDocumentListener(listenerFiltrar);
		
		comboOrdenar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cargarEquiposTablaFiltro(filtradoNombre.getText());
			}
		});
		
		usoBotonAtras(super.framePrevio);
		inicializarTabla();
		cargarEquiposTablaFiltro(filtradoNombre.getText());
		
		JScrollPane scrollPaneEquipos = new JScrollPane(this.tablaEquipos);
        scrollPaneEquipos.getViewport().setBackground(new Color(248, 249, 250));
		
		panel.add(BorderLayout.CENTER, scrollPaneEquipos);
		this.add(panel);
		
		KeyStroke ctrlF = KeyStroke.getKeyStroke(KeyEvent.VK_F,KeyEvent.CTRL_DOWN_MASK);
		KeyStroke ctrl1 = KeyStroke.getKeyStroke(KeyEvent.VK_1,KeyEvent.CTRL_DOWN_MASK);
		KeyStroke ctrl2 = KeyStroke.getKeyStroke(KeyEvent.VK_2,KeyEvent.CTRL_DOWN_MASK);
		KeyStroke ctrl3 = KeyStroke.getKeyStroke(KeyEvent.VK_3,KeyEvent.CTRL_DOWN_MASK);
		KeyStroke ctrl4 = KeyStroke.getKeyStroke(KeyEvent.VK_4,KeyEvent.CTRL_DOWN_MASK);
        String accionFoco = "focusFilterAction";
        String accionOrdenAlfabetico_asc = "sortOrdenAlf_asc";
        String accionOrdenAlfabetico_des = "sortOrdenAlf_des";
        String accionOrdenTitulos_asc="sortOrdenTit_asc";
        String accionOrdenTitulos_des="sortOrdenTit_des";

        
        Action accion = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) { filtradoNombre.requestFocusInWindow(); }
        };
        
       
        Action accionAlf_asc= new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) { 
				comboOrdenar.setSelectedItem("Alfabético (asc)"); }
        };
        Action accionAlf_des= new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) { 
				comboOrdenar.setSelectedItem("Alfabético (des)"); }
        };
        Action accionTit_asc= new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) { 
				comboOrdenar.setSelectedItem("Titulos (asc)"); }
        };
        Action accionTit_des= new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) { 
				comboOrdenar.setSelectedItem("Titulos (des)"); }
        };
        
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrlF, accionFoco);
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrl1, accionOrdenAlfabetico_asc);
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrl2, accionOrdenAlfabetico_des);
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrl3, accionOrdenTitulos_asc);
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrl4, accionOrdenTitulos_des);
        
        panel.getActionMap().put(accionFoco, accion);
        panel.getActionMap().put(accionOrdenAlfabetico_asc, accionAlf_asc);
        panel.getActionMap().put(accionOrdenAlfabetico_des, accionAlf_des);
        panel.getActionMap().put(accionOrdenTitulos_asc, accionTit_asc);
        panel.getActionMap().put(accionOrdenTitulos_des, accionTit_des);
    
        
        this.tablaEquipos.addMouseMotionListener(mousemotlist);
        this.tablaEquipos.addMouseListener(mouseClicListener); 
	}
	
	private void inicializarTabla() {
		Vector<String> cabezeraEquipos = new Vector<String>(Arrays.asList("ESCUDO", "NOMBRE", "TÍTULOS", "AÑO FUND."));
		modeloDatosEquipos = new DefaultTableModel(new Vector<Vector<Object>>(), cabezeraEquipos);
		
		this.tablaEquipos = new JTable(this.modeloDatosEquipos) {
            
			@Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);

                if (isRowSelected(row) || row == columnaSelccionada){
                	
                    c.setBackground(COLOR_HOVER_FILA); 
                    c.setForeground(Color.BLACK);
                } else if (row % 2 == 0) {
                    c.setBackground(new Color(171, 237, 198)); 
                    c.setForeground(Color.BLACK);
                } else if (row % 2 == 1) {
                    c.setBackground(new Color(171, 237, 198)); 
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(248, 249, 250)); 
                    c.setForeground(Color.BLACK);
                }
                
                return c;
            }
            
			@Override
			public boolean isCellEditable(int row, int column){
				return false;
			}
			
		};
		

		this.tablaEquipos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
		this.tablaEquipos.getTableHeader().setBackground(new Color(152, 217, 194));
		this.tablaEquipos.getTableHeader().setForeground(Color.BLACK);
		this.tablaEquipos.getTableHeader().setReorderingAllowed(false);
		this.tablaEquipos.setShowGrid(false);
		

		TableCellRenderer cellRenderer = (table, value, isSelected, hasFocus, row, column) -> 	{
		    
		    if (column == 0) { // ESCUDO
		        JLabel result = new JLabel();
		    
		        ImageIcon escudo = mapaEscudos.get(value.toString());
		        
		        result.setOpaque(true);
		        
		        try {
		            result.setIcon(escudo);
		        } catch (Exception e) {
		            System.err.println("No se ha encontrado el escudo del equipo: "+result.getText());
		        }
		        
		        result.setText(null);	
		        result.setHorizontalAlignment(JLabel.CENTER);
		        return result;
		    } else if (column == 1) { // NOMBRE (Texto interactivo/Enlace)
		        JLabel result = new JLabel(value.toString());
		        result.setFont(FONT_NOMBRE_EQUIPO);
		        result.setHorizontalAlignment(JLabel.LEFT);
		        result.setBorder(new EmptyBorder(0, 15, 0, 0)); 
		        result.setOpaque(true);
		        
		        // Simular un enlace web
		        if (!isSelected && row != columnaSelccionada) {
		            result.setForeground(COLOR_TEXTO_ENLACE); 
		        } else {
		             result.setForeground(Color.BLACK); 
		        }

		        return result;
		    } else if (column == 2){ // TÍTULOS
		        JLabel result1 = new JLabel(value.toString(), JLabel.CENTER);
		        result1.setOpaque(true);
		        result1.setFont(new Font("Arial", Font.BOLD, 16));
		        return result1;
		    } else if (column == 3) {//Año fundacion
		    	JLabel result = new JLabel(value.toString(), JLabel.CENTER);
		        result.setOpaque(true);
		        result.setFont(new Font("Arial", Font.BOLD, 16));
		        return result;
		    }
		    
		    return new JLabel(value != null ? value.toString() : "");
		};

		this.tablaEquipos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		// Establecer el cellRenderer como Render por defecto
		this.tablaEquipos.setDefaultRenderer(Object.class, cellRenderer);
        
        // Estilo de las líneas de la tabla
        this.tablaEquipos.setIntercellSpacing(new Dimension(0, 0)); 
        this.tablaEquipos.setBorder(new LineBorder(COLOR_BORDE_TABLA));
		
		// Configuración de anchos de columna
		TableColumn columnaEscudo = tablaEquipos.getColumnModel().getColumn(0);
		columnaEscudo.setPreferredWidth(70);
		
		TableColumn columnaTitulos = tablaEquipos.getColumnModel().getColumn(2);
		columnaTitulos.setPreferredWidth(90); 
		
		TableColumn columnaAnyo = tablaEquipos.getColumnModel().getColumn(3);
		columnaAnyo.setPreferredWidth(90); 
		
		// El ancho de la columna del Nombre será dinámico
		TableColumn columnaNombre = tablaEquipos.getColumnModel().getColumn(1);
		columnaNombre.setPreferredWidth(this.getWidth() - 283); // Ajustado para un ancho razonable

		// Se establece la altura de la columna
		this.tablaEquipos.setRowHeight(50); 
		this.tablaEquipos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	// Listener para manejar el cursor de mano y el efecto de hover
	MouseMotionListener mousemotlist = new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			int row = tablaEquipos.rowAtPoint(e.getPoint());
			int col = tablaEquipos.columnAtPoint(e.getPoint());

			// Efecto hover (para todo el ancho de la fila)
			if (row != columnaSelccionada) {
				columnaSelccionada = row;
				tablaEquipos.repaint(); // Redibujar para aplicar color de hover
			}
			
	        if (col == 1) { // Cursor de mano solo en la columna del NOMBRE
	            tablaEquipos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	        } else {
	            tablaEquipos.setCursor(Cursor.getDefaultCursor());
	        }
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {}
	};
    
    // Listener para manejar el evento de clic y deshabilitar el hover cuando el ratón sale
    MouseListener mouseClicListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            int row = tablaEquipos.rowAtPoint(e.getPoint());
	        int col = tablaEquipos.columnAtPoint(e.getPoint());

	        // DOBLE CLIC en cualquier parte de la fila o CLIC en el nombre (columna 1)
	        if ((e.getClickCount() == 2 && row != -1) || (e.getClickCount() == 1 && col == 1 && row != -1)) { 
	            abrirVentanaEquipo(row);
	        }
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
        	// TODO Auto-generated method stub
             if (columnaSelccionada != -1) {
            	 columnaSelccionada = -1;
                tablaEquipos.repaint(); // Redibujar para quitar el color de hover
            }
        }

        @Override public void mousePressed(MouseEvent e) {
        	// TODO Auto-generated method stub
        }
        @Override public void mouseReleased(MouseEvent e) {
        	// TODO Auto-generated method stub
        }
        @Override public void mouseEntered(MouseEvent e) {
        	// TODO Auto-generated method stub
        }

        private void abrirVentanaEquipo(int row) {
			String nombreEquipo = (String) tablaEquipos.getValueAt(row, 1);
			Equipo equipoSeleccionado = null;
			
			for (Equipo eq : liga.getEquipos()) {
				if (eq.getNombre().equals(nombreEquipo)) {
					equipoSeleccionado = eq;
					break;
				}
			}
			
			if (equipoSeleccionado != null) {
				dispose();
				JFrameEquipo jfe = new JFrameEquipo(equipoSeleccionado, JFrameListaEquipos.this);
				jfe.setVisible(true);
			} else {
				System.err.println("Error: No se encontró el objeto Equipo para el nombre: " + nombreEquipo);
			}
		}
    };
	//IAG
	// Función que modifica el tamaño de la imagen a la altura de la celda
	private ImageIcon escalarIcono(ImageIcon icon, int targetHeight) {
	    if (icon == null || icon.getImage() == null || icon.getIconHeight() <= 0) {
	        return null; 
	    }

	    if (icon.getIconHeight() == targetHeight) {
	        return icon; 
	    }

	    Image originalImage = icon.getImage();
	    
	    Image scaledImage = originalImage.getScaledInstance(-1, targetHeight, Image.SCALE_SMOOTH);

	    return new ImageIcon(scaledImage);
	}
	
	// Cargar los equipos teniendo en cuenta el contenido en el JTextField filtradoNombre
	private void cargarEquiposTablaFiltro (String filtro) {
		List<Equipo> equiposOrdenados = new ArrayList<>(this.liga.getEquipos());
		String orden= (String) comboOrdenar.getSelectedItem();
		
		// Lógica de ordenación
		if (orden != null && orden.equals("Titulos (asc)")){
			equiposOrdenados.sort(Comparator.comparingInt(Equipo::getTitulos));//IAG esta line, las demas, teniendo esta en cuenta
			
		} else if (orden != null && orden.equals("Titulos (des)")) {
			equiposOrdenados.sort(Comparator.comparingInt(Equipo::getTitulos).reversed());
		} else if (orden != null && orden.equals("Alfabético (des)")) {
			equiposOrdenados.sort(Comparator.comparing(Equipo::getNombre).reversed());
			
		} else {
			equiposOrdenados.sort(Comparator.comparing(Equipo::getNombre));
		} 
		
		modeloDatosEquipos.setRowCount(0);
		for (Equipo eq: equiposOrdenados) {
			if (eq.getNombre().toLowerCase().contains(filtro.toLowerCase())) {
				this.modeloDatosEquipos.addRow(new Object[] {eq.getNombrePNGEquipo(), eq.getNombre(), eq.getTitulos(), eq.getAnyoFundacion()});
			}
		}
	}
	//Funcion que carga los escudos en un HashMap para mejorar el rendimiento
	private void cargarEscudos () {
		mapaEscudos = new HashMap<String, ImageIcon>();
		for (Equipo e : liga.getEquipos()) {
			ImageIcon escudo = new ImageIcon("resources/images/equipos/"+liga.getNombre()+"/"+e.getNombrePNGEquipo()+".png");
			ImageIcon escudoAjustado = new ImageIcon(escudo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
			mapaEscudos.put(e.getNombrePNGEquipo(), escudoAjustado);
		}
	}
}
