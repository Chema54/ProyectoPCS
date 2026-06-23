package main.business.dto;

import java.math.BigDecimal;

public class SelfAssessmentDTO {

    private final int selfAssessmentId;
    private final int assignmentId;
    private final String deliverableName;
    private final byte[] file;
    private final BigDecimal score;
    private final String comments;
    private final String status; // Added because all deliverables have status

    private SelfAssessmentDTO(SelfAssessmentBuilder builder) {
        this.selfAssessmentId = builder.selfAssessmentId;
        this.assignmentId = builder.assignmentId;
        this.deliverableName = builder.deliverableName;
        this.file = builder.file;
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

    public byte[] getFile() {
        return file;
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

    public static class SelfAssessmentBuilder {

        private int selfAssessmentId;
        private int assignmentId;
        private String deliverableName;
        private byte[] file;
        private BigDecimal score;
        private String comments;
        private String status;

        public SelfAssessmentBuilder setSelfAssessmentId(int selfAssessmentId) {
            this.selfAssessmentId = selfAssessmentId;
            return this;
        }

        public SelfAssessmentBuilder setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
            return this;
        }

        public SelfAssessmentBuilder setDeliverableName(String deliverableName) {
            this.deliverableName = deliverableName;
            return this;
        }

        public SelfAssessmentBuilder setFile(byte[] file) {
            this.file = file;
            return this;
        }

        public SelfAssessmentBuilder setScore(BigDecimal score) {
            this.score = score;
            return this;
        }

        public SelfAssessmentBuilder setComments(String comments) {
            this.comments = comments;
            return this;
        }
        
        public SelfAssessmentBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public SelfAssessmentDTO build() {
            return new SelfAssessmentDTO(this);
        }
    }
}
