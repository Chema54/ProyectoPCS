package main.servicio;

import java.util.List;
import main.negocio.dto.ProyectoDTO;
import main.basedatos.dao.ProyectoDAO;
import main.negocio.dto.ResponsableProyectoDTO;
import main.basedatos.dao.ResponsableProyectoDAO;
import main.comun.ExcepcionMostrableUsuario;

public class ServicioProyecto {

    public static List<ProyectoDTO> getAllProyectos() throws ExcepcionMostrableUsuario {
        ProyectoDAO dao = new ProyectoDAO();
        return dao.getAll();
    }

    public static void registrarNuevoProyecto(ProyectoDTO proyecto, ResponsableProyectoDTO titular) throws ExcepcionMostrableUsuario {
        

        if (proyecto.getCupoTotal() <= 0) {
             throw new ExcepcionMostrableUsuario(
                "Restricción de Proyecto",
                "Cupo Total Inválido",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }
        
        ResponsableProyectoDAO titularDAO = new ResponsableProyectoDAO();
        titularDAO.createOne(titular);
        ResponsableProyectoDTO titularGuardado = titularDAO.getByNumeroPersonal(titular.getNumeroPersonal());

        ProyectoDTO proyectoFinal = new ProyectoDTO.ProyectoBuilder()
                .setNombre(proyecto.getNombre())
                .setTitularId(titularGuardado.getTitularId())
                .setCupoTotal(proyecto.getCupoTotal())
                .setEspaciosDisponibles(proyecto.getEspaciosDisponibles())
                .setEstado(proyecto.getEstado())
                .build();
        
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        proyectoDAO.createOne(proyectoFinal);
    }

    public static void actualizarProyecto(ProyectoDTO proyecto, ResponsableProyectoDTO titular) throws ExcepcionMostrableUsuario {

        if (proyecto.getCupoTotal() <= 0) {
             throw new ExcepcionMostrableUsuario(
                "Restricción de Proyecto",
                "Cupo Total Inválido",
                "Dato asignado tiene un valor invalido, debe seguir un formato asignado"
            );
        }
        
        ResponsableProyectoDAO titularDAO = new ResponsableProyectoDAO();
        titularDAO.updateOne(titular);
        
        ProyectoDTO proyectoFinal = new ProyectoDTO.ProyectoBuilder()
                .setProyectoId(proyecto.getProyectoId())
                .setNombre(proyecto.getNombre())
                .setTitularId(titular.getTitularId())
                .setCupoTotal(proyecto.getCupoTotal())
                .setEspaciosDisponibles(proyecto.getEspaciosDisponibles())
                .setEstado(proyecto.getEstado())
                .build();
        
        ProyectoDAO proyectoDAO = new ProyectoDAO();
        proyectoDAO.updateOne(proyectoFinal);
    }
}
