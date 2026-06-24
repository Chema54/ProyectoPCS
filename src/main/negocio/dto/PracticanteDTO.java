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

    private PracticanteDTO(PracticanteBuilder builder) {
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

    public String getNombreCompleto() {
        return nombre + " " + apellidoPaterno + (apellidoMaterno != null && !apellidoMaterno.trim().isEmpty() ? " " + apellidoMaterno : "");
    }

    @Override
    public String toString() {
        return matricula + " - " + nombre + " " + apellidoPaterno;
    }

    public static class PracticanteBuilder {

        private int practicanteId;
        private String nombre;
        private String apellidoPaterno;
        private String apellidoMaterno;
        private String correo;
        private String matricula;
        private String estado;
        private int usuarioId;
        private String nombreUsuario;

        public PracticanteBuilder setPracticanteId(int practicanteId) {
            this.practicanteId = practicanteId;
            return this;
        }

        public PracticanteBuilder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public PracticanteBuilder setApellidoPaterno(String apellidoPaterno) {
            this.apellidoPaterno = apellidoPaterno;
            return this;
        }

        public PracticanteBuilder setApellidoMaterno(String apellidoMaterno) {
            this.apellidoMaterno = apellidoMaterno;
            return this;
        }

        public PracticanteBuilder setCorreo(String correo) {
            this.correo = correo;
            return this;
        }

        public PracticanteBuilder setMatricula(String matricula) {
            this.matricula = matricula;
            return this;
        }

        public PracticanteBuilder setEstado(String status) {
            this.estado = status;
            return this;
        }

        public PracticanteBuilder setUsuarioId(int usuarioId) {
            this.usuarioId = usuarioId;
            return this;
        }

        public PracticanteBuilder setNombreUsuario(String nombreUsuario) {
            this.nombreUsuario = nombreUsuario;
            return this;
        }

        public PracticanteDTO build() {
            return new PracticanteDTO(this);
        }
    }
}
