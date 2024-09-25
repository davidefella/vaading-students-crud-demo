package com.davidefella.vaadindemo.students.ui.views.components.grid;

import com.davidefella.vaadindemo.students.model.Student;
import com.davidefella.vaadindemo.students.service.StudentService;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;


public class StudentGrid extends Grid<Student> {

    private final StudentService studentService;

    public StudentGrid(StudentService studentService) {
        super(Student.class);
        this.studentService = studentService;
        configureGrid();
    }

    private void configureGrid() {
        setWidthFull();
        setColumns("firstName", "lastName", "email");
        getColumnByKey("firstName").setHeader("Nome");
        getColumnByKey("lastName").setHeader("Cognome");
        getColumnByKey("email").setHeader("Email");

        addComponentColumn(student -> createEditIcon(student)).setHeader("Modifica");
        addComponentColumn(student -> createDeleteIcon(student)).setHeader("Cancella");

        getColumns().forEach(col -> col.setAutoWidth(true));
        setAllRowsVisible(true);
        addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }

    private Icon createEditIcon(Student student) {
        Icon editIcon = new Icon(VaadinIcon.PENCIL);
        editIcon.getStyle().set("cursor", "pointer");
        editIcon.setColor("blue");
        editIcon.addClickListener(click -> {
            new StudentFormDialog(student, updatedStudent -> {
                studentService.save(updatedStudent);
                getDataProvider().refreshAll();  // Ricarica la griglia
            }).open();
        });
        return editIcon;
    }

    private Icon createDeleteIcon(Student student) {
        Icon deleteIcon = new Icon(VaadinIcon.TRASH);
        deleteIcon.getStyle().set("cursor", "pointer");
        deleteIcon.setColor("red");
        deleteIcon.addClickListener(click -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle("Conferma eliminazione");
            dialog.add("Sei sicuro di voler eliminare lo studente " + student.getFirstName() + " " + student.getLastName() + "?");
            
            Button confirmButton = new Button("Conferma", event -> {
                studentService.delete(student);
                getDataProvider().refreshAll();  // Ricarica la griglia
                dialog.close();
            });

            Button cancelButton = new Button("Annulla", event -> dialog.close());
            dialog.getFooter().add(confirmButton, cancelButton);
            dialog.open();
        });
        return deleteIcon;
    }
}
