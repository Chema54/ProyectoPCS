package main.business.dto;

import java.math.BigDecimal;
import java.sql.Date;

public class ReporteDTO {

    private final int monthlyReportId;
    private final int assignmentId;
    private final String deliverableName;
    private final byte[] file;
    private final String status;
    private final Date deliveryDate;
    private final Date deadline;
    private final int reportedHours;
    private final BigDecimal score;
    private final String comments;

    private ReporteDTO(ReporteBuilder builder) {
        this.monthlyReportId = builder.monthlyReportId;
        this.assignmentId = builder.assignmentId;
        this.deliverableName = builder.deliverableName;
        this.file = builder.file;
        this.status = builder.status;
        this.deliveryDate = builder.deliveryDate;
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

    public Date getDeliveryDate() {
        return deliveryDate;
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

    public static class ReporteBuilder {

        private int monthlyReportId;
        private int assignmentId;
        private String deliverableName;
        private byte[] file;
        private String status;
        private Date deliveryDate;
        private Date deadline;
        private int reportedHours;
        private BigDecimal score;
        private String comments;

        public ReporteBuilder setMonthlyReportId(int monthlyReportId) {
            this.monthlyReportId = monthlyReportId;
            return this;
        }

        public ReporteBuilder setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
            return this;
        }

        public ReporteBuilder setDeliverableName(String deliverableName) {
            this.deliverableName = deliverableName;
            return this;
        }

        public ReporteBuilder setFile(byte[] file) {
            this.file = file;
            return this;
        }

        public ReporteBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public ReporteBuilder setDeliveryDate(Date deliveryDate) {
            this.deliveryDate = deliveryDate;
            return this;
        }

        public ReporteBuilder setDeadline(Date deadline) {
            this.deadline = deadline;
            return this;
        }

        public ReporteBuilder setReportedHours(int reportedHours) {
            this.reportedHours = reportedHours;
            return this;
        }

        public ReporteBuilder setScore(BigDecimal score) {
            this.score = score;
            return this;
        }

        public ReporteBuilder setComments(String comments) {
            this.comments = comments;
            return this;
        }

        public ReporteDTO build() {
            return new ReporteDTO(this);
        }
    }
}
