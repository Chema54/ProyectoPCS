package main.business.dto;

public class PracticanteExperienciaDTO {
    
    private final int internId;
    private final int experienceId;

    private PracticanteExperienciaDTO(PracticanteExperienciaBuilder builder) {
        this.internId = builder.internId;
        this.experienceId = builder.experienceId;
    }

    public int getInternId() {
        return internId;
    }

    public int getExperienceId() {
        return experienceId;
    }

    public static class PracticanteExperienciaBuilder {

        private int internId;
        private int experienceId;

        public PracticanteExperienciaBuilder setInternId(int internId) {
            this.internId = internId;
            return this;
        }

        public PracticanteExperienciaBuilder setExperienceId(int experienceId) {
            this.experienceId = experienceId;
            return this;
        }

        public PracticanteExperienciaDTO build() {
            return new PracticanteExperienciaDTO(this);
        }
    }
}
