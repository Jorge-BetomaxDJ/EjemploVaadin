package com.examen.jorge.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabVariant;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.server.VaadinServlet;
import com.examen.jorge.app.security.SecurityUtils;
import com.examen.jorge.views.clientes.ClientesView;
import com.examen.jorge.views.formulariocliente.FormularioClienteView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes, viewport-fit=cover")
@PWA(name = "Jorge App Starter", shortName = "Examen Jorge",
		startPath = "login",
		backgroundColor = "#227aef", themeColor = "#227aef",
		offlinePath = "offline-page.html",
		offlineResources = {"images/offline-login-banner.jpg"},
		enableInstallPrompt = false)
public class MainView extends AppLayout {

	private final Tabs menu;

	public MainView() {

		this.setDrawerOpened(false);
		Span appName = new Span("Examen Jorge");
		appName.addClassName("hide-on-mobile");

		menu = createMenuTabs();

		this.addToNavbar(appName);
		this.addToNavbar(true, menu);

		getElement().addEventListener("search-focus", e -> {
			getElement().getClassList().add("hide-navbar");
		});

		getElement().addEventListener("search-blur", e -> {
			getElement().getClassList().remove("hide-navbar");
		});
	}

	@Override
	protected void afterNavigation() {
		super.afterNavigation();
		
		RouteConfiguration configuration = RouteConfiguration.forSessionScope();
		if (configuration.isRouteRegistered(this.getContent().getClass())) {
			String target = configuration.getUrl(this.getContent().getClass());
			Optional < Component > tabToSelect = menu.getChildren().filter(tab -> {
				Component child = tab.getChildren().findFirst().get();
				return child instanceof RouterLink && ((RouterLink) child).getHref().equals(target);
			}).findFirst();
			tabToSelect.ifPresent(tab -> menu.setSelectedTab((Tab) tab));
		} else {
			menu.setSelectedTab(null);
		}
	}

	private static Tabs createMenuTabs() {
		final Tabs tabs = new Tabs();
		tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
		tabs.add(getAvailableTabs());
		return tabs;
	}

	private static Tab[] getAvailableTabs() {
		final List<Tab> tabs = new ArrayList<>(4);
		if (SecurityUtils.isAccessGranted(ClientesView.class)) {
			tabs.add(createTab(VaadinIcon.USERS, "Clientes", ClientesView.class));
		}
		if (SecurityUtils.isAccessGranted(FormularioClienteView.class)) {
			tabs.add(createTab(VaadinIcon.USER, "Formulario Cliente", FormularioClienteView.class));
		}
		final String contextPath = VaadinServlet.getCurrent().getServletContext().getContextPath();
		final Tab logoutTab = createTab(createLogoutLink(contextPath));
		tabs.add(logoutTab);
		return tabs.toArray(new Tab[tabs.size()]);
	}

	private static Tab createTab(VaadinIcon icon, String title, Class<? extends Component> viewClass) {
		return createTab(populateLink(new RouterLink(null, viewClass), icon, title));
	}

	private static Tab createTab(Component content) {
		final Tab tab = new Tab();
		tab.addThemeVariants(TabVariant.LUMO_ICON_ON_TOP);
		tab.add(content);
		return tab;
	}

	private static Anchor createLogoutLink(String contextPath) {
		final Anchor a = populateLink(new Anchor(), VaadinIcon.ARROW_RIGHT, "Salir");
		a.setHref(contextPath + "/logout");
		return a;
	}

	private static <T extends HasComponents> T populateLink(T a, VaadinIcon icon, String title) {
		a.add(icon.create());
		a.add(title);
		return a;
	}
}