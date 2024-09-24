package com.davidefella.vaadindemo.students.ui.views;

import com.davidefella.vaadindemo.students.model.Student;
import com.davidefella.vaadindemo.students.security.SecurityService;
import com.davidefella.vaadindemo.students.service.StudentService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

@Route("")
@PermitAll
public class MainView extends VerticalLayout {

    private final StudentService studentService;
    private final SecurityService securityService;
    private final Grid<Student> studentGrid;

    @Autowired
    public MainView(StudentService studentService, SecurityService securityService) {
        this.studentService = studentService;
        this.securityService = securityService;
        this.studentGrid = new Grid<>(Student.class);

        // Rimuove padding e margini dal layout principale per eliminare i gap
        getStyle().set("margin", "0");
        getStyle().set("padding", "0");

        setSizeFull(); // Imposta il layout per occupare l'intera finestra del browser
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        createNavbar();

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
            createAddStudentDialog(); // Chiama il metodo che apre il modale
        });

        gridAndButtonLayout.add(addStudentButton);

        gridAndButtonLayout.setAlignItems(Alignment.CENTER); // Centra il titolo e la griglia

        gridAndButtonLayout.setHorizontalComponentAlignment(Alignment.CENTER, addStudentButton);

        centerLayout.add(gridAndButtonLayout);

        add(centerLayout);

        updateStudentList();
    }

    private void createNavbar() {
        HorizontalLayout navbar = new HorizontalLayout();
        navbar.setWidthFull();
        navbar.setPadding(true);
        navbar.getStyle().set("background-color", "green");
        navbar.getStyle().set("color", "white");
        navbar.getStyle().set("position", "fixed");
        navbar.getStyle().set("top", "0");
        navbar.getStyle().set("left", "0");
        navbar.getStyle().set("right", "0");
        navbar.getStyle().set("z-index", "1000");

        Anchor chiSiamo = new Anchor("under-construction", "Chi siamo");
        chiSiamo.getStyle().set("padding", "0 2rem");
        chiSiamo.getStyle().set("font-size", "0.9rem");

        Anchor servizi = new Anchor("under-construction", "Servizi");
        servizi.getStyle().set("padding", "0 2rem");
        servizi.getStyle().set("font-size", "0.9rem");

        Anchor contatti = new Anchor("under-construction", "Contatti");
        contatti.getStyle().set("padding", "0 2rem");
        contatti.getStyle().set("font-size", "0.9rem");

        Anchor logoutLink = new Anchor("", "Logout");
        logoutLink.getStyle().set("padding", "0 2rem");
        logoutLink.getStyle().set("font-size", "0.9rem");
        logoutLink.getElement().addEventListener("click", event -> {
            securityService.logout();
        });

        Div spacer = new Div();

        navbar.add(spacer, chiSiamo, servizi, contatti, logoutLink);
        navbar.setAlignItems(Alignment.CENTER);
        navbar.setJustifyContentMode(JustifyContentMode.END);

        add(navbar);
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
        Dialog dialog = new Dialog();

        TextField firstNameField = new TextField("Nome");
        TextField lastNameField = new TextField("Cognome");
        TextField emailField = new TextField("Email");

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstNameField, lastNameField, emailField);

        Button saveButton = new Button("Salva", event -> {

            if (firstNameField.isEmpty() || lastNameField.isEmpty() || emailField.isEmpty()) {
                Notification.show("Per favore, compila tutti i campi");
            } else {

                Student newStudent = new Student(firstNameField.getValue(), lastNameField.getValue(),
                        emailField.getValue());
                studentService.save(newStudent);
                updateStudentList();
                dialog.close();
            }
        });

        VerticalLayout dialogLayout = new VerticalLayout(formLayout, saveButton);
        dialog.add(dialogLayout);

        dialog.open();
    }

    private void showDeleteConfirmationDialog(Student student) {
        Dialog dialog = new Dialog();

        Div confirmText = new Div();
        confirmText.setText("Sei sicuro di voler eliminare lo studente " + student.getFirstName() + " "
                + student.getLastName() + "?");

        Button confirmButton = new Button("Conferma", event -> {
            studentService.delete(student);
            updateStudentList();
            dialog.close();
            Notification.show("Studente eliminato con successo!");
        });

        Button cancelButton = new Button("Annulla", event -> {
            dialog.close();
        });

        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmButton, cancelButton);

        VerticalLayout dialogLayout = new VerticalLayout(confirmText, buttonsLayout);
        dialog.add(dialogLayout);

        dialog.open();
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