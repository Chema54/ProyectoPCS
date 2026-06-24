package main.negocio.dto;

public class ExperienciaPracticanteDTO {
    
    private final int practicanteId;
    private final int experienciaId;

    private ExperienciaPracticanteDTO(ExperienciaPracticanteBuilder builder) {
        this.practicanteId = builder.practicanteId;
        this.experienciaId = builder.experienciaId;
    }

    public int getPracticanteId() {
        return practicanteId;
    }

    public int getExperienciaId() {
        return experienciaId;
    }

    public static class ExperienciaPracticanteBuilder {

        private int practicanteId;
        private int experienciaId;

        public ExperienciaPracticanteBuilder setPracticanteId(int practicanteId) {
            this.practicanteId = practicanteId;
            return this;
        }

        public ExperienciaPracticanteBuilder setExperienciaId(int experienciaId) {
            this.experienciaId = experienciaId;
            return this;
        }

        public ExperienciaPracticanteDTO build() {
            return new ExperienciaPracticanteDTO(this);
        }
    }
}
