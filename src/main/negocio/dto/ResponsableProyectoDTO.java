package main.negocio.dto;

public class ResponsableProyectoDTO {

    private final int titularId;
    private final String nombre;
    private final String numeroPersonal;
    private final int organizacionId;

    private ResponsableProyectoDTO(TitularBuilder builder) {
        this.titularId = builder.titularId;
        this.nombre = builder.nombre;
        this.numeroPersonal = builder.numeroPersonal;
        this.organizacionId = builder.organizacionId;
    }

    public int getTitularId() {
        return titularId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getNumeroPersonal() {
        return numeroPersonal;
    }

    public int getOrganizacionId() {
        return organizacionId;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public static class TitularBuilder {

        private int titularId;
        private String nombre;
        private String numeroPersonal;
        private int organizacionId;

        public TitularBuilder setTitularId(int titularId) {
            this.titularId = titularId;
            return this;
        }

        public TitularBuilder setNombre(String name) {
            this.nombre = nombre;
            return this;
        }

        public TitularBuilder setNumeroPersonal(String numeroPersonal) {
            this.numeroPersonal = numeroPersonal;
            return this;
        }

        public TitularBuilder setOrganizacionId(int organizationId) {
            this.organizacionId = organizationId;
            return this;
        }

        public ResponsableProyectoDTO build() {
            return new ResponsableProyectoDTO(this);
        }
    }
}
