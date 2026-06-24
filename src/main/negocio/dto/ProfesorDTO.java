package main.negocio.dto;

public class ProfesorDTO {

    private final int profesorId;
    private final String personalNumber;
    private final String nombre;
    private final String apellidoPaterno;
    private final String apellidoMaterno;
    private final String correo;
    private final String estado;
    private final int usuarioId;
    private final String nombreUsuario;

    private ProfesorDTO(ProfesorBuilder builder) {
        this.profesorId = builder.profesorId;
        this.personalNumber = builder.personalNumber;
        this.nombre = builder.nombre;
        this.apellidoPaterno = builder.apellidoPaterno;
        this.apellidoMaterno = builder.apellidoMaterno;
        this.correo = builder.correo;
        this.estado = builder.estado;
        this.usuarioId = builder.usuarioId;
        this.nombreUsuario = builder.nombreUsuario;
    }

    public int getProfesorId() {
        return profesorId;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public String getCorreo() {
        return correo;
    }

    public String getEstado() {
        return estado;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getFullName() {
        return nombre + " " + apellidoPaterno + (apellidoMaterno != null && !apellidoMaterno.trim().isEmpty() ? " " + apellidoMaterno : "");
    }

    @Override
    public String toString() {
        return nombre + " " + apellidoPaterno + " " + apellidoMaterno;
    }

    public static class ProfesorBuilder {

        private int profesorId;
        private String personalNumber;
        private String nombre;
        private String apellidoPaterno;
        private String apellidoMaterno;
        private String correo;
        private String estado;
        private int usuarioId;
        private String nombreUsuario;

        public ProfesorBuilder setProfesorId(int profesorId) {
            this.profesorId = profesorId;
            return this;
        }

        public ProfesorBuilder setPersonalNumber(String numeroPersonal) {
            this.personalNumber = personalNumber;
            return this;
        }

        public ProfesorBuilder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public ProfesorBuilder setApellidoPaterno(String apellidoPaterno) {
            this.apellidoPaterno = apellidoPaterno;
            return this;
        }

        public ProfesorBuilder setApellidoMaterno(String apellidoMaterno) {
            this.apellidoMaterno = apellidoMaterno;
            return this;
        }

        public ProfesorBuilder setCorreo(String correo) {
            this.correo = correo;
            return this;
        }

        public ProfesorBuilder setEstado(String status) {
            this.estado = status;
            return this;
        }

        public ProfesorBuilder setUsuarioId(int usuarioId) {
            this.usuarioId = usuarioId;
            return this;
        }

        public ProfesorBuilder setNombreUsuario(String nombreUsuario) {
            this.nombreUsuario = nombreUsuario;
            return this;
        }

        public ProfesorDTO build() {
            return new ProfesorDTO(this);
        }
    }
}
