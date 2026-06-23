package main.business.dto;

public class ProyectoDTO {

    private final int projectId;
    private final String name;
    private final Integer titularId;
    private final String status;
    private final int totalCapacity;
    private final int availableSpaces;
    private final String organizationName;
    private final String titularDisplay;

    private ProyectoDTO(ProyectoBuilder builder) {
        this.projectId = builder.projectId;
        this.name = builder.name;
        this.titularId = builder.titularId;
        this.status = builder.status;
        this.totalCapacity = builder.totalCapacity;
        this.availableSpaces = builder.availableSpaces;
        this.organizationName = builder.organizationName;
        this.titularDisplay = builder.titularDisplay;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public Integer getTitularId() {
        return titularId;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public int getAvailableSpaces() {
        return availableSpaces;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public String getTitularDisplay() {
        return titularDisplay;
    }

    @Override
    public String toString() {
        return name + " (" + availableSpaces + "/" + totalCapacity + " disponibles)";
    }

    public static class ProyectoBuilder {

        private int projectId;
        private String name;
        private Integer titularId;
        private String status;
        private int totalCapacity;
        private int availableSpaces;
        private String organizationName;
        private String titularDisplay;

        public ProyectoBuilder setProjectId(int projectId) {
            this.projectId = projectId;
            return this;
        }

        public ProyectoBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ProyectoBuilder setTitularId(Integer titularId) {
            this.titularId = titularId;
            return this;
        }

        public ProyectoBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public ProyectoBuilder setTotalCapacity(int totalCapacity) {
            this.totalCapacity = totalCapacity;
            return this;
        }

        public ProyectoBuilder setAvailableSpaces(int availableSpaces) {
            this.availableSpaces = availableSpaces;
            return this;
        }

        public ProyectoBuilder setOrganizationName(String organizationName) {
            this.organizationName = organizationName;
            return this;
        }

        public ProyectoBuilder setTitularDisplay(String titularDisplay) {
            this.titularDisplay = titularDisplay;
            return this;
        }

        public ProyectoDTO build() {
            return new ProyectoDTO(this);
        }
    }
}
