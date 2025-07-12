package com.bubches.literalura.Principal;

import com.bubches.literalura.Model.Autor;
import com.bubches.literalura.Model.Libro;
import com.bubches.literalura.Repository.AutorRepository;
import com.bubches.literalura.Repository.LibroRepository;
import com.bubches.literalura.Service.ConsumoAPI;
import com.bubches.literalura.Service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal implements CommandLineRunner {

    private final Scanner teclado = new Scanner(System.in);
    private final Scanner scanner = new Scanner(System.in);
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/?search=";

    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    @Autowired
    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void mostrarMenu() {
        int opcion = -1;

        do {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1 - Buscar libro por título y registrarlo");
            System.out.println("2 - Listar libros registrados");
            System.out.println("3 - Listar autores registrados");
            System.out.println("4 - Listar autores vivos en un año específico");
            System.out.println("5 - Listar libros por idioma");
            System.out.println("0 - Salir");
            System.out.print("Seleccione una opción: ");

            String entrada = teclado.nextLine();

            try {
                opcion = Integer.parseInt(entrada);

                switch (opcion) {
                    case 1:
                        buscarLibro();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutores();
                        break;
                    case 4:
                        listarAutoresVivosPorAnio();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Saliendo...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opción inválida. Intente nuevamente.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
            }

        } while (opcion != 0);
    }

    private void buscarLibro() {
        System.out.print("Ingrese el título del libro: ");
        String entradaUsuario = teclado.nextLine().trim();

        // Consumir API
        String json = consumoApi.obtenerDatos(URL_BASE + entradaUsuario.replace(" ", "+"));
        var respuesta = conversor.obtenerDatos(json, com.bubches.literalura.DTO.ApiResponseDTO.class);

        if (respuesta.results().isEmpty()) {
            System.out.println("❌ Libro no encontrado en la API.");
            return;
        }

        var libroDTO = respuesta.results().get(0);
        String tituloReal = libroDTO.title();

        // Usamos método que trae autores con EntityGraph
        List<Libro> librosEnBD = libroRepository.findAllByTituloIgnoreCase(tituloReal);

        if (!librosEnBD.isEmpty()) {
            Libro libro = librosEnBD.get(0); // Tomamos el primero
            System.out.println("📚 Este libro ya está registrado en la base de datos:");
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Idioma: " + libro.getIdioma());
            System.out.println("Número de descargas: " + libro.getNumeroDeDescargas());

            var autores = libro.getAutores();
            if (!autores.isEmpty()) {
                System.out.println("Autor(es):");
                for (var autor : autores) {
                    System.out.println("- " + autor.getNombre());
                }
            } else {
                System.out.println("Sin autores registrados.");
            }
            return;
        }

        // Mostrar datos del libro encontrado
        System.out.println("✅ Libro encontrado:");
        System.out.println("Título: " + tituloReal);
        System.out.println("Idioma: " + libroDTO.languages());
        System.out.println("Descargas: " + libroDTO.download_count());
        libroDTO.authors().forEach(a -> System.out.println("Autor: " + a.name()));

        // Crear entidad Libro
        Libro libro = new Libro(tituloReal, libroDTO.languages().get(0), libroDTO.download_count());

        for (var autorDTO : libroDTO.authors()) {
            // Buscar por nombre directamente en la base de datos (más eficiente)
            Autor autor = autorRepository.findByNombreIgnoreCase(autorDTO.name())
                    .orElseGet(() -> new Autor(autorDTO.name(), autorDTO.birth_year(), autorDTO.death_year()));

            autorRepository.save(autor);
            libro.agregarAutor(autor);
        }

        libroRepository.save(libro);
        System.out.println("💾 Libro guardado exitosamente en la base de datos.");
    }


    private void listarLibrosRegistrados() {
        var libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("\nNo hay libros registrados en la base de datos.");
            return;
        }

        System.out.println("\n📚 Libros registrados:");

        for (var libro : libros) {
            System.out.println("\nTítulo: " + libro.getTitulo());
            System.out.println("Idioma: " + libro.getIdioma());
            System.out.println("Número de descargas: " + libro.getNumeroDeDescargas());

            var autores = libro.getAutores();
            if (!autores.isEmpty()) {
                System.out.println("Autor(es):");
                for (var autor : autores) {
                    System.out.println("- " + autor.getNombre());
                }
            } else {
                System.out.println("Sin autores registrados.");
            }
        }
    }

    private void listarAutores() {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
            return;
        }

        System.out.println("\n--- AUTORES REGISTRADOS ---");
        for (Autor autor : autores) {
            System.out.println("Nombre: " + autor.getNombre());
            System.out.println("Año de nacimiento: " + (autor.getAñoNacimiento() != null ? autor.getAñoNacimiento() : "Desconocido"));
            System.out.println("Año de muerte: " + (autor.getAñoMuerte() != null ? autor.getAñoMuerte() : "Desconocido"));
            System.out.println("------------------------------------");
        }
    }

    private void listarAutoresVivosPorAnio() {
        System.out.print("Ingrese el año para consultar autores vivos: ");
        int anio = Integer.parseInt(teclado.nextLine());

        List<Autor> autores = autorRepository.findAll();

        List<Autor> autoresVivos = autores.stream()
                .filter(a -> a.getAñoNacimiento() != null && a.getAñoNacimiento() <= anio)
                .filter(a -> a.getAñoMuerte() == null || a.getAñoMuerte() >= anio)
                .toList();

        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio + ".");
            return;
        }

        System.out.println("\n--- AUTORES VIVOS EN EL AÑO " + anio + " ---");
        for (Autor autor : autoresVivos) {
            System.out.println("Nombre: " + autor.getNombre());
            System.out.println("Año de nacimiento: " + autor.getAñoNacimiento());
            System.out.println("Año de muerte: " + (autor.getAñoMuerte() != null ? autor.getAñoMuerte() : "Sigue vivo o desconocido"));
            System.out.println("------------------------------------");
        }
    }

    private void listarLibrosPorIdioma() {
        List<String> idiomas = libroRepository.findDistinctIdiomas();

        if (idiomas.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        System.out.println("Idiomas disponibles:");
        for (int i = 0; i < idiomas.size(); i++) {
            System.out.printf("%d - %s%n", i + 1, idiomas.get(i));
        }

        System.out.print("Seleccione un idioma: ");
        int opcion;

        try {
            opcion = scanner.nextInt();
            scanner.nextLine(); // limpiar buffer
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Entrada inválida. Debe ingresar un número.");
            return;
        }

        if (opcion < 1 || opcion > idiomas.size()) {
            System.out.println("Opción inválida.");
            return;
        }

        String idiomaSeleccionado = idiomas.get(opcion - 1);
        List<Libro> libros = libroRepository.findByIdiomaIgnoreCase(idiomaSeleccionado);

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en ese idioma.");
        } else {
            System.out.println("\nLibros en idioma '" + idiomaSeleccionado + "':");
            libros.forEach(libro -> System.out.println("- " + libro.getTitulo()));
        }
    }

    @Override
    public void run(String... args) {
        mostrarMenu();
    }
}

