package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import domain.Equipo;
import domain.Liga;
public class JFrameListaEquipos extends JFramePadre {
	private static final long serialVersionUID = 1L;
	private Liga liga;
	private JTextField filtradoNombre;
	private JTable tablaEquipos;
	private DefaultTableModel modeloDatosEquipos;
	public JFrameListaEquipos (Liga liga) {
		JPanel panel = super.panel;
		panel.setLayout(new BorderLayout());
		this.liga = liga;
		//Crear Barra buscadora
		filtradoNombre = new JTextField(15);
		JPanel panelFiltro = new JPanel();
		//Panel donde aparece la barra buscadora
		panelFiltro.add(new JLabel("Filtrado por nombre: "));
		panelFiltro.add(filtradoNombre);
		
		//Añadir panel y boton 
		panel.add(BorderLayout.NORTH, panelFiltro);
		panel.add(botonAtras);
		//Dar funcionalidad a botonAtras
		usoBotonAtras();
		DocumentListener doclistener=new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				loadEquiposFiltro(filtradoNombre.getText());
				
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				loadEquiposFiltro(filtradoNombre.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		};
		//Inicializar las tablas con los equipos
		inicializarTabla();
		cargarEquiposTabla();
		//Insertar la tabla de equipos en un panel con scroll
		JScrollPane scrollPaneEquipos = new JScrollPane(this.tablaEquipos);
		scrollPaneEquipos.setBorder(new TitledBorder("Equipos"));
		this.tablaEquipos.setFillsViewportHeight(true);
		
		panel.add(BorderLayout.CENTER, scrollPaneEquipos);
		this.add(panel);
		filtradoNombre.getDocument().addDocumentListener(doclistener);
	}
	private void inicializarTabla() {
		Vector<String> cabezeraEquipos = new Vector<String>(Arrays.asList("ESCUDO", "NOMBRE"));
		this.modeloDatosEquipos = new DefaultTableModel(new Vector<Vector<Object>>(), cabezeraEquipos);
		this.tablaEquipos = new JTable(this.modeloDatosEquipos){
			
	public boolean isCellEditable(int row, int column) {
		
		if(column ==0 || column == 1) {
			return false;
		}else {
			return true;
		}
		
	}
	};
		this.tablaEquipos.setRowHeight(40);
		
		//Crear tableCellRenderer
		 //no esta completo
		TableCellRenderer cellRenderer =(table, value, isSelected, hasFocus, row, column) -> {
			JLabel label = new JLabel();
			label.setOpaque(true);
			if (column == 0) {
				label.setIcon((ImageIcon) value);
				label.setHorizontalAlignment(JLabel.CENTER);
			} else {
		
				label.setText(value.toString());
			}
			//para poner las fotos he usado chatgpt
			
			if (isSelected) {
				label.setBackground(Color.BLUE);
				label.setForeground(Color.BLACK);
			} else {
		        label.setBackground(table.getBackground());
		        label.setForeground(table.getForeground());
		    }

			return label;
		};
		this.tablaEquipos.setDefaultRenderer(Object.class, cellRenderer);//chatgpt
	}
	private void cargarEquiposTabla()  {
		this.modeloDatosEquipos.setRowCount(0);
		for (Equipo e:liga.getEquipos()) {
			String rutaDelIcono = "images/equipos/" + liga.getNombre().replace(" ", "") + "/" + e.getNombre().toLowerCase().replace(" ", "") + ".png";
			System.out.println( rutaDelIcono);//para ver donde estaba buscando porque no me iban las fotos por estar metiendo mal la ruta, y aun asi hay q cambair nombres de fotos o de equipos
			ImageIcon iconoLiga = new ImageIcon("images/equipos/"+liga.getNombre().replace(" ", "") +"/"+ e.getNombre().toLowerCase().replace(" ", "") + ".png");
            ImageIcon iconoAjustado = new ImageIcon(iconoLiga.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
            this.modeloDatosEquipos.addRow(new Object[] {
    				iconoAjustado,  
    				e.getNombre()  
    		});
		}
		
		
	}
	
	@Override
	public void usoBotonAtras() {
		// TODO Auto-generated method stub
		super.botonAtras.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//Regreso a la ventana de liga
				JFrameLiga jfl = new JFrameLiga(ligas, liga);
				//Desaparece la ventana ListaEquipos
				setVisible(false);
				//Aparece ventana Liga
				jfl.setVisible(true);
				
			}
		});
	}
	private void loadEquiposFiltro (String string) {
		this.modeloDatosEquipos.setRowCount(0);
		for (Equipo e:liga.getEquipos()) {
			if(e.getNombre().toLowerCase().contains(string)) {
				ImageIcon iconoLiga = new ImageIcon("images/equipos/"+liga.getNombre().replace(" ", "") +"/"+ e.getNombre().toLowerCase().replace(" ", "") + ".png");
	            ImageIcon iconoAjustado = new ImageIcon(iconoLiga.getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH));
				this.modeloDatosEquipos.addRow(
						new Object[] {iconoAjustado,e.getNombre()}
				);
				}
			}
			
		}
	

	}

