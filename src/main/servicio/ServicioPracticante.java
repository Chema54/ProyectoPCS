package main.servicio;

import java.util.List;
import java.util.UUID;
import main.negocio.dto.PracticanteDTO;
import main.negocio.dto.UsuarioDTO;
import main.negocio.dto.enumeracion.RolUsuario;
import main.basedatos.dao.PracticanteDAO;
import main.basedatos.dao.UsuarioDAO;
import main.comun.ExcepcionMostrableUsuario;
import main.comun.Validador;

public class ServicioPracticante {

    public static List<PracticanteDTO> getAllPracticantes() throws ExcepcionMostrableUsuario {
        PracticanteDAO dao = new PracticanteDAO();
        return dao.getAll();
    }

    public static void registrarNuevoPracticante(PracticanteDTO practicante) throws ExcepcionMostrableUsuario {
        
        String matricula = practicante.getMatricula();
        String email = practicante.getCorreo();

        
        if (!Validador.isValidEnrollment(matricula)) {
            throw new ExcepcionMostrableUsuario(
                "Restricción de Practicante",
                "Formato de matrícula inválido",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }
        
        if (!Validador.isValidStudentEmail(email)) {
            throw new ExcepcionMostrableUsuario(
                "Restricción de Practicante",
                "Formato de correo electrónico inválido",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }

        if (!Validador.isAlphabeticWithAccents(practicante.getNombre()) || 
            !Validador.isAlphabeticWithAccents(practicante.getApellidoPaterno()) || 
            !Validador.isAlphabeticWithAccents(practicante.getApellidoMaterno())) {
            throw new ExcepcionMostrableUsuario(
                "Restricción de Practicante", 
                "El nombre y apellidos solo pueden contener letras y acentos", 
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }
        
        PracticanteDAO practicanteDAO = new PracticanteDAO();
        if (practicanteDAO.isEnrollmentRegistered(matricula)) {
            throw new ExcepcionMostrableUsuario(
                "Matrícula Duplicada",
                "El Practicante ya existe en el sistema",
                "Existe un Practicante Registrado con la misma matricula"
            );
        }
        
        // Generación automática de contraseña inicial
        String rawPassword = practicante.getMatricula().toLowerCase();
        
        UsuarioDTO nuevoUsuario = new UsuarioDTO.UsuarioBuilder()
            .setNombreUsuario(practicante.getMatricula())
            .setPassword(rawPassword)
            .setRole(RolUsuario.INTERN)
            .setAccess(true)
            .build();
            
        UsuarioDAO userDAO = new UsuarioDAO();
        int nuevoIdUsuario = userDAO.createOneAndReturnId(nuevoUsuario);
        
        PracticanteDTO practicanteAInsertar = new PracticanteDTO.PracticanteBuilder()
            .setPracticanteId(practicante.getPracticanteId())
            .setNombre(practicante.getNombre())
            .setApellidoPaterno(practicante.getApellidoPaterno())
            .setApellidoMaterno(practicante.getApellidoMaterno())
            .setCorreo(practicante.getCorreo())
            .setMatricula(practicante.getMatricula())
            .setEstado(practicante.getEstado())
            .setUsuarioId(nuevoIdUsuario)
            .build();
        
        practicanteDAO.createOne(practicanteAInsertar);
    }

    public static void actualizarPracticante(PracticanteDTO practicante) throws ExcepcionMostrableUsuario {
        if (!Validador.isValidEnrollment(practicante.getMatricula())) {
            throw new ExcepcionMostrableUsuario("Restricción de Practicante", "Formato de matrícula inválido", "Dato asignado tiene un valor invalido");
        }
        if (!Validador.isValidStudentEmail(practicante.getCorreo())) {
            throw new ExcepcionMostrableUsuario("Restricción de Practicante", "Formato de correo electrónico inválido", "Dato asignado tiene un valor invalido");
        }
        if (!Validador.isAlphabeticWithAccents(practicante.getNombre()) || 
            !Validador.isAlphabeticWithAccents(practicante.getApellidoPaterno()) || 
            !Validador.isAlphabeticWithAccents(practicante.getApellidoMaterno())) {
            throw new ExcepcionMostrableUsuario("Restricción de Practicante", "El nombre y apellidos solo pueden contener letras y acentos", "Dato asignado tiene un valor invalido");
        }

        PracticanteDAO dao = new PracticanteDAO();
        dao.updateOne(practicante);
    }
}
