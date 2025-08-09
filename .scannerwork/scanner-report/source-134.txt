package com.unis.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ASEGURADORA_CONEXION", schema = "C##PROYECTO")
public class AseguradoraConexion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "aseg_conexion_seq")
    @SequenceGenerator(name = "aseg_conexion_seq", sequenceName = "SEQ_ASEGURADORA_CONEXION", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOMBRE", unique = true, nullable = false, length = 100)
    private String nombre;

    @Column(name = "URL_BASE", nullable = false, length = 255)
    private String urlBase;

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrlBase() {
        return urlBase;
    }

    public void setUrlBase(String urlBase) {
        this.urlBase = urlBase;
    }
}
