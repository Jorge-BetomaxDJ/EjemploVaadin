package com.examen.jorge.views.formulariocliente;

import org.springframework.security.access.annotation.Secured;

import com.examen.jorge.backend.data.Role;
import com.examen.jorge.backend.data.entity.Cliente;
import com.examen.jorge.backend.service.ClienteService;
import com.examen.jorge.ui.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Formulario Cliente")
@Route(value = "formulario-cliente", layout = MainView.class)
@Uses(Icon.class)
@Secured(Role.ADMIN)
@CssImport(value = "./styles/views/formulario-cliente-view.css")
public class FormularioClienteView extends Div {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextField nombre = new TextField("Nombre");
    private EmailField correoElectronico = new EmailField("Correo electronico");
    private DatePicker fechaNacimiento = new DatePicker("Fecha de nacimiento");
    private PhoneNumberField celular = new PhoneNumberField("Celular");

    private Button cancel = new Button("Cancelar");
    private Button save = new Button("Guardar");

    private Binder<Cliente> binder = new Binder<Cliente>(Cliente.class);

    public FormularioClienteView(ClienteService clienteService) {
        addClassName("formulario-cliente-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        binder.bindInstanceFields(this);
        clearForm();

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            clienteService.update(binder.getBean());
            Notification.show(binder.getBean().getClass().getSimpleName() + " guardado.");
            clearForm();
        });
    }

    private void clearForm() {
        binder.setBean(new Cliente());
    }

    private Component createTitle() {
        return new H3("Datos Cliente");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        correoElectronico.setErrorMessage("Porvafor ingresa un correo electronico valido");
        formLayout.add(nombre, celular, fechaNacimiento, correoElectronico);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

    private static class PhoneNumberField extends CustomField<String> {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ComboBox<String> countryCode = new ComboBox<>();
        private TextField number = new TextField();

        public PhoneNumberField(String label) {
            setLabel(label);
            countryCode.setWidth("120px");
            countryCode.setPlaceholder("Country");
            countryCode.setPattern("\\+\\d*");
            countryCode.setPreventInvalidInput(true);
            countryCode.setItems("+354", "+91", "+62", "+98", "+964", "+353", "+44", "+972", "+39", "+225");
            countryCode.addCustomValueSetListener(e -> countryCode.setValue(e.getDetail()));
            number.setPattern("\\d*");
            number.setPreventInvalidInput(true);
            HorizontalLayout layout = new HorizontalLayout(countryCode, number);
            layout.setFlexGrow(1.0, number);
            add(layout);
        }

        @Override
        protected String generateModelValue() {
            if (countryCode.getValue() != null && number.getValue() != null) {
                String s = countryCode.getValue() + " " + number.getValue();
                return s;
            }
            return "";
        }

        @Override
        protected void setPresentationValue(String phoneNumber) {
            String[] parts = phoneNumber != null ? phoneNumber.split(" ", 2) : new String[0];
            if (parts.length == 1) {
                countryCode.clear();
                number.setValue(parts[0]);
            } else if (parts.length == 2) {
                countryCode.setValue(parts[0]);
                number.setValue(parts[1]);
            } else {
                countryCode.clear();
                number.clear();
            }
        }
    }

}
