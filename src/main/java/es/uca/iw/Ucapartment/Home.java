package es.uca.iw.Ucapartment;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;


import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.AbstractErrorMessage.ContentMode;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import es.uca.iw.Ucapartment.apartaments.Apartaments;
import es.uca.iw.Ucapartment.apartaments.ApartamentsRepository;
import es.uca.iw.Ucapartment.security.SecurityUtils;

public class Home extends VerticalLayout
{
	
	TextField home = new TextField();
	List<Apartaments> apartamentoo = null;
	
	
	public Home(HomeCallback callback, List<Apartaments> apartamento, ApartamentsRepository repo, String[] filter, 
			LoginCallback callback2, RegistroCallback regcallback)
	{ 
		apartamentoo = apartamento;

		final Button login = new Button("Iniciar Sesion");
		final Button registro = new Button("Registrarse");
		final Button input = new Button("Buscar");
		final Button perfil = new Button("Mi perfil");
		final Button logoutButton = new Button("Logout", event -> logout());
		
        login.setIcon(VaadinIcons.USER);
        
	    
		VerticalLayout layout = new VerticalLayout();
		HorizontalLayout layout2 = new HorizontalLayout();
		HorizontalLayout barra_superior = new HorizontalLayout(); // Barra superior con botones
		Panel loginPanel = new Panel("<h1 style='color:blue; text-align: center;'>UCApartment</h1>");
		loginPanel.setWidth("800px");
	    loginPanel.setHeight("800px");
	    
	    // Aquí lo que hacemos es que si no estamos logueados se muestran los botones de login y registro
		if(!SecurityUtils.isLoggedIn()) {
			
			barra_superior.addComponent(login);
			barra_superior.setComponentAlignment(login, Alignment.MIDDLE_CENTER);
			barra_superior.addComponent(registro);
			barra_superior.setComponentAlignment(registro,Alignment.MIDDLE_CENTER);
		}
		else {
			barra_superior.addComponent(perfil);
			barra_superior.setComponentAlignment(perfil, Alignment.MIDDLE_CENTER);
			logoutButton.setStyleName(ValoTheme.BUTTON_LINK);
			barra_superior.addComponent(logoutButton);
		}
		
		// Añadimos la barra superior a la ventana
	    addComponent(barra_superior);
	    setComponentAlignment(barra_superior, Alignment.MIDDLE_CENTER);
	    
	    layout.addComponent(loginPanel);
	    
	    layout.setComponentAlignment(loginPanel, Alignment.BOTTOM_CENTER );
	    
	    
	    
        //setMargin(true);
        //setSpacing(true);
	    
	    //loginPanel.setHeight(100.0f, Unit.PERCENTAGE);
		
		loginPanel.setWidth(null);
		
		final FormLayout loginLayout = new FormLayout();
		// Add some components inside the layout
		loginLayout.setWidth(500, Unit.PIXELS);
		
		loginPanel.setContent(loginLayout);
		String basepath = VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath();
		
		/*FileResource resource = new FileResource(new File(basepath +
                "/WEB-INF/images/descarga.png"));
		
		Image image = new Image("Image from file", resource);*/
		
		
		
		NativeSelect<String> select = new NativeSelect<>("Ciudad");
		
		// Add some items
		select.setItems("Todo","Cadiz", "Malaga","Jaen");
		select.setSelectedItem(filter[0]);
			
		NativeSelect<String> select2 = new NativeSelect<>("Habitaciones");

				// Add some items
		select2.setItems("Todo","1", "2","3");
		select2.setSelectedItem(filter[1]);
		
		NativeSelect<String> select3 = new NativeSelect<>("Precio");
		
		select3.setItems("Todo","20","30","40");
		select3.setSelectedItem(filter[2]);
		
		login.addClickListener(new ClickListener(){
			public void buttonClick(ClickEvent event)
			{
				callback2.showLoginScreen();
			}
		});
		
		registro.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				regcallback.showRegisterScreen();
			}
		});
				
				
		input.addClickListener(new ClickListener() {
		public void buttonClick(ClickEvent event) 
		{
			
			
			if(select.getValue() == "Todo")
			{
				if(select2.getValue() == "Todo")
				{
					if(select3.getValue() == "Todo")
					{
						apartamentoo = repo.findAll();
					}
					else
					{
						apartamentoo = repo.findByPrecio(select3.getValue());
					}
				}
				else
				{
					if(select3.getValue() == "Todo")
					{
						apartamentoo = repo.findByHabitacion(select2.getValue());
					}
					else
					{
						apartamentoo = repo.findByHabitacionAndPrecio(select2.getValue(), select3.getValue());
					}
					
				}
			}
			else
			{
				if(select2.getValue() == "Todo")
				{
					if(select3.getValue() == "Todo")
					{
						apartamentoo = repo.findByCiudad(select.getValue());
					}
					else
					{
						apartamentoo = repo.findByCiudadAndPrecio(select.getValue(), select3.getValue());
					}
				}
				else
				{
					if(select3.getValue() == "Todo")
					{
						apartamentoo = repo.findByCiudadAndHabitacion(select.getValue(), select2.getValue());
					}
					else
					{
						apartamentoo = repo.findByCiudadAndHabitacionAndPrecio(select.getValue(), select2.getValue(), select3.getValue());
					}
					
				}
			}
			
			filter[0] = select.getValue();
			filter[1] = select2.getValue();
			filter[2] = select3.getValue();
			
			callback.home(apartamentoo, filter);	
		}
				});
				
				/*Button input = new Button("Buscar", evt -> {
		            String nombre = select.getValue();
		            //System.out.println(nombre);
		            Apartaments apartamentoo = repo.findByNombre(nombre);
		            System.out.println(apartamentoo.getCiudad());
		            //callback.home(apartamentoo);
		        });*/
				
				
		//loginLayout.addComponent(image);
		loginLayout.addComponent(select);
		loginLayout.addComponent(select2);
		loginLayout.addComponent(select3);
		loginLayout.addComponent(input);
		
		
		try{
			for(Apartaments apartamentoa : apartamentoo)
			{
				loginLayout.addComponent(new Image(null,
				        new ClassResource(apartamentoa.getImage())));
				loginLayout.addComponent(new Label(apartamentoa.getNombre()));
				loginLayout.addComponent(new Button("Reservar"));
				loginLayout.addComponent(new Button("Informacion"));
			}
		}catch(Exception e) {
			
			for(Apartaments apartamentoa : repo.findAll())
			{
				loginLayout.addComponent(new Image(null,
				        new ClassResource(apartamentoa.getImage())));
				loginLayout.addComponent(new Label(apartamentoa.getNombre()));
				loginLayout.addComponent(new Button("Reservar"));
				loginLayout.addComponent(new Button("Informacion"));
			}
			
		}
		
		
		addComponent(layout);
		
	}

	@FunctionalInterface
    public interface HomeCallback {

        void home(List<Apartaments> apartamento, String[] filter);
       
    }
	
	@FunctionalInterface
	public interface LoginCallback
	{
		void showLoginScreen();
	}
	
    @FunctionalInterface
    public interface RegistroCallback {
        void showRegisterScreen();
    }

	// Con esta función lo que hacemos es cerrar la sesión de usuario y recargar la página 
	private void logout() {
		getUI().getPage().reload();
		getSession().close();
	}
	

}