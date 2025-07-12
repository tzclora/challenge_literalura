package com.bubches.literalura.Model;



import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer añoNacimiento;
    private Integer añoMuerte;
    @ManyToMany(mappedBy = "autores")
    private List<Libro> libros = new ArrayList<>();


    // Constructor vacío requerido por JPA
    public Autor() {}

    // Constructor con parámetros útiles
    public Autor(String nombre, Integer añoNacimiento, Integer añoMuerte) {
        this.nombre = nombre;
        this.añoNacimiento = añoNacimiento;
        this.añoMuerte = añoMuerte;
    }

    // Getters y setters


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getAñoNacimiento() { return añoNacimiento; }
    public void setAñoNacimiento(Integer añoNacimiento) { this.añoNacimiento = añoNacimiento; }

    public Integer getAñoMuerte() { return añoMuerte; }
    public void setAñoMuerte(Integer añoMuerte) { this.añoMuerte = añoMuerte; }
}
