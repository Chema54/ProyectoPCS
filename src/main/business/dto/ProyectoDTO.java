package main.business.dto;

public class ProyectoDTO {

    private final int projectId;
    private final String name;
    private final int titularId;
    private final String status;
    private final int totalCapacity;
    private final int availableSpaces;

    private ProyectoDTO(ProyectoBuilder builder) {
        this.projectId = builder.projectId;
        this.name = builder.name;
        this.titularId = builder.titularId;
        this.status = builder.status;
        this.totalCapacity = builder.totalCapacity;
        this.availableSpaces = builder.availableSpaces;
    }

    public int getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public int getTitularId() {
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

    @Override
    public boolean equals(Object instance) {
        if (this == instance) {
            return true;
        }
        if (instance == null || getClass() != instance.getClass()) {
            return false;
        }
        ProyectoDTO that = (ProyectoDTO) instance;
        return projectId == that.projectId
                && titularId == that.titularId
                && totalCapacity == that.totalCapacity
                && availableSpaces == that.availableSpaces
                && name.equals(that.name)
                && status.equals(that.status);
    }

    @Override
    public String toString() {
        return name + " (" + availableSpaces + "/" + totalCapacity + " disponibles)";
    }

    public static class ProyectoBuilder {

        private int projectId;
        private String name;
        private int titularId;
        private String status;
        private int totalCapacity;
        private int availableSpaces;

        public ProyectoBuilder setProjectId(int projectId) {
            this.projectId = projectId;
            return this;
        }

        public ProyectoBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ProyectoBuilder setTitularId(int titularId) {
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

        public ProyectoDTO build() {
            return new ProyectoDTO(this);
        }
    }
}
