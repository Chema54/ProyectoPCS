package main.business.dto;

public class AsignacionDTO {

    private final int assignmentId;
    private final int internId;
    private final int projectId;
    private final int educationalExperienceId;
    private final String status;

    private AsignacionDTO(AsignacionBuilder builder) {
        this.assignmentId = builder.assignmentId;
        this.internId = builder.internId;
        this.projectId = builder.projectId;
        this.educationalExperienceId = builder.educationalExperienceId;
        this.status = builder.status;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public int getInternId() {
        return internId;
    }

    public int getProjectId() {
        return projectId;
    }

    public int getEducationalExperienceId() {
        return educationalExperienceId;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object instance) {
        if (this == instance) {
            return true;
        }
        if (instance == null || getClass() != instance.getClass()) {
            return false;
        }
        AsignacionDTO that = (AsignacionDTO) instance;
        return assignmentId == that.assignmentId
                && internId == that.internId
                && projectId == that.projectId
                && educationalExperienceId == that.educationalExperienceId
                && status.equals(that.status);
    }

    @Override
    public String toString() {
        return "Asignacion{" + "ID=" + assignmentId + ", status=" + status + '}';
    }

    public static class AsignacionBuilder {

        private int assignmentId;
        private int internId;
        private int projectId;
        private int educationalExperienceId;
        private String status;

        public AsignacionBuilder setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
            return this;
        }

        public AsignacionBuilder setInternId(int internId) {
            this.internId = internId;
            return this;
        }

        public AsignacionBuilder setProjectId(int projectId) {
            this.projectId = projectId;
            return this;
        }

        public AsignacionBuilder setEducationalExperienceId(int educationalExperienceId) {
            this.educationalExperienceId = educationalExperienceId;
            return this;
        }

        public AsignacionBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public AsignacionDTO build() {
            return new AsignacionDTO(this);
        }
    }
}
