package com.ipc2.todoweb2s.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarea {
    private int idTarea;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private int prioridad;
    private int estado;
    private int idUsuario;
}
