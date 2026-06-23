package main.business.dto;

public class ProjectDTO {

    private final int projectId;
    private final String name;
    private final Integer titularId;
    private final String status;
    private final int totalCapacity;
    private final int availableSpaces;
    private final String organizationName;
    private final String titularDisplay;

    private ProjectDTO(ProjectBuilder builder) {
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

    public static class ProjectBuilder {

        private int projectId;
        private String name;
        private Integer titularId;
        private String status;
        private int totalCapacity;
        private int availableSpaces;
        private String organizationName;
        private String titularDisplay;

        public ProjectBuilder setProjectId(int projectId) {
            this.projectId = projectId;
            return this;
        }

        public ProjectBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ProjectBuilder setTitularId(Integer titularId) {
            this.titularId = titularId;
            return this;
        }

        public ProjectBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public ProjectBuilder setTotalCapacity(int totalCapacity) {
            this.totalCapacity = totalCapacity;
            return this;
        }

        public ProjectBuilder setAvailableSpaces(int availableSpaces) {
            this.availableSpaces = availableSpaces;
            return this;
        }

        public ProjectBuilder setOrganizationName(String organizationName) {
            this.organizationName = organizationName;
            return this;
        }

        public ProjectBuilder setTitularDisplay(String titularDisplay) {
            this.titularDisplay = titularDisplay;
            return this;
        }

        public ProjectDTO build() {
            return new ProjectDTO(this);
        }
    }
}
