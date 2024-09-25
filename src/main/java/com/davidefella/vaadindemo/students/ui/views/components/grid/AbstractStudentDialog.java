package com.davidefella.vaadindemo.students.ui.views.components.grid;

import com.davidefella.vaadindemo.students.model.Student;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.function.Consumer;

public abstract class AbstractStudentDialog extends Dialog {
    protected final TextField firstNameField;
    protected final TextField lastNameField;
    protected final TextField emailField;

    public AbstractStudentDialog(Student student, Consumer<Student> onSave) {
        this.firstNameField = new TextField("Nome");
        this.lastNameField = new TextField("Cognome");
        this.emailField = new TextField("Email");

        // Pre-carica i campi se lo studente è già presente
        if (student != null) {
            firstNameField.setValue(student.getFirstName() != null ? student.getFirstName() : "");
            lastNameField.setValue(student.getLastName() != null ? student.getLastName() : "");
            emailField.setValue(student.getEmail() != null ? student.getEmail() : "");
            emailField.setReadOnly(true); // L'email non può essere modificata
        }

        FormLayout formLayout = new FormLayout(firstNameField, lastNameField, emailField);

        Button saveButton = new Button("Salva", event -> {
            if (validateForm()) {
                onSave.accept(populateStudentData(student));
                this.close();
            } else {
                Notification.show("Per favore, compila tutti i campi");
            }
        });

        add(new VerticalLayout(formLayout, saveButton));
        setWidth("400px");
    }

    protected abstract Student populateStudentData(Student student);

    private boolean validateForm() {
        return !firstNameField.isEmpty() && !lastNameField.isEmpty() && !emailField.isEmpty();
    }
}
