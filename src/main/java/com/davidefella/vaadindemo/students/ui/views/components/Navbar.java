package com.davidefella.vaadindemo.students.ui.views.components;

import com.davidefella.vaadindemo.students.security.SecurityService;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class Navbar extends HorizontalLayout {

    public Navbar(SecurityService securityService){
        setWidthFull();
        setPadding(true);
        getStyle().set("background-color", "green");
        getStyle().set("color", "white");
        getStyle().set("position", "fixed");
        getStyle().set("top", "0");
        getStyle().set("left", "0");
        getStyle().set("right", "0");
        getStyle().set("z-index", "1000");

        // Crea i link di navigazione
        Anchor chiSiamo = new Anchor("under-construction", "Chi siamo");
        chiSiamo.getStyle().set("padding", "0 2rem");
        chiSiamo.getStyle().set("font-size", "0.9rem");

        Anchor servizi = new Anchor("under-construction", "Servizi");
        servizi.getStyle().set("padding", "0 2rem");
        servizi.getStyle().set("font-size", "0.9rem");

        Anchor contatti = new Anchor("under-construction", "Contatti");
        contatti.getStyle().set("padding", "0 2rem");
        contatti.getStyle().set("font-size", "0.9rem");

        // Link di logout
        Anchor logoutLink = new Anchor("", "Logout");
        logoutLink.getStyle().set("padding", "0 2rem");
        logoutLink.getStyle().set("font-size", "0.9rem");
        logoutLink.getElement().addEventListener("click", event -> securityService.logout());

        Div spacer = new Div();

        add(spacer, chiSiamo, servizi, contatti, logoutLink);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.END);
    }

}
