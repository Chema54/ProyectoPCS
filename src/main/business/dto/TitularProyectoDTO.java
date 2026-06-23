package main.business.dto;

public class TitularProyectoDTO {

    private final int titularId;
    private final String name;
    private final String numeroPersonal;
    private final int organizationId;

    private TitularProyectoDTO(TitularBuilder builder) {
        this.titularId = builder.titularId;
        this.name = builder.name;
        this.numeroPersonal = builder.numeroPersonal;
        this.organizationId = builder.organizationId;
    }

    public int getTitularId() {
        return titularId;
    }

    public String getName() {
        return name;
    }

    public String getNumeroPersonal() {
        return numeroPersonal;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    @Override
    public String toString() {
        return name;
    }

    public static class TitularBuilder {

        private int titularId;
        private String name;
        private String numeroPersonal;
        private int organizationId;

        public TitularBuilder setTitularId(int titularId) {
            this.titularId = titularId;
            return this;
        }

        public TitularBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public TitularBuilder setNumeroPersonal(String numeroPersonal) {
            this.numeroPersonal = numeroPersonal;
            return this;
        }

        public TitularBuilder setOrganizationId(int organizationId) {
            this.organizationId = organizationId;
            return this;
        }

        public TitularProyectoDTO build() {
            return new TitularProyectoDTO(this);
        }
    }
}
