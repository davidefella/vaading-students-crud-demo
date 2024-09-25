package com.davidefella.vaadindemo.students.ui.views;

import com.davidefella.vaadindemo.students.security.SecurityService;
import com.davidefella.vaadindemo.students.ui.views.components.navbar.Navbar;
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

    @Autowired
    public UnderConstructionView(Navbar navbar) {

        add(navbar);

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
}
