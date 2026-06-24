package main.negocio.dto;

public class ProyectoDTO {

    private final int proyectoId;
    private final String nombre;
    private final Integer titularId;
    private final String estado;
    private final int cupoTotal;
    private final int espaciosDisponibles;
    private final String nombreOrganizacion;
    private final String nombreTitular;

    private ProyectoDTO(ProyectoBuilder builder) {
        this.proyectoId = builder.proyectoId;
        this.nombre = builder.nombre;
        this.titularId = builder.titularId;
        this.estado = builder.estado;
        this.cupoTotal = builder.cupoTotal;
        this.espaciosDisponibles = builder.espaciosDisponibles;
        this.nombreOrganizacion = builder.nombreOrganizacion;
        this.nombreTitular = builder.nombreTitular;
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

    public String getNombreTitular() {
        return nombreTitular;
    }

    @Override
    public String toString() {
        return nombre + " (" + espaciosDisponibles + "/" + cupoTotal + " disponibles)";
    }

    public static class ProyectoBuilder {

        private int proyectoId;
        private String nombre;
        private Integer titularId;
        private String estado;
        private int cupoTotal;
        private int espaciosDisponibles;
        private String nombreOrganizacion;
        private String nombreTitular;

        public ProyectoBuilder setProyectoId(int proyectoId) {
            this.proyectoId = proyectoId;
            return this;
        }

        public ProyectoBuilder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public ProyectoBuilder setTitularId(Integer titularId) {
            this.titularId = titularId;
            return this;
        }

        public ProyectoBuilder setEstado(String status) {
            this.estado = status;
            return this;
        }

        public ProyectoBuilder setCupoTotal(int cupoTotal) {
            this.cupoTotal = cupoTotal;
            return this;
        }

        public ProyectoBuilder setEspaciosDisponibles(int espaciosDisponibles) {
            this.espaciosDisponibles = espaciosDisponibles;
            return this;
        }

        public ProyectoBuilder setNombreOrganizacion(String nombreOrganizacion) {
            this.nombreOrganizacion = nombreOrganizacion;
            return this;
        }

        public ProyectoBuilder setNombreTitular(String nombreTitular) {
            this.nombreTitular = nombreTitular;
            return this;
        }

        public ProyectoDTO build() {
            return new ProyectoDTO(this);
        }
    }
}
