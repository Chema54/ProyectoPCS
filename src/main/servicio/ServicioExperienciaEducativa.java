package main.servicio;

import java.util.List;
import main.basedatos.dao.ExperienciaEducativaDAO;
import main.basedatos.dao.PeriodoDAO;
import main.basedatos.dao.ProfesorDAO;
import main.comun.ExcepcionMostrableUsuario;
import main.negocio.dto.ExperienciaEducativaDTO;
import main.negocio.dto.PeriodoDTO;
import main.negocio.dto.ProfesorDTO;

public class ServicioExperienciaEducativa {

    public static List<ExperienciaEducativaDTO> getAllExperiencias() throws ExcepcionMostrableUsuario {
        ExperienciaEducativaDAO dao = new ExperienciaEducativaDAO();
        return dao.getAll();
    }

    public static List<PeriodoDTO> getPeriodosSinAbrir() throws ExcepcionMostrableUsuario {
        PeriodoDAO periodoDAO = new PeriodoDAO();
        return periodoDAO.getClosedPeriods();
    }

    public static List<ProfesorDTO> getProfesoresDisponibles() throws ExcepcionMostrableUsuario {
        ProfesorDAO profesorDAO = new ProfesorDAO();
        return profesorDAO.getAvailableProfessors();
    }

    public static void registrarNuevaExperienciaEducativa(
            ExperienciaEducativaDTO experiencia,
            ProfesorDTO profesor
    ) throws ExcepcionMostrableUsuario {

        validarExperiencia(experiencia, profesor);

        ExperienciaEducativaDAO experienciaDAO = new ExperienciaEducativaDAO();
        PeriodoDAO periodoDAO = new PeriodoDAO();
        ProfesorDAO profesorDAO = new ProfesorDAO();

        PeriodoDTO periodo = periodoDAO.getOne(experiencia.getPeriodoId());
        if (periodo == null) {
            throw new ExcepcionMostrableUsuario(
                    "Periodo no encontrado",
                    "El periodo seleccionado no es válido",
                    "Seleccione un periodo escolar registrado en el sistema."
            );
        }

        if (periodo.getEstado() == null || !"Cerrado".equalsIgnoreCase(periodo.getEstado())) {
            throw new ExcepcionMostrableUsuario(
                    "Periodo no disponible",
                    "El periodo seleccionado no se encuentra sin abrir",
                    "Solo se pueden cargar experiencias educativas en periodos con estado Cerrado."
            );
        }

        ProfesorDTO profesorRegistrado = profesorDAO.getOne(profesor.getProfesorId());
        if (profesorRegistrado == null) {
            throw new ExcepcionMostrableUsuario(
                    "Profesor no encontrado",
                    "El profesor seleccionado no es válido",
                    "Seleccione un profesor registrado en el sistema."
            );
        }

        if (profesorRegistrado.getEstado() == null || !"Activo".equalsIgnoreCase(profesorRegistrado.getEstado())) {
            throw new ExcepcionMostrableUsuario(
                    "Profesor no disponible",
                    "El profesor seleccionado no está disponible",
                    "Seleccione un profesor con estado Activo."
            );
        }

        if (experienciaDAO.existsByNrc(experiencia.getNrc())) {
            throw new ExcepcionMostrableUsuario(
                    "NRC duplicado",
                    "La Experiencia Educativa ya se encuentra registrada",
                    "Ya existe una experiencia educativa registrada con el NRC " + experiencia.getNrc() + "."
            );
        }

        if (experienciaDAO.existsByNameNrcAndPeriod(
                experiencia.getNombre(),
                experiencia.getNrc(),
                experiencia.getPeriodoId())) {
            throw new ExcepcionMostrableUsuario(
                    "Experiencia Educativa repetida",
                    "Esta Experiencia Educativa ya se encuentra registrada",
                    "Ya existe una experiencia educativa con el mismo nombre, NRC y periodo."
            );
        }

        experienciaDAO.createOneWithProfessor(experiencia, profesor.getProfesorId());
    }

    private static void validarExperiencia(
            ExperienciaEducativaDTO experiencia,
            ProfesorDTO profesor
    ) throws ExcepcionMostrableUsuario {

        if (experiencia == null) {
            throw new ExcepcionMostrableUsuario(
                    "Datos incompletos",
                    "No se recibió la información de la Experiencia Educativa",
                    "Capture la información de la experiencia educativa."
            );
        }

        if (experiencia.getNombre() == null || experiencia.getNombre().trim().isEmpty()) {
            throw new ExcepcionMostrableUsuario(
                    "Campos obligatorios",
                    "El nombre es obligatorio",
                    "Capture el nombre de la Experiencia Educativa."
            );
        }

        if (!experiencia.getNombre().matches("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\.\\-]+$")) {
            throw new ExcepcionMostrableUsuario(
                    "Datos inválidos",
                    "El nombre contiene valores inválidos",
                    "El nombre solo puede contener letras, números, espacios, punto y guion."
            );
        }

        if (experiencia.getNrc() == null || experiencia.getNrc().trim().isEmpty()) {
            throw new ExcepcionMostrableUsuario(
                    "Campos obligatorios",
                    "El NRC es obligatorio",
                    "Capture el NRC de la Experiencia Educativa."
            );
        }

        if (!experiencia.getNrc().matches("^\\d{5,10}$")) {
            throw new ExcepcionMostrableUsuario(
                    "Datos inválidos",
                    "El NRC contiene valores inválidos",
                    "El NRC debe ser numérico y tener entre 5 y 10 dígitos."
            );
        }

        if (experiencia.getPeriodoId() == null) {
            throw new ExcepcionMostrableUsuario(
                    "Campos obligatorios",
                    "Debe seleccionar un periodo",
                    "Vincule la Experiencia Educativa a un periodo escolar sin abrir."
            );
        }

        if (profesor == null) {
            throw new ExcepcionMostrableUsuario(
                    "Campos obligatorios",
                    "Debe seleccionar un profesor",
                    "Seleccione el profesor que será asignado a la Experiencia Educativa."
            );
        }
    }
}