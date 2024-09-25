package com.davidefella.vaadindemo.students.ui.views.components;

import com.davidefella.vaadindemo.students.model.Student;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.function.Consumer;
public class StudentFormDialog extends Dialog {
    private final TextField firstNameField;
    private final TextField lastNameField;
    private final TextField emailField;

    public StudentFormDialog(Student student, Consumer<Student> onSave) {
        this.firstNameField = new TextField("Nome");
        this.lastNameField = new TextField("Cognome");
        this.emailField = new TextField("Email");

        if (student != null) {
            firstNameField.setValue(student.getFirstName() != null ? student.getFirstName() : "");
            lastNameField.setValue(student.getLastName() != null ? student.getLastName() : "");
            emailField.setValue(student.getEmail() != null ? student.getEmail() : "");
            emailField.setReadOnly(true);
        }
    
        FormLayout formLayout = new FormLayout(firstNameField, lastNameField, emailField);

        Button saveButton = new Button("Salva", event -> {
            if (validateForm()) {
                student.setFirstName(firstNameField.getValue());
                student.setLastName(lastNameField.getValue());
                student.setEmail(emailField.getValue());

                onSave.accept(student);
                this.close();
            } else {
                Notification.show("Per favore, compila tutti i campi");
            }
        });

        add(new VerticalLayout(formLayout, saveButton));
        setWidth("400px");
    }

    private boolean validateForm() {
        return !firstNameField.isEmpty() && !lastNameField.isEmpty() && !emailField.isEmpty();
    }
}
