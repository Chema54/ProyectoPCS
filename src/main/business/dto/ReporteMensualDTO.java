package main.business.dto;

import java.sql.Date;
import java.math.BigDecimal;

public class ReporteMensualDTO {

    private final int monthlyReportId;
    private final int assignmentId;
    private final String file;
    private final String status;
    private final Date deliveryDate;
    private final Date deadline;
    private final int reportedHours;
    private final BigDecimal score;
    private final String comments;

    private ReporteMensualDTO(ReporteMensualBuilder builder) {
        this.monthlyReportId = builder.monthlyReportId;
        this.assignmentId = builder.assignmentId;
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

    public String getFile() {
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

    @Override
    public boolean equals(Object instance) {
        if (this == instance) {
            return true;
        }
        if (instance == null || getClass() != instance.getClass()) {
            return false;
        }
        ReporteMensualDTO that = (ReporteMensualDTO) instance;
        return monthlyReportId == that.monthlyReportId
                && assignmentId == that.assignmentId;
    }

    public static class ReporteMensualBuilder {

        private int monthlyReportId;
        private int assignmentId;
        private String file;
        private String status;
        private Date deliveryDate;
        private Date deadline;
        private int reportedHours;
        private BigDecimal score;
        private String comments;

        public ReporteMensualBuilder setMonthlyReportId(int monthlyReportId) {
            this.monthlyReportId = monthlyReportId;
            return this;
        }

        public ReporteMensualBuilder setAssignmentId(int assignmentId) {
            this.assignmentId = assignmentId;
            return this;
        }

        public ReporteMensualBuilder setFile(String file) {
            this.file = file;
            return this;
        }

        public ReporteMensualBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public ReporteMensualBuilder setDeliveryDate(Date deliveryDate) {
            this.deliveryDate = deliveryDate;
            return this;
        }

        public ReporteMensualBuilder setDeadline(Date deadline) {
            this.deadline = deadline;
            return this;
        }

        public ReporteMensualBuilder setReportedHours(int reportedHours) {
            this.reportedHours = reportedHours;
            return this;
        }

        public ReporteMensualBuilder setScore(BigDecimal score) {
            this.score = score;
            return this;
        }

        public ReporteMensualBuilder setComments(String comments) {
            this.comments = comments;
            return this;
        }

        public ReporteMensualDTO build() {
            return new ReporteMensualDTO(this);
        }
    }
}
