package org.iesalixar.daw2.danielquintero.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad que representa un usuario de la aplicación.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String username;

    private String passwordHash;

    private Boolean active;

    private Boolean accountNonLocked;

    private LocalDateTime lastPasswordChange;

    private LocalDateTime passwordExpiresAt;

    private Integer failedLoginAttempts;

    private Boolean emailVerified;

    private Boolean mustChangePassword;

    /**
     * Constructor utilizado para crear usuarios nuevos sin indicar el id.
     *
     * @param username usuario de acceso
     * @param passwordHash contraseña del usuario
     * @param active indica si la cuenta está activa
     * @param accountNonLocked indica si la cuenta está desbloqueada
     * @param lastPasswordChange fecha del último cambio de contraseña
     * @param passwordExpiresAt fecha de caducidad de la contraseña
     * @param failedLoginAttempts número de intentos fallidos
     * @param emailVerified indica si el email está verificado
     * @param mustChangePassword indica si debe cambiar la contraseña
     */
    public User(
            String username,
            String passwordHash,
            Boolean active,
            Boolean accountNonLocked,
            LocalDateTime lastPasswordChange,
            LocalDateTime passwordExpiresAt,
            Integer failedLoginAttempts,
            Boolean emailVerified,
            Boolean mustChangePassword
    ) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.active = active;
        this.accountNonLocked = accountNonLocked;
        this.lastPasswordChange = lastPasswordChange;
        this.passwordExpiresAt = passwordExpiresAt;
        this.failedLoginAttempts = failedLoginAttempts;
        this.emailVerified = emailVerified;
        this.mustChangePassword = mustChangePassword;
    }
}