package main.business.dto;

public class ExperienciaEducativaDTO {

    private final int educationalExperienceId;
    private final String name;
    private final Integer periodId;
    private final String nrc;
    private final String periodName;
    private final String professorName;

    private ExperienciaEducativaDTO(ExperienciaEducativaBuilder builder) {
        this.educationalExperienceId = builder.educationalExperienceId;
        this.name = builder.name;
        this.periodId = builder.periodId;
        this.nrc = builder.nrc;
        this.periodName = builder.periodName;
        this.professorName = builder.professorName;
    }

    public int getEducationalExperienceId() {
        return educationalExperienceId;
    }

    public String getName() {
        return name;
    }

    public Integer getPeriodId() {
        return periodId;
    }

    public String getNrc() {
        return nrc;
    }

    public String getPeriodName() {
        return periodName;
    }

    public String getProfessorName() {
        return professorName;
    }

    @Override
    public String toString() {
        return nrc + " - " + name;
    }

    public static class ExperienciaEducativaBuilder {

        private int educationalExperienceId;
        private String name;
        private Integer periodId;
        private String nrc;
        private String periodName;
        private String professorName;

        public ExperienciaEducativaBuilder setEducationalExperienceId(int educationalExperienceId) {
            this.educationalExperienceId = educationalExperienceId;
            return this;
        }

        public ExperienciaEducativaBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ExperienciaEducativaBuilder setPeriodId(Integer periodId) {
            this.periodId = periodId;
            return this;
        }

        public ExperienciaEducativaBuilder setNrc(String nrc) {
            this.nrc = nrc;
            return this;
        }

        public ExperienciaEducativaBuilder setPeriodName(String periodName) {
            this.periodName = periodName;
            return this;
        }

        public ExperienciaEducativaBuilder setProfessorName(String professorName) {
            this.professorName = professorName;
            return this;
        }

        public ExperienciaEducativaDTO build() {
            return new ExperienciaEducativaDTO(this);
        }
    }
}
