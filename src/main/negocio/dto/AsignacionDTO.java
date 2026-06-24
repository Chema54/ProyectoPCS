package main.negocio.dto;

public class AsignacionDTO {

    private final int asignacionId;
    private final int practicanteId;
    private final int proyectoId;
    private final int experienciaEducativaId;
    private final String estado;
    private final String nombreProyecto;
    private final String nombrePracticante;
    private final String matriculaPracticante;
    private final String nrc;

    private AsignacionDTO(AsignacionBuilder builder) {
        this.asignacionId = builder.asignacionId;
        this.practicanteId = builder.practicanteId;
        this.proyectoId = builder.proyectoId;
        this.experienciaEducativaId = builder.experienciaEducativaId;
        this.estado = builder.estado;
        this.nombreProyecto = builder.nombreProyecto;
        this.nombrePracticante = builder.nombrePracticante;
        this.matriculaPracticante = builder.matriculaPracticante;
        this.nrc = builder.nrc;
    }

    public int getAsignacionId() {
        return asignacionId;
    }

    public int getPracticanteId() {
        return practicanteId;
    }

    public int getProyectoId() {
        return proyectoId;
    }

    public int getExperienciaEducativaId() {
        return experienciaEducativaId;
    }

    public String getEstado() {
        return estado;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public String getNombrePracticante() {
        return nombrePracticante;
    }

    public String getMatriculaPracticante() {
        return matriculaPracticante;
    }

    public String getNrc() {
        return nrc;
    }

    @Override
    public String toString() {
        return "Asignacion{" + "ID=" + asignacionId + ", estado=" + estado + '}';
    }

    public static class AsignacionBuilder {

        private int asignacionId;
        private int practicanteId;
        private int proyectoId;
        private int experienciaEducativaId;
        private String estado;
        private String nombreProyecto;
        private String nombrePracticante;
        private String matriculaPracticante;
        private String nrc;

        public AsignacionBuilder setAsignacionId(int asignacionId) {
            this.asignacionId = asignacionId;
            return this;
        }

        public AsignacionBuilder setPracticanteId(int practicanteId) {
            this.practicanteId = practicanteId;
            return this;
        }

        public AsignacionBuilder setProyectoId(int proyectoId) {
            this.proyectoId = proyectoId;
            return this;
        }

        public AsignacionBuilder setExperienciaEducativaId(int experienciaEducativaId) {
            this.experienciaEducativaId = experienciaEducativaId;
            return this;
        }

        public AsignacionBuilder setEstado(String status) {
            this.estado = status;
            return this;
        }

        public AsignacionBuilder setNombreProyecto(String nombreProyecto) {
            this.nombreProyecto = nombreProyecto;
            return this;
        }

        public AsignacionBuilder setNombrePracticante(String nombrePracticante) {
            this.nombrePracticante = nombrePracticante;
            return this;
        }

        public AsignacionBuilder setMatriculaPracticante(String matriculaPracticante) {
            this.matriculaPracticante = matriculaPracticante;
            return this;
        }

        public AsignacionBuilder setNrc(String nrc) {
            this.nrc = nrc;
            return this;
        }

        public AsignacionDTO build() {
            return new AsignacionDTO(this);
        }
    }
}
