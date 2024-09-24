package com.davidefella.vaadindemo.students.service;

import com.davidefella.vaadindemo.students.model.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final List<Student> dummyDB;

    public StudentService() {
        this.dummyDB = new ArrayList<>();
        // Aggiungi alcuni studenti di esempio all'inizio
        dummyDB.add(new Student(1L, "Davide", "Gialli", "davide@example.com"));
        dummyDB.add(new Student(2L, "Mario", "Rossi", "mario@example.com"));
        dummyDB.add(new Student(3L, "Luigi", "Verdi", "luigi@example.com"));
    }

    // Restituisce tutti gli studenti
    public List<Student> findAll() {
        return dummyDB;
    }

    // Cerca uno studente per ID
    public Optional<Student> findById(Long id) {
        return dummyDB.stream()
                .filter(student -> student.getId().equals(id))
                .findFirst();
    }

    // Salva o aggiorna uno studente
    public void save(Student student) {
        // Se lo studente esiste già, aggiorna i suoi dati
        Optional<Student> existingStudent = findById(student.getId());
        if (existingStudent.isPresent()) {
            Student toUpdate = existingStudent.get();
            toUpdate.setFirstName(student.getFirstName());
            toUpdate.setLastName(student.getLastName());
            toUpdate.setEmail(student.getEmail());
        } else {
            // Se lo studente non esiste, aggiungilo al database
            student.setId(generateNewId());
            dummyDB.add(student);
        }
    }

    // Cancella uno studente per ID
    public void deleteById(Long id) {
        dummyDB.removeIf(student -> student.getId().equals(id));
    }

    // Cancella uno studente per ID
    public void delete(Student s) {
        dummyDB.removeIf(student -> student.getId().equals(s.getId()));
    }

    // Genera un nuovo ID per i nuovi studenti
    private Long generateNewId() {
        return dummyDB.stream()
                .mapToLong(Student::getId)
                .max()
                .orElse(0L) + 1;
    }
}
