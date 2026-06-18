package main.business.dto;

import java.math.BigDecimal;

public class AutoevaluacionDTO {

    private final int selfAssessmentId;
    private final int assignmentId;
    private final BigDecimal score;
    private final String comments;

    private AutoevaluacionDTO(AutoevaluacionBuilder builder) {
        this.selfAssessmentId = builder.selfAssessmentId;
        this.assignmentId = builder.assignmentId;
        this.score = builder.score;
        this.comments = builder.comments;
    }

    public int getSelfAssessmentId() {
        return selfAssessmentId;
    }

    public int getAssignmentId() {
        return assignmentId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public boolean equals(Object instance) {
        if (this == instance) {
            return true;
        }
        if (instance == null || getClass() != instance.getClass()) {
            return false;
        }
        AutoevaluacionDTO that = (AutoevaluacionDTO) instance;
        return selfAssessmentId == that.selfAssessmentId
                && assignmentId == that.assignmentId;
    }

    public static class AutoevaluacionBuilder {

        private int selfAssessmentId;
        private int assignmentId;
        private BigDecimal score;
        private String comments;

        public AutoevaluacionBuilder setSelfAssessmentId(int selfAssessmentId) {
            this.selfAssessmentId = selfAssessmentId;
            return this;
        }

        public AutoevaluacionBuilder setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
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

        public AutoevaluacionDTO build() {
            return new AutoevaluacionDTO(this);
        }
    }
}
