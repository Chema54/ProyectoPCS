package main.negocio.dto;

public class PracticanteDTO {

    private final int practicanteId;
    private final String nombre;
    private final String apellidoPaterno;
    private final String apellidoMaterno;
    private final String correo;
    private final String matricula;
    private final String estado;
    private final int usuarioId;
    private final String nombreUsuario;

    private PracticanteDTO(InternBuilder builder) {
        this.practicanteId = builder.practicanteId;
        this.nombre = builder.nombre;
        this.apellidoPaterno = builder.apellidoPaterno;
        this.apellidoMaterno = builder.apellidoMaterno;
        this.correo = builder.correo;
        this.matricula = builder.matricula;
        this.estado = builder.estado;
        this.usuarioId = builder.usuarioId;
        this.nombreUsuario = builder.nombreUsuario;
    }

    public int getPracticanteId() {
        return practicanteId;
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

    public String getMatricula() {
        return matricula;
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
        return matricula + " - " + nombre + " " + apellidoPaterno;
    }

    public static class InternBuilder {

        private int practicanteId;
        private String nombre;
        private String apellidoPaterno;
        private String apellidoMaterno;
        private String correo;
        private String matricula;
        private String estado;
        private int usuarioId;
        private String nombreUsuario;

        public InternBuilder setPracticanteId(int internId) {
            this.practicanteId = internId;
            return this;
        }

        public InternBuilder setNombre(String name) {
            this.nombre = nombre;
            return this;
        }

        public InternBuilder setApellidoPaterno(String apellidoPaterno) {
            this.apellidoPaterno = apellidoPaterno;
            return this;
        }

        public InternBuilder setApellidoMaterno(String apellidoMaterno) {
            this.apellidoMaterno = apellidoMaterno;
            return this;
        }

        public InternBuilder setCorreo(String email) {
            this.correo = correo;
            return this;
        }

        public InternBuilder setMatricula(String enrollment) {
            this.matricula = enrollment;
            return this;
        }

        public InternBuilder setEstado(String status) {
            this.estado = status;
            return this;
        }

        public InternBuilder setUsuarioId(int userId) {
            this.usuarioId = userId;
            return this;
        }

        public InternBuilder setNombreUsuario(String username) {
            this.nombreUsuario = nombreUsuario;
            return this;
        }

        public PracticanteDTO build() {
            return new PracticanteDTO(this);
        }
    }
}
