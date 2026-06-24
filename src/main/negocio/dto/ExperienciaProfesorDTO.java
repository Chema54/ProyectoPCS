package main.negocio.dto;

public class ExperienciaProfesorDTO {
    
    private final int profesorId;
    private final int experienciaId;

    private ExperienciaProfesorDTO(ExperienciaProfesorBuilder builder) {
        this.profesorId = builder.profesorId;
        this.experienciaId = builder.experienciaId;
    }

    public int getProfesorId() {
        return profesorId;
    }

    public int getExperienciaId() {
        return experienciaId;
    }

    public static class ExperienciaProfesorBuilder {

        private int profesorId;
        private int experienciaId;

        public ExperienciaProfesorBuilder setProfesorId(int profesorId) {
            this.profesorId = profesorId;
            return this;
        }

        public ExperienciaProfesorBuilder setExperienciaId(int experienciaId) {
            this.experienciaId = experienciaId;
            return this;
        }

        public ExperienciaProfesorDTO build() {
            return new ExperienciaProfesorDTO(this);
        }
    }
}
