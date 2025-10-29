package gui;

import domain.Liga;

public class JFramePlantilla extends JFramePadre {
	private Liga liga;
	
	
	
	@Override
	public void usoBotonAtras() {
		// TODO Auto-generated method stub
		botonAtras.addActionListener(e -> {
			setVisible(false);
			JFrameListaEquipos fle = new JFrameListaEquipos(liga);
			fle.setVisible(true);
		});
		
	}

}
