package com.davidefella.vaadindemo.students.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("under-construction")  // Imposta il percorso per questa vista
public class UnderConstructionView extends VerticalLayout {

    public UnderConstructionView() {
        // Aggiungi la navbar
        createNavbar();

        // Messaggio di "Under Construction"
        H1 message = new H1("Sito in Costruzione");


                // Pulsante Home
        Button homeButton = new Button("Home", event -> {
            // Naviga alla rotta principale ("/")
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        // Layout principale
        VerticalLayout layout = new VerticalLayout(message, homeButton);
        layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);  // Centra gli elementi
        layout.setSizeFull();
        layout.setAlignItems(Alignment.CENTER);

        // Aggiungi il messaggio e il pulsante alla vista
        add(layout);
    }

    // Copia il metodo createNavbar per mantenere la navbar
    private void createNavbar() {
        HorizontalLayout navbar = new HorizontalLayout();
        navbar.setWidthFull();
        navbar.setPadding(true);
        navbar.getStyle().set("background-color", "green");
        navbar.getStyle().set("color", "white");
        navbar.getStyle().set("position", "fixed");
        navbar.getStyle().set("top", "0");
        navbar.getStyle().set("left", "0");
        navbar.getStyle().set("right", "0");
        navbar.getStyle().set("z-index", "1000");

        Anchor chiSiamo = new Anchor("under-construction", "Chi siamo");
        chiSiamo.getStyle().set("padding", "0 2rem");
        chiSiamo.getStyle().set("font-size", "0.9rem");

        Anchor servizi = new Anchor("under-construction", "Servizi");
        servizi.getStyle().set("padding", "0 2rem");
        servizi.getStyle().set("font-size", "0.9rem");

        Anchor contatti = new Anchor("under-construction", "Contatti");
        contatti.getStyle().set("padding", "0 2rem");
        contatti.getStyle().set("font-size", "0.9rem");

        Div spacer = new Div();
        navbar.add(spacer, chiSiamo, servizi, contatti);
        navbar.setAlignItems(FlexComponent.Alignment.CENTER);
        navbar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        add(navbar);
    }
}
