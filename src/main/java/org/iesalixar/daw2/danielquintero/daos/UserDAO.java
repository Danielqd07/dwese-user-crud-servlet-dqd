package org.iesalixar.daw2.danielquintero.daos;

import org.iesalixar.daw2.danielquintero.entities.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para gestionar las operaciones CRUD de usuarios.
 */
public interface UserDAO {

    /**
     * Devuelve todos los usuarios de la base de datos.
     *
     * @return lista de usuarios
     * @throws SQLException si ocurre un error en la consulta
     */
    List<User> listAllUsers() throws SQLException;

    /**
     * Inserta un nuevo usuario.
     *
     * @param user usuario a insertar
     * @throws SQLException si ocurre un error en la inserción
     */
    void insertUser(User user) throws SQLException;

    /**
     * Comprueba si ya existe un usuario con el username indicado.
     *
     * @param username nombre de usuario
     * @return true si existe, false en caso contrario
     * @throws SQLException si ocurre un error en la consulta
     */
    boolean existsUserByUsername(String username) throws SQLException;

    /**
     * Obtiene un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @return usuario encontrado o null
     * @throws SQLException si ocurre un error en la consulta
     */
    User getUserById(Long id) throws SQLException;

    /**
     * Actualiza un usuario existente.
     *
     * @param user usuario con los datos actualizados
     * @throws SQLException si ocurre un error en la actualización
     */
    void updateUser(User user) throws SQLException;

    /**
     * Comprueba si existe otro usuario con el mismo username,
     * excluyendo el usuario indicado por id.
     *
     * @param username nombre de usuario
     * @param id identificador que se debe excluir
     * @return true si existe otro usuario con ese username
     * @throws SQLException si ocurre un error en la consulta
     */
    boolean existsUserByUsernameAndNotId(String username, Long id)
            throws SQLException;

    /**
     * Elimina un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @throws SQLException si ocurre un error en la eliminación
     */
    void deleteUser(Long id) throws SQLException;
}