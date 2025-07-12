# challenge_literalura
# 📚 Literalura

Este proyecto forma parte del programa ONE – Oracle Next Education. El desafío consistió en desarrollar una aplicación de consola utilizando Java y Spring Boot que permita interactuar con la API pública de Gutendex, almacenar los datos en una base de datos PostgreSQL y realizar distintas consultas sobre los libros registrados.

---

## 🎯 Objetivo

El objetivo principal fue poner en práctica conceptos de Java, Spring Boot, consumo de APIs, JPA y persistencia con base de datos relacional (PostgreSQL), desarrollando una aplicación funcional con un menú interactivo en consola.

---

## ⚙️ Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Hibernate
- Gutendex API

---

## 📌 Funcionalidades implementadas

- Buscar un libro por título (usando la API de Gutendex)
- Guardar libros y sus autores en la base de datos
- Listar todos los libros registrados
- Listar todos los autores registrados
- Listar autores vivos en un año específico
- Listar libros por idioma (con menú dinámico basado en los idiomas registrados)

---

## ▶️ Cómo ejecutar el proyecto

1. Clonar el repositorio:
```bash
git clone https://github.com/tzclora/literalura.git
```

2. Configurar la base de datos PostgreSQL en el archivo `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost/literalura
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
spring.jpa.hibernate.ddl-auto=update
```

3. Ejecutar el proyecto desde tu IDE o con Maven:
```bash
.\mvnw spring-boot:run
```

La aplicación mostrará un menú interactivo en consola al iniciar.

---

## 🔍 Fuente de datos

Todos los libros, autores e información relacionada se obtienen desde la API pública de Gutendex:

(https://gutendex.com/)

---

## 🧑 Autor

Este proyecto fue desarrollado como parte del desafío final del programa ONE.

- Nombre: Juan Carlos L. R.
- GitHub: [tzclora (BUBCHES)](https://github.com/tzclora)
