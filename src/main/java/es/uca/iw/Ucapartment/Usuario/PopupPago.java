package es.uca.iw.Ucapartment.Usuario;

import com.vaadin.ui.Button;

import com.vaadin.ui.Window;

public class PopupPago extends Window {
	
	//VerticalLayout vertical = new VerticalLayout();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PopupPago() {
        super("Introduzca el número de Cuenta"); // Set window caption
        center();
        
        setClosable(true);
        
        

        setContent(new Button("Cerrar", event -> close()));
    }
	
}
