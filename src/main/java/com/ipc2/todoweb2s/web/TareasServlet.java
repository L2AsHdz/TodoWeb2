package com.ipc2.todoweb2s.web;

import com.ipc2.todoweb2s.data.TareaDB;
import com.ipc2.todoweb2s.model.Tarea;
import com.ipc2.todoweb2s.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDateTime;

@WebServlet("/tareas")
public class TareasServlet extends HttpServlet {

    private TareaDB tareaDB;
    private boolean eliminarMsj = true;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accion = request.getParameter("accion");

        if(accion == null || accion.equals("listar")){
            HttpSession session = request.getSession();
            Connection conexion = (Connection) session.getAttribute("conexion");
            Usuario user = (Usuario) session.getAttribute("user");
            if (eliminarMsj) {
                session.removeAttribute("success");
            }
            eliminarMsj = true;

            tareaDB = new TareaDB(conexion);

            var tareas = tareaDB.listar(user.getIdUsuario());

            request.setAttribute("tareas", tareas);
        }
        else if (accion.equals("obtenerTarea")) {
            int idTarea = Integer.parseInt(request.getParameter("id"));

            var tarea = tareaDB.obtenerById(idTarea);

            request.setAttribute("tarea", tarea.get());
        }
        request.getRequestDispatcher("tarea/tareas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Connection connection = (Connection) session.getAttribute("conexion");
        Usuario usuario = (Usuario) session.getAttribute("user");
        tareaDB = new TareaDB(connection);

        String accion = request.getParameter("accion");
        String id = request.getParameter("id");
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String prioridad = request.getParameter("prioridad");
        String estado = request.getParameter("estado");

        // Validaciones

        Tarea tarea = new Tarea();
        if (accion != null) tarea.setIdTarea(Integer.parseInt(id));
        tarea.setTitulo(titulo);
        tarea.setDescripcion(descripcion);
        tarea.setFechaCreacion(LocalDateTime.now()); // LocalDate LocalTime LocalDateTime
        tarea.setPrioridad(Integer.parseInt(prioridad));
        tarea.setEstado(Integer.parseInt(estado));
        tarea.setIdUsuario(usuario.getIdUsuario());

        if (accion == null) {
            tareaDB.insertar(tarea);
            session.setAttribute("success", "Se agrego la tarea");
        } else {
            tareaDB.actualizar(tarea);
            session.setAttribute("success", "Tarea actualizada correctamente");
        }
        eliminarMsj = false;
        response.sendRedirect("tareas");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
