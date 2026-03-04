package es.damian.notas.model;

import java.util.Scanner;
import repository.AuthService;
import repository.FileRepository;
import repository.NoteService;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static FileRepository repository = new FileRepository();
    private static AuthService authService = new AuthService(repository);

    public static void main(String[] args) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n=== SISTEMA DE NOTAS ===");
            System.out.println("1. Registrarse");
            System.out.println("2. Iniciar sesion");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> registrarUsuario();
                    case 2 -> loginUsuario();
                    case 0 -> System.out.println("Hasta pronto");
                    default -> System.out.println("Error: Opcion no valida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce un numero valido.");
            }
        }
    }

    private static void registrarUsuario() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contrasena: ");
        String pass = scanner.nextLine();
        authService.registrar(email, pass);
    }

    private static void loginUsuario() {
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Contrasena: ");
        String pass = scanner.nextLine();

        Usuario usuario = authService.login(email, pass);
        if (usuario != null) {
            String emailSanitizado = authService.sanitizarEmail(usuario.getEmail());
            NoteService noteService = new NoteService(repository, emailSanitizado);
            menuUsuario(noteService);
        }
    }

    private static void menuUsuario(NoteService noteService) {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n--- MENU DE NOTAS ---");
            System.out.println("1. Crear nota");
            System.out.println("2. Listar notas");
            System.out.println("3. Ver nota por numero");
            System.out.println("4. Eliminar nota");
            System.out.println("0. Cerrar sesion");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> {
                        System.out.print("Titulo: ");
                        String titulo = scanner.nextLine();
                        System.out.print("Contenido: ");
                        String contenido = scanner.nextLine();
                        noteService.crearNota(titulo, contenido);
                    }
                    case 2 -> noteService.listarNotas();
                    case 3 -> {
                        System.out.print("Numero de nota: ");
                        int num = Integer.parseInt(scanner.nextLine());
                        noteService.verNota(num - 1);
                    }
                    case 4 -> {
                        System.out.print("Numero de nota a eliminar: ");
                        int numDel = Integer.parseInt(scanner.nextLine());
                        noteService.eliminarNota(numDel - 1);
                    }
                    case 0 -> System.out.println("Cerrando sesion...");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Introduce un numero valido.");
            }
        }
    }
}