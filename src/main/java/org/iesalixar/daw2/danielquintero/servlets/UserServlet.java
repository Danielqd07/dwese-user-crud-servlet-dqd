package org.iesalixar.daw2.danielquintero.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.iesalixar.daw2.danielquintero.daos.UserDAO;
import org.iesalixar.daw2.danielquintero.daos.UserDAOImpl;
import org.iesalixar.daw2.danielquintero.entities.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet encargado de gestionar el CRUD de usuarios.
 */
@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private static final Logger logger =
            LoggerFactory.getLogger(UserServlet.class);

    private UserDAO userDAO;

    /**
     * Inicializa el DAO de usuarios.
     */
    @Override
    public void init() {

        logger.info("Entrando en init");

        userDAO = new UserDAOImpl();

        logger.info("Saliendo de init");
    }

    /**
     * Gestiona las peticiones GET.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        logger.info("Entrando en doGet");

        String action = request.getParameter("action");

        try {

            if (action == null) {
                listUsers(request, response);

            } else {

                switch (action) {

                    case "new":
                        showNewForm(request, response);
                        break;

                    case "edit":
                        showEditForm(request, response);
                        break;

                    default:
                        listUsers(request, response);
                        break;
                }
            }

        } catch (SQLException e) {

            logger.error("Error SQL en doGet", e);

            throw new ServletException(e);
        }

        logger.info("Saliendo de doGet");
    }

    /**
     * Gestiona las peticiones POST.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error del servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        logger.info("Entrando en doPost");

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {

            if ("insert".equals(action)) {

                insertUser(request, response);

            } else if ("update".equals(action)) {

                updateUser(request, response);

            } else if ("delete".equals(action)) {

                deleteUser(request, response);

            } else {

                listUsers(request, response);
            }

        } catch (SQLException e) {

            logger.error("Error SQL en doPost", e);

            throw new ServletException(e);
        }

        logger.info("Saliendo de doPost");
    }

    /**
     * Muestra todos los usuarios.
     */
    private void listUsers(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SQLException, ServletException, IOException {

        logger.info("Entrando en listUsers");

        List<User> users =
                userDAO.listAllUsers();

        request.setAttribute(
                "listUsers",
                users
        );

        request.getRequestDispatcher(
                "/views/user/user-list.jsp"
        ).forward(request, response);

        logger.info("Saliendo de listUsers");
    }

    /**
     * Muestra el formulario para crear un usuario.
     */
    private void showNewForm(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        logger.info("Entrando en showNewForm");

        request.getRequestDispatcher(
                "/views/user/user-form.jsp"
        ).forward(request, response);

        logger.info("Saliendo de showNewForm");
    }

    /**
     * Muestra el formulario de edición.
     */
    private void showEditForm(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SQLException, ServletException, IOException {

        logger.info("Entrando en showEditForm");

        Long id =
                Long.valueOf(
                        request.getParameter("id")
                );

        User user =
                userDAO.getUserById(id);

        request.setAttribute(
                "user",
                user
        );

        request.getRequestDispatcher(
                "/views/user/user-form.jsp"
        ).forward(request, response);

        logger.info("Saliendo de showEditForm");
    }

    /**
     * Inserta un nuevo usuario.
     */
    private void insertUser(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SQLException, ServletException, IOException {

        logger.info("Entrando en insertUser");

        String username =
                request.getParameter("username");

        String passwordHash =
                request.getParameter("passwordHash");

        String lastPasswordChangeParam =
                request.getParameter("lastPasswordChange");

        String failedLoginAttemptsParam =
                request.getParameter("failedLoginAttempts");

        List<String> errors =
                new ArrayList<>();

        /*
         * Validación obligatoria pedida en el enunciado.
         */
        String[] requiredFields = {
                username,
                passwordHash,
                request.getParameter("lastPasswordChange")
        };

        for (String field : requiredFields) {

            if (field == null || field.isBlank()) {

                errors.add(
                        "Todos los campos obligatorios deben estar rellenos."
                );

                break;
            }
        }

        if (username != null &&
                username.length() > 40) {

            errors.add(
                    "El username no puede superar los 40 caracteres."
            );
        }

        if (passwordHash != null &&
                passwordHash.length() > 500) {

            errors.add(
                    "La contraseña no puede superar los 500 caracteres."
            );
        }

        Integer failedLoginAttempts = 0;

        try {

            if (failedLoginAttemptsParam != null &&
                    !failedLoginAttemptsParam.isBlank()) {

                failedLoginAttempts =
                        Integer.valueOf(
                                failedLoginAttemptsParam
                        );

                if (failedLoginAttempts < 0) {

                    errors.add(
                            "Los intentos fallidos no pueden ser negativos."
                    );
                }
            }

        } catch (NumberFormatException e) {

            logger.warn(
                    "Valor inválido en failedLoginAttempts",
                    e
            );

            errors.add(
                    "Los intentos fallidos deben ser un número válido."
            );
        }

        LocalDateTime lastPasswordChange = null;

        try {

            if (lastPasswordChangeParam != null &&
                    !lastPasswordChangeParam.isBlank()) {

                lastPasswordChange =
                        LocalDateTime.parse(
                                lastPasswordChangeParam
                        );
            }

        } catch (Exception e) {

            logger.warn(
                    "Fecha lastPasswordChange inválida",
                    e
            );

            errors.add(
                    "La fecha del último cambio de contraseña no es válida."
            );
        }

        if (username != null &&
                !username.isBlank() &&
                userDAO.existsUserByUsername(
                        username.trim()
                )) {

            errors.add(
                    "Ya existe un usuario con ese username."
            );
        }

        Boolean active =
                Boolean.valueOf(
                        request.getParameter("active")
                );

        Boolean accountNonLocked =
                Boolean.valueOf(
                        request.getParameter("accountNonLocked")
                );

        Boolean emailVerified =
                Boolean.valueOf(
                        request.getParameter("emailVerified")
                );

        Boolean mustChangePassword =
                Boolean.valueOf(
                        request.getParameter("mustChangePassword")
                );

        LocalDateTime passwordExpiresAt = null;

        if (lastPasswordChange != null) {

            /*
             * Requisito del enunciado:
             * la contraseña caduca tres meses después.
             */
            passwordExpiresAt =
                    lastPasswordChange.plusMonths(3);
        }

        User user = new User(
                username != null
                        ? username.trim()
                        : null,

                passwordHash,

                active,

                accountNonLocked,

                lastPasswordChange,

                passwordExpiresAt,

                failedLoginAttempts,

                emailVerified,

                mustChangePassword
        );

        if (!errors.isEmpty()) {

            logger.warn(
                    "Errores de validación al insertar usuario: {}",
                    errors
            );

            request.setAttribute(
                    "errors",
                    errors
            );

            request.setAttribute(
                    "user",
                    user
            );

            request.getRequestDispatcher(
                    "/views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        userDAO.insertUser(user);

        logger.info(
                "Usuario insertado correctamente: {}",
                username
        );

        response.sendRedirect(
                request.getContextPath() + "/users"
        );

        logger.info("Saliendo de insertUser");
    }

    /**
     * Actualiza un usuario.
     */
    private void updateUser(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SQLException, ServletException, IOException {

        logger.info("Entrando en updateUser");

        Long id =
                Long.valueOf(
                        request.getParameter("id")
                );

        String username =
                request.getParameter("username");

        String passwordHash =
                request.getParameter("passwordHash");

        String lastPasswordChangeParam =
                request.getParameter("lastPasswordChange");

        String failedLoginAttemptsParam =
                request.getParameter("failedLoginAttempts");

        List<String> errors =
                new ArrayList<>();

        String[] requiredFields = {
                username,
                passwordHash,
                request.getParameter("lastPasswordChange")
        };

        for (String field : requiredFields) {

            if (field == null || field.isBlank()) {

                errors.add(
                        "Todos los campos obligatorios deben estar rellenos."
                );

                break;
            }
        }

        if (username != null &&
                username.length() > 40) {

            errors.add(
                    "El username no puede superar los 40 caracteres."
            );
        }

        if (passwordHash != null &&
                passwordHash.length() > 500) {

            errors.add(
                    "La contraseña no puede superar los 500 caracteres."
            );
        }

        Integer failedLoginAttempts = 0;

        try {

            if (failedLoginAttemptsParam != null &&
                    !failedLoginAttemptsParam.isBlank()) {

                failedLoginAttempts =
                        Integer.valueOf(
                                failedLoginAttemptsParam
                        );

                if (failedLoginAttempts < 0) {

                    errors.add(
                            "Los intentos fallidos no pueden ser negativos."
                    );
                }
            }

        } catch (NumberFormatException e) {

            logger.warn(
                    "Valor inválido en failedLoginAttempts",
                    e
            );

            errors.add(
                    "Los intentos fallidos deben ser un número válido."
            );
        }

        LocalDateTime lastPasswordChange = null;

        try {

            if (lastPasswordChangeParam != null &&
                    !lastPasswordChangeParam.isBlank()) {

                lastPasswordChange =
                        LocalDateTime.parse(
                                lastPasswordChangeParam
                        );
            }

        } catch (Exception e) {

            logger.warn(
                    "Fecha lastPasswordChange inválida",
                    e
            );

            errors.add(
                    "La fecha del último cambio de contraseña no es válida."
            );
        }

        if (username != null &&
                !username.isBlank() &&
                userDAO.existsUserByUsernameAndNotId(
                        username.trim(),
                        id
                )) {

            errors.add(
                    "Ya existe otro usuario con ese username."
            );
        }

        Boolean active =
                Boolean.valueOf(
                        request.getParameter("active")
                );

        Boolean accountNonLocked =
                Boolean.valueOf(
                        request.getParameter("accountNonLocked")
                );

        Boolean emailVerified =
                Boolean.valueOf(
                        request.getParameter("emailVerified")
                );

        Boolean mustChangePassword =
                Boolean.valueOf(
                        request.getParameter("mustChangePassword")
                );

        LocalDateTime passwordExpiresAt = null;

        if (lastPasswordChange != null) {

            passwordExpiresAt =
                    lastPasswordChange.plusMonths(3);
        }

        User user = new User(
                id,

                username != null
                        ? username.trim()
                        : null,

                passwordHash,

                active,

                accountNonLocked,

                lastPasswordChange,

                passwordExpiresAt,

                failedLoginAttempts,

                emailVerified,

                mustChangePassword
        );

        if (!errors.isEmpty()) {

            logger.warn(
                    "Errores de validación al actualizar usuario: {}",
                    errors
            );

            request.setAttribute(
                    "errors",
                    errors
            );

            request.setAttribute(
                    "user",
                    user
            );

            request.getRequestDispatcher(
                    "/views/user/user-form.jsp"
            ).forward(request, response);

            return;
        }

        userDAO.updateUser(user);

        logger.info(
                "Usuario actualizado correctamente con id: {}",
                id
        );

        response.sendRedirect(
                request.getContextPath() + "/users"
        );

        logger.info("Saliendo de updateUser");
    }

    /**
     * Elimina un usuario.
     */
    private void deleteUser(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws SQLException, IOException {

        logger.info("Entrando en deleteUser");

        Long id =
                Long.valueOf(
                        request.getParameter("id")
                );

        userDAO.deleteUser(id);

        logger.info(
                "Usuario eliminado correctamente con id: {}",
                id
        );

        response.sendRedirect(
                request.getContextPath() + "/users"
        );

        logger.info("Saliendo de deleteUser");
    }
}