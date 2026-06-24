package main.negocio.dto;

public class ExperienciaEducativaDTO {

    private final int experienciaEducativaId;
    private final String nombre;
    private final Integer periodoId;
    private final String nrc;
    private final String nombrePeriodo;
    private final String nombreProfesor;

    private ExperienciaEducativaDTO(EducationalExperienceBuilder builder) {
        this.experienciaEducativaId = builder.experienciaEducativaId;
        this.nombre = builder.nombre;
        this.periodoId = builder.periodoId;
        this.nrc = builder.nrc;
        this.nombrePeriodo = builder.nombrePeriodo;
        this.nombreProfesor = builder.nombreProfesor;
    }

    public int getExperienciaEducativaId() {
        return experienciaEducativaId;
    }

    public String getNombre() {
        return nombre;
    }

    public Integer getPeriodoId() {
        return periodoId;
    }

    public String getNrc() {
        return nrc;
    }

    public String getNombrePeriodo() {
        return nombrePeriodo;
    }

    public String getNombreProfesor() {
        return nombreProfesor;
    }

    @Override
    public String toString() {
        return nrc + " - " + nombre;
    }

    public static class EducationalExperienceBuilder {

        private int experienciaEducativaId;
        private String nombre;
        private Integer periodoId;
        private String nrc;
        private String nombrePeriodo;
        private String nombreProfesor;

        public EducationalExperienceBuilder setExperienciaEducativaId(int educationalExperienceId) {
            this.experienciaEducativaId = educationalExperienceId;
            return this;
        }

        public EducationalExperienceBuilder setNombre(String name) {
            this.nombre = nombre;
            return this;
        }

        public EducationalExperienceBuilder setPeriodoId(Integer periodId) {
            this.periodoId = periodId;
            return this;
        }

        public EducationalExperienceBuilder setNrc(String nrc) {
            this.nrc = nrc;
            return this;
        }

        public EducationalExperienceBuilder setNombrePeriodo(String periodName) {
            this.nombrePeriodo = periodName;
            return this;
        }

        public EducationalExperienceBuilder setNombreProfesor(String professorName) {
            this.nombreProfesor = professorName;
            return this;
        }

        public ExperienciaEducativaDTO build() {
            return new ExperienciaEducativaDTO(this);
        }
    }
}
