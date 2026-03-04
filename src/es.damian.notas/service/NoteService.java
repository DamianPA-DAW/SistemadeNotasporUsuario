package es.damian.notas.service;

import es.damian.notas.model.Nota;
import es.damian.notas.repository.FileRepository;
import java.io.IOException;
import java.util.List;

public class NoteService {
    private FileRepository repository;
    private String emailSanitizado;

    public NoteService(FileRepository repository, String emailSanitizado) {
        this.repository = repository;
        this.emailSanitizado = emailSanitizado;
    }

    public void crearNota(String titulo, String contenido) {
        try {
            List<Nota> notas = repository.leerNotas(emailSanitizado);
            notas.add(new Nota(titulo, contenido));
            repository.guardarNotas(emailSanitizado, notas);
            System.out.println("Nota guardada correctamente.");
        } catch (IOException e) {
            System.err.println("Error al guardar la nota: " + e.getMessage());
        }
    }

    public void listarNotas() {
        try {
            List<Nota> notas = repository.leerNotas(emailSanitizado);
            if (notas.isEmpty()) {
                System.out.println("📭 No tienes notas guardadas.");
                return;
            }
            System.out.println("\n--- TUS NOTAS ---");
            for (int i = 0; i < notas.size(); i++) {
                System.out.println((i + 1) + ". " + notas.get(i).getTitulo());
            }
        } catch (IOException e) {
            System.err.println("❌ Error al leer las notas: " + e.getMessage());
        }
    }

    public void verNota(int indice) {
        try {
            List<Nota> notas = repository.leerNotas(emailSanitizado);
            if (indice >= 0 && indice < notas.size()) {
                System.out.println(notas.get(indice).toString());
            } else {
                System.out.println("Número de nota no válido.");
            }
        } catch (IOException e) {
            System.err.println("Error al acceder a la nota: " + e.getMessage());
        }
    }

    public void eliminarNota(int indice) {
        try {
            List<Nota> notas = repository.leerNotas(