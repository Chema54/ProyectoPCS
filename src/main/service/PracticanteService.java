package main.service;

import java.util.UUID;
import main.business.dto.PracticanteDTO;
import main.business.dto.UserDTO;
import main.business.dto.enumeration.UserRole;
import main.database.dao.PracticanteDAO;
import main.database.dao.UserDAO;
import main.common.UserDisplayableException;

public class PracticanteService {

    public static void registrarNuevoPracticante(PracticanteDTO practicante) throws UserDisplayableException {
        
        // 1. Validaciones de negocio (Si aplica, ej. verificar que la matrícula no exista)
        
        // 2. Generación automática de contraseña
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        String hashedPassword = UserDTO.getGeneratedHashedPassword(rawPassword);
        
        // 3. Crear DTO de Usuario y guardarlo para obtener el ID
        UserDTO nuevoUsuario = new UserDTO.UserBuilder()
            .setUsername(practicante.getEnrollment()) // Usamos matrícula como username
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
        PracticanteDAO practicanteDAO = new PracticanteDAO();
        practicanteDAO.createOne(practicanteAInsertar);
    }
}
