package com.davidefella.vaadindemo.students.ui.views;

import com.davidefella.vaadindemo.students.security.SecurityService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route("under-construction")
@PermitAll
public class UnderConstructionView extends VerticalLayout {

    private final SecurityService securityService;

    @Autowired
    public UnderConstructionView(SecurityService securityService) {
        this.securityService = securityService;

        createNavbar();

        H1 message = new H1("Sito in Costruzione");

        Button homeButton = new Button("Home", event -> {
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        VerticalLayout layout = new VerticalLayout(message, homeButton);
        layout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        layout.setSizeFull();
        layout.setAlignItems(Alignment.CENTER);

        add(layout);
    }

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

        Anchor logoutLink = new Anchor("", "Logout");
        logoutLink.getElement().addEventListener("click", event -> {
            securityService.logout();
        });

        Div spacer = new Div();
        navbar.add(spacer, chiSiamo, servizi, contatti, logoutLink);
        navbar.setAlignItems(FlexComponent.Alignment.CENTER);
        navbar.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        add(navbar);
    }
}
