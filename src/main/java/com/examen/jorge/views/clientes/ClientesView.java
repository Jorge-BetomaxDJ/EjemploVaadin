package com.examen.jorge.views.clientes;

import com.examen.jorge.backend.data.Role;
import com.examen.jorge.backend.data.entity.Cliente;
import com.examen.jorge.backend.service.ClienteService;
import com.examen.jorge.ui.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.artur.helpers.CrudServiceDataProvider;

@PageTitle("Clientes")
@Route(value = "clientes/:clienteID?/:action?(edit)", layout = MainView.class)
@RouteAlias(value = "clientes", layout = MainView.class)
@Secured(Role.ADMIN)
@CssImport(value = "./styles/views/clientes.css")
public class ClientesView extends Div implements BeforeEnterObserver {

    private final String CLIENTE_ID = "clienteID";
    private final String CLIENTE_EDIT_ROUTE_TEMPLATE = "clientes/%d/edit";

    private Grid<Cliente> grid = new Grid<>(Cliente.class, false);

    private TextField nombre;
    private TextField celular;
    private TextField correoElectronico;
    private DatePicker fechaNacimiento;

    private Button cancel = new Button("Cancelar");
    private Button save = new Button("Guardar");

    private BeanValidationBinder<Cliente> binder;

    private Cliente cliente;

    private ClienteService clienteService;

    public ClientesView(@Autowired ClienteService clienteService) {
        this.clienteService = clienteService;
        setSizeFull();
        addClassNames("clientes-view", "container");
        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        splitLayout.setSizeFull();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("celular").setAutoWidth(true);
        grid.addColumn("correoElectronico").setAutoWidth(true);
        grid.addColumn("fechaNacimiento").setAutoWidth(true);
        grid.setDataProvider(new CrudServiceDataProvider<>(clienteService));
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.setHeightFull();

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(CLIENTE_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(ClientesView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Cliente.class);

        // Bind fields. This where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.cliente == null) {
                    this.cliente = new Cliente();
                }
                binder.writeBean(this.cliente);

                clienteService.update(this.cliente);
                clearForm();
                refreshGrid();
                Notification.show("Cliente details stored.");
                UI.getCurrent().navigate(ClientesView.class);
            } catch (ValidationException validationException) {
                Notification.show("Ocurrió una excepción al intentar almacenar los detalles del cliente.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Integer> clienteId = event.getRouteParameters().getInteger(CLIENTE_ID);
        if (clienteId.isPresent()) {
            Optional<Cliente> clienteFromBackend = clienteService.get(clienteId.get());
            if (clienteFromBackend.isPresent()) {
                populateForm(clienteFromBackend.get());
            } else {
                Notification.show(String.format("No se encontró el cliente solicitado, ID = %d", clienteId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ClientesView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("flex flex-col");
        editorLayoutDiv.setWidth("400px");

        Div editorDiv = new Div();
        editorDiv.setClassName("p-l flex-grow");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        nombre = new TextField("Nombre");
        celular = new TextField("Celular");
        correoElectronico = new TextField("Correo Electronico");
        fechaNacimiento = new DatePicker("Fecha Nacimiento");
        Component[] fields = new Component[]{nombre, celular, correoElectronico, fechaNacimiento};

        for (Component field : fields) {
            ((HasStyle) field).addClassName("full-width");
        }
        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("w-full flex-wrap bg-contrast-5 py-s px-l");
        buttonLayout.setSpacing(true);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setId("grid-wrapper");
        wrapper.setWidthFull();
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Cliente value) {
        this.cliente = value;
        binder.readBean(this.cliente);

    }
}
