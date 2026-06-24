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

    private AsignacionDTO(AssignmentBuilder builder) {
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

    public static class AssignmentBuilder {

        private int asignacionId;
        private int practicanteId;
        private int proyectoId;
        private int experienciaEducativaId;
        private String estado;
        private String nombreProyecto;
        private String nombrePracticante;
        private String matriculaPracticante;
        private String nrc;

        public AssignmentBuilder setAsignacionId(int asignacionId) {
            this.asignacionId = asignacionId;
            return this;
        }

        public AssignmentBuilder setPracticanteId(int internId) {
            this.practicanteId = internId;
            return this;
        }

        public AssignmentBuilder setProyectoId(int projectId) {
            this.proyectoId = projectId;
            return this;
        }

        public AssignmentBuilder setExperienciaEducativaId(int educationalExperienceId) {
            this.experienciaEducativaId = educationalExperienceId;
            return this;
        }

        public AssignmentBuilder setEstado(String status) {
            this.estado = status;
            return this;
        }

        public AssignmentBuilder setNombreProyecto(String projectName) {
            this.nombreProyecto = projectName;
            return this;
        }

        public AssignmentBuilder setNombrePracticante(String practicanteName) {
            this.nombrePracticante = practicanteName;
            return this;
        }

        public AssignmentBuilder setMatriculaPracticante(String practicanteMatricula) {
            this.matriculaPracticante = practicanteMatricula;
            return this;
        }

        public AssignmentBuilder setNrc(String nrc) {
            this.nrc = nrc;
            return this;
        }

        public AsignacionDTO build() {
            return new AsignacionDTO(this);
        }
    }
}
