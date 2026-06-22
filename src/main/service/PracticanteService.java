package main.service;

import java.util.List;
import java.util.UUID;
import main.business.dto.PracticanteDTO;
import main.business.dto.UserDTO;
import main.business.dto.enumeration.UserRole;
import main.database.dao.PracticanteDAO;
import main.database.dao.UserDAO;
import main.common.UserDisplayableException;

public class PracticanteService {

    public static List<PracticanteDTO> getAllPracticantes() throws UserDisplayableException {
        PracticanteDAO dao = new PracticanteDAO();
        return dao.getAll();
    }

    public static void registrarNuevoPracticante(PracticanteDTO practicante) throws UserDisplayableException {
        
        // 1. Validaciones de negocio estrictas
        String enrollment = practicante.getEnrollment();
        String email = practicante.getEmail();
        
        if (enrollment == null || !enrollment.matches("^S\\d{8}$")) {
            throw new UserDisplayableException(
                "Restricción de Practicante",
                "Formato de matrícula inválido",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }
        
        if (email == null || !email.matches("^zS\\d{8}@estudiantes\\.uv\\.mx$")) {
            throw new UserDisplayableException(
                "Restricción de Practicante",
                "Formato de correo electrónico inválido",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }
        
        PracticanteDAO practicanteDAO = new PracticanteDAO();
        if (practicanteDAO.isEnrollmentRegistered(enrollment)) {
            throw new UserDisplayableException(
                "Matrícula Duplicada",
                "El Practicante ya existe en el sistema",
                "Existe un Practicante Registrado con la misma matricula"
            );
        }
        
        // 2. Generación automática de contraseña
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        String hashedPassword = UserDTO.getGeneratedHashedPassword(rawPassword);
        
        // 3. Crear DTO de Usuario y guardarlo para obtener el ID
        UserDTO nuevoUsuario = new UserDTO.UserBuilder()
            .setUsername(practicante.getEnrollment())
            .setPassword(hashedPassword)
            .setRole(UserRole.INTERN)
            .setAccess(true)
            .build();
            
        UserDAO userDAO = new UserDAO();
        int nuevoIdUsuario = userDAO.createOneAndReturnId(nuevoUsuario);
        
        // 4. Actualizar el DTO del Practicante con el ID del usuario generado
        PracticanteDTO practicanteAInsertar = new PracticanteDTO.PracticanteBuilder()
            .setInternId(practicante.getInternId())
            .setName(practicante.getName())
            .setPaternalSurname(practicante.getPaternalSurname())
            .setMaternalSurname(practicante.getMaternalSurname())
            .setEmail(practicante.getEmail())
            .setEnrollment(practicante.getEnrollment())
            .setStatus(practicante.getStatus())
            .setUserId(nuevoIdUsuario)
            .build();
        
        // 5. Guardar Practicante en la BD
        practicanteDAO.createOne(practicanteAInsertar);
    }
}
