package servicio;

import dades.UsuarioDB;
import modelo.Usuario;
import util.PasswordUtil;
import java.sql.SQLException;

public class AuthServicio {

    private final UsuarioDB usuarioDB;

    public AuthServicio() {
        this.usuarioDB = new UsuarioDB();
    }



    public Usuario login(String nombre, String password) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }

        try {
            Usuario usuario = usuarioDB.buscarPorNombre(nombre.trim());

            if (usuario == null) {
                throw new IllegalArgumentException("Usuario no encontrado.");
            }
            if (!PasswordUtil.verificar(password, usuario.getPasswordHash())) {
                throw new IllegalArgumentException("Contraseña incorrecta.");
            }

            return usuario;

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }


    public Usuario registrar(String nombre, String password, String confirmacion) {
        validarDatosRegistro(nombre, password, confirmacion);

        try {
            if (usuarioDB.existeNombre(nombre.trim())) {
                throw new IllegalArgumentException(
                        "El nombre de usuario '" + nombre.trim() + "' ya está en uso."
                );
            }

            String hash = PasswordUtil.hashSHA256(password);
            Usuario nuevo = new Usuario(nombre.trim(), hash);
            usuarioDB.insertar(nuevo);
            return nuevo;

        } catch (SQLException e) {
            throw new RuntimeException("Error de base de datos: " + e.getMessage(), e);
        }
    }

    private void validarDatosRegistro(String nombre, String password, String confirmacion) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (nombre.trim().length() < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 caracteres.");
        }
        if (nombre.trim().length() > 50) {
            throw new IllegalArgumentException("El nombre no puede superar 50 caracteres.");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres.");
        }
        if (!password.equals(confirmacion)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }
    }
}
