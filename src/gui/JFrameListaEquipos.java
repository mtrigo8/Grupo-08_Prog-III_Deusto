package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EventObject;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import domain.Equipo;
import domain.Liga;

public class JFrameListaEquipos extends JFramePadre {
	private static final long serialVersionUID = 1L;
	private Liga liga;
	private JComboBox <String> comboOrdenar;
	private JTextField filtradoNombre;
	private JTable tablaEquipos;
	private DefaultTableModel modeloDatosEquipos;
	private JButton botonEquipo;
	private Random random= new Random();
	public JFrameListaEquipos (Liga liga, JFramePadre ventanaAnterior) {
		this.liga = liga;
		super.framePrevio = ventanaAnterior;
		JPanel panel = super.panel;
		panel.setLayout(new BorderLayout());
		this.liga = liga;
		//Crear Barra buscadora
		filtradoNombre = new JTextField(20);
		JPanel panelFiltro = new JPanel();
		//Panel donde aparece la barra buscadora
		panelFiltro.add(new JLabel("Filtrado por nombre: "));
		panelFiltro.add(filtradoNombre);
		
		//El combobox para ordenar la lista de equipos al gusto de cada uno
		panelFiltro.add(new JLabel("Ordenar por:"));
		String [] opciones= { "Alfabético (asc)","Alfabético (des)","Titulos (asc)","Titulos (des)"};
		comboOrdenar=new JComboBox<>(opciones);
		panelFiltro.add(comboOrdenar);
		//Crear un panel arriba para añadir el boton y el filtrado por texto
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.add(panelFiltro);
		topPanel.add(botonAtras, BorderLayout.WEST);
		//Se define el funcionamiento del filtro de texto
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
		//Aplicar listener
		this.filtradoNombre.getDocument().addDocumentListener(listenerFiltrar);
		
		comboOrdenar.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cargarEquiposTablaFiltro(filtradoNombre.getText());
			}
		});
		
		//Añadir panel y boton 
		panel.add(BorderLayout.NORTH, topPanel);
		
		//Dar funcionalidad a botonAtras
		usoBotonAtras(super.framePrevio);
		//Inicializar las tablas con los equipos
		inicializarTabla();
		cargarEquiposTablaFiltro(filtradoNombre.getText());
		//Insertar la tabla de equipos en un panel con scroll
		JScrollPane scrollPaneEquipos = new JScrollPane(this.tablaEquipos);
		scrollPaneEquipos.setBorder(new TitledBorder("Equipos"));
		this.tablaEquipos.setFillsViewportHeight(true);
		
		panel.add(BorderLayout.CENTER, scrollPaneEquipos);
		this.add(panel);
		
		//IAG para todos los pasos del primer boton, los 5 pasos : 1. Definimos la combinación de teclas
		KeyStroke ctrlQ = KeyStroke.getKeyStroke(KeyEvent.VK_Q,KeyEvent.CTRL_DOWN_MASK);
		KeyStroke ctrlF = KeyStroke.getKeyStroke(KeyEvent.VK_F,KeyEvent.CTRL_DOWN_MASK);
		KeyStroke ctrl1 = KeyStroke.getKeyStroke(KeyEvent.VK_1,KeyEvent.CTRL_DOWN_MASK);
		KeyStroke ctrl2 = KeyStroke.getKeyStroke(KeyEvent.VK_2,KeyEvent.CTRL_DOWN_MASK);
		KeyStroke ctrl3 = KeyStroke.getKeyStroke(KeyEvent.VK_3,KeyEvent.CTRL_DOWN_MASK);
		KeyStroke ctrl4 = KeyStroke.getKeyStroke(KeyEvent.VK_4,KeyEvent.CTRL_DOWN_MASK);
		//2. Definimos un nombre único para esta acción (es un simple String)
        String accionFoco = "focusFilterAction";
        String accionOrdenAlfabetico_asc = "sortOrdenAlf_asc";
        String accionOrdenAlfabetico_des = "sortOrdenAlf_des";
        String accionOrdenTitulos_asc="sortOrdenTit_asc";
        String accionOrdenTitulos_des="sortOrdenTit_des";
        String accionQuiz= "actionQuiz";
        // 3. Creamos la acción que se ejecutará:
        Action accion = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtradoNombre.requestFocusInWindow();
            }
        };
        Action accionJuego = new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int tipoPregunta = random.nextInt(4);
				List<Equipo> listadeEquipos= liga.getEquipos();
				int indice= random.nextInt(listadeEquipos.size());
				Equipo equipo= listadeEquipos.get(indice);
				String pregunta= "";
				String respuesta= "";
				
				if (tipoPregunta==1) {
					pregunta= "¿Cuántos títulos de liga tiene el "+ equipo.getNombre() +"?";
					respuesta= String.valueOf(equipo.getTitulos());
				}else if (tipoPregunta==2) {
					pregunta= "¿En qué estadio juega el "+ equipo.getNombre() +"?";
					respuesta=equipo.getEstadio();
					
				}else if (tipoPregunta==3) {
					pregunta= "¿En qué año se fundó el "+ equipo.getNombre() +"?";
					respuesta=String.valueOf(equipo.getAnyoFundacion());
					
				}else {
					pregunta= "¿En qué ciudad juega el "+ equipo.getNombre() +"?";
					respuesta=equipo.getCiudad();
				}
				//IAG para crear respuestacorrecta
				String respuestaUsuario= JOptionPane.showInputDialog(
						JFrameListaEquipos.this,
						pregunta,
						"¡Quiz!",
						JOptionPane.QUESTION_MESSAGE
						);
				if (respuestaUsuario != null) {
					String respuestaUsuarioValida= respuestaUsuario.toLowerCase().replace("\\s", "");
					String respuestaValida= respuesta.toLowerCase().replace("\\s", "");
					//esto esta en la de los comics
					if(respuestaUsuarioValida.equals(respuestaValida)) {
						JOptionPane.showMessageDialog(
								JFrameListaEquipos.this, 
								"¡CORRECTO!",
								"¡Felicidades!",
								JOptionPane.INFORMATION_MESSAGE
							);
					}else {
						JOptionPane.showMessageDialog(
								JFrameListaEquipos.this,
								"¡INCORRECTO!",
								"La respuesta correcta era: "+ respuesta,
								JOptionPane.ERROR_MESSAGE
								);
								
					}
				}
			}
			
					
        	
        };
        Action accionAlf_asc= new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				comboOrdenar.setSelectedItem("Alfabético (asc)");
				
			}
        	
        };
        Action accionAlf_des= new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				comboOrdenar.setSelectedItem("Alfabético (des)");
				
			}
        	
        };
        Action accionTit_asc= new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				comboOrdenar.setSelectedItem("Titulos (asc)");
				
			}
        };
        Action accionTit_des= new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				comboOrdenar.setSelectedItem("Titulos (des)");
				
			}
        };
     //4. Vinculamos la tecla (Ctrl+F) con el nombre de la acción ("focusFilterAction") 
        // Usamos WHEN_IN_FOCUSED_WINDOW para que funcione en cualquier parte de la ventana
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrlF, accionFoco);
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrl1, accionOrdenAlfabetico_asc);
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrl2, accionOrdenAlfabetico_des);
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrl3, accionOrdenTitulos_asc);
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrl4, accionOrdenTitulos_des);
        panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(ctrlQ, accionQuiz);
        //5. Vinculamos el nombre de la acción ("focusFilterAction") con la acción real (el código a ejecutar)
        panel.getActionMap().put(accionFoco, accion);
        panel.getActionMap().put(accionOrdenAlfabetico_asc, accionAlf_asc);
        panel.getActionMap().put(accionOrdenAlfabetico_des, accionAlf_des);
        panel.getActionMap().put(accionOrdenTitulos_asc, accionTit_asc);
        panel.getActionMap().put(accionOrdenTitulos_des, accionTit_des);
        panel.getActionMap().put(accionQuiz, accionJuego);
        this.tablaEquipos.addMouseMotionListener(mousemotlist);
	}
	private void inicializarTabla() {
		Vector<String> cabezeraEquipos = new Vector<String>(Arrays.asList("ESCUDO", "NOMBRE", "TITULOS"));
		modeloDatosEquipos = new DefaultTableModel(new Vector<Vector<Object>>(), cabezeraEquipos);
		this.tablaEquipos = new JTable(this.modeloDatosEquipos) {
			public boolean isCellEditable(int row, int column){
				return column == 1;
				
			}
			
		};
		

		//Crear tableCellRenderer
		TableCellRenderer cellRenderer = (table, value, isSelected, hasFocus, row, column) -> 	{
		    
		    //Renderizar imagen en base del nombre del equipo
		    
		    if (column == 0) {//MODIFICACIONES EN LA COLUMNA DE IMAGENES
		        JLabel result = new JLabel(value.toString());
		        String nombrePNG = (String) value;	
		        String nombreLiga  = liga.getNombre().toLowerCase();
		        String ruta = "resources/images/equipos/"+nombreLiga+"/"+nombrePNG+".png";
		        
		        //Modificar tamaño de la imagen
		        int alturaObjetivo = table.getRowHeight(row);
		        ImageIcon imagenOriginal = null;
		        try {
		            imagenOriginal = new ImageIcon(ruta);
		        } catch (Exception e) {
		            System.err.println("No se ha encontrado el archivo: "+ruta);
		        }
		        ImageIcon imagenModificada = escalarIcono(imagenOriginal, alturaObjetivo);
		        //Insertar Imagen
		        result.setIcon(imagenModificada);		
		        result.setText(null);	
		        result.setHorizontalAlignment(JLabel.CENTER);
		        return result;
		    }else if (column == 1) {	//MODIFICACIONES EN LA COLUMNA DEL NOMBRE 
		        return (Component) value;
		    } else if (column == 2){
		        JLabel result1 = new JLabel(value.toString(), JLabel.CENTER);
		        result1.setFont(new Font("Arial", Font.BOLD, 16));
		        return result1;
		    }
		    
		    // Return por defecto para cualquier otra columna
		    return new JLabel(value != null ? value.toString() : "");
		};

		this.tablaEquipos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		//Crear tableCellEditor para que la celda funcione como un JButton
		TableColumn botonColumn = this.tablaEquipos.getColumnModel().getColumn(1);
	    botonColumn.setCellEditor(new ComponentCellEditor());
		
		//Establecer el cellRenderer como Render por defecto
		this.tablaEquipos.setDefaultRenderer(Object.class, cellRenderer);
		//Cambiar el ancho de la columna de los escudos
		TableColumn columnaEscudo = tablaEquipos.getColumnModel().getColumn(0);
		columnaEscudo.setPreferredWidth(75);
		
		TableColumn columnaBtn = tablaEquipos.getColumnModel().getColumn(1);
		columnaBtn.setPreferredWidth(this.getWidth() - 193);
		
		TableColumn columnaTitulos = tablaEquipos.getColumnModel().getColumn(2);
		columnaTitulos.setPreferredWidth(75); // Ajusta el valor según tus necesidades

		//Se establece la altura de la columna
		this.tablaEquipos.setRowHeight(40);
		//Se modifica el  modelo de seleccion para que solamente se pueda seleccionar una fila
		this.tablaEquipos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	
	MouseMotionListener mousemotlist = new MouseMotionListener() {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			int col = tablaEquipos.columnAtPoint(e.getPoint());
	        if (col == 1) {
	            tablaEquipos.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	        } else {
	            tablaEquipos.setCursor(Cursor.getDefaultCursor());
	        }
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	};
    
	
	
	//IAG
	//Funcion que modifica el tamaño de la imagen a la altura de la celda
	
	private ImageIcon escalarIcono(ImageIcon icon, int targetHeight) {
	    if (icon == null || icon.getImage() == null || icon.getIconHeight() <= 0) {
	        return null; // Devuelve nulo si la imagen original es inválida
	    }

	    if (icon.getIconHeight() == targetHeight) {
	        return icon; // Ya tiene el tamaño correcto
	    }

	    Image originalImage = icon.getImage();
	    
	    // Escala la imagen (el -1 en el ancho mantiene la proporción)
	    Image scaledImage = originalImage.getScaledInstance(-1, targetHeight, Image.SCALE_SMOOTH);

	    return new ImageIcon(scaledImage);
	}
	//Cargar los equipos teniendo en cuenta el contenido en el JTextField filtradoNombre
	private void cargarEquiposTablaFiltro (String filtro) {
		//Borrar los equipos existentes
		List<Equipo> equiposOrdenados = new ArrayList<>(this.liga.getEquipos());
		String orden= (String) comboOrdenar.getSelectedItem();
		if (orden != null && orden.equals("Titulos (asc)")){
			equiposOrdenados.sort(Comparator.comparingInt(Equipo::getTitulos));//IAG esta line, las demas, teniendo esta en cuenta
			
		} else if (orden != null && orden.equals("Titulos (des)")) {
			equiposOrdenados.sort(Comparator.comparingInt(Equipo::getTitulos).reversed());
		} else if (orden != null && orden.equals("Alfabético (des)")) {
			equiposOrdenados.sort(Comparator.comparing(Equipo::getNombre).reversed());
			
			//orden alfabetico por defecto
			
		} else {
			equiposOrdenados.sort(Comparator.comparing(Equipo::getNombre));
		} 
		modeloDatosEquipos.setRowCount(0);
		for (Equipo eq: equiposOrdenados) {
			if (eq.getNombre().toLowerCase().contains(filtro.toLowerCase())) {
				botonEquipo = new JButton(eq.getNombre());	
				botonEquipo.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						//JFramePlantilla jfp = new JFramePlantilla (ligas, liga, eq, JFrameListaEquipos.this);
						//jfp.setVisible(true);
						dispose();
						
						JFrameEquipo jfe = new JFrameEquipo(eq, JFrameListaEquipos.this);
						jfe.setVisible(true);
						//System.out.println("Abrir ventana de equipo: "+eq.getNombre());
					}
				});
			
				this.modeloDatosEquipos.addRow(new Object[] {eq.getNombrePNGEquipo(), botonEquipo, eq.getTitulos()});
			}
		}
	}
	
	
	

}
//GUI
//Clase generada con chat-gpt para dar la funcionalidad a los botones
class ComponentCellEditor extends AbstractCellEditor implements TableCellEditor{
	private Component component;
	@Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        // 'value' es el componente (nuestro JButton) que guardamos en el modelo
        this.component = (Component) value;
        return this.component;
    }

    @Override
    public Object getCellEditorValue() {
        // No necesitamos devolver un valor de edición, así que null está bien.
        // O podrías devolver el 'component' si quisieras.
        return null; 
    }
    
    /**
     * Esta es la parte crucial. Sobrescribimos este método para que,
     * cuando se haga clic en el botón, el listener del botón se ejecute
     * inmediatamente SIN necesidad de un segundo clic.
     */
    @Override
    public boolean isCellEditable(java.util.EventObject e) {
        // Si el evento es un clic del ratón, empieza a editar (activar el botón)
        if (e instanceof java.awt.event.MouseEvent) {
            // (MouseEvent) e).getClickCount() == 1 ... puedes añadir más lógica
            return true;
        }
        return false;
    }
	
}