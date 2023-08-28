package com.ipc2.todoweb2s.data;

import com.ipc2.todoweb2s.model.Tarea;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TareaDB {
    private Connection conexion;

    public TareaDB(Connection conexion) {
        this.conexion = conexion;
    }
    public void insertar(Tarea tarea) {
        String query = "INSERT INTO TAREA (titulo, descripcion, prioridad, fecha_creacion, id_usuario, id_estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (var preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, tarea.getTitulo());
            preparedStatement.setString(2, tarea.getDescripcion());
            preparedStatement.setInt(3, tarea.getPrioridad());
            preparedStatement.setString(4, tarea.getFechaCreacion().toString());
            preparedStatement.setInt(5, tarea.getIdUsuario());
            preparedStatement.setInt(6, tarea.getEstado());
            preparedStatement.executeUpdate();
            System.out.println("Tarea creada");
        } catch (SQLException e) {
            System.out.println("Error al crear tarea: " + e);
        }
    }
    public void actualizar(Tarea tarea) {
        String query = "UPDATE TAREA SET titulo = ?, descripcion = ?, prioridad = ?, id_usuario = ?, id_estado = ? WHERE id_tarea = ?";
        try (var preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, tarea.getTitulo());
            preparedStatement.setString(2, tarea.getDescripcion());
            preparedStatement.setInt(3, tarea.getPrioridad());
            preparedStatement.setInt(4, tarea.getIdUsuario());
            preparedStatement.setInt(5, tarea.getEstado());
            preparedStatement.setInt(6, tarea.getIdTarea());
            preparedStatement.executeUpdate();
            System.out.println("Tarea actualizada");
        } catch (SQLException e) {
            System.out.println("Error al actualizar tarea: " + e);
        }
    }
    public void eliminar(int id) {
        String query = "DELETE FROM TAREA WHERE id_tarea = ?";
        try (var preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Tarea eliminada");
        } catch (SQLException e) {
            System.out.println("Error al eliminar tarea: " + e);
        }
    }
    public List<Tarea> listar(int idUsuario) {
        String query = "SELECT * FROM TAREA WHERE id_usuario = ?";
        List<Tarea> tareas = new ArrayList<>();

        try (var preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, idUsuario);

            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {

                    var idTarea = resultSet.getInt("id_tarea");
                    var titulo = resultSet.getString("titulo");
                    var descripcion = resultSet.getString("descripcion");
                    var prioridad = resultSet.getInt("prioridad");
                    var fecha_creacion = LocalDateTime.parse(resultSet.getString("fecha_creacion"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    var idEstado = resultSet.getInt("id_estado");

                    var tarea = new Tarea(idTarea, titulo, descripcion, fecha_creacion, prioridad, idEstado, idUsuario);
                    tareas.add(tarea);
                }
            }
        }catch (SQLException e) {
            System.out.println("Error al consultar: " + e);
        }
        return tareas;
    }

    public Optional<Tarea> obtenerById(int id) {
        String query = "SELECT * FROM TAREA WHERE id_tarea = ?";
        Tarea tarea = null;

        try (var preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (var resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {

                    var idTarea = resultSet.getInt("id_tarea");
                    var titulo = resultSet.getString("titulo");
                    var descripcion = resultSet.getString("descripcion");
                    var prioridad = resultSet.getInt("prioridad");
                    var fecha_creacion = LocalDateTime.parse(resultSet.getString("fecha_creacion"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    var idEstado = resultSet.getInt("id_estado");
                    var idUsuario = resultSet.getInt("id_usuario");

                    tarea = new Tarea(idTarea, titulo, descripcion, fecha_creacion, prioridad, idEstado, idUsuario);
                }
            }
        }catch (SQLException e) {
            System.out.println("Error al consultar: " + e);
        }
        return Optional.ofNullable(tarea);
    }
}
