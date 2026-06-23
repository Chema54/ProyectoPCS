package main.business.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class EvaluacionOVDTO {

    private final int linkedOrganizationEvaluationId;
    private final int assignmentId;
    private final String deliverableName;
    private final byte[] file;
    private final String status;
    private final Date deadline;
    private final BigDecimal score;
    private final String comments;

    private EvaluacionOVDTO(EvaluacionOVBuilder builder) {
        this.linkedOrganizationEvaluationId = builder.linkedOrganizationEvaluationId;
        this.assignmentId = builder.assignmentId;
        this.deliverableName = builder.deliverableName;
        this.file = builder.file;
        this.status = builder.status;
        this.deadline = builder.deadline;
        this.score = builder.score;
        this.comments = builder.comments;
    }

    public int getLinkedOrganizationEvaluationId() {
        return linkedOrganizationEvaluationId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public String getDeliverableName() {
        return deliverableName;
    }

    public byte[] getFile() {
        return file;
    }

    public String getStatus() {
        return status;
    }

    public Date getDeadline() {
        return deadline;
    }

    public BigDecimal getScore() {
        return score;
    }

    public String getComments() {
        return comments;
    }

    public static class EvaluacionOVBuilder {

        private int linkedOrganizationEvaluationId;
        private int assignmentId;
        private String deliverableName;
        private byte[] file;
        private String status;
        private Date deadline;
        private BigDecimal score;
        private String comments;

        public EvaluacionOVBuilder setLinkedOrganizationEvaluationId(int linkedOrganizationEvaluationId) {
            this.linkedOrganizationEvaluationId = linkedOrganizationEvaluationId;
            return this;
        }

        public EvaluacionOVBuilder setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
            return this;
        }

        public EvaluacionOVBuilder setDeliverableName(String deliverableName) {
            this.deliverableName = deliverableName;
            return this;
        }

        public EvaluacionOVBuilder setFile(byte[] file) {
            this.file = file;
            return this;
        }

        public EvaluacionOVBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public EvaluacionOVBuilder setDeadline(Date deadline) {
            this.deadline = deadline;
            return this;
        }

        public EvaluacionOVBuilder setScore(BigDecimal score) {
            this.score = score;
            return this;
        }

        public EvaluacionOVBuilder setComments(String comments) {
            this.comments = comments;
            return this;
        }

        public EvaluacionOVDTO build() {
            return new EvaluacionOVDTO(this);
        }
    }
}
