package main.service;

import java.util.List;
import java.util.UUID;
import main.business.dto.PracticanteDTO;
import main.business.dto.UserDTO;
import main.business.dto.enumeration.UserRole;
import main.database.dao.PracticanteDAO;
import main.database.dao.UserDAO;
import main.common.UserDisplayableException;
import main.common.Validator;

public class PracticanteService {

    public static List<PracticanteDTO> getAllPracticantes() throws UserDisplayableException {
        PracticanteDAO dao = new PracticanteDAO();
        return dao.getAll();
    }

    public static void registrarNuevoPracticante(PracticanteDTO practicante) throws UserDisplayableException {
        
        // 1. Validaciones de negocio estrictas
        String enrollment = practicante.getEnrollment();
        String email = practicante.getEmail();
        
        if (!Validator.isValidEnrollment(enrollment)) {
            throw new UserDisplayableException(
                "Restricción de Practicante",
                "Formato de matrícula inválido",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }
        
        if (!Validator.isValidStudentEmail(email)) {
            throw new UserDisplayableException(
                "Restricción de Practicante",
                "Formato de correo electrónico inválido",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }

        if (!Validator.isAlphabeticWithAccents(practicante.getName()) || 
            !Validator.isAlphabeticWithAccents(practicante.getPaternalSurname()) || 
            !Validator.isAlphabeticWithAccents(practicante.getMaternalSurname())) {
            throw new UserDisplayableException(
                "Restricción de Practicante", 
                "El nombre y apellidos solo pueden contener letras y acentos", 
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
        
        // 3. Crear DTO de Usuario y guardarlo para obtener el ID
        UserDTO nuevoUsuario = new UserDTO.UserBuilder()
            .setUsername(practicante.getEnrollment())
            .setPassword(rawPassword)
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

    public static void actualizarPracticante(PracticanteDTO practicante) throws UserDisplayableException {
        // Validaciones
        if (!Validator.isValidEnrollment(practicante.getEnrollment())) {
            throw new UserDisplayableException("Restricción de Practicante", "Formato de matrícula inválido", "Dato asignado tiene un valor invalido");
        }
        if (!Validator.isValidStudentEmail(practicante.getEmail())) {
            throw new UserDisplayableException("Restricción de Practicante", "Formato de correo electrónico inválido", "Dato asignado tiene un valor invalido");
        }
        if (!Validator.isAlphabeticWithAccents(practicante.getName()) || 
            !Validator.isAlphabeticWithAccents(practicante.getPaternalSurname()) || 
            !Validator.isAlphabeticWithAccents(practicante.getMaternalSurname())) {
            throw new UserDisplayableException("Restricción de Practicante", "El nombre y apellidos solo pueden contener letras y acentos", "Dato asignado tiene un valor invalido");
        }

        PracticanteDAO dao = new PracticanteDAO();
        dao.updateOne(practicante);
    }
}
