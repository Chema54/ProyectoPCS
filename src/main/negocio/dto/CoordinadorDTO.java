package main.negocio.dto;

public class CoordinadorDTO {

    private final int coordinadorId;
    private final int usuarioId;
    private final String nombreUsuario;
    private final String numeroPersonal;
    private final String nombre;
    private final String apellidoPaterno;
    private final String apellidoMaterno;
    private final String correo;

    public CoordinadorDTO(CoordinadorBuilder builder) {
        this.coordinadorId = builder.coordinadorId;
        this.usuarioId = builder.usuarioId;
        this.nombreUsuario = builder.nombreUsuario;
        this.numeroPersonal = builder.numeroPersonal;
        this.nombre = builder.nombre;
        this.apellidoPaterno = builder.apellidoPaterno;
        this.apellidoMaterno = builder.apellidoMaterno;
        this.correo = builder.correo;
    }

    // Getters
    public int getCoordinadorId() {
        return coordinadorId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getNumeroPersonal() {
        return numeroPersonal;
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

    public static class CoordinadorBuilder {

        protected int coordinadorId;
        protected int usuarioId;
        protected String nombreUsuario;
        protected String numeroPersonal;
        protected String nombre;
        protected String apellidoPaterno;
        protected String apellidoMaterno;
        protected String correo;

        public CoordinadorBuilder setCoordinadorId(int coordinadorId) {
            this.coordinadorId = coordinadorId;
            return this;
        }

        public CoordinadorBuilder setUsuarioId(int usuarioId) {
            this.usuarioId = usuarioId;
            return this;
        }

        public CoordinadorBuilder setNombreUsuario(String nombreUsuario) {
            this.nombreUsuario = nombreUsuario;
            return this;
        }

        public CoordinadorBuilder setNumeroPersonal(String numeroPersonal) {
            this.numeroPersonal = numeroPersonal;
            return this;
        }

        public CoordinadorBuilder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public CoordinadorBuilder setApellidoPaterno(String apellidoPaterno) {
            this.apellidoPaterno = apellidoPaterno;
            return this;
        }

        public CoordinadorBuilder setApellidoMaterno(String apellidoMaterno) {
            this.apellidoMaterno = apellidoMaterno;
            return this;
        }

        public CoordinadorBuilder setCorreo(String correo) {
            this.correo = correo;
            return this;
        }

        public CoordinadorDTO build() {
            return new CoordinadorDTO(this);
        }
    }

    @Override
    public String toString() {
        return nombre + " " + apellidoPaterno + " (" + numeroPersonal + ")";
    }
}
