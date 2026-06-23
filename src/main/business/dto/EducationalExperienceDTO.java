package main.business.dto;

public class EducationalExperienceDTO {

    private final int educationalExperienceId;
    private final String name;
    private final Integer periodId;
    private final String nrc;
    private final String periodName;
    private final String professorName;

    private EducationalExperienceDTO(EducationalExperienceBuilder builder) {
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

    public static class EducationalExperienceBuilder {

        private int educationalExperienceId;
        private String name;
        private Integer periodId;
        private String nrc;
        private String periodName;
        private String professorName;

        public EducationalExperienceBuilder setEducationalExperienceId(int educationalExperienceId) {
            this.educationalExperienceId = educationalExperienceId;
            return this;
        }

        public EducationalExperienceBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public EducationalExperienceBuilder setPeriodId(Integer periodId) {
            this.periodId = periodId;
            return this;
        }

        public EducationalExperienceBuilder setNrc(String nrc) {
            this.nrc = nrc;
            return this;
        }

        public EducationalExperienceBuilder setPeriodName(String periodName) {
            this.periodName = periodName;
            return this;
        }

        public EducationalExperienceBuilder setProfessorName(String professorName) {
            this.professorName = professorName;
            return this;
        }

        public EducationalExperienceDTO build() {
            return new EducationalExperienceDTO(this);
        }
    }
}
