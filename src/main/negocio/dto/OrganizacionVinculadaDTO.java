package main.negocio.dto;

public class OrganizacionVinculadaDTO {

    private final int organizacionId;
    private final String nombreEmpresa;
    private final String direccion;
    private final String telefono;
    private final String correo;

    private OrganizacionVinculadaDTO(OrganizacionBuilder builder) {
        this.organizacionId = builder.organizacionId;
        this.nombreEmpresa = builder.nombreEmpresa;
        this.direccion = builder.direccion;
        this.telefono = builder.telefono;
        this.correo = builder.correo;
    }

    public int getOrganizacionId() {
        return organizacionId;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    @Override
    public String toString() {
        return nombreEmpresa;
    }

    public static class OrganizacionBuilder {

        private int organizacionId;
        private String nombreEmpresa;
        private String direccion;
        private String telefono;
        private String correo;

        public OrganizacionBuilder setOrganizacionId(int organizacionId) {
            this.organizacionId = organizacionId;
            return this;
        }

        public OrganizacionBuilder setNombreEmpresa(String nombreEmpresa) {
            this.nombreEmpresa = nombreEmpresa;
            return this;
        }

        public OrganizacionBuilder setDireccion(String direccion) {
            this.direccion = direccion;
            return this;
        }

        public OrganizacionBuilder setTelefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public OrganizacionBuilder setCorreo(String correo) {
            this.correo = correo;
            return this;
        }

        public OrganizacionVinculadaDTO build() {
            return new OrganizacionVinculadaDTO(this);
        }
    }
}
