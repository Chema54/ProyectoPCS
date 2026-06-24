package main.negocio.dto;

public class ExperienciaProfesorDTO {
    
    private final int profesorId;
    private final int experienceId;

    private ExperienciaProfesorDTO(ProfessorExperienceBuilder builder) {
        this.profesorId = builder.profesorId;
        this.experienceId = builder.experienceId;
    }

    public int getProfesorId() {
        return profesorId;
    }

    public int getExperienceId() {
        return experienceId;
    }

    public static class ProfessorExperienceBuilder {

        private int profesorId;
        private int experienceId;

        public ProfessorExperienceBuilder setProfesorId(int professorId) {
            this.profesorId = professorId;
            return this;
        }

        public ProfessorExperienceBuilder setExperienceId(int experienceId) {
            this.experienceId = experienceId;
            return this;
        }

        public ExperienciaProfesorDTO build() {
            return new ExperienciaProfesorDTO(this);
        }
    }
}
