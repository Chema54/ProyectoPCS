package main.business.dto;

public class TitularProyectoDTO {

    private final int titularId;
    private final String name;
    private final int organizationId;

    private TitularProyectoDTO(TitularBuilder builder) {
        this.titularId = builder.titularId;
        this.name = builder.name;
        this.organizationId = builder.organizationId;
    }

    public int getTitularId() {
        return titularId;
    }

    public String getName() {
        return name;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    @Override
    public boolean equals(Object instance) {
        if (this == instance) {
            return true;
        }
        if (instance == null || getClass() != instance.getClass()) {
            return false;
        }
        TitularProyectoDTO that = (TitularProyectoDTO) instance;
        return titularId == that.titularId
                && organizationId == that.organizationId
                && name.equals(that.name);
    }

    @Override
    public String toString() {
        return name;
    }

    public static class TitularBuilder {

        private int titularId;
        private String name;
        private int organizationId;

        public TitularBuilder setTitularId(int titularId) {
            this.titularId = titularId;
            return this;
        }

        public TitularBuilder setName(String name) {
            this.name = name;
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
