package main.negocio.dto;

public class OrganizacionVinculadaDTO {

    private final int organizacionId;
    private final String nombreEmpresa;
    private final String direccion;
    private final String telefono;
    private final String correo;

    private OrganizacionVinculadaDTO(LinkedOrganizationBuilder builder) {
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

    public static class LinkedOrganizationBuilder {

        private int organizacionId;
        private String nombreEmpresa;
        private String direccion;
        private String telefono;
        private String correo;

        public LinkedOrganizationBuilder setOrganizacionId(int organizationId) {
            this.organizacionId = organizationId;
            return this;
        }

        public LinkedOrganizationBuilder setNombreEmpresa(String businessName) {
            this.nombreEmpresa = businessName;
            return this;
        }

        public LinkedOrganizationBuilder setDireccion(String location) {
            this.direccion = location;
            return this;
        }

        public LinkedOrganizationBuilder setTelefono(String phoneNumber) {
            this.telefono = phoneNumber;
            return this;
        }

        public LinkedOrganizationBuilder setCorreo(String email) {
            this.correo = correo;
            return this;
        }

        public OrganizacionVinculadaDTO build() {
            return new OrganizacionVinculadaDTO(this);
        }
    }
}
