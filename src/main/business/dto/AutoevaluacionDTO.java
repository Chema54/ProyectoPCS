package main.business.dto;

import java.math.BigDecimal;

public class AutoevaluacionDTO {

    private final int selfAssessmentId;
    private final int assignmentId;
    private final String deliverableName;
    private final BigDecimal score;
    private final String comments;
    private final String status; // Added because all deliverables have status

    private AutoevaluacionDTO(AutoevaluacionBuilder builder) {
        this.selfAssessmentId = builder.selfAssessmentId;
        this.assignmentId = builder.assignmentId;
        this.deliverableName = builder.deliverableName;
        this.score = builder.score;
        this.comments = builder.comments;
        this.status = builder.status;
    }

    public int getSelfAssessmentId() {
        return selfAssessmentId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public String getDeliverableName() {
        return deliverableName;
    }

    public BigDecimal getScore() {
        return score;
    }

    public String getComments() {
        return comments;
    }
    
    public String getStatus() {
        return status;
    }

    public static class AutoevaluacionBuilder {

        private int selfAssessmentId;
        private int assignmentId;
        private String deliverableName;
        private BigDecimal score;
        private String comments;
        private String status;

        public AutoevaluacionBuilder setSelfAssessmentId(int selfAssessmentId) {
            this.selfAssessmentId = selfAssessmentId;
            return this;
        }

        public AutoevaluacionBuilder setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
            return this;
        }

        public AutoevaluacionBuilder setDeliverableName(String deliverableName) {
            this.deliverableName = deliverableName;
            return this;
        }

        public AutoevaluacionBuilder setScore(BigDecimal score) {
            this.score = score;
            return this;
        }

        public AutoevaluacionBuilder setComments(String comments) {
            this.comments = comments;
            return this;
        }
        
        public AutoevaluacionBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public AutoevaluacionDTO build() {
            return new AutoevaluacionDTO(this);
        }
    }
}
