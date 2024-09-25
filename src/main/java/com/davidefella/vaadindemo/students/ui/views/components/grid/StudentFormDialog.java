package com.davidefella.vaadindemo.students.ui.views.components.grid;

import com.davidefella.vaadindemo.students.model.Student;
import java.util.function.Consumer;

public class StudentFormDialog extends AbstractStudentDialog {

    public StudentFormDialog(Student student, Consumer<Student> onSave) {
        super(student, onSave);
    }

    @Override
    protected Student populateStudentData(Student student) {
        if (student == null) {
            student = new Student();  // Crea un nuovo studente se student è null
        }
        student.setFirstName(firstNameField.getValue());
        student.setLastName(lastNameField.getValue());
        student.setEmail(emailField.getValue());

        return student; 
    }
}
