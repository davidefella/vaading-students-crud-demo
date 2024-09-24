package com.davidefella.vaadindemo.students.ui.views;

import com.davidefella.vaadindemo.students.model.Student;
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
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

@Route("")
public class MainView extends VerticalLayout {

    private final StudentService studentService;
    private final Grid<Student> studentGrid;

   
    @Autowired
    public MainView(StudentService studentService) {
        this.studentService = studentService;
        this.studentGrid = new Grid<>(Student.class);

        // Rimuove padding e margini dal layout principale per eliminare i gap
        getStyle().set("margin", "0");
        getStyle().set("padding", "0");

        setSizeFull();  // Imposta il layout per occupare l'intera finestra del browser
        setDefaultHorizontalComponentAlignment(Alignment.CENTER); // Centra il layout orizzontalmente

        // Barra di navigazione
        createNavbar();

        // Layout flessibile per centrare titolo e griglia
        VerticalLayout centerLayout = new VerticalLayout();
        centerLayout.setSizeFull();  // Occupa tutto lo spazio disponibile
        centerLayout.setAlignItems(Alignment.CENTER);  // Centra orizzontalmente

        // Titolo
        H1 title = new H1("Gestione Studenti");
        centerLayout.add(title);

        // Configura la griglia
        configureGrid();

        // Wrapper per centrare la griglia
        Div gridWrapper = new Div(studentGrid);
        gridWrapper.setWidth("66.66%");
        gridWrapper.getStyle().set("display", "flex");
        gridWrapper.getStyle().set("justify-content", "center");
        gridWrapper.getStyle().set("margin", "0");
        gridWrapper.getStyle().set("padding", "0");

        // Layout per griglia e pulsante
        VerticalLayout gridAndButtonLayout = new VerticalLayout();
        gridAndButtonLayout.setWidthFull();
        gridAndButtonLayout.setAlignItems(Alignment.END);  // Allinea i componenti a destra
        gridAndButtonLayout.add(gridWrapper);  // Aggiunge la griglia

        // Pulsante per aggiungere uno studente
        Button addStudentButton = new Button("Aggiungi Studente", event -> {
            createAddStudentDialog();  // Chiama il metodo che apre il modale
        });

        // Aggiungi il pulsante alla parte destra subito sotto la griglia
        gridAndButtonLayout.add(addStudentButton);

        // Nel layout per griglia e pulsante, centriamo il contenuto tranne il pulsante
        gridAndButtonLayout.setAlignItems(Alignment.CENTER);  // Centra il titolo e la griglia

        // Solo il pulsante deve essere allineato a destra
        gridAndButtonLayout.setHorizontalComponentAlignment(Alignment.CENTER, addStudentButton);

        // Aggiungi il layout centrato al layout principale
        centerLayout.add(gridAndButtonLayout);

        // Aggiungi il layout principale alla vista
        add(centerLayout);

        // Imposta i dati della griglia con gli studenti dal servizio
        updateStudentList();
    }

    private void createNavbar() {
        // Crea il layout orizzontale per la barra di navigazione
        HorizontalLayout navbar = new HorizontalLayout();
        navbar.setWidthFull(); // Occupa tutta la larghezza
        navbar.setPadding(true); // Aggiungi padding
        navbar.getStyle().set("background-color", "green"); // Sfondo verde per la barra di navigazione
        navbar.getStyle().set("color", "white"); // Testo bianco per il contrasto
        navbar.getStyle().set("position", "fixed"); // Posiziona la barra in modo fisso
        navbar.getStyle().set("top", "0"); // Fissa la barra in alto
        navbar.getStyle().set("left", "0"); // Fissa la barra sul lato sinistro
        navbar.getStyle().set("right", "0"); // Fissa la barra sul lato destro
        navbar.getStyle().set("z-index", "1000"); // Imposta un alto z-index per evitare che venga coperta

        // Crea i link per la barra di navigazione con padding extra e dimensione del
        // testo ridotta
        Anchor chiSiamo = new Anchor("under-construction", "Chi siamo");
        chiSiamo.getStyle().set("padding", "0 2rem"); // Aggiungi più spazio tra le voci usando `rem`
        chiSiamo.getStyle().set("font-size", "0.9rem"); // Riduci leggermente la dimensione del font

        Anchor servizi = new Anchor("under-construction", "Servizi");
        servizi.getStyle().set("padding", "0 2rem"); // Aggiungi più spazio tra le voci usando `rem`
        servizi.getStyle().set("font-size", "0.9rem"); // Riduci leggermente la dimensione del font

        Anchor contatti = new Anchor("under-construction", "Contatti");
        contatti.getStyle().set("padding", "0 2rem"); // Aggiungi più spazio tra le voci usando `rem`
        contatti.getStyle().set("font-size", "0.9rem"); // Riduci leggermente la dimensione del font

        // Spazio a sinistra per simulare logo o titolo
        Div spacer = new Div();

        // Aggiungi i link al layout di navigazione
        navbar.add(spacer, chiSiamo, servizi, contatti);
        navbar.setAlignItems(FlexComponent.Alignment.CENTER); // Allinea verticalmente al centro
        navbar.setJustifyContentMode(FlexComponent.JustifyContentMode.END); // Allinea i link a destra

        // Aggiungi la barra di navigazione al layout principale
        add(navbar);
    }

    
    private void configureGrid() {
        studentGrid.setWidthFull();
    
        studentGrid.setColumns("firstName", "lastName", "email");
        studentGrid.getColumnByKey("firstName").setHeader("Nome");
        studentGrid.getColumnByKey("lastName").setHeader("Cognome");
        studentGrid.getColumnByKey("email").setHeader("Email");
    
        // Aggiungi una colonna con l'icona di modifica
        studentGrid.addComponentColumn(student -> {
            // Crea l'icona di modifica
            Icon editIcon = new Icon(VaadinIcon.PENCIL);
            editIcon.getStyle().set("cursor", "pointer");
            editIcon.setColor("blue");
    
            // Aggiungi l'azione di modifica
            editIcon.addClickListener(click -> {
                showEditStudentDialog(student);  // Mostra il modale per modificare lo studente
            });
    
            return editIcon;
        }).setHeader("Modifica");  // Imposta l'header della colonna
    
        // Aggiungi una colonna con l'icona di cancellazione
        studentGrid.addComponentColumn(student -> {
            // Crea l'icona di cancellazione
            Icon deleteIcon = new Icon(VaadinIcon.TRASH);
            deleteIcon.getStyle().set("cursor", "pointer");
            deleteIcon.setColor("red");
    
            // Aggiungi l'azione di cancellazione
            deleteIcon.addClickListener(click -> {
                showDeleteConfirmationDialog(student);  // Mostra il modale di conferma prima di cancellare
            });
    
            return deleteIcon;
        }).setHeader("Cancella");  // Imposta l'header della colonna
    
        // Imposta le colonne in modo che siano tutte adattate automaticamente
        studentGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    
        studentGrid.setAllRowsVisible(true);
        studentGrid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }

