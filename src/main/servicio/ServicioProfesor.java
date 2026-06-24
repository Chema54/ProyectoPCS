package main.servicio;

import java.util.List;
import main.basedatos.dao.ProfesorDAO;
import main.basedatos.dao.UsuarioDAO;
import main.comun.ExcepcionMostrableUsuario;
import main.comun.Validador;
import main.negocio.dto.ProfesorDTO;
import main.negocio.dto.UsuarioDTO;
import main.negocio.dto.enumeracion.RolUsuario;

public class ServicioProfesor {

    public static void registrarNuevoProfesor(
            ProfesorDTO profesor,
            String username,
            String password
    ) throws ExcepcionMostrableUsuario {

        validarProfesor(profesor, username, password);

        ProfesorDAO profesorDAO = new ProfesorDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        if (usuarioDAO.getOne(username) != null) {
            throw new ExcepcionMostrableUsuario(
                    "Usuario Duplicado",
                    "Ya existe un usuario con ese nombre de usuario",
                    "Ingrese un nombre de usuario diferente."
            );
        }

        if (existeNumeroPersonal(profesorDAO.getAll(), profesor.getNumeroPersonal())) {
            throw new ExcepcionMostrableUsuario(
                    "Número de personal duplicado",
                    "El profesor ya existe en el sistema",
                    "Existe un profesor registrado con el mismo número de personal."
            );
        }

        UsuarioDTO nuevoUsuario = new UsuarioDTO.UsuarioBuilder()
                .setNombreUsuario(username)
                .setContrasenia(password)
                .setRol(RolUsuario.PROFESSOR)
                .setAcceso(true)
                .build();

        int idUsuario = usuarioDAO.createOneAndReturnId(nuevoUsuario);

        ProfesorDTO profesorAInsertar = new ProfesorDTO.ProfesorBuilder()
                .setNumeroPersonal(profesor.getNumeroPersonal())
                .setNombre(profesor.getNombre())
                .setApellidoPaterno(profesor.getApellidoPaterno())
                .setApellidoMaterno(profesor.getApellidoMaterno())
                .setCorreo(profesor.getCorreo())
                .setEstado(profesor.getEstado())
                .setUsuarioId(idUsuario)
                .build();

        profesorDAO.createOne(profesorAInsertar);
    }

    private static void validarProfesor(
            ProfesorDTO profesor,
            String username,
            String password
    ) throws ExcepcionMostrableUsuario {

        if (!esNumeroPersonalValido(profesor.getNumeroPersonal())) {
            throw new ExcepcionMostrableUsuario(
                    "Restricción de Profesor",
                    "Formato de número de personal inválido",
                    "El número de personal debe contener solo números y tener entre 3 y 10 dígitos."
            );
        }

        if (!Validador.isAlphabeticWithAccents(profesor.getNombre())
                || !Validador.isAlphabeticWithAccents(profesor.getApellidoPaterno())
                || !Validador.isAlphabeticWithAccents(profesor.getApellidoMaterno())) {
            throw new ExcepcionMostrableUsuario(
                    "Restricción de Profesor",
                    "El nombre y apellidos solo pueden contener letras y acentos",
                    "Dato asignado tiene un valor inválido, debe seguir el formato asignado."
            );
        }

        if (!esCorreoValido(profesor.getCorreo())) {
            throw new ExcepcionMostrableUsuario(
                    "Restricción de Profesor",
                    "Formato de correo electrónico inválido",
                    "Ingrese un correo electrónico válido."
            );
        }

        if (!esUsernameValido(username)) {
            throw new ExcepcionMostrableUsuario(
                    "Restricción de Usuario",
                    "Formato de usuario inválido",
                    "El usuario debe tener de 3 a 50 caracteres y solo puede usar letras, números, guion, guion bajo o punto."
            );
        }

        if (!esPasswordValida(password)) {
            throw new ExcepcionMostrableUsuario(
                    "Restricción de Usuario",
                    "Formato de contraseña inválido",
                    "La contraseña debe tener al menos 4 caracteres y no debe contener espacios, comillas ni diagonal invertida."
            );
        }
    }

    private static boolean existeNumeroPersonal(List<ProfesorDTO> profesores, String numeroPersonal) {
        for (ProfesorDTO profesor : profesores) {
            if (profesor.getNumeroPersonal() != null
                    && profesor.getNumeroPersonal().equalsIgnoreCase(numeroPersonal)) {
                return true;
            }
        }
        return false;
    }

    private static boolean esNumeroPersonalValido(String numeroPersonal) {
        return numeroPersonal != null && numeroPersonal.matches("^\\d{3,10}$");
    }

    private static boolean esCorreoValido(String correo) {
        return correo != null
                && correo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    private static boolean esUsernameValido(String username) {
        return username != null && username.matches("^[A-Za-z0-9._-]{3,50}$");
    }

    private static boolean esPasswordValida(String password) {
        return password != null
                && password.length() >= 4
                && !password.contains(" ")
                && !password.contains("'")
                && !password.contains("\"")
                && !password.contains("\\\\");
    }
}
