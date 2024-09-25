package com.davidefella.vaadindemo.students.ui.views.components.grid;

import com.davidefella.vaadindemo.students.model.Student;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.grid.GridVariant;

import java.util.function.Consumer;

public class StudentGrid extends Grid<Student> {

    public StudentGrid(Consumer<Student> editAction, Consumer<Student> deleteAction) {
        super(Student.class);
        configureGrid(editAction, deleteAction);
    }

    private void configureGrid(Consumer<Student> editAction, Consumer<Student> deleteAction) {
        setWidthFull();
        setColumns("firstName", "lastName", "email");
        getColumnByKey("firstName").setHeader("Nome");
        getColumnByKey("lastName").setHeader("Cognome");
        getColumnByKey("email").setHeader("Email");

        addComponentColumn(student -> createEditIcon(student, editAction)).setHeader("Modifica");
        addComponentColumn(student -> createDeleteIcon(student, deleteAction)).setHeader("Cancella");

        getColumns().forEach(col -> col.setAutoWidth(true));
        setAllRowsVisible(true);
        addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }

    private Icon createEditIcon(Student student, Consumer<Student> editAction) {
        Icon editIcon = new Icon(VaadinIcon.PENCIL);
        editIcon.getStyle().set("cursor", "pointer");
        editIcon.setColor("blue");
        editIcon.addClickListener(click -> editAction.accept(student));
        return editIcon;
    }

    private Icon createDeleteIcon(Student student, Consumer<Student> deleteAction) {
        Icon deleteIcon = new Icon(VaadinIcon.TRASH);
        deleteIcon.getStyle().set("cursor", "pointer");
        deleteIcon.setColor("red");
        deleteIcon.addClickListener(click -> deleteAction.accept(student));
        return deleteIcon;
    }
}
