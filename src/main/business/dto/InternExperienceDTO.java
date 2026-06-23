package main.business.dto;

public class InternExperienceDTO {
    
    private final int internId;
    private final int experienceId;

    private InternExperienceDTO(InternExperienceBuilder builder) {
        this.internId = builder.internId;
        this.experienceId = builder.experienceId;
    }

    public int getInternId() {
        return internId;
    }

    public int getExperienceId() {
        return experienceId;
    }

    public static class InternExperienceBuilder {

        private int internId;
        private int experienceId;

        public InternExperienceBuilder setInternId(int internId) {
            this.internId = internId;
            return this;
        }

        public InternExperienceBuilder setExperienceId(int experienceId) {
            this.experienceId = experienceId;
            return this;
        }

        public InternExperienceDTO build() {
            return new InternExperienceDTO(this);
        }
    }
}
