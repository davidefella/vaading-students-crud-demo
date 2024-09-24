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
        this.firstNameField = new TextField("Nome", student != null ? student.getFirstName() : "");
        this.lastNameField = new TextField("Cognome", student != null ? student.getLastName() : "");
        this.emailField = new TextField("Email", student != null ? student.getEmail() : "");

        if (student != null) {
            emailField.setReadOnly(true); // L'email non può essere modificata
        }

        FormLayout formLayout = new FormLayout(firstNameField, lastNameField, emailField);

        Button saveButton = new Button("Salva", event -> {
            if (validateForm()) {
                Student newStudent = student == null ? new Student() : student;
                newStudent.setFirstName(firstNameField.getValue());
                newStudent.setLastName(lastNameField.getValue());
                newStudent.setEmail(emailField.getValue());

                onSave.accept(newStudent); // Chiama il callback per la logica di salvataggio
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
