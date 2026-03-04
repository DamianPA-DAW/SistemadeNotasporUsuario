package repository;

import es.damian.notas.model.Usuario;
import java.io.IOException;
import java.nio.file.*; 
import java.util.ArrayList;
import java.util.List;

public class FileRepository {
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path USERS_FILE = DATA_DIR.resolve("users.txt");
    private static final Path USUARIOS_DIR = DATA_DIR.resolve("usuarios");

    public FileRepository() {
        try {
            if (Files.notExists(DATA_DIR)) Files.createDirectories(DATA_DIR);
            if (Files.notExists(USUARIOS_DIR)) Files.createDirectories(USUARIOS_DIR);
            if (Files.notExists(USERS_FILE)) Files.createFile(USERS_FILE);
        } catch (IOException e) {
            System.err.println("Error inicializando el sistema de archivos: " + e.getMessage());
        }
    }

    public void guardarUsuario(Usuario usuario) throws IOException {
        String linea = usuario.getEmail() + ";" + usuario.getPassword() + System.lineSeparator();
        Files.write(USERS_FILE, linea.getBytes(), StandardOpenOption.APPEND);
    }

    public List<Usuario> leerTodosLosUsuarios() throws IOException {
        List<Usuario> usuarios = new ArrayList<>();
        if (Files.exists(USERS_FILE)) {
            List<String> lineas = Files.readAllLines(USERS_FILE);
            for (String linea : lineas) {
                String[] partes = linea.split(";");
                if (partes.length == 2) {
                    usuarios.add(new Usuario(partes[0], partes[1]));
                }
            }
        }
        return usuarios;
    }

    public void crearCarpetaUsuario(String emailSanitizado) throws IOException {
        Path rutaUsuario = USUARIOS_DIR.resolve(emailSanitizado);
        if (Files.notExists(rutaUsuario)) {
            Files.createDirectories(rutaUsuario);
        }
    }

    public void guardarNotas(String emailSanitizado, List<Nota> notas) throws IOException {
        Path carpetaUsuario = USUARIOS_DIR.resolve(emailSanitizado);
        if (Files.notExists(carpetaUsuario)) crearCarpetaUsuario(emailSanitizado);
        
        Path archivoNotas = carpetaUsuario.resolve("notas.txt");
        List<String> lineas = new ArrayList<>();
        for (Nota n : notas) {
            lineas.add(n.getTitulo() + ";" + n.getContenido());
        }
        Files.write(archivoNotas, lineas, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public List<Nota> leerNotas(String emailSanitizado) throws IOException {
        Path archivoNotas = USUARIOS_DIR.resolve(emailSanitizado).resolve("notas.txt");
        List<Nota> notas = new ArrayList<>();
        
        if (Files.exists(archivoNotas)) {
            List<String> lineas = Files.readAllLines(archivoNotas);
            for (String linea : lineas) {
                String[] partes = linea.split(";", 2);
                if (partes.length == 2) {
                    notas.add(new Nota(partes[0], partes[1]));
                }
            }
        }
        return notas;
    }
}