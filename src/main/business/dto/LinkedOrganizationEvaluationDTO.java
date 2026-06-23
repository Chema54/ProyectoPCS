package main.business.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class LinkedOrganizationEvaluationDTO {

    private final int linkedOrganizationEvaluationId;
    private final int assignmentId;
    private final String deliverableName;
    private final byte[] file;
    private final String status;
    private final Date deadline;
    private final BigDecimal score;
    private final String comments;

    private LinkedOrganizationEvaluationDTO(LinkedOrganizationEvaluationBuilder builder) {
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

    public static class LinkedOrganizationEvaluationBuilder {

        private int linkedOrganizationEvaluationId;
        private int assignmentId;
        private String deliverableName;
        private byte[] file;
        private String status;
        private Date deadline;
        private BigDecimal score;
        private String comments;

        public LinkedOrganizationEvaluationBuilder setLinkedOrganizationEvaluationId(int linkedOrganizationEvaluationId) {
            this.linkedOrganizationEvaluationId = linkedOrganizationEvaluationId;
            return this;
        }

        public LinkedOrganizationEvaluationBuilder setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
            return this;
        }

        public LinkedOrganizationEvaluationBuilder setDeliverableName(String deliverableName) {
            this.deliverableName = deliverableName;
            return this;
        }

        public LinkedOrganizationEvaluationBuilder setFile(byte[] file) {
            this.file = file;
            return this;
        }

        public LinkedOrganizationEvaluationBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public LinkedOrganizationEvaluationBuilder setDeadline(Date deadline) {
            this.deadline = deadline;
            return this;
        }

        public LinkedOrganizationEvaluationBuilder setScore(BigDecimal score) {
            this.score = score;
            return this;
        }

        public LinkedOrganizationEvaluationBuilder setComments(String comments) {
            this.comments = comments;
            return this;
        }

        public LinkedOrganizationEvaluationDTO build() {
            return new LinkedOrganizationEvaluationDTO(this);
        }
    }
}
