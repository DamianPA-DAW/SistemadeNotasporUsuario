package es.damian.notas.model;

import es.damian.notas.model.Note;
import es.damian.notas.repository.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.files.*;
import java.util.ArrayList;
import java.util.List;

    public class FileRepository {
        private static final Path DATA_DIR = Paths.get("data");
        private static final Path USERS_FILE = DATA_DIR.resolve("users.txt");
        private static final Path USUARIOS_DIR = DATA_DIR.resolve("usuarios");
    
        public FileRepository(){
            try{
              if (Files.notExists(DATA_DIR)) Files.createDirectory(DATA_DIR);
            if (Files.notExists(USUARIOS_DIR)) Files.createDirectory(USUARIOS_DIR);
            if (Files.notExists(USERS_FILE)) Files.createFile(USERS_FILE);
            } catch (IOException e) {
                System.err.println("Error inicializando el archivo de usuarios: " + e.getMessage());
            }
        }

        public void guardarUsuario(Usuario usuario) throws IOException {
            String linea = usuario.getNombre() + "," + usuario.getEmail() + "," + usuario.getPassword() + "\n";
            Files.write(USERS_FILE, linea.getBytes(), StandardOpenOption.APPEND);
        }
    
        public List<Usuario> leerTodosLosUsuarios() throws IOException {
        List<Usuario> usuarios = new ArrayList<>();
        List<String> lineas = Files.readAllLines(USERS_FILE);
        for (String linea : lineas) {
            String[] partes = linea.split(";");
            if (partes.length == 2) {
                usuarios.add(new Usuario(partes[0], partes[1]));
            }
        }
        return usuarios;
    }

    public void crearCarpetaUsuario(String emailSanitizado) throws IOException {
        Path rutaUsuario = USUARIOS_DIR.resolve(emailSanitizado);
        if (Files.notExists(rutaUsuario)) {
            Files.createDirectory(rutaUsuario);
        }
    }

    public void guardarNotas(String emailSanitizado, List<Nota> notas) throws IOException {
        Path archivoNotas = USUARIOS_DIR.resolve(emailSanitizado).resolve("notas.txt");
        List<String> lineas = new ArrayList<>();
        for (Nota n : notas) {
            lineas.add(n.getTitulo() + ";" + n.getContenido());
        }
        // Files.write con StandardOpenOption.TRUNCATE_EXISTING sobrescribe el archivo con la lista actualizada
        Files.write(archivoNotas, lineas, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    public List<Nota> leerNotas(String emailSanitizado) throws IOException {
        Path archivoNotas = USUARIOS_DIR.resolve(emailSanitizado).resolve("notas.txt");
        List<Nota> notas = new ArrayList<>();
        
        if (Files.exists(archivoNotas)) {
            List<String> lineas = Files.readAllLines(archivoNotas);
            for (String linea : lineas) {
                String[] partes = linea.split(";", 2); // Separamos solo por el primer ';'
                if (partes.length == 2) {
                    notas.add(new Nota(partes[0], partes[1]));
                }
            }
        }
        return notas;
    }
}