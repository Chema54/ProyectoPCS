package main.negocio.dto;

public class ProyectoDTO {

    private final int proyectoId;
    private final String nombre;
    private final Integer titularId;
    private final String estado;
    private final int cupoTotal;
    private final int espaciosDisponibles;
    private final String nombreOrganizacion;
    private final String titularDisplay;

    private ProyectoDTO(ProjectBuilder builder) {
        this.proyectoId = builder.proyectoId;
        this.nombre = builder.nombre;
        this.titularId = builder.titularId;
        this.estado = builder.estado;
        this.cupoTotal = builder.cupoTotal;
        this.espaciosDisponibles = builder.espaciosDisponibles;
        this.nombreOrganizacion = builder.nombreOrganizacion;
        this.titularDisplay = builder.titularDisplay;
    }

    public int getProyectoId() {
        return proyectoId;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getTitularId() {
        return titularId;
    }

    public String getEstado() {
        return estado;
    }

    public int getCupoTotal() {
        return cupoTotal;
    }

    public int getEspaciosDisponibles() {
        return espaciosDisponibles;
    }

    public String getNombreOrganizacion() {
        return nombreOrganizacion;
    }

    public String getTitularDisplay() {
        return titularDisplay;
    }

    @Override
    public String toString() {
        return nombre + " (" + espaciosDisponibles + "/" + cupoTotal + " disponibles)";
    }

    public static class ProjectBuilder {

        private int proyectoId;
        private String nombre;
        private Integer titularId;
        private String estado;
        private int cupoTotal;
        private int espaciosDisponibles;
        private String nombreOrganizacion;
        private String titularDisplay;

        public ProjectBuilder setProyectoId(int projectId) {
            this.proyectoId = projectId;
            return this;
        }

        public ProjectBuilder setNombre(String name) {
            this.nombre = nombre;
            return this;
        }

        public ProjectBuilder setTitularId(Integer titularId) {
            this.titularId = titularId;
            return this;
        }

        public ProjectBuilder setEstado(String status) {
            this.estado = status;
            return this;
        }

        public ProjectBuilder setCupoTotal(int cupoTotal) {
            this.cupoTotal = cupoTotal;
            return this;
        }

        public ProjectBuilder setEspaciosDisponibles(int espaciosDisponibles) {
            this.espaciosDisponibles = espaciosDisponibles;
            return this;
        }

        public ProjectBuilder setNombreOrganizacion(String organizationName) {
            this.nombreOrganizacion = organizationName;
            return this;
        }

        public ProjectBuilder setTitularDisplay(String titularDisplay) {
            this.titularDisplay = titularDisplay;
            return this;
        }

        public ProyectoDTO build() {
            return new ProyectoDTO(this);
        }
    }
}
