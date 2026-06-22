package main.business.dto;

public class AsignacionDTO {

    private final int assignmentId;
    private final int internId;
    private final int projectId;
    private final int educationalExperienceId;
    private final String status;
    private final String projectName;
    private final String practicanteName;
    private final String practicanteMatricula;
    private final String nrc;

    private AsignacionDTO(AsignacionBuilder builder) {
        this.assignmentId = builder.assignmentId;
        this.internId = builder.internId;
        this.projectId = builder.projectId;
        this.educationalExperienceId = builder.educationalExperienceId;
        this.status = builder.status;
        this.projectName = builder.projectName;
        this.practicanteName = builder.practicanteName;
        this.practicanteMatricula = builder.practicanteMatricula;
        this.nrc = builder.nrc;
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

    public String getProjectName() {
        return projectName;
    }

    public String getPracticanteName() {
        return practicanteName;
    }

    public String getPracticanteMatricula() {
        return practicanteMatricula;
    }

    public String getNrc() {
        return nrc;
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
        private String projectName;
        private String practicanteName;
        private String practicanteMatricula;
        private String nrc;

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

        public AsignacionBuilder setProjectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public AsignacionBuilder setPracticanteName(String practicanteName) {
            this.practicanteName = practicanteName;
            return this;
        }

        public AsignacionBuilder setPracticanteMatricula(String practicanteMatricula) {
            this.practicanteMatricula = practicanteMatricula;
            return this;
        }

        public AsignacionBuilder setNrc(String nrc) {
            this.nrc = nrc;
            return this;
        }

        public AsignacionDTO build() {
            return new AsignacionDTO(this);
        }
    }
}
