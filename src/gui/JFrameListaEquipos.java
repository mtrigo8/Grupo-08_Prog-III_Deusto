package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
	private JTextField filtradoNombre;
	private JTable tablaEquipos;
	private DefaultTableModel modeloDatosEquipos;
	private JButton botonEquipo;
	private JFramePadre ventanaAnterior;
	
	public JFrameListaEquipos (ArrayList<Liga> ligas, Liga liga, JFramePadre ventanaAnterior) {
		this.liga = liga;
		this.ligas = ligas;
		this.ventanaAnterior = ventanaAnterior;
		JPanel panel = super.panel;
		panel.setLayout(new BorderLayout());
		this.liga = liga;
		//Crear Barra buscadora
		filtradoNombre = new JTextField(20);
		JPanel panelFiltro = new JPanel();
		//Panel donde aparece la barra buscadora
		panelFiltro.add(new JLabel("Filtrado por nombre: "));
		panelFiltro.add(filtradoNombre);
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
		
		//Añadir panel y boton 
		panel.add(BorderLayout.NORTH, panelFiltro);
		panel.add(botonAtras);
		//Dar funcionalidad a botonAtras
		usoBotonAtras(ligas, liga);
		//Inicializar las tablas con los equipos
		inicializarTabla();
		cargarEquiposTabla();
		//Insertar la tabla de equipos en un panel con scroll
		JScrollPane scrollPaneEquipos = new JScrollPane(this.tablaEquipos);
		scrollPaneEquipos.setBorder(new TitledBorder("Equipos"));
		this.tablaEquipos.setFillsViewportHeight(true);
		
		panel.add(BorderLayout.CENTER, scrollPaneEquipos);
		this.add(panel);
	}
	private void inicializarTabla() {
		Vector<String> cabezeraEquipos = new Vector<String>(Arrays.asList("ESCUDO", "NOMBRE"));
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
				return result;
			}else {	//MODIFICACIONES EN LA COLUMNA DEL NOMBRE 
				return (Component) value;
			}
			
			
			
		};
		//Crear tableCellEditor para que la celda funcione como un JButton
		TableColumn botonColumn = this.tablaEquipos.getColumnModel().getColumn(1);
	    botonColumn.setCellEditor(new ComponentCellEditor());
		
		//Establecer el cellRenderer como Render por defecto
		this.tablaEquipos.setDefaultRenderer(Object.class, cellRenderer);
		//Cambiar el ancho de la columna de los escudos
		TableColumn columnaEscudo = tablaEquipos.getColumnModel().getColumn(0);
		columnaEscudo.setPreferredWidth(50);
		columnaEscudo.setMaxWidth(50);
		columnaEscudo.setMinWidth(50);
		//Se establece la altura de la columna
		this.tablaEquipos.setRowHeight(40);
		//Se modifica el  modelo de seleccion para que solamente se pueda seleccionar una fila
		this.tablaEquipos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
	}
	
	private void cargarEquiposTabla() {
		//Borrar datos
		this.modeloDatosEquipos.setRowCount(0);
		//Añadir uno a uno al modelo de datos
		for (Equipo eq: liga.getEquipos()) {
			botonEquipo = new JButton();
			botonEquipo.setText(eq.getNombre());
			//Funcionalidad de abrir la ventana del equipo al boton;
			botonEquipo.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					//JFramePlantilla jfp = new JFramePlantilla (ligas, liga, eq, JFrameListaEquipos.this);
					//jfp.setVisible(true);
					setVisible(false);
					
					JFrameEquipo jfe = new JFrameEquipo(ligas, liga, eq, JFrameListaEquipos.this);
					jfe.setVisible(true);
					//System.out.println("Abrir ventana de equipo: "+eq.getNombre());
				}
			});
			this.modeloDatosEquipos.addRow(new Object[] {eq.getNombrePNGEquipo(),botonEquipo});
		}
	}

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
		modeloDatosEquipos.setRowCount(0);
		for (Equipo e: liga.getEquipos()) {
			if (e.getNombre().toLowerCase().contains(filtro.toLowerCase())) {
				botonEquipo = new JButton(e.getNombre());
				this.modeloDatosEquipos.addRow(new Object[] {e.getNombrePNGEquipo(),botonEquipo });
			}
		}
	}
	
	@Override
	public void usoBotonAtras(ArrayList<Liga> ligas, Liga liga) {
		// TODO Auto-generated method stub
		super.botonAtras.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//Regreso a la ventana de liga
				//Desaparece la ventana ListaEquipos
				setVisible(false);
				//Aparece ventana anterior
				ventanaAnterior.setVisible(true);
				
				
			}
		});
	}

}
//IAG
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