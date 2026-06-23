package main.business.dto;

public class AssignmentDTO {

    private final int assignmentId;
    private final int internId;
    private final int projectId;
    private final int educationalExperienceId;
    private final String status;
    private final String projectName;
    private final String practicanteName;
    private final String practicanteMatricula;
    private final String nrc;

    private AssignmentDTO(AssignmentBuilder builder) {
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

    public static class AssignmentBuilder {

        private int assignmentId;
        private int internId;
        private int projectId;
        private int educationalExperienceId;
        private String status;
        private String projectName;
        private String practicanteName;
        private String practicanteMatricula;
        private String nrc;

        public AssignmentBuilder setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
            return this;
        }

        public AssignmentBuilder setInternId(int internId) {
            this.internId = internId;
            return this;
        }

        public AssignmentBuilder setProjectId(int projectId) {
            this.projectId = projectId;
            return this;
        }

        public AssignmentBuilder setEducationalExperienceId(int educationalExperienceId) {
            this.educationalExperienceId = educationalExperienceId;
            return this;
        }

        public AssignmentBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public AssignmentBuilder setProjectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public AssignmentBuilder setPracticanteName(String practicanteName) {
            this.practicanteName = practicanteName;
            return this;
        }

        public AssignmentBuilder setPracticanteMatricula(String practicanteMatricula) {
            this.practicanteMatricula = practicanteMatricula;
            return this;
        }

        public AssignmentBuilder setNrc(String nrc) {
            this.nrc = nrc;
            return this;
        }

        public AssignmentDTO build() {
            return new AssignmentDTO(this);
        }
    }
}
