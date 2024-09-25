package com.davidefella.vaadindemo.students.ui.views;

import com.davidefella.vaadindemo.students.model.Student;
import com.davidefella.vaadindemo.students.service.StudentService;
import com.davidefella.vaadindemo.students.ui.views.components.navbar.Navbar;
import com.davidefella.vaadindemo.students.ui.views.components.grid.StudentFormDialog;
import com.davidefella.vaadindemo.students.ui.views.components.grid.StudentGrid;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
@PermitAll
public class MainView extends VerticalLayout {

    private final StudentService studentService;
    private final StudentGrid studentGrid;

    @Autowired
    public MainView(StudentService studentService, Navbar navbar) {
        this.studentService = studentService;
        this.studentGrid = new StudentGrid(this::showEditStudentDialog, this::showDeleteConfirmationDialog);

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

    private void showEditStudentDialog(Student student) {
        new StudentFormDialog(student, updatedStudent -> {
            studentService.save(updatedStudent);
            updateStudentList();
            Notification.show("Studente modificato con successo!");
        }).open();
    }

    private void showDeleteConfirmationDialog(Student student) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Conferma eliminazione");
        dialog.add("Sei sicuro di voler eliminare lo studente " + student.getFirstName() + " " + student.getLastName() + "?");
        
        Button confirmButton = new Button("Conferma", event -> {
            studentService.delete(student);
            updateStudentList();
            dialog.close();
            Notification.show("Studente eliminato con successo!");
        });
    
        Button cancelButton = new Button("Annulla", event -> dialog.close());
        dialog.getFooter().add(confirmButton, cancelButton);
        dialog.open();
    }
}
