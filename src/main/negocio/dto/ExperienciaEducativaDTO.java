package main.negocio.dto;

public class ExperienciaEducativaDTO {

    private final int experienciaEducativaId;
    private final String nombre;
    private final Integer periodoId;
    private final String nrc;
    private final String nombrePeriodo;
    private final String nombreProfesor;

    private ExperienciaEducativaDTO(ExperienciaEducativaBuilder builder) {
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

    public static class ExperienciaEducativaBuilder {

        private int experienciaEducativaId;
        private String nombre;
        private Integer periodoId;
        private String nrc;
        private String nombrePeriodo;
        private String nombreProfesor;

        public ExperienciaEducativaBuilder setExperienciaEducativaId(int experienciaEducativaId) {
            this.experienciaEducativaId = experienciaEducativaId;
            return this;
        }

        public ExperienciaEducativaBuilder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public ExperienciaEducativaBuilder setPeriodoId(Integer periodoId) {
            this.periodoId = periodoId;
            return this;
        }

        public ExperienciaEducativaBuilder setNrc(String nrc) {
            this.nrc = nrc;
            return this;
        }

        public ExperienciaEducativaBuilder setNombrePeriodo(String nombrePeriodo) {
            this.nombrePeriodo = nombrePeriodo;
            return this;
        }

        public ExperienciaEducativaBuilder setNombreProfesor(String nombreProfesor) {
            this.nombreProfesor = nombreProfesor;
            return this;
        }

        public ExperienciaEducativaDTO build() {
            return new ExperienciaEducativaDTO(this);
        }
    }
}
