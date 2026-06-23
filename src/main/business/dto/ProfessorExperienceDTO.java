package main.business.dto;

public class ProfessorExperienceDTO {
    
    private final int professorId;
    private final int experienceId;

    private ProfessorExperienceDTO(ProfessorExperienceBuilder builder) {
        this.professorId = builder.professorId;
        this.experienceId = builder.experienceId;
    }

    public int getProfessorId() {
        return professorId;
    }

    public int getExperienceId() {
        return experienceId;
    }

    public static class ProfessorExperienceBuilder {

        private int professorId;
        private int experienceId;

        public ProfessorExperienceBuilder setProfessorId(int professorId) {
            this.professorId = professorId;
            return this;
        }

        public ProfessorExperienceBuilder setExperienceId(int experienceId) {
            this.experienceId = experienceId;
            return this;
        }

        public ProfessorExperienceDTO build() {
            return new ProfessorExperienceDTO(this);
        }
    }
}