    // Metodo per aggiornare i dati della griglia con gli studenti
    private void updateStudentList() {
        studentGrid.setItems(studentService.findAll()); // Popola la griglia con gli studenti
    }

    private void createAddStudentDialog() {
        Dialog dialog = new Dialog();

        // Creiamo i campi per il form
        TextField firstNameField = new TextField("Nome");
        TextField lastNameField = new TextField("Cognome");
        TextField emailField = new TextField("Email");

        // Layout per il form
        FormLayout formLayout = new FormLayout();
        formLayout.add(firstNameField, lastNameField, emailField);

        // Pulsante per confermare l'aggiunta dello studente
        Button saveButton = new Button("Salva", event -> {
            // Validazione di base
            if (firstNameField.isEmpty() || lastNameField.isEmpty() || emailField.isEmpty()) {
                Notification.show("Per favore, compila tutti i campi");
            } else {
                // Crea e salva il nuovo studente
                Student newStudent = new Student(firstNameField.getValue(), lastNameField.getValue(),
                        emailField.getValue());
                studentService.save(newStudent); // Salva il nuovo studente
                updateStudentList(); // Aggiorna la griglia con il nuovo studente
                dialog.close(); // Chiudi il modale
            }
        });

        // Layout del bottone
        VerticalLayout dialogLayout = new VerticalLayout(formLayout, saveButton);
        dialog.add(dialogLayout);

        // Apri il dialog
        dialog.open();
    }

    private void showDeleteConfirmationDialog(Student student) {
        Dialog dialog = new Dialog();
    
        // Testo di conferma
        Div confirmText = new Div();
        confirmText.setText("Sei sicuro di voler eliminare lo studente " + student.getFirstName() + " " + student.getLastName() + "?");
    
        // Pulsante per confermare la cancellazione
        Button confirmButton = new Button("Conferma", event -> {
            studentService.delete(student);  // Cancella lo studente
            updateStudentList();  // Aggiorna la griglia
            dialog.close();  // Chiudi il dialogo
            Notification.show("Studente eliminato con successo!");  // Mostra una notifica
        });
    
        // Pulsante per annullare l'azione
        Button cancelButton = new Button("Annulla", event -> {
            dialog.close();  // Chiudi il dialogo senza cancellare
        });
    
        // Layout per i pulsanti
        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmButton, cancelButton);
    
        // Aggiungi tutto al dialogo
        VerticalLayout dialogLayout = new VerticalLayout(confirmText, buttonsLayout);
        dialog.add(dialogLayout);
    
        // Mostra il dialogo
        dialog.open();
    }


    private void showEditStudentDialog(Student student) {
        Dialog dialog = new Dialog();
    
        // Crea i campi con i valori precompilati
        TextField firstNameField = new TextField("Nome");
        firstNameField.setValue(student.getFirstName());  // Precompila il nome
    
        TextField lastNameField = new TextField("Cognome");
        lastNameField.setValue(student.getLastName());  // Precompila il cognome
    
        TextField emailField = new TextField("Email");
        emailField.setValue(student.getEmail());  // Precompila l'email
        emailField.setReadOnly(true);  // Rendi il campo email non modificabile
    
        // Layout per il form
        FormLayout formLayout = new FormLayout();
        formLayout.add(firstNameField, lastNameField, emailField);
        
        // Pulsante per confermare la modifica
        Button saveButton = new Button("Salva modifiche", event -> {
            // Validazione di base
            if (firstNameField.isEmpty() || lastNameField.isEmpty()) {
                Notification.show("Per favore, compila tutti i campi");
            } else {
                // Aggiorna i dati dello studente
                student.setFirstName(firstNameField.getValue());
                student.setLastName(lastNameField.getValue());
    
                studentService.save(student);  // Salva le modifiche dello studente
                updateStudentList();  // Aggiorna la griglia
                dialog.close();  // Chiudi il modale
                Notification.show("Studente modificato con successo!");  // Mostra una notifica
            }
        });
    
        // Pulsante per annullare l'azione
        Button cancelButton = new Button("Annulla", event -> {
            dialog.close();  // Chiudi il dialogo senza modificare
        });
    
        // Layout per i pulsanti
        HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, cancelButton);
    
        // Aggiungi tutto al dialogo
        VerticalLayout dialogLayout = new VerticalLayout(formLayout, buttonsLayout);
        dialog.add(dialogLayout);
    
        // Mostra il dialogo
        dialog.open();
    }
    
}
