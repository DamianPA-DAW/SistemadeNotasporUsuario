package es.damian.notas.service;

import es.damian.notas.model.Usuario;
import es.damian.notas.repository.FileRepository;
import java.io.IOException;
import java.util.List;

public class AuthService {
    private FileRepository repository;

    public AuthService(FileRepository repository) {
        this.repository = repository;
    }

    public boolean registrar(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            System.out.println("⚠️ Error: El email y la contraseña no pueden estar vacíos.");
            return false;
        }

        try {
            List<Usuario> usuarios = repository.leerTodosLosUsuarios();
            for (Usuario u : usuarios) {
                if (u.getEmail().equalsIgnoreCase(email)) {
                    System.out.println("⚠️ Error: Ya existe un usuario registrado con ese email.");
                    return false;
                }
            }

            Usuario nuevoUsuario = new Usuario(email, password);
            repository.guardarUsuario(nuevoUsuario);

            String emailSanitizado = sanitizarEmail(email);
            repository.crearCarpetaUsuario(emailSanitizado);

            System.out.println("✅ Registro completado con éxito.");
            return true;

        } catch (IOException e) {
            System.err.println("❌ Error crítico en el registro: " + e.getMessage());
            return false;
        }
    }

    public Usuario login(String email, String password) {
        List<Usuario> usuarios = repository.leerTodosLosUsuarios();
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                System.out.println("🔓 Sesión iniciada para: " + email);
                return u;
            }
        }
        
        System.out.println("⚠️ Error: Email o contraseña incorrectos.");
        return null;
    }

    public String sanitizarEmail(String email) {
        return email.toLowerCase().replace("@", "_at_").replace(".", "_");
    }
}