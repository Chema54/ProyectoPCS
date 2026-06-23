package main.business.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class ReportDTO {

    private final int monthlyReportId;
    private final int assignmentId;
    private final String deliverableName;
    private final byte[] file;
    private final String status;
    private final Date deadline;
    private final int reportedHours;
    private final BigDecimal score;
    private final String comments;

    private ReportDTO(ReportBuilder builder) {
        this.monthlyReportId = builder.monthlyReportId;
        this.assignmentId = builder.assignmentId;
        this.deliverableName = builder.deliverableName;
        this.file = builder.file;
        this.status = builder.status;
        this.deadline = builder.deadline;
        this.reportedHours = builder.reportedHours;
        this.score = builder.score;
        this.comments = builder.comments;
    }

    public int getMonthlyReportId() {
        return monthlyReportId;
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

    public int getReportedHours() {
        return reportedHours;
    }

    public BigDecimal getScore() {
        return score;
    }

    public String getComments() {
        return comments;
    }

    public static class ReportBuilder {

        private int monthlyReportId;
        private int assignmentId;
        private String deliverableName;
        private byte[] file;
        private String status;
        private Date deadline;
        private int reportedHours;
        private BigDecimal score;
        private String comments;

        public ReportBuilder setMonthlyReportId(int monthlyReportId) {
            this.monthlyReportId = monthlyReportId;
            return this;
        }

        public ReportBuilder setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
            return this;
        }

        public ReportBuilder setDeliverableName(String deliverableName) {
            this.deliverableName = deliverableName;
            return this;
        }

        public ReportBuilder setFile(byte[] file) {
            this.file = file;
            return this;
        }

        public ReportBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public ReportBuilder setDeadline(Date deadline) {
            this.deadline = deadline;
            return this;
        }

        public ReportBuilder setReportedHours(int reportedHours) {
            this.reportedHours = reportedHours;
            return this;
        }

        public ReportBuilder setScore(BigDecimal score) {
            this.score = score;
            return this;
        }

        public ReportBuilder setComments(String comments) {
            this.comments = comments;
            return this;
        }

        public ReportDTO build() {
            return new ReportDTO(this);
        }
    }
}
