package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.sun.jdi.Value;

import domain.Equipo;
import domain.Liga;
import domain.Partido;

public class JFrameClasificacion extends JFramePadre {

	private int jornadaSeleccionada = 1;
	private Liga liga;
	private static final long serialVersionUID = 1L;
	private int pos = 1;
	
	public JFrameClasificacion(Liga liga, JFramePadre ventanaAnterior) {
		super();
		this.liga = liga;
		super.framePrevio = ventanaAnterior;
		ArrayList<Equipo> clasificacion = this.liga.getEquipos();
		Collections.sort(clasificacion);
		JPanel norte = new JPanel(new GridLayout(2,1));
		JLabel titulo = new JLabel("CLASIFICACIÓN", JLabel.CENTER);
		JLabel nomLiga = new JLabel(this.liga.getNombre(), JLabel.CENTER);
		nomLiga.setFont(new Font("Arial", Font.BOLD, 20));
		nomLiga.setHorizontalAlignment(0);
		titulo.setFont(new Font("Arial", Font.BOLD, 30));
		titulo.setHorizontalAlignment(0);
		norte.setOpaque(false);
		norte.add(titulo);
		norte.add(nomLiga);
		norte.setBounds(350, 40, 300, 60);
		crearLeyenda();
		
		Vector<String> columnNames = new Vector<String>(Arrays.asList("Pos", "Equipo", "Pts", "PJ", "DG"));
		DefaultTableModel mDatTab = new DefaultTableModel(new Vector<Vector<Object>>(), columnNames);
		JTable table = new JTable(mDatTab);
		table.setRowHeight(30);
		table.setDefaultEditor(Object.class, null);
		table.getTableHeader().setReorderingAllowed(false);
		panel.setLayout(null);
		//Se modifica el modelo de selección de la tabla para que se pueda selecciona únicamente una fila
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//Se define el comportamiento el evento de selección de una fila de la tabla
		table.getSelectionModel().addListSelectionListener(e -> {
			int equiposelec = table.getSelectedRow();
			if (equiposelec != -1) {
				String equiposeleccion = (String) table.getValueAt(equiposelec, 1);
				for(Equipo eq: clasificacion) {
					if (equiposeleccion.equals(eq.getNombre())) {
						setVisible(false);
						JFrameEquipo jfp = new JFrameEquipo(eq, this);
						jfp.setVisible(true);
					}
				}
			}
		});
		for (Equipo equipo : clasificacion) {
			String posicion =  Integer.toString(pos);
			String puntos = Integer.toString(equipo.getPuntos());
			String partidos = Integer.toString(equipo.getPartidosJugados());
			String goles = Integer.toString(equipo.getGoles());
			mDatTab.addRow(new Object[] {posicion, equipo.getNombre(), puntos, partidos, goles});
			pos++;
			}
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(270, 150, 460, 300);
		scrollPane.setOpaque(false);
		panel.add(scrollPane);
		panel.add(norte);
		this.add(panel);
		this.setSize(1000,600);
		this.setVisible(true);
		table.setVisible(true);
		usoBotonAtras(super.framePrevio);
		
		TableCellRenderer cellrenderer = new TableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				
				// TODO Auto-generated method stub
				JLabel result = new JLabel(value.toString());
				result.setOpaque(true);
				if (column != 1) { 
				    result.setHorizontalAlignment(JLabel.CENTER);
				}
				if (row == 0 && column == 0) {
					result.setBackground(new Color(255,251,0));
				}else if (row>0 && row<=3 && column == 0) {
					result.setBackground(new Color(27, 125, 242));
				}else if (row>3 && row <= 5 && column == 0) {
					result.setBackground(new Color(252,136,0));
				}else if (row>= 17 && column == 0) {
					result.setBackground(new Color(255,0,0));
					
				}else if (column == 0) {
					result.setBackground(new Color(115,111,111));
				}else {
					result.setBackground(Color.WHITE);
				} if (row == 16 && column == 0 && liga.getNombre().equals("Bundesliga")) {
					result.setBackground(new Color(255,0,0));
				}
				
				return result;
			}
		};
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setOpaque(false);
		table.getColumnModel().getColumn(0).setPreferredWidth(60);   // Pos
		table.getColumnModel().getColumn(1).setPreferredWidth(200);   // Nom
		table.getColumnModel().getColumn(2).setPreferredWidth(60);   // Pts
		table.getColumnModel().getColumn(3).setPreferredWidth(60);   // PJ
		table.getColumnModel().getColumn(4).setPreferredWidth(60);// DG
		table.setDefaultRenderer(Object.class, cellrenderer);
		
		}
		
	
	private void crearLeyenda() {
		Color[] colores = {new Color(255,251,0), new Color(27, 125, 242), new Color(252,136,0), new Color(255,0,0)};
		String[] textos = {"CAMPEON", "CHAMPIONS LEAGUE", "EUROPA LEAGUE", "DESCENSO"};
		String[] logos = {"resources/images/logos/campeon.png", "resources/images/logos/champions.png", "resources/images/logos/europa.png", "resources/images/logos/descenso.png"};
		JPanel colors = new JPanel(new GridLayout(4, 1));
		JPanel texts = new JPanel(new GridLayout(4, 1));
		for (int i = 0; i < 4; i++) {
			ImageIcon icono = new ImageIcon(logos[i]);
			ImageIcon iconoAjustado = new ImageIcon(icono.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
			JLabel color = new JLabel(iconoAjustado);
			color.setBackground(colores[i]);
			color.setOpaque(true);
			JLabel texto = new JLabel(textos[i]);
			colors.add(color);
			texts.add(texto);
		}
		panel.add(colors);
		colors.setBounds(50, 455, 25, 100);
		panel.add(texts);
		texts.setBounds(75, 455, 150, 100);
	}
	


}
