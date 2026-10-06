package org.iesalixar.daw2.danielquintero.daos;

import org.iesalixar.daw2.danielquintero.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO de usuarios.
 */
public class UserDAOImpl implements UserDAO {

    private static final Logger logger =
            LoggerFactory.getLogger(UserDAOImpl.class);

    /**
     * Devuelve todos los usuarios.
     *
     * @return lista de usuarios
     * @throws SQLException si ocurre un error
     */
    @Override
    public List<User> listAllUsers() throws SQLException {

        logger.info("Entrando en listAllUsers");

        List<User> users = new ArrayList<>();

        String query = """
                SELECT id, username, passwordHash, active, accountNonLocked,
                       lastPasswordChange, passwordExpiresAt,
                       failedLoginAttempts, emailVerified, mustChangePassword
                FROM users
                """;

        Connection connection =
                DatabaseConnectionManager.getInstance().getConnection();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                users.add(mapUser(resultSet));
            }
        }

        logger.info("Usuarios recuperados: {}", users.size());
        logger.info("Saliendo de listAllUsers");

        return users;
    }

    /**
     * Inserta un nuevo usuario.
     *
     * @param user usuario a insertar
     * @throws SQLException si ocurre un error
     */
    @Override
    public void insertUser(User user) throws SQLException {

        logger.info("Entrando en insertUser");

        String query = """
                INSERT INTO users (
                    username,
                    passwordHash,
                    active,
                    accountNonLocked,
                    lastPasswordChange,
                    passwordExpiresAt,
                    failedLoginAttempts,
                    emailVerified,
                    mustChangePassword
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        Connection connection =
                DatabaseConnectionManager.getInstance().getConnection();

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            setUserParameters(preparedStatement, user);

            preparedStatement.executeUpdate();

            logger.info("Usuario insertado correctamente: {}",
                    user.getUsername());
        }

        logger.info("Saliendo de insertUser");
    }

    /**
     * Comprueba si existe un usuario por username.
     *
     * @param username nombre de usuario
     * @return true si existe
     * @throws SQLException si ocurre un error
     */
    @Override
    public boolean existsUserByUsername(String username)
            throws SQLException {

        logger.info("Entrando en existsUserByUsername");

        String query =
                "SELECT COUNT(*) FROM users WHERE username = ?";

        Connection connection =
                DatabaseConnectionManager.getInstance().getConnection();

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);

            try (ResultSet resultSet =
                         preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    boolean exists = resultSet.getInt(1) > 0;

                    logger.info(
                            "Resultado comprobación username {}: {}",
                            username,
                            exists
                    );

                    logger.info("Saliendo de existsUserByUsername");

                    return exists;
                }
            }
        }

        logger.info("Saliendo de existsUserByUsername");

        return false;
    }

    /**
     * Obtiene un usuario por su id.
     *
     * @param id identificador del usuario
     * @return usuario encontrado o null
     * @throws SQLException si ocurre un error
     */
    @Override
    public User getUserById(Long id) throws SQLException {

        logger.info("Entrando en getUserById");

        String query = """
                SELECT id, username, passwordHash, active, accountNonLocked,
                       lastPasswordChange, passwordExpiresAt,
                       failedLoginAttempts, emailVerified, mustChangePassword
                FROM users
                WHERE id = ?
                """;

        Connection connection =
                DatabaseConnectionManager.getInstance().getConnection();

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet =
                         preparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    logger.info("Usuario encontrado con id: {}", id);
                    logger.info("Saliendo de getUserById");

                    return mapUser(resultSet);
                }
            }
        }

        logger.warn("No se encontró usuario con id: {}", id);
        logger.info("Saliendo de getUserById");

        return null;
    }

    /**
     * Actualiza un usuario existente.
     *
     * @param user usuario actualizado
     * @throws SQLException si ocurre un error
     */
    @Override
    public void updateUser(User user) throws SQLException {

        logger.info("Entrando en updateUser");

        String query = """
                UPDATE users
                SET username = ?,
                    passwordHash = ?,
                    active = ?,
                    accountNonLocked = ?,
                    lastPasswordChange = ?,
                    passwordExpiresAt = ?,
                    failedLoginAttempts = ?,
                    emailVerified = ?,
                    mustChangePassword = ?
                WHERE id = ?
                """;

        Connection connection =
                DatabaseConnectionManager.getInstance().getConnection();

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            setUserParameters(preparedStatement, user);

            preparedStatement.setLong(10, user.getId());

            preparedStatement.executeUpdate();

            logger.info(
                    "Usuario actualizado correctamente con id: {}",
                    user.getId()
            );
        }

        logger.info("Saliendo de updateUser");
    }

    /**
     * Comprueba si existe otro usuario con el mismo username.
     *
     * @param username username que se quiere comprobar
     * @param id id del usuario que debe excluirse
     * @return true si existe otro usuario con ese username
     * @throws SQLException si ocurre un error
     */
    @Override
    public boolean existsUserByUsernameAndNotId(
            String username,
            Long id
    ) throws SQLException {

        logger.info("Entrando en existsUserByUsernameAndNotId");

        String query = """
                SELECT COUNT(*)
                FROM users
                WHERE username = ?
                AND id != ?
                """;

        Connection connection =
                DatabaseConnectionManager.getInstance().getConnection();

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);
            preparedStatement.setLong(2, id);

            try (ResultSet resultSet =
                         preparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    boolean exists =
                            resultSet.getInt(1) > 0;

                    logger.info(
                            "Resultado de comprobación: {}",
                            exists
                    );

                    logger.info(
                            "Saliendo de existsUserByUsernameAndNotId"
                    );

                    return exists;
                }
            }
        }

        logger.info(
                "Saliendo de existsUserByUsernameAndNotId"
        );

        return false;
    }

    /**
     * Elimina un usuario por id.
     *
     * @param id identificador del usuario
     * @throws SQLException si ocurre un error
     */
    @Override
    public void deleteUser(Long id) throws SQLException {

        logger.info("Entrando en deleteUser");

        String query =
                "DELETE FROM users WHERE id = ?";

        Connection connection =
                DatabaseConnectionManager.getInstance().getConnection();

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();

            logger.info(
                    "Usuario eliminado correctamente con id: {}",
                    id
            );
        }

        logger.info("Saliendo de deleteUser");
    }

    /**
     * Convierte una fila del ResultSet en un objeto User.
     *
     * @param resultSet resultado de la consulta
     * @return usuario construido
     * @throws SQLException si ocurre un error
     */
    private User mapUser(ResultSet resultSet)
            throws SQLException {

        logger.info("Entrando en mapUser");

        Timestamp lastPasswordChangeTimestamp =
                resultSet.getTimestamp("lastPasswordChange");

        Timestamp passwordExpiresAtTimestamp =
                resultSet.getTimestamp("passwordExpiresAt");

        User user = new User(
                resultSet.getLong("id"),
                resultSet.getString("username"),
                resultSet.getString("passwordHash"),
                resultSet.getBoolean("active"),
                resultSet.getBoolean("accountNonLocked"),
                lastPasswordChangeTimestamp != null
                        ? lastPasswordChangeTimestamp.toLocalDateTime()
                        : null,
                passwordExpiresAtTimestamp != null
                        ? passwordExpiresAtTimestamp.toLocalDateTime()
                        : null,
                resultSet.getInt("failedLoginAttempts"),
                resultSet.getBoolean("emailVerified"),
                resultSet.getBoolean("mustChangePassword")
        );

        logger.info("Saliendo de mapUser");

        return user;
    }

    /**
     * Añade los datos del usuario a un PreparedStatement.
     *
     * @param preparedStatement sentencia preparada
     * @param user usuario
     * @throws SQLException si ocurre un error
     */
    private void setUserParameters(
            PreparedStatement preparedStatement,
            User user
    ) throws SQLException {

        logger.info("Entrando en setUserParameters");

        preparedStatement.setString(
                1,
                user.getUsername()
        );

        preparedStatement.setString(
                2,
                user.getPasswordHash()
        );

        preparedStatement.setBoolean(
                3,
                user.getActive()
        );

        preparedStatement.setBoolean(
                4,
                user.getAccountNonLocked()
        );

        preparedStatement.setTimestamp(
                5,
                Timestamp.valueOf(
                        user.getLastPasswordChange()
                )
        );

        preparedStatement.setTimestamp(
                6,
                Timestamp.valueOf(
                        user.getPasswordExpiresAt()
                )
        );

        preparedStatement.setInt(
                7,
                user.getFailedLoginAttempts()
        );

        preparedStatement.setBoolean(
                8,
                user.getEmailVerified()
        );

        preparedStatement.setBoolean(
                9,
                user.getMustChangePassword()
        );

        logger.info("Saliendo de setUserParameters");
    }
}