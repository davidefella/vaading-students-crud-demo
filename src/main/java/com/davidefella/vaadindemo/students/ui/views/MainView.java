package com.davidefella.vaadindemo.students.ui.views;

import com.davidefella.vaadindemo.students.model.Student;
import com.davidefella.vaadindemo.students.service.StudentService;
import com.davidefella.vaadindemo.students.ui.views.components.Navbar;
import com.davidefella.vaadindemo.students.ui.views.components.StudentFormDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

@Route("")
@PermitAll
public class MainView extends VerticalLayout {

    private final StudentService studentService;
    private final Grid<Student> studentGrid;

    @Autowired
    public MainView(StudentService studentService, Navbar navbar) {
        this.studentService = studentService;
        this.studentGrid = new Grid<>(Student.class);

        // Rimuove padding e margini dal layout principale per eliminare i gap
        getStyle().set("margin", "0");
        getStyle().set("padding", "0");
        setSizeFull(); // Imposta il layout per occupare l'intera finestra del browser
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(navbar);

        VerticalLayout centerLayout = new VerticalLayout();
        centerLayout.setSizeFull();
        centerLayout.setAlignItems(Alignment.CENTER);
        H1 title = new H1("Gestione Studenti");
        centerLayout.add(title);

        configureGrid();
        Div gridWrapper = new Div(studentGrid);
        gridWrapper.setWidth("66.66%");
        gridWrapper.getStyle().set("display", "flex");
        gridWrapper.getStyle().set("justify-content", "center");
        gridWrapper.getStyle().set("margin", "0");
        gridWrapper.getStyle().set("padding", "0");

        VerticalLayout gridAndButtonLayout = new VerticalLayout();
        gridAndButtonLayout.setWidthFull();
        gridAndButtonLayout.setAlignItems(Alignment.END);
        gridAndButtonLayout.add(gridWrapper);

        Button addStudentButton = new Button("Aggiungi Studente", event -> {
            createAddStudentDialog();
        });

        gridAndButtonLayout.add(addStudentButton);

        gridAndButtonLayout.setAlignItems(Alignment.CENTER); // Centra il titolo e la griglia

        gridAndButtonLayout.setHorizontalComponentAlignment(Alignment.CENTER, addStudentButton);

        centerLayout.add(gridAndButtonLayout);

        add(centerLayout);

        updateStudentList();
    }

    private void configureGrid() {
        studentGrid.setWidthFull();

        studentGrid.setColumns("firstName", "lastName", "email");
        studentGrid.getColumnByKey("firstName").setHeader("Nome");
        studentGrid.getColumnByKey("lastName").setHeader("Cognome");
        studentGrid.getColumnByKey("email").setHeader("Email");

        studentGrid.addComponentColumn(student -> {
            Icon editIcon = new Icon(VaadinIcon.PENCIL);
            editIcon.getStyle().set("cursor", "pointer");
            editIcon.setColor("blue");

            editIcon.addClickListener(click -> {
                showEditStudentDialog(student);
            });

            return editIcon;
        }).setHeader("Modifica");

        studentGrid.addComponentColumn(student -> {
            Icon deleteIcon = new Icon(VaadinIcon.TRASH);
            deleteIcon.getStyle().set("cursor", "pointer");
            deleteIcon.setColor("red");

            deleteIcon.addClickListener(click -> {
                showDeleteConfirmationDialog(student);
            });

            return deleteIcon;
        }).setHeader("Cancella");

        studentGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        studentGrid.setAllRowsVisible(true);
        studentGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }

    private void updateStudentList() {
        studentGrid.setItems(studentService.findAll());
    }

    private void createAddStudentDialog() {
        StudentFormDialog dialog = new StudentFormDialog(null, student -> {
            studentService.save(student);
            updateStudentList();
            Notification.show("Studente aggiunto con successo!");
        });

        dialog.open();
    }

    private void showDeleteConfirmationDialog(Student student) {
        Dialog dialog = new Dialog();

        Div confirmText = new Div();
        confirmText.setText("Sei sicuro di voler eliminare lo studente " + student.getFirstName() + " " + student.getLastName() + "?");

        Button confirmButton = new Button("Conferma", event -> {
            studentService.delete(student);  // Elimina lo studente
            updateStudentList();  // Aggiorna la griglia
            dialog.close();
            Notification.show("Studente eliminato con successo!");  // Mostra notifica
        });

        Button cancelButton = new Button("Annulla", event -> dialog.close());  // Chiudi il dialogo se annullato

        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmButton, cancelButton);

        VerticalLayout dialogLayout = new VerticalLayout(confirmText, buttonsLayout);
        dialog.add(dialogLayout);

        dialog.open();  // Mostra il dialog di conferma
    }

    private void showEditStudentDialog(Student student) {
        Dialog dialog = new Dialog();

        TextField firstNameField = new TextField("Nome");
        firstNameField.setValue(student.getFirstName());

        TextField lastNameField = new TextField("Cognome");
        lastNameField.setValue(student.getLastName());

        TextField emailField = new TextField("Email");
        emailField.setValue(student.getEmail());
        emailField.setReadOnly(true);

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstNameField, lastNameField, emailField);

        Button saveButton = new Button("Salva modifiche", event -> {
            if (firstNameField.isEmpty() || lastNameField.isEmpty()) {
                Notification.show("Per favore, compila tutti i campi");
            } else {
                student.setFirstName(firstNameField.getValue());
                student.setLastName(lastNameField.getValue());

                studentService.save(student);
                updateStudentList();
                dialog.close();
                Notification.show("Studente modificato con successo!");
            }
        });

        Button cancelButton = new Button("Annulla", event -> {
            dialog.close();
        });

        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);

        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttonsLayout);
        dialog.add(dialogLayout);

        dialog.open();
    }
}