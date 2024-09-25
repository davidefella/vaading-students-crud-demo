package com.davidefella.vaadindemo.students.ui.views;

import com.davidefella.vaadindemo.students.service.StudentService;
import com.davidefella.vaadindemo.students.ui.views.components.navbar.Navbar;
import com.davidefella.vaadindemo.students.ui.views.components.grid.StudentFormDialog;
import com.davidefella.vaadindemo.students.ui.views.components.grid.StudentGrid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@PermitAll
public class MainView extends VerticalLayout {

    private final StudentService studentService;
    private final StudentGrid studentGrid;

    @Autowired
    public MainView(StudentService studentService, Navbar navbar) {
        this.studentService = studentService;
        this.studentGrid = new StudentGrid(studentService);

        // Layout setup
        setupLayout(navbar);
        updateStudentList();
    }

    private void setupLayout(Navbar navbar) {
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(navbar);

        VerticalLayout centerLayout = new VerticalLayout();
        centerLayout.setSizeFull();
        centerLayout.setAlignItems(Alignment.CENTER);
        centerLayout.add(new H1("Gestione Studenti"));

        Div gridWrapper = new Div(studentGrid);
        gridWrapper.setWidth("66.66%");
        gridWrapper.getStyle().set("display", "flex");
        gridWrapper.getStyle().set("justify-content", "center");

        VerticalLayout gridAndButtonLayout = new VerticalLayout();
        gridAndButtonLayout.setWidthFull();
        gridAndButtonLayout.setAlignItems(Alignment.END);
        gridAndButtonLayout.add(gridWrapper);

        Button addStudentButton = new Button("Aggiungi Studente", event -> createAddStudentDialog());
        gridAndButtonLayout.add(addStudentButton);
        gridAndButtonLayout.setAlignItems(Alignment.CENTER);
        gridAndButtonLayout.setHorizontalComponentAlignment(Alignment.CENTER, addStudentButton);

        centerLayout.add(gridAndButtonLayout);
        add(centerLayout);
    }

    private void updateStudentList() {
        studentGrid.setItems(studentService.findAll());
    }

    private void createAddStudentDialog() {
        new StudentFormDialog(null, student -> {
            studentService.save(student);
            updateStudentList();
            Notification.show("Studente aggiunto con successo!");
        }).open();
    }
}
