package com.examen.jorge.ui.views.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.examen.jorge.app.security.SecurityUtils;
import com.examen.jorge.views.clientes.ClientesView;

@Route
@PageTitle("Examen Jorge")
@JsModule("./styles/shared-styles.js")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@CssImport(value = "./styles/bootstrap.min.css")
@CssImport(value = "./styles/styles.css")
public class LoginView extends LoginOverlay
	implements AfterNavigationObserver, BeforeEnterObserver {

	public LoginView() {
		LoginI18n i18n = LoginI18n.createDefault();
		i18n.setHeader(new LoginI18n.Header());
		i18n.getHeader().setTitle("Examen Jorge");
		i18n.getHeader().setDescription(
			"Entrar usando: admin + admin\n" + "          o    user + user");
		i18n.setAdditionalInformation(null);
		i18n.setForm(new LoginI18n.Form());
		i18n.getForm().setSubmit("Entrar");
		i18n.getForm().setTitle("Entrar");
		i18n.getForm().setUsername("Usuario");
		i18n.getForm().setPassword("Contraseña");
		setI18n(i18n);
		setForgotPasswordButtonVisible(false);
		setAction("login");
	}
	
	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (SecurityUtils.isUserLoggedIn()) {
			event.forwardTo(ClientesView.class);
			//UI.getCurrent().navigate(ClientesView.class);
		} else {
			setOpened(true);
		}
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		setError(
			event.getLocation().getQueryParameters().getParameters().containsKey(
				"error"));
	}

}
