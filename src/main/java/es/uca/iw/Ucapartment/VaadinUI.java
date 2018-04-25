package es.uca.iw.Ucapartment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.UI;

import es.uca.iw.Ucapartment.apartaments.Apartaments;
import es.uca.iw.Ucapartment.apartaments.ApartamentsRepository;
import es.uca.iw.Ucapartment.security.AccessDeniedView;
import es.uca.iw.Ucapartment.security.ErrorView;
import es.uca.iw.Ucapartment.security.LoginScreen;
import es.uca.iw.Ucapartment.security.SecurityUtils;

@SpringUI
public class VaadinUI extends UI {

	@Autowired
	SpringViewProvider viewProvider;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
    MainScreen mainScreen;
	
	@Autowired
	ApartamentsRepository repo;
	
	List<Apartaments> apartamento;
	
	String[] filter = new String[3];
	
	
	@Override
	protected void init(VaadinRequest request) {

	   	this.getUI().getNavigator().setErrorView(ErrorView.class);
		viewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
	
		if (SecurityUtils.isLoggedIn()) {
			showMainScreen();
		} else {
			
			//showLoginScreen();
		
			for(int i = 0; i<3 ;i++)
			{
				filter[i] = "Todo";
			}
			showHome(apartamento, repo, filter);
		}

	}
	
	private void showHome(List<Apartaments> apartamento, ApartamentsRepository repo, String[] filter)
	{
		setContent(new Home(this::home, apartamento, repo, filter, this::showLoginScreen));
	}

	private void showLoginScreen() {
		setContent(new LoginScreen(this::login));
	}

	private void showMainScreen() {
		setContent(mainScreen);
	}
	
	private void home(List<Apartaments> apartamento, String[] filter)
	{
		//System.out.println(apartamento.getCiudad());
		showHome(apartamento,repo, filter);
	}

	
	private boolean login(String username, String password) {
		try {
			Authentication token = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			// Reinitialize the session to protect against session fixation
			// attacks. This does not work with websocket communication.
			VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
			SecurityContextHolder.getContext().setAuthentication(token);
			
			// Show the main UI
			showMainScreen();
			return true;
		} catch (AuthenticationException ex) {
			return false;
		}
	}

	
}