package main.business.dto;

public class ProfesorExperienciaDTO {
    
    private final int professorId;
    private final int experienceId;

    private ProfesorExperienciaDTO(ProfesorExperienciaBuilder builder) {
        this.professorId = builder.professorId;
        this.experienceId = builder.experienceId;
    }

    public int getProfessorId() {
        return professorId;
    }

    public int getExperienceId() {
        return experienceId;
    }

    public static class ProfesorExperienciaBuilder {

        private int professorId;
        private int experienceId;

        public ProfesorExperienciaBuilder setProfessorId(int professorId) {
            this.professorId = professorId;
            return this;
        }

        public ProfesorExperienciaBuilder setExperienceId(int experienceId) {
            this.experienceId = experienceId;
            return this;
        }

        public ProfesorExperienciaDTO build() {
            return new ProfesorExperienciaDTO(this);
        }
    }
}
