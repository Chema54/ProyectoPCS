package main.negocio.dto;

public class ExperienciaPracticanteDTO {
    
    private final int practicanteId;
    private final int experienceId;

    private ExperienciaPracticanteDTO(InternExperienceBuilder builder) {
        this.practicanteId = builder.practicanteId;
        this.experienceId = builder.experienceId;
    }

    public int getPracticanteId() {
        return practicanteId;
    }

    public int getExperienceId() {
        return experienceId;
    }

    public static class InternExperienceBuilder {

        private int practicanteId;
        private int experienceId;

        public InternExperienceBuilder setPracticanteId(int internId) {
            this.practicanteId = internId;
            return this;
        }

        public InternExperienceBuilder setExperienceId(int experienceId) {
            this.experienceId = experienceId;
            return this;
        }

        public ExperienciaPracticanteDTO build() {
            return new ExperienciaPracticanteDTO(this);
        }
    }
}
